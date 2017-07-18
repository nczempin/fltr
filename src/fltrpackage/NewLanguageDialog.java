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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.text.AbstractDocument;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class NewLanguageDialog extends JDialog {

	private JComboBox cbLang2;
	private JComboBox cbLang1;
	private JTextField tfLangName;
	private JButton butCancel;
	private JButton butCreate;
	private NewLanguageDialogListener listener;
	private int result;
	private NewLanguageDialogInputChecker checkListener;

	public NewLanguageDialog() {
		super();
		setModal(true);
		setTitle("New Language");
		result = 0;
		listener = new NewLanguageDialogListener(this);
		checkListener = new NewLanguageDialogInputChecker(this);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		MigLayout mainPanelLayout = new MigLayout("fill", // Layout Constraints
				"[right]rel[]", // Column constraints
				"[]3[]"); // Row constraints
		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(new JLabel("I want to study:"), "right");
		cbLang2 = new JComboBox(new Vector<ComboBoxItem>());
		cbLang2.addItem(new ComboBoxItem("[Select…]", 1000));
		cbLang2.addItem(new ComboBoxItem("[Other Language]", 1000));
		Vector<String> texts = Application.getLangDefs().getTextList();
		for (String text : texts) {
			cbLang2.addItem(new ComboBoxItem(text, 1000));
		}
		cbLang2.setEditable(false);
		cbLang2.setMaximumRowCount(20);
		cbLang2.addActionListener(checkListener);
		mainPanel.add(cbLang2, "grow, wrap");

		mainPanel.add(new JLabel("My Native Language:"), "right");
		cbLang1 = new JComboBox(new Vector<ComboBoxItem>());
		cbLang1.addItem(new ComboBoxItem("[Select…]", 1000));
		cbLang1.addItem(new ComboBoxItem("[Other Language]", 1000));
		for (String text : texts) {
			cbLang1.addItem(new ComboBoxItem(text, 1000));
		}
		cbLang1.setEditable(false);
		cbLang1.setMaximumRowCount(20);
		cbLang1.addActionListener(checkListener);
		mainPanel.add(cbLang1, "grow, wrap");

		mainPanel.add(new JLabel("Name:"), "right");
		tfLangName = new JTextField("", 0);
		AbstractDocument doc = (AbstractDocument) tfLangName.getDocument();
		doc.setDocumentFilter(new TextFieldCharLimiter(30));
		tfLangName.getDocument().addDocumentListener(checkListener);
		tfLangName.addFocusListener(listener);
		mainPanel.add(tfLangName, "grow, wrap");

		butCancel = new JButton("Cancel");
		butCancel.addActionListener(listener);
		mainPanel.add(butCancel, "grow");
		butCreate = new JButton("Create");
		butCreate.addActionListener(listener);
		butCreate.setEnabled(false);
		mainPanel.add(butCreate, "grow");

		getContentPane().add(mainPanel);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(butCreate);
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

	public JButton getButCancel() {
		return butCancel;
	}

	public JButton getButCreate() {
		return butCreate;
	}

	public JComboBox getCbLang1() {
		return cbLang1;
	}

	public JComboBox getCbLang2() {
		return cbLang2;
	}

	public JTextField getTfLangName() {
		return tfLangName;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int showDialog() {
		setVisible(true);
		return result;
	}

}
