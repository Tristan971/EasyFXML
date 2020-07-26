# EasyFXML - JUnit
A collection of tools to make _JavaFX_ testing easier and safer

[![Maven Central](https://img.shields.io/maven-central/v/moe.tristan/easyfxml-junit.svg?style=for-the-badge)](https://search.maven.org/artifact/moe.tristan/easyfxml-junit)

## Features

- Full asynchronous testing, no need to manually synchronize
- Includes a DSL-like set of functions to have clear intent-centered test code
- Fully Spring Boot aware, so you get all these test-related benefits for free

## Philosophy

When writing _JavaFX_ tests, some of the harder issues to deal with include not getting
drowned in boilerplate just for preparing the environment.
Using [TestFX](https://github.com/TestFX/TestFX), this can be partly taken care of, but
still leaves many things to account for.

Another larger issue is that testing naturally asynchronous things proves difficult
in many cases as we do expect tests to properly finish in a reasonable amount of time.
For that matter, [Awaitility](https://github.com/awaitility/awaitility) can be leveraged
and make it mostly painless.

This module's goals are thus to reconcile both of these aspects and offer a sleek combined
experience that makes it properly pleasant to write integration tests.

## Getting started

This section is mostly an example of testing for the  [Hello World](../easyfxml-samples/easyfxml-sample-hello-world) 
sample. You can go check for more complex examples in the [samples module](../easyfxml-samples). All samples
are tested and for the most part using these features.

So, assuming a very minimal greeter window, let's test it:

![Hello World Sample Screenshot](../easyfxml-samples/easyfxml-sample-hello-world/doc/sample-hello-world.png)

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class HelloComponentTest extends FxNodeTest {

    @Autowired
    private EasyFxml easyFxml;

    @Autowired
    private HelloComponent helloComponent;

    private Pane helloPane;

    @BeforeEach
    public void setUp() {
        // for each test ran, we reload a brand new pane on which to apply testing
        // allowing proper test independency
        this.helloPane = easyFxml.loadNode(helloComponent).getNodeOrExceptionPane();
    }

    @Test
    public void shouldGreetWithUserEnteredName() {
        final String expectedUserName = "Tristan Deloche";

        withNodes(helloPane) // will wait for these nodes to be fully loaded up by JavaFX
            .willDo(         // then execute all of these actions one by one, and await for them to be done
                () -> clickOn("#userNameTextField").write(expectedUserName),
                () -> clickOn("#helloButton")
            ).andAwaitFor(   // and finally, we await for asynchronously triggered actions to be done before proceeding
                () -> lookup("#greetingBox").queryAs(HBox.class).isVisible()
            );

        // when arriving here, we have the following guarantees:
        // 1 the greeter pane is fully loaded
        // 2 expectedUserName has been writted in the user name TextField
        // 3 the greeting button has been clicked
        // 4 (potentially asynchronous) handling of the click action has been fully handled 

        assertThat(lookup("#greetingName").queryAs(Label.class)).hasText(expectedUserName);
    }
}
```
