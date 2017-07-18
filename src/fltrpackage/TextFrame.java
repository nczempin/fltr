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

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class TextFrame extends JFrame {

	private TextFrameListener listener;
	private TextPanel tp;
	private JLabel labinfo;
	private TextPanelPopupMenu popupMenu;

	public TextFrame() {
		super();
		listener = new TextFrameListener(this);
		setTitle(Constants.SHORT_NAME);
		addWindowListener(listener);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS);
		mainPanel.setLayout(mainLayout);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JPanel subPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		tp = new TextPanel(new Dimension(Preferences.getCurrWidthTextPanel(),
				Preferences.getCurrHeightTextPanel()), new Font(Application
				.getLanguage().getFontName(), Font.PLAIN, Application
				.getLanguage().getFontSize()));
		popupMenu = new TextPanelPopupMenu(listener);
		getTextPanelScrollPane().getViewport().addMouseListener(listener);
		getTextPanelScrollPane().getViewport().addMouseMotionListener(listener);
		subPanel1.add(getTextPanelScrollPane());
		subPanel1.setBorder(new EmptyBorder(0, 0, 0, 0));
		mainPanel.add(subPanel1);

		JPanel subPanel2 = new JPanel(new FlowLayout(Application.getLanguage()
				.getRightToLeft() ? FlowLayout.RIGHT : FlowLayout.LEFT));
		labinfo = new JLabel("<html>&nbsp;<br>&nbsp;</html>");
		Language lang = Application.getLanguage();
		labinfo.setFont(new Font(lang.getStatusFontName(), Font.PLAIN, lang
				.getStatusFontSize()));
		subPanel2.add(labinfo);
		subPanel2.setBorder(new EmptyBorder(-4, 3, 0, 3));
		mainPanel.add(subPanel2);

		getContentPane().add(mainPanel);
		pack();
		setResizable(false);
		getTextPanelScrollPane().requestFocusInWindow();

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(Preferences.getCurrXPosTextWindow((d.width - this
				.getSize().width) / 2), Preferences
				.getCurrYPosTextWindow((d.height - this.getSize().height) / 2));
		getContentPane().addHierarchyBoundsListener(listener);

		if (!Utilities.isMac()) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(
					this.getClass().getResource(Constants.ICONPATH)));
		}
	}

	public JLabel getLabinfo() {
		return labinfo;
	}

	public TextPanelPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public TextPanel getTextPanel() {
		return tp;
	}

	public JScrollPane getTextPanelScrollPane() {
		return tp.getScrollPane();
	}

}
