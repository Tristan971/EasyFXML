package moe.tristan.easyfxml.model.components.listview.cell;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.components.listview.ComponentListCell;
import moe.tristan.easyfxml.model.components.listview.CustomListViewTestComponents;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComponentListCellSample extends ComponentListCell<String> {

    public ComponentListCellSample(EasyFxml easyFxml) {
        super(easyFxml, CustomListViewTestComponents.CELL);
    }

}
