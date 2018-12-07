package com.alfredLab.remote.notepad;

public class Callback implements ICallback{
    private String mRetString;
    private boolean upDir;

    public String getRetString(){
        return mRetString;
    }

    @Override public void callMeBack(String retString/*,boolean upDir*/){
        mRetString = retString;
    }

//    public boolean isUpDir(){
//        return upDir;
//    }
}
