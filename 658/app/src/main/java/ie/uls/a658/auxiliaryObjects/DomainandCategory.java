package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class DomainandCategory {

    @Embedded
    public Domain domain;

    @Embedded
    public ContentQuery contentQuery;

    public String domainline;

    @Relation(parentColumn="domID", entityColumn = "domainID" , entity=ContentQuery.class, projection = {"categoryID"})
    public List<String> whichCategory;

    public DomainandCategory(){}

}