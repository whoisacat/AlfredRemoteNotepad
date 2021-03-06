package com.alfredLab.remote.notepad;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

abstract class GUI{
    private JFrame mJFrame;
    private JTextArea mJTextArea;
    private String editingFileName;

    GUI(){

        JMenuBar menuBar = new MyMenuBarBuilder(){

            @Override protected void delText(){
                mJTextArea.replaceSelection("");
            }

            @Override protected void pastText(){
                mJTextArea.paste();
            }

            @Override protected void copyText(){
                mJTextArea.copy();
            }

            @Override protected void cutText(){
                mJTextArea.cut();
            }

            @Override protected void openFile(String fileName){
                editingFileName = fileName;
                String text = null;
                try{
                    text = getFileContent(editingFileName);
                }catch(IOException e){
                    e.printStackTrace();
                }
                makeWrightableTextArea();
                mJTextArea.setText(text);
            }

            @Override protected void saveFile(){
                String textAreaContent = mJTextArea.getText();
                wrightInFile(textAreaContent);
            }

            @Override void createOrRecreateFile(String fileName){
                getNewFile(fileName);
            }

            @Override File getWorkDir(){
                return (getRemoteWorkDir());
            }

            @Override String[] getWorkDirList(){
                return getRemoteDirList();
            }
        }.build();

        mJFrame = new JFrame("AlfredRemoteNotepad");
        mJFrame.setBackground(Color.BLACK);
        mJFrame.setForeground(Color.ORANGE);
        mJFrame.setJMenuBar(menuBar);
        mJFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mJFrame.setLocationRelativeTo(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mJFrame.setSize(screenSize.width / 4 * 3,screenSize.height / 4 * 3);
        mJFrame.setLocationRelativeTo(null);
        try{
            mJFrame.setIconImage(ImageIO.read(new File(
                    "src/main/resources/icon.png")));
        } catch(IOException e){
            e.printStackTrace();
        }
        mJFrame.getContentPane().setBackground(Color.BLACK);
        mJFrame.setVisible(true);
        mJFrame.addWindowListener(new WindowAdapter(){
            @Override public void windowClosing(WindowEvent e){
                try{
                    if(!mJTextArea.getText().equals(getFileContent(editingFileName))){
                        System.out.println(mJTextArea.getText());
                        System.out.println(getFileContent(editingFileName));
                        onClosingWindow();
                    }
                    else stopApp(false,"");
                }catch(IOException ioe){
                    stopApp(false,null);
                    System.out.println(ioe.toString() + " \n" + ioe.getStackTrace());
                }catch(NullPointerException npe){
                    stopApp(false,null);
                    System.out.println(npe.toString() + " \n" + npe.getStackTrace());
                }
            }
        });
    }

    private void onClosingWindow(){
        int result = JOptionPane
                .showConfirmDialog(null,"Should I save the changes?","Closing the app",
                                   JOptionPane.YES_NO_CANCEL_OPTION,
                                   JOptionPane.WARNING_MESSAGE);
        switch (result) {
            case JOptionPane.YES_OPTION:
                saveIfThereWasAnyChanges();
                break;
            case JOptionPane.NO_OPTION :
                stopApp(false, null);
                break;
            case JOptionPane.CANCEL_OPTION :
        }
    }

    private void saveIfThereWasAnyChanges(){
        stopApp(true, mJTextArea.getText());
    }

    private void makeWrightableTextArea(){
        mJFrame.getContentPane().removeAll();
        mJTextArea = new JTextArea();
        JScrollPane JScrollPane = new JScrollPane(mJTextArea);
        JScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mJFrame.getContentPane().add(JScrollPane);
        mJTextArea.setBackground(Color.black);
        Font f = new Font(Font.MONOSPACED,Font.PLAIN,14);
        mJTextArea.setFont(f);
        mJTextArea.setForeground(Color.ORANGE);
        mJTextArea.setCaretColor(Color.ORANGE);
        mJFrame.setVisible(true);
    }

    void closeDialog(){
        mJFrame.dispose();
    }

    abstract String getDirOfExisting();
    abstract String getFileContent(String fileName) throws IOException;
    abstract void stopApp(boolean save, String text);
    abstract void wrightInFile(String textAreaContent);
    abstract void getNewFile(String fileName);
    abstract File getRemoteWorkDir();
    abstract String[] getRemoteDirList();

}
