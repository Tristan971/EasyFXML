package moe.tristan.easyfxml.model.beanmanagement;

import moe.tristan.easyfxml.model.FxmlStylesheet;
import moe.tristan.easyfxml.spring.SpringContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
        testCache = new TestStylesheetCache();
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
        testCache.tryLoadContent(() -> "fxml/test_style.css");
    }
}
