package com.medcognize.util.export;

import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import com.medcognize.domain.FamilyMember;
import com.medcognize.domain.Fsa;
import com.medcognize.domain.MedicalExpense;
import com.medcognize.domain.Plan;
import com.medcognize.domain.Provider;
import com.medcognize.domain.User;
import com.medcognize.domain.basic.DisplayFriendly;
import lombok.extern.slf4j.Slf4j;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.csveed.bean.BeanInstructions;
import org.csveed.bean.BeanInstructionsImpl;
import org.csveed.bean.conversion.AbstractConverter;
import org.csveed.report.CsvException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
@Slf4j
public class CsvUtil implements Serializable {

    @SuppressWarnings("unchecked")
    public static <T extends DisplayFriendly> List<T> csvWithHeaderToDisplayFriendlyList(Class<T> clazz, String csvString, User u) {
        //        System.out.println("==========================");
        //        System.out.println("CSV for class " + clazz.getSimpleName());
        //        System.out.println(csvString);
        if ((null == csvString) || ("".equals(csvString.trim()))) {
            return new ArrayList<>();
        }
        BeanInstructions bi = getBeanInstructions(clazz, u);
        if (null == bi) {
            return new ArrayList<>();
        }
        StringReader sr = new StringReader(csvString);
        CsvClientImpl<T> csvReader = new CsvClientImpl<>(sr, bi);
        csvReader.setSeparator(",".charAt(0));
        List<T> dfs;
        try {
            dfs = csvReader.readBeans();
        } catch (CsvException e) {
            dfs = new ArrayList<>();
        }
        return dfs;
    }

    private static <T extends DisplayFriendly> BeanInstructions getBeanInstructions(Class<T> clazz, User u) {
        BeanInstructions bi;
        if (Plan.class == clazz) {
            bi = getPlanBeanInstructions();
        } else if (Fsa.class == clazz) {
            bi = getFsaBeanInstructions();
        } else if (MedicalExpense.class == clazz) {
            bi = getMedicalExpenseBeanInstructions(u);
        } else if (Provider.class == clazz) {
            bi = getProviderBeanInstructions();
        } else if (User.class == clazz) {
            Collection<String> pids = DisplayFriendly.propertyIdList(clazz);
            pids.remove("password");
            bi = getListedPropertiesBeanInstructions(clazz, pids);
        } else {
            bi = getListedPropertiesBeanInstructions(clazz, DisplayFriendly.propertyIdList(clazz));
        }
        return bi;
    }

    private static <T extends DisplayFriendly> BeanInstructions getListedPropertiesBeanInstructions(Class<T> clazz, Collection<String> pids) {
        BeanInstructions bi = new BeanInstructionsImpl(clazz);
        bi.setMapper(TolerantColumnNameMapper.class);
        for (String pid : pids) {
            try {
                bi.mapColumnNameToProperty(pid, pid);
            } catch (CsvException ce) {
                ce.printStackTrace();
            }
        }
        return bi;
    }

    private static BeanInstructions getPlanBeanInstructions() {
        BeanInstructions bi = getListedPropertiesBeanInstructions(Plan.class, DisplayFriendly.propertyIdList(Plan.class));
        bi.setDate("planStartDate", "dd-MM-yyyy");
        bi.setDate("planEndDate", "dd-MM-yyyy");
        bi.setConverter("planType", new AbstractConverter<Plan.PlanType>(Plan.PlanType.class) {
            @Override
            public Plan.PlanType fromString(String text) throws Exception {
                Plan.PlanType retVal = Plan.PlanType.valueOf(text);
                if (null == retVal) {
                    retVal = Plan.defaultPlanType;
                }
                return retVal;
            }
            @Override
            public String toString(Plan.PlanType value) throws Exception {
                return null;
            }
        });
        return bi;
    }

    private static BeanInstructions getFsaBeanInstructions() {
        BeanInstructions bi = getListedPropertiesBeanInstructions(Fsa.class, DisplayFriendly.propertyIdList(Fsa.class));
        bi.setDate("fsaStartDate", "dd-MM-yyyy");
        bi.setDate("fsaEndDate", "dd-MM-yyyy");
        return bi;
    }

