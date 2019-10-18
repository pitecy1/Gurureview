package com.example.projectandroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ReviewModdel {
    Review review;
    List<Review> listreview ;

    public ReviewModdel(){
        review = new Review();
    }

    public ReviewModdel(String jsonResponse){
        Gson gson = new GsonBuilder().create();

        try{
            review = gson.fromJson(jsonResponse , Review.class);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            TypeToken<List<Review>> listTypeToken = new TypeToken<List<Review>>(){};
            listreview = gson.fromJson(jsonResponse,listTypeToken.getType());
        }
    }

    public String toJSONString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.review);
    }

    public List<Review> getListreview() {
        return listreview;
    }

    public void setListreview(List<Review> listreview) {
        this.listreview = listreview;
    }

    public Review getReview() {
        return review;
    }



}
