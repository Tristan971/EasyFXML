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

package moe.tristan.easyfxml;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import javafx.application.Application;
import javafx.application.HostServices;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlComponent;
import moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager;
import moe.tristan.easyfxml.model.components.listview.ComponentListCell;

@ComponentScan(
    lazyInit = true,
    includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = {
        EasyFxml.class,
        FxmlComponent.class,
        FxApplication.class,
        FxUiManager.class,
        FxmlController.class,
        AbstractInstanceManager.class,
        ComponentListCell.class
    })
)
@EnableAutoConfiguration
@EnableConfigurationProperties(EasyFxmlProperties.class)
public class EasyFxmlAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyFxmlAutoConfiguration.class);

    private final ApplicationContext context;

    @Autowired
    public EasyFxmlAutoConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void logFoundControllers() {
        final String fxmlControllersFound = String.join("\n->\t", context.getBeanNamesForType(FxmlController.class));
        LOGGER.debug("\nFound the following FxmlControllers : \n->\t{}", fxmlControllersFound);
        LOGGER.info("EasyFXML is now configured for: {}", context.getApplicationName());
    }

    @Bean
    public HostServices hostServices(final Application application) {
        return application.getHostServices();
    }

}
