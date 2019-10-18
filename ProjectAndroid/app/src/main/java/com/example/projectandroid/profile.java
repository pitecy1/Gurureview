package com.example.projectandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.manager.UserManager;
import com.example.projectandroid.model.Category;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

public class profile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Uri filePath;
    private final int REQ = 5847;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView header = headerview.findViewById(R.id.header_name);
        TextView email = headerview.findViewById(R.id.header_email);
        ImageView headerImg = headerview.findViewById(R.id.imageView);




        GlobalClass globalClass = (GlobalClass) getApplicationContext();
        header.setText(globalClass.getUser().getName());
        email.setText(globalClass.getUser().getEmail());


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        ImageView pImg = findViewById(R.id.img_profile);

        Log.e("profile","start profile");


        if(globalClass.getUser().getImg_user() != null ){
            String eUserimg = "";
            try {
                eUserimg = URLDecoder.decode(globalClass.getUser().getImg_user(),"UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/"+ eUserimg;
            Log.e("url",u);



            Picasso.with(profile.this).load(u).into(pImg);
            Picasso.with(profile.this).load(u).into(headerImg);
        }

        TextView txtUsername = findViewById(R.id.txtUsername_profile);
        TextView txtName = findViewById(R.id.txtName_profile);
        TextView txtMail = findViewById(R.id.txtEmail_profile);

        txtUsername.setText("Username : " + globalClass.getUser().getUsername());
        txtName.setText("Name : " + globalClass.getUser().getName());
        txtMail.setText("E-mail : " + globalClass.getUser().getEmail());

        Button btnST = findViewById(R.id.btnSetting);
        btnST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this,setting.class);
                startActivity(intent);
            }
        });

        Button btnuploadimg = findViewById(R.id.btnChangImgProfile);
        btnuploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void chooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture" ), REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                ImageView img = findViewById(R.id.img_profile);
                img.setImageBitmap(bitmap);
                Toast.makeText(profile.this,img+"",Toast.LENGTH_SHORT).show();


                Log.e("upload imgprofile","start upload");
                if (filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(profile.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference ref = storageReference.child("IT411FinalProjectProfile/"+ UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();

                                    GlobalClass globalClass = (GlobalClass) getApplicationContext();

                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    String[] surl = url.split("/");

                                    globalClass.getUser().setImg_user(surl[7]);

                                    UserManager userManager = UserManager.getUserManager(profile.this);
                                    userManager.doUpdateImgUser(globalClass.getUser(), new UserManager.UserManagerListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            Log.e("response",response.toString());
                                        }

                                        @Override
                                        public void onError(String err) {
                                            Log.e("response",err);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure( Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(profile.this,"Upload Fails!",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+ (int) progress +"%");
                                }
                            });
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
