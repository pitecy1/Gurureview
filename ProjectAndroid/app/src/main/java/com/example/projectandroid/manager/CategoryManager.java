package com.example.projectandroid.manager;

import android.content.Context;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Category;
import com.example.projectandroid.model.CategoryModel;
import com.example.projectandroid.task.WSTask;

public class CategoryManager {
    private static CategoryManager categoryManager;
    private Context context;

    public interface CategoryManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public CategoryManager (Context context){this.context = context;}

    public static CategoryManager getCategoryManager(Context context){
        if(categoryManager == null){
            categoryManager = new CategoryManager(context);
        }
        return  categoryManager;
    }

    public void doGetCategory(final CategoryManagerListener listener){
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                CategoryModel categoryModel = new CategoryModel(response);
                listener.onComplete(categoryModel);
            }
            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.getCategory));
    }

}
