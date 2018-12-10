package com.alfredLab.remote.notepad;

import javax.swing.*;
import java.awt.*;
import java.io.File;

abstract class MyMenuBarBuilder extends JMenuBar{

    abstract void delText();
    abstract void pastText();
    abstract void copyText();
    abstract void cutText();
    abstract void openFile(String fullFileName);
    abstract void saveFile();
    abstract void createOrRecreateFile(String fileName);
    abstract File getWorkDir();
    abstract String[] getWorkDirList();

    JMenuBar build(){
        JMenu menuFile = createMenuFile();
        JMenu menuEdit = createMenuEdit();
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.setOpaque(true);
        menuBar.setBackground(Color.BLACK);
        return menuBar;
    }

    private JMenu createMenuEdit(){
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(e->cutText());

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(e->copyText());

        JMenuItem pastItem = new JMenuItem("Past");
        pastItem.addActionListener(e->pastText());

        JMenuItem delItem = new JMenuItem("Delete");
        delItem.addActionListener(e->delText());

        JMenu menuEdit = new JMenu("Edit");
        menuEdit.add(cutItem);
        menuEdit.add(cutItem);
        menuEdit.add(pastItem);
        menuEdit.add(delItem);
        return menuEdit;
    }

    private JMenu createMenuFile(){

        JMenuItem createNewFileItem = new JMenuItem("Create new file");
        JMenuItem openExistingFileItem = new JMenuItem("Open existing file");
        JMenuItem saveFileItem = new JMenuItem("Save file");
        JMenuItem saveFileAsItem = new JMenuItem("Save file as...");

        createNewFileItem.addActionListener(e->{
            String fileName = getFileName(createNewFileItem,"Create new");
            fileName = checkFileName(fileName);
            createOrRecreateFile(fileName);
            openFile(fileName);
        });

        openExistingFileItem.addActionListener(e->{
            String fileName = getFileNameFromOpenExistingDialog(openExistingFileItem);
            if(fileName == null || fileName.equals("")){
                return;
            }
            openFile(fileName);
        });

        saveFileItem.addActionListener(e->saveFile());

        saveFileAsItem.addActionListener(e->{
            String fileName = getFileName(createNewFileItem,"Save as");
            fileName = checkFileName(fileName);
            LocalLauncher.changeWorkFile(fileName);
            createOrRecreateFile(fileName);
            saveFile();
        });

        JMenu JMenuFile = new JMenu("File");
        JMenuFile.add(createNewFileItem);
        JMenuFile.add(openExistingFileItem);
        JMenuFile.add(saveFileItem);
        JMenuFile.add(saveFileAsItem);
        return JMenuFile;
    }

    private String getFileName(JMenuItem menuItem,String btnOkText){
        Callback callback = new Callback();
        CreateNewFileChooser remoteFileChooser =
                new CreateNewFileChooser(getWorkDir().getAbsolutePath(),getWorkDirList(),
                                         menuItem.getText(),callback,btnOkText);
        showDialog(remoteFileChooser);
        String filenameString = callback.getRetString();
        boolean upDir = callback.isUpDir();
        if(upDir){
            LocalLauncher.goUpDir();
            filenameString = getFileName(menuItem,btnOkText);
        }else
            if(LocalLauncher.isDir(filenameString)){
                LocalLauncher.changeWorkDir(filenameString);
                filenameString = getFileName(menuItem,btnOkText);
            }
        return filenameString;
    }

    private void showDialog(OpenExistingFileChooser remoteFileChooser){
        remoteFileChooser.buildDialog();
        remoteFileChooser.setDialogVisible();
    }

    private String checkFileName(String fileName){
        if(fileName == null || fileName.equals("")){
            fileName = "New text doc.txt";
        }
        return fileName;
    }

    private String getFileNameFromOpenExistingDialog(JMenuItem menuItem){
        Callback callback = new Callback();
        OpenExistingFileChooser remoteFileChooser =
                new OpenExistingFileChooser(getWorkDir().getAbsolutePath(),
                                            getWorkDirList(),
                                            menuItem.getText(),callback);
        showDialog(remoteFileChooser);
        String filenameString = callback.getRetString();
        boolean upDir = callback.isUpDir();
        if(upDir){
            LocalLauncher.goUpDir();
            filenameString = getFileNameFromOpenExistingDialog(menuItem);
        }else if(LocalLauncher.isDir(filenameString)){
            LocalLauncher.changeWorkDir(filenameString);
            filenameString = getFileNameFromOpenExistingDialog(menuItem);
        }
        return filenameString;
    }
}
