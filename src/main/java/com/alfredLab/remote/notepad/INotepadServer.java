package com.alfredLab.remote.notepad;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

interface INotepadServer extends Remote{

    String BINDING_NAME = "AlfredRemoteNotepadServer";

    void createFileWithExistingOrWithNo(String fileName) throws
            RemoteException;

    void wrightInFile(String textAreaContent) throws RemoteException;

    String getTextFromExistingFile(String fileName) throws IOException;

    String getFilePath() throws RemoteException;

    void changeWorkFile(String fileName) throws RemoteException;

    File getDir() throws RemoteException;

    boolean isDir(String dir) throws RemoteException;

    String[] getDirList() throws RemoteException;

    void changeWorkDir(String newDir) throws RemoteException;

    void goUp() throws RemoteException;
}
