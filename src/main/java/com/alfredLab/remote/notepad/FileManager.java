package com.alfredLab.remote.notepad;

import java.io.*;

public class FileManager{

    File mFile;

    public FileManager(String fullFileName,boolean existing){
        mFile = new File(fullFileName);
        if(mFile.exists()){
            if(!existing){
                mFile.delete();
            }
        } else{
            try{
                mFile.createNewFile();
            } catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

    public void wrightInRemoteFile(String s){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mFile));
            bw.write(s);
            bw.close();
        } catch(IOException ioe){
            System.out.println(this.getClass().getSimpleName() + ioe.toString());
        }
    }

    public String getTextFromExistingFile() throws IOException{
        BufferedReader br;
        br = new BufferedReader(new FileReader(mFile));
        String text = "";
        String s;
        while((s = br.readLine()) != null){
            text = text.concat(s).concat("\n");
        }
        br.close();
        return text;
    }
}
