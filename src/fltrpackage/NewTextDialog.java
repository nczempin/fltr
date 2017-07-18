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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

@SuppressWarnings("serial")
public class NewTextDialog extends JDialog {

	private JTextArea textArea;
	private JScrollPane textAreaScrollPane;
	private JTextField tfTextName;
	private JButton butCancel;
	private JButton butClear;
	private JButton butPaste;
	private JButton butSave;
	private NewTextDialogListener listener;
	private int result;
	private NewTextDialogNameChecker checkListener;

	public NewTextDialog(String text) {
		super();
		setModal(true);
		setTitle("New " + Preferences.getCurrLang() + " Text");
		result = 0;
		listener = new NewTextDialogListener(this);
		checkListener = new NewTextDialogNameChecker(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
		mainPanel.setLayout(mainLayout);
		mainPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

		JPanel subPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		textArea = new JTextArea(text);
		textArea.setRows(20);
		textArea.setColumns(40);
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font(
				"Dialog",
				Font.PLAIN,
				Math.round((15.0f * Preferences.getCurrDialogFontSizePercent()) / 100.0f)));
		textAreaScrollPane = new JScrollPane(textArea);
		textAreaScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		subPanel2.add(textAreaScrollPane);
		mainPanel.add(subPanel2);

		JPanel subPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		subPanel4.add(new JLabel("Text Name:"));
		tfTextName = new JTextField("", 25);
		AbstractDocument doc = (AbstractDocument) tfTextName.getDocument();
		doc.setDocumentFilter(new TextFieldCharLimiter(100));
		tfTextName.getDocument().addDocumentListener(checkListener);
		subPanel4.add(tfTextName);
		mainPanel.add(subPanel4);

		JPanel subPanel5 = new JPanel();
		subPanel5.setLayout(new BoxLayout(subPanel5, BoxLayout.LINE_AXIS));
		butCancel = new JButton("Cancel");
		butCancel.addActionListener(listener);
		subPanel5.add(butCancel);
		subPanel5.add(Box.createHorizontalGlue());
		butClear = new JButton("Clear Text");
		butClear.addActionListener(listener);
		subPanel5.add(butClear);
		butPaste = new JButton("Paste Clipboard");
		butPaste.addActionListener(listener);
		subPanel5.add(butPaste);
		subPanel5.add(Box.createHorizontalGlue());
		butSave = new JButton("Save Text");
		butSave.addActionListener(listener);
		butSave.setEnabled(false);
		subPanel5.add(butSave);
		mainPanel.add(subPanel5);

		getContentPane().add(mainPanel);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(butSave);
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

		tfTextName.requestFocusInWindow();
		Utilities.setComponentOrientation(textArea);

	}

	public JButton getButCancel() {
		return butCancel;
	}

	public JButton getButClear() {
		return butClear;
	}

	public JButton getButPaste() {
		return butPaste;
	}

	public JButton getButSave() {
		return butSave;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JTextField getTfTextName() {
		return tfTextName;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int showDialog() {
		setVisible(true);
		return result;
	}

}
