package com.alfredLab.remote.notepad;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewFileChooser extends OpenExistingFileChooser{

    JTextArea mFileNameArea;
    private JButton mButtonOk;
    private Box mDownBox;

    public CreateNewFileChooser(String absolutePath,String[] workDirList,String text,
                                Callback callback){
        super(absolutePath,workDirList,text,callback);
    }

    @Override
    public void setDialogVisible(){
        mFileNameArea = new JTextArea();
        mFileNameArea.setBackground(Color.BLACK);
        mFileNameArea.setForeground(Color.ORANGE);
        mFileNameArea.setCaretColor(Color.ORANGE);
        mFileNameArea.setSelectedTextColor(new Color(70,70,70));
        Font f = new Font(Font.MONOSPACED,Font.PLAIN,14);
        mFileNameArea.setFont(f);
        mFileNameArea.setEditable(true);
        mFileNameArea.getCaret().setVisible(true);

        mButtonOk = new JButton("Create new");
        mButtonOk.setContentAreaFilled(false);
        mButtonOk.setOpaque(true);
        mButtonOk.setBackground(new Color(45,45,45));
        mButtonOk.setForeground(Color.ORANGE);
        mButtonOk.setFont(f);
        mButtonOk.setBorderPainted(false);
        mButtonOk.setMargin(new Insets(3, 3, 3, 3));
        mButtonOk.addActionListener(new ButtonOkListener());

        mDownBox = new Box(BoxLayout.X_AXIS);
        mDownBox.add(mFileNameArea);
        mDownBox.add(mButtonOk);

//        mFileNameArea.setMaximumSize(new Dimension(12000,mButtonOk.getHeight()));

        mDownBox.setOpaque(true);
        mDownBox.setBackground(Color.BLACK);
        mDownBox.setMaximumSize(new Dimension(12000,mButtonOk.getHeight()));

        mUpLevelBox.add(mDownBox);
        this.getContentPane().add(this.mUpLevelBox);
        this.setVisible(true);
    }
    @Override public void valueChanged(ListSelectionEvent e){
        mFileNameArea.setText((String) mScrdFilesList.getSelectedValue());
    }

    @Override protected void mouseExitedRepaint(){
        super.mouseExitedRepaint();
        mButtonOk.setBackground(new Color(20,20,20));
        mButtonOk.setForeground(new Color(230,150,0));
        mDownBox.setBackground(new Color(20,20,20));
        mDownBox.setForeground(new Color(230,150,0));
        mFileNameArea.setBackground(new Color(20,20,20));
        mFileNameArea.setForeground(new Color(230,150,0));
    }

    @Override protected void mouseEnteredRepaint(){
        super.mouseEnteredRepaint();
        mButtonOk.setBackground(new Color(45,45,45));
        mButtonOk.setForeground(new Color(255,200,0));
        mDownBox.setBackground(Color.BLACK);
        mDownBox.setForeground(new Color(255,200,0));
        mFileNameArea.setBackground(Color.BLACK);
        mFileNameArea.setForeground(new Color(255,200,0));
    }

    private class ButtonOkListener implements ActionListener{
        @Override public void actionPerformed(ActionEvent e){
            String s = (String) mFileNameArea.getText();
            sendCallbackToMenuBuilderAndDispose(s,false);
        }
    }

}
