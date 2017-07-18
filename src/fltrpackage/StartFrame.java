/*
 *
 * Foreign Language Text Reader (FLTR) - A Tool for Language Learning.
 *
 * Copyright (c) 2012 FLTR Developers.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package fltrpackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class StartFrame extends JFrame {

    private final JButton butChooseMainDir;
    private final JButton butOpenMainDir;
    private final JComboBox cbLang;
    private final JComboBox cbText;
    private final JButton butAbout;
    private final JButton butNewLang;
    private final JButton butEditLang;
    private final JButton butRefresh;
    private final JButton butStart;
    private final JButton butNewText;
    private final JButton butEditText;
    private final JButton butGenSett;
    private final StartFrameListener listener;
    private boolean inSetData;

    public StartFrame() {
        super();
        setTitle(Constants.SHORT_NAME + " - " + Constants.LONG_NAME + " " + Constants.SHORT_VERSION);
        listener = new StartFrameListener(this);
        addWindowListener(listener);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        final String smallButt = "";
        // if (Utilities.isMac()) {
        // new MacMenuController();
        // if (Preferences.getCurrLookAndFeel().equals("system")) {
        // smallButt = "w 40!, ";
        // }
        // }

        final JPanel mainPanel = new JPanel();
        final MigLayout mainLayout = new MigLayout("fill", // Layout Constraints
                "[right]rel[]", // Column constraints
                "[]8[]"); // Row constraints
        mainPanel.setLayout(mainLayout);

        mainPanel.add(new JLabel(Constants.SHORT_NAME + " Directory:"), "");

        butChooseMainDir = new JButton();
        butChooseMainDir.addActionListener(listener);
        mainPanel.add(butChooseMainDir, "split 2, grow");
        butOpenMainDir = new JButton("View…");
        butOpenMainDir.addActionListener(listener);
        mainPanel.add(butOpenMainDir, "wrap");

        mainPanel.add(new JLabel("Language:"), "");

        butNewLang = new JButton("+");
        butNewLang.addActionListener(listener);

        mainPanel.add(butNewLang, smallButt + "split 3");
        cbLang = new JComboBox(new Vector<ComboBoxItem>());
        cbLang.setEditable(false);
        cbLang.setMaximumRowCount(20);
        cbLang.addActionListener(listener);
        mainPanel.add(cbLang, "grow");
        butEditLang = new JButton("Edit…");
        butEditLang.addActionListener(listener);
        mainPanel.add(butEditLang, "wrap");

        mainPanel.add(new JLabel("Text:"), "");

        butNewText = new JButton("+");
        butNewText.addActionListener(listener);
        mainPanel.add(butNewText, smallButt + "split 3");
        cbText = new JComboBox(new Vector<ComboBoxItem>());
        cbText.setEditable(false);
        cbText.setMaximumRowCount(20);
        cbText.addActionListener(listener);
        mainPanel.add(cbText, "grow");
        butEditText = new JButton("Edit…");
        butEditText.addActionListener(listener);
        mainPanel.add(butEditText, "wrap");

        butAbout = new JButton("About…");
        butAbout.addActionListener(listener);
        mainPanel.add(butAbout, "grow");

        butGenSett = new JButton("General Settings…");
        butGenSett.addActionListener(listener);
        mainPanel.add(butGenSett, "split 3");
        butRefresh = new JButton("Refresh");
        butRefresh.addActionListener(listener);
        mainPanel.add(butRefresh, "grow");
        butStart = new JButton("Read/Study…");
        butStart.addActionListener(listener);
        mainPanel.add(butStart, "grow");

        getContentPane().add(mainPanel);

        final JRootPane rootPane = getRootPane();
        rootPane.setDefaultButton(butStart);

        setResizable(false);
        inSetData = false;
        setDataAndPack();

        final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(Preferences.getCurrXPosStartWindow((d.width - this.getSize().width) / 2),
                Preferences.getCurrYPosStartWindow((d.height - this.getSize().height) / 2));
        getContentPane().addHierarchyBoundsListener(listener);

        if (!Utilities.isMac()) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Constants.ICONPATH)));
        }
    }

    public JButton getButAbout() {
        return butAbout;
    }

    public JButton getButChooseMainDir() {
        return butChooseMainDir;
    }

    public JButton getButEditLang() {
        return butEditLang;
    }

    public JButton getButEditText() {
        return butEditText;
    }

    public JButton getButGenSett() {
        return butGenSett;
    }

    public JButton getButNewLang() {
        return butNewLang;
    }

    public JButton getButNewText() {
        return butNewText;
    }

    public JButton getButOpenMainDir() {
        return butOpenMainDir;
    }

    public JButton getButRefresh() {
        return butRefresh;
    }

    public JButton getButStart() {
        return butStart;
    }

    public JComboBox getCbLang() {
        return cbLang;
    }

    public JComboBox getCbText() {
        return cbText;
    }

    public void setDataAndPack() {
        Application.checkAndInitBaseDirAndLanguage();
        setDataAndPack1();
        setDataAndPack1();
    }

    private void setDataAndPack1() {
        if (inSetData) {
            return;
        }
        inSetData = true;
        butStart.setEnabled(false);
        butNewLang.setEnabled(false);
        butEditLang.setEnabled(false);
        butNewText.setEnabled(false);
        butOpenMainDir.setEnabled(false);
        butEditText.setEnabled(false);
        cbLang.removeAllItems();
        cbText.removeAllItems();
        final String currMainDir = Preferences.getCurrMainDir();
        final String currLang = Preferences.getCurrLang();
        final String currText = Preferences.getCurrText();
        getButChooseMainDir().setText(Utilities.limitStringRight(Constants.MAX_DATA_LENGTH_START_FRAME, currMainDir));
        final File fcurrMainDir = Application.getBaseDir();
        if (fcurrMainDir != null) {
            butNewLang.setEnabled(true);
            butOpenMainDir.setEnabled(true);
            final Vector<String> langs = Utilities.getSubDirectories(fcurrMainDir);
            if (langs.isEmpty()) {
                cbLang.addItem(new ComboBoxItem("[None]", Constants.MAX_LANG_LENGTH_START_FRAME));
                cbText.addItem(new ComboBoxItem("[None]", Constants.MAX_TEXT_LENGTH_START_FRAME));
            } else {
                String langMod;
                int currIndex = -1;
                for (final String lang : langs) {
                    langMod = lang.substring(0, lang.length() - Constants.TEXT_DIR_SUFFIX_LENGTH);
                    cbLang.addItem(new ComboBoxItem(langMod, Constants.MAX_LANG_LENGTH_START_FRAME));
                    if (langMod.equals(currLang)) {
                        currIndex = cbLang.getItemCount() - 1;
                    }
                }
                if (currIndex >= 0) {
                    cbLang.setSelectedIndex(currIndex);
                    butEditLang.setEnabled(true);
                    butNewText.setEnabled(true);
                }
                final File fcurrLangDir = new File(fcurrMainDir, currLang + Constants.TEXT_DIR_SUFFIX);
                if (fcurrLangDir.isDirectory()) {
                    final Vector<String> texts = Utilities.getTextFileList(fcurrLangDir);
                    if (texts.isEmpty()) {
                        cbText.addItem(new ComboBoxItem("[None]", Constants.MAX_TEXT_LENGTH_START_FRAME));
                    } else {
                        String textMod;
                        int currIndexT = -1;
                        for (final String text : texts) {
                            textMod = text.substring(0, text.length() - Constants.TEXT_FILE_EXTENSION_LENGTH);
                            cbText.addItem(new ComboBoxItem(textMod, Constants.MAX_TEXT_LENGTH_START_FRAME));
                            if (textMod.equals(currText)) {
                                currIndexT = cbText.getItemCount() - 1;
                            }
                        }
                        textMod = "<Vocabulary>";
                        cbText.addItem(new ComboBoxItem(textMod, Constants.MAX_TEXT_LENGTH_START_FRAME));
                        if (textMod.equals(currText)) {
                            currIndexT = cbText.getItemCount() - 1;
                        }
                        if (currIndexT >= 0) {
                            cbText.setSelectedIndex(currIndexT);
                        }
                        butStart.setEnabled(true);
                        butEditText.setEnabled((!Preferences.getCurrText().equals("<Vocabulary>")));
                    }
                } else {
                    cbText.addItem(new ComboBoxItem("[None]", Constants.MAX_TEXT_LENGTH_START_FRAME));
                }
            }
        } else {
            getButChooseMainDir().setText("[Select…]");
            cbLang.addItem(new ComboBoxItem("[None]", Constants.MAX_LANG_LENGTH_START_FRAME));
            cbText.addItem(new ComboBoxItem("[None]", Constants.MAX_TEXT_LENGTH_START_FRAME));
        }
        pack();
        if (butStart.isEnabled()) {
            getRootPane().setDefaultButton(butStart);
            butStart.requestFocusInWindow();
        }
        inSetData = false;
    }

}
