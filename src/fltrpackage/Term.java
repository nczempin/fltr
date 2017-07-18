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

public class Term {

	private String key;
	private String term;
	private String translation;
	private String romanization;
	private String sentence;
	private TermStatus status;
	private Text text;
	private int wordCount;
	private boolean multiWord;
	private ArrayList<char[]> bigram;

	public Term(String term, String translation, String sentence,
			String romanization, int statusCode) {
		super();
		this.term = Utilities.replaceControlCharactersWithSpace(term)
				.replaceAll("\\s{2,}", " ");
		key = this.term.toLowerCase();
		this.translation = Utilities
				.replaceControlCharactersWithSpace(translation);
		this.romanization = Utilities
				.replaceControlCharactersWithSpace(romanization);
		this.sentence = Utilities.replaceControlCharactersWithSpace(sentence);
		status = TermStatus.getStatusFromCode(statusCode);
		text = new Text(this.term);
		wordCount = text.getTextItems().size();
		multiWord = (wordCount > 1);
		bigram = FuzzySearch.calcBigram(key);
	}

	public String displayWithoutStatus() {
		boolean rtl = Application.getLanguage().getRightToLeft();
		String s = "";
		String tr = translation;
		if (!romanization.equals("")) {
			s = (rtl ? "" : " ") + "[" + romanization + "]" + (rtl ? " " : "");
		}
		if (tr.equals("")) {
			tr = "(No Translation)";
		}
		if (rtl) {
			return tr + " — " + s + term;
		} else {
			return term + s + " — " + tr;
		}
	}

	public String displayWithStatusHTML() {
		String s = "";
		String tr = translation;
		if (!romanization.equals("")) {
			s = romanization + " — ";
		}
		if (tr.equals("")) {
			tr = "(No Translation)";
		}
		return Utilities.escapeHTML(term)
				+ "<br>"
				+ Utilities.escapeHTML(s + tr + " — "
						+ status.getStatusShortText());
	}

	public ArrayList<char[]> getBigram() {
		return bigram;
	}

	public String getKey() {
		return key;
	}

	public String getRomanization() {
		return romanization;
	}

	public String getSentence() {
		return sentence;
	}

	public TermStatus getStatus() {
		return status;
	}

	public String getTerm() {
		return term;
	}

	public Text getText() {
		return text;
	}

	public String getTranslation() {
		return translation;
	}

	public int getWordCount() {
		return wordCount;
	}

	public boolean isMultiWord() {
		return multiWord;
	}

	public String makeExportTemplateLine(String statusList,
			String exportTemplate) {
		String s = "";
		Term t = this;
		String status = "|" + String.valueOf(t.getStatus().getStatusCode())
				+ "|";
		if (statusList.indexOf(status) >= 0) {
			s = exportTemplate;
			s = s.replace("%w", t.getTerm());
			s = s.replace("%t", t.getTranslation());
			s = s.replace("%r", t.getRomanization());
			s = s.replace("%s", t.getSentence());
			s = s.replace("%c", t.getSentence()
					.replaceAll("\\{.+?\\}", "{***}"));
			s = s.replace(
					"%d",
					t.getSentence().replaceAll("\\{.+?\\}",
							"{***" + t.getTranslation() + "***}"));
			s = s.replace("%a", String.valueOf(t.getStatus().getStatusCode()));
			s = s.replace("%k", t.getKey());
			s = s.replace("$w", Utilities.escapeHTML(t.getTerm()));
			s = s.replace("$t", Utilities.escapeHTML(t.getTranslation()));
			s = s.replace("$r", Utilities.escapeHTML(t.getRomanization()));
			s = s.replace("$s", Utilities.escapeHTML(t.getSentence()));
			s = s.replace(
					"$c",
					Utilities.escapeHTML(t.getSentence().replaceAll(
							"\\{.+?\\}", "{***}")));
			s = s.replace(
					"$d",
					Utilities.escapeHTML(t.getSentence().replaceAll(
							"\\{.+?\\}", "{***" + t.getTranslation() + "***}")));
			s = s.replace("$a", Utilities.escapeHTML(String.valueOf(t
					.getStatus().getStatusCode())));
			s = s.replace("$k", Utilities.escapeHTML(t.getKey()));
			s = s.replace("\\t", "\t");
			s = s.replace("\\n", "\r\n");
			s = s.replace("$$", "$");
			s = s.replace("%%", "%");
			s = s.replace("\\\\", "\\");
		}
		return s;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setRomanization(String romanization) {
		this.romanization = romanization;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public void setStatus(TermStatus status) {
		this.status = status;
	}

	public void setStatusFromCode(int statusCode) {
		status = TermStatus.getStatusFromCode(statusCode);
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	@Override
	public String toString() {
		return term + Constants.TAB + translation + Constants.TAB + sentence
				+ Constants.TAB + romanization + Constants.TAB
				+ String.valueOf(status.getStatusCode()) + Constants.TAB + key;
	}

}
