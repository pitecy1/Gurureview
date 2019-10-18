package com.example.projectandroid;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.ReviewModdel;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class Search extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
            Picasso.with(Search.this).load(u).into(imageView);
        }



        Button btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText txtSearch = findViewById(R.id.txtSearch);

                String ss = txtSearch.getText().toString()+"";
                Log.e("sd",ss+"");

                final String g = ss;

                ReviewManager manager = ReviewManager.geReviewManager(Search.this);
                manager.doSearchReview(ss,new ReviewManager.ReviewManagerListener() {
                    @Override
                    public void onComplete(Object response) {


                        Log.e("log",response.toString());
                        ReviewModdel re = (ReviewModdel) response;
                        List<Review> listreviews = re.getListreview();



                        Log.e("size" , listreviews.size()+"");

                        LinearLayout linearLayout  = findViewById(R.id.linearSearchs);

                        linearLayout.removeAllViews();

                        for(Review rs : listreviews) {

                            final int idreview = rs.getId_review();

                            try {
                                String dDess = URLDecoder.decode(rs.getDescription(),"UTF-8");
                                rs.setDescription(dDess);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            View views = getLayoutInflater().inflate(R.layout.feeds_layout, null);

                            ImageView imgC = views.findViewById(R.id.imgCover);
                            TextView txtD = views.findViewById(R.id.txtDescrpition);
                            String uu = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";
                            String df = "";

                            txtD.setText(rs.getDescription());
                            String uDecode = "";
                            try {
                                String uDcodeStep1 = URLDecoder.decode( rs.getUrl_img() ,"UTF-8");
                                uDecode = URLDecoder.decode( uDcodeStep1 ,"UTF-8");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            String urlSuccess = uu+uDecode;

                            Picasso.with(Search.this).load(urlSuccess).into(imgC);


                            imgC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Search.this,review_detail.class);

                                    intent.putExtra("idreview",idreview+"");
                                    startActivity(intent);
                                }
                            });
                            linearLayout.addView(views);


                        }


//                            if(g.contains(rs.getDescription())){
//                                try {
//                                    String dcode = URLDecoder.decode(rs.getDescription(),"UTF-8");
//                                    txtD.setText(dcode);
//                                    String urldcode = URLDecoder.decode(rs.getUrl_img(),"UTF-8");
//                                    Log.e("sdsd",urldcode);
//                                    df = URLDecoder.decode(urldcode,"UTF-8");
//
//                                    String useurl = uu + df ;
//                                    Log.e("ule",useurl);
//                                    Picasso.with(Search.this).load(useurl).into(imgC);
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }

                    }

                    @Override
                    public void onError(String err) {
                        Log.e("sdds",err);
                    }
                });





                //searchReview(txtSearch.getText().toString());

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
        getMenuInflater().inflate(R.menu.search, menu);
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

//    public void searchReview(String search){
//        ReviewManager manager = ReviewManager.geReviewManager(Search.this);
//
//        manager.doSearchReview(search, new ReviewManager.ReviewManagerListener() {
//            @Override
//            public void onComplete(Object response) {
//                Log.e("c","search success!");
//            }
//
//            @Override
//            public void onError(String err) {
//                Log.e("s","search error");
//            }
//        });
//
//    }

}
