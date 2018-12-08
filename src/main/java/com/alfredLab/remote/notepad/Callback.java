package com.alfredLab.remote.notepad;

public class Callback implements ICallback{
    private String mRetString;
    private boolean mUpDir;

    public String getRetString(){
        return mRetString;
    }

    @Override public void callMeBack(String retString,boolean upDir){
        mRetString = retString;
        mUpDir = upDir;
    }

    public boolean isUpDir(){
        return mUpDir;
    }
}
