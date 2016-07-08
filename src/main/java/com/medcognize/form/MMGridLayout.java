package com.medcognize.form;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import org.vaadin.viritin.MSize;

import java.util.Collection;

/**
 * Until Viritin 1.55 is released with the MGridLayout with the column and row number
 * constructor, use this one.
 */
public class MMGridLayout extends GridLayout {

    public MMGridLayout() {
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MMGridLayout(int columns, int rows) {
        super(columns, rows);
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MMGridLayout(int columns, int rows, Component... children) {
        super(columns, rows, children);
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MMGridLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MMGridLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MMGridLayout withSpacing(boolean spacing) {
        setSpacing(spacing);
        return this;
    }

    public MMGridLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MMGridLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
        return this;
    }

    public MMGridLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MMGridLayout withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MMGridLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MMGridLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MMGridLayout withHeight(float height, Unit unit) {
        setHeight(height, unit);
        return this;
    }

    public MMGridLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MMGridLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MMGridLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    public MMGridLayout add(Component... component) {
        return with(component);
    }

    public MMGridLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MMGridLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MMGridLayout withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        setCaptionAsHtml(captionAsHtml);
        return this;
    }

    public MMGridLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MMGridLayout withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MMGridLayout withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MMGridLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MMGridLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MMGridLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    public MMGridLayout withResponsive(boolean responsive) {
        setResponsive(responsive);
        return this;
    }

    public MMGridLayout withDefaultComponentAlignment(Alignment defaultAlignment) {
        setDefaultComponentAlignment(defaultAlignment);
        return this;
    }

    public MMGridLayout withId(String id) {
        setId(id);
        return this;
    }

    public MMGridLayout withDescription(String description) {
        setDescription(description);
        return this;
    }
}
