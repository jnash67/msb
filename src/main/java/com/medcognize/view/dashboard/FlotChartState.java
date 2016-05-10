package com.medcognize.view.dashboard;

import elemental.json.*;
import com.vaadin.shared.ui.JavaScriptComponentState;

// see: https://vaadin.com/forum/#!/thread/11441026/11441025
public class FlotChartState extends JavaScriptComponentState {

    private JsonArray data;
    private JsonObject options;

    public JsonArray getData() {
        return data;
    }

    public void setData(JsonArray data) {
        this.data = data;
    }

    public JsonObject getOptions() {
        return options;
    }

    public void setOptions(JsonObject options) {
        this.options = options;
    }
}