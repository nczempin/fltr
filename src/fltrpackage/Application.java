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

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Application {

	public static void checkAndInitBaseDirAndLanguage() {
		String currMainDir = Preferences.getCurrMainDir();
		File dir = new File(currMainDir);
		if (dir.exists() && dir.isDirectory()) {
			Application.setBaseDir(dir);
			String currLang = Preferences.getCurrLang();
			File langdir = new File(dir, currLang + Constants.TEXT_DIR_SUFFIX);
			File langFile = new File(dir, currLang
					+ Constants.LANG_SETTINGS_FILE_SUFFIX);
			if (langFile.exists() && langFile.isFile() && langFile.canRead()
					&& langFile.canWrite() && langdir.exists()
					&& langdir.isDirectory()) {
				Application.setLanguage(new Language(langFile));
			} else {
				Application.setText(null);
				Application.setTerms(null);
				Application.setLanguage(null);
				Preferences.putCurrText("[None]");
				Preferences.putCurrLang("[None]");
			}
		} else {
			Application.setText(null);
			Application.setTerms(null);
			Application.setLanguage(null);
			Application.setBaseDir(null);
			Preferences.putCurrText("[None]");
			Preferences.putCurrLang("[None]");
			Preferences.putCurrMainDir("[Select…]");
		}
	}

	private static void createAndShowGUI() {
		Utilities.checkSingleProgramInstance();
		Utilities.checkJavaVersion(1, 6);
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				Constants.SHORT_NAME);
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		System.setProperty("swing.aatext", "true");
		boolean LAFok = false;
		String lookAndFeel = Preferences.getCurrLookAndFeel();
		if (lookAndFeel.equals("nimbus")) {
			try {
				for (LookAndFeelInfo info : UIManager
						.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						LAFok = true;
						Preferences.putCurrDialogFontSizePercent(100);
						break;
					}
				}
			} catch (Exception ex) {
			}
		} else if (lookAndFeel.equals("metal")) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getCrossPlatformLookAndFeelClassName());
				LAFok = true;
				Utilities.initializeFontSize(Preferences
						.getCurrDialogFontSizePercent());
			} catch (Exception ex) {
			}
		}
		if (!LAFok) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
				Utilities.initializeFontSize(Preferences
						.getCurrDialogFontSizePercent());
			} catch (Exception ex) {
			}
		}
		Application.instance = new Application();
		Application.setStartFrame(new StartFrame());
		Application.getStartFrame().setVisible(true);
	}

	public static LanguageDefinitions getLangDefs() {
		return Application.instance.langDefs;
	}

	public static Language getLanguage() {
		return Application.instance.language;
	}

	public static StartFrame getStartFrame() {
		return Application.instance.startFrame;
	}

	public static TermFrame getTermFrame() {
		return Application.instance.termFrame;
	}

	public static Terms getTerms() {
		return Application.instance.terms;
	}

	public static Text getText() {
		return Application.instance.text;
	}

	public static TextFrame getTextFrame() {
		return Application.instance.textFrame;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Application.createAndShowGUI();
			}
		});
	}

	public static void setLanguage(Language language) {
		Application.instance.language = language;
	}

	public static void setTermFrame(TermFrame termFrame) {
		Application.instance.termFrame = termFrame;
	}

	public static void setTerms(Terms terms) {
		Application.instance.terms = terms;
	}

	public static void setText(Text text) {
		Application.instance.text = text;
	}

	public static void setTextFrame(TextFrame textFrame) {
		Application.instance.textFrame = textFrame;
	}

	public static void stop() {
		System.exit(0);
	}

	private StartFrame startFrame;
	private TextFrame textFrame;
	private TermFrame termFrame;
	private Text text;
	private Language language;
	private Terms terms;
	private LanguageDefinitions langDefs;
	private File baseDir;

	private static Application instance = null;

	public static File getBaseDir() {
		return Application.instance.baseDir;
	}

	public static void setBaseDir(File baseDir) {
		Application.instance.baseDir = baseDir;
	}

	public static void setStartFrame(StartFrame startFrame) {
		Application.instance.startFrame = startFrame;
	}

	private Application() {
		langDefs = new LanguageDefinitions();
		textFrame = null;
		termFrame = null;
		text = null;
		language = null;
		terms = null;
		baseDir = null;
		startFrame = null;
	}

}
