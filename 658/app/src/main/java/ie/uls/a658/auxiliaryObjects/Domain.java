package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Domain {

    @PrimaryKey
    private int domID;

    private String domain;

    private String greeting;

    private String farewell;

    public Domain(){}

    public int getDomID() {
        return domID;
    }

    public void setDomID(int domID) {
        this.domID = domID;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getFarewell() {
        return farewell;
    }

    public void setFarewell(String farewell) {
        this.farewell = farewell;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
