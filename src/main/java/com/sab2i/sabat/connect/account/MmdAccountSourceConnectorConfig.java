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

import com.github.jcustenborder.kafka.connect.spooldir.SpoolDirSourceConnectorConfig;
import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.errors.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;

class MmdAccountSourceConnectorConfig extends SpoolDirSourceConnectorConfig {
    private static final Logger log = LoggerFactory.getLogger(MmdAccountSchemaGenerator.class);

    private static final String MMD_CHARSET_CONF = "mmd.file.charset";
    private static final String MMD_CHARSET_DISPLAY = "File character set.";

    private static final String MMD_CHARSET_DOC = "Character set to read wth file with.";
    private static final String MMD_CHARSET_DEFAULT = Charset.defaultCharset().name();

    private static final String CSV_GROUP = "CSV Parsing";
    public final Charset charset;

    public MmdAccountSourceConnectorConfig(final boolean isTask, Map<String, ?> settings) {
        super(isTask, conf(), settings);

        String charsetName = this.getString(MmdAccountSourceConnectorConfig.MMD_CHARSET_CONF);
        this.charset = Charset.forName(charsetName);
    }

    static ConfigDef conf() {
        return SpoolDirSourceConnectorConfig.config()
                .define(
                        ConfigKeyBuilder.of(MMD_CHARSET_CONF, ConfigDef.Type.STRING)
                                .defaultValue(MMD_CHARSET_DEFAULT)
                                .validator(CharsetValidator.of())
                                .importance(ConfigDef.Importance.LOW)
                                .documentation(MMD_CHARSET_DOC)
                                .displayName(MMD_CHARSET_DISPLAY)
                                .group(CSV_GROUP)
                                .width(ConfigDef.Width.LONG)
                                .build()
                );
    }

    @Override
    public boolean schemasRequired() {
        return false;
    }

    static class CharsetValidator implements ConfigDef.Validator {
        static CharsetValidator of() {
            return new CharsetValidator();
        }

        @Override
        public void ensureValid(String s, Object o) {
            try {
                Preconditions.checkState(o instanceof String);
                String input = (String) o;
                Charset.forName(input);
            } catch (IllegalArgumentException e) {
                throw new DataException(String.format("Charset '%s' is invalid for %s", o, s), e);
            }
        }

        @Override
        public String toString() {
            return Joiner.on(",").join(Charset.availableCharsets().keySet());
        }
    }
}
