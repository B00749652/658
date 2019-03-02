package ie.uls.a658.auxiliary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import ie.uls.a658.AdminActivity;
import ie.uls.a658.preferences.MetaCogActivity;
import ie.uls.a658.R;

public class LoginActivity extends AppCompatActivity {
    private static int numAttempts = 3;
    private static DAO dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new DAO(this.getApplicationContext(),"ContentDeliverySystem.db",null,1);
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
        EditText mEditText = findViewById(R.id.EditUsernameL);
        String username = mEditText.getText().toString();
        EditText mEditText2 = findViewById((R.id.EditPasswordL));
        String password = mEditText2.getText().toString();
        int nextViewNumber = 4;
        if(username.equals("") || password.equals("")){
            Toast.makeText(LoginActivity.this, "Forgotten Password? ", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                AppCrypto appCrypto = new AppCrypto(this.getBaseContext());
                nextViewNumber = appCrypto.onSecureLoginClick(username, password);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            Intent intent = null;
            switch (nextViewNumber) {
                case 0:
                    intent = new Intent(this, AdminActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(this, MetaCogActivity.class);
                    intent.putExtra("user",username);
                    startActivity(intent);
                    break;
                case 2:
                    finish();
                    break;
                case 3:
                    if (numAttempts > 0) {
                        Toast.makeText(LoginActivity.this, "Forgotten Password? Only " + numAttempts + " more attempts", Toast.LENGTH_LONG).show();
                        numAttempts--;
                    } else {
                        finish();
                    }
                    break;
                default:
                    finish();
                    break;

            }
        }
    }//OnLoginClick Method

}//Login Activity Class
