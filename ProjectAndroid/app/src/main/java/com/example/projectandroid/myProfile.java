package com.example.projectandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.ReviewModdel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class myProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ReviewManager manager = ReviewManager.geReviewManager(myProfile.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
            Picasso.with(myProfile.this).load(u).into(imageView);
        }




        manager.doSelectReviewByUsername(globalClass.getUser(), new ReviewManager.ReviewManagerListener() {
            @Override
            public void onComplete(Object response) {
                final LinearLayout  linearLayout = findViewById(R.id.linearlistphoto);

                ReviewModdel re = (ReviewModdel) response;
                List<Review> listreviews = re.getListreview();

                String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";
                int intforremove = 0 ;
                for(final Review r : listreviews){

                    try {
                        String dDes = URLDecoder.decode( r.getDescription(),"UTF-8");
                        String dName = URLDecoder.decode(r.getUser().getName(),"UTF-8");
                        r.setDescription(dDes);
                        r.getUser().setName(dName);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    final String urls = r.getUrl_img();
                    View view = getLayoutInflater().inflate(R.layout.myprofile_listview , null );

                    ImageView imageView = view.findViewById(R.id.imgCover);
                    TextView txtTitle = view.findViewById(R.id.txtTitle);
                    txtTitle.setText(r.getDescription());
                    TextView txtUname = view.findViewById(R.id.txtUname);
                    txtUname.setText(r.getUser().getName());

                    String decode = "";
                    String ed = "";
                    String decodes = "";
                    try{

                        decode = URLDecoder.decode(r.getUrl_img(),"UTF-8");
                        ed = URLDecoder.decode(decode,"UTF-8");
                        decodes = URLDecoder.decode(ed ,"UTF-8");
                        Log.e("decode", decodes);

                    }catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = u + decodes ;
                    Log.e("asds",url);


                    Picasso.with(myProfile.this).load(url).into(imageView);

                    final int idreview = r.getId_review();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(myProfile.this,review_detail.class);
                            intent.putExtra("idreview",idreview+"");
                            startActivity(intent);
                        }
                    });

                    Button btnRemove = view.findViewById(R.id.btnDelete);

                    final int d = intforremove;



                    btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(myProfile.this);
                            builder.setTitle("You Sure Delete");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    linearLayout.removeViewAt(d);

                                    String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";
                                    String decode = "";
                                    String ed = "";
                                   ;
                                    try{
                                        decode = URLDecoder.decode(urls,"UTF-8");
                                        ed = URLDecoder.decode(decode,"UTF-8");

                                        Log.e("asd", ed);

                                    }catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    String url = u + ed ;
                                    Log.e("url remove",url);

                                    doRemovePhotoOnFirebase(url);
                                    doRemoveReviewOnDatabase(idreview);




                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }
                    });

                    linearLayout.addView(view);
                    intforremove++;
                }
            }

            @Override
            public void onError(String err) {

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
        getMenuInflater().inflate(R.menu.my_profile, menu);
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

    public void doRemovePhotoOnFirebase(String url){
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.e("remove","remove success");
            }
        });
    }

    public void doRemoveReviewOnDatabase(int idreview){
        manager.doDeleteReviewFromId(idreview, new ReviewManager.ReviewManagerListener() {
            @Override
            public void onComplete(Object response) {
                Toast.makeText(myProfile.this,"Remove Success",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {

            }
        });

    }


}
