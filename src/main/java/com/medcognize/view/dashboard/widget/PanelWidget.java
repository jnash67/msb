package com.medcognize.view.dashboard.widget;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class PanelWidget extends CssLayout {

    private Button configureButton;
    private Component content;

    public PanelWidget() {
        super();
        addStyleName("layout-panel");
        setSizeFull();
    }

    public PanelWidget(Component content) {
        this();
        createContent(content);
    }

    protected void createContent(Component content) {
        this.content = content;
        this.configureButton = createConfigureButton();
        addComponent(configureButton);
        addComponent(content);
    }

    protected Button createConfigureButton() {
        Button configure = new Button();
        configure.addStyleName("configure");
        configure.addStyleName("icon-cog");
        configure.addStyleName("icon-only");
        configure.addStyleName("borderless");
        configure.setDescription("Configure");
        configure.addStyleName("small");
        configure.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //Notification.show("Not implemented yet");
            }
        });
        return configure;
    }

    @SuppressWarnings("unused")
    public Button getConfigureButton() {
        return configureButton;
    }

    public Component getContent() {
        return content;
    }

    public void refresh() {
        markAsDirty();
    }
}

