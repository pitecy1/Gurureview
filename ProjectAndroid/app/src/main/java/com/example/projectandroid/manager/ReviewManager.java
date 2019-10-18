package com.example.projectandroid.manager;

import android.content.Context;
import android.util.Log;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.model.ReviewModdel;
import com.example.projectandroid.model.User;
import com.example.projectandroid.model.UserModel;
import com.example.projectandroid.task.WSTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;

public class ReviewManager {
    private static ReviewManager reviewManager;
    private Context context;

    public interface ReviewManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public ReviewManager(Context context){this.context = context;}

    public static ReviewManager geReviewManager(Context context){
        if(reviewManager == null) {
            reviewManager = new ReviewManager(context);
        }
        return reviewManager;
    }

    public void doAddReview(Object object, final ReviewManagerListener listener){
        if(!(object instanceof Review)){
            Log.e("step","return");
            return;
        }
        final Review review = (Review) object;
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(review);
        WSTask task  = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Review reviews;
                Gson gson = new GsonBuilder().create();
                Log.e("step","chang gson to class");
                reviews = gson.fromJson(response , Review.class);

                try {
                    String dDes = URLDecoder.decode(reviews.getDescription(),"UTF-8");
                    reviews.setDescription(dDes);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                listener.onComplete(reviews);
                Log.e("step","chang success");
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.addreview_url),s);
    }

    public void doSelectAllReview(final ReviewManagerListener listener){
       WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
           @Override
           public void onComplete(String response) {
               ReviewModdel reviewModdel = new ReviewModdel(response);
               listener.onComplete(reviewModdel);

           }
           @Override
           public void onError(String err) {
                listener.onError(err);
           }
       });
        task.execute(context.getString(R.string.getAllreview_url));
    }

    public void doSearchReview(String search , final ReviewManagerListener listener){
        Gson gson = new GsonBuilder().create();
        String encode = "";
        try {
            encode = URLEncoder.encode(search,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String json = gson.toJson(encode);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                ReviewModdel review = new ReviewModdel(response);
                listener.onComplete(review);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.searchReview),json);
    }


    public void doSelectReviewByUsername(Object object ,final ReviewManagerListener listener ){
        if(!(object instanceof User)){
            Log.e("step","return");
            return;
        }
        final User user = (User) object;
        Gson gson = new GsonBuilder().create();
        String JSONUSer = gson.toJson(user);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                ReviewModdel reviewModdel = new ReviewModdel(response);
                listener.onComplete(reviewModdel);

            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.getReviewByUsername),JSONUSer);
    }




    public void doSelectReviewByIdReview(int id_review , final ReviewManagerListener listener){
        Gson gson = new GsonBuilder().create();
        String JSONIdReview = gson.toJson(id_review);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Review reviews;
                Gson gson = new GsonBuilder().create();
                Log.e("step","chang gson to class");
                reviews = gson.fromJson(response , Review.class);

                listener.onComplete(reviews);
            }

            @Override
            public void onError(String err) {

            }
        });
        task.execute(context.getString(R.string.getReviewByIdReview),JSONIdReview);
    }

    public void doDeleteReviewFromId(int id , final ReviewManagerListener listener){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(id);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.deleteReviewOnId),json);
    }




}
