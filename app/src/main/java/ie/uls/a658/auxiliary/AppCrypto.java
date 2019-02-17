package ie.uls.a658.auxiliary;

import android.content.Context;
import android.database.Cursor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppCrypto {
    private Context context;


    public AppCrypto(Context context){
        this.context = context;
    }



    public int onSecureLoginClick(String username, String password){
        // Security Management Method for
        // Checking Database Stored Credentials and deciding
        // which use-case story should be actuated
        boolean[] result = checkCredentials(username, password);
        int next = securityAction(result);
        return next;
    }//onSecureLoginClick


    protected String createCrypto(String passphrase) throws NoSuchAlgorithmException {
        // Create a CryptoGram using
        // SHA-2, 256-bit algorithm and store
        // as a legible alphanumeric string
        byte[] clearText = passphrase.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(clearText);
        byte[] cipherText = messageDigest.digest();
        StringBuilder sb = new StringBuilder(256);
        for(int i = 0; i< cipherText.length; i++) {
            sb.append(String.format("%02X",cipherText[i]));
        }
        return sb.toString().toLowerCase();
    }//CreateCryptoMethod



    private boolean[] checkCredentials(String username, String password){
        // Consult DataBase for
        // UserName and Password and
        // AccessLevel Credentials
        boolean[] checkResult = new boolean[2];

        try {
            DAO secTable = DAO.getInstance(this.context);
            String loginAttempt = createCrypto(password);
            secTable.getReadableDatabase();
            Cursor cursor = secTable.getCreds(username);
            cursor.moveToFirst();
            if(cursor != null) {
                while (cursor.isBeforeFirst()) {
                    if (cursor.getString(cursor.getColumnIndex("id")).equals(username)) {
                        checkResult[1] = false;
                        if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Admin")))) {
                            checkResult[1] = true;
                        }
                        if (cursor.getString(cursor.getColumnIndex("Passphrase")).equals(loginAttempt)) {
                            checkResult[0] = true;
                            return checkResult;
                        } else {
                            break;
                        }
                    }
                }
            }
            }catch(NoSuchAlgorithmException ex){
                ex.printStackTrace();
            }
        return checkResult;
    }//checkCredentials Method



    private int securityAction(boolean[] permissionsVector){
    // Define Matrix 'Truth Table' as follows:
    // 0 TRUE TRUE    -> Authorised Admin Login   -> Start Admin Case Story
    // 1 TRUE FALSE   -> Authorised User Login    -> Start User Case Story
    // 2 FALSE TRUE   -> UnAuthorised Admin Login -> Close App
    // 3 FALSE FALSE  -> Unauthorised User Login  -> Give 3 Attempts then Finish if Unsuccessful
        int decision;
        if(permissionsVector[0]){
            if(permissionsVector[1]){
                decision = 0;
                return decision;
            }
            decision = 1;
            return decision;
        }
        else {
                if (!permissionsVector[1]) {
                    decision = 3;
                    return decision;
                }
                decision = 2;
                return decision;
            }
    }

}//class AppCrypto