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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.ReportManager;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Report;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.User;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class report extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerview = navigationView.getHeaderView(0);
        TextView header = headerview.findViewById(R.id.header_name);
        TextView email = headerview.findViewById(R.id.header_email);

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
            Picasso.with(report.this).load(u).into(imageView);
        }

        String idr = getIntent().getStringExtra("idreview");
        int idreview = Integer.parseInt(idr);

        ReviewManager manager = ReviewManager.geReviewManager(report.this);
        manager.doSelectReviewByIdReview(idreview, new ReviewManager.ReviewManagerListener() {
            @Override
            public void onComplete(Object response) {
                Review review = (Review) response;

                String d,c ="";
                try {
                    d = URLDecoder.decode(review.getUrl_img(),"UTF-8");
                    c = URLDecoder.decode(d,"UTF-8");
                    String dDes = URLDecoder.decode( review.getDescription() ,"UTF-8");
                    review.setDescription(dDes);
                    String dName = URLDecoder.decode(review.getUser().getName(),"UTF-8");
                    review.getUser().setName(dName);

                    String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";
                    String url = u + c;

                    ImageView img = findViewById(R.id.reportimg);

                    Picasso.with(report.this).load(url).into(img);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Spinner spnreport = findViewById(R.id.spnReport);
                Report report = new Report();
                report.setReport_content(spnreport.getSelectedItem().toString());
                report.setReview(review);
                report.setUser(globalClass.getUser());

                final Report rp = report;

                Button btnreport = findViewById(R.id.btnReport);
                btnreport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savereport(rp);
                    }
                });



            }



            @Override
            public void onError(String err) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report, menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void savereport(Report report) {

        Report r = report;
        GlobalClass globalClass = (GlobalClass)getApplicationContext();
        User user = globalClass.getUser();
        try {
            String rpDes = URLEncoder.encode(r.getReport_content(),"UTF-8");
            String rDes = URLEncoder.encode(r.getReview().getDescription(),"UTF-8");
            String rImg = URLEncoder.encode(r.getReview().getUrl_img(),"UTF-8");
            String ruName = URLEncoder.encode(r.getReview().getUser().getName(),"UTF-8");
            if(r.getReview().getUser().getImg_user() != null){
                String ruImg = URLEncoder.encode(r.getReview().getUser().getImg_user(),"UTF-8");
                r.getReview().getUser().setImg_user(ruImg);
            }

            String uName = URLEncoder.encode(user.getName(),"UTF-8");
            if(globalClass.getUser().getImg_user() != null){
                String u = URLEncoder.encode(user.getImg_user(),"UTF-8");
                user.setImg_user(u);
            }
            user.setName(uName);

            r.setReport_content(rpDes);
            r.getReview().setDescription(rDes);
            r.getReview().setUrl_img(rImg);
            r.getUser().setName(ruName);
            r.setUser(user);
            doTakerReport(r);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void doTakerReport(Report r) {
        ReportManager manager = ReportManager.getLoginManager(report.this);
        manager.doTakerReport(r, new ReportManager.ReportManagerListener() {
            @Override
            public void onComplete(Object response) {
                Log.e("report",response.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(report.this);
                builder.setTitle("Success");
                builder.setIcon(R.drawable.complete);
                builder.setMessage("\n Report Success!");
                builder.create();
                builder.show();
                Intent intent = new Intent(report.this,feeds.class);
                startActivity(intent);

            }

            @Override
            public void onError(String err) {
                Log.e("report",err.toString());

            }
        });

    }
}
