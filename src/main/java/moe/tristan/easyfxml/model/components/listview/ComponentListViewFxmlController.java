package moe.tristan.easyfxml.model.components.listview;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import moe.tristan.easyfxml.api.FxmlController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ComponentListViewFxmlController<T> implements FxmlController {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentListViewFxmlController.class);

    static final AtomicBoolean HAS_CHECKED_BEAN_DEFINITIONS = new AtomicBoolean(false);
    static final Set<String> BADLY_SCOPED_BEANS = new HashSet<>();

    @FXML
    protected ListView<T> listView;

    protected final ConfigurableApplicationContext applicationContext;
    protected final Supplier<ComponentListCell<T>> cellSupplier;

    public ComponentListViewFxmlController(
            ApplicationContext applicationContext,
            Class<? extends ComponentListCell<T>> customCellClass
    ) {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        this.cellSupplier = () -> applicationContext.getBean(customCellClass);
        if (!HAS_CHECKED_BEAN_DEFINITIONS.get()) {
            findBadlyScopedComponents();
        }
    }

    /**
     * You should not need to care about this method. In the unlikely case where you actually want all the cells of your
     * ListView to actually be the same one, it is left protected and not private so you can override it and avoid the
     * warnings being spouted at you.
     * <p>
     * Else, the idea is that a set of cells is created and they share the elements one after another during scrolling
     * and JavaFX might generate more of them later so we should just not make any assumption and expect all of them to
     * be different and not singletons.
     */
    protected void findBadlyScopedComponents() {
        synchronized (HAS_CHECKED_BEAN_DEFINITIONS) {
            if (HAS_CHECKED_BEAN_DEFINITIONS.get()) {
                return;
            }

            final ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
            Stream.of(ComponentCellFxmlController.class, ComponentListCell.class)
                  .map(beanFactory::getBeanNamesForType)
                  .flatMap(Arrays::stream)
                  .filter(beanName -> {
                      final String effectiveScope = beanFactory.getBeanDefinition(beanName).getScope();
                      return !ConfigurableBeanFactory.SCOPE_PROTOTYPE.equals(effectiveScope);
                  }).forEach(BADLY_SCOPED_BEANS::add);

            HAS_CHECKED_BEAN_DEFINITIONS.set(true);
            final String faulties = String.join(",", BADLY_SCOPED_BEANS);
            LOG.warn(
                    "Custom ListView cells wrappers and controllers " +
                    "should be prototype-scoped bean. " +
                    "See @Scope annotation.\n" +
                    "Faulty beans were : [{}]",
                    faulties
            );
        }
    }

    @Override
    public void initialize() {
        setCustomCellFactory();
    }

    private void setCustomCellFactory() {
        listView.setCellFactory(list -> cellSupplier.get());
    }

}
