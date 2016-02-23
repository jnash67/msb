package com.medcognize.domain.basic;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyAccessorFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
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
    public static final Map<Class<? extends Enum>, String> friendlyEnumMap;
    public static final Map<Class<? extends Enum>, Class<? extends DisplayFriendly>> friendlyEnumParentMap;

    static {
        // these need to be initialized with the actual domain of the application
        friendlyNameMap = HashBiMap.create();
        friendlyEnumMap = new HashMap<>();
        friendlyEnumParentMap = new HashMap<>();
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
        if (friendlyEnumMap.keySet().contains(clazz)) {
            return true;
        }
        return false;
    }

    public static String getEnumCaption(Enum e) {
        if (isDisplayFriendlyEnum(e.getClass())) {
            Class<? extends DisplayFriendly> parentClass = friendlyEnumParentMap.get(e.getClass());
            String fieldMap = friendlyEnumMap.get(e.getClass());
            try {
                BiMap<String, String> stringMap = (BiMap<String, String>) parentClass.getField(fieldMap).get(null);
                return stringMap.get(e.toString());
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Enum getEnumFromCaption(Class<? extends Enum> enumClazz, String caption) {
        if (isDisplayFriendlyEnum(enumClazz)) {
            Class<? extends DisplayFriendly> parentClass = friendlyEnumParentMap.get(enumClazz);
            String fieldMap = friendlyEnumMap.get(enumClazz);
            try {
                BiMap<String, String> stringMap = (BiMap<String, String>) parentClass.getField(fieldMap).get(null);
                String enumStringVal = stringMap.inverse().get(caption);
                return Enum.valueOf(enumClazz, enumStringVal);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
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
            propVal = PropertyAccessorFactory.forBeanPropertyAccess(copyFrom).getPropertyValue(pid);
            PropertyAccessorFactory.forBeanPropertyAccess(copyTo).setPropertyValue(pid, propVal);
        }
    }
}


