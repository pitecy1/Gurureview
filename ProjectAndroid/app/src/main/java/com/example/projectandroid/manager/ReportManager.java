package com.example.projectandroid.manager;

import android.content.Context;
import android.util.Log;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Report;
import com.example.projectandroid.task.WSTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReportManager {
    private static ReportManager reportManager;
    private Context context;

    public interface ReportManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public ReportManager(Context context){this.context = context;}

    public static ReportManager getLoginManager(Context context){
        if(reportManager == null) {
            reportManager = new ReportManager(context);
        }
        return reportManager;
    }

    public void doTakerReport(Report report , final ReportManagerListener listener){
        if(!(report instanceof Report)){
            return;
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(report);
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
        task.execute(context.getString(R.string.takerReport),json);

    }
}
