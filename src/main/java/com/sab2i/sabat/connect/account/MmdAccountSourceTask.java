/**
 * Copyright © 2016 Jeremy Custenborder (jcustenborder@gmail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sab2i.sabat.connect.account;

import com.github.jcustenborder.kafka.connect.spooldir.SpoolDirSourceTask;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MmdAccountSourceTask extends SpoolDirSourceTask<MmdAccountSourceConnectorConfig> {
    static final Logger log = LoggerFactory.getLogger(SpoolDirSourceTask.class);

    private LineNumberReader reader;
    private Map<String, String> fileMetadata;

    @Override
    protected MmdAccountSourceConnectorConfig config(Map<String, ?> settings) {
        return new MmdAccountSourceConnectorConfig(true, settings);
    }

    @Override
    protected void configure(InputStream inputStream, Map<String, String> metadata, final Long lastOffset) throws IOException {
        log.trace("configure() - creating csvParser");
        this.reader = new LineNumberReader(new InputStreamReader(inputStream, this.config.charset));
        this.fileMetadata = metadata;
    }

    @Override
    public void start(Map<String, String> settings) {
        super.start(settings);
    }

    @Override
    public long recordOffset() {
        return this.reader.getLineNumber();
    }

    @Override
    public List<SourceRecord> process() throws IOException {
        List<SourceRecord> records = new ArrayList<>(this.config.batchSize);

        while (records.size() < this.config.batchSize) {
            String row = this.reader.readLine();

            if (row == null) {
                break;
            }

            log.trace("process() - Row on line {}", recordOffset());

            Schema keySchema = SchemaBuilder.struct().name("sabat.account.key")
                    .field("iban", Schema.STRING_SCHEMA)
                    .build();

            Schema valueSchema = SchemaBuilder.struct().name("sabat.account.value")
                    .field("firstname", Schema.STRING_SCHEMA)
                    .field("lastname", Schema.STRING_SCHEMA)
                    .build();

            Struct keyStruct = new Struct(keySchema);
            Struct valueStruct = new Struct(valueSchema);

            try {
                String[] split = row.split("\\|");

                // Key
                keyStruct.put("iban", split[0]);

                // Value
                valueStruct.put("firstname", split[1]);
                valueStruct.put("lastname", split[2]);

                log.trace("process() - key='{}', values='{}', '{}'", split[0], split[1], split[2]);

                addRecord(records, keyStruct, valueStruct);

            } catch (Exception ex) {
                String message = String.format("Exception thrown while parsing data for linenumber=%s", this.recordOffset());
                throw new DataException(message, ex);
            }
        }
        return records;
    }
}
