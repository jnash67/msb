package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.vaadin.data.fieldgroup.Caption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
// two family members are equal if they have the same name
@EqualsAndHashCode(callSuper = false, of = {"familyMemberName"})
@Entity
public class FamilyMember extends DisplayFriendlyAbstractEntity {
    private static final String captionString = "familyMemberName:Name";
    @SuppressWarnings("UnusedDeclaration")
    public static final BiMap<String, String> captionMap = createBiMap(captionString);
    // was previously "name" and Provider also had a "name" field which caused a conflict. So
    // changed it to be more specific.
    @NotBlank(message = "The family member name cannot be blank")
    @Caption("Name")
    private String familyMemberName = "";

    @Override
    public String toString() {
        return familyMemberName;
    }
}
