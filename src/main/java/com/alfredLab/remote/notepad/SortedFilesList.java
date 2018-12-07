package com.alfredLab.remote.notepad;

import javafx.util.Pair;

import java.io.File;
import java.util.*;

public class SortedFilesList{
//    HashMap<String,String> filesMap = new HashMap<>();
    ArrayList<String> list;

    public SortedFilesList(File dir,String[] dirList){
        System.out.println(this.getClass().getSimpleName());
        System.out.println(
                (dir == null ? "dir == null" : "dir == " + dir.getAbsolutePath()));
        System.out.println(dirList == null ?
                                   "dirList == null" :
                                   "dirList == " + dirList.toString() +
                                           "dirListLent == " + dirList.length);
        list = new ArrayList<>(Arrays.asList(dirList));
        int i = 0;
        for(String s : list){
            list.set(i,dir.getAbsolutePath().concat("/").concat(s));
            System.out.println(i);
            i++;
        }
        i = 0;
        Collections.sort(list,new MyComparator());
        for(String s : list){
            String[] as = s.split("/");
            list.set(i,as[as.length - 1]);
            i++;
        }
    }

    public Vector<String> getVector(){
        return new Vector<>(list);
    }

    private class MyComparator implements Comparator<String>{

        @Override public int compare(String o1,String o2){
            if(new File(o1).isDirectory() & new File(o2).isDirectory()){
                return o1.compareTo(o2);
            }
            else
                if(new File(o1).isDirectory()){
                    return -1;
                }
                else
                    if(new File(o2).isDirectory()){
                        return 1;
                    }
                    else {
                        return o1.compareTo(o2);
                    }
        }

        @Override public boolean equals(Object obj){
            return false;
        }
    }
}
