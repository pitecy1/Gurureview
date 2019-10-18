package com.example.projectandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectandroid.manager.LoginManager;
import com.example.projectandroid.model.UserModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userregister);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText txtUsername = findViewById(R.id.txtUser);
                EditText txtPassword = findViewById(R.id.txtPass);
                EditText txtName = findViewById(R.id.txtName);
                EditText txtEmail = findViewById(R.id.txtEmail);

                final ProgressDialog progress = ProgressDialog.show(RegisterActivity.this, getString(R.string.please_wait),
                        getString(R.string.please_wait), true);
                LoginManager manager = LoginManager.getLoginManager(RegisterActivity.this);
                UserModel userModel = new UserModel();

                try {
                    String eName = URLEncoder.encode(txtName.getText().toString(),"UTF-8");
                    String ePass = URLEncoder.encode(txtPassword.getText().toString(),"UTF-8");
                    String eMail = URLEncoder.encode(txtEmail.getText().toString(),"UTF-8");

                    userModel.getUser().setName(eName);
                    userModel.getUser().setPassword(ePass);
                    userModel.getUser().setEmail(eMail);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                userModel.getUser().setUsername(txtUsername.getText().toString());

                manager.doRegister(userModel, new LoginManager.LoginManagerListener() {
                    @Override
                    public void onComplete(Object response) {
                        progress.dismiss();
                        finish();

                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onError(String err) {
                        progress.dismiss();
                        Toast.makeText(RegisterActivity.this,"fails!",Toast.LENGTH_LONG).show();
                    }
                });



            }
        });





    }
}
