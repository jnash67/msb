package com.medcognize.view.dashboard;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import elemental.json.JsonException;
import elemental.json.JsonObject;
import elemental.json.impl.JsonUtil;

@JavaScript({"jquery.min.js", "highcharts.js", "highcharts_connector.js"})
public class Highcharts extends AbstractJavaScriptComponent {

	@Override
	public HighchartsState getState() {
		return (HighchartsState) super.getState();
	}

	public void setData(String jsonData) {
		try {
			JsonObject data = JsonUtil.parse(jsonData);
			getState().setData(data);
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
}