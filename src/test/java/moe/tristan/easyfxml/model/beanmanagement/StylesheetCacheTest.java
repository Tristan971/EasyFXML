package moe.tristan.easyfxml.model.beanmanagement;

import io.vavr.control.Try;
import moe.tristan.easyfxml.model.FxmlStylesheet;
import moe.tristan.easyfxml.spring.SpringContext;
import moe.tristan.easyfxml.util.PathUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static io.vavr.API.unchecked;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ContextConfiguration(classes = SpringContext.class)
@RunWith(SpringRunner.class)
public class StylesheetCacheTest {

    private static final class TestStylesheetCache extends StylesheetCache {

        public TestStylesheetCache() {
            super();
        }

        public Map<FxmlStylesheet, String> getCacheMap() {
            return this.cacheMap;
        }
    }
    private TestStylesheetCache testCache;

    @Before
    public void setUp() {
        this.testCache = new TestStylesheetCache();
    }

    @Test
    public void getInstance() {
        assertThat(StylesheetCache.getInstance()).isNotNull();
        final StylesheetCache ref1 = StylesheetCache.getInstance();
        final StylesheetCache ref2 = StylesheetCache.getInstance();
        assertThat(ref1).isEqualTo(ref2);
    }

    @Test
    public void try_load_success() {
        final FxmlStylesheet stylesheet = () -> "fxml/test_style.css";
        final Try<String> loadedStyle = this.testCache.tryLoadContent(stylesheet);
        final Try<String> expectedStyle = PathUtils.getPathForResource("fxml/test_style.css")
            .map(unchecked(Files::readAllBytes))
            .map(String::new);

        assertThat(loadedStyle.isSuccess()).isTrue();
        assertThat(expectedStyle.isSuccess()).isTrue();
        assertThat(loadedStyle.get()).isEqualToIgnoringWhitespace(expectedStyle.get());
    }

    @Test
    public void reads_only_once() throws IOException {
        final String FILE_NAME = "test_style.css";

        final Try<String> cssFile = PathUtils.getPathForResource("fxml/test_style.css")
            .map(unchecked(Files::readAllBytes))
            .map(String::new);

        assertThat(cssFile.isSuccess()).isTrue();

        final Path testPath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(FILE_NAME);
        Files.deleteIfExists(testPath);

        cssFile.peek(cssContent ->  {
            try {
                Files.write(testPath, cssContent.getBytes(UTF_8), CREATE);
            } catch (final IOException e) {
                fail("Couldn't write original css file", e);
            }
        });

        final FxmlStylesheet stylesheet = () -> testPath.toAbsolutePath().toString();
        final Try<String> stylesheetContentOriginal = stylesheet.getContentOfSheet();

        cssFile.peek(cssContent -> {
            final Optional<String> reduce = cssContent.chars()
                .mapToObj(character -> (char) (character + 1))
                .map(character -> "" + character)
                .reduce((s1, s2) -> s1 + s2);
            assertThat(reduce.isPresent()).isTrue();
            reduce.ifPresent(shifted -> {
                try {
                    Files.delete(testPath);
                    Files.write(testPath, shifted.getBytes(UTF_8), CREATE);
                } catch (final IOException e) {
                    fail("IO Error", e);
                }
            });
        });

        final Try<String> stylesheetContentCached = stylesheet.getContentOfSheet();

        assertThat(stylesheetContentOriginal.get()).isEqualToIgnoringWhitespace(stylesheetContentCached.get());
    }
}
