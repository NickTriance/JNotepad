//
//  Name: Triance, Nick
//  Due: 03 Dec. 2021
//  Description:
//              Go To dialog for Notepad

import javax.swing.*;

public class GotoDlg {
    
    private int lineNo = 0;

    /**
     * @return int return the lineNo
     */
    public int getLineNo() {
        return lineNo;
    }

    /**
     * @param lineNo the lineNo to set
     */
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    /**
     * 
     * @param parent the <code> JFrame </code> to attach the dialog to.
     * @return <code>int</code>, the line number which the user would like to go to (-1 if invalid or cancelled).
     */
    public static int showDialog(JFrame parent, int initial) {
        GotoDlg lineNum = new GotoDlg();
        lineNum.setLineNo(initial);
        JDialog dlg = new JDialog(parent,"Go To Line", true);
        JLabel label = new JLabel("Line number:");
        label.setDisplayedMnemonic('L');

        JPanel panel = new JPanel();
        panel.add(label);
        JTextField jtf = new JTextField(10);
        jtf.setText(String.valueOf(initial));
        panel.add(jtf);

        JButton goButton = new JButton("Go To");
        goButton.addActionListener((ae) -> {
            String input = jtf.getText();
            try {
                int _lineNum;
                _lineNum = Integer.parseInt(input);
                lineNum.setLineNo(_lineNum);
                dlg.setVisible(false);
            } catch (Exception e) {
                System.out.println("Error while parsing integer: " + e.toString());
                lineNum.setLineNo(-1);
                dlg.setVisible(false);
            }
        });
        panel.add(goButton);

        JButton noButton = new JButton("Cancel");
        noButton.addActionListener((ae) -> {
            lineNum.setLineNo(-1);
            dlg.setVisible(false);
        });
        panel.add(noButton);

        dlg.add(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(parent);
        dlg.getRootPane().setDefaultButton(goButton);
        dlg.setVisible(true);

        return lineNum.getLineNo();
    }

}
