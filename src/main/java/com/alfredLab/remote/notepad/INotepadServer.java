package com.alfredLab.remote.notepad;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface INotepadServer extends Remote{

    String BINDING_NAME = "AlfredRemoteNotepadServer";

    ArrayList<String> getFileList() throws RemoteException;

    void createFileWithExistingOrWithNo(String fileName/*,boolean existing*/) throws
            RemoteException; //FileManager

    void wrightInFile(String textAreaContent) throws RemoteException;

    String getTextFromExistingFile(String fileName) throws RemoteException, IOException;

    String getFilePath() throws RemoteException;

    void createFile() throws RemoteException;

    File getFile() throws RemoteException;

    File getDir() throws RemoteException;

    boolean isDir(String dir) throws RemoteException;

    String[] getDirList() throws RemoteException;

    void changeWorkDir(String newDir) throws RemoteException;

    void goUp() throws RemoteException;
}
