package moe.tristan.easyfxml.model.components.listview.cell;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import moe.tristan.easyfxml.EasyFxml;
import moe.tristan.easyfxml.model.components.listview.ComponentListCell;
import moe.tristan.easyfxml.model.components.listview.CustomListViewTestComponents;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComponentListCellSample extends ComponentListCell<String> {

    public ComponentListCellSample(EasyFxml easyFxml) {
        super(easyFxml, CustomListViewTestComponents.CELL);
    }

}
