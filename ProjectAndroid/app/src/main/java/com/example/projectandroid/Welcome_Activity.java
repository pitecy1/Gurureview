package com.example.projectandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Category;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

public class Welcome_Activity extends AppCompatActivity {

    private Uri filePath;
    private final int REQ = 4750;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        Button btnchoose = findViewById(R.id.btnChoose);
        Button btnupload = findViewById(R.id.btnUpload);

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

                                Review r = new Review();
                                User user = new User();
                                Category category = new Category();
                                category.setId_category(4);
                                user.setUsername("1234");
                                r.setUser(user);
                                r.setCategory(category);
                                r.setViewer(0);
                                String sq = URLEncoder.encode(surl[7],"UTF-8");
                                r.setUrl_img(sq);
                                r.setDescription("asdaff");

                                String f = URLDecoder.decode(sq,"UTF-8");
                                Log.e("fff",sq);
                                Log.e("fff",f);

                                final ProgressDialog progress = ProgressDialog.show(Welcome_Activity.this, getString(R.string.please_wait),
                                        getString(R.string.please_wait), true);
                                ReviewManager manager = ReviewManager.geReviewManager(Welcome_Activity.this);
                                manager.doAddReview(r, new ReviewManager.ReviewManagerListener() {
                                    @Override
                                    public void onComplete(Object response) {
                                        progress.dismiss();
                                        finish();

                                        Log.e("dsds","add review success");

                                        Intent intent = new Intent(Welcome_Activity.this,MainActivity.class);
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



                            Toast.makeText(Welcome_Activity.this,"Uploaded!",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Welcome_Activity.this,"Upload Fails!",Toast.LENGTH_LONG).show();
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
                Toast.makeText(Welcome_Activity.this,img+"",Toast.LENGTH_SHORT).show();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
