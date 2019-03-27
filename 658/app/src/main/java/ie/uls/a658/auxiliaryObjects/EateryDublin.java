package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class EateryDublin {

    @PrimaryKey
    @NonNull
    private String num;

    private String establishment;

    private String type;

    private String cluster;

    private String address;

    public EateryDublin(){}

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type;    }

    @NonNull
    public String getNum() {
        return num;
    }

    public void setNum(@NonNull String num) {
        this.num = num;
    }
}
