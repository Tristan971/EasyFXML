/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package moe.tristan.easyfxml.model.components.listview;

import static org.awaitility.Awaitility.await;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

import moe.tristan.easyfxml.api.FxmlController;

public abstract class ComponentListViewFxmlController<T> implements FxmlController {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentListViewFxmlController.class);

    private static final String SCROLL_BAR_SELECTOR = ".scroll-bar";
    private static final double SCROLL_BAR_END_VALUE = 1.;

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
     * You should not need to care about this method. In the unlikely case where you actually want all the cells of your ListView to actually be the same one,
     * it is left protected and not private so you can override it and avoid the warnings being spouted at you.
     * <p>
     * Else, the idea is that a set of cells is created and they share the elements one after another during scrolling and JavaFX might generate more of them
     * later so we should just not make any assumption and expect all of them to be different and not singletons.
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
            if (BADLY_SCOPED_BEANS.isEmpty()) {
                return;
            }

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
        listenToScroll();
    }

    private void setCustomCellFactory() {
        listView.setCellFactory(list -> cellSupplier.get());
    }

    protected void onScrolledToEndOfListView() {
        // nothing there by default
    }

    private void listenToScroll() {
        CompletableFuture
            .runAsync(this::awaitScrollBarLoaded)
            .thenRunAsync(this::listenToScrollBarValue, Platform::runLater);
    }

    private void awaitScrollBarLoaded() {
        await()
            .atMost(5, TimeUnit.SECONDS)
            .until(() -> {
                LOG.debug("Looking for scrollbar !");
                return listView.lookup(SCROLL_BAR_SELECTOR) != null;
            });
    }

    private void listenToScrollBarValue() {
        LOG.debug("Found scrollbar !");
        ScrollBar bar = (ScrollBar) listView.lookup(SCROLL_BAR_SELECTOR);
        bar.valueProperty().addListener((src, ov, nv) -> {
            LOG.trace("Scrolled to position : {}", nv);
            if (nv.doubleValue() == SCROLL_BAR_END_VALUE) {
                LOG.debug("Scrolled to the end of the list!");
                onScrolledToEndOfListView();
            }
        });
    }

}
