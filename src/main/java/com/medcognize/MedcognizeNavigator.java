package com.medcognize;


import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.view.DashboardViewType;
import com.medcognize.view.homepage.HomepageView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("serial")
@Slf4j
// use @SpringComponent with Vaadin not @Component ("The annotation is exactly the same as the
// regular Spring @Component, but has been given an alias, because Vaadin has a Component interface, which can cause trouble.").
@SpringComponent
public class MedcognizeNavigator extends Navigator {

    private View currentView;
    private ViewProvider errorViewProvider;

    public void initAfterSpringCreation(UI ui, ViewDisplay display) {
        init(ui, null, display);

        // This is intended to resolve the issue that we're calling our first
        // nav.NavigateTo from within the UI init method
        // this results in an erroneous second call to nav.NavigateTo. See:
        // https://vaadin.com/forum/#!/thread/3395652
        // Previously handled this with a complex counter in the custom ViewDisplay
        getStateManager().setState(HomepageView.NAME);

        initViewChangeListener();
        setErrorView(HomepageView.class);
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                currentView = event.getNewView();
                DashboardViewType view = DashboardViewType.getByViewName(event
                        .getViewName());
                // Appropriate events get fired after the view is changed.
                MedcognizeEventBus.post(new MedcognizeEvent.PostViewChangeEvent(view));
                MedcognizeEventBus.post(new MedcognizeEvent.BrowserResizeEvent());
                MedcognizeEventBus.post(new MedcognizeEvent.CloseOpenWindowsEvent());
            }
        });
    }
}