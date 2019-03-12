package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Attributes {
    @PrimaryKey
    @NonNull
    private String userID;

    private String company;

    private String cognition;

    private String mobility;

    private String sex;

    public Attributes(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCognition() {
        return cognition;
    }

    public void setCognition(String cognition) {
        this.cognition = cognition;
    }

    public String getMobility() {
        return mobility;
    }

    public void setMobility(String mobility) {
        this.mobility = mobility;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
