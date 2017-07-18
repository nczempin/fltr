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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TermFrame extends JFrame {

	private MultiLineTextField tfTerm;
	private MultiLineTextField tfTranslation;
	private MultiLineTextField tfRomanization;
	private MultiLineTextField tfSentence;
	private JRadioButton[] rbStatus;
	private JComboBox cbSimilar;
	private JButton butDelete;
	private JButton butSave;
	private JButton butLookup1;
	private JButton butLookup2;
	private JButton butLookup3;
	private TermFrameListener listener;
	private String originalKey;

	public TermFrame() {
		super();
		setTitle("Term");
		listener = new TermFrameListener(this);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		originalKey = "";

		JPanel mainPanel = new JPanel();
		MigLayout mainPanelLayout = new MigLayout("fill", // Layout Constraints
				"[right]rel[]", // Column constraints
				"[]3[]"); // Row constraints
		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(new JLabel("Term:"), "right");
		tfTerm = new MultiLineTextField("", 200, 2, 35, this);
		mainPanel.add(tfTerm.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Similar Terms:"), "right");
		cbSimilar = new JComboBox(new Vector<ComboBoxItem>());
		cbSimilar.setEditable(false);
		cbSimilar.setMaximumRowCount(Constants.MAX_SIMILAR_TERMS + 1);
		mainPanel.add(cbSimilar, "wrap");

		mainPanel.add(new JLabel("Translation:"), "right");
		tfTranslation = new MultiLineTextField("", 200, 2, 35, this);
		mainPanel.add(tfTranslation.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Romanization:"), "right");
		tfRomanization = new MultiLineTextField("", 200, 2, 35, this);
		mainPanel.add(tfRomanization.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Sentence:"), "right");
		tfSentence = new MultiLineTextField("", 400, 2, 35, this);
		mainPanel.add(tfSentence.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Status:"), "gapbottom 10px, right");
		rbStatus = new JRadioButton[TermStatus.values().length - 1];
		ButtonGroup bgStatus = new ButtonGroup();
		for (int i = 0; i < rbStatus.length; i++) {
			rbStatus[i] = new JRadioButton(String.valueOf(i + 1));
			rbStatus[i].addActionListener(listener);
			bgStatus.add(rbStatus[i]);
			mainPanel.add(rbStatus[i], "gapbottom 10px"
					+ (i == 0 ? ", split"
							: (i == (rbStatus.length - 1) ? ", wrap" : "")));
		}
		rbStatus[5].setText("Ign");
		rbStatus[6].setText("WKn");

		butDelete = new JButton("Delete");
		butDelete.addActionListener(listener);
		mainPanel.add(butDelete, "left");

		Language lang = Application.getLanguage();

		butLookup1 = new JButton("Dict1");
		butLookup1.setEnabled(lang.getDictionaryURL1().startsWith(
				Constants.URL_BEGIN));
		butLookup1.addActionListener(listener);
		mainPanel.add(butLookup1, "split 4, right");

		butLookup2 = new JButton("Dict2");
		butLookup2.setEnabled(lang.getDictionaryURL2().startsWith(
				Constants.URL_BEGIN));
		butLookup2.addActionListener(listener);
		mainPanel.add(butLookup2, "center");

		butLookup3 = new JButton("Dict3");
		butLookup3.setEnabled(lang.getDictionaryURL3().startsWith(
				Constants.URL_BEGIN));
		butLookup3.addActionListener(listener);
		mainPanel.add(butLookup3, "left");

		butSave = new JButton("Save");
		butSave.addActionListener(listener);
		mainPanel.add(butSave, "gapleft 30px, grow, right");

		getContentPane().add(mainPanel);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(butSave);

		pack();
		setResizable(false);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(Preferences.getCurrXPosTermWindow((d.width - this
				.getSize().width) / 2), Preferences
				.getCurrYPosTermWindow((d.height - this.getSize().height) / 2));
		getContentPane().addHierarchyBoundsListener(listener);

		if (!Utilities.isMac()) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(
					this.getClass().getResource(Constants.ICONPATH)));
		}

	}

	public JButton getButDelete() {
		return butDelete;
	}

	public JButton getButLookup1() {
		return butLookup1;
	}

	public JButton getButLookup2() {
		return butLookup2;
	}

	public JButton getButLookup3() {
		return butLookup3;
	}

	public JButton getButSave() {
		return butSave;
	}

	public String getOriginalKey() {
		return originalKey;
	}

	public TermStatus getRbStatus() {
		for (int i = 0; i < rbStatus.length; i++) {
			if (rbStatus[i].isSelected()) {
				return TermStatus.getStatusFromOrdinal(i + 1);
			}
		}
		return TermStatus.Unknown;
	}

	public MultiLineTextField getTfRomanization() {
		return tfRomanization;
	}

	public MultiLineTextField getTfSentence() {
		return tfSentence;
	}

	public MultiLineTextField getTfTerm() {
		return tfTerm;
	}

	public MultiLineTextField getTfTranslation() {
		return tfTranslation;
	}

	public void setRbStatus(TermStatus ts) {
		int index = ts.ordinal() - 1;
		int count = 0;
		for (int i = 0; i < rbStatus.length; i++) {
			rbStatus[i].setSelected(i == index);
			if (i == index) {
				count++;
			}
		}
		if (count == 0) {
			rbStatus[0].setSelected(true);
		}
	}

	public void startEdit(Term t, String sentence) {
		Utilities.setComponentOrientation(tfTerm.getTextArea());
		Utilities.setComponentOrientation(tfSentence.getTextArea());
		Utilities.setComponentOrientation(cbSimilar);
		tfSentence.getTextArea().setForeground(Color.BLACK);
		setTitle("Edit Term");
		originalKey = t.getKey();
		tfTerm.getTextArea().setText(t.getTerm());
		tfTranslation.getTextArea().setText(t.getTranslation());
		tfRomanization.getTextArea().setText(t.getRomanization());
		if (t.getSentence().trim().equals("")) {
			if ((!Preferences.getCurrText().equals("<Vocabulary>"))
					&& (!sentence.equals(""))) {
				tfSentence.getTextArea().setText(sentence);
				tfSentence.getTextArea().setForeground(new Color(0, 0, 128));
			} else {
				tfSentence.getTextArea().setText("");
			}
		} else {
			tfSentence.getTextArea().setText(t.getSentence());
		}
		setRbStatus(t.getStatus());
		cbSimilar.removeAllItems();
		ArrayList<Term> list = Application.getTerms().getNearlyEquals(
				t.getTerm(), Constants.MAX_SIMILAR_TERMS);
		boolean ok = false;
		for (Term tt : list) {
			if (!tt.equals(t)) {
				ok = true;
				cbSimilar.addItem(new ComboBoxItem(tt.displayWithoutStatus(),
						Constants.MAX_SIMILAR_TERMS_LENGTH));
			}
		}
		if (!ok) {
			cbSimilar.addItem(new ComboBoxItem("[None]",
					Constants.MAX_SIMILAR_TERMS_LENGTH));
		}
		pack();
		setVisible(true);
		tfTranslation.getTextArea().requestFocusInWindow();
	}

	public void startNew(String term, String sentence) {
		Utilities.setComponentOrientation(tfTerm.getTextArea());
		Utilities.setComponentOrientation(tfSentence.getTextArea());
		Utilities.setComponentOrientation(cbSimilar);
		tfSentence.getTextArea().setForeground(Color.BLACK);
		setTitle("New Term");
		originalKey = term.toLowerCase();
		tfTerm.getTextArea().setText(term);
		tfTranslation.getTextArea().setText("");
		tfRomanization.getTextArea().setText("");
		if (!Preferences.getCurrText().equals("<Vocabulary>")) {
			tfSentence.getTextArea().setText(sentence);
		} else {
			tfSentence.getTextArea().setText("");
		}
		setRbStatus(TermStatus.Unknown);
		cbSimilar.removeAllItems();
		ArrayList<Term> list = Application.getTerms().getNearlyEquals(term,
				Constants.MAX_SIMILAR_TERMS);
		boolean ok = false;
		for (Term tt : list) {
			ok = true;
			cbSimilar.addItem(new ComboBoxItem(tt.displayWithoutStatus(),
					Constants.MAX_SIMILAR_TERMS_LENGTH));
		}
		if (!ok) {
			cbSimilar.addItem(new ComboBoxItem("[None]",
					Constants.MAX_SIMILAR_TERMS_LENGTH));
		}
		pack();
		setVisible(true);
		tfTranslation.getTextArea().requestFocusInWindow();
	}

}
