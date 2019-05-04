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

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moe.tristan.easyfxml.natives.settings.SettingsRepository;

public abstract class InMemoryBufferedSettingsRepository implements SettingsRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBufferedSettingsRepository.class);

    private final Map<String, Object> inMemoryCache = supplyAsync(this::loadSettings).join();

    abstract Map<String, Object> loadSettings();

    abstract void saveSettings(Map<String, Object> inMemorySettings);

    @Override
    public <T> Optional<T> findSetting(String settingName, Class<? extends T> valueType) {
        return Optional.of(inMemoryCache).map(cache -> cache.get(settingName)).map(valueType::cast);
    }

    @Override
    public <T> void saveSetting(String settingName, T value) {
        inMemoryCache.put(settingName, value);

        LOGGER.debug("Queued saving settings for {}", this);
        runAsync(
            () -> saveSettings(inMemoryCache)
        ).thenAcceptAsync(
            __ -> LOGGER.debug("Saved settings for {} successfully.", this)
        );
    }

}
