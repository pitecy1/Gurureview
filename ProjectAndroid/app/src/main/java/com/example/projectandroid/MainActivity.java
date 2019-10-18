package com.example.projectandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.LoginManager;
import com.example.projectandroid.model.User;
import com.example.projectandroid.model.UserModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btnlogin = findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtUsername = findViewById(R.id.txtUsername);
                EditText txtPassword = findViewById(R.id.txtPassword);

                final ProgressDialog progress = ProgressDialog.show(MainActivity.this, getString(R.string.please_wait),
                        getString(R.string.please_wait), true);
                LoginManager manager = LoginManager.getLoginManager(MainActivity.this);
                UserModel userModel = new UserModel();
                userModel.getUser().setUsername(txtUsername.getText().toString());
                userModel.getUser().setPassword(txtPassword.getText().toString());

                manager.doLogin(userModel, new LoginManager.LoginManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        progress.dismiss();
                        finish();

                        UserModel user = (UserModel) response;
                        User users = new User() ;
                        try {
                            String dName = URLDecoder.decode( user.getUser().getName(),"UTF-8");
                            String dMail = URLDecoder.decode( user.getUser().getEmail() ,"UTF-8");
                            String dPass = URLDecoder.decode( user.getUser().getPassword() ,"UTF-8" );

                            users.setName(dName);
                            users.setEmail(dMail);
                            users.setPassword(dPass);
                            if(user.getUser().getImg_user() != null){
                                String urldecode = URLDecoder.decode(user.getUser().getImg_user(),"UTF-8");
                                users.setImg_user(urldecode);
                            }

                            users.setUsername(user.getUser().getUsername());

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.e("dsds",user.getUser().getName());
                        GlobalClass globalClass = (GlobalClass) getApplicationContext();
                        globalClass.setUser(users);
                        startActivity(new Intent(MainActivity.this,feeds.class));
                    }

                    @Override
                    public void onError(String err) {
                        progress.dismiss();
                        Toast.makeText(MainActivity.this,err,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
