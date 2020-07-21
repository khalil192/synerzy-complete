package com.example.synerzy;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class DownloadClass extends AsyncTask<Void ,Void , Void> {

    ArrayList<String> ipList;
    ArrayList<String> idList;
    String fileName;
    String fileCompleteName;
    Activity activity;
    ImageButton download_button;
    ProgressBar download_bar;
    DownloadClass(Activity acc , String file_name ,ImageButton img_button , ProgressBar p_bar ){
        activity = acc;
        fileName =file_name;
        download_button = img_button;
        download_bar = p_bar;
    }

    String removeExtension(String s) {
        String separator = System.getProperty("file.separator");
        String filename;
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;
        return filename.substring(0, extensionIndex);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        download_button.setVisibility(View.GONE);
        download_bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        System.out.println("syn: filling idList");
        idList = new ArrayList<>();
        ipList = new ArrayList<>();

        fill_id_list();
        while(idList.size() ==0) {

        }
        System.out.println("syn: filling ipList");
        fill_ip_list();
        while(ipList.size() ==0){

        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("ROH1 : file will be downloaded from " + ipList.get(0));
        clientParams clp = new clientParams(activity,ipList.get(0),9996+ Integer.parseInt(idList.get(0))*10,fileCompleteName,download_button , download_bar);
        System.out.println("syn: req from this posrt num" + (9996+ Integer.parseInt(idList.get(0))*10) );
        clp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    void fill_id_list(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dr = db.getReference("file_id_list/" + fileName);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println("ROH1 : id with " + fileName + " " + ds.getKey() + " loc : " + ds.getValue());

                    fileCompleteName = ds.getValue().toString();
                    fileCompleteName = fileCompleteName.substring(fileCompleteName.lastIndexOf(File.separator)+1 , fileCompleteName.length());
                    System.out.println("ROH1 : filename is + "+ fileCompleteName);
                    idList.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    void fill_ip_list(){
        for(final String id : idList){
            System.out.println("ROH1 : id is" + id);
            DatabaseReference id_ip_ref  = FirebaseDatabase.getInstance().getReference("id_ip_list/");
            id_ip_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    System.out.println("ROH1: ip is" + dataSnapshot.child(id).getValue());
                    ipList.add(dataSnapshot.child(id).getValue().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
