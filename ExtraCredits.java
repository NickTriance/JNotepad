//
//  Name: Triance, Nick
//  Due: 03 Dec. 2021
//  Description:
//              Extra credits dialog for notepad

import javax.swing.*;
import java.awt.GridLayout;

public class ExtraCredits {
    private static final String[] ec = {"View Help", 
        "Frame title changes dynamically", 
        "Undo/Redo",
        "Print",
        "Page Setup",
        "Zoom in/out/reset",
        "Custom Colors",
        "Find Next",
        "Replace"
    };

    /**
     * Displays the Extra Credit dialog
     * @param parent <code>JFrame</code> to attach the dialog to.
     * @return <code>0</code>
     */
    public static int showDialog(JFrame parent) {

        JDialog dlg = new JDialog(parent, "Extra Credits", true);
        JPanel jpl = new JPanel(new GridLayout(2,1));
        String displayString = new String();
        
        for(int i = 0; i < ec.length; i++) {
            displayString+=ec[i];
            displayString+="\n";
        }

        JTextArea jLab = new JTextArea(displayString);
        jLab.setEditable(false);
        JButton okBtn = new JButton("Close");
        okBtn.addActionListener((ae) -> {
            dlg.dispose();
        });

        jpl.add(jLab);
        jpl.add(okBtn);
        dlg.add(jpl);
        dlg.pack();
        dlg.setLocationRelativeTo(parent);
        dlg.getRootPane().setDefaultButton(okBtn);
        dlg.setVisible(true);

        return 0; //for some reason the dialog wasn't working right if it didn't return anything.
    }
}
