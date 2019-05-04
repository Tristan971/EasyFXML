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

package moe.tristan.easyfxml.natives.settings;

import java.util.Objects;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import moe.tristan.easyfxml.natives.platform.PlatformDetectionService;
import moe.tristan.easyfxml.natives.settings.platforms.windows.WindowsSystemSettingsRepository;
import moe.tristan.easyfxml.natives.settings.platforms.windows.WindowsUserSettingsRepository;

import oshi.PlatformEnum;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class EasyFxmlNativeSettingsAutoconfiguration {

    private final PlatformDetectionService platformDetectionService;

    public EasyFxmlNativeSettingsAutoconfiguration(PlatformDetectionService platformDetectionService) {
        this.platformDetectionService = platformDetectionService;
    }

    @Bean
    public SettingsService settingsService() {
        final PlatformEnum platformType = platformDetectionService.getPlatformType();

        SettingsRepository userRepo = null;
        SettingsRepository systemRepo = null;

        switch (platformType) {
            case WINDOWS:
                userRepo = new WindowsUserSettingsRepository();
                systemRepo = new WindowsSystemSettingsRepository();
            case MACOSX:
                break;
            case LINUX:
            case FREEBSD:
                break;
            case SOLARIS:
            case UNKNOWN:
                break;
        }

        final SettingsService settingsService = new SettingsService(userRepo, systemRepo);
        return Objects.requireNonNull(
            settingsService,
            "Could not build the appropriate settings management service for your platform: " + platformType
        );
    }

}
