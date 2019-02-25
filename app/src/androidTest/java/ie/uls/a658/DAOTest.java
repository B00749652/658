package ie.uls.a658;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ie.uls.a658.auxiliary.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DAOTest {
    private DAO dao;
    private static SQLiteDatabase dbReadOnly;
    private static SQLiteDatabase dbWrite;

    @Before
    public void setup(){
        dao = new DAO(InstrumentationRegistry.getTargetContext(),"ContentDeliverySystem.db",null,1);
        dbReadOnly = dao.getReadableDatabase();
        dbWrite = dao.getWritableDatabase();
    }

    @Test
    public void isDatabaseReal(){
        assertTrue(dbReadOnly.isOpen());
    }

    @Test
    public void isDatabaseReadable(){
        int num = (int) DatabaseUtils.queryNumEntries(dbReadOnly,"Security");
        assertEquals(num,10);
    }

    @Test
    public void isDatabaseIterable(){
        Cursor cus = dbReadOnly.rawQuery("SELECT * FROM Security",null);
        int counter = 0;
        cus.moveToFirst();
        while(counter < 10){
            String line = cus.getString(cus.getColumnIndex("passphrase"));
            if(line != null){
                counter++;
            }
        }
        assertTrue(counter>=2);
    }


}
