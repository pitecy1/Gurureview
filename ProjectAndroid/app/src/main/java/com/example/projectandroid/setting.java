package com.example.projectandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.UserManager;
import com.example.projectandroid.model.User;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class setting extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String newP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView header = headerview.findViewById(R.id.header_name);
        final TextView email = headerview.findViewById(R.id.header_email);


        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        header.setText(globalClass.getUser().getName());
        email.setText(globalClass.getUser().getEmail());

        if(globalClass.getUser().getImg_user() != null){
            String eUserimg = "";
            try {
                eUserimg = URLDecoder.decode(globalClass.getUser().getImg_user(),"UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/"+ eUserimg;
            Log.e("url",u);


            ImageView imageView = headerview.findViewById(R.id.imageView);
            Picasso.with(setting.this).load(u).into(imageView);
        }

        final EditText eName = findViewById(R.id.settingName);
        EditText eEmail = findViewById(R.id.settingEmail);
        final EditText eOldpass = findViewById(R.id.settingOldPassword);
        EditText eNewpass = findViewById(R.id.settingNewPassword);

        Button btnSetting = findViewById(R.id.btnSettingChang);

        eName.setText(globalClass.getUser().getName());
        eEmail.setText(globalClass.getUser().getEmail());

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText eNamec = findViewById(R.id.settingName);
                EditText eEmailc = findViewById(R.id.settingEmail);
                EditText eOldpasswordc = findViewById(R.id.settingOldPassword);
                EditText eNewpassc = findViewById(R.id.settingNewPassword);

                if(!eNamec.getText().toString().matches("[a-zA-Z]{2,20}") || eEmailc.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                    builder.setTitle("WARNING");
                    builder.setIcon(R.drawable.warning);
                    builder.setMessage("\n Please enter Name please!");
                    builder.create();
                    builder.show();
                    eNamec.setText("");
                }
                else if(eEmailc.getText().toString().matches("") || eEmailc.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                    builder.setTitle("WARNING");
                    builder.setIcon(R.drawable.warning);
                    builder.setMessage("\n Please enter email please!");
                    builder.create();
                    builder.show();
                    eEmailc.setText("");
                }
                else if(!eOldpasswordc.getText().toString().matches("[a-zA-Z0-9]{2,15}") || eOldpasswordc.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                    builder.setTitle("WARNING");
                    builder.setIcon(R.drawable.warning);
                    builder.setMessage("\n password id wrong!");
                    builder.create();
                    builder.show();
                    eOldpasswordc.setText("");
                    eNewpassc.setText("");
                }
                else if(!eNewpassc.getText().toString().matches("[a-zA-Z0-9]{4,15}") || eNewpassc.getText().toString().isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                    builder.setTitle("WARNING");
                    builder.setIcon(R.drawable.warning);
                    builder.setMessage("\n Please enter password please!");
                    builder.create();
                    builder.show();
                    eOldpasswordc.setText("");
                    eNewpassc.setText("");
                }
                else{
                    UserManager manager = UserManager.getUserManager(setting.this);
                    manager.doConvertPasswordToSHA(eOldpasswordc.getText().toString(), new UserManager.UserManagerListener() {
                        @Override
                        public void onComplete(Object response) {
                            String defaultpassword = globalClass.getUser().getPassword();
                            String newpassword = response.toString();
                            EditText eN = findViewById(R.id.settingNewPassword);
                            newP = eN.getText().toString();
                            Log.e("new p " , newP);

                            if(defaultpassword.equalsIgnoreCase(newpassword)){
                                Log.e("update","start updateprofile");
                                User u = new User();
                                u.setUsername(globalClass.getUser().getUsername());
                                u.setName(eName.getText().toString());
                                u.setEmail(email.getText().toString());
                                u.setImg_user(globalClass.getUser().getImg_user());
                                u.setPassword(newP);
                                onUpdateUser(u);
                                Log.e("update","update success");


                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                                builder.setTitle("WARNING");
                                builder.setIcon(R.drawable.error);
                                builder.setMessage("\n Update fail please try agian!");
                                builder.create();
                                builder.show();

                            }

                        }

                        @Override
                        public void onError(String err) {
                            Log.e("setting err",err);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this,profile.class);
            startActivity(intent);
        } else if (id == R.id.nav_feeds) {
            Intent intent = new Intent(this,feeds.class);
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this,Search.class);
            startActivity(intent);
        } else if (id == R.id.nav_addreview) {
            Intent intent = new Intent(this,addreview.class);
            startActivity(intent);
        } else if (id == R.id.nav_myprofile) {
            Intent intent = new Intent(this,myProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this,setting.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onUpdateUser(User user){
        UserManager userManager = UserManager.getUserManager(setting.this);
        userManager.doUpdateUser(user, new UserManager.UserManagerListener() {
            @Override
            public void onComplete(Object response) {
                GlobalClass globalClass = (GlobalClass) getApplicationContext();
                User u = (User) response;
                try {
                    String dName = URLDecoder.decode(u.getName(),"UTF-8");


                    String ePass = URLDecoder.decode(u.getPassword(),"UTF-8");
                    String dEmail = URLDecoder.decode(u.getEmail(),"UTF-8");

                    User newUser = new User();
                    newUser.setName(dName);
                    newUser.setPassword(ePass);

                    newUser.setEmail(dEmail);
                    newUser.setUsername(u.getUsername());
                    if(u.getImg_user() != null){
                        String eImg = URLDecoder.decode(u.getImg_user(),"UTF-8");
                        newUser.setImg_user(eImg);
                    }

                    globalClass.setUser(newUser);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                builder.setTitle("WARNING");
                builder.setIcon(R.drawable.complete);
                builder.setMessage("\n update profile success!");
                builder.create();
                builder.show();

                Intent intent = new Intent(setting.this,profile.class);
                startActivity(intent);

            }

            @Override
            public void onError(String err) {

            }
        });

    }
}
