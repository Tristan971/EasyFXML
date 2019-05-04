open module moe.tristan.easyfxml.natives.platform {

    exports moe.tristan.easyfxml.natives.platform;

    requires java.annotation;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    requires transitive oshi.core;

    requires slf4j.api;

}
