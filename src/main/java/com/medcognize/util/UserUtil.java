package com.medcognize.util;

import com.medcognize.UserRepository;
import com.medcognize.domain.*;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.basic.EmailAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UserUtil implements Serializable {

    private static User createNewUser(final UserRepository repo, final EmailAddress emailAddress, final String pwd,
                                      final boolean admin) {
        Assert.isTrue(!existsByUsername(repo, emailAddress), "Email address cannot already exists in database");
        User u = new User();
        u.setUsername(emailAddress.toString());
        u.setPassword(new BCryptPasswordEncoder().encode(pwd));
        u.getUserRoles().add(SpringUtil.RoleType.ROLE_USER.name());
        if (admin) {
            u.getUserRoles().add(SpringUtil.RoleType.ROLE_ADMIN.name());
        }
        u.setCredentialsNonExpired(true);
        u.setAccountNonExpired(true);
        u.setAccountNonLocked(true);
        u.setEnabled(true);
        createDefaultInitialSettings(u);
        return repo.save(u);
    }

    public static boolean existsByUsername(final UserRepository repo, EmailAddress emailAddress) {
        return repo.existsByUsername(emailAddress.toString());
    }

    private static User createNewUser(final UserRepository repo, final String emailAddress, final String pwd, final
    boolean admin) {
        return createNewUser(repo, new EmailAddress(emailAddress), pwd, admin);
    }

    public static User createNewAdminUser(final UserRepository repo, final EmailAddress emailAddress, final String
            pwd) {
        return createNewUser(repo, emailAddress, pwd, true);
    }

    public static User createNewRegularUser(final UserRepository repo, final EmailAddress emailAddress, final String
            pwd) {
        return createNewUser(repo, emailAddress, pwd, false);
    }

    @SuppressWarnings("unchecked")
    public static void addToCollection(final UserRepository repo, User user, DisplayFriendly ownedItem) {
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
        } else
        // log.warn("Saved new " + ownedItem.getClass().getSimpleName() + " (" + ownedItem + ").");
        {
            DisplayFriendly ae = (DisplayFriendly) items.get(index);
            DisplayFriendly.copyListedProperties(ownedItem, ae);
        }
        repo.save(user);
    }

    public static void removeFromCollection(final UserRepository repo, User user, DisplayFriendly ownedItem) {
        if (null == ownedItem) {
            return;
        }
        List items = getAll(user, ownedItem);
        if (null == items) {
            return;
        }
        items.remove(ownedItem);
        repo.save(user);
    }

    public static <T extends DisplayFriendly> List<T> getAll(User user, T df) {
        //noinspection unchecked
        return (List<T>) getAll(user, df.getClass());
    }

    public static void deleteMedicalExpensesForPlan(final UserRepository repo, User user, Plan ap) {
        if (null == ap) {
            return;
        }
        List<MedicalExpense> medicalExpenses = user.getMedicalExpenses();
        for (MedicalExpense me : medicalExpenses) {
            if (ap.equals(me.getPlan())) {
                medicalExpenses.remove(me);
            }
        }
        repo.save(user);
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

    public static Plan getActivePlan(User user) {
        for (Plan p : user.getPlans()) {
            if (p.isActivePlan()) {
                return p;
            }
        }
        return null;
    }

    public static void setActivePlan(final UserRepository repo, User user, Plan ap) {
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
        } else {
            repo.save(user);
        }
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

    public static <T extends DisplayFriendly> String ensureUniqueName(String initialName,
                                                                      Class<T> clazz, String stringPropertyName) {
        Collection<? extends DisplayFriendly> dfs = UserUtil.getAll(DbUtil.getLoggedInUser(), clazz);
        String name = initialName;
        int v = 2;
        while (!existsName(name, dfs, stringPropertyName)) {
            name = initialName + "v" + String.valueOf(v);
            v++;
        }
        return name;
    }

    public static boolean existsName(String name, Collection<? extends DisplayFriendly> dfs, String pid) {
        for (DisplayFriendly df : dfs) {
            String val;
            try {
                val = PropertyUtils.getProperty(df, pid).toString();
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                val = "";
            }
            if (name.equals(val)) {
                return false;
            }
        }
        return true;
    }

    // Create relevant defaults for the required user entities
    public static void createDefaultInitialSettings(User u) {
        FamilyMember fm;
        Plan p;
        fm = new FamilyMember();
        String email = u.getUsername();
        String firstName = u.getFirstName();
        if (null == firstName) {
            firstName = "";
        }
        if ("".equals(firstName)) {
            // now create the first family member and the plan for the user
            firstName = email.substring(0, email.indexOf("@"));
        }
        fm.setFamilyMemberName(firstName);
        // this saves fm and then u
        u.getFamilyMembers().add(fm);
        p = new Plan();
        String planName = firstName + "\'s Plan";
        if (planName.length() > 50) {
            p.setPlanName(planName.substring(0, 50));
        } else {
            p.setPlanName(planName);
        }
        // this saves p and then u
        p.setActivePlan(true);
        u.getPlans().add(p);
    }

}