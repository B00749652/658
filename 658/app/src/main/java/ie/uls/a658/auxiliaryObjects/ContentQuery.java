package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ContentQuery {
    @PrimaryKey
    private int setID;

    private int levelID;

    private int domainID;

//    @ForeignKey(entity = Content.class, parentColumns = "categoryID", childColumns = "categoryID")
    private String categoryID;

    public ContentQuery(){}


    public int getSetID() {
        return setID;
    }

    public void setSetID(int setID) {
        this.setID = setID;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }

    public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
