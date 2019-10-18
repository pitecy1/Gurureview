package com.example.projectandroid;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.projectandroid.global.GlobalClass;
import com.example.projectandroid.manager.CommentManager;
import com.example.projectandroid.manager.LikedManager;
import com.example.projectandroid.manager.ReviewManager;
import com.example.projectandroid.model.Comment;
import com.example.projectandroid.model.CommentModel;
import com.example.projectandroid.model.Review;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

public class review_detail extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ReviewManager manager = ReviewManager.geReviewManager(review_detail.this);
    LikedManager likedManager = LikedManager.getLikedManager(review_detail.this);
    CommentManager commentManager = CommentManager.getCommentManager(review_detail.this);

    Review r ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
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


        final GlobalClass globalClass = (GlobalClass) getApplicationContext();
        header.setText(globalClass.getUser().getName());
        email.setText(globalClass.getUser().getEmail());



        final int id_review = Integer.parseInt(getIntent().getStringExtra("idreview"));

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
            Picasso.with(review_detail.this).load(u).into(imageView);
        }
        checkLiked(id_review , globalClass.getUser().getUsername());

        getCount(id_review);

        selectReview(id_review);

        doGetListCommentByIdReview(id_review);

        Button btnLike = findViewById(R.id.btnLiked_button);

        final int idreview = id_review;
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass g = (GlobalClass) getApplicationContext();

                checkLiked2(id_review,g.getUser().getUsername());

            }
        });

        Button btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText txtComment = findViewById(R.id.txtComment);

                Comment com = new Comment();
                com.setContent_commemt(txtComment.getText().toString());
                com.setReview(r);
                com.setUser(globalClass.getUser());
                Log.e("sd",com.toString());

                doAddComment(com);
                doGetListCommentByIdReview(id_review);




            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(review_detail.this,report.class);
                intent.putExtra("idreview",id_review+"");
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.review_detail, menu);
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


    public void checkLiked(int id , String username){
        likedManager.doCheckLiked(id, username, new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {

                Button btnLiked = findViewById(R.id.btnLiked_button);
                if( "nolike".equalsIgnoreCase(response.toString().toLowerCase()) ){
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_border_black_unlike_24dp);
                }
                else if("liked".equalsIgnoreCase(response.toString().toLowerCase())){
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_red_liked_24dp);
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void checkLiked2(final int id , final String username){
        likedManager.doCheckLiked(id, username, new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {

                if( "nolike".equalsIgnoreCase(response.toString().toLowerCase()) ){
                    dolike(id, username);
                   getCount(id);
                    Button btnLiked = findViewById(R.id.btnLiked_button);
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_red_liked_24dp);
                }
                else if("liked".equalsIgnoreCase(response.toString().toLowerCase())){
                    doUnlike(id,username);
                    getCount(id);
                    Button btnLiked = findViewById(R.id.btnLiked_button);
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_border_black_unlike_24dp);
                }
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void getCount(int id_review){
        likedManager.doGetCount(id_review, new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {
                TextView txtLiked = findViewById(R.id.txtLiked);
                txtLiked.setText(response.toString());
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void selectReview(int id_review){
        manager.doSelectReviewByIdReview(id_review, new ReviewManager.ReviewManagerListener() {
            @Override
            public void onComplete(Object response) {

                Review review = (Review) response;
                r = review;

                String d,c ="";
                try {
                    d = URLDecoder.decode(review.getUrl_img(),"UTF-8");
                    c = URLDecoder.decode(d,"UTF-8");
                    String dDes = URLDecoder.decode( review.getDescription() ,"UTF-8");
                    review.setDescription(dDes);
                    String dName = URLDecoder.decode(review.getUser().getName(),"UTF-8");
                    review.getUser().setName(dName);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                ImageView img = findViewById(R.id.img_details);
                String u = "https://firebasestorage.googleapis.com/v0/b/it411fianl.appspot.com/o/";
                String url = u + c;
                Picasso.with(review_detail.this).load(url).into(img);

                TextView txtViewer = findViewById(R.id.txtViewer);
                TextView txtTitle = findViewById(R.id.txtCTR_description);


                txtViewer.setText(review.getViewer()+"");
                txtTitle.setText(review.getDescription());


            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void likeAndUnlike(int idreview){

        GlobalClass g = (GlobalClass) getApplicationContext();
        likedManager.doSelectCountLiked(idreview, g.getUser().getUsername(), new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {
                TextView txtLiked = findViewById(R.id.txtLiked);
                txtLiked.setText(response.toString()+"");

                Button btnLiked = findViewById(R.id.btnLiked_button);
                if("1.0".equalsIgnoreCase(response.toString())){
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_red_liked_24dp);
                }else{
                    btnLiked.setBackgroundResource(R.drawable.ic_favorite_border_black_unlike_24dp);
                }

            }

            @Override
            public void onError(String err) {

            }
        });
    }

    public void dolike(int id ,String username){
        likedManager.doLiked(id, username, new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {
                Log.e("res like" , "liked");
            }

            @Override
            public void onError(String err) {
                Log.e("res unlike" , "unLiked");
            }
        });


    }

    public void doUnlike(int id ,String username){
        likedManager.doUnlike(id, username, new LikedManager.LikedManagerListener() {
            @Override
            public void onComplete(Object response) {
                Log.e("res unLiked" , "unLiked");
            }

            @Override
            public void onError(String err) {
                Log.e("res unlike" , "unLiked");
            }
        });
    }
    public void doGetListCommentByIdReview(int idreview){
        commentManager.doGetListCommentByIdReview(idreview, new CommentManager.CommentManagerListener() {
            @Override
            public void onComplete(Object response) {
                CommentModel commentModel = (CommentModel) response;
                List<Comment> list = commentModel.getListcomment();
                Log.e("comment" , "getListComment");
                LinearLayout linear = findViewById(R.id.linear_comment);
                linear.removeAllViews();


                for(int i = list.size()-1; i >= 0 ; i--){
                    TextView comment = new TextView(review_detail.this);

                    String date = "" ;
                    String cm = "";
                    try {
                        date = URLDecoder.decode( list.get(i).getCommemt_date() ,"UTF-8");
                        cm = URLDecoder.decode( list.get(i).getContent_commemt(),"UTF-8");

                        String dName = URLDecoder.decode( list.get(i).getUser().getName() ,"UTF-8");
                        list.get(i).getUser().setName(dName);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    comment.setText(list.get(i).getUser().getName()+" : "+   cm + "  " + date);

                    linear.addView(comment);
                }
            }

            @Override
            public void onError(String err) {
                Log.e("error","getListComment error");
            }
        });
    }


    public void doAddComment(Comment comment){
        commentManager.doAddComment(comment, new CommentManager.CommentManagerListener() {
            @Override
            public void onComplete(Object response) {
                Log.e("comment" , "addcomment success");
            }

            @Override
            public void onError(String err) {

            }
        });
    }



}
