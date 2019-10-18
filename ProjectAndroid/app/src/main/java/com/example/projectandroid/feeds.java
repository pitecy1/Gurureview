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

public class feeds extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feeds);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
            Picasso.with(feeds.this).load(u).into(imageView);
        }

        ReviewManager manager = ReviewManager.geReviewManager(feeds.this);
        manager.doSelectAllReview(new ReviewManager.ReviewManagerListener() {
            @Override
            public void onComplete(Object response) {
                LinearLayout linearLayout = findViewById(R.id.linear_feeds);

                ReviewModdel re = (ReviewModdel) response;
                List<Review> listreviews = re.getListreview();

                String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";

                for(int i = listreviews.size()-1 ; i >= 0 ; i--){
                    try {
                        String dDes = URLDecoder.decode( listreviews.get(i).getDescription() ,"UTF-8");
                        listreviews.get(i).setDescription(dDes);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    View view = getLayoutInflater().inflate(R.layout.feeds_layout , null );

                    ImageView imageView = view.findViewById(R.id.imgCover);
                    TextView txtTitle = view.findViewById(R.id.txtDescrpition);
                    txtTitle.setText(listreviews.get(i).getDescription());


                    String decode = "";
                    String ed = "";
                    try{
                        decode = URLDecoder.decode(listreviews.get(i).getUrl_img(),"UTF-8");
                        ed = URLDecoder.decode(decode,"UTF-8");
                        Log.e("asd", ed);

                    }catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = u + ed ;
                    Log.e("feeds url",url);
                    Picasso.with(feeds.this).load(url).into(imageView);
                    final int idreview = listreviews.get(i).getId_review();
                    final int viewer = listreviews.get(i).getViewer();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view ) {
                            Intent intent = new Intent(feeds.this,review_detail.class);
                            intent.putExtra("idreview", idreview +"");
                            intent.putExtra("viewer",viewer+"");
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(view);
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
        getMenuInflater().inflate(R.menu.feeds, menu);
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
}
