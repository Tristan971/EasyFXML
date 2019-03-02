package moe.tristan.easyfxml.samples.helloworld;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import moe.tristan.easyfxml.EasyFxml;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HelloWorldContextTest {

    @Autowired
    private EasyFxml easyFxml;

    @Test
    public void contextLoadsAndInjectsEasyFXML() {
        assertThat(easyFxml).isNotNull();
    }

}

