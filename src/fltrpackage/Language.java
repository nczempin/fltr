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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Language {

	public static final String KEYcharSubstitutions = "charSubstitutions";
	public static final String DFTcharSubstitutions = "´='|`='|’='|‘='|′='|‵='";

	public static final String KEYdictionaryURL1 = "dictionaryURL1";
	public static final String DFTdictionaryURL1 = "http://translate.google.com/?ie=UTF-8&sl=auto&tl=en&text=###";

	public static final String KEYdictionaryURL2 = "dictionaryURL2";
	public static final String DFTdictionaryURL2 = "";

	public static final String KEYdictionaryURL3 = "dictionaryURL3";
	public static final String DFTdictionaryURL3 = "";

	public static final String KEYopenAutomaticallyURL1 = "openAutomaticallyURL1";
	public static final String DFTopenAutomaticallyURL1 = "1";

	public static final String KEYopenAutomaticallyURL2 = "openAutomaticallyURL2";
	public static final String DFTopenAutomaticallyURL2 = "0";

	public static final String KEYopenAutomaticallyURL3 = "openAutomaticallyURL3";
	public static final String DFTopenAutomaticallyURL3 = "0";

	public static final String KEYfontName = "fontName";
	public static final String DFTfontName = "Dialog";

	public static final String KEYfontSize = "fontSize";
	public static final String DFTfontSize = "20";

	public static final String KEYstatusFontName = "statusFontName";
	public static final String DFTstatusFontName = "Dialog";

	public static final String KEYstatusFontSize = "statusFontSize";
	public static final String DFTstatusFontSize = "15";

	public static final String KEYwordCharRegExp = "wordCharRegExp";
	public static final String DFTwordCharRegExp = "\\-\\'a-zA-ZÀ-ÖØ-ö\\u00F8-\\u01BF\\u01C4-\\u024F\\u0370-\\u052F\\u1E00-\\u1FFF";

	public static final String KEYwordEncodingURL1 = "wordEncodingURL1";
	public static final String DFTwordEncodingURL1 = Constants.ENCODING;

	public static final String KEYwordEncodingURL2 = "wordEncodingURL2";
	public static final String DFTwordEncodingURL2 = Constants.ENCODING;

	public static final String KEYwordEncodingURL3 = "wordEncodingURL3";
	public static final String DFTwordEncodingURL3 = Constants.ENCODING;

	public static final String KEYmakeCharacterWord = "makeCharacterWord";
	public static final String DFTmakeCharacterWord = "0";

	public static final String KEYremoveSpaces = "removeSpaces";
	public static final String DFTremoveSpaces = "0";

	public static final String KEYrightToLeft = "rightToLeft";
	public static final String DFTrightToLeft = "0";

	public static final String KEYexportTemplate = "exportTemplate";
	public static final String DFTexportTemplate = "$w\\t$t\\t$s\\t$r\\t$a\\t$k";

	public static final String KEYexportStatuses = "exportStatuses";
	public static final String DFTexportStatuses = "1|2|3|4";

	public static final String KEYdoExport = "doExport";
	public static final String DFTdoExport = "1";

	public static final String FILE_IDENTIFIER = "FLTRLANGPREFS";

	private Hashtable<String, String> langPrefs;
	private File langFile;
	private File textDir;

	public Language(File prefFile) {
		super();
		langFile = prefFile;
		textDir = new File(langFile.getParent(), getLangName()
				+ Constants.TEXT_DIR_SUFFIX);
		langPrefs = new Hashtable<String, String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(prefFile), Constants.ENCODING));
			String line = in.readLine();
			if (line != null) {
				if (line.trim().equalsIgnoreCase(Language.FILE_IDENTIFIER)) {
					while ((line = in.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line,
								Constants.TAB);
						int cnt = st.countTokens();
						if (cnt >= 2) {
							String key = st.nextToken().trim();
							String value = st.nextToken().trim();
							langPrefs.put(key, value);
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
		}
		saveFile();
	}

	private boolean getBoolPref(String key, String def) {
		return (getIntPref(key, def) != 0);
	}

	public String getCharSubstitutions() {
		return getPref(Language.KEYcharSubstitutions,
				Language.DFTcharSubstitutions);
	}

	public String getDictionaryURL1() {
		return getPref(Language.KEYdictionaryURL1, Language.DFTdictionaryURL1);
	}

	public String getDictionaryURL2() {
		return getPref(Language.KEYdictionaryURL2, Language.DFTdictionaryURL2);
	}

	public String getDictionaryURL3() {
		return getPref(Language.KEYdictionaryURL3, Language.DFTdictionaryURL3);
	}

	public boolean getDoExport() {
		return getBoolPref(Language.KEYdoExport, Language.DFTdoExport);
	}

	public String getExportStatuses() {
		return getPref(Language.KEYexportStatuses, Language.DFTexportStatuses);
	}

	public String getExportTemplate() {
		return getPref(Language.KEYexportTemplate, Language.DFTexportTemplate);
	}

	public String getFontName() {
		return getPref(Language.KEYfontName, Language.DFTfontName);
	}

	public int getFontSize() {
		return getIntPref(Language.KEYfontSize, Language.DFTfontSize);
	}

	private int getIntPref(String key, String def) {
		int result;
		try {
			result = Integer.parseInt(getPref(key, def));
		} catch (Exception e) {
			result = Integer.parseInt(def);
		}
		return result;
	}

	public File getLangFile() {
		return langFile;
	}

	public String getLangName() {
		return langFile.getName().substring(
				0,
				langFile.getName().length()
						- Constants.LANG_SETTINGS_FILE_SUFFIX_LENGTH);
	}

	public boolean getMakeCharacterWord() {
		return getBoolPref(Language.KEYmakeCharacterWord,
				Language.DFTmakeCharacterWord);
	}

	public boolean getOpenAutomaticallyURL1() {
		return getBoolPref(Language.KEYopenAutomaticallyURL1,
				Language.DFTopenAutomaticallyURL1);
	}

	public boolean getOpenAutomaticallyURL2() {
		return getBoolPref(Language.KEYopenAutomaticallyURL2,
				Language.DFTopenAutomaticallyURL2);
	}

	public boolean getOpenAutomaticallyURL3() {
		return getBoolPref(Language.KEYopenAutomaticallyURL3,
				Language.DFTopenAutomaticallyURL3);
	}

	private String getPref(String key, String def) {
		String result = "";
		if (langPrefs.containsKey(key)) {
			result = langPrefs.get(key).trim();
		}
		if (result.isEmpty()) {
			result = def;
		}
		return result;
	}

	public boolean getRemoveSpaces() {
		return getBoolPref(Language.KEYremoveSpaces, Language.DFTremoveSpaces);
	}

	public boolean getRightToLeft() {
		return getBoolPref(Language.KEYrightToLeft, Language.DFTrightToLeft);
	}

	public String getStatusFontName() {
		return getPref(Language.KEYstatusFontName, Language.DFTstatusFontName);
	}

	public int getStatusFontSize() {
		return getIntPref(Language.KEYstatusFontSize,
				Language.DFTstatusFontSize);
	}

	public File getTextDir() {
		return textDir;
	}

	public String getURLHost(int linkNo) {
		if (!isURLset(linkNo)) {
			return "";
		}
		String u = "";
		if (linkNo == 1) {
			u = getDictionaryURL1();
		} else if (linkNo == 2) {
			u = getDictionaryURL2();
		} else if (linkNo == 3) {
			u = getDictionaryURL3();
		}
		try {
			URL url = new URL(u);
			return url.getHost();
		} catch (Exception ex) {
			return "";
		}
	}

	public String getWordCharRegExp() {
		return getPref(Language.KEYwordCharRegExp, Language.DFTwordCharRegExp);
	}

	public String getWordEncodingURL1() {
		return getPref(Language.KEYwordEncodingURL1,
				Language.DFTwordEncodingURL1);
	}

	public String getWordEncodingURL2() {
		return getPref(Language.KEYwordEncodingURL2,
				Language.DFTwordEncodingURL2);
	}

	public String getWordEncodingURL3() {
		return getPref(Language.KEYwordEncodingURL3,
				Language.DFTwordEncodingURL3);
	}

	public boolean isURLset(int linkNo) {
		String URL = "";
		if (linkNo == 1) {
			URL = getDictionaryURL1();
		} else if (linkNo == 2) {
			URL = getDictionaryURL2();
		} else if (linkNo == 3) {
			URL = getDictionaryURL3();
		}
		return URL.startsWith(Constants.URL_BEGIN);
	}

	public void lookupWordInBrowser(String word, int linkNo, boolean always) {
		if (!isURLset(linkNo)) {
			return;
		}
		String URL = "";
		String encoding = "";
		boolean autoOpen = false;
		if (linkNo == 1) {
			URL = getDictionaryURL1();
			encoding = getWordEncodingURL1();
			autoOpen = getOpenAutomaticallyURL1();
		} else if (linkNo == 2) {
			URL = getDictionaryURL2();
			encoding = getWordEncodingURL2();
			autoOpen = getOpenAutomaticallyURL2();
		} else if (linkNo == 3) {
			URL = getDictionaryURL3();
			encoding = getWordEncodingURL3();
			autoOpen = getOpenAutomaticallyURL3();
		}
		try {
			if (always || autoOpen) {
				Utilities.openURLInDefaultBrowser(URL.replace(
						Constants.TERM_PLACEHOLDER,
						URLEncoder.encode(word.trim(), encoding)));
			}
		} catch (Exception ex) {
		}
	}

	public void putPref(String key, String value) {
		langPrefs.put(key, value.trim());
	}

	public void saveFile() {
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(langFile), Constants.ENCODING));
			out.print(Language.FILE_IDENTIFIER + Constants.EOL);
			out.print(Language.KEYcharSubstitutions + Constants.TAB
					+ getCharSubstitutions() + Constants.EOL);
			out.print(Language.KEYwordCharRegExp + Constants.TAB
					+ getWordCharRegExp() + Constants.EOL);
			out.print(Language.KEYmakeCharacterWord + Constants.TAB
					+ (getMakeCharacterWord() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYremoveSpaces + Constants.TAB
					+ (getRemoveSpaces() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYrightToLeft + Constants.TAB
					+ (getRightToLeft() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYfontName + Constants.TAB + getFontName()
					+ Constants.EOL);
			out.print(Language.KEYfontSize + Constants.TAB
					+ String.valueOf(getFontSize()) + Constants.EOL);
			out.print(Language.KEYstatusFontName + Constants.TAB
					+ getStatusFontName() + Constants.EOL);
			out.print(Language.KEYstatusFontSize + Constants.TAB
					+ String.valueOf(getStatusFontSize()) + Constants.EOL);
			out.print(Language.KEYdictionaryURL1 + Constants.TAB
					+ getDictionaryURL1() + Constants.EOL);
			out.print(Language.KEYwordEncodingURL1 + Constants.TAB
					+ getWordEncodingURL1() + Constants.EOL);
			out.print(Language.KEYopenAutomaticallyURL1 + Constants.TAB
					+ (getOpenAutomaticallyURL1() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYdictionaryURL2 + Constants.TAB
					+ getDictionaryURL2() + Constants.EOL);
			out.print(Language.KEYwordEncodingURL2 + Constants.TAB
					+ getWordEncodingURL2() + Constants.EOL);
			out.print(Language.KEYopenAutomaticallyURL2 + Constants.TAB
					+ (getOpenAutomaticallyURL2() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYdictionaryURL3 + Constants.TAB
					+ getDictionaryURL3() + Constants.EOL);
			out.print(Language.KEYwordEncodingURL3 + Constants.TAB
					+ getWordEncodingURL3() + Constants.EOL);
			out.print(Language.KEYopenAutomaticallyURL3 + Constants.TAB
					+ (getOpenAutomaticallyURL3() ? "1" : "0") + Constants.EOL);
			out.print(Language.KEYexportTemplate + Constants.TAB
					+ getExportTemplate() + Constants.EOL);
			out.print(Language.KEYexportStatuses + Constants.TAB
					+ getExportStatuses() + Constants.EOL);
			out.print(Language.KEYdoExport + Constants.TAB
					+ (getDoExport() ? "1" : "0") + Constants.EOL);
			out.close();
		} catch (Exception e) {
		}
	}

}
