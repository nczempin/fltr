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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Preferences {

	public static final String FILE_IDENTIFIER = "FLTRPREFS";

	private static boolean getBoolPreference(String key, boolean def) {
		return (Preferences.getIntPreference(key, (def ? 1 : 0)) != 0);
	}

	public static int getCurrDialogFontSizePercent() {
		return Preferences.getIntPreference("currDialogFontSizePercent", 100);
	}

	public static int getCurrHeightTextPanel() {
		return Preferences.getIntPreference("currHeightTextPanel", 400);
	}

	public static String getCurrLang() {
		return Preferences.getPreference("currLang", "[None]");
	}

	public static String getCurrLookAndFeel() {
		return Preferences.getPreference("currLookAndFeel", "system");
	}

	public static String getCurrMainDir() {
		return Preferences.getPreference("currMainDir", "[Select…]");
	}

	public static boolean getCurrPopupMenusNested() {
		return Preferences.getBoolPreference("currPopupMenusNested", false);
	}

	public static String getCurrText() {
		return Preferences.getPreference("currText", "[None]");
	}

	public static int getCurrVocabMaxResults() {
		return Preferences.getIntPreference("currVocabMaxResults", 0);
	}

	public static int getCurrVocabSortOrder() {
		return Preferences.getIntPreference("currVocabSortOrder", 1);
	}

	public static String getCurrVocabStatusFilter() {
		return Preferences.getPreference("currVocabStatusFilter", "1|2|3|4");
	}

	public static String getCurrVocabTextFilter() {
		return Preferences.getPreference("currVocabTextFilter", "[All Terms]");
	}

	public static String getCurrVocabWordFilter() {
		return Preferences.getPreference("currVocabWordFilter", "");
	}

	public static int getCurrWidthTextPanel() {
		return Preferences.getIntPreference("currWidthTextPanel", 600);
	}

	public static int getCurrXPosStartWindow(int dft) {
		return Preferences.getIntPreference("currXPosStartWindow", dft);
	}

	public static int getCurrXPosTermWindow(int dft) {
		return Preferences.getIntPreference("currXPosTermWindow", dft);
	}

	public static int getCurrXPosTextWindow(int dft) {
		return Preferences.getIntPreference("currXPosTextWindow", dft);
	}

	public static int getCurrYPosStartWindow(int dft) {
		return Preferences.getIntPreference("currYPosStartWindow", dft);
	}

	public static int getCurrYPosTermWindow(int dft) {
		return Preferences.getIntPreference("currYPosTermWindow", dft);
	}

	public static int getCurrYPosTextWindow(int dft) {
		return Preferences.getIntPreference("currYPosTextWindow", dft);
	}

	private static int getIntPreference(String key, int def) {
		String s = Preferences.getPreference(key.trim(), Integer.toString(def)
				.trim());
		int result;
		try {
			result = Integer.parseInt(s);
		} catch (Exception e) {
			result = def;
		}
		return result;
	}

	private static String getPreference(String key, String def) {
		if (Preferences.instance == null) {
			Preferences.instance = new Preferences();
		}
		String value;
		if (Preferences.instance.prefs.containsKey(key)) {
			value = Preferences.instance.prefs.get(key).trim();
		} else {
			value = def.trim();
		}
		Preferences.putPreference(key, value);
		return value;
	}

	private static void putBoolPreference(String key, boolean value) {
		Preferences.putIntPreference(key, (value ? 1 : 0));
	}

	public static void putCurrDialogFontSizePercent(int i) {
		Preferences.putIntPreference("currDialogFontSizePercent", i);
	}

	public static void putCurrHeightTextPanel(int i) {
		Preferences.putIntPreference("currHeightTextPanel", i);
	}

	public static void putCurrLang(String s) {
		Preferences.putPreference("currLang", s);
	}

	public static void putCurrLookAndFeel(String s) {
		Preferences.putPreference("currLookAndFeel", s);
	}

	public static void putCurrMainDir(String s) {
		Preferences.putPreference("currMainDir", s);
	}

	public static void putCurrPopupMenusNested(boolean b) {
		Preferences.putBoolPreference("currPopupMenusNested", b);
	}

	public static void putCurrText(String s) {
		Preferences.putPreference("currText", s);
	}

	public static void putCurrVocabMaxResult(int i) {
		Preferences.putIntPreference("currVocabMaxResults", i);
	}

	public static void putCurrVocabSortOrder(int i) {
		Preferences.putIntPreference("currVocabSortOrder", i);
	}

	public static void putCurrVocabStatusFilter(String s) {
		Preferences.putPreference("currVocabStatusFilter", s);
	}

	public static void putCurrVocabTextFilter(String s) {
		Preferences.putPreference("currVocabTextFilter", s);
	}

	public static void putCurrVocabWordFilter(String s) {
		Preferences.putPreference("currVocabWordFilter", s);
	}

	public static void putCurrWidthTextPanel(int i) {
		Preferences.putIntPreference("currWidthTextPanel", i);
	}

	public static void putCurrXPosStartWindow(int i) {
		Preferences.putIntPreference("currXPosStartWindow", i);
	}

	public static void putCurrXPosTermWindow(int i) {
		Preferences.putIntPreference("currXPosTermWindow", i);
	}

	public static void putCurrXPosTextWindow(int i) {
		Preferences.putIntPreference("currXPosTextWindow", i);
	}

	public static void putCurrYPosStartWindow(int i) {
		Preferences.putIntPreference("currYPosStartWindow", i);
	}

	public static void putCurrYPosTermWindow(int i) {
		Preferences.putIntPreference("currYPosTermWindow", i);
	}

	public static void putCurrYPosTextWindow(int i) {
		Preferences.putIntPreference("currYPosTextWindow", i);
	}

	private static void putIntPreference(String key, int value) {
		Preferences.putPreference(key.trim(), Integer.toString(value).trim());
	}

	private static void putPreference(String key, String value) {
		if (Preferences.instance == null) {
			Preferences.instance = new Preferences();
		}
		Preferences.instance.prefs.put(key.trim(), value.trim());
		Preferences.instance.savePreferences();
	}

	private Hashtable<String, String> prefs;
	private String fileName;

	private static Preferences instance = null;

	public static int scaleIntValue(int i) {
		return (int) (((float) i * (float) Preferences
				.getCurrDialogFontSizePercent()) / 100.0f);
	}

	private Preferences() {
		prefs = new Hashtable<String, String>();
		fileName = Constants.PREF_FILE_PATH;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), Constants.ENCODING));
			String line = in.readLine();
			if (line != null) {
				if (line.trim().equalsIgnoreCase(Preferences.FILE_IDENTIFIER)) {
					while ((line = in.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line,
								Constants.TAB);
						int cnt = st.countTokens();
						if (cnt >= 2) {
							String key = st.nextToken().trim();
							String value = st.nextToken().trim();
							prefs.put(key, value);
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
		}
	}

	private void savePreferences() {
		Enumeration<String> pk = prefs.keys();
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), Constants.ENCODING));
			out.print(Preferences.FILE_IDENTIFIER + Constants.EOL);
			while (pk.hasMoreElements()) {
				String key = pk.nextElement().trim();
				String value = prefs.get(key).trim();
				out.print(key + Constants.TAB + value + Constants.EOL);
			}
			out.close();
		} catch (Exception e) {
		}
	}

}
