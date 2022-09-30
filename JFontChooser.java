//
//  Name: Triance, Nick
//  Project: 6 (Final)
//  Due: 03 Dec. 2021
//  Course: CS-2450-01
//  Description:
//              Custom font selector dialog.
//

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;

public class JFontChooser {

    private Font font;

    protected void setFont(Font _font) {
        this.font = _font;
    }

    protected Font getFont() {
        return this.font;
    }

    /**
     * Shows the FontChooser dialog.
     * @param parent <code>JFrame</code> frame to attach the dialog to.
     * @param defaultFont <code>Font</code> default Font to be selected
     * @return <code>Font</code> that the user selected.
     */
    public static Font showDialog(JFrame parent, Font defaultFont) {
        JFontChooser returnFont = new JFontChooser();
        returnFont.setFont(defaultFont);
        JDialog dlg = new JDialog(parent, "Choose a font...", true);

        //Create our JPanels and add borders.
        JPanel sizePanel = new JPanel(new GridLayout(2,1));
        JPanel fontPanel = new JPanel(new GridLayout(1,1));
        JPanel stylePanel = new JPanel(new GridLayout(4,1));
        JPanel confirmPanel = new JPanel(new GridLayout(1,2));
        JPanel previewPanel = new JPanel(new GridLayout(1,1));
        sizePanel.setBorder(new EtchedBorder());
        fontPanel.setBorder(new EtchedBorder());
        stylePanel.setBorder(new EtchedBorder());
        confirmPanel.setBorder(new EtchedBorder());
        previewPanel.setBorder(new EtchedBorder());

        //assign our preview text and add it to the panel
        JLabel previewLabel = new JLabel("the quick brown fox jumps over the lazy dog 0123456789");
        previewLabel.setFont(defaultFont);
        previewPanel.add(previewLabel);

        //create our size slider, subscribe to it, and add it to the panel
        JSlider sizeSlider = new JSlider(8, 20, defaultFont.getSize());
        sizeSlider.setValue(defaultFont.getSize());
        sizeSlider.setPaintTicks(true);
        sizePanel.add(new JLabel("Size:"));
        sizePanel.add(sizeSlider);
        sizeSlider.setMajorTickSpacing(1);
        sizeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Font _old = previewLabel.getFont();
                Font _new = new Font(_old.getName(), _old.getStyle(), sizeSlider.getValue());
                previewLabel.setFont(_new);
                returnFont.setFont(_new);
            }
        });

        //confirmation buttons
        JButton okBtn = new JButton("OK");
        JButton noBtn = new JButton("Cancel");
        confirmPanel.add(okBtn);
        confirmPanel.add(noBtn);
        okBtn.addActionListener((ae) -> {
            dlg.setVisible(false);
        });
        noBtn.addActionListener((ae) -> {
            returnFont.setFont(null);
            dlg.setVisible(false);
        });

        //create our style radio buttons, set their mnemonics, subscribe to them, and add them to the panel
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton regButton = new JRadioButton("Regular", true);
        JRadioButton italicButton = new JRadioButton("Italic");
        JRadioButton boldButton = new JRadioButton("Bold");
        regButton.setMnemonic(KeyEvent.VK_R);
        italicButton.setMnemonic(KeyEvent.VK_I);
        boldButton.setMnemonic(KeyEvent.VK_B);
        buttonGroup.add(regButton);
        buttonGroup.add(italicButton);
        buttonGroup.add(boldButton);
        stylePanel.add(new JLabel("Style:"));
        stylePanel.add(regButton);
        stylePanel.add(italicButton);
        stylePanel.add(boldButton);
        regButton.addActionListener((ae) -> {
            Font _old = returnFont.getFont();
            Font _new = new Font(_old.getName(), Font.PLAIN, _old.getSize());
            returnFont.setFont(_new);
        });
        italicButton.addActionListener((ae) -> {
            Font _old = returnFont.getFont();
            Font _new = new Font(_old.getName(), Font.ITALIC, _old.getSize());
            returnFont.setFont(_new);
        });
        boldButton.addActionListener((ae) -> {
            Font _old = returnFont.getFont();
            Font _new = new Font(_old.getName(), Font.BOLD, _old.getSize());
            returnFont.setFont(_new);
        });
        

        //set up our font list, and subscribe to any selections the user makes
        JList<String> fontList = new JList<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        JScrollPane fontPane = new JScrollPane(fontList);
        fontPane.setColumnHeaderView(new JLabel("Fonts:"));
        fontPanel.add(fontPane);
        fontList.setSelectedValue(previewLabel.getFont().getName(), true);
        fontList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                Font _old = previewLabel.getFont();
                Font _new = new Font(fontList.getSelectedValue(), _old.getStyle(), _old.getSize());
                previewLabel.setFont(_new);
                returnFont.setFont(_new);
            }
        });;

        //add all our panels to the frame, and display it.
        dlg.add(sizePanel, BorderLayout.NORTH);
        dlg.add(fontPanel, BorderLayout.WEST);
        dlg.add(stylePanel, BorderLayout.CENTER);
        dlg.add(confirmPanel, BorderLayout.EAST);
        dlg.add(previewPanel, BorderLayout.SOUTH);

        dlg.pack();
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);

        return returnFont.getFont();
    }
}
