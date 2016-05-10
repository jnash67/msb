package com.medcognize.view.dashboard;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import elemental.json.JsonArray;
import elemental.json.JsonException;
import elemental.json.JsonObject;
import elemental.json.impl.JsonUtil;

@JavaScript({"jquery.min.js", "jquery.flot.js", "flot_connector.js"})
public class FlotChart extends AbstractJavaScriptComponent {

	public FlotChart(String caption, String data, String options) {
		setCaption(caption);
		setData(data);
		setOptions(options);
	}

	@Override
	public FlotChartState getState() {
		return (FlotChartState) super.getState();
	}

	public void setData(String source) {
		JsonArray data;
		try {
			data = JsonUtil.parse(source);
			getState().setData(data);
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}

	public void setOptions(String options) {
		try {
			JsonObject root = JsonUtil.parse(options);
			getState().setOptions(root);
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
}