package moe.tristan.easyfxml.samples.helloworld;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import moe.tristan.easyfxml.EasyFxmlAutoConfiguration;
import moe.tristan.easyfxml.FxApplication;

@SpringBootApplication
@Import(EasyFxmlAutoConfiguration.class)
public class HelloWorld extends FxApplication {

    public static void main(String[] args) {
        launch(args);
    }

}
