package ie.uls.a658.auxiliary;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import ie.uls.a658.R;

/**
 * Database Access Object Auxiliary Class
 *
 * */
public class DAO extends SQLiteOpenHelper {
    private static DAO dao;
    private static final String DATABASE_NAME = "ContentDeliverySystem.db";
    private static final String LIFESTYLE_TABLE_NAME = "LifeStyle";
    private static final String LIFESTYLE_COL_ID = "id";
    private static final String LIFESTYLE_COL_MSG = "Message";
    private static final String HUMOUR_TABLE_NAME = "Humour";
    private static final String HUMOUR_COL_ID = "id";
    private static final String HUMOUR_COL_MSG = "Message";
    private static final String ACTIVE_TABLE_NAME = "Active";
    private static final String ACTIVE_COL_ID = "id";
    private static final String ACTIVE_COL_MSG = "Message";
    private static final String SUGGESTION_TABLE_NAME = "Suggestion";
    private static final String SUGGESTION_COL_ID = "id";
    private static final String SUGGESTION_COL_MSG = "Message";
    private static final String SECURITY_TABLE_NAME = "Security";
    private static final String SECURITY_ID = "id";
    private static final String SECURITY_PASSWORD = "Passphrase";
    private static final String SECURITY_ADMIN = "Admin";
    private static final String SECURITY_FULLNAME = "FullName";
    private static final String SECURITY_PHOTO = "Photo";

    private Context context;


    private DAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,null,1);
    }

    public static DAO getInstance(Context context){
        dao = new DAO(context.getApplicationContext(),null,null,1);
        return dao;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +LIFESTYLE_TABLE_NAME + " ( " +
                LIFESTYLE_COL_ID + " INTEGER " + "PRIMARY KEY ," +
                LIFESTYLE_COL_MSG + " VARCHAR(250) " + "NOT NULL );" );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ACTIVE_TABLE_NAME + " ( " +
                ACTIVE_COL_ID + " INTEGER " + " PRIMARY KEY ," +
                ACTIVE_COL_MSG + " VARCHAR(250)" + " NOT NULL );" );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + HUMOUR_TABLE_NAME + " ( " +
                HUMOUR_COL_ID + " INTEGER " + " PRIMARY KEY ," +
                HUMOUR_COL_MSG + " VARCHAR(250) " + " NOT NULL );");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SUGGESTION_TABLE_NAME + " ( " +
                SUGGESTION_COL_ID + " INTEGER " + " PRIMARY KEY ," +
                SUGGESTION_COL_MSG + " VARCHAR(250) " + " NOT NULL );");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SECURITY_TABLE_NAME + " ( " +
                SECURITY_FULLNAME + " VARCHAR(50) " + " NOT NULL ," +
                SECURITY_ID + " VARCHAR(15) " + " PRIMARY KEY ," +
                SECURITY_PASSWORD + " VARCHAR(256) " + " NOT NULL ," +
                SECURITY_ADMIN + "BOOLEAN "+ " NOT NULL ," +
                SECURITY_PHOTO + " TEXT );");
        loadData();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            deleteAllData(db);
            onCreate(db);
            loadData();
        }
    }



    public Cursor getCreds(String UserName){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql0 = String.format("SELECT DISTINCT %s, %s, %s,%s FROM %s, WHERE %s = %s ;",
                SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,SECURITY_TABLE_NAME,
                SECURITY_ID,UserName);
        Cursor cursor = db.rawQuery(sql0,null);
        return cursor;
    }//getLocalSecurity Method



    public Cursor getMessages(String querySQL){
        // Any SQL Query
        // Select Operation
        // Returns a Database Cursor
        DAO appdao = getInstance(this.context);
        SQLiteDatabase db = appdao.getWritableDatabase();
        Cursor cursor = db.rawQuery(querySQL,null);
        return cursor;
    }//General getMessages Method



    private void loadData(){
        // Load Data into Data Base
        // ROOT Security
        // Other Data for Content Delivery
        DAO dbStart = getInstance(this.context);
        SQLiteDatabase secure = dbStart.getWritableDatabase();

        final String ADMIN_NAME = "ROOT";
        final String AUTH_CIPHER = "0ccfab85fac5e3d72f31147fee5df3f8b9f3f796c7d10d49fdb497719ac3964c";
        final boolean ADMIN_PERMISSION = true;
        final String DQL = String.format("INSERT INTO %s,( %s, %s, %s, %s ) VALUES(%s,%s,%s,%s)",
                SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,
                ADMIN_NAME,ADMIN_NAME,AUTH_CIPHER,ADMIN_PERMISSION);
        secure.execSQL(DQL);

        Map<String,String> objMap = new HashMap<>();
        Resources res = context.getResources();
        InputStream in = res.openRawResource(R.raw.testdatabasedata);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<String> data = new ArrayList<>();
        try {
            int count = 0;
            while (br.readLine() != null) {
                String line = br.readLine();
                StringTokenizer st = new StringTokenizer(line,",");
                while(st.hasMoreTokens()){
                      String domain = st.nextToken();
                      String suggestion = st.nextToken();

                      /*Add data to data list here*/

                    break;
                }
                count++;
            }

        }catch(IOException ex){ex.printStackTrace();}

        /* INSERT DATA READING FORMAT HERE */

    }


    private void insertData(){
        /*To be Written*/
    }


    public void insertNewUser(String fullname, String username, String password, boolean admin,@Nullable String photo) throws NoSuchAlgorithmException {
        // Insert New User into Database
        // Photo Optional stored locally, encrypted password
        // with all other tuple attributes
        SQLiteDatabase db2 = this.getWritableDatabase();
        AppCrypto crypt = new AppCrypto(this.context);
        String cipherText = crypt.createCrypto(password);
        String s = "";
        if(photo!= null) {
            String dql1 = String.format("INSERT INTO %s,( %s, %s, %s, %s, %s ) VALUES(%s,%s,%s,%s,%s)",
            SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,SECURITY_PHOTO,
                    fullname,username,cipherText,admin,photo);
            db2.execSQL(dql1);
        }else{
            String dql2 = String.format("INSERT INTO %s,( %s, %s, %s, %s ) VALUES(%s,%s,%s,%s)",
                    SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,
                    fullname,username,cipherText,admin);
            db2.execSQL(dql2);
        }
        db2.close();
    }//insertNewUser Method

    private void deleteAllData(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+LIFESTYLE_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+ACTIVE_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+HUMOUR_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+SUGGESTION_TABLE_NAME );
    }


}//DAO Class
