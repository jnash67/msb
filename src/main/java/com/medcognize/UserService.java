package com.medcognize;

import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import com.medcognize.domain.basic.EmailAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService {
    @Autowired
    UserRepository repo;

    // EXPOSE basic UserRepository methods here
    // NOTE: Whenever we return a User, we need to be sure we call setRepo for the User to make
    // sure it is aware of the repo.  We cannot Autowire the repo into the User because it needs
    // a no-arg constructor (as a JPA Entity).  And for legacy reasons there are still some areas
    // where the only reference to the repo is through a User instance.
    // ---------------------------------------------------------------------------------------------
    public long countByUserName(String username) {
        return repo.countByUsername(username);
    }

    public long countByAdminTrue() {
        return repo.countByAdminTrue();
    }

    public boolean existsByAdminTrue() {
        return repo.existsByAdminTrue();
    }

    public boolean existsByUsername(EmailAddress emailAddress) {
        return existsByUsername(emailAddress.toString());
    }

    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    public void deleteUser(User u) {
        repo.delete(u);
    }

    public List<User> getAllUsers() {
        List<User> users = repo.findAll();
        for (User u : users) {
            u.setRepo(this);
        }
        return users;
    }

    public User getUserById(Long id) {
        User u = repo.findOne(id);
        u.setRepo(this);
        return u;
    }

    public User getUserByUsername(String username) {
        User u = repo.findByUsername(username);
        u.setRepo(this);
        return u;
    }

    public String findPasswordForUsername(String username) {
        return repo.findPasswordForUsername(username);
    }

    // ---------------------------------------------------------------------------------------------
    private User createNewUser(final EmailAddress emailAddress, final String pwd, final boolean admin) {
        Assert.isTrue(!existsByUsername(emailAddress), "Email address cannot already exists in database");
        User u = new User(this, emailAddress, pwd);
        u.setAdmin(admin);
        createDefaultInitialSettings(u);
        return repo.save(u);
    }

    private User createNewUser(final String emailAddress, final String pwd, final boolean admin) {
        return createNewUser(new EmailAddress(emailAddress), pwd, admin);
    }

    public User createNewAdminUser(final EmailAddress emailAddress, final String pwd) {
        return createNewUser(emailAddress, pwd, true);
    }

    public User createNewRegularUser(final EmailAddress emailAddress, final String pwd) {
        return createNewUser(emailAddress, pwd, false);
    }

    @SuppressWarnings("unchecked")
    public void addToCollection(User user, DisplayFriendly ownedItem) {
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

    public void removeFromCollection(User user, DisplayFriendly ownedItem) {
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

    public <T extends DisplayFriendly> List<T> getAll(User user, T df) {
        //noinspection unchecked
        return (List<T>) getAll(user, df.getClass());
    }

    public void deleteMedicalExpensesForPlan(User user, Plan ap) {
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

    public List<MedicalExpense> getMedicalExpensesForPlan(User user, Plan ap) {
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

    public List<FamilyMember> getFamilyMembersWithPlanExpenses(User user, Plan ap) {
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

    public Plan getActivePlan(User user) {
        for (Plan p : user.getPlans()) {
            if (p.isActivePlan()) {
                return p;
            }
        }
        return null;
    }

    public void setActivePlan(User user, Plan ap) {
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
    public <T extends DisplayFriendly> List<T> getAll(User user, Class<T> clazz) {
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

    // Create relevant defaults for the required user entities
    public void createDefaultInitialSettings(User u) {
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
