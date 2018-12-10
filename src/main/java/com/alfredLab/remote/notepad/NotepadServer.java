package com.alfredLab.remote.notepad;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NotepadServer extends UnicastRemoteObject implements INotepadServer{
    private static final String SLASH = "/";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private File mDir = new File("d:/edu");
    private File mFile;

    private NotepadServer() throws RemoteException{}

    public void createFileWithExistingOrWithNo(String fileName){
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

    public void wrightInFile(String textAreaContent){
        String text = textAreaContent.replace("\n",LINE_SEPARATOR);
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(mFile));
            bw.write(text);
            bw.close();
        } catch(IOException ioe){
            System.out.println(this.getClass().getSimpleName() + ioe.toString());
        }
    }

    public String getTextFromExistingFile(String fileName) throws IOException{
        mFile = new File(mDir.getAbsolutePath().concat(SLASH).concat(fileName));
        String content = "";
        BufferedReader br = new BufferedReader(new FileReader(mFile));
        String s;
        while((s = br.readLine()) != null){
            content = content.concat(s).concat(LINE_SEPARATOR);
        }
        br.close();
        content = content.replace(LINE_SEPARATOR,"\n");
        if(content.length() > 0)
            return content.substring(0,content.length() - 1);
        return content;
    }

    public String getFilePath(){
        return mFile.getAbsolutePath();
    }

    @Override public File getDir(){
        return mDir;
    }

    @Override public boolean isDir(String dir){
        return new File(mDir.getAbsolutePath().concat(SLASH).concat(dir)).isDirectory();
    }

    @Override public String[] getDirList(){
        return mDir.list();
    }

    @Override public void changeWorkDir(String newDir){
        mDir = new File(mDir.getAbsolutePath().concat(SLASH).concat(newDir));
        if(!mDir.exists()){
            try{
                mDir.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override public void changeWorkFile(String fileName){
        mFile = new File(mDir.getAbsolutePath().concat(SLASH).concat(fileName));
        if(mFile.exists()){
            mFile.delete();
        }
        try{
            mFile.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override public void goUp(){
        String mDirString = mDir.getAbsolutePath();
        mDirString = mDirString.replace('\\','/');
        String[] as = mDirString.split(SLASH);
        String newDir = as[0];
        for(int i = 1; i < as.length - 1; i++){
            newDir = newDir.concat(SLASH).concat(as[i]);
        }
        mDir = new File(newDir);
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
