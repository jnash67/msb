package com.medcognize.view.dashboard.widget;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Iterator;

public class ContentWidget extends CssLayout {

    private final CssLayout dashboardPanels;
    private final VerticalLayout root;
    private Component widgetContent;

    public ContentWidget(final VerticalLayout root, final CssLayout dashboardPanels) {
        super();
        this.root = root;
        this.dashboardPanels = dashboardPanels;
    }

    public void setContent(final Component content) {
        this.widgetContent = content;
        this.setWidth("100%");
        this.addStyleName("dashboard-panel-slot");

        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("dashboard-panel-toolbar");
        toolbar.setWidth("100%");

        Label caption = new Label(content.getCaption());
        caption.addStyleName(ValoTheme.LABEL_H4);
        caption.addStyleName(ValoTheme.LABEL_COLORED);
        caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        content.setCaption(null);

        MenuBar tools = new MenuBar();
        tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
        MenuBar.MenuItem max = tools.addItem("", FontAwesome.EXPAND, new MenuBar.Command() {

            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                if (!ContentWidget.this.getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
                    toggleMaximized(ContentWidget.this, true);
                } else {
                    ContentWidget.this.removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
                    toggleMaximized(ContentWidget.this, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuBar.MenuItem cog = tools.addItem("", FontAwesome.COG, null);
        cog.addItem("Configure", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        cog.addSeparator();
        cog.addItem("Close", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);

        card.addComponents(toolbar, content);
        this.addComponent(card);
    }

    public Component getContent() {
        return this.widgetContent;
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        for (Iterator<Component> it = root.iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        dashboardPanels.setVisible(true);

        for (Iterator<Component> it = dashboardPanels.iterator(); it.hasNext();) {
            Component c = it.next();
            c.setVisible(!maximized);
        }

        if (maximized) {
            panel.setVisible(true);
            panel.addStyleName("max");
        } else {
            panel.removeStyleName("max");
        }
    }
}

