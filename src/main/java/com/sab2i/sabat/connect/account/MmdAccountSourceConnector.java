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
import com.github.jcustenborder.kafka.connect.spooldir.SpoolDirSourceConnector;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;

import java.util.LinkedHashMap;
import java.util.Map;

public class MmdAccountSourceConnector extends SpoolDirSourceConnector<MmdAccountSourceConnectorConfig> {
    @Override
    protected MmdAccountSourceConnectorConfig config(Map<String, String> settings) {
        return new MmdAccountSourceConnectorConfig(false, settings);
    }

    @Override
    protected SchemaGenerator<MmdAccountSourceConnectorConfig> generator(Map<String, String> settings) {
        throw new NotImplementedException("SchemaGenerator is not implemented for MMD");
    }

    @Override
    public Class<? extends Task> taskClass() {
        return MmdAccountSourceTask.class;
    }

    @Override
    public ConfigDef config() {
        return MmdAccountSourceConnectorConfig.conf();
    }

    @Override
    public void start(Map<String, String> input) {
        this.settings = new LinkedHashMap<>(input);
    }
}
