package com.alfredLab.remote.notepad;

import javax.swing.*;
import java.awt.*;
import java.io.File;

abstract class MyMenuBarBuilder extends JMenuBar{

    abstract void delText();
    abstract void pastText();
    abstract void copyText();
    abstract void cutText();
    abstract String getFileDir();
    abstract void openFile(String fullFileName);
    abstract void saveFile();
    abstract void createOrRecreateFile(String fileName);
    abstract OpenExistingFileChooser getRemoteJFileChooser();
    abstract File getWorkDir();
    abstract String[] getWorkDirList();

    public JMenuBar build(){


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
        cutItem.addActionListener(e->{
            cutText();
        });

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(e->{
            copyText();
        });

        JMenuItem pastItem = new JMenuItem("Past");
        pastItem.addActionListener(e->{
            pastText();
        });

        JMenuItem delItem = new JMenuItem("Delete");
        delItem.addActionListener(e->{
            delText();
        });

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
            String fileName = getFileNameFromCreateNewDialog(createNewFileItem);
            if(fileName == null || fileName.equals(OpenExistingFileChooser.NO_VALUE)){
                fileName ="New text doc.txt";
            }
            createOrRecreateFile(fileName);
            openFile(fileName);
        });


        openExistingFileItem.addActionListener(e->{
            String fileName = getFileNameFromOpenExistingDialog(openExistingFileItem);
            System.out.println("openFile(" + fileName + ");");
            if(fileName == null || fileName.equals(OpenExistingFileChooser.NO_VALUE)){
                return;
            }
            openFile(fileName);
        });


        saveFileItem.addActionListener(e->saveFile());


        saveFileAsItem.addActionListener(e->{

//            try{
//                JFileChooser fileChooser = new JFileChooser(getFileDir());
//                if(fileChooser.showDialog(null,"Save as") == JFileChooser.APPROVE_OPTION){
//                    String fullFileName = fileChooser.getSelectedFile().getCanonicalPath();
//                    createOrRecreateFile(fullFileName);
//                    saveFile();
//                }
//            } catch(IOException ioe){
//                ioe.printStackTrace();
//            }
        });

        JMenu JMenuFile = new JMenu("File");
        JMenuFile.add(createNewFileItem);
        JMenuFile.add(openExistingFileItem);
        JMenuFile.add(saveFileItem);
        JMenuFile.add(saveFileAsItem);
        return JMenuFile;
    }

    private String getFileNameFromOpenExistingDialog(JMenuItem menuItem){
        Callback callback = new Callback();
        OpenExistingFileChooser remoteFileChooser =
                new OpenExistingFileChooser(getWorkDir().getAbsolutePath(),
                                            getWorkDirList(),
                                            menuItem.getText(),callback);
        remoteFileChooser. buildWindow();
        remoteFileChooser.setDialogVisible();
        String ret = callback.getRetString();
        System.out.println("String ret = callback.getRetString(); ret = " + ret);
        boolean upDir = callback.isUpDir();
        System.out.println("upDir = " + upDir);
        if(upDir){
            LocalLauncher.goUpDir();
            ret = getFileNameFromOpenExistingDialog(menuItem);
        }else if(LocalLauncher.isDir(ret)){
            LocalLauncher.changeWorkDir(ret);
            ret = getFileNameFromOpenExistingDialog(menuItem);
        }
        return ret;

    }private String getFileNameFromCreateNewDialog(JMenuItem menuItem){
        Callback callback = new Callback();
        CreateNewFileChooser remoteFileChooser =
                new CreateNewFileChooser(getWorkDir().getAbsolutePath(),getWorkDirList(),
                                            menuItem.getText(),callback);
        remoteFileChooser. buildWindow();
        remoteFileChooser.setDialogVisible();
        String ret = callback.getRetString();
        System.out.println("String ret = callback.getRetString(); ret = " + ret);
        boolean upDir = callback.isUpDir();
        System.out.println("upDir = " + upDir);
        if(upDir){
            LocalLauncher.goUpDir();
            ret = getFileNameFromCreateNewDialog(menuItem);
        }else if(LocalLauncher.isDir(ret)){
            LocalLauncher.changeWorkDir(ret);
            ret = getFileNameFromCreateNewDialog(menuItem);
        }
        return ret;
    }
}
