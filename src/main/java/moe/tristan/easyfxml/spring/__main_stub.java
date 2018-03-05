package moe.tristan.easyfxml.spring;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stub class for the packaging the library as a nice and reusable jar file. It requires a main class so we provide it
 * with this. Feels like the C days huh.
 */
@SpringBootApplication
public class __main_stub {

    private static final Logger LOG = LoggerFactory.getLogger(__main_stub.class);

    public static void main(final String... args) {
        final SpringApplicationBuilder sab = new SpringApplicationBuilder(__main_stub.class)
            .headless(false)
            .web(WebApplicationType.NONE);

        final ConfigurableApplicationContext ctx = sab.run(args);
        LOG.info("STUB CLASS. USELESS EXCEPT FOR A FEW CONFIG THINGS.");
        ctx.close();
        throw new IllegalStateException("NOT TO BE EXECUTED");
    }
}
