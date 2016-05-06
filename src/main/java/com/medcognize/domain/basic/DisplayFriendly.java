package com.medcognize.domain.basic;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.PlanLimit;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class DisplayFriendly implements Serializable {

    static final AtomicLong NEXT_ID = new AtomicLong(0);
    // each DisplayFriendly will have a unique ID for purposes of separating from others
    // in a BeanContainer.  This ID is not persistent and will be different each time
    // the application is run
    final long sessionId = NEXT_ID.getAndIncrement();

    public long getUniqueSessionId() {
        return sessionId;
    }

    public static final HashBiMap<Class<? extends DisplayFriendly>, String> friendlyNameMap;

    static {
        friendlyNameMap = HashBiMap.create();
        friendlyNameMap.put(User.class, "User");
        friendlyNameMap.put(Plan.class, "Plan");
        friendlyNameMap.put(Provider.class, "Provider");
        friendlyNameMap.put(FamilyMember.class, "Family Member");
        friendlyNameMap.put(MedicalExpense.class, "Medical Expense");
        friendlyNameMap.put(Fsa.class, "FSA");
        friendlyNameMap.put(Address.class, "Address");
        friendlyNameMap.put(PlanLimit.class, "Plan Limit");
    }

    public static final Splitter.MapSplitter mp = Splitter.on(",").trimResults().withKeyValueSeparator(":");

    public static BiMap<String, String> createBiMap(String defineString) {
        Map<String, String> captionMap = mp.split(defineString);
        BiMap<String, String> biMap = HashBiMap.create();
        for (String key : captionMap.keySet()) {
            biMap.put(key, captionMap.get(key));
        }
        return biMap;
    }

    public static <E extends Enum<E>> boolean isDisplayFriendlyEnum(Class<E> clazz) {
        if (Provider.ProviderType.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (MedicalExpense.MedicalExpenseType.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (MedicalExpense.PrescriptionTierType.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Plan.PlanType.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    public static String getEnumCaption(Enum e) {
        if (e instanceof Provider.ProviderType) {
            return Provider.providerTypeStringMap.get(e.toString());
        }
        if (e instanceof MedicalExpense.MedicalExpenseType) {
            return MedicalExpense.medicalExpenseTypeStringMap.get(e.toString());
        }
        if (e instanceof MedicalExpense.PrescriptionTierType) {
            return MedicalExpense.prescriptionTierStringMap.get(e.toString());
        }
        if (e instanceof Plan.PlanType) {
            return Plan.planTypeStringMap.get(e.toString());
        }
        return null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Enum getEnumFromCaption(Class<? extends Enum> enumClazz, String caption) {
        if (Provider.ProviderType.class.equals(enumClazz)) {
            return Provider.ProviderType.valueOf(Provider.providerTypeStringMap.inverse().get(caption));
        }
        if (MedicalExpense.MedicalExpenseType.class.equals(enumClazz)) {
            return MedicalExpense.MedicalExpenseType.valueOf(MedicalExpense.medicalExpenseTypeStringMap.inverse().get
                    (caption));
        }
        if (MedicalExpense.PrescriptionTierType.class.equals(enumClazz)) {
            return MedicalExpense.PrescriptionTierType.valueOf(MedicalExpense.prescriptionTierStringMap.inverse().get
                    (caption));
        }
        if (Plan.PlanType.class.equals(enumClazz)) {
            return Plan.PlanType.valueOf(Plan.planTypeStringMap.inverse().get(caption));
        }
        return null;
    }

    private static Map<String, String> getCaptionMap(Class<? extends DisplayFriendly> clazz) {
        if (friendlyNameMap.keySet().contains(clazz)) {
            try {
                Field field = clazz.getDeclaredField("captionMap");
                @SuppressWarnings("unchecked") Map<String, String> captionMap = (Map<String, String>) field.get(null);
                return captionMap;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error("Failed to retrieve caption by reflection " + e);
                return null;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                log.error("Failed to retrieve caption by reflection " + e);
                return null;
            }
        } else {
            log.error("DisplayFriendly extending class not properly registered within the DisplayFriendly class!");
            return null;
        }
    }

    public static String getPropertyCaption(Class<? extends DisplayFriendly> clazz, String propertyId) {
        Map<String, String> captionMap = getCaptionMap(clazz);
        if (null == captionMap) {
            return null;
        }
        return captionMap.get(propertyId);
    }

    public static Collection<String> propertyIdList(Class<? extends DisplayFriendly> clazz) {
        Map<String, String> captionMap = getCaptionMap(clazz);
        if (null == captionMap) {
            return null;
        }
        return captionMap.keySet();
    }

    @SuppressWarnings("unused")
    public static Class<? extends DisplayFriendly> getClazzFromFriendlyName(String friendlyName) {
        if (!friendlyNameMap.containsValue(friendlyName)) {
            log.error("String (" + friendlyName + ") is not the friendly name of a DisplayFriendly class!");
            return null;
        }
        return friendlyNameMap.inverse().get(friendlyName);
    }

    public static String getFriendlyClassName(Class<? extends DisplayFriendly> clazz) {
        if (!friendlyNameMap.containsKey(clazz)) {
            log.error("DisplayFriendly extending class not properly registered within the DisplayFriendly class!");
            if (null == clazz) {
                return "";
            } else {
                return clazz.getSimpleName();
            }
        }
        return friendlyNameMap.get(clazz);
    }

    /*
    We only copy the properties listed in the captionString, which are the only properties
    which show up in forms.  Therefore, for example the Password field of User wouldn't be
    copied because it's not listed.  The point of this functionality is to reload previously
    downloaded data, which also wouldn't have the not-listed fields.
     */
    public static <T extends DisplayFriendly> void copyListedProperties(T copyFrom, T copyTo) {
        if ((null == copyFrom) || (null == copyTo)) {
            return;
        }
        Collection<String> pids = propertyIdList(copyFrom.getClass());
        Object propVal;
        for (String pid : pids) {
            try {
                propVal = PropertyUtils.getProperty(copyFrom, pid);
                PropertyUtils.setProperty(copyTo, pid, propVal);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}


