package ie.uls.a658;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ie.uls.a658.auxiliaryObjects.ContentDeliverySystem;
import ie.uls.a658.auxiliaryObjects.DatabaseAccessObject;
import ie.uls.a658.auxiliaryObjects.Security;


public class AppCrypto {
    public static Context context;
    private DatabaseAccessObject dao;
    int next;
    AppCrypto(Context context1){
        context =context1;
    }


    synchronized int onSecureLoginClick(String username, String password) throws NoSuchAlgorithmException{
        // Security Management Method for
        // Checking Database Stored Credentials and deciding
        // which use-case story should be actuated
            boolean[] result = checkCredentials(username, password);
            this.next = securityAction(result);
        return next;
    }//onSecureLoginClick


    String createCrypto(String passphrase) throws NoSuchAlgorithmException {
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



    private boolean[] checkCredentials(String username, String password) {
        // Consult DataBase for
        // UserName and Password and
        // AccessLevel Credentials
        boolean[] checkResult = new boolean[2];
        dao = ContentDeliverySystem.getInstance(context).dao();
        final Security suspect = dao.getCreds(username);
         try {
             String loginAttempt = createCrypto(password);

            if(suspect != null) {
                    if (suspect.getUserID().equals(username)) {

                        if (Boolean.parseBoolean(suspect.getAdmin())) {
                            checkResult[1] = true;
                        }
                        if (suspect.getPassphrase().equals(loginAttempt)) {
                            checkResult[0] = true;
                        }
                    }
                }
            }catch(NoSuchAlgorithmException ne){ne.printStackTrace();}

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