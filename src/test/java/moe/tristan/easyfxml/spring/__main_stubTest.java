package moe.tristan.easyfxml.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringContext.class)
public class __main_stubTest {

    @Test(expected = RuntimeException.class)
    public void main() {
        __main_stub.main();
    }
}
