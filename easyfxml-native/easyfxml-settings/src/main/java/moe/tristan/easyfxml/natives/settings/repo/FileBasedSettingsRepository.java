/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.natives.settings.repo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileBasedSettingsRepository extends InMemoryBufferedSettingsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedSettingsRepository.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File underlyingFile;

    public FileBasedSettingsRepository(String underlyingFilePath) {
        underlyingFile = checkAndSetUnderlyingFile(underlyingFilePath);
    }

    @Override
    Map<String, Object> loadSettings() {
        synchronized (underlyingFile) {
            try {
                if (!underlyingFile.exists()) {
                    final boolean newFile = underlyingFile.createNewFile();
                    LOGGER.info(
                        "No pre-existing underlying settings file [{}]. Created one: {}",
                        underlyingFile.getAbsolutePath(),
                        newFile ? "yes" : "no"
                    );
                }
                return objectMapper.readValue(underlyingFile, objectMapper.getTypeFactory().constructMapType(
                    HashMap.class,
                    String.class,
                    Object.class
                ));
            } catch (IOException e) {
                throw new RuntimeException(
                    "Cannot initialize settings with "
                    + "underlying file [" + underlyingFile.getAbsolutePath() + "] !",
                    e
                );
            }
        }
    }

    void saveSettings(Map<String, Object> inMemorySettings) {
        synchronized (underlyingFile) {
            try {
                LOGGER.info("Updating settings store at {} with data from in-memory settings.", underlyingFile);
                objectMapper.writeValue(underlyingFile, inMemorySettings);
            } catch (JsonGenerationException e) {
                throw new RuntimeException("Cannot encode in-memory settings cache as Json properly!", e);
            } catch (JsonMappingException e) {
                throw new RuntimeException("Cannot map settings cache object to Json properly!", e);
            } catch (IOException e) {
                throw new RuntimeException("Error writing json-serialized cache object to underlying file [" + underlyingFile.getAbsolutePath() + "]", e);
            }
        }
    }

    private static File checkAndSetUnderlyingFile(String underlyingFilePath) {
        File underlyingFile = new File(underlyingFilePath);
        try {
            //noinspection ResultOfMethodCallIgnored
            underlyingFile.toPath();
        } catch (Exception e) {
            LOGGER.error("Cannot map given path to a file properly. Was [{}].", underlyingFilePath, e);
        }
        return underlyingFile;
    }

}
