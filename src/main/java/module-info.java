open module easyfxml {
    requires java.annotation;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    requires slf4j.api;

    requires io.vavr;
    requires awaitility;

    exports moe.tristan.easyfxml;
    exports moe.tristan.easyfxml.api;
    exports moe.tristan.easyfxml.util;
    exports moe.tristan.easyfxml.model.beanmanagement;
    exports moe.tristan.easyfxml.model.components.listview;
    exports moe.tristan.easyfxml.model.exception;
    exports moe.tristan.easyfxml.model.fxml;
    exports moe.tristan.easyfxml.model.system;
}
