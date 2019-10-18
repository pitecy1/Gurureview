package com.example.projectandroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CategoryModel {
    Category category;
    List<Category> list ;

    public CategoryModel() {
        category = new Category();
    }

    public CategoryModel(String jsonResponse){
        Gson gson = new GsonBuilder().create();
        try{
            category = gson.fromJson(jsonResponse , Category.class);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            TypeToken<List<Category>> listTypeToken = new TypeToken<List<Category>>(){};
            list = gson.fromJson(jsonResponse,listTypeToken.getType());
        }
    }

    public String toJSONString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.category);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getList() {
        return list;
    }

    public void setList(List<Category> list) {
        this.list = list;
    }

    public Category getCategory() {
        return category;
    }

}
