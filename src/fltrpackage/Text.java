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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

	private File file;
	private String text;
	private ArrayList<TextItem> textItems;
	private boolean coordSet;
	private boolean rangeMarked;
	private int markIndexStart;
	private int markIndexEnd;

	public Text(File f) {
		super();
		file = f;
		text = Utilities.readFileIntoString(f);
		textItems = new ArrayList<TextItem>(10000);
		splitText();
		coordSet = false;
		textItems.trimToSize();
	}

	public Text(String text) {
		super();
		file = null;
		this.text = text;
		textItems = new ArrayList<TextItem>(100);
		splitText();
		coordSet = false;
		textItems.trimToSize();
	}

	public File getFile() {
		return file;
	}

	public String getInfo() {
		int newcount = getUnlearnedWordCount();
		int total = newcount;
		ArrayList<Term> temp = new ArrayList<Term>(10000);
		for (TextItem ti : textItems) {
			Term t = ti.getLink();
			if (t != null) {
				if (!temp.contains(t)) {
					temp.add(t);
					total++;
				}
			}
		}
		temp.trimToSize();
		HashMap<TermStatus, Integer> stats = new HashMap<TermStatus, Integer>();
		for (Term t : temp) {
			TermStatus ts = t.getStatus();
			if (stats.containsKey(ts)) {
				stats.put(ts, stats.get(ts) + 1);
			} else {
				stats.put(ts, 1);
			}
		}
		String fileName = file.getName();
		String r = "";
		if (fileName.substring(fileName.length() - 4).equals(".tmp")) {
			r = "This Vocabulary Selection:";
		} else {
			r = "Text \""
					+ fileName.substring(0, fileName.length()
							- Constants.TEXT_FILE_EXTENSION_LENGTH) + "\":";
		}
		r += "\nTOTAL (New Words & Saved Terms): " + String.valueOf(total);
		r += "\nNew Words: " + String.valueOf(newcount)
				+ Utilities.calcPercent(newcount, total) + "\n";
		for (TermStatus ts : TermStatus.getAllActive()) {
			r += ts.getStatusText() + ": ";
			if (stats.containsKey(ts)) {
				int count = stats.get(ts);
				r += String.valueOf(count)
						+ Utilities.calcPercent(count, total) + "\n";
			} else {
				r += "0" + Utilities.calcPercent(0, 0) + "\n";
			}
		}
		return r;
	}

	public String getMarkedText(boolean dragging) {
		String s = "";
		if (rangeMarked) {
			int indexStart = Math.min(markIndexStart, markIndexEnd);
			int indexEnd = Math.max(markIndexStart, markIndexEnd);
			if ((indexStart >= 0) && (indexEnd >= 0)) {
				if ((indexStart == indexEnd) && !dragging) {
					TextItem ti = textItems.get(indexStart);
					Term term = ti.getLink();
					if (term == null) {
						s = ti.getTextItemValue();
					} else {
						s = term.getTerm();
					}
				} else {
					for (int i = indexStart; i <= indexEnd; i++) {
						TextItem ti = textItems.get(i);
						s += ti.getTextItemValue();
						if (i < indexEnd) {
							s += ti.getAfterItemValue();
						}
					}
				}
			}
		}
		return s;
	}

	public String getMarkedTextSentence(String term) {
		String s;
		if (rangeMarked) {
			int indexStart = Math.min(markIndexStart, markIndexEnd) - 9;
			int indexEnd = Math.max(markIndexStart, markIndexEnd) + 9;
			indexStart = Math.max(0, indexStart);
			indexEnd = Math.min(textItems.size() - 1, indexEnd);
			s = (indexStart == 0 ? "" : "… ")
					+ getTextRange(indexStart, indexEnd, true)
					+ (indexEnd == (textItems.size() - 1) ? "" : " …");
			s = s.replace(term, "{" + term + "}");
		} else {
			s = "???";
		}
		return s.replace(Constants.PARAGRAPH_MARKER, "");
	}

	public int getMarkIndexEnd() {
		return markIndexEnd;
	}

	public int getMarkIndexStart() {
		return markIndexStart;
	}

	public int getMissingSentenceCount() {
		String fileName = file.getName();
		if (fileName.substring(fileName.length() - 4).equals(".tmp")) {
			return 0;
		}
		ArrayList<Term> temp = new ArrayList<Term>(10000);
		for (TextItem ti : textItems) {
			Term t = ti.getLink();
			if (t != null) {
				if (!temp.contains(t) && t.getSentence().equals("")) {
					temp.add(t);
				}
			}
		}
		int r = temp.size();
		temp = null;
		return r;
	}

	public int getPointedTextItemIndex(Point p) {
		for (int i = 0; i < textItems.size(); i++) {
			if (textItems.get(i).isPointOnTextItem(p)) {
				return i;
			}
		}
		return -1;
	}

	public String getText() {
		return text;
	}

	public ArrayList<TextItem> getTextItems() {
		return textItems;
	}

	public String getTextRange(int from, int to, boolean lastAlso) {
		String r = "";
		for (int i = Math.max(from, 0); (i < textItems.size()) && (i <= to); i++) {
			TextItem ti = textItems.get(i);
			r += ti.getTextItemValue();
			if ((i < to) || lastAlso) {
				r += ti.getAfterItemValue();
			}
		}
		return r;
	}

	public String getTextSentence(TextItem ti) {
		Term t = ti.getLink();
		if (t != null) {
			String s = t.getTerm();
			int index = textItems.indexOf(ti);
			for (int i = index;; i++) {
				if (textItems.get(i).isLastWord()) {
					setMarkIndexEnd(i);
					break;
				}
			}
			for (int i = index - 1; i >= 0; i--) {
				if (textItems.get(i).isLastWord()) {
					setMarkIndexStart(i + 1);
					break;
				}
			}
			setRangeMarked(true);
			return getMarkedTextSentence(s);
		} else {
			String s = ti.getTextItemValue();
			int index = textItems.indexOf(ti);
			setMarkIndexStart(index);
			setMarkIndexEnd(index);
			setRangeMarked(true);
			return getMarkedTextSentence(s);
		}
	}

	public int getUnlearnedWordCount() {
		int res = 0;
		HashMap<String, Integer> temp = new HashMap<String, Integer>();
		for (int i = 0; i < textItems.size(); i++) {
			if (textItems.get(i).getLink() == null) {
				String w = textItems.get(i).getTextItemValue()
						.replace(Constants.PARAGRAPH_MARKER, "");
				if (!w.isEmpty()) {
					if (!temp.containsKey(w)) {
						res++;
						temp.put(w, 1);
					}
				}
			}
		}
		temp = null;
		return res;
	}

	public boolean isCoordSet() {
		return coordSet;
	}

	public boolean isRangeMarked() {
		return rangeMarked;
	}

	public void matchWithTerms() {
		Terms terms = Application.getTerms();
		for (int i = 0; i < textItems.size();) {
			i = terms.match(this, i);
		}
	}

	private void printTerm(PrintWriter out, String term, String rom,
			String trans, boolean justTerm, boolean rtl) {
		if (justTerm) {
			out.print("<table class=\"ann\" border=\"0\"><tr><td"
					+ (rtl ? " dir=\"rtl\"" : "") + "><div class=\"text\">");
			out.print(term.equals("") ? "&nbsp;" : Utilities.escapeHTML(term));
			out.print("</div></td></tr></table>");
		} else {
			out.print("<table class=\"ann\" border=\"0\"><tr><td"
					+ (rtl ? " dir=\"rtl\"" : "") + "><div class=\"term\">");
			out.print(term.equals("") ? "&nbsp;" : Utilities.escapeHTML(term));
			out.print("</div><div class=\"rom\">");
			out.print(rom.equals("") ? "&nbsp;" : Utilities.escapeHTML(rom));
			out.print("</div><div class=\"trans\">");
			out.print(trans.equals("") ? "&nbsp;" : Utilities.escapeHTML(trans));
			out.print("</div></td></tr></table>");
		}
		out.print(Constants.EOL);
	}

	public boolean saveTextToHTMLFileForReview(File f) {
		boolean result = false;
		if (f.canWrite()) {
			String s1 = Utilities
					.readFileFromJarIntoString(Constants.HEADER_HTML_PATH);
			String s2 = Utilities
					.readFileFromJarIntoString(Constants.TEXT_HTML_PATH);
			if ((!s1.equals("")) && (!s2.equals(""))) {
				try {
					PrintWriter out = new PrintWriter(new OutputStreamWriter(
							new FileOutputStream(f), Constants.ENCODING));
					out.print(s1.replace("$$$$TITLE$$$$",
							Utilities.escapeHTML(Preferences.getCurrText())));
					out.print(Constants.EOL);
					out.print(s2.replace("$$$$TITLE$$$$",
							Utilities.escapeHTML(Preferences.getCurrText())));
					out.print(Constants.EOL);
					boolean rtl = Application.getLanguage().getRightToLeft();
					if (rtl) {
						out.print("<div dir=\"rtl\" style=\"text-align:right\">"
								+ Constants.EOL);
					}
					String termBuffer = "";
					for (TextItem item : textItems) {
						if (item.getTextItemValue().equals(
								Constants.PARAGRAPH_MARKER)
								&& item.getAfterItemValue().equals("")) {
							out.print("<br />");
							out.print(Constants.EOL);
						} else {
							if (item.getLink() == null) {
								if (!item.getTextItemValue().trim().equals("")) {
									printTerm(out, item.getTextItemValue(), "",
											"", false, rtl);
								}
								if (!item.getAfterItemValue().trim().equals("")) {
									printTerm(out, item.getAfterItemValue(),
											"", "", true, rtl);
								}
							} else {
								Term t = item.getLink();
								termBuffer += item.getTextItemValue();
								if (item.isLastWord()) {
									printTerm(out, termBuffer,
											t.getRomanization(),
											t.getTranslation(), false, rtl);
									termBuffer = "";
									if (!item.getAfterItemValue().trim()
											.equals("")) {
										printTerm(out,
												item.getAfterItemValue(), "",
												"", true, rtl);
									}
								} else {
									termBuffer += item.getAfterItemValue();
								}
							}
						}
					}
					if (rtl) {
						out.print("</div>" + Constants.EOL);
					}
					out.print("</div></body></html>" + Constants.EOL);
					out.close();
					result = true;
				} catch (Exception e) {
				}
			} else {
				Utilities.showErrorMessage("Sorry - Unable to find HTML file.");
			}
		}
		return result;
	}

	public void setCoordSet(boolean coordSet) {
		this.coordSet = coordSet;
	}

	public void setMarkIndexEnd(int markIndexEnd) {
		this.markIndexEnd = markIndexEnd;
	}

	public void setMarkIndexStart(int markIndexStart) {
		this.markIndexStart = markIndexStart;
	}

	public int setMissingSentences() {
		String fileName = file.getName();
		if (fileName.substring(fileName.length() - 4).equals(".tmp")) {
			return 0;
		}
		int r = 0;
		for (TextItem ti : textItems) {
			Term t = ti.getLink();
			if (t != null) {
				if (t.getSentence().equals("")) {
					String s = getTextSentence(ti);
					if (!s.equals("")) {
						t.setSentence(s);
						r++;
					}
				}
			}
		}
		if (r > 0) {
			Application.getTerms().setDirty(true);
		}
		return r;
	}

	public void setRangeMarked(boolean rangeMarked) {
		this.rangeMarked = rangeMarked;
	}

	private void splitText() {
		text = text.replace(Constants.EOL, " " + Constants.PARAGRAPH_MARKER
				+ " ");
		text = text.replace(Constants.UNIX_EOL, " "
				+ Constants.PARAGRAPH_MARKER + " ");
		text = text.replace(Constants.TAB, " ");
		text = text.trim();
		if (Application.getLanguage().getMakeCharacterWord()) {
			Pattern p = Pattern.compile("([^\\s])");
			Matcher m = p.matcher(text);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				m.appendReplacement(sb, m.group() + " ");
			}
			text = sb.toString();
		}
		text = text.replaceAll("\\s{2,}", " ");
		String[] substitutions = Application.getLanguage()
				.getCharSubstitutions().split("\\|");
		for (String subst : substitutions) {
			if (subst.contains("=")) {
				String[] fromto = (subst + "=x").split("=");
				if (fromto.length == 3) {
					text = text.replace(fromto[0].trim(), fromto[1].trim());
				}
			}
		}
		text = text.trim();
		Language lang = Application.getLanguage();
		Pattern p = Pattern.compile("[^" + Constants.PARAGRAPH_MARKER
				+ lang.getWordCharRegExp() + "]+");
		Matcher m = p.matcher(text);
		int start = 0;
		boolean noSpaces = lang.getRemoveSpaces();
		while (m.find(start)) {
			String s = m.group();
			if (noSpaces) {
				s = s.replaceAll("\\s", "");
			}
			if (start == m.start()) {
				textItems.add(new TextItem("", s));
				start = m.end();
			} else {
				String pref = text.substring(start, m.start());
				if (pref.equals(Constants.PARAGRAPH_MARKER)) {
					textItems.add(new TextItem(pref, ""));
					s = Utilities.leftTrim(s);
					if (!s.equals("")) {
						textItems.add(new TextItem("", s));
					}
				} else {
					textItems.add(new TextItem(
							text.substring(start, m.start()), s));
				}
				start = m.end();
			}
		}
		String s = text.substring(start);
		if (!s.isEmpty()) {
			textItems.add(new TextItem(s, ""));
		}
	}

	@Override
	public String toString() {
		String r = "";
		for (TextItem item : textItems) {
			r += "{" + item.getTextItemValue() + "|" + item.getAfterItemValue()
					+ "} ";
		}
		return r;
	}

}
