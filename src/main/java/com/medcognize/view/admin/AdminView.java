package com.medcognize.view.admin;

import com.medcognize.UserService;
import com.medcognize.domain.User;
import com.medcognize.util.DbUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@SpringView(name = AdminView.NAME)
public class AdminView extends TabSheet implements View {
    public static final String NAME = "admin";

    private UserService repo;

    @Autowired
    public AdminView(UserService repo) {
        super();
        this.repo = repo;
        User owner = DbUtil.getLoggedInUser();
        if (null == owner) {
            log.error("owner should not be null here");
            return;
        }
        // addTab(new TestTab(), "Test");
        // addTab(new ChartTestTab(),"Chart");
        addTab(new AnyUserZipCsvUploadTab(repo), "Zip Upload");
        addTab(new ZipCsvDownloadTab(), "Zip Download");
        addTab(new ManualCsvUploadTab(), "Manual CSV Upload");
        addTab(new UserEditAdminView(repo), "User Admin");
//        final Layout layoutQuadrant = new QuadrantGridLayout();
//        final Layout layoutQuadrantWithButtons = new QuadrantWithButtonsGridLayout();
//        setSizeFull(); // Make the TabSheet fill all available space. By default the height is fixed.
//        addTab(layoutQuadrant, "Simple");
//        addTab(layoutQuadrantWithButtons, "With Buttons");
//	    addSelectedTabChangeListener(new SelectedTabChangeListener() {
//		    @Override
//		    public void selectedTabChange(SelectedTabChangeEvent event) {
//			    markAsDirty();
//		    }
//	    });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}

