package com.alfredLab.remote.notepad;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class LocalLauncher{

    private GUI mGUI;
    private static String HOST = "127.0.0.1";
    private static INotepadServer mService = null;

    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException |
                UnsupportedLookAndFeelException e){
            e.printStackTrace();
        }

        try{
            Registry reg = LocateRegistry.getRegistry(HOST,19877);
            mService = (INotepadServer) reg.lookup(INotepadServer.BINDING_NAME);
        } catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
        LocalLauncher sLocalLauncher = new LocalLauncher();
        sLocalLauncher.run();
    }

    static boolean isDir(String fileName){
        boolean ret = false;
        if(fileName != null && !fileName.equals("")){
            try{
                ret = mService.isDir(fileName);
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
        return ret;
    }

    static void changeWorkDir(String newDir){
        try{
            mService.changeWorkDir(newDir);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    static void goUpDir(){
        try{
            mService.goUp();
        }catch(RemoteException re){
            re.printStackTrace();
        }
    }

    static void changeWorkFile(String fileName){
        try{
            mService.changeWorkFile(fileName);
        }catch(RemoteException e){
            e.printStackTrace();
        }
    }

    private void run(){
        mGUI = new GUI(){
            @Override String getDirOfExisting(){
                try{
                    return mService.getFilePath();
                } catch(RemoteException e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override protected String getFileContent(String fileName) throws IOException{
                return mService.getTextFromExistingFile(fileName);
            }

            @Override protected void stopApp(boolean save, String text){
                stop(save, text, mService);
            }

            @Override protected void wrightInFile(String textAreaContent){
                try{
                    mService.wrightInFile(textAreaContent);
                } catch(RemoteException e){
                    e.printStackTrace();
                }
            }

            @Override void getNewFile(String fileName){
                try{
                    mService.createFileWithExistingOrWithNo(fileName);
                } catch(RemoteException e){
                    e.printStackTrace();
                }
            }

            @Override File getRemoteWorkDir(){
                File dir = null;
                try{
                    dir =  mService.getDir();
                }catch(RemoteException e){
                    e.printStackTrace();
                }
                return dir;
            }

            @Override String[] getRemoteDirList(){
                String[] dirList = null;
                try{
                    dirList = mService.getDirList();
                }catch(RemoteException e){
                    e.printStackTrace();
                }
                return dirList;
            }
        };
    }

    private void stop(boolean doSave,String text,INotepadServer service){
        if(doSave){
            if(text != null){
                try{
                    service.wrightInFile(text);
                } catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        }
        mGUI.closeDialog();
        System.exit(0);
    }
}
