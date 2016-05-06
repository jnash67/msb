package com.medcognize.view.crud;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.User;
import com.medcognize.domain.Fsa;
import com.medcognize.form.FsaForm;
import com.vaadin.data.util.converter.StringToDateConverter;
import com.vaadin.navigator.ViewChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class FsaView extends CrudView<Fsa> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanView.class);
    static final ArrayList<String> pids = new ArrayList<String>() {
        {
            add("fsaName");
            add("amountInFsa");
            add("fsaStartDate");
            add("fsaEndDate");
        }
    };

    public FsaView() {
        super(Fsa.class, FsaForm.class, pids, "Flexible Spending Account (FSA)");
        User owner = ((MedcognizeUI) MedcognizeUI.getCurrent()).getUser();
        if (null == owner) {
            LOGGER.error("owner should not be null here");
            return;
        }
        Collection<Fsa> fsas = owner.getAll(Fsa.class);
        setData(fsas, owner);
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