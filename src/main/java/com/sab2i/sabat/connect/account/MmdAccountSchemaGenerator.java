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

import com.github.jcustenborder.kafka.connect.spooldir.SchemaGenerator;
import org.apache.kafka.connect.data.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class MmdAccountSchemaGenerator extends SchemaGenerator<MmdAccountSourceConnectorConfig> {
    private static final Logger log = LoggerFactory.getLogger(MmdAccountSchemaGenerator.class);

    public MmdAccountSchemaGenerator(Map<String, ?> settings) {
        super(settings);
    }

    @Override
    protected MmdAccountSourceConnectorConfig config(Map<String, ?> settings) {
        return new MmdAccountSourceConnectorConfig(false, settings);
    }

    @Override
    protected Map<String, Schema.Type> determineFieldTypes(InputStream inputStream) throws IOException {
        return new LinkedHashMap<>();
    }
}
