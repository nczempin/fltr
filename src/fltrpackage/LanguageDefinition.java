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

public class LanguageDefinition {

	private String name;
	private String isoCode;
	private boolean ttsAvailable;
	private boolean biggerFont;
	private String wordCharRegExp;
	private boolean makeCharacterWord;
	private boolean removeSpaces;
	private boolean rightToLeft;

	public LanguageDefinition(String name, String isoCode,
			boolean ttsAvailable, boolean biggerFont, String wordCharRegExp,
			boolean makeCharacterWord, boolean removeSpaces, boolean rightToLeft) {
		super();
		this.name = name;
		this.isoCode = isoCode;
		this.ttsAvailable = ttsAvailable;
		this.biggerFont = biggerFont;
		this.wordCharRegExp = wordCharRegExp;
		this.makeCharacterWord = makeCharacterWord;
		this.removeSpaces = removeSpaces;
		this.rightToLeft = rightToLeft;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public String getName() {
		return name;
	}

	public String getWordCharRegExp() {
		return wordCharRegExp;
	}

	public boolean isBiggerFont() {
		return biggerFont;
	}

	public boolean isMakeCharacterWord() {
		return makeCharacterWord;
	}

	public boolean isRemoveSpaces() {
		return removeSpaces;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public boolean isTtsAvailable() {
		return ttsAvailable;
	}

}
