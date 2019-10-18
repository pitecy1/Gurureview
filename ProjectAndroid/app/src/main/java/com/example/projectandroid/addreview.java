package com.example.projectandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.CategoryManager;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Category;
import com.example.projectandroid.model.CategoryModel;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class addreview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Uri filePath;
    private final int REQ = 4750;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    List<Category> listCat;
    List<String> spnArray = new ArrayList<String>();
    Category category = new Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView header = headerview.findViewById(R.id.header_name);
        TextView email = headerview.findViewById(R.id.header_email);

        GlobalClass globalClass = (GlobalClass) getApplicationContext();
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
            Picasso.with(addreview.this).load(u).into(imageView);
        }

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        Button btnchoose = findViewById(R.id.btnChoose);
        Button btnupload = findViewById(R.id.btnUpload);

        getCategory();

        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");






        ImageView img = findViewById(R.id.imgView);

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImg();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImg();
            }
        });
    }

    private void chooseImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture" ), REQ);
    }

    public void uploadImg(){
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref = storageReference.child("IT411FinalProject/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            //r.setUrl_img(""+surl[7]+"");
                            try {
                                String url = taskSnapshot.getDownloadUrl().toString();
                                Log.e("sds",url);
                                String[] surl = url.split("/");

                                EditText txtphoto = findViewById(R.id.description);

                                GlobalClass globalClass = (GlobalClass) getApplicationContext();
                                Review r = new Review();
                                User user = new User();

                                Spinner spn = findViewById(R.id.spnCategory);
                                String scat = spn.getSelectedItem().toString();



                                Category category = selectCategory(scat);
                                user.setUsername(globalClass.getUser().getUsername());
                                r.setUser(user);
                                r.setCategory(category);
                                r.setViewer(0);
                                String sq = URLEncoder.encode(surl[7],"UTF-8");
                                r.setUrl_img(sq);
                                String eUserdes = URLEncoder.encode( txtphoto.getText().toString() ,"UTF-8");

                                r.setDescription(eUserdes);

                                String f = URLDecoder.decode(sq,"UTF-8");
                                Log.e("fff",sq);
                                Log.e("fff",f);

                                final ProgressDialog progress = ProgressDialog.show(addreview.this, getString(R.string.please_wait),
                                        getString(R.string.please_wait), true);
                                ReviewManager manager = ReviewManager.geReviewManager(addreview.this);
                                manager.doAddReview(r, new ReviewManager.ReviewManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        progress.dismiss();
                                        finish();

                                        Log.e("dsds","add review success");

                                        Intent intent = new Intent(addreview.this,feeds.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(String err) {
                                        progress.dismiss();
                                    }
                                });

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }



                            Toast.makeText(addreview.this,"Uploaded!",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(addreview.this,"Upload Fails!",Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                ImageView img = findViewById(R.id.imgView);
                img.setImageBitmap(bitmap);
                Toast.makeText(addreview.this,img+"",Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
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
        getMenuInflater().inflate(R.menu.addreview, menu);
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

            GlobalClass globalClass = (GlobalClass) getApplicationContext();
            globalClass.setUser(null);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getCategory(){
        CategoryManager categoryManager = CategoryManager.getCategoryManager(addreview.this);
        categoryManager.doGetCategory(new CategoryManager.CategoryManagerListener() {
            @Override
            public void onComplete(Object response) {
                CategoryModel c = (CategoryModel) response;
                List<Category> listString = c.getList();
                Log.e("size category", listString.size()+"");
                listCat = listString;

                Spinner spn = findViewById(R.id.spnCategory);

                for(Category category : listString){
                    spnArray.add(category.getCategory_name());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(addreview.this,android.R.layout.simple_spinner_dropdown_item, spnArray);
                spn.setAdapter(adapter);


            }
            @Override
            public void onError(String err) {
                Log.e("category",err);
            }
        });
    }
    
    public Category selectCategory(String cat){
        Category category = new Category();
        for(Category c : listCat){
            if(cat.equalsIgnoreCase(c.getCategory_name())){
                category = c;
                Log.e("cat","found! category");
                break;
            }
        }
        return category;
    }

}
