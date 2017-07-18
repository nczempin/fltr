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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class NewTextDialogListener implements ActionListener {

	private NewTextDialog frame;

	public NewTextDialogListener(NewTextDialog frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(frame.getRootPane())) {
			frame.setResult(0);
			frame.setVisible(false);
			frame.dispose();
		} else if (o.equals(frame.getButCancel())) {
			frame.setResult(0);
			frame.setVisible(false);
			frame.dispose();
		} else if (o.equals(frame.getButClear())) {
			frame.getTextArea().setText("");
		} else if (o.equals(frame.getButPaste())) {
			frame.getTextArea().setText(Utilities.getClipBoardText().trim());
		} else if (o.equals(frame.getButSave())) {
			String textName = frame.getTfTextName().getText().trim();
			frame.getTfTextName().setText(textName);
			if (textName.isEmpty()) {
				Utilities.showErrorMessage("Text name not specified.");
				frame.getTfTextName().requestFocusInWindow();
				return;
			}
			if (!Utilities.checkFileNameOK(textName)) {
				Utilities
						.showErrorMessage("Text name contains invalid characters \\, /, :, \", *, ?,\n<, >, |, NEWLINE, TAB or begins with '.'.\nThis has been corrected, please check!");
				frame.getTfTextName().setText(
						Utilities.replaceNonFileNameCharacters(textName));
				frame.getTfTextName().requestFocusInWindow();
				return;
			}
			textName = frame.getTfTextName().getText().trim();
			String text = frame.getTextArea().getText();
			String currMainDir = Preferences.getCurrMainDir();
			String currLang = Preferences.getCurrLang();
			File dir = new File(currMainDir);
			if (dir.isDirectory()) {
				File langdir = new File(dir, currLang
						+ Constants.TEXT_DIR_SUFFIX);
				if (langdir.isDirectory()) {
					File langFile = new File(dir, currLang
							+ Constants.LANG_SETTINGS_FILE_SUFFIX);
					Language lang = new Language(langFile);
					Application.setLanguage(lang);
					File f = new File(lang.getTextDir(), textName + ".txt");
					if (!Utilities.createNewFile(f)) {
						Utilities.showErrorMessage("Text Name exists.");
						frame.getTfTextName().requestFocusInWindow();
						return;
					}
					if (text.indexOf(Constants.EOL) == -1) {
						text = text.replace(Constants.UNIX_EOL, Constants.EOL);
					}
					Utilities.writeStringIntoFile(f, text);
					Utilities.showInfoMessage("Text successfully created:\n"
							+ f.getAbsolutePath());
					Preferences.putCurrText(textName);
				}
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
			frame.setResult(1);
			frame.setVisible(false);
			frame.dispose();
			Application.getStartFrame().setDataAndPack();
		}
	}

}
