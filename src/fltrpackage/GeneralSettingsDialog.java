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

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class GeneralSettingsDialog extends JDialog {

	private JTable settingsTable;
	private GeneralSettingsTableModel settingsTableModel;
	private JScrollPane settingsTableScrollPane;
	private GeneralSettingsDialogListener listener;

	public GeneralSettingsDialog() {
		super();
		setModal(true);
		setTitle("General Settings");
		listener = new GeneralSettingsDialogListener(this);
		addWindowListener(listener);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
		mainPanel.setLayout(mainLayout);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		settingsTableModel = new GeneralSettingsTableModel();
		settingsTable = new JTable(settingsTableModel);
		settingsTable
				.setPreferredScrollableViewportSize(new Dimension(Preferences
						.scaleIntValue(300), Preferences.scaleIntValue(100)));
		settingsTable.setFillsViewportHeight(true);
		settingsTable.getTableHeader().setReorderingAllowed(false);
		settingsTable.getColumnModel().getColumn(1)
				.setPreferredWidth(Preferences.scaleIntValue(120));
		settingsTable.setRowHeight(Preferences.scaleIntValue(settingsTable
				.getRowHeight()));
		settingsTableScrollPane = new JScrollPane(settingsTable);
		settingsTableScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		settingsTableScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(settingsTableScrollPane);

		getContentPane().add(mainPanel);

		getRootPane().registerKeyboardAction(listener,
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

	public JTable getGeneralSettingsTable() {
		return settingsTable;
	}

	public GeneralSettingsTableModel getGeneralSettingsTableModel() {
		return settingsTableModel;
	}

}