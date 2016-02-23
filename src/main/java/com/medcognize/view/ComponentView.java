package com.medcognize.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

public interface ComponentView extends Component, View {
	public void showHeader(boolean b);
	public void ShowFilter(boolean b);
}