    private static BeanInstructions getMedicalExpenseBeanInstructions(final User u) {
        BeanInstructions bi = getListedPropertiesBeanInstructions(MedicalExpense.class, DisplayFriendly.propertyIdList(MedicalExpense.class));
        bi.setDate("date", "dd-MM-yyyy");
        bi.setConverter("medicalExpenseType", new EasierAbstractConverter<MedicalExpense.MedicalExpenseType>(MedicalExpense.MedicalExpenseType.class) {
            @Override
            public MedicalExpense.MedicalExpenseType fromString(String text) throws Exception {
                MedicalExpense.MedicalExpenseType retVal = MedicalExpense.MedicalExpenseType.valueOf(text);
                if (null == retVal) {
                    retVal = MedicalExpense.defaultMedicalExpenseType;
                }
                return retVal;
            }
        });
        bi.setConverter("prescriptionTierType", new EasierAbstractConverter<MedicalExpense.PrescriptionTierType>(MedicalExpense.PrescriptionTierType.class) {
            @Override
            public MedicalExpense.PrescriptionTierType fromString(String text) throws Exception {
                MedicalExpense.PrescriptionTierType retVal = MedicalExpense.PrescriptionTierType.valueOf(text);
                if (null == retVal) {
                    retVal = MedicalExpense.defaultPrescriptionTierType;
                }
                return retVal;
            }
        });
        bi.setConverter("familyMember", new EasierAbstractConverter<FamilyMember>(FamilyMember.class) {
            @Override
            public FamilyMember fromString(String name) throws Exception {
                return getFamilyMemberByName(u, name);
            }
        });
        bi.setConverter("provider", new EasierAbstractConverter<Provider>(Provider.class) {
            @Override
            public Provider fromString(String name) throws Exception {
                return getProviderByName(u, name);
            }
        });
        bi.setConverter("plan", new EasierAbstractConverter<Plan>(Plan.class) {
            @Override
            public Plan fromString(String name) throws Exception {
                return getPlanByName(u, name);
            }
        });
        return bi;
    }

    private static BeanInstructions getProviderBeanInstructions() {
        BeanInstructions bi = getListedPropertiesBeanInstructions(Provider.class, DisplayFriendly.propertyIdList(Provider.class));
        bi.setConverter("providerType", new EasierAbstractConverter<Provider.ProviderType>(Provider.ProviderType.class) {
            @Override
            public Provider.ProviderType fromString(String text) throws Exception {
                Provider.ProviderType retVal = Provider.ProviderType.valueOf(text);
                if (null == retVal) {
                    retVal = Provider.defaultProviderType;
                }
                return retVal;
            }
        });
        return bi;
    }

    // METHODS BELOW ARE FOR CSV WRITING. ABOVE IS FOR READING.
    public static <T extends DisplayFriendly> String displayFriendlyToCsv(Class<T> clazz, T entity, User u) {
        final List<T> oneEntityList = new ArrayList<>();
        if (null != entity) {
            oneEntityList.add(entity);
        }
        return displayFriendlyListToCsv(clazz, oneEntityList, u);
        // switch to the below when csveed bean writing functionality works fully
        //return newDisplayFriendlyListToCsv(clazz, oneEntityList, ulm);
    }

    public static <T extends DisplayFriendly> String displayFriendlyListToCsv(Class<T> clazz, List<T> list, User u) {
        ColumnPositionMappingStrategy<T> strat = new ColumnPositionMappingStrategy<>();
        strat.setType(clazz);
        Collection<String> pids = DisplayFriendly.propertyIdList(clazz);
        String[] pidsArray = new String[pids.size()];
        pids.toArray(pidsArray);
        strat.setColumnMapping(pidsArray);
        // HeaderBeanToCsv writes a header even if the list is empty
        BeanToCsv<T> btc = new HeaderBeanToCsv<>(clazz);
        StringWriter sw = new StringWriter();
        btc.write(strat, sw, list);
        return sw.toString();
    }

    // switch to the below when csveed bean writing functionality works fully
    public static <T extends DisplayFriendly> String newDisplayFriendlyListToCsv(Class<T> clazz, List<T> list, User u) {
        if (null == list) {
            return "";
        }
        BeanInstructions bi = getBeanInstructions(clazz, u);
        if (null == bi) {
            CsvUtil.log.error("Cannot pass null BeanInstructions");
        }
        StringWriter sw = new StringWriter();
        CsvClient<T> csvWriter = new CsvClientImpl<>(sw, bi);
        csvWriter.setSeparator(",".charAt(0));
        try {
            csvWriter.writeBeans(list);
        } catch (NullPointerException npe) {
            System.out.println("Error writing " + clazz.getSimpleName());
            npe.printStackTrace();
        }
        String csv = sw.toString();
        System.out.println("Generated CSV " + csv);
        return csv;
    }

    private static <T extends DisplayFriendly> List<T> readBeansTolerantly(Class<T> clazz, CsvClient<T> client) {
        List<T> beans = new ArrayList<>();
        while (!client.isFinished()) {
            try {
                T bean = client.readBean();
                if (bean != null) {
                    beans.add(bean);
                }
            } catch (CsvException exc) {
                exc.printStackTrace();
                System.out.println("CSV row for class " + clazz.getSimpleName() + " failed to parse.  Row will be " + "ignored and permanently lost when class file is re-persisted.");
            }
        }
        return beans;
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
}
