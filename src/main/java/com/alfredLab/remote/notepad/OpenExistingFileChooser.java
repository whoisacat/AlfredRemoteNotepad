package com.alfredLab.remote.notepad;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

class OpenExistingFileChooser extends JDialog implements ListSelectionListener{

    private SortedFilesList mFiles;
    private ICallback mCallback;
    JList mScrdFilesList;
    Box mUpLevelBox;
    private JTextArea mUpHoleFiller;
    private Box mUpButtonBox;
    private JButton mGoUpDirBtn;

    public OpenExistingFileChooser(String dir,String[] dirList,String title,ICallback cb){
        super();
        mCallback = cb;
        mFiles = new SortedFilesList(new File(dir),dirList);
        this.setModal(true);
        this.setTitle(title);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(500,300);
        try{
            this.setIconImage(ImageIO.read(new File(
                    "src/main/resources/icon.png")));
        } catch(IOException e){
            e.printStackTrace();
        }
        this.addMouseListener(new MyDialogMouseListener());
    }

    synchronized void buildDialog(){
        System.out.println(Thread.currentThread().getName());
        mScrdFilesList = new JList();
        mScrdFilesList.addListSelectionListener(this);
        mScrdFilesList.addMouseListener(new MyDialogMouseListener());
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

        mUpButtonBox = new Box(BoxLayout.X_AXIS);
        mUpButtonBox.setOpaque(true);
        mUpButtonBox.setBackground(Color.BLACK);
        mGoUpDirBtn = new JButton("Up dir");
        mGoUpDirBtn.setContentAreaFilled(false);
        mGoUpDirBtn.setOpaque(true);
        mGoUpDirBtn.setBackground(new Color(45,45,45));
        mGoUpDirBtn.setForeground(Color.ORANGE);
        mGoUpDirBtn.setFont(f);
        mGoUpDirBtn.setBorderPainted(false);
        mGoUpDirBtn.setMargin(new Insets(3, 3, 3, 3));
        mGoUpDirBtn.addActionListener(new MyUpDirListener());
        mGoUpDirBtn.addMouseListener(new MyDialogMouseListener());
        mUpButtonBox.addMouseListener(new MyDialogMouseListener());
        mUpButtonBox.add(mGoUpDirBtn);
        mUpButtonBox.setMaximumSize(new Dimension(12000,mGoUpDirBtn.getHeight()));

        mUpHoleFiller = new JTextArea();
        mUpHoleFiller.setBackground(Color.BLACK);
        mUpHoleFiller.setEditable(false);
        mUpHoleFiller.setMaximumSize(new Dimension(12000,mGoUpDirBtn.getHeight()));
        mUpHoleFiller.addMouseListener(new MyDialogMouseListener());
        mUpButtonBox.add(mUpHoleFiller);

        mUpLevelBox = new Box(BoxLayout.Y_AXIS);
        mUpLevelBox.add(mUpButtonBox);
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

    void sendCallbackToMenuBuilderAndDispose(String s,boolean goUpDir){
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
        mScrdFilesList.setSelectionForeground(new Color(230,170,170));
        mScrdFilesList.setBackground(new Color(20,20,20));
        mScrdFilesList.setForeground(new Color(200,130,50));
        mUpHoleFiller.setBackground(new Color(20,20,20));
        mUpButtonBox.setBackground(new Color(20,20,20));
        mGoUpDirBtn.setBackground(new Color(20,20,20));
        mGoUpDirBtn.setForeground(new Color(200,130,50));

    }

    protected void mouseEnteredRepaint(){
        mScrdFilesList.setSelectionBackground(new Color(30,30,30));
        mScrdFilesList.setSelectionForeground(new Color(255,150,150));
        mScrdFilesList.setBackground(Color.BLACK);
        mScrdFilesList.setForeground(new Color(255,200,0));
        mUpHoleFiller.setBackground(Color.BLACK);
        mUpButtonBox.setBackground(Color.BLACK);
        mGoUpDirBtn.setBackground(new Color(45,45,45));
        mGoUpDirBtn.setForeground(new Color(255,200,0));
    }

    private class MyUpDirListener implements ActionListener{
        final String NO_MATTER = "";
        final boolean YES_GO_UP = true;
        @Override public void actionPerformed(ActionEvent e){
            sendCallbackToMenuBuilderAndDispose(NO_MATTER,YES_GO_UP);
        }
    }

    protected class MyDialogMouseListener implements MouseListener{
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