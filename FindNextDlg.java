//
//  Name: Triance, Nick
//  Due: 03 Dec. 2021
//  Description:
//              Implementation of Find Next... dialog

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class FindNextDlg extends FindDlg {
    
    int workingIndex = 0;
    
     /**
     * Dialog for finding a string in a <code>JTextArea</code>
     * @param parent <code>JFrame</code> to attach dialog to.
     * @param jta <code>JTextArea</code> to search
     * @return <code>String</code> the user would like to search for.
     */
    public static String showDialog(JFrame parent, JTextArea jta) {
        
        FindNextDlg find = new FindNextDlg();
        JDialog dlg = new JDialog(parent, "Find Next...", false);
        JLabel label = new JLabel("Find: ");
        JTextField jtf = new JTextField();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new FlowLayout());

        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                find.removeHighlights(jta);
            }
        });

        JButton nextBtn = new JButton("Next");
        nextBtn.setMnemonic('F');
        nextBtn.addActionListener((ae) -> {
            find.setTarget(jtf.getText());
            find.removeHighlights(jta);
            find.findNext(find.getTarget(), jta);
        });

        JButton noBtn = new JButton("Cancel");
        noBtn.setMnemonic('C');
        noBtn.addActionListener((ae) -> {
            find.removeHighlights(jta);
            dlg.dispose();
        });

        btnPanel.add(noBtn);
        btnPanel.add(nextBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        panel.add(jtf, BorderLayout.CENTER);
        panel.add(label, BorderLayout.WEST);
        dlg.add(panel);
        dlg.getRootPane().setDefaultButton(nextBtn);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);

        return find.getTarget();
    }

    
    /**
     * Highlights the given string in the <code>JTextArea</code>
     * @param s <code>String</code> to highlight
     * @param jta <code>JTextArea</code> on which to preform the operation
     */
    public void findNext(String s, JTextArea jta) {
        workingIndex = jta.getText().indexOf(s, workingIndex);
        if (workingIndex != -1) {
            int hlStart = workingIndex;
            int hlEnd = hlStart + s.length();
            workingIndex++;

            Highlighter hl = jta.getHighlighter();
            HighlightPainter hlp = new DefaultHighlighter.DefaultHighlightPainter(Defaults.HIGHLIGHT_COLOR);

            try {
                hl.addHighlight(hlStart, hlEnd, hlp);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}
