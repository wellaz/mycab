package com.equation.mycab.util.maintab;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

public class InsertTab {

	public static void insertTab(TabSheet tabs, Component component, String title, Resource icon) {
		int size = tabs.getComponentCount();
		boolean exists = false;
		for (int i = 0; i < size; i++) {
			if (tabs.getTab(i).getCaption().equals(title)) {
				tabs.setSelectedTab(i);
				exists = true;
				break;
			}
		}
		if (!exists) {
			Tab tab = tabs.addTab(component, title, icon);
			tab.setClosable(true);
			tabs.setSelectedTab(tab);
		}
	}

}
