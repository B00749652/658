package ie.uls.a658;

import android.support.test.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import ie.uls.a658.auxiliary.AppCrypto;

@RunWith(JUnit4.class)
public class AppCryptoTest {
    static AppCrypto appCrypto = new AppCrypto(InstrumentationRegistry.getTargetContext());


    @Test
    public void isCryptoAdminValid(){
        int result = appCrypto.onSecureLoginClick("admin","admin");
        assertEquals(result, 0);
    }


    @Test
    public void isCryptoValid(){
        int result = appCrypto.onSecureLoginClick("username","password");
        assertEquals(result, 1);
    }


    @Test
    public void isCryptoAdminPasswordValid(){
        int result = appCrypto.onSecureLoginClick("admin","NotAdminPassword");
        assertEquals(result, 2);
    }



    @Test
    public void isCryptoUserPasswordValid(){
        int result = appCrypto.onSecureLoginClick("username","NotMyPassword");
        assertEquals(result, 3);
    }


    @Test
    public void isCryptoWorking(){
        int result1 = appCrypto.onSecureLoginClick("username","password");
        int result2 = appCrypto.onSecureLoginClick("username","PASSWORD");
        assertFalse(result1 == result2);
    }



}
