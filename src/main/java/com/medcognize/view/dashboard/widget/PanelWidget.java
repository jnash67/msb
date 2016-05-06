package com.medcognize.view.dashboard.widget;

import com.medcognize.view.dashboard.DashboardView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Iterator;

public class PanelWidget extends CssLayout {

    private Component content;
    private DashboardView view;

    public PanelWidget(DashboardView inView) {
        super();
        this.view = inView;
        setWidth("100%");
        addStyleName("dashboard-panel-slot");
    }

    public PanelWidget(Component content, DashboardView inView) {
        this(inView);
        createContent(content);
    }

    protected void createContent(Component content) {
        this.content = content;
        CssLayout card = new CssLayout();
        card.setWidth("100%");
        card.addStyleName(ValoTheme.LAYOUT_CARD);

        HorizontalLayout toolbar = createToolbar();
        card.addComponents(toolbar, content);
        addComponent(card);
    }

    private void toggleMaximized(final Component panel, final boolean maximized) {
        if (null == view) {
            return;
        }
        for (Iterator<Component> it = view.getRoot().iterator(); it.hasNext();) {
            it.next().setVisible(!maximized);
        }
        view.getWidgetsLayout().setVisible(true);

        for (Iterator<Component> it = view.getWidgetsLayout().iterator(); it.hasNext();) {
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

    protected HorizontalLayout createToolbar() {
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
                if (!getStyleName().contains("max")) {
                    selectedItem.setIcon(FontAwesome.COMPRESS);
                    toggleMaximized(PanelWidget.this, true);
                } else {
                    removeStyleName("max");
                    selectedItem.setIcon(FontAwesome.EXPAND);
                    toggleMaximized(PanelWidget.this, false);
                }
            }
        });
        max.setStyleName("icon-only");
        MenuBar.MenuItem root = tools.addItem("", FontAwesome.COG, null);
        root.addItem("Configure", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });
        root.addSeparator();
        root.addItem("Close", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Not implemented in this demo");
            }
        });

        toolbar.addComponents(caption, tools);
        toolbar.setExpandRatio(caption, 1);
        toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
        return toolbar;
    }

    public Component getContent() {
        return content;
    }

    public void refresh() {
        markAsDirty();
    }
}

