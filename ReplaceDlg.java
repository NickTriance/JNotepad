//
//  Name: Triance, Nick
//  Project: 6 (FINAL)
//  Due: 03 Dec. 2021
//  Course: CS-2450-01
//  Description:
//              Implementation of a replaceAll dialog

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReplaceDlg extends FindDlg{
    private String source;
    private String target;

     /**
     * Dialog for replacing a string in a <code>JTextArea</code>
     * @param parent <code>JFrame</code> to attach dialog to.
     * @param jta <code>JTextArea</code> to search
     * @return <code>null</code>
     */
    public static String showDialog(JFrame parent, JTextArea jta) {

        ReplaceDlg find = new ReplaceDlg();
        JDialog dlg = new JDialog(parent, "Replace...", false);
        JLabel targetLabel = new JLabel("Replace: ");
        JLabel sourceLabel = new JLabel("With: ");
        JTextField sourceJtf = new JTextField(15);
        JTextField targetJtf = new JTextField(15);
        JPanel inputPanel = new JPanel(new FlowLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new FlowLayout());

        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                find.removeHighlights(jta);
            }
        });

        JButton nextBtn = new JButton("Replace All");
        nextBtn.setMnemonic('F');
        nextBtn.addActionListener((ae) -> {
            find.setSource(sourceJtf.getText());
            find.setTarget(targetJtf.getText());
            find.replace(find.getSource(), find.getTarget(), jta);
        });

        JButton noBtn = new JButton("Cancel");
        noBtn.setMnemonic('C');
        noBtn.addActionListener((ae) -> {
            find.removeHighlights(jta);
            dlg.dispose();
        });

        btnPanel.add(noBtn);
        btnPanel.add(nextBtn);
        inputPanel.add(targetLabel);
        inputPanel.add(targetJtf);
        inputPanel.add(sourceLabel);
        inputPanel.add(sourceJtf);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        dlg.add(mainPanel);
        dlg.getRootPane().setDefaultButton(nextBtn);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);

        return null;
    }

    /**
     * Replaces all instances of a <code>String</code> withing a <code>JTextArea</code>
     * @param source
     * @param target
     * @param jta
     */
    public void replace(String source, String target, JTextArea jta) {
        int index = 0;
        
        //loop through text field, replacing as we go.
        while(index != -1) {
            index = jta.getText().indexOf(target);
            if (index != -1) {
                jta.replaceRange(source, index, index + source.length());
            }
        }
    }
    
    /**
     * @return String return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return String return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

}
