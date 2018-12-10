package com.alfredLab.remote.notepad;

import java.io.File;
import java.util.*;

class SortedFilesList{
    private ArrayList<String> list;

    SortedFilesList(File dir,String[] dirList){
        list = new ArrayList<>(Arrays.asList(dirList));
        int i = 0;
        for(String s : list){
            list.set(i,dir.getAbsolutePath().concat("/").concat(s));
            System.out.println(i);
            i++;
        }
        i = 0;
        list.sort(new MyComparator());
        for(String s : list){
            String[] as = s.split("/");
            list.set(i,as[as.length - 1]);
            i++;
        }
    }

    Vector<String> getVector(){
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
