package ie.uls.a658.auxiliaryObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface DatabaseAccessObject {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT categoryID FROM Domain INNER JOIN ContentQuery ON domainID = domID WHERE domainID IN (SELECT domID FROM Domain WHERE domain == :choice )  AND levelID = :level")
    List<String> getAll(String choice, int level);

    @Query("SELECT content FROM Content WHERE categoryID = :category")
    List<String> getContent(String category);

    @Query("SELECT * FROM Domain WHERE domID = :choice")
    Domain[] getFormalities(int choice);

    @Query("SELECT Count(*) AS A, SUM(CASE WHEN admin = 'false' THEN 1 END) AS B, SUM(CASE WHEN admin = 'true' THEN 1 END) AS C FROM Security")
    ManagementData getManagementData();

    @Query("SELECT COUNT(*) AS total FROM sqlite_master WHERE type = 'table'")
    int getTableCount();

    @Query("SELECT * FROM Security WHERE userID = :user")
    Security getCreds(String user);

    @Query("SELECT COUNT(*) FROM Security")
    int getNumUsers();

    @Insert
    void insertNewUser(Security user);

    @Insert
    void insertDomain(List<Domain> domain);

    @Insert
    void insertAttributes(Attributes attrib);

    @Insert
    void insertContentQuery(List<ContentQuery> conq);

    @Insert
    void insertContent(List<Content> content);


}
