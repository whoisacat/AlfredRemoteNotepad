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

    private SortedFilesList mFiles;
    private String returnVal = NO_VALUE;
    private ICallback mCallback;
    private String mDirString;
    protected JList mScrdFilesList;
    protected Box mUpLevelBox;
    private JTextArea mUpHoleFiller;

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
        this.setSize(500,300);
        this.addMouseListener(new MyDialogMouseListener());
    }

//    public static void main(String[] args){
//        OpenExistingFileChooser mrfc =
//                new OpenExistingFileChooser(
//                        "d:/edu",new File("d:/edu").list(),
//                        "title",new Callback());
//        mrfc.buildWindow();
//    }

    public synchronized void buildWindow(){
        System.out.println(Thread.currentThread().getName());
        mScrdFilesList = new JList();
        mScrdFilesList.addListSelectionListener(this);
        mScrdFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mScrdFilesList.addMouseListener(new MyMouseListener());
        mScrdFilesList.setListData(mFiles.getVector());
        System.out.println(mFiles.toString());

        System.out.println(Thread.currentThread().getName());
        Font f = new Font(Font.MONOSPACED,Font.PLAIN,14);
        mScrdFilesList.setFont(f);
        mScrdFilesList.setBackground(Color.BLACK);
        mScrdFilesList.setForeground(Color.ORANGE);

        System.out.println(Thread.currentThread().getName());
        System.out.println(mFiles.toString());
        JScrollPane scrollPane = new JScrollPane(mScrdFilesList);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        System.out.println(Thread.currentThread().getName());
        System.out.println(mFiles.toString());

        Box upButtonBox = new Box(BoxLayout.X_AXIS);
        upButtonBox.setOpaque(true);
        upButtonBox.setBackground(Color.BLACK);
        JButton goUpDir = new JButton("Up dir");
        goUpDir.addActionListener(new MyUpDirListener());
        upButtonBox.add(goUpDir);
        upButtonBox.setMaximumSize(new Dimension(12000,goUpDir.getHeight()));

        mUpHoleFiller = new JTextArea();
        mUpHoleFiller.setBackground(Color.BLACK);
        mUpHoleFiller.setEditable(false);
        mUpHoleFiller.setMaximumSize(new Dimension(12000,goUpDir.getHeight()));
        upButtonBox.add(mUpHoleFiller);

        mUpLevelBox = new Box(BoxLayout.Y_AXIS);
        mUpLevelBox.add(upButtonBox);
        mUpLevelBox.add(scrollPane);
        this.setLocationRelativeTo(null);
    }

    public void setDialogVisible(){
        this.getContentPane().add(mUpLevelBox);
        this.setVisible(true);
    }

    @Override public void valueChanged(ListSelectionEvent e){

//            JOptionPane.showConfirmDialog(null, "Что-то неправильно!\n",
//                                          "Error!", JOptionPane.PLAIN_MESSAGE);
    }

    protected void sendCallbackToMenuBuilderAndDispose(String s,boolean goUpDir){
        mCallback.callMeBack(s,goUpDir);
        this.dispose();
    }

    private class MyMouseListener implements MouseListener{
        @Override public void mouseClicked(MouseEvent evt){
            int index = 0;
            JList list = (JList)evt.getSource();
            if (evt.getClickCount() == 2) {
                Rectangle r = mScrdFilesList
                        .getCellBounds(0,mScrdFilesList.getLastVisibleIndex());
                if (r != null && r.contains(evt.getPoint())) {
                    index = list.locationToIndex(evt.getPoint());
                    String s = (String) mScrdFilesList.getSelectedValue();
                    sendCallbackToMenuBuilderAndDispose(s,false);
                }
            }
        }
        @Override public void mousePressed(MouseEvent e){
        }
        @Override public void mouseReleased(MouseEvent e){
        }
        @Override public void mouseEntered(MouseEvent e){
        }
        @Override public void mouseExited(MouseEvent e){
        }
    }

    protected void mouseExitedRepaint(){
        mScrdFilesList.setSelectionBackground(new Color(50,50,50));
        mScrdFilesList.setSelectionForeground(new Color(230,150,150));
        mScrdFilesList.setBackground(new Color(20,20,20));
        mScrdFilesList.setForeground(new Color(230,150,0));
        mUpHoleFiller.setBackground(new Color(20,20,20));
    }

    protected void mouseEnteredRepaint(){
        mScrdFilesList.setSelectionBackground(new Color(30,30,30));
        mScrdFilesList.setSelectionForeground(new Color(255,175,175));
        mScrdFilesList.setBackground(Color.BLACK);
        mScrdFilesList.setForeground(new Color(255,200,0));
        mUpHoleFiller.setBackground(Color.BLACK);
    }

    private class MyUpDirListener implements ActionListener{
        public final String NO_MATTER = "";
        public final boolean YES_GO_UP = true;
        @Override public void actionPerformed(ActionEvent e){
            sendCallbackToMenuBuilderAndDispose(NO_MATTER,YES_GO_UP);
        }
    }

    private class MyDialogMouseListener implements MouseListener{
        @Override public void mouseClicked(MouseEvent e){
        }

        @Override public void mousePressed(MouseEvent e){
        }

        @Override public void mouseReleased(MouseEvent e){
        }

        @Override public void mouseEntered(MouseEvent e){
            mouseEnteredRepaint();
        }

        @Override public void mouseExited(MouseEvent e){
            mouseExitedRepaint();
        }
    }
}