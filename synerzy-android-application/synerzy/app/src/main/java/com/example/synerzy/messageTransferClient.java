package com.example.synerzy;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


class clientParams extends AsyncTask<Void, Integer, Void>{

    String ipAddress;
    int portNum;
    String fileId;
    Activity activity;
    static String responseToReceive;
    static clientClass.byteClass byteObjReceived;
    ImageButton download_button;
    ProgressBar download_bar;
    clientParams( Activity ac,  String ip , int port ,String fid,ImageButton img_button , ProgressBar p_bar){
        this.activity = ac;
        this.ipAddress = ip;
        this.portNum = port;
        this.fileId = fid;
        responseToReceive = "-2";
        download_button = img_button;
        download_bar = p_bar;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("syn: socket at " +  (portNum) + "message " + fileId + "is sent : " + ipAddress);
        clientClass.byteClass.sendMessageClient(ipAddress,portNum,fileId);

        System.out.println("syn: socket at " +  (portNum+1) + "waiting...");

        responseToReceive =clientClass.byteClass.getMessageClient(ipAddress,portNum+1);
        System.out.println("syn: message received yet " + responseToReceive);

        System.out.println("syn: socket at " +  (portNum+2));
        byteObjReceived =clientClass.byteClass.getFile(ipAddress,portNum+2,fileId);
        System.out.println("syn: byte obj rec" + byteObjReceived.actualLength + " "+portNum+2);
        System.out.println("syn: file is received" + byteObjReceived.actualLength);
        saveFileFromByte();
        System.out.println("syn: process is done");


        return null;
    }

    public void saveFileFromByte(){
        File myExternalFile = new File(activity.getExternalFilesDir("mediaFiles"),fileId);
        try {
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            fos.write(byteObjReceived.byteArray,0,byteObjReceived.actualLength);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("syn: the complete process is done");
        download_button.setVisibility(View.VISIBLE);
        download_bar.setVisibility(View.GONE);
        download_button.setImageResource(R.drawable.ic_done_black_24dp);
    }
}


class messageSendClient extends AsyncTask<Void, Void,Void> {
    String ipAddress ;
    int portNum;
    String messageToSend = "duplicate message";
    Activity activity;
    messageSendClient(String ipAddress , int portNum,String messaage,Activity acc){
        this.ipAddress = ipAddress ;
        this.portNum = portNum;
        messageToSend = messaage;
        activity = acc;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        int c = 1;
        while(c==1) {
            try {
                clientClass.byteClass.sendMessageClient(ipAddress, portNum, messageToSend);
                c=0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        messageReceiveClient mrc = new messageReceiveClient(ipAddress , portNum+1,activity);
//        mrc.execute();
    }

}

class messageReceiveClient extends AsyncTask<Void, Void,Void> {
    String ipAddress ;
    int portNum;
    String messageToReceive = "duplicate message";
    Activity activity;

    messageReceiveClient(String ipAddress , int portNum,Activity acc){
        this.ipAddress = ipAddress ;
        this.portNum = portNum;
        activity = acc;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        int c = 1;
        while(c==1) {
            try {
                messageToReceive = clientClass.byteClass.getMessageClient(ipAddress, portNum);
                if(messageToReceive.isEmpty() == false){
                    c = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("syn: messfor"+messageToReceive);
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        TextView textView = activity.findViewById(R.id.messageView);
//        textView.setText(messageToReceive);
    }
}

class fileReceiveClient extends AsyncTask<Void, Void,Void> {
    String ipAddress ;
    int portNum;
    String fileName = " ";
    Activity activity;
    static clientClass.byteClass globalObj;
    static int actualLength;
    fileReceiveClient(String ipAddress , int portNum,String fileName,Activity acc){
        this.ipAddress = ipAddress ;
        this.portNum = portNum;
        fileName = fileName;
        activity = acc;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        int c = 1;
        while(c==1) {
            try {
                globalObj =clientClass.byteClass.getFile(ipAddress,portNum,fileName);
                if(globalObj.actualLength != 0){
                    c = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

}

