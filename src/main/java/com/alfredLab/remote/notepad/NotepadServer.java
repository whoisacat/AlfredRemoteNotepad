package com.alfredLab.remote.notepad;

import javax.swing.*;
import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NotepadServer extends UnicastRemoteObject implements INotepadServer{
    public static final String SLASH = "/";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");//"\n";//
    private File mDir = new File("d:/edu");
    private File mFile;

    protected NotepadServer() throws RemoteException{}

    public ArrayList<String> getFileList() throws RemoteException{
        return new ArrayList<>();
    }

    public void createFileWithExistingOrWithNo(String fileName/*,
                                               boolean existing*/) throws RemoteException{
        File newFile =
                new File(mDir.getAbsolutePath().concat(SLASH).concat(fileName));
        if(newFile.exists()){
            newFile.delete();
        }
        try{
            newFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void wrightInFile(String textAreaContent) throws RemoteException{

        String text = textAreaContent.replace("\n",LINE_SEPARATOR);
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mFile));
            bw.write(text);
            bw.close();
        } catch(IOException ioe){
            System.out.println(this.getClass().getSimpleName() + ioe.toString());
        }finally{

        }
    }

    public String getTextFromExistingFile(String fileName) throws RemoteException, IOException{
        mFile = new File(mDir.getAbsolutePath().concat(SLASH).concat(fileName));
        System.out.println("getTextFromExistingFile(String " + fileName + ")");
        System.out.println(mFile.exists());
        System.out.println();
        String content = "";
        BufferedReader br = new BufferedReader(new FileReader(mFile));
        String s;
        while((s = br.readLine()) != null){
            content = content.concat(s).concat(LINE_SEPARATOR);
            System.out.println(s);
            System.out.println(content);
        }
        br.close();
        return content;
    }

    public String getFilePath() throws RemoteException{
        return mFile.getAbsolutePath();
    }

    public void createFile() throws RemoteException{

    }

    public File getFile() throws RemoteException{
        return null;
    }

    @Override public File getDir() throws RemoteException{
        System.out.println(mDir.getAbsolutePath());
        return mDir/*.getAbsolutePath()*/;
    }

    @Override public boolean isDir(String dir) throws RemoteException{
        return new File(mDir.getAbsolutePath().concat(SLASH).concat(dir)).isDirectory();
    }

    @Override public String[] getDirList() throws RemoteException{
        System.out.println(this.getClass().getSimpleName() + ": ");
        System.out.println(mDir.getAbsolutePath());
        System.out.println(mDir);
        System.out.println(mDir.list());
        System.out.println(this.getClass().getSimpleName() + " end");
        return mDir.list();
    }

    @Override public void changeWorkDir(String newDir) throws RemoteException{
        mDir = new File(mDir.getAbsolutePath().concat(SLASH).concat(newDir));
        System.out.println(mDir.getAbsolutePath());
        System.out.println(mDir);
        if(!mDir.exists()){
            try{
                mDir.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override public void goUp(){
        System.out.println("@Override public void goUp(){");
        System.out.println("    mDir = " + mDir.getAbsolutePath());
        String mDirString = mDir.getAbsolutePath();
        mDirString = mDirString.replace('\\','/');
        String[] as = mDirString.split(SLASH);
        System.out.println("    as = " + as);
        String newDir = as[0];
        System.out.println("    newDir = " + newDir);
        for(int i = 1; i < as.length - 1; i++){
            newDir = newDir.concat(SLASH).concat(as[i]);
        }
        System.out.println("    newDir = " + newDir);
        mDir = new File(newDir);
        System.out.println("    mDir = new File(" + newDir + ");");
        System.out.println("    " + mDir.getAbsolutePath());
        System.out.println("    " + mDir.exists());
        System.out.println("}");
    }

    public static void main(String[] args){
        try{
            INotepadServer notepadServer = new NotepadServer();
            System.out.print("Starting registry...");
            Registry registry = LocateRegistry.createRegistry(19877);
            System.out.println(" OK");
            try{
                registry.bind("ClientRegister",notepadServer);
            } catch(AlreadyBoundException abe){
                abe.printStackTrace();
            }
            try{
                System.out.print("Binding service...");
                registry.bind(BINDING_NAME,notepadServer);
                System.out.println(" OK");
            } catch(AlreadyBoundException e){
                e.printStackTrace();
            }
        } catch(RemoteException e){
            e.printStackTrace();
        }
    }
}
