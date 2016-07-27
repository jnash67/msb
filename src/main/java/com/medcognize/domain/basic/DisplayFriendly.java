package com.medcognize.domain.basic;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vaadin.data.util.BeanUtil;
import com.vaadin.shared.util.SharedUtil;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class DisplayFriendly implements Serializable {

    static final AtomicLong NEXT_ID = new AtomicLong(0);
    // each DisplayFriendly will have a unique ID for purposes of separating from others
    // in a Container.  This ID is not persistent and will be different each time
    // the application is run.
    final long sessionId = NEXT_ID.getAndIncrement();

    public long getUniqueSessionId() {
        return sessionId;
    }

    public static final List<Class<? extends DisplayFriendly>> domainList;
    public static final HashBiMap<Class<? extends DisplayFriendly>, String> friendlyClassNameMap;
    public static final Map<Class<? extends DisplayFriendly>, BiMap<String, String>> friendlyPropertyNameMap;

    static {
        // these need to be initialized with the actual domain of the application
        domainList = new ArrayList<>();
        friendlyClassNameMap = HashBiMap.create();
        friendlyPropertyNameMap = new HashMap<>();
    }

    // The main UI class in its static block should call this
    // e.g. DisplayFriendly.registerClasses("com.myapp.domain");
    public static void registerClasses(String topLevelPackageToScan) {
        Reflections refl = new Reflections(topLevelPackageToScan);
        Set<Class<? extends DisplayFriendly>> df_clazzes = refl.getSubTypesOf(DisplayFriendly.class);
        for (Class<? extends DisplayFriendly> df_clazz : df_clazzes) {
            if (!DisplayFriendlyAbstractEntity.class.equals(df_clazz)) {
                registerClass(df_clazz);
            }
        }
        System.out.println(friendlyPropertyNameMap);
    }

    private static void registerClass(Class<? extends DisplayFriendly> clazz)    {
        DisplayFriendlyCaption dn;
        domainList.add(clazz);
        dn = clazz.getAnnotation(DisplayFriendlyCaption.class);
        if (null == dn) {
            friendlyClassNameMap.put(clazz, clazz.getSimpleName());
        } else {
            friendlyClassNameMap.put(clazz, dn.value());
        }
        BiMap<String, String> friendlyPropertyNames = HashBiMap.create();
        try {
            List<PropertyDescriptor> properties = BeanUtil.getBeanPropertyDescriptor(clazz);
            for (PropertyDescriptor pd : properties) {
                String n = pd.getName();
                if (!("class".equals(n) || "id".equals(n) || "uniqueSessionId".equals(n))) {
                // if (!("class".equals(n))) {
                    // There's a getAuthorities method in User because it implements UserDetails
                    if (!("authorities".equals(n) && clazz.getSimpleName().equals("User"))) {
                        dn = (DisplayFriendlyCaption) getAnnotation(clazz, n, DisplayFriendlyCaption.class);
                        if (null == dn) {
                            friendlyPropertyNames.put(n, SharedUtil.propertyIdToHumanFriendly(n));
                        } else {
                            friendlyPropertyNames.put(n, dn.value());
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        friendlyPropertyNameMap.put(clazz, friendlyPropertyNames);
    }

    public static Annotation getAnnotation(Class<?> clazz, String fieldName, Class<? extends Annotation> annotationToLookFor) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            Annotation[] annotations = f.getAnnotations();
            for (Annotation a : annotations) {
                if (annotationToLookFor.isAssignableFrom(a.getClass())) {
                    return a;
                }
            }
        } catch (NoSuchFieldException e) {
            System.out.println("NoSuchFieldException " + fieldName + " in class " + clazz.getSimpleName());
            e.printStackTrace();
        }
        return null;
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

    private static Map<String, String> getFriendlyPropertyNameMap(Class<? extends DisplayFriendly> clazz) {
        if (friendlyClassNameMap.keySet().contains(clazz)) {
            Map<String, String> captionMap = friendlyPropertyNameMap.get(clazz);
            return captionMap;
        } else {
            log.error("DisplayFriendly extending class not properly registered within the DisplayFriendly class!");
            return null;
        }
    }

    public static String getFriendlyPropertyName(Class<? extends DisplayFriendly> clazz, String propertyId) {
        Map<String, String> captionMap = getFriendlyPropertyNameMap(clazz);
        if (null == captionMap) {
            return null;
        }
        return captionMap.get(propertyId);
    }

    public static Collection<String> propertyIdList(Class<? extends DisplayFriendly> clazz) {
        Map<String, String> captionMap = getFriendlyPropertyNameMap(clazz);
        if (null == captionMap) {
            return null;
        }
        return captionMap.keySet();
    }

    @SuppressWarnings("unused")
    public static Class<? extends DisplayFriendly> getClazzFromFriendlyName(String friendlyName) {
        if (!friendlyClassNameMap.containsValue(friendlyName)) {
            log.error("String (" + friendlyName + ") is not the friendly name of a DisplayFriendly class!");
            return null;
        }
        return friendlyClassNameMap.inverse().get(friendlyName);
    }

    public static String getFriendlyClassName(Class<? extends DisplayFriendly> clazz) {
        if (!friendlyClassNameMap.containsKey(clazz)) {
            log.error("DisplayFriendly extending class not properly registered within the DisplayFriendly class!");
            if (null == clazz) {
                return "";
            } else {
                return clazz.getSimpleName();
            }
        }
        return friendlyClassNameMap.get(clazz);
    }

    /*
    We only copy the properties registered in registerClass
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
