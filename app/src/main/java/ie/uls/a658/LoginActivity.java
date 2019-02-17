package ie.uls.a658;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ie.uls.a658.auxiliary.AppCrypto;
import ie.uls.a658.auxiliary.DAO;

public class LoginActivity extends AppCompatActivity {
    private static int numAttempts = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DAO dataStore = DAO.getInstance(this.getBaseContext());
        setContentView(R.layout.activity_login);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // Return from SignUP Activity
        // Requirement to Login with new Credentials
        // No Action Required

    }



    public void onSignUp(View view){
        // Start Sign Up Activity with
        // intention to return to Login when
        // user has registered
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent,1);

    }


    public void onLoginClick(View view){
        // Login credential check against
        // Security Table in Database
        // Start next activity either ADMIN or META-COG
        EditText mEditText = findViewById(R.id.EditUsername);
        String username = mEditText.getText().toString();
        EditText mEditText2 = findViewById((R.id.EditPassword));
        String password = mEditText2.getText().toString();
        AppCrypto crypt = new AppCrypto(this.getBaseContext());
        int nextViewNumber = crypt.onSecureLoginClick(username, password);
        Intent intent = null;
        switch(nextViewNumber) {
            case 0:
                intent = new Intent(this, AdminActivity.class);
                break;
            case 1:
                intent = new Intent(this, MetaCogActivity.class);
                break;
            case 2:
                finish();
                break;
            case 3:
                if(numAttempts > 0) {
                    Toast.makeText(LoginActivity.this, "Forgotten Password? Only " + numAttempts + " more attempts", Toast.LENGTH_LONG).show();
                    numAttempts--;
                }else{
                    finish();
                }
                break;
            default:
                finish();
                break;
        }
        if(intent != null){
            startActivity(intent);
        }

    }//OnLoginClick Method

}//Login Activity Class
