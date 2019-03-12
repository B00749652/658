package ie.uls.a658;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ie.uls.a658.auxiliaryObjects.Attributes;
import ie.uls.a658.auxiliaryObjects.Content;
import ie.uls.a658.auxiliaryObjects.ContentDeliverySystem;
import ie.uls.a658.auxiliaryObjects.ContentQuery;
import ie.uls.a658.auxiliaryObjects.DatabaseAccessObject;
import ie.uls.a658.auxiliaryObjects.Domain;
import ie.uls.a658.auxiliaryObjects.Security;
import ie.uls.a658.preferences.MetaCogActivity;
import ie.uls.a658.preferences.Score;

public class LoginActivity extends AppCompatActivity {
    private static int numAttempts = 3;
    private int nextViewNumber = 4;
    private Context contxt;
    private DatabaseAccessObject dao;
    private Score score = Score.getScore();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contxt = getApplicationContext();
        dao = ContentDeliverySystem.getInstance(getApplicationContext()).dao();
        new Thread(() -> insertdata()).start();
        setContentView(R.layout.activity_login);

    }


    private void insertdata(){
        if(!(dao.getNumUsers() > 0)) {
            Security root = new Security();
            root.setFullname("ROOT");
            root.setUserID("ROOT");
            root.setAdmin("True");
            root.setPassphrase("0ccfab85fac5e3d72f31147fee5df3f8b9f3f796c7d10d49fdb497719ac3964c");
            dao.insertNewUser(root);

            List<Domain> domlist = new ArrayList<>();
            InputStream dataIn = contxt.getResources().openRawResource(R.raw.domain);
            BufferedReader br = new BufferedReader(new InputStreamReader(dataIn));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    Domain dom = new Domain();
                    dom.setDomID(Integer.parseInt(data[0]));
                    dom.setDomain(data[1]);
                    dom.setGreeting(data[2]);
                    dom.setFarewell(data[3]);
                    domlist.add(dom);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dao.insertDomain(domlist);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            dataIn = contxt.getResources().openRawResource(R.raw.attributes);
            br = new BufferedReader(new InputStreamReader(dataIn));
            try {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    Attributes att = new Attributes();
                    att.setUserID(data[0]);
                    att.setMobility(data[1]);
                    att.setCompany(data[2]);
                    att.setCognition(data[3]);
                    att.setSex(data[4]);
                    dao.insertAttributes(att);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<ContentQuery> conqlist = new ArrayList<>();
            dataIn = contxt.getResources().openRawResource(R.raw.contentquery);
            br = new BufferedReader(new InputStreamReader(dataIn));
            try {
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",");
                    ContentQuery conq = new ContentQuery();
                    conq.setSetID(Integer.parseInt(st.nextToken()));
                    conq.setLevelID(Integer.parseInt(st.nextToken()));
                    conq.setDomainID(Integer.parseInt(st.nextToken()));
                    conq.setCategoryID(st.nextToken());
                    conqlist.add(conq);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dao.insertContentQuery(conqlist);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Content> contlist = new ArrayList<>();
            dataIn = contxt.getResources().openRawResource(R.raw.content);
            br = new BufferedReader(new InputStreamReader(dataIn));
            try {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    Content cont = new Content();
                    cont.setContentID(Integer.parseInt(data[0]));
                    cont.setCategoryID(data[1]);
                    cont.setContent(data[2]);
                    contlist.add(cont);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dao.insertContent(contlist);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

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
        new Thread(()->{
        score.setUserName(username);
            try {
                AppCrypto appCrypto = new AppCrypto(this.getBaseContext());
                nextViewNumber = appCrypto.onSecureLoginClick(username, password);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            Intent intent = null;
            if(nextViewNumber == 0){
                intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            }else if(nextViewNumber == 1){
                intent = new Intent(this, MetaCogActivity.class);
                intent.putExtra("user",username);
                startActivity(intent);

            }else if(nextViewNumber >=3){
                finish();
            }
        }).start();


    }//OnLoginClick Method

}//Login Activity Class
