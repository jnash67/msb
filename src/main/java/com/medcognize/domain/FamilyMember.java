package com.medcognize.domain;

import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import com.medcognize.domain.basic.DisplayName;
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
@DisplayName("Family Member")
public class FamilyMember extends DisplayFriendlyAbstractEntity {

    @NotBlank(message = "The family member name cannot be blank")
    @DisplayName("Name")
    private String familyMemberName = "";

    @Override
    public String toString() {
        return familyMemberName;
    }
}
