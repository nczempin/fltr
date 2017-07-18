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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class LanguageDefinitions {

	private ArrayList<LanguageDefinition> defArray;
	private HashMap<String, LanguageDefinition> nameHashMap;

	public LanguageDefinitions() {
		super();
		defArray = new ArrayList<LanguageDefinition>(50);
		nameHashMap = new HashMap<String, LanguageDefinition>();
		defArray.add(new LanguageDefinition("Afrikaans", "af", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Arabic", "ar", true, false,
				"\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70-\\uFEFF",
				false, false, true));
		defArray.add(new LanguageDefinition("Belarusian", "be", false, false,
				"", false, false, false));
		defArray.add(new LanguageDefinition("Bulgarian", "bg", false, false,
				"", false, false, false));
		defArray.add(new LanguageDefinition("Chinese (Simplified)", "zh-CN",
				true, true, "\\u4E00-\\u9FFF\\uF900-\\uFAFF", true, true, false));
		defArray.add(new LanguageDefinition("Chinese (Traditional)", "zh-TW",
				true, true, "\\u4E00-\\u9FFF\\uF900-\\uFAFF\\u3100-\\u312F",
				true, true, false));
		defArray.add(new LanguageDefinition("Croatian", "hr", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Czech", "cs", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Danish", "da", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Dutch", "nl", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("English", "en", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Estonian", "et", false, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Finnish", "fi", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("French", "fr", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("German", "de", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Hebrew", "iw", false, true,
				"\\u0590-\\u05FF", false, false, true));
		defArray.add(new LanguageDefinition("Hungarian", "hu", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Italian", "it", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Japanese", "ja", true, true,
				"\\u4E00-\\u9FFF\\uF900-\\uFAFF\\u3040-\\u30FF\\u31F0-\\u31FF",
				true, true, false));
		defArray.add(new LanguageDefinition("Korean", "ko", true, true,
				"\\u4E00-\\u9FFF\\uF900-\\uFAFF\\u1100-\\u11FF"
						+ "\\u3130-\\u318F\\uAC00-\\uD7A0", false, false, false));
		defArray.add(new LanguageDefinition("Latvian", "lv", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Lithuanian", "lt", false, false,
				"", false, false, false));
		defArray.add(new LanguageDefinition("Macedonian", "mk", true, false,
				"", false, false, false));
		defArray.add(new LanguageDefinition("Norwegian", "no", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Polish", "pl", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Portuguese", "pt", true, false,
				"", false, false, false));
		defArray.add(new LanguageDefinition("Romanian", "ro", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Russian", "ru", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Serbian", "sr", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Slovak", "sk", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Spanish", "es", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Swedish", "sv", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Turkish", "tr", true, false, "",
				false, false, false));
		defArray.add(new LanguageDefinition("Ukrainian", "uk", false, false,
				"", false, false, false));
		defArray.trimToSize();
		for (LanguageDefinition ld : defArray) {
			nameHashMap.put(ld.getName(), ld);
		}
	}

	public LanguageDefinition get(String name) {
		return nameHashMap.get(name);
	}

	public ArrayList<LanguageDefinition> getArray() {
		return defArray;
	}

	public int getSize() {
		return defArray.size();
	}

	public Vector<String> getTextList() {
		Vector<String> result = new Vector<String>(defArray.size());
		for (LanguageDefinition ld : defArray) {
			result.add(ld.getName());
		}
		result.trimToSize();
		return result;
	}

}
