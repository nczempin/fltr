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

public class Constants {

	public static final String SHORT_VERSION = "0.8.6";
	public static final String VERSION = Constants.SHORT_VERSION
			+ " (28-Aug-2012)";

	public static final String SHORT_NAME = "FLTR";
	public static final String LONG_NAME = "Foreign Language Text Reader";
	public static final String WEBSITE = "http://fltr.googlecode.com";
	public static final String COPYRIGHT = "Copyright © 2012 "
			+ Constants.SHORT_NAME + " Developers.";

	public static final String ICONPATH = "/fltrpackage/icon128.png";
	public static final String HEADER_HTML_PATH = "/fltrpackage/_Header.htm";
	public static final String VOCAB_HTML_PATH = "/fltrpackage/_Vocabulary.htm";
	public static final String TEXT_HTML_PATH = "/fltrpackage/_Text.htm";
	public static final String LOCK_FILE_PATH = System.getProperty("user.home")
			+ System.getProperty("file.separator") + ".fltrlock";
	public static final String PREF_FILE_PATH = System.getProperty("user.home")
			+ System.getProperty("file.separator") + ".fltrprefs";

	public static final String TEXT_DIR_SUFFIX = "_Texts";
	public static final int TEXT_DIR_SUFFIX_LENGTH = Constants.TEXT_DIR_SUFFIX
			.length();

	public static final String LANG_SETTINGS_FILE_SUFFIX = "_Settings.csv";
	public static final int LANG_SETTINGS_FILE_SUFFIX_LENGTH = Constants.LANG_SETTINGS_FILE_SUFFIX
			.length();

	public static final String WORDS_FILE_SUFFIX = "_Words.csv";
	public static final String EXPORT_WORDS_FILE_SUFFIX = "_Export.txt";

	public static final String TEXT_FILE_EXTENSION = ".txt";
	public static final int TEXT_FILE_EXTENSION_LENGTH = Constants.TEXT_FILE_EXTENSION
			.length();

	public static final int MAX_DATA_LENGTH_START_FRAME = 35;
	public static final int MAX_TEXT_LENGTH_START_FRAME = 30;
	public static final int MAX_LANG_LENGTH_START_FRAME = 30;

	public static final int MAX_SIMILAR_TERMS = 10;
	public static final int MAX_SIMILAR_TERMS_LENGTH = 45;
	public static final double FUZZY_THRESHOLD = 0.333;

	public static final String PARAGRAPH_MARKER = "¶";

	public static final String ENCODING = "UTF-8";

	public static final String TAB = "\t";
	public static final String EOL = "\r\n";
	public static final String UNIX_EOL = "\n";
	public static final String TERMS_SEPARATOR = " ･ ";
	public static final String URL_BEGIN = "http://";
	public static final String TERM_PLACEHOLDER = "###";

}
