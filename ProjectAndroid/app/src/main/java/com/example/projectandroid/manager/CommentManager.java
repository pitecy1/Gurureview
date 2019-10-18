package com.example.projectandroid.manager;

import android.content.Context;
import android.util.Log;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Comment;
import com.example.projectandroid.model.CommentModel;
import com.example.projectandroid.task.WSTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommentManager {

    private  static CommentManager commentManager;
    private Context context;

    public interface CommentManagerListener{
        void onComplete(Object response);
        void onError(String err);
    }

    public CommentManager(Context context){this.context = context;}

    public static CommentManager getCommentManager(Context context){
        if(commentManager == null){
            commentManager = new CommentManager(context);
        }
        return commentManager;
    }

    public void doAddComment(Object object , final CommentManagerListener listener){
        if(!(object instanceof Comment)){
            Log.e("step","return");
            return;
        }
        final Comment comment = (Comment) object;
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(comment);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                  Log.e("comment" , response);
            }

            @Override
            public void onError(String err) {
                Log.e("comment" , err);
            }
        });
        task.execute(context.getString(R.string.addComment),json);
    }

    public void doGetListCommentByIdReview(int idreview , final CommentManagerListener listener){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(idreview);

        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                CommentModel commentModel = new CommentModel(response) ;
                listener.onComplete(commentModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.getListComment),json);
    }


}
