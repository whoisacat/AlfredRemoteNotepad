package com.alfredLab.remote.notepad;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class OpenExistingFileChooser extends JDialog implements ListSelectionListener{

    public static final String NO_VALUE = "no value";

    private JList scrdFilesList;
    private SortedFilesList mFiles;
    private String returnVal = NO_VALUE;
    private ICallback mCallback;
    private String mDirString;

    public OpenExistingFileChooser(String dir,String[] dirList,String title,ICallback cb){
        super();
        mDirString = dir;
        mCallback = cb;
        System.out.println(this.getClass().getSimpleName());
        System.out.println(dir);
        System.out.println(dirList == null ?
                                   "dirList == null" :
                                   "dirList == " + dirList.toString() +
                                           "dirListLent == " + dirList.length);
        mFiles = new SortedFilesList(new File(dir),dirList);

        this.setModal(true);
        this.setTitle(title);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(300,300);
    }

    public static void main(String[] args){
        OpenExistingFileChooser mrfc =
                new OpenExistingFileChooser("d:/edu",new File("d:/edu").list(),"title",
                                            new Callback());
        mrfc.buildWindow();
    }

    public synchronized void buildWindow(){
        System.out.println(Thread.currentThread().getName());
        scrdFilesList = new JList();
        scrdFilesList.addListSelectionListener(this);
        scrdFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrdFilesList.addMouseListener(new MyMouseListener());
        scrdFilesList.setListData(mFiles.getVector());
        System.out.println(mFiles.toString());

        System.out.println(Thread.currentThread().getName());
        Font f = new Font(Font.MONOSPACED,Font.PLAIN,14);
        scrdFilesList.setFont(f);
        scrdFilesList.setBackground(Color.BLACK);
        scrdFilesList.setForeground(Color.ORANGE);

        System.out.println(Thread.currentThread().getName());
        System.out.println(mFiles.toString());
        JScrollPane scrollPane = new JScrollPane(scrdFilesList);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        System.out.println(Thread.currentThread().getName());
        System.out.println(mFiles.toString());

        Box upButtonBox = new Box(BoxLayout.X_AXIS);//todo левее надо
        JButton upDir = new JButton("Up dir");
        upDir.addActionListener(new MyUpDirListener());
        upButtonBox.add(upDir);
        Box upLevelBox = new Box(BoxLayout.Y_AXIS);
        upLevelBox.add(upButtonBox);
        upLevelBox.add(scrollPane);
        this.getContentPane().add(upLevelBox);
        this.setVisible(true);

    }

    @Override public void valueChanged(ListSelectionEvent e){

//            JOptionPane.showConfirmDialog(null, "Что-то неправильно!\n",
//                                          "Error!", JOptionPane.PLAIN_MESSAGE);
    }

//    private void retFileName(String s){
//        if(LocalLauncher.isDir(s)){
//            sendCallbackToMenuBuilderAndDispose(s);
//        }else{
//            sendCallbackToMenuBuilderAndDispose(s);
//        }
//    }

    private void sendCallbackToMenuBuilderAndDispose(String s,boolean goUpDir){
        mCallback.callMeBack(s,goUpDir);
        this.dispose();
    }

    private class MyMouseListener implements MouseListener{
        @Override public void mouseClicked(MouseEvent evt){
            int index = 0;
            JList list = (JList)evt.getSource();
            if (evt.getClickCount() == 2) {
                Rectangle r = scrdFilesList.getCellBounds(0, scrdFilesList.getLastVisibleIndex());
                if (r != null && r.contains(evt.getPoint())) {
                    index = list.locationToIndex(evt.getPoint());
                    String s = (String)scrdFilesList.getSelectedValue();
                    sendCallbackToMenuBuilderAndDispose(s,false);
                }
            }
        }
        @Override public void mousePressed(MouseEvent e){
        }
        @Override public void mouseReleased(MouseEvent e){
        }
        @Override public void mouseEntered(MouseEvent e){
            scrdFilesList.setSelectionBackground(new Color(30,30,30));
            scrdFilesList.setSelectionForeground(new Color(255, 175, 175));
            scrdFilesList.setBackground(Color.BLACK);
            scrdFilesList.setForeground(new Color(255, 200, 0));
        }
        @Override public void mouseExited(MouseEvent e){
            scrdFilesList.setSelectionBackground(new Color(50,50,50));
            scrdFilesList.setSelectionForeground(new Color(230, 150, 150));
            scrdFilesList.setBackground(new Color(20,20,20));
            scrdFilesList.setForeground(new Color(230, 150, 0));
        }
    }

    private class MyUpDirListener implements ActionListener{
        public final String NO_MATTER = "";
        public final boolean YES_GO_UP = true;
        @Override public void actionPerformed(ActionEvent e){
            sendCallbackToMenuBuilderAndDispose(NO_MATTER,YES_GO_UP);
        }
    }
}