package ie.uls.a658;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.uls.a658.auxiliary.DAO;
import ie.uls.a658.auxiliary.LoginActivity;
import ie.uls.a658.auxiliary.SignupActivity;

public class AdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        String content = initializedata();
        TextView statview = findViewById(R.id.adminstatview);
        statview.setText(content);

        drawerLayout = findViewById(R.id.adminDrawer);
        NavigationView navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isEnabled());
                switch(menuItem.getItemId()){
                    case R.id.menu_1:
                        Toast.makeText(getApplicationContext(),"User Experience",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu_2:
                        Toast.makeText(getApplicationContext(),"New User Registration",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent intent0 = new Intent(getApplicationContext(), SignupActivity.class);
                        intent0.putExtra(Intent.EXTRA_TEXT,"true");
                        startActivity(intent0);
                        return true;
                    case R.id.menu_3:
                        Toast.makeText(getApplicationContext(),"Change Password",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.menu_4:
                        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                            return false;
                }

            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

    }//onCreate Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String initializedata(){
        DAO dao = new DAO(this.getBaseContext(),"ContentDeliverySystem.db",null,1);
        String content = "";
        StringBuilder sb = new StringBuilder(1000);

        String managed = "SELECT Count(*) AS A, "+
                " SUM(CASE WHEN admin = 'false' THEN 1 END) AS B, " +
                " SUM(CASE WHEN admin = 'true' THEN 1 END) AS C " +
                " FROM Security; ";
        //String numUsers = "SELECT COUNT(*) AS total FROM Security;";
        Cursor cursor = dao.getMessages(managed);
        String num ="",num1= "",num2 = "";
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
            num = cursor.getString(cursor.getColumnIndex("A"));
            num1 = cursor.getString(cursor.getColumnIndex("B"));
            num2 = cursor.getString(cursor.getColumnIndex("C"));
            cursor.move(1);
            }
        }
        cursor.close();


        String numTables = "SELECT name , COUNT(*) AS total FROM sqlite_master WHERE type = 'table';";
        Cursor cursor1 = dao.getMessages(numTables);
        String num3 = "";
        List<String> entities = new ArrayList<>();
        List<String> sqlit = new ArrayList<>();
        if(cursor1 != null) {
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                num3 = ((cursor1.getString(cursor1.getColumnIndex("total"))) == null) ? num3 : (cursor1.getString(cursor1.getColumnIndex("total")));
                cursor1.move(1);
            }
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                String namedentity = cursor1.getString(cursor1.getColumnIndex("name"));
                entities.add(namedentity);
                cursor1.move(1);
            }
        }
        cursor1.close();


        String line1 = String.format("Total Registered Users :\t%s\n",num);
        String line2 = String.format("Total Administrator Users :\t%s\n",num1);
        String line3 = String.format("Total Ordinary Users :\t%s\n",num2);
        String line4 = String.format("Total Tables in Database :\t%s\n",num3);
        sb.append(line1);
        sb.append(line2);
        sb.append(line3);
        sb.append(line4);
        for(String line5: entities){sb.append(line5).append("\n");}
        content = sb.toString();
        return content;
    }


}
