package moe.tristan.easyfxml;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.PropertySource;

import javafx.application.Application;
import javafx.application.HostServices;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.model.beanmanagement.AbstractInstanceManager;
import moe.tristan.easyfxml.model.components.listview.ComponentListCell;

@ComponentScan(
    basePackages = "moe.tristan.easyfxml",
    lazyInit = true,
    includeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = {
        EasyFxml.class,
        FxApplication.class,
        FxUiManager.class,
        FxmlController.class,
        AbstractInstanceManager.class,
        ComponentListCell.class
    })
)
@PropertySource("classpath:easyfxml.properties")
public class EasyFxmlAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyFxmlAutoConfiguration.class);

    private final ApplicationContext context;

    @Autowired
    public EasyFxmlAutoConfiguration(ApplicationContext context) {
        LOGGER.info("Loading EasyFXML auto-configuration...");
        this.context = context;
    }

    @PostConstruct
    public void logFoundControllers() {
        final String fxmlControllersFound = String.join("\n->\t", context.getBeanNamesForType(FxmlController.class));
        LOGGER.debug("\nFound the following FxmlControllers : \n->\t{}", fxmlControllersFound);
    }

    @Bean
    public HostServices hostServices(final Application application) {
        return application.getHostServices();
    }

}
