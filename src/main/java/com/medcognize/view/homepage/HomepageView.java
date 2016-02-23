package com.medcognize.view.homepage;

import com.medcognize.MedcognizeUI;
import com.medcognize.util.ImageUtil;
import com.medcognize.view.ComponentWindow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = HomepageView.NAME)
public class HomepageView extends HomepageDesign implements View {
    public static final String NAME = "home";
    private MedcognizeUI ref = (MedcognizeUI) MedcognizeUI.getCurrent();

    public static void showTermsOfService() {
        ComponentWindow window = new ComponentWindow("Terms of Service", true, true);
        window.showCancelButton(true);
        window.showSubmitButton(false);
        window.setCancelButtonText("OK");
        window.setWidth(400.0f, Unit.PIXELS);
        window.setHeight(400.0f, Unit.PIXELS);
        // CustomLayout doesn't support margins so we wrap it in another
        // layout which does
        CustomLayout cl = new CustomLayout("terms");
        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(true);
        vl.addComponent(cl);
        window.setContent(vl);
        UI.getCurrent().addWindow(window);
        window.focus();
    }

    public HomepageView() {
        menu.addItem("Register", ImageUtil.getRegisterImage(), new MenuBar.Command() {
                    @Override
                    public void menuSelected(MenuBar.MenuItem selectedItem) {
                        ref.getNavigator().navigateTo(RegisterView.NAME);
                    }
                }
        );
        menu.addItem("Login", ImageUtil.getLoginImage(), new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                ref.getNavigator().navigateTo(LoginView.NAME);
            }
        });
        registerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ref.getNavigator().navigateTo(RegisterView.NAME);
            }
        });
        termsLink.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                showTermsOfService();
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
