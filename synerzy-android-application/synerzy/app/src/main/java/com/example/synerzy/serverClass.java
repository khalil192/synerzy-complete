package com.example.synerzy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import android.os.AsyncTask;

public class serverClass {

    static class byteClass{
        byte[] byteArray;
        int actualLength;
        byteClass(byte ByteArray[],int ActualLength){
            byteArray = ByteArray;
            actualLength = ActualLength;
        }
        public static byteClass getByteFromString(String str){
            byte[] byteString = str.getBytes();
            return new byteClass(byteString,byteString.length);
        }
        public static String getStringFrombyteClass(byteClass stringByte){
            byte[] actualByte  = stringByte.byteArray;
            actualByte = Arrays.copyOfRange(stringByte.byteArray, 0, stringByte.actualLength);
            String str = new String(actualByte);
            return str;
        }
        public static String getMessageServer(int socketPort){
            System.out.println("syn: socket Port from server : " + socketPort + "is open");
            String ans = "-1";
            try{
                byteClass receivedByte = byteClass.getByteClassServer(socketPort);
                return getStringFrombyteClass(receivedByte);
            }catch (Exception e){
                e.printStackTrace();
            }
            return ans;
        }
        public static byteClass joinByteClass(byteClass byte1,byteClass byte2){
            byte[] joinedArray = new byte[byte1.actualLength + byte2.actualLength];
            int last = 0;
            for(int i=0;i<byte1.actualLength;i++){
                joinedArray[last++] = byte1.byteArray[i];
            }
            for(int i=0;i<byte2.actualLength;i++){
                joinedArray[last++] = byte2.byteArray[i];
            }
            byteClass joinedByte = new byteClass(joinedArray,byte1.actualLength+byte2.actualLength);
            return joinedByte;
        }

        public static byteClass getByteClassServer(int socket_port){
            byte[] receivedByteArray = new byte[1];
            int current = 0 , c=1;
            while(c == 1) {
                try {
                    System.out.println("syn: waiting");
                    ServerSocket serverSocket = new ServerSocket(socket_port);
                    Socket socket = serverSocket.accept();
                    c =0;
                    int bytesRead;
                    try {
                        FileOutputStream fos = null;
                        BufferedOutputStream bos = null;
                        System.out.println("Connecting...");
                        int FILE_SIZE = 6033596;
                        receivedByteArray = new byte[FILE_SIZE];
                        InputStream is = socket.getInputStream();
                        bytesRead = is.read(receivedByteArray, 0, receivedByteArray.length);
                        current = bytesRead;
                        do {
                            bytesRead = is.read(receivedByteArray, current, (receivedByteArray.length - current));
                            if (bytesRead >= 0) current += bytesRead;
                        } while (bytesRead > -1);
                        System.out.println("curr:" + current + "bytesRead:" + bytesRead);
                        byteClass receivedByte = new byteClass(receivedByteArray, current);
                        System.out.println("last is error free");
                        socket.close();
                        serverSocket.close();
                        return receivedByte;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            byteClass receivedByte = new byteClass(receivedByteArray,current);
            return receivedByte;
        }

        public static void sendMessageServer(int socketPort,String message){

            byteClass messageByte = byteClass.getByteFromString(message);
            try{
                System.out.println("server was setup");
                byteClass.sendByte(messageByte,socketPort);
                System.out.println("the message is sent");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public static void sendByte(byteClass byteToSend , int portNum){
            OutputStream os = null;
            try{
                ServerSocket serverSocket = new ServerSocket(portNum);
                Socket socket = serverSocket.accept();
                os = socket.getOutputStream();
                System.out.println("syn: Sending "  + "(" + byteToSend.byteArray.length + " bytes)");
                os.write(byteToSend.byteArray,0,byteToSend.byteArray.length);
                os.flush();
                System.out.println("syn: Done.");
                socket.close();
                serverSocket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public static void createFile(byteClass fileByte,String fileName){
            try{
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                fos = new FileOutputStream(fileName);
                bos = new BufferedOutputStream(fos);
                bos.write(fileByte.byteArray, 0 ,fileByte.actualLength);
                bos.flush();
                bos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public static byteClass getByteOfFile(String file){
            FileInputStream fis;
            BufferedInputStream bis;
            byte [] mybytearray = new byte[1];
            byteClass fileByte = new byteClass(mybytearray,mybytearray.length);
            try{
                File myFile = new File (file);
                mybytearray  = new byte [(int)myFile.length()];
                // System.out.println("file length is "+myFile.length());
                fis = new FileInputStream(myFile);
                bis = new BufferedInputStream(fis);
                bis.read(mybytearray,0,mybytearray.length);
                bis.close();
                return new byteClass(mybytearray,mybytearray.length);
            }catch(Exception e){
                e.printStackTrace();
            }
            return fileByte;
        }
        public static void sendFile(int socketPort,String fileToSend){
            try{
                System.out.println("connection established");
                byteClass fileByte = byteClass.getByteOfFile(fileToSend);
                byteClass.sendByte(fileByte , socketPort);
//            socket.close();
//            serverSocket.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
