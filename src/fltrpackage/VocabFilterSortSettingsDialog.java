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
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class VocabFilterSortSettingsDialog extends JDialog {

	private JCheckBox[] cbStatusFilter;
	private JButton butAllStatusFilter;
	private JButton butNoStatusFilter;
	private JTextField tfWordFilter;
	private JButton butClearWordFilter;
	private JComboBox cbTextFiles;
	private JRadioButton[] rbSortOptions;
	private JRadioButton[] rbMaxResults;
	private JButton butCancel;
	private JButton butGoExport;
	private JButton butGoBrowser;
	private JButton butGo;
	private VocabFilterSortSettingsDialogListener listener;
	private int result;

	public VocabFilterSortSettingsDialog() {
		super();
		setModal(true);
		setTitle("Vocabulary Filter and Sort Options");
		result = 0;
		listener = new VocabFilterSortSettingsDialogListener(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		MigLayout mainPanelLayout = new MigLayout("fill", // Layout Constraints
				"[right]rel[]", // Column constraints
				"[]3[]"); // Row constraints
		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(new JLabel("Status Filter:"), "right");
		cbStatusFilter = new JCheckBox[TermStatus.values().length - 1];
		String currStatuses = "|"
				+ Preferences.getCurrVocabStatusFilter().replaceAll("\\s", "")
				+ "|";
		for (int i = 0; i < cbStatusFilter.length; i++) {
			String status = "|"
					+ String.valueOf(i < 5 ? i + 1 : (i == 5 ? 98 : 99)) + "|";
			cbStatusFilter[i] = new JCheckBox(String.valueOf(i + 1));
			cbStatusFilter[i].setSelected(currStatuses.indexOf(status) >= 0);
			mainPanel.add(cbStatusFilter[i], (i == 0 ? "split"
					: (i == (cbStatusFilter.length - 1) ? "wrap" : "")));
		}
		cbStatusFilter[5].setText("Ign");
		cbStatusFilter[6].setText("WKn");

		mainPanel.add(new JLabel("  "), "right");
		butAllStatusFilter = new JButton("All");
		butAllStatusFilter.addActionListener(listener);
		mainPanel.add(butAllStatusFilter, "split");
		butNoStatusFilter = new JButton("None");
		butNoStatusFilter.addActionListener(listener);
		mainPanel.add(butNoStatusFilter, "wrap");

		mainPanel.add(new JLabel("Word Filter:"), "right");
		tfWordFilter = new JTextField(Preferences.getCurrVocabWordFilter(), 10);
		Utilities.setComponentOrientation(tfWordFilter);
		AbstractDocument doc = (AbstractDocument) tfWordFilter.getDocument();
		doc.setDocumentFilter(new TextFieldCharLimiter(30));
		mainPanel.add(tfWordFilter, "split");
		butClearWordFilter = new JButton("Clear");
		butClearWordFilter.addActionListener(listener);
		mainPanel.add(butClearWordFilter, "");
		mainPanel.add(new JLabel("   Joker: * and ?"), "wrap");

		mainPanel.add(new JLabel("Text Filter:"), "right");
		cbTextFiles = new JComboBox(new Vector<ComboBoxItem>());
		Vector<String> texts = Utilities.getTextFileList(Application
				.getLanguage().getTextDir());
		String textCurr = Preferences.getCurrVocabTextFilter();
		cbTextFiles.addItem(new ComboBoxItem("[All Terms]",
				Constants.MAX_DATA_LENGTH_START_FRAME));
		cbTextFiles.addItem(new ComboBoxItem("[Terms In All Texts]",
				Constants.MAX_DATA_LENGTH_START_FRAME));
		if (textCurr.equals("[All Terms]")) {
			cbTextFiles.setSelectedIndex(0);
		} else if (textCurr.equals("[Terms In All Texts]")) {
			cbTextFiles.setSelectedIndex(1);
		}
		String textMod;
		for (String text : texts) {
			textMod = text.substring(0, text.length()
					- Constants.TEXT_FILE_EXTENSION_LENGTH);
			cbTextFiles.addItem(new ComboBoxItem(textMod,
					Constants.MAX_DATA_LENGTH_START_FRAME));
			if (textCurr.equals(textMod)) {
				cbTextFiles.setSelectedIndex(cbTextFiles.getItemCount() - 1);
			}
		}
		cbTextFiles.setEditable(false);
		cbTextFiles.setMaximumRowCount(20);
		mainPanel.add(cbTextFiles, "wrap");

		mainPanel.add(new JLabel("Sort Order:"), "right");
		int currSort = Preferences.getCurrVocabSortOrder();
		rbSortOptions = new JRadioButton[3];
		ButtonGroup bgSortOptions = new ButtonGroup();
		rbSortOptions[0] = new JRadioButton("A-Z");
		rbSortOptions[0].setSelected(currSort == 0);
		bgSortOptions.add(rbSortOptions[0]);
		mainPanel.add(rbSortOptions[0], "split");
		rbSortOptions[1] = new JRadioButton("Status");
		rbSortOptions[1].setSelected(currSort == 1);
		bgSortOptions.add(rbSortOptions[1]);
		mainPanel.add(rbSortOptions[1], "");
		rbSortOptions[2] = new JRadioButton("Random");
		rbSortOptions[2].setSelected(currSort == 2);
		bgSortOptions.add(rbSortOptions[2]);
		mainPanel.add(rbSortOptions[2], "wrap");

		mainPanel.add(new JLabel("Max. Results:"), "right, gapbottom 10px");
		int currMaxResults = Preferences.getCurrVocabMaxResults();
		rbMaxResults = new JRadioButton[6];
		String[] maxResTexts = { "All", "100", "50", "25", "12", "7" };
		ButtonGroup bgMaxResults = new ButtonGroup();
		for (int i = 0; i < rbMaxResults.length; i++) {
			rbMaxResults[i] = new JRadioButton(maxResTexts[i]);
			rbMaxResults[i].setSelected(currMaxResults == i);
			bgMaxResults.add(rbMaxResults[i]);
			mainPanel.add(rbMaxResults[i], (i == 0 ? "split, " : "")
					+ "gapbottom 10px" + (i == 5 ? ", wrap" : ""));
		}

		butCancel = new JButton("Cancel");
		butCancel.addActionListener(listener);
		mainPanel.add(butCancel, "left");
		butGoExport = new JButton("Export");
		butGoExport.addActionListener(listener);
		mainPanel.add(butGoExport, "split, grow, left");
		butGoBrowser = new JButton("Browser");
		butGoBrowser.addActionListener(listener);
		mainPanel.add(butGoBrowser, "center, grow");
		butGo = new JButton("Review & Edit");
		butGo.addActionListener(listener);
		mainPanel.add(butGo, "right, grow, wrap");

		getContentPane().add(mainPanel);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(butGo);
		rootPane.registerKeyboardAction(listener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		pack();
		setResizable(false);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((d.width - this.getSize().width) / 2,
				(d.height - this.getSize().height) / 2);
		if (!Utilities.isMac()) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(
					this.getClass().getResource(Constants.ICONPATH)));
		}
	}

	public JButton getButAllStatusFilter() {
		return butAllStatusFilter;
	}

	public JButton getButCancel() {
		return butCancel;
	}

	public JButton getButClearWordFilter() {
		return butClearWordFilter;
	}

	public JButton getButGo() {
		return butGo;
	}

	public JButton getButGoBrowser() {
		return butGoBrowser;
	}

	public JButton getButGoExport() {
		return butGoExport;
	}

	public JButton getButNoStatusFilter() {
		return butNoStatusFilter;
	}

	public JCheckBox[] getCbStatusFilter() {
		return cbStatusFilter;
	}

	public JComboBox getCbTextFiles() {
		return cbTextFiles;
	}

	public JRadioButton[] getRbMaxResults() {
		return rbMaxResults;
	}

	public JRadioButton[] getRbSortOptions() {
		return rbSortOptions;
	}

	public JTextField getTfWordFilter() {
		return tfWordFilter;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int showDialog() {
		setVisible(true);
		return result;
	}

}
