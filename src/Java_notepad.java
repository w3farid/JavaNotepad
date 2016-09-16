
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PARVES
 */
public class Java_notepad extends JFrame {

    static JTextArea mainArea;
    JMenuBar mbar;
    JMenu mnuFile, mnuEdit, mnuFormat, mnuHelp;
    JMenuItem itmNew, itmOpen, itmSave, itmSaveAs, itmExit, itmCut,
            itmCopy, itmPaste, itmFontColor, itmFind, itmReplace, itmFont;

    JButton btnOpen;
    JCheckBoxMenuItem wordWrap;

    String fileName;
    JFileChooser fileChooser;
    String fileContent;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    //public static Java_notepad  frmMain =  new Java_notepad();
    FontHelper font;
    JToolBar toolBar;

    public Java_notepad() {

        initComponent();
        font = new FontHelper();
        toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);
        toolBar.add(btnOpen);

        itmSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        itmSaveAs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        itmOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        btnOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        itmNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                open_new();
            }
        });
        itmExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        itmCut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.cut();
            }
        });
        itmCopy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.copy();
            }
        });
        itmPaste.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.paste();
            }
        });
        mainArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.update();
                redoAction.update();
            }

        });

        // mainArea.setWrapStyleWord(true);
        wordWrap.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (wordWrap.isSelected()) {
                    mainArea.setLineWrap(true);
                    mainArea.setWrapStyleWord(true);
                } else {
                    mainArea.setLineWrap(false);
                    mainArea.setWrapStyleWord(false);
                }
            }
        });
        itmFontColor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(itmOpen, "Choose font color", Color.yellow);
                mainArea.setForeground(c);
            }
        });
        itmFind.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(null, false);
            }
        });
        itmReplace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(null, true);
            }
        });
        itmFont.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });

        font.getOk().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.setFont(font.font());
                font.setVisible(false);
            }
        });

        font.getCancel().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(false);
            }
        });

    }

    private void initComponent() {
        fileChooser = new JFileChooser("."); //"." is for open root directory
        mainArea = new JTextArea();
        undo = new UndoManager();
        ImageIcon iconUndo = new ImageIcon(getClass().getResource("/img/undo.gif"));
        ImageIcon iconRedo = new ImageIcon(getClass().getResource("/img/redo.gif"));
        undoAction = new UndoAction(iconUndo);
        redoAction = new RedoAction(iconRedo);

        getContentPane().add(mainArea);
        getContentPane().add(new JScrollPane(mainArea), BorderLayout.CENTER);
        setTitle("Untitled Notepad");
        setSize(800, 600);
        //MenuBar create

        mbar = new JMenuBar();
        mnuFile = new JMenu("File");
        mnuEdit = new JMenu("Edit");
        mnuFormat = new JMenu("Format");
        mnuHelp = new JMenu("Help");

        //Icon Object create
        ImageIcon iconNew = new ImageIcon(getClass().getResource("/img/new.gif"));
        ImageIcon iconOpen = new ImageIcon(getClass().getResource("/img/open.gif"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("/img/save.gif"));
        ImageIcon iconSaveAs = new ImageIcon(getClass().getResource("/img/saveAs.gif"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("/img/exit.gif"));

        ImageIcon iconCut = new ImageIcon(getClass().getResource("/img/cut.gif"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("/img/copy.gif"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("/img/paste.gif"));

        ImageIcon iconFind = new ImageIcon(getClass().getResource("/img/find.gif"));
        ImageIcon iconReplace = new ImageIcon(getClass().getResource("/img/replace.gif"));

        ImageIcon iconFont = new ImageIcon(getClass().getResource("/img/font.gif"));

        btnOpen = new JButton(iconOpen);

        //Create menu item
        itmNew = new JMenuItem("New", iconNew);
        itmOpen = new JMenuItem("Open", iconOpen);
        itmSave = new JMenuItem("Save", iconSave);
        itmSaveAs = new JMenuItem("Save As", iconSaveAs);
        itmExit = new JMenuItem("Exit", iconExit);

        itmCut = new JMenuItem("Cut", iconCut);
        itmCopy = new JMenuItem("Copy", iconCopy);
        itmPaste = new JMenuItem("Paste", iconPaste);
        itmFind = new JMenuItem("Find", iconFind);
        itmReplace = new JMenuItem("Replace", iconReplace);

        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        itmFontColor = new JMenuItem("Font Color");
        itmFont = new JMenuItem("Font", iconFont);

        //ShortCut key setting
        itmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        itmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        itmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        //add menu item
        mnuFile.add(itmNew);
        mnuFile.add(itmOpen);
        mnuFile.add(itmSave);
        mnuFile.add(itmSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(itmExit);

        mnuEdit.add(itmCut);
        mnuEdit.add(itmCopy);
        mnuEdit.add(itmPaste);
        mnuEdit.add(undoAction);
        mnuEdit.add(redoAction);
        mnuEdit.add(itmFind);
        mnuEdit.add(itmReplace);
        mnuEdit.add(itmFont);

        mnuFormat.add(wordWrap);
        mnuFormat.add(itmFontColor);

        //add meubar to frame
        mbar.add(mnuFile);
        mbar.add(mnuEdit);
        mbar.add(mnuFormat);
        mbar.add(mnuHelp);
        //add toot bar with menubar

        //add memu bar with menubar
        setJMenuBar(mbar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        Java_notepad jn = new Java_notepad();
    }

    public static JTextArea getArea() {
        return mainArea;
    }

    private void save() {
        PrintWriter pWriter = null;
        //int retVal = -1;

        try {
            if (fileName == null) {
                saveAs();
            } else {
                pWriter = new PrintWriter(new FileWriter(fileName));

                String s = mainArea.getText();
                StringTokenizer sToken = new StringTokenizer(s, System.getProperty("line.separator"));
                while (sToken.hasMoreElements()) {
                    pWriter.println(sToken.nextToken());

                }
                //JOptionPane.showMessageDialog(rootPane, "File saved");

            }

            //JOptionPane.showMessageDialog(rootPane, "Data saved");
            fileContent = mainArea.getText();

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (pWriter != null) {
                    pWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAs() {
        PrintWriter pWriter = null;
        int retVal = -1;
        try {

            retVal = fileChooser.showSaveDialog(this);

            if (retVal == JFileChooser.APPROVE_OPTION) {

                if (fileChooser.getSelectedFile().exists()) {
                    int option = JOptionPane.showConfirmDialog(rootPane, "Do you want replace file ", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (option == 0) {

                        pWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                        String s = mainArea.getText();
                        StringTokenizer sToken = new StringTokenizer(s, System.getProperty("line.separator"));
                        while (sToken.hasMoreElements()) {
                            pWriter.println(sToken.nextToken());

                        }
                        JOptionPane.showMessageDialog(rootPane, "File saved");
                        fileContent = mainArea.getText();
                        fileName = fileChooser.getSelectedFile().getName();
                        setTitle(fileName = fileChooser.getSelectedFile().getName());
                    } else {
                        saveAs();
                    }
                } else {
                    pWriter = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
                    String s = mainArea.getText();
                    StringTokenizer sToken = new StringTokenizer(s, System.getProperty("line.separator"));
                    while (sToken.hasMoreElements()) {
                        pWriter.println(sToken.nextToken());

                    }
                    JOptionPane.showMessageDialog(rootPane, "File saved");
                    fileContent = mainArea.getText();
                    fileName = fileChooser.getSelectedFile().getName();
                    setTitle(fileName = fileChooser.getSelectedFile().getName());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pWriter != null) {
                pWriter.close();
            }
        }
    }

    private void open() {
        try {
            int retVal = fileChooser.showOpenDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                mainArea.setText(null);
                Reader inReader = new FileReader(fileChooser.getSelectedFile());
                char[] buff = new char[100000];
                int nch;
                while ((nch = inReader.read(buff, 0, buff.length)) != -1) {
                    mainArea.append(new String(buff, 0, nch));
                }

                fileName = fileChooser.getSelectedFile().getName();
                setTitle(fileName = fileChooser.getSelectedFile().getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open_new() {
        if (!mainArea.getText().equals("") && !mainArea.getText().equals(fileContent)) {
            if (fileName == null) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    saveAs();
                    clear();
                } else if (option == 2) {

                } else {
                    clear();
                }
            } else {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    save();
                    clear();
                } else if (option == 2) {
                    clear();
                } else {
                    clear();
                }

            }

        } else {
            clear();
        }

    }

    private void clear() {
        mainArea.setText(null);
        setTitle("Untitled Notepad");
        fileName = null;
        fileContent = null;
    }

    class UndoAction extends AbstractAction {

        public UndoAction(ImageIcon undoIcon) {
            super("Undo", undoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }

    }

    class RedoAction extends AbstractAction {

        public RedoAction(ImageIcon undoIcon) {
            super("Redo", undoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException rx) {
                rx.printStackTrace();
            }
            update();
            undoAction.update();

        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }

    }

}
