open module easyfxml.settings {

    exports moe.tristan.easyfxml.natives.settings;

    requires moe.tristan.easyfxml.natives.platform;

    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    requires com.fasterxml.jackson.databind;

    requires slf4j.api;
    requires com.fasterxml.jackson.core;

}
