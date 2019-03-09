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

import ie.uls.a658.R;


/**
 * Database Access Object Auxiliary Class
 *
 * */
public class DAO extends SQLiteOpenHelper {
    private static SQLiteDatabase db;
    private static DAO dao;
    private static Context contxt;
    private static final String DATABASE_NAME = "ContentDeliverySystem.db";
    private static final String CONTENT_TABLE_NAME = "Content";
    private static final String CONTENT_COL_ID = "contentID";
    private static final String CONTENT_COL_DOMAIN = "domain";
    private static final String CONTENT_COL_CONTENT = "content";
    private static final String ATTRIBUTES_COL_ID = "userID";
    private static final String ATTRIBUTES_COL_COG = "cognition";
    private static final String ATTRIBUTES_COL_COM = "company";
    private static final String ATTRIBUTES_COL_SEX = "sex";
    private static final String ATTRIBUTES_COL_MOB = "mobility";
    private static final String ATTRIBUTES_TABLE_NAME = "Attributes";
    private static final String PHYSICAL_TABLE_NAME = "Physical";
    private static final String PHYSICAL_COL_LIGHT = "light";
    private static final String PHYSICAL_COL_TEMP = "temperature";
    private static final String PHYSICAL_COL_AMB = "ambient";
    private static final String PHYSICAL_COL_DOM = "domain";
    private static final String DOMAIN_TABLE_NAME = "Domain";
    private static final String DOMAIN_COL_ID = "domainID";
    private static final String DOMAIN_COL_MSG = "domain";
    private static final String DOMAIN_COL_GREETING = "greeting";
    private static final String DOMAIN_COL_FAREWELL = "farewell";
    private static final String SECURITY_TABLE_NAME = "Security";
    private static final String SECURITY_ID = "userID";
    private static final String SECURITY_PASSWORD = "passphrase";
    private static final String SECURITY_ADMIN = "admin";
    private static final String SECURITY_FULLNAME = "fullname";
    private static final String SECURITY_PHOTO = "photo";



    public DAO(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,null,1);
        contxt = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1, table2, table3, table4;
        table1 = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY," +
                "%s INTEGER , %s INTEGER, %s INTEGER, %s VARCHAR(20));",ATTRIBUTES_TABLE_NAME,ATTRIBUTES_COL_ID,ATTRIBUTES_COL_SEX,
                ATTRIBUTES_COL_COM,ATTRIBUTES_COL_MOB,ATTRIBUTES_COL_COG);
        db.execSQL(table1);
        table2 = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY," +
                        "%s TEXT , %s TEXT , %s TEXT);",DOMAIN_TABLE_NAME,DOMAIN_COL_ID,DOMAIN_COL_MSG, DOMAIN_COL_GREETING, DOMAIN_COL_FAREWELL);
        db.execSQL(table2);
        table3 = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER," +
                "%s INTEGER , %s INTEGER, %s TEXT);",PHYSICAL_TABLE_NAME,PHYSICAL_COL_AMB,PHYSICAL_COL_LIGHT,PHYSICAL_COL_TEMP,PHYSICAL_COL_DOM);
        db.execSQL(table3);
        table4 = String.format("CREATE TABLE IF NOT EXISTS %s ( %s TEXT," +
                "%s TEXT , %s INTEGER PRIMARY KEY);",CONTENT_TABLE_NAME,CONTENT_COL_CONTENT,CONTENT_COL_DOMAIN, CONTENT_COL_ID);
        db.execSQL(table4);

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SECURITY_TABLE_NAME + " ( " +
                SECURITY_FULLNAME + " VARCHAR(50) " + " NOT NULL ," +
                SECURITY_ID + " VARCHAR(15) " + " PRIMARY KEY ," +
                SECURITY_PASSWORD + " VARCHAR(60) " + " NOT NULL ," +
                SECURITY_ADMIN + " TEXT "+ " NOT NULL ," +
                SECURITY_PHOTO + " TEXT );");
        final String ADMIN_NAME = "ROOT";
        final String AUTH_CIPHER = "0ccfab85fac5e3d72f31147fee5df3f8b9f3f796c7d10d49fdb497719ac3964c";
        final boolean ADMIN_PERMISSION = true;
        final String DQL = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES('%s','%s','%s','%s')",
                SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,
                ADMIN_NAME,ADMIN_NAME,AUTH_CIPHER,ADMIN_PERMISSION);
        db.execSQL(DQL);
        // Insert Data
        InputStream dataIn = contxt.getResources().openRawResource(R.raw.domain);
        BufferedReader br = new BufferedReader(new InputStreamReader(dataIn));
        String line; StringBuilder sb = new StringBuilder(100000);
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }
        }catch(IOException e){e.printStackTrace();}
        String query = sb.toString();
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            deleteAllData(db);
            onCreate(db);
        }
    }



    public Cursor getCreds(String UserName){
        db = null;
        db = getReadableDatabase();
        String sql0 = String.format("SELECT %s, %s, %s FROM %s WHERE %s = '%s' ;",
                SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,SECURITY_TABLE_NAME,
                SECURITY_ID,UserName);
        Cursor cursor = db.rawQuery(sql0,null);
        return cursor;
    }//getLocalSecurity Method



    public Cursor getMessages(String querySQL){
        // Any SQL Query
        // Select Operation
        // Returns a Database Cursor
        Cursor cursor = db.rawQuery(querySQL,null);
        return cursor;
    }//General getMessages Method


    public void insertNewUser(String fullname, String username, String password, boolean admin,@Nullable String photo) throws NoSuchAlgorithmException {
        // Insert New User into Database
        // Photo Optional stored locally, encrypted password
        // with all other tuple attributes
        db = null;
        db = getWritableDatabase();
        AppCrypto crypt = new AppCrypto(contxt);
        String cipherText = crypt.createCrypto(password);

        if(photo!= null) {
            String dql1 = String.format("INSERT INTO %s ( %s, %s, %s, %s, %s ) VALUES('%s','%s','%s','%s','%s');",
            SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,SECURITY_PHOTO,
                    fullname,username,cipherText,admin,photo);
            db.execSQL(dql1);
        }else{
            String dql2 = String.format("INSERT INTO %s ( %s, %s, %s, %s ) VALUES('%s','%s','%s','%s');",
                    SECURITY_TABLE_NAME,SECURITY_FULLNAME,SECURITY_ID,SECURITY_PASSWORD,SECURITY_ADMIN,
                    fullname,username,cipherText,admin);
            db.execSQL(dql2);
        }
        db.close();
    }//insertNewUser Method

    private void deleteAllData(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+DOMAIN_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+CONTENT_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+PHYSICAL_TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS "+ATTRIBUTES_TABLE_NAME);
    }


}//DAO Class
