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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class StartFrameListener implements ActionListener, WindowListener,
		HierarchyBoundsListener {

	private StartFrame frame;

	public StartFrameListener(StartFrame frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(frame.getButChooseMainDir())) {
			File dir = Utilities.selectDirectory(frame, "Select "
					+ Constants.SHORT_NAME + " data directory…",
					System.getProperty("user.home"));
			if ((dir != null) && dir.isDirectory()) {
				Preferences.putCurrMainDir(dir.getAbsolutePath());
				Preferences.putCurrLang("[None]");
				Preferences.putCurrText("[None]");
				frame.setDataAndPack();
			}
		} else if (o.equals(frame.getButAbout())) {
			Utilities.showAboutDialog();
		} else if (o.equals(frame.getButEditText())) {
			Application.checkAndInitBaseDirAndLanguage();
			Language lang = Application.getLanguage();
			if (lang != null) {
				String currText = Preferences.getCurrText();
				if (!currText.equals("<Vocabulary>")) {
					File textFile = new File(lang.getTextDir(), currText
							+ Constants.TEXT_FILE_EXTENSION);
					if (textFile.isFile()) {
						Utilities.openTextFileInEditor(textFile);
						return;
					}
				}
			}
			Utilities.showErrorMessage("Not possible.");
		} else if (o.equals(frame.getButOpenMainDir())) {
			Application.checkAndInitBaseDirAndLanguage();
			File dir = Application.getBaseDir();
			if (dir != null) {
				Utilities.openDirectoryInFileExplorer(dir);
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
		} else if (o.equals(frame.getButNewText())) {
			Application.checkAndInitBaseDirAndLanguage();
			Language lang = Application.getLanguage();
			if (lang != null) {
				NewTextDialog dlg = new NewTextDialog(Utilities
						.getClipBoardText().trim());
				dlg.showDialog();
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
		} else if (o.equals(frame.getButGenSett())) {
			GeneralSettingsDialog f = new GeneralSettingsDialog();
			f.setVisible(true);
		} else if (o.equals(frame.getButEditLang())) {
			Application.checkAndInitBaseDirAndLanguage();
			Language lang = Application.getLanguage();
			if (lang != null) {
				LangSettingsDialog f = new LangSettingsDialog();
				f.setVisible(true);
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
		} else if (o.equals(frame.getButNewLang())) {
			Application.checkAndInitBaseDirAndLanguage();
			if (Application.getBaseDir() != null) {
				NewLanguageDialog dlg = new NewLanguageDialog();
				dlg.showDialog();
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
		} else if (o.equals(frame.getButRefresh())) {
			frame.setDataAndPack();
		} else if (o.equals(frame.getButStart())) {
			Application.checkAndInitBaseDirAndLanguage();
			Language lang = Application.getLanguage();
			if (lang != null) {
				String currLang = lang.getLangName();
				String currText = Preferences.getCurrText();
				File langdir = lang.getTextDir();
				File termFile = new File(Application.getBaseDir(), currLang
						+ Constants.WORDS_FILE_SUFFIX);
				Terms terms = new Terms();
				Application.setTerms(terms);
				if (!terms.isLoadTermsFromFileOK(termFile)) {
					Utilities.showErrorMessage("Loading Words File\n"
							+ termFile.getAbsolutePath()
							+ "\nfailed.\n\nCreating empty Words File…");
				}
				File textFile = null;
				if (currText.equals("<Vocabulary>")) {
					if (terms.getData().size() > 0) {
						VocabFilterSortSettingsDialog dlg = new VocabFilterSortSettingsDialog();
						int dlgResult = dlg.showDialog();
						if (dlgResult == 1) {
							textFile = Utilities.CreateTempFile("Vocabulary_",
									".tmp", langdir);
							if (!terms.saveTermsToFileForReview(textFile)) {
								Utilities
										.showErrorMessage("No Vocabulary to display.\nChange your Vocabulary Filters and try again.");
								return;
							}
						} else if (dlgResult == 2) {
							textFile = Utilities.CreateTempFile("Vocabulary_",
									".htm", langdir);
							if (!terms.saveTermsToHTMLFileForReview(textFile)) {
								Utilities
										.showErrorMessage("No Vocabulary to display.\nChange your Vocabulary Filters and try again.");
								return;
							} else {
								Utilities.openURLInDefaultBrowser(textFile
										.toURI().toString());
								return;
							}
						} else if (dlgResult == 3) {
							String s = terms.getTermsForExport();
							if (s.trim().equals("")) {
								Utilities
										.showErrorMessage("No Vocabulary to export.\nChange your Vocabulary Filters and try again.");
							} else {
								File f = Utilities.saveFileDialog(frame,
										"Save Export File",
										Utilities.getDownloadsDirectoryPath());
								if (f == null) {
									Utilities
											.showInfoMessage("Action aborted. Nothing exported.");
								} else {
									if (Utilities.writeStringIntoFile(f, s)) {
										Utilities.showInfoMessage("Export to "
												+ f.getAbsolutePath()
												+ " successful.");
									} else {
										Utilities
												.showErrorMessage("Export to "
														+ f.getAbsolutePath()
														+ " NOT successful.\nPlease try again.");
									}
								}
							}
							return;
						} else {
							return;
						}
					} else {
						Utilities.showErrorMessage("Vocabulary file is empty.");
						return;
					}
				} else {
					textFile = new File(langdir, currText
							+ Constants.TEXT_FILE_EXTENSION);
				}
				if (textFile.isFile()) {
					Application.setText(new Text(textFile));
					Application.getText().matchWithTerms();
					TextFrame textFrame = Application.getTextFrame();
					if (textFrame != null) {
						textFrame.dispose();
					}
					textFrame = new TextFrame();
					Application.setTextFrame(textFrame);
					frame.setVisible(false);
					textFrame.setVisible(true);
				} else {
					Utilities.showErrorMessage(textFile.getAbsolutePath()
							+ " not possible (Deleted?).");
				}
			} else {
				Utilities.showErrorMessage("Not possible.");
			}
		} else if (o.equals(frame.getCbLang())) {
			if (frame.getCbLang().getSelectedItem() != null) {
				Preferences.putCurrLang(((ComboBoxItem) frame.getCbLang()
						.getSelectedItem()).getText());
				frame.setDataAndPack();
			}
		} else if (o.equals(frame.getCbText())) {
			if (frame.getCbText().getSelectedItem() != null) {
				Preferences.putCurrText(((ComboBoxItem) frame.getCbText()
						.getSelectedItem()).getText());
				frame.setDataAndPack();
			}
		}
	}

	@Override
	public void ancestorMoved(HierarchyEvent e) {
		Point p = frame.getLocation();
		Preferences.putCurrXPosStartWindow(p.x);
		Preferences.putCurrYPosStartWindow(p.y);
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Application.stop();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
