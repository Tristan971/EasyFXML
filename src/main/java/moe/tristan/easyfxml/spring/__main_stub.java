package moe.tristan.easyfxml.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Stub class for the packaging the library as a nice and reusable jar file.
 * It requires a main class so we provide it with this. Feels like the C days huh.
 */
@SpringBootApplication
public class __main_stub {
    private static final Logger LOG = LoggerFactory.getLogger(__main_stub.class);

    public static void main(final String... args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(__main_stub.class)) {
            LOG.info("STUB CLASS. USELESS EXCEPT FOR A FEW CONFIG THINS.");
            throw new RuntimeException("NOT TO BE EXECUTED");
        }
    }
}
