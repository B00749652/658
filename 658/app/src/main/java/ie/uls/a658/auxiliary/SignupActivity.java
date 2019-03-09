package ie.uls.a658.auxiliary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import ie.uls.a658.R;
import ie.uls.a658.preferences.Score;


public class SignupActivity extends AppCompatActivity {
    private static EditText mEditText;
    private static String fullname, username, password;
    private boolean isAdmin = false;
    static final int PHOTO = 1;
    private File image;
    private Score score = Score.getScore();
    private static DAO dao;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // Passing Intent Data on Return from Camera App
        // Processed and Image Button Background Updated
        //

        if((requestCode == PHOTO) && (resultCode == RESULT_OK)){
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            ImageButton imButton = findViewById(R.id.Userimage);
            imButton.setImageBitmap(picture);
            if(username == null){username = "example";}
            try{
                File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                this.image = new File(file.toString(),username+".png");
                OutputStream out = new FileOutputStream(image);
                picture.compress(Bitmap.CompressFormat.PNG,100,out);
                out.flush();
                out.close();
            }
            catch(NullPointerException ex){ex.printStackTrace();}
            catch(IOException ex){ex.printStackTrace();}
        }

    }//OnActivityResult Method



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create SignUp Activity
        // Check Access Route for Admin Privileges
        // Add Additional Functionality to Activity
        setContentView(R.layout.activity_signup);

        try {
            if ((getIntent() != null) && (getIntent().hasExtra(Intent.EXTRA_TEXT))) {
                Bundle extras = getIntent().getExtras();
                this.isAdmin = Boolean.parseBoolean(extras.getString(Intent.EXTRA_TEXT));
            }
        }catch(NullPointerException ex){ex.printStackTrace();}
        if(isAdmin) {
            ViewStub isAdminCheck = findViewById(R.id.signupAdmin);
            View inflatedView = isAdminCheck.inflate();
        }

    }//OnCreate Method



    public void onRegisterClick(View view) throws NoSuchAlgorithmException {
        // Obtain User Input
        // Connect with Database
        // Insert new User with all attributes
        mEditText = findViewById(R.id.EditFullname);
        fullname = mEditText.getText().toString();
        mEditText = findViewById(R.id.EditUsername);
        username = mEditText.getText().toString();
        mEditText = findViewById(R.id.EditPassword);
        password = mEditText.getText().toString();
        score.setUserName(username);
        boolean isAdminUser = false;
        dao = new DAO(this.getBaseContext(),"ContentDeliverySystem.db",null,1);

        if(isAdmin) {
            CheckBox checkBox = findViewById(R.id.isAdminchk);
            isAdminUser = (checkBox.isChecked());
            if (this.image != null) {
                dao.insertNewUser(fullname, username, password, isAdminUser, this.image.getAbsolutePath());
            } else {
                dao.insertNewUser(fullname, username, password, isAdminUser, null);
            }
        }else{
            if (this.image != null) {
                dao.insertNewUser(fullname, username, password, false, this.image.getAbsolutePath());
            } else {
                dao.insertNewUser(fullname, username, password, false, null);
            }
        }
        Intent returnHome = new Intent(this,LoginActivity.class);
        startActivity(returnHome);
    }//OnRegisterClick Method



    public void onPhotoClick(View view) {
        // Start Android Camera
        // Watch for a results and return
        // a Photo to Sign up Activity
        mEditText = findViewById(R.id.EditUsername);
        username = mEditText.getText().toString();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,PHOTO);

    }//OnPhotoClick Method



}//Signup Activity Class
