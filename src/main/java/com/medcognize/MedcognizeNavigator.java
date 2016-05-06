package com.medcognize;


import com.medcognize.event.MedcognizeEvent;
import com.medcognize.event.MedcognizeEventBus;
import com.medcognize.view.MedcognizeViewType;
import com.medcognize.view.homepage.HomepageView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Slf4j
@Component
public class MedcognizeNavigator extends Navigator {

    private View currentView;
    private ViewProvider errorViewProvider;

    public void initAfterSpringCreation(UI ui, ViewDisplay display) {
        init(ui, null, display);

        // This is intended to resolve the issue that we're calling our first
        // nav.NavigateTo from within the UI init method
        // this results in an erroneous second call to nav.NavigateTo. See:
        // https://vaadin.com/forum/#!/thread/3395652
        getStateManager().setState(HomepageView.NAME);

        initViewChangeListener();
        // add the views
        for (final MedcognizeViewType viewType : MedcognizeViewType.values()) {
            addView(viewType.getViewName(), viewType.getViewClass());
        }
        setErrorView(HomepageView.class);
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // boolean l = isLoggedIn();
//                View oldView = event.getOldView();
//                View newView = event.getNewView();
//                if (null == newView) {
//                    log.error("newView should never be null");
//                    return false;
//                }
//                System.out.println("count is " + count + "    ignoreNext is " + ignoreNext);
//                System.out.println("Navigating from -" + oldView +
//                        "- to -" + newView + "-");
//                count++;
//                if ((null == oldView) && (newView.getClass().isAssignableFrom(HomepageView.class))) {
//                    ignoreNext = true;
//                }
//                if (ignoreNext) {
//                    if (null != oldView) {
//                        if (oldView.getClass().isAssignableFrom(HomepageView.class) && newView.getClass()
//                                .isAssignableFrom(HomepageView.class)) {
//                            // this is the erroneous second call we're screening out
//                            return false;
//                        }
//                    }
//                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                currentView = event.getNewView();
                MedcognizeViewType view = MedcognizeViewType.getByViewName(event
                        .getViewName());
                // Appropriate events get fired after the view is changed.
                MedcognizeEventBus.post(new MedcognizeEvent.PostViewChangeEvent(view));
                MedcognizeEventBus.post(new MedcognizeEvent.BrowserResizeEvent());
                MedcognizeEventBus.post(new MedcognizeEvent.CloseOpenWindowsEvent());
            }
        });
    }
}