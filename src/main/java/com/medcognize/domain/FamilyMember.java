// Generated by delombok at Sun May 08 23:43:45 EDT 2016
package com.medcognize.domain;

import com.google.common.collect.BiMap;
import com.medcognize.domain.basic.DisplayFriendlyAbstractEntity;
import org.hibernate.validator.constraints.NotEmpty;
import javax.persistence.Entity;
import java.io.Serializable;
// two family members are equal if they have the same name
@Entity
public class FamilyMember extends DisplayFriendlyAbstractEntity {
    private static final String captionString = "familyMemberName:Name";
    @SuppressWarnings("UnusedDeclaration")
    public static final BiMap<String, String> captionMap = createBiMap(captionString);
    // was previously "name" and Provider also had a "name" field which caused a conflict. So
    // changed it to be more specific.
    @NotEmpty
    private String familyMemberName = "";

    @Override
    public String toString() {
        return familyMemberName;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public String getFamilyMemberName() {
        return this.familyMemberName;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public void setFamilyMemberName(final String familyMemberName) {
        this.familyMemberName = familyMemberName;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public FamilyMember() {
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof FamilyMember)) return false;
        final FamilyMember other = (FamilyMember) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$familyMemberName = this.getFamilyMemberName();
        final java.lang.Object other$familyMemberName = other.getFamilyMemberName();
        if (this$familyMemberName == null ? other$familyMemberName != null : !this$familyMemberName.equals(other$familyMemberName)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof FamilyMember;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $familyMemberName = this.getFamilyMemberName();
        result = result * PRIME + ($familyMemberName == null ? 43 : $familyMemberName.hashCode());
        return result;
    }
}
