package com.medcognize.domain.basic;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DisplayFriendlyCollectionManager {

    public static void merge(User copyTo, User copyFrom) {
        // copy listed User properties which doesn't include the data file
        DisplayFriendly.copyListedProperties(copyFrom, copyTo);

        // add or copy listed properties if already exists
        mergeList(copyTo, copyFrom.getAll(Plan.class));
        mergeList(copyTo, copyFrom.getAll(Fsa.class));
        mergeList(copyTo, copyFrom.getAll(FamilyMember.class));
        mergeList(copyTo, copyFrom.getAll(Provider.class));
        mergeList(copyTo, copyFrom.getAll(MedicalExpense.class));
    }

    public static void deleteMedicalExpensesForPlan(User user, Plan ap) {
        if (null == ap) {
            return;
        }
        List<MedicalExpense> medicalExpenses = user.getMedicalExpenses();
        for (MedicalExpense me : medicalExpenses) {
            if (ap.equals(me.getPlan())) {
                medicalExpenses.remove(me);
            }
        }
    }

    public static <T extends DisplayFriendly> void mergeList(User user, List<T> list) {
        for (T item : list) {
            add(user, item);
        }
    }

    @SuppressWarnings("unchecked")
    public static void add(User user, DisplayFriendly ownedItem) {
        if (null == ownedItem) {
            return;
        }
        List items = getAll(user, ownedItem);
        if (null == items) {
            return;
        }
        int index = items.indexOf(ownedItem);
        if (-1 == index) {
            items.add(ownedItem);
            // log.warn("Saved new " + ownedItem.getClass().getSimpleName() + " (" + ownedItem + ").");
        } else {
            DisplayFriendly ae = (DisplayFriendly) items.get(index);
            DisplayFriendly.copyListedProperties(ownedItem, ae);
        }
    }

    public static void remove(User user, DisplayFriendly ownedItem) {
        if (null == ownedItem) {
            return;
        }
        List items = getAll(user, ownedItem);
        if (null == items) {
            return;
        }
        items.remove(ownedItem);
    }

    public static <T extends DisplayFriendly> List<T> getAll(User user, T df) {
        //noinspection unchecked
        return (List<T>) getAll(user, df.getClass());
    }

    public static FamilyMember getFamilyMemberByName(User user, String name) {
        for (FamilyMember fm : user.getFamilyMembers()) {
            if (fm.getFamilyMemberName().equals(name)) {
                return fm;
            }
        }
        return null;
    }

    public static Provider getProviderByName(User user, String name) {
        for (Provider pr : user.getProviders()) {
            if (pr.getProviderName().equals(name)) {
                return pr;
            }
        }
        return null;
    }

    public static Plan getPlanByName(User user, String name) {
        for (Plan pl : user.getPlans()) {
            if (pl.getPlanName().equals(name)) {
                return pl;
            }
        }
        return null;
    }

    public static List<MedicalExpense> getMedicalExpensesForPlan(User user, Plan ap) {
        List<MedicalExpense> expenses = new ArrayList<>();
        if (null == ap) {
            return expenses;
        }
        for (MedicalExpense me : user.getMedicalExpenses()) {
            Plan expPlan = me.getPlan();
            if (ap.equals(expPlan)) {
                expenses.add(me);
            }
        }
        return expenses;
    }

    public static List<FamilyMember> getFamilyMembersWithPlanExpenses(User user, Plan ap) {
        List<MedicalExpense> expenses = getMedicalExpensesForPlan(user, ap);
        List<FamilyMember> fms = new ArrayList<>();
        FamilyMember fm;
        for (MedicalExpense me : expenses) {
            fm = me.getFamilyMember();
            if (!fms.contains(fm)) {
                fms.add(fm);
            }
        }
        return fms;
    }

    @SuppressWarnings("unchecked")
    public static <T extends DisplayFriendly> List<T> getAll(User user, Class<T> clazz) {
        if (null == clazz) {
            return null;
        }
        if (clazz.isAssignableFrom(Plan.class)) {
            return (List<T>) user.getPlans();
        } else {
            if (clazz.isAssignableFrom(Provider.class)) {
                return (List<T>) user.getProviders();
            } else {
                if (clazz.isAssignableFrom(FamilyMember.class)) {
                    return (List<T>) user.getFamilyMembers();
                } else {
                    if (clazz.isAssignableFrom(MedicalExpense.class)) {
                        return (List<T>) user.getMedicalExpenses();
                    } else {
                        if (clazz.isAssignableFrom(Fsa.class)) {
                            return (List<T>) user.getFsas();
                        }
                    }
                }
            }
        }
        log.error("Requested unowned class " + clazz);
        return null;
    }

    public static Plan getActivePlan(User user) {
        for (Plan p : user.getPlans()) {
            if (p.isActivePlan()) {
                return p;
            }
        }
        return null;
    }

    public static void setActivePlan(User user, Plan ap) {
        boolean set = false;
        for (Plan p : user.getPlans()) {
            if (p.equals(ap)) {
                p.setActivePlan(true);
                set = true;
            } else {
                p.setActivePlan(false);
            }
        }
        if (!set) {
            log.error("Active plan (" + ap + ") not set. Not found in user plans list.");
        }
    }

}
