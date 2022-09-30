//
//  Name: Triance, Nick
//  Date: 03 Dec. 2021
//  Description:
//              Clone of Windows Notepad using Java and the Swing Library

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.*;
import java.awt.print.*;
import java.io.*;



public class Notepad {

    //components that it will be useful to have global access to
    private JFrame frame;
    private JTextArea jTextArea;
    private JPopupMenu popupMenu;
    private UndoManager undo;

    private ImageIcon icon = new ImageIcon("Notepad.png");

    private boolean unsavedChanges = false;
    private String documentName = "Untitled";

    private boolean wordWrapEnabled = false;
    private int currentZoom = Defaults.DEFAULT_FONT_SIZE;
    private boolean isNewDocument;
    private String filepath;
    
    /**
     * Creates a new Notepad window for an untitled document
     */
    public Notepad() {
       this("Untitled");
       isNewDocument = true;
    }

    /**
     * Creates a new Notepad window with the given document name.
     * @param docName
     */
    public Notepad(String docName) {
        
        frame = createFrame();
        setDocumentName(docName);
        undo = new UndoManager();
        
        //keeping track of changes to the document
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {

            //shows unsaved changes when something is removed to the text area
            public void insertUpdate(DocumentEvent e) {
                unsavedChanges = true;
                updateFrameTitle();
            }

            //shows unsaved changes when something is removed to the text area
            public void removeUpdate(DocumentEvent e) {
                unsavedChanges = true;
                updateFrameTitle();
            }

            //this method needs to exist, but does not need to do anything.
            public void changedUpdate(DocumentEvent e) {}
            
        });
        
        jTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
            }
        });
        
        isNewDocument = false;
        filepath = docName;
        frame.setVisible(true);
    }

    /**
     * Builds the JFrame for the Notepad, and sets it to create an untitled document. by default the window is 800 by 600
     * @return <code> JFrame </code> which has been created
     */
    private JFrame createFrame() {
        
        //creating frame
        JFrame _jfrm = new JFrame();
        _jfrm.setSize(800,600);
        _jfrm.setLayout(new BorderLayout());

        //initializing text area
        jTextArea = new JTextArea();
        jTextArea.setFont(Defaults.DEFAULT_FONT);
        JScrollPane _jsp = new JScrollPane(jTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        _jfrm.add(_jsp);

        //handling closing
        _jfrm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        _jfrm.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (!unsavedChanges) {
                    System.exit(0);
                }   else {
                    int exitChoice = JOptionPane.showConfirmDialog(frame, "Would you like to save the changes to " + documentName + "?", "Warning: Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (exitChoice == JOptionPane.YES_OPTION) {
                        saveFile();
                        System.exit(0);
                    }
                    else if(exitChoice == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });

        //creating menus
        _jfrm.setJMenuBar(createJMenuBar());

        //create and set up the popup menu
        popupMenu = createPopupMenu();
        jTextArea.addMouseListener(new MouseInputListener() {

            //listening for right click on the text area
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(jTextArea, e.getX(), e.getY());
                }
            }

            //these methods need to exist, but do not need to do anything.
            public void mouseClicked(java.awt.event.MouseEvent e) {}
            public void mousePressed(java.awt.event.MouseEvent e) {}
            public void mouseEntered(java.awt.event.MouseEvent e) {}
            public void mouseExited(java.awt.event.MouseEvent e) {}
            public void mouseDragged(java.awt.event.MouseEvent e) {}
            public void mouseMoved(java.awt.event.MouseEvent e) {}
        });

        _jfrm.setIconImage(icon.getImage());

        return _jfrm;
    }

    /**
     * creates a <code>JPopupMenu</code> that has cut, copy, and paste functionality
     * @return <code>JPopupMenu</code> popupmenu
     */
    private JPopupMenu createPopupMenu() {
       
        JPopupMenu _jpm = new JPopupMenu("Hello");
        
        JMenuItem jpmCut = new JMenuItem("Cut");
        jpmCut.setMnemonic('t');
        jpmCut.addActionListener((ae) ->{
            jTextArea.cut();
        });
        _jpm.add(jpmCut);

        JMenuItem jpmCopy = new JMenuItem("Copy");
        jpmCopy.setMnemonic('C');
        jpmCopy.addActionListener((ae) -> {
            jTextArea.copy();
        });
        _jpm.add(jpmCopy);

        JMenuItem jpmPaste = new JMenuItem("Paste");
        jpmPaste.setMnemonic('P');
        jpmPaste.addActionListener((ae) -> {
            jTextArea.paste();
        });
        _jpm.add(jpmPaste);

        return _jpm;
    }

    /**
     * Builds the JMenuBar for the NotePad
     * @return <code>JMenuBar</code> constructed for the notepad.
     */
    private JMenuBar createJMenuBar() {
       
        JMenuBar _jmb = new JMenuBar();
        _jmb.add(createFileMenu());
        _jmb.add(createEditMenu());
        _jmb.add(createFormatMenu());
        _jmb.add(createViewMenu());
        _jmb.add(createHelpMenu());

        return _jmb;
    }

    /**
     * Builds the help menu for the notepad
     * @return <code>JMenu</code>, the Notepad's help menu
     */
    private JMenu createHelpMenu() {
        
        JMenu jmHelp = new JMenu("Help");
        jmHelp.setMnemonic('H');

        JMenuItem jmiHelp = new JMenuItem("View Help");
        jmiHelp.setMnemonic('H');
        jmiHelp.addActionListener((ae) -> {
            URI helpURI;
            try {
                helpURI = new URI("https://www.google.com/search?q=get+help+with+notepad+in+windows");
                try {
                    java.awt.Desktop.getDesktop().browse(helpURI);
                } catch (IOException e) {
                    e.printStackTrace(); //if we can't open the browser it's not a huge deal, so we'll just squash it.
                }
            } catch (URISyntaxException ignored) {
                //this should never fire as our URI is hardcoded, and thus will never have a syntax exception.
            }
        });
        jmHelp.add(jmiHelp);

        JMenuItem jmiEC = new JMenuItem("Extra Credits...");
        jmiEC.setMnemonic('x');
        jmiEC.addActionListener((ae) -> {
            ExtraCredits.showDialog(frame);
        });
        jmHelp.add(jmiEC);

        jmHelp.addSeparator();
        
        JMenuItem jmiAbout = new JMenuItem("About...");
        jmiAbout.setMnemonic('A');
        jmiAbout.addActionListener((ae) -> {
 
            //if we just give the message dialog our application image, it's gonna be huge, so we're gonna downscale it first.
            Image iconImg = icon.getImage();
            Image scaledImg = iconImg.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);
            JOptionPane.showMessageDialog(frame, "Notepad V0.1 by N.Triance", "About", JOptionPane.INFORMATION_MESSAGE, scaledIcon);
        });
        jmHelp.add(jmiAbout);
        
        return jmHelp;
    }

    /**
     * Builds the view menu for the notepad
     * @return <code>JMenu</code>, the Notepad view menu
     */
    private JMenu createViewMenu() {

        JMenu jmView = new JMenu("View");
        jmView.setMnemonic('V');

        
        JMenu jmZoom = new JMenu("Zoom");
        jmZoom.setMnemonic('Z');
        jmView.add(jmZoom);
        
        JMenuItem jmiZoomIn = new JMenuItem("Zoom in");
        jmiZoomIn.setMnemonic('i');
        jmiZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK));
        jmiZoomIn.addActionListener((ae) -> {
            currentZoom+=Defaults.ZOOM_INCREMENT;
            Font _font = new Font(jTextArea.getFont().getName(), jTextArea.getFont().getStyle(), currentZoom);
            jTextArea.setFont(_font);
        });
        jmZoom.add(jmiZoomIn);

        JMenuItem jmiZoomOUt = new JMenuItem("Zoom out");
        jmiZoomOUt.setMnemonic('o');
        jmiZoomOUt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK));
        jmiZoomOUt.addActionListener((ae) -> {
            currentZoom-=Defaults.ZOOM_INCREMENT;
            if (currentZoom <= 2) { //cap minimum zoom at 2
                currentZoom = 2;
            }
            Font _font = new Font(jTextArea.getFont().getName(), jTextArea.getFont().getStyle(), currentZoom);
            jTextArea.setFont(_font);
        });
        jmZoom.add(jmiZoomOUt);
        
        JMenuItem jmiResetZoom = new JMenuItem("Restore Default Zoom");
        jmiResetZoom.setMnemonic('R');
        jmiResetZoom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_DOWN_MASK));
        jmiResetZoom.addActionListener((ae) -> {
            currentZoom = Defaults.DEFAULT_FONT_SIZE;
            Font _font = new Font(jTextArea.getFont().getName(), jTextArea.getFont().getStyle(), currentZoom);
            jTextArea.setFont(_font);
        });
        jmZoom.add(jmiResetZoom);
        
        return jmView;
    }

    /**
     * Builds the format menu for the notepad
     * @return <code>JMenu</code>, the Notepad's format menu
     */
    private JMenu createFormatMenu() {
        
        JMenu jmFormat = new JMenu("Format");
        jmFormat.setMnemonic('o');

        JCheckBoxMenuItem jmiWordWrap = new JCheckBoxMenuItem("Word Wrap", wordWrapEnabled);
        jmiWordWrap.setMnemonic('W');
        jmiWordWrap.addActionListener((ae) -> {
            wordWrapEnabled = !wordWrapEnabled;
            jTextArea.setLineWrap(wordWrapEnabled);
            jmiWordWrap.setState(wordWrapEnabled);
        });
        jmFormat.add(jmiWordWrap);

        JMenuItem jmiFont = new JMenuItem("Font...");
        jmiFont.addActionListener((ae) -> {
            Font _font = JFontChooser.showDialog(frame, jTextArea.getFont());
            jTextArea.setFont(_font);
        });
        jmiFont.setMnemonic('F');
        jmFormat.add(jmiFont);

        return jmFormat;
    }

    /**
     * Builds the edit menu for the notepad
     * @return <code>JMenu</code>, the Notepad's edit menu
     */
    private JMenu createEditMenu() {
        
        JMenu jmEdit = new JMenu("Edit");
        jmEdit.setMnemonic('E');

        JMenuItem jmiUndo = new JMenuItem("Undo");
        jmiUndo.setMnemonic('U');
        jmiUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        jmiUndo.addActionListener((ae) -> {
            try{
                if(undo.canUndo()) {
                    undo.undo();
                }
            } catch (CannotUndoException ignored) {
                //the expected behaviour if there is nothing to undo is for nothing to happen.
            }
        });
        jmEdit.add(jmiUndo);

        JMenuItem jmiRedo = new JMenuItem("Redo");
        jmiRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        jmiRedo.addActionListener((ae) -> {
            try{
                if(undo.canRedo()) {
                    undo.redo();
                }
            } catch (CannotUndoException ignored) {
                //the expected behaviour if there is nothing to redo is for nothing to happen.
            }
        });
        jmEdit.add(jmiRedo);

        jmEdit.addSeparator();

        JMenuItem jmiCut = new JMenuItem("Cut");
        jmiCut.setMnemonic('t');
        jmiCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        jmiCut.addActionListener((ae) -> {
            jTextArea.cut();
        });
        jmEdit.add(jmiCut);

        JMenuItem jmiCopy = new JMenuItem("Copy");
        jmiCopy.setMnemonic('C');
        jmiCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        jmiCopy.addActionListener((ae) -> {
            jTextArea.copy();
        });
        jmEdit.add(jmiCopy);

        JMenuItem jmiPaste = new JMenuItem("Paste");
        jmiPaste.setMnemonic('P');
        jmiPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        jmiPaste.addActionListener((ae) -> {
            jTextArea.paste();
        });
        jmEdit.add(jmiPaste);

        JMenuItem jmiDelete = new JMenuItem("Delete");
        jmiDelete.setMnemonic('l');
        jmiDelete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        jmiDelete.addActionListener((ae) -> {
            jTextArea.replaceSelection("");
        });
        jmEdit.add(jmiDelete);

        jmEdit.addSeparator();

        JMenuItem jmiFind = new JMenuItem("Find...");
        jmiFind.setMnemonic('F');
        jmiFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        jmiFind.addActionListener((ae) -> {
            FindDlg.showDialog(frame, jTextArea);
        });
        jmEdit.add(jmiFind);

        JMenuItem jmiFindNext = new JMenuItem("Find Next...");
        jmiFindNext.setMnemonic('N');
        jmiFindNext.addActionListener((ae) -> {
            FindNextDlg.showDialog(frame, jTextArea);
        });
        jmEdit.add(jmiFindNext);

        JMenuItem jmiReplace = new JMenuItem("Replace...");
        jmiReplace.setMnemonic('R');
        jmiReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        jmiReplace.addActionListener((ae) -> {
            ReplaceDlg.showDialog(frame, jTextArea);
        });
        jmEdit.add(jmiReplace);

        JMenuItem jmiGoTo = new JMenuItem("Go to...");
        jmiGoTo.setMnemonic('G');
        jmiGoTo.addActionListener((ae) -> {
            int offset = getCursorLine();
            int lineNo = GotoDlg.showDialog(frame, ++offset);
            if (lineNo > 0) {
                int index = getLineStartIndex(jTextArea, lineNo);
                if (index != -1) {
                    jTextArea.setCaretPosition(index);
                    jTextArea.requestFocus();
                }
            }
        });
        jmiGoTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        jmEdit.add(jmiGoTo);

        jmEdit.addSeparator();
        
        JMenuItem jmiSelectAll = new JMenuItem("Select All");
        jmiSelectAll.setMnemonic('A');
        jmiSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        jmiSelectAll.addActionListener((ae) -> {
            jTextArea.selectAll();
        });
        jmEdit.add(jmiSelectAll);

        JMenuItem jmiTimeDate = new JMenuItem("Time/Date");
        jmiTimeDate.setMnemonic('D');
        jmiTimeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
        jmiTimeDate.addActionListener((ae) -> {
            DateFormat _format = new SimpleDateFormat("hh:mm aa MM/dd/YYYY");
            String _dateString = _format.format(new Date());
            jTextArea.insert(_dateString, jTextArea.getCaretPosition());
        });
        jmEdit.add(jmiTimeDate);
        jmEdit.addSeparator();

        JMenu jmColor = createColorMenu();
        jmEdit.add(jmColor);
        
        return jmEdit;
    }

    /**
     * Helper method for creating the menu to change the notepad's colors
     * @return <code>JMenu</code> the color menu.
     */
    private JMenu createColorMenu() {
        
        JMenu jmColor = new JMenu("Colors");
        jmColor.setMnemonic('o');

        JCheckBoxMenuItem jmiLight = new JCheckBoxMenuItem("Light", true);
        JCheckBoxMenuItem jmiDark = new JCheckBoxMenuItem("Dark", false);
        jmiLight.addActionListener((ae) -> {
            jmiLight.setSelected(true);
            jmiDark.setSelected(false);
            jTextArea.setBackground(Color.WHITE);
            jTextArea.setForeground(Color.BLACK);
        });
        jmiDark.addActionListener((ae) -> {
            jmiDark.setSelected(true);
            jmiLight.setSelected(false);
            jTextArea.setBackground(Color.DARK_GRAY);
            jTextArea.setForeground(Color.WHITE);
        });
        jmiLight.setMnemonic('L');
        jmiDark.setMnemonic('D');
        jmColor.add(jmiLight);
        jmColor.add(jmiDark);

        JMenu jmCustom = new JMenu("Custom");
        jmCustom.setMnemonic('C');
        jmColor.add(jmCustom);

        JMenuItem jmiFG = new JMenuItem("Foreground color...");
        jmiFG.setMnemonic('F');
        jmiFG.addActionListener((ae) -> {
            jmiLight.setSelected(false);
            jmiDark.setSelected(false);
            Color fgColor = JColorChooser.showDialog(frame, "Pick a foreground color...", Color.BLACK);
            jTextArea.setForeground(fgColor);
        });
        jmCustom.add(jmiFG);

        JMenuItem jmiBG = new JMenuItem("Backgroud color...");
        jmiBG.setMnemonic('B');
        jmiBG.addActionListener((ae) -> {
            jmiLight.setSelected(false);
            jmiDark.setSelected(false);
            Color bgColor = JColorChooser.showDialog(frame, "Pick a Background color...", Color.WHITE);
            jTextArea.setBackground(bgColor);
        });
        jmCustom.add(jmiBG);
        
        return jmColor;
    }

    /**
     * Builds the file menu for the notepad
     * @return <code>JMenu</code>, the Notepad's file menu
     */
    private JMenu createFileMenu() {
       
        JMenu jmFile = new JMenu("File");
        jmFile.setMnemonic('F');
        
        JMenuItem jmiNew = new JMenuItem("New");
        jmiNew.setMnemonic('N');
        jmiNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        jmiNew.addActionListener((ae) -> {
            if(!unsavedChanges) {
                clearNotepad();
            } else {
                int newChoice = JOptionPane.showConfirmDialog(frame, "Would you like to save the changes to " + documentName + "?", "Warning: Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);
                if (newChoice == JOptionPane.YES_OPTION) {
                    saveFile();
                    clearNotepad();
                }
                else if(newChoice == JOptionPane.NO_OPTION) {
                    clearNotepad();
                }
            }
        });
        jmFile.add(jmiNew);

        JMenuItem jmiOpen = new JMenuItem("Open...");
        jmiOpen.setMnemonic('O');
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        jmiOpen.addActionListener((ae) -> {
            
            //set up the file chooser, we want to default to be able to open text docs and java files
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Text Documents", "txt");
            FileNameExtensionFilter javaFilter = new FileNameExtensionFilter("Java Source Files", "java");
            fileChooser.setFileFilter(textFilter);
            fileChooser.addChoosableFileFilter(textFilter);
            fileChooser.addChoosableFileFilter(javaFilter);

            int returnVal = fileChooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (unsavedChanges) {
                    int openChoice = JOptionPane.showConfirmDialog(frame, "Would you like to save the changes to " + documentName + "?", "Warning: Unsaved Changes", JOptionPane.YES_NO_OPTION);
                    if (openChoice == JOptionPane.NO_OPTION) {
                        clearNotepad();
                        String _filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        openFile(_filePath);
                    } else if (openChoice == JOptionPane.YES_OPTION) {
                        saveFile();
                        clearNotepad();
                        String _filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        openFile(_filePath);
                    }
                } else {
                    clearNotepad();
                    String _filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    openFile(_filePath);
                }
            }
        });
        jmFile.add(jmiOpen);

        JMenuItem jmiSave = new JMenuItem("Save");
        jmiSave.setMnemonic('S');
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        jmiSave.addActionListener((ae) -> {
            saveFile();
        });
        jmFile.add(jmiSave);

        JMenuItem jmiSaveAs = new JMenuItem("Save As...");
        jmiSaveAs.setMnemonic('A');
        jmiSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK));
        jmiSaveAs.addActionListener((ae) -> {
            saveFileAs();
        });
        jmFile.add(jmiSaveAs);

        jmFile.addSeparator();

        //on my machine, the page setup button just crashes notepad. I have no idea what it's actually supposed to do.
        JMenuItem jmiPageSetup = new JMenuItem("Page setup...");
        jmiPageSetup.setMnemonic('u');
        jmiPageSetup.addActionListener((ae) -> {
            int _lol = JOptionPane.showConfirmDialog(frame, "On my machine, Page setup simply crashes Windows Notepad. Upon a Google search, \nthis seems to be a common issue of unknown origin. As such, I have no idea what this button is actually \n supposed to do, so I'll just copy what Windows Notepad does. Press OK to crash the program.", "[WARNING] So about that...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (_lol == JOptionPane.OK_OPTION) {
                //! THE ENTIRE PURPOSE OF THIS IS TO CRASH THE NOTEPAD, BECAUSE THAT'S WHAT WINDOWS NOTEPAD DOES ON MY PC AND I THOUGHT IT WOULD BE FUNNY.
                Object [] o = null;
                while (true) {
                    o = new Object[] {o}; //crash the JVM with a giant object array.
                }
            }
        });
        jmFile.add(jmiPageSetup);

        JMenuItem jmiPrint = new JMenuItem("Print...");
        jmiPrint.setMnemonic('P');
        jmiPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        jmiPrint.addActionListener((ae) -> {
            PrinterJob pj = PrinterJob.getPrinterJob();
            Printer printer = new Printer();
            pj.setPrintable(printer);
            boolean doPrint = pj.printDialog();
            if (doPrint) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.out.println("Error while printing: " + e.toString());
                    e.printStackTrace();
                }
            }
        });
        jmFile.add(jmiPrint);

        jmFile.addSeparator();

        JMenuItem jmiExit = new JMenuItem("Exit");
        jmiExit.setMnemonic('x');
        jmiExit.addActionListener((ae) -> {
            if(!unsavedChanges) {
                System.exit(0);
            } else {
                int exitChoice = confirmExit();
                if (exitChoice == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });
        jmFile.add(jmiExit);


        return jmFile;
    }

    /**
     * Open a file from the path in the notepad
     * @param path <code>String</code> path to file.
     */
    public void openFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            String contents = new String();
            
            while(scanner.hasNext()) {
                contents+=scanner.nextLine();
                contents+="\n";
            }

            jTextArea.setText(contents);
            unsavedChanges = false;
            setDocumentName(file.getName());
            scanner.close();
            this.filepath = path;
            isNewDocument = false;

        } catch (Exception e) {
            System.out.println("Error while opening file: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Saves the file at whatever filepath is currently open
     */
    private void saveFile() {

        if (!isNewDocument) {
            File file = new File(filepath);
            try {
                FileWriter fileWriter = new FileWriter(file, false);
                BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

                bufferWriter.write(jTextArea.getText());

                bufferWriter.flush();
                bufferWriter.close();
                this.unsavedChanges = false;
                updateFrameTitle();
            } catch (Exception e) {
                System.out.println("error while saving: " + e.toString());
            }
        } else {
            saveFileAs();
        }
    }

    /**
     * Opens the Save As dialog.
     */
    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int saveChoice = fileChooser.showSaveDialog(frame);
        if (saveChoice == JFileChooser.APPROVE_OPTION) {
            this.filepath = fileChooser.getSelectedFile().getAbsolutePath();
            this.isNewDocument = false;
            setDocumentName(fileChooser.getSelectedFile().getName());
            saveFile();
        }
    }
    
    /**
     * Clears the notepad, also resets the font. Does not reset color.
     */
    private void clearNotepad() {
        setDocumentName("Untitled");
        jTextArea.setText("");
        unsavedChanges = false;
        filepath = "Untitled";
        isNewDocument = true;
        jTextArea.setFont(Defaults.DEFAULT_FONT);
        undo.discardAllEdits();
        updateFrameTitle();
    }

    /**
     * shows a confirmation dialog if the user is trying to exit with unsaved changes.
     * @return <code>int</code> choice the exit picks from the dialog.
     */
    private int confirmExit() {
        int _exitChoice = JOptionPane.showConfirmDialog(frame, "Would you like to save the changes to " + documentName + "?", "Warning: Unsaved Changes", JOptionPane.OK_CANCEL_OPTION);
        return _exitChoice;
    }

    /**
     * Gets the index for where to place the cursor given a line number
     * @param textComp reference to the notepad's <code>JTextArea</code>
     * @param lineNumber the desired line number
     * @return <code>int</code> index to place the cursor at
     */
    private int getLineStartIndex(JTextArea jta, int lineNumber) {
        if (lineNumber == 0)  {
            return 0; 
        }
        
        try {
            return jta.getLineStartOffset(lineNumber-1);
        } catch (Exception e) { 
            System.out.println("Error while getting line start offset: " + e.toString());
            return -1; 
        }
        
    }

    /**
     * Gets what line the cursor is currently on
     * @return <code>int</code> line number.
     */
    private int getCursorLine() {
        int offset = jTextArea.getCaretPosition();
        try {
            offset = jTextArea.getLineOfOffset(offset);
        } catch (Exception e) {
            offset = 1;
            e.printStackTrace();
        }
        return offset;
    }

    /**
     * Sets the name of the active document and updates the title of the frame.
     * @param name <code>String</code> name of document.
     */
    public void setDocumentName(String name) {
        this.documentName = name;
        updateFrameTitle();
    }

    /**
     * Updates the title of the frame. Uses the current document name, and appends * to it if there are unsaved changes.
     */
    private void updateFrameTitle() {
        String newTitle = this.documentName + " - Notepad";
        if (unsavedChanges) {
            newTitle += "*";
        }
        frame.setTitle(newTitle);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //check to see if we have been passed any arguments, if we have, use it as a file name.
                if (args.length > 0) {
                    Notepad notepad = new Notepad(args[0]);
                    notepad.openFile(args[0]);
                } else {
                    new Notepad();
                }
            }
        });
    }
}