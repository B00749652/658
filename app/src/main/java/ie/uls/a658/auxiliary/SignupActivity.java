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
import java.security.NoSuchAlgorithmException;

import ie.uls.a658.R;


public class SignupActivity extends AppCompatActivity {
    private static EditText mEditText;
    private static String fullname, username, password;
    private boolean isAdmin = false;
    static final int PHOTO = 1;
    private static File image;
    private static DAO dao;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // Passing Intent Data on Return from Camera App
        // Processed and Image Button Background Updated
        //
        FileOutputStream out;
        if((requestCode == PHOTO) && (resultCode == RESULT_OK)){
            Bitmap picture = (Bitmap) data.getExtras().get("data");
            ImageButton imButton = findViewById(R.id.Userimage);
            imButton.setImageBitmap(picture);
            if(username == null){username = "example";}
            try{
                File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                image = File.createTempFile(username,".png",file);
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
        CheckBox checkBox = findViewById(R.id.isAdminchk);
        this.isAdmin = (checkBox.isChecked());

        dao = new DAO(this.getBaseContext(),"ContentDeliverySystem.db",null,1);

        if(image != null) {
            dao.insertNewUser(fullname, username, password, isAdmin, image.getAbsolutePath());
        }else{
            dao.insertNewUser(fullname, username, password, isAdmin, null);
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