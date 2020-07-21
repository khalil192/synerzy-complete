package com.example.synerzy;

import java.util.ArrayList;


public class fileEntry {
    private String fileName;
    private String fileType;

    public fileEntry(String file , String type){
        fileName = file;
        fileType = type;
    }
    public void changeName(String text){
        fileName = text;
    }
    public String getFileName(){ return fileName;}
    public String getFileType(){return  fileType;}
    public static ArrayList<fileEntry> getRandomData(int num){
        ArrayList<fileEntry> fileList= new ArrayList<>();
        for(int i = 0;i<num;i++){
            fileList.add(new fileEntry("file " + i ,"music file" ));
        }
        return fileList;
    }


}

