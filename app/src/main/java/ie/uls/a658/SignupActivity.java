package ie.uls.a658;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import ie.uls.a658.auxiliary.DAO;

public class SignupActivity extends AppCompatActivity {
    private EditText mEditText;
    private String fullname, username, password;
    private String photoname = this.username + ".png";
    private Uri photoUri = Uri.fromFile(new File(photoname));
    private boolean isAdmin = false;
    static final int PHOTO = 1;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // Passing Intent Data on Return from Camera App
        // Processed and Image Button Background Updated
        //
        FileOutputStream out;
        if(requestCode == PHOTO){
            ImageButton imButton = findViewById(R.id.Userimage);
            imButton.setImageBitmap((Bitmap) data.getExtras().get("data"));
            try{
                out = openFileOutput(photoname, Context.MODE_PRIVATE);
                out.write(data.getExtras().get("data").toString().getBytes());
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
        ViewStub isAdminCheck = findViewById(R.id.signupAdmin);
        if(!isAdmin) {
            isAdminCheck.setVisibility(View.INVISIBLE);
        }else {
            isAdminCheck.setVisibility(View.VISIBLE);
        }

    }//OnCreate Method



    public void onRegisterClick(View view) throws NoSuchAlgorithmException {
        // Obtain User Input
        // Connect with Database
        // Insert new User with all attributes
        mEditText = findViewById(R.id.EditFullname);
        this.fullname = mEditText.getText().toString();
        mEditText = findViewById(R.id.EditUsername);
        this.username = mEditText.getText().toString();
        mEditText = findViewById(R.id.EditPassword);
        this.password = mEditText.getText().toString();

        DAO dataLib = DAO.getInstance(this.getApplicationContext());
        File file = new File(username+".png");
        if(file.isFile()) {
            dataLib.insertNewUser(fullname, username, password, isAdmin, file.getPath());
        }else{
            dataLib.insertNewUser(fullname, username, password, isAdmin, null);
        }
        finish();

    }//OnRegisterClick Method



    public void onPhotoClick(View view) {
        // Start Android Camera
        // Watch for a results and return
        // a Photo to Sign up Activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent,PHOTO);

    }//OnPhotoClick Method



}//Signup Activity Class
