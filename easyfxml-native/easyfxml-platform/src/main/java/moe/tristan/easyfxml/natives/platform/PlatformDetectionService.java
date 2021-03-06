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

package moe.tristan.easyfxml.natives.platform;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oshi.PlatformEnum;
import oshi.SystemInfo;

@Component
public class PlatformDetectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformDetectionService.class);

    private final AtomicReference<SystemInfo> systemInfo = new AtomicReference<>();

    public PlatformEnum getPlatformType() {
        return SystemInfo.getCurrentPlatformEnum();
    }

    public Optional<PlatformEnum> lookupPlatformType() {
        return Optional.of(getPlatformType());
    }

    public SystemInfo getFullSystemInformation() {
        return getSystemInfo();
    }

    private SystemInfo getSystemInfo() {
        return systemInfo.updateAndGet(existing -> {
            if (existing != null) {
                return existing;
            }
            LOGGER.info("Lazy-loading underlying platform information.");
            return new SystemInfo();
        });
    }

}
