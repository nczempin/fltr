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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Vector;

public class NewLanguageDialogListener implements ActionListener, FocusListener {

	private NewLanguageDialog frame;

	public NewLanguageDialogListener(NewLanguageDialog frame) {
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
		} else if (o.equals(frame.getButCreate())) {
			String newLang = frame.getTfLangName().getText().trim();
			if (!Utilities.checkFileNameOK(newLang)) {
				Utilities
						.showErrorMessage("Language name contains invalid characters \\, /, :, \", *, ?,\n<, >, |, NEWLINE, TAB or begins with '.'.\nThis has been corrected, please check!");
				frame.getTfLangName().setText(
						Utilities.replaceNonFileNameCharacters(newLang));
				frame.getTfLangName().requestFocusInWindow();
				return;
			}
			newLang = frame.getTfLangName().getText().trim();
			int l1 = frame.getCbLang1().getSelectedIndex() - 2;
			int l2 = frame.getCbLang2().getSelectedIndex() - 2;
			String currMainDir = Preferences.getCurrMainDir();
			File fcurrMainDir = new File(currMainDir);
			String newLangDir = newLang + Constants.TEXT_DIR_SUFFIX;
			Vector<String> langs = Utilities.getSubDirectories(fcurrMainDir);
			boolean notOK = false;
			if (!langs.contains(newLangDir)) {
				File fnewLangDir = new File(fcurrMainDir, newLangDir);
				if (fnewLangDir.exists()) {
					notOK = true;
				} else {
					File fTermFile = new File(fcurrMainDir, newLang
							+ Constants.WORDS_FILE_SUFFIX);
					File fLangSettingsFile = new File(fcurrMainDir, newLang
							+ Constants.LANG_SETTINGS_FILE_SUFFIX);
					if (fTermFile.exists() || fLangSettingsFile.exists()) {
						notOK = true;
					} else {
						boolean ok1 = fnewLangDir.mkdir();
						Language lang = new Language(fLangSettingsFile);
						String l1code = "";
						String l1code2 = "";
						if (l1 >= 0) {
							l1code = Application.getLangDefs().getArray()
									.get(l1).getIsoCode().trim();
							l1code2 = l1code;
							if (l1code2.length() > 2) {
								l1code2 = l1code2.substring(0, 2);
							}
						}
						if (l2 >= 0) {
							LanguageDefinition ld2 = Application.getLangDefs()
									.getArray().get(l2);
							if (ld2.isBiggerFont()) {
								lang.putPref(
										Language.KEYfontSize,
										String.valueOf(((lang.getFontSize() * 3) / 2)));
							}
							if (!ld2.getWordCharRegExp().trim().equals("")) {
								lang.putPref(Language.KEYwordCharRegExp, ld2
										.getWordCharRegExp().trim());
							}
							lang.putPref(Language.KEYmakeCharacterWord,
									(ld2.isMakeCharacterWord() ? "1" : "0"));
							lang.putPref(Language.KEYremoveSpaces,
									(ld2.isRemoveSpaces() ? "1" : "0"));
							lang.putPref(Language.KEYrightToLeft,
									(ld2.isRightToLeft() ? "1" : "0"));
							String l2code = ld2.getIsoCode().trim();
							if (l2code.length() > 2) {
								l2code = l2code.substring(0, 2);
							}
							if ((!l1code.equals("")) && (!l2code.equals(""))) {
								lang.putPref(Language.KEYdictionaryURL1,
										"http://translate.google.com/?ie=UTF-8&sl="
												+ l2code + "&tl=" + l1code
												+ "&text=###");
								lang.putPref(Language.KEYdictionaryURL2,
										"http://glosbe.com/" + l2code + "/"
												+ l1code2 + "/###");
								if (ld2.isTtsAvailable()) {
									lang.putPref(Language.KEYdictionaryURL3,
											"http://translate.google.com/translate_tts?ie=UTF-8&tl="
													+ l2code + "&q=###");
								}
							}
							lang.saveFile();
						} else {
							if (!l1code.equals("")) {
								lang.putPref(Language.KEYdictionaryURL1,
										"http://translate.google.com/?ie=UTF-8&sl=auto&tl="
												+ l1code + "&text=###");
								lang.saveFile();
							}
						}
						Application.setLanguage(lang);
						boolean ok2 = fLangSettingsFile.exists();
						boolean ok3 = Utilities.createEmptyFile(fTermFile);
						if (ok1 && ok2 && ok3) {
							Utilities
									.showInfoMessage("Language '"
											+ newLang
											+ "' successfully created.\n\n"
											+ "Please put your texts into the directory\n"
											+ fnewLangDir.getAbsolutePath()
											+ ".\n\n"
											+ "The vocabulary will be saved in\n"
											+ fTermFile.getAbsolutePath() + ".");
							Preferences.putCurrLang(newLang);
							Application.getStartFrame().setDataAndPack();
							frame.setResult(1);
							frame.setVisible(false);
							frame.dispose();
						} else {
							Utilities.showErrorMessage("Language '" + newLang
									+ "' NOT successfully created."
									+ "\n\nPlease check "
									+ Constants.SHORT_NAME + " directory.");
						}
					}
				}
			} else {
				notOK = true;
			}
			if (notOK) {
				Utilities
						.showErrorMessage("Creation of language '"
								+ newLang
								+ "' NOT possible,\n"
								+ "directory or file(s) already exist.\n\nPlease check "
								+ Constants.SHORT_NAME + " directory.");
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		int l2 = frame.getCbLang2().getSelectedIndex();
		if (l2 > 1) {
			String l2Text = ((ComboBoxItem) frame.getCbLang2()
					.getSelectedItem()).getText();
			String text = frame.getTfLangName().getText().trim();
			if (text.isEmpty()) {
				frame.getTfLangName().setText(l2Text);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

}
