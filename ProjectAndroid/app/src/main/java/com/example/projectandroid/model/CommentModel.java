package com.example.projectandroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommentModel {
    Comment comment;
    List<Comment> listcomment;

    public CommentModel(){comment = new Comment();}

    public CommentModel(String jsonResponse){
        Gson gson = new GsonBuilder().create();

        try{
            comment = gson.fromJson(jsonResponse , Comment.class);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            TypeToken<List<Comment>> listTypeToken = new TypeToken<List<Comment>>(){};
            listcomment = gson.fromJson(jsonResponse,listTypeToken.getType());
        }
    }

    public String toJSONString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.comment);
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<Comment> getListcomment() {
        return listcomment;
    }

    public void setListcomment(List<Comment> listcomment) {
        this.listcomment = listcomment;
    }
}
