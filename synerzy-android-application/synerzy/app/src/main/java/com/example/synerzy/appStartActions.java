package com.example.synerzy;

import android.app.Activity;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;



public class appStartActions extends AsyncTask<Void , Void , Void>{
    static String CURRENT_ID;
    Activity activity;
    String IpAddress;
    static int CURR_ID_INT;
    appStartActions(Activity activity, String ipAddress ){
        this.activity = activity;
        this.IpAddress = ipAddress;
        CURR_ID_INT = -1;
    }
    String returnType(String fileName){
        return "video";
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
    protected Void doInBackground(Void... voids) {

        final File idFile = new File(activity.getExternalFilesDir("developer"),"idFile.txt");
        FirebaseDatabase fbase = FirebaseDatabase.getInstance();
        final DatabaseReference idRef = fbase.getReference("id_ip_list");
        if(idFile.exists() == false){
            System.out.println("ROH : the file doesn't exist");
            try {
                System.out.println("ROH : came here");
                if (idFile.createNewFile()) {
                    System.out.println("ROH : File created: " + idFile.getName());
                }
                final FileWriter fw = new FileWriter(idFile);

                System.out.println("ROH : and here");
                idRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        System.out.println("ROH : " + count + " is this");

                        try {
                            FileOutputStream fos = new FileOutputStream(idFile);
                            fos.write(Long.toString(count).getBytes());
                            System.out.println("ROH : " + count + " was written into file");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("ROH :  the file Exists ");
            try {
                System.out.println("ROH :  reading the file");
                FileInputStream fin = new FileInputStream(idFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                String Id = reader.readLine();
                System.out.println("ROH : id = " + Id);
                appStartActions.CURRENT_ID = Id;
                idRef.child(Id).setValue(IpAddress);
                System.out.println("ROH : Id = " + Id + " " + " ip = " + IpAddress);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                File dir = new File(activity.getExternalFilesDir(""), "mediaFiles");
                File[] directoryListing = dir.listFiles();
                String CURRENT_ID = Id;
                CURR_ID_INT = Integer.parseInt(Id);
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        System.out.println("ROH : uploading " + child.getName());
                        // Do something with child
                        String file_name = removeExtension(child.getName());
                        DatabaseReference myRef = database.getReference("central_list/" + file_name);
                        myRef.setValue(returnType(child.getName()));
                        database.getReference("file_id_list/" + file_name + "/" + CURRENT_ID).setValue("mediaFiles/" + child.getName());

                    }
                }
                System.out.println("syn: opening from  port" + (9996+ (CURR_ID_INT)*10) + "no loop here though");
                serverParams sp = new serverParams(activity,CURR_ID_INT*10 + 9996);
                sp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
}






/*
class appIdUpdate extends AsyncTask<Void, Void, Void> {
    String IpAddress;
    Activity activity;

    @Override
    protected Void doInBackground(Void... voids) {
        final File idFile = new File(activity.getExternalFilesDir("developer"),"idFile.txt");
        FirebaseDatabase fbase = FirebaseDatabase.getInstance();
        final DatabaseReference idRef = fbase.getReference("id_ip_list");
        if(idFile.exists() == false){
            System.out.println("ROH : the file doesn't exist");
            try {
                System.out.println("ROH : came here");
                if (idFile.createNewFile()) {
                    System.out.println("ROH : File created: " + idFile.getName());
                }
                final FileWriter fw = new FileWriter(idFile);

                System.out.println("ROH : and here");
                idRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long count = dataSnapshot.getChildrenCount();
                        System.out.println("ROH : " + count + " is this");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileOutputStream fos = new FileOutputStream(idFile);
                            fos.write(Long.toString(count).getBytes());
                            System.out.println("ROH : " + count + " was written into file");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("ROH :  the file Exists ");
            try{
                System.out.println("ROH :  reading the file");
                FileInputStream fin=new FileInputStream(idFile);
                BufferedReader reader =new BufferedReader(new InputStreamReader(fin));
                String Id = reader.readLine();
                System.out.println("ROH : id = " +Id);
                appStartActions.CURRENT_ID = Id;
                idRef.child(Id).setValue(IpAddress);
                System.out.println("ROH : Id = " + Id + " "+ " ip = " + IpAddress);
            }catch(Exception e){System.out.println(e);}
        }

        return null;
    }
}


class appFilesUpdate extends AsyncTask<Void,Void,Void>{
    Activity activity;

    String returnType(String fileName){
        return "video";
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
    protected Void doInBackground(Void... voids) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        File dir = new File(activity.getExternalFilesDir(""),"mediaFiles");
        File[] directoryListing = dir.listFiles();
        String CURRENT_ID =appStartActions.CURRENT_ID;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                System.out.println("ROH : uploading "+ child.getName());
                // Do something with child
                String file_name = removeExtension(child.getName());
                DatabaseReference myRef = database.getReference("central_list/" + file_name);
                myRef.setValue(returnType(child.getName()));
                database.getReference("file_id_list/" + file_name + "/" + CURRENT_ID).setValue("mediaFiles/" + child.getName());
            }
        }
        return null;
    }
}



*/