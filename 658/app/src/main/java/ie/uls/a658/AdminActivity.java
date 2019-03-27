package ie.uls.a658;

import android.content.Context;
import android.content.Intent;
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

import java.util.Locale;

import ie.uls.a658.auxiliaryObjects.ContentDeliverySystem;
import ie.uls.a658.auxiliaryObjects.DatabaseAccessObject;
import ie.uls.a658.auxiliaryObjects.ManagementData;

public class AdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Context contxt;
    DatabaseAccessObject dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        this.contxt = getApplicationContext();
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
        String content = "";
        StringBuilder sb = new StringBuilder(1000);
        dao = ContentDeliverySystem.getInstance(contxt).dao();
        //String numUsers = "SELECT COUNT(*) AS total FROM Security;";
        new Thread(()-> {
            ManagementData managementData = dao.getManagementData();
            String num = "", num1 = "", num2 = "";
            num = String.format(Locale.ENGLISH, "%d", managementData.getC());
            num1 = String.format(Locale.ENGLISH, "%d", managementData.getA());
            num2 = String.format(Locale.ENGLISH, "%d", managementData.getB());

//            String num3 = String.format(Locale.ENGLISH, "%d", dao.getTableCount());

            String line1 = String.format("Total Registered Users :\t%s\n", num);
            String line2 = String.format("Total Administrator Users :\t%s\n", num2);
            String line3 = String.format("Total Ordinary Users :\t%s\n", num1);
 //           String line4 = String.format("Total Tables in Database :\t%s\n", num3);
            sb.append(line1);
            sb.append(line2);
            sb.append(line3);
 //           sb.append(line4);
        }).start();
        content = sb.toString();
        return content;
    }


}
