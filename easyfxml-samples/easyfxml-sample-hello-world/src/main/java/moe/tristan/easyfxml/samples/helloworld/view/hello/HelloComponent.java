package moe.tristan.easyfxml.samples.helloworld.view.hello;

import org.springframework.stereotype.Component;

import moe.tristan.easyfxml.api.FxmlController;
import moe.tristan.easyfxml.api.FxmlFile;
import moe.tristan.easyfxml.api.FxmlNode;

@Component
public class HelloComponent implements FxmlNode {

    @Override
    public FxmlFile getFile() {
        return () -> "moe/tristan/easyfxml/samples/helloworld/view/hello/HelloView.fxml";
    }

    @Override
    public Class<? extends FxmlController> getControllerClass() {
        return HelloController.class;
    }

}
