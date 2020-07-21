package com.example.synerzy;


import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;





class serverParams extends AsyncTask<Void,Integer,Void> {

    Activity activity;
    int messageReceivePortNumber;
    File externalFile;
    public static byte[] mybyteArray;
    static String messageReceived = "sample.mp4";
    String sentMessage = "9999";

    serverParams(Activity ac , int mspn){
        activity = ac;
        messageReceivePortNumber = mspn;
        sentMessage = Integer.toString(mspn + 2);
        System.out.println("syn: sentMessage = " + sentMessage);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        System.out.println("syn: serverParams will start");
        receiveMessageServer();
        System.out.println("syn: messageGot" + messageReceived);

        System.out.println("syn: rec completed");
        sendMessageServer();
        System.out.println("syn: tot completed");
        createByteFromFile();
        System.out.println("syn: file is prepared ");

        sendByteServer();
        System.out.println("syn: file is sent completed");

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        System.out.println("syn: opening from  port" + (messageReceivePortNumber) + "no loop here though");
        serverParams sp = new serverParams(activity,messageReceivePortNumber);
        sp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void receiveMessageServer(){
        System.out.println("syn: rec in");
        System.out.println("syn: socket at " +  (messageReceivePortNumber));
        String messageToGet = serverClass.byteClass.getMessageServer(messageReceivePortNumber);
        System.out.println("syn: " + messageToGet);
        messageReceived = messageToGet;
        System.out.println("syn: rec out");
    }
    public void sendMessageServer(){
        System.out.println("syn: send in");
        System.out.println("syn: socket at " +  (messageReceivePortNumber+1));
        serverClass.byteClass.sendMessageServer(messageReceivePortNumber+1 ,sentMessage);
        System.out.println("syn: send out message + " + sentMessage);
    }
    public void sendByteServer(){
//        messageView.setText("the byte transfer is initiated");
        System.out.println("syn: socket at " +  (messageReceivePortNumber+2));
        serverClass.byteClass byteHere = new serverClass.byteClass(mybyteArray , mybyteArray.length);
        System.out.println("syn: byteHEre" +mybyteArray.length + "sent message = " + sentMessage);

        serverClass.byteClass.sendByte(byteHere,Integer.parseInt(sentMessage));
        System.out.println("syn: byteHEre is sent" +mybyteArray.length);

    }
    public void createByteFromFile(){
        externalFile = new File(activity.getExternalFilesDir("mediaFiles") ,messageReceived);
        System.out.println("syn: file is " + messageReceived);
        try {
            mybyteArray = new byte[(int)externalFile.length()];
            FileInputStream fis = new FileInputStream(externalFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybyteArray , 0,mybyteArray.length);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
class receiveMessageServer extends AsyncTask<Void, Void,Void>{
    int portNum;
    Activity activity;
    static String messageReceived = "not received yet";

    receiveMessageServer(Activity acc, int portNum){
        this.activity = acc;
        this.portNum = portNum;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        String messageToGet = serverClass.byteClass.getMessageServer(portNum);
        System.out.println("syn: " + messageToGet);
        messageReceived = messageToGet;
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

class messageSendServer extends AsyncTask<Void , Void , Void>{

    Activity activity;
    int portNum;
    String messageToSend;
    messageSendServer(Activity acc , int num,String messageToSend) {
        this.activity = acc;
        this.portNum = num;
        this.messageToSend = messageToSend;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        serverClass.byteClass.sendMessageServer(portNum , messageToSend);
        return null;

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("syn: the message : " +messageToSend +" is sent");
    }

}

class byteSendServer extends AsyncTask<Void , Void , Void>{

    Activity activity;
    byte[] byteArrayTosend;
    int portNum;
    byteSendServer(Activity acc , byte[] byteArray , int portNum){
        this.activity = acc;
        this.byteArrayTosend = byteArray;
        this.portNum = portNum;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        serverClass.byteClass byteHere = new serverClass.byteClass(byteArrayTosend , byteArrayTosend.length);
        serverClass.byteClass.sendByte(byteHere,portNum);
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("syn: the file :  is sent");
    }

}

*/

