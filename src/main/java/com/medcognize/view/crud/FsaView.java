package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.UserRepository;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.User;
import com.medcognize.form.FsaForm;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@SpringView(name = FsaView.NAME)
public class FsaView extends CrudView<Fsa> {
    public static final String NAME = "fsa";
    static final ArrayList<String> pids = new ArrayList<String>() {
        {
            add("fsaName");
            add("amountInFsa");
            add("fsaStartDate");
            add("fsaEndDate");
        }
    };

    @Autowired
    public FsaView(UserRepository repo) {
        super(Fsa.class, "Flexible Spending Account (FSA)", new CrudTable<Fsa>(repo, Fsa.class, FsaForm.class, pids));
        User owner = DbUtil.getLoggedInUser();
        if (null == owner) {
            log.error("owner should not be null here");
            return;
        }
        // Collection<Fsa> fsas = UserUtil.getAll(owner, Fsa.class);
        setData(owner);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        table.setConverter("fsaStartDate", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        table.setConverter("fsaEndDate", new StringToDateConverter() {
            @Override
            protected DateFormat getFormat(java.util.Locale locale) {
                return new SimpleDateFormat(MedcognizeUI.US_DATE_FORMAT);
            }
        });
        if (showHeader) {
            toolbar.showAddNewButton();
        }
    }
}
