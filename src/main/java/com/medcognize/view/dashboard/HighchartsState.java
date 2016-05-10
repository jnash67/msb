package com.medcognize.view.dashboard;

import com.vaadin.shared.ui.JavaScriptComponentState;
import elemental.json.JsonObject;

// see: https://vaadin.com/forum/#!/thread/11441026/11441025
public class HighchartsState extends JavaScriptComponentState {
    private JsonObject data;
    public JsonObject getData() {
        return data;
    }
    public void setData(JsonObject data) {
        this.data = data;
    }
}