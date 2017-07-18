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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Terms {

	private ArrayList<Term> data;
	private HashMap<String, Integer> keyIndex;
	private HashMap<String, TreeMap<Integer, ArrayList<Integer>>> keyIndex2;
	private File file;
	private File exportFile;
	private boolean dirty;

	public Terms() {
		super();
		data = new ArrayList<Term>(10000);
		keyIndex = new HashMap<String, Integer>();
		keyIndex2 = new HashMap<String, TreeMap<Integer, ArrayList<Integer>>>();
		file = null;
		exportFile = null;
		dirty = false;
	}

	public void addTerm(Term t) {
		if (keyIndex.containsKey(t.getKey())) {
			data.set(keyIndex.get(t.getKey()), t);
		} else {
			data.add(t);
			int index = data.lastIndexOf(t);
			keyIndex.put(t.getKey(), index);
			int l = t.getWordCount();
			String firstWord = t.getText().getTextItems().get(0)
					.getTextItemValue().toLowerCase();
			TreeMap<Integer, ArrayList<Integer>> temp;
			if (!keyIndex2.containsKey(firstWord)) {
				temp = new TreeMap<Integer, ArrayList<Integer>>(
						Collections.reverseOrder());
				keyIndex2.put(firstWord, temp);
			}
			temp = keyIndex2.get(firstWord);
			ArrayList<Integer> temp2;
			if (!temp.containsKey(l)) {
				temp2 = new ArrayList<Integer>(5);
				temp.put(l, temp2);
			}
			temp2 = temp.get(l);
			temp2.add(index);
		}
		dirty = true;
	}

	public ArrayList<Term> getData() {
		return data;
	}

	public File getExportFile() {
		return exportFile;
	}

	public File getFile() {
		return file;
	}

	private ArrayList<Term> getFilteredTermList() {
		String statusFilter = "|"
				+ Preferences.getCurrVocabStatusFilter().replaceAll("\\s", "")
				+ "|";
		String textFilter = Preferences.getCurrVocabTextFilter();
		String wordFilter = Utilities.createRegExpFromWildCard(Preferences
				.getCurrVocabWordFilter());
		if (wordFilter.isEmpty()) {
			wordFilter = ".*";
		}
		Pattern wordFilterPattern = Pattern.compile(wordFilter,
				Pattern.UNICODE_CASE + Pattern.CASE_INSENSITIVE);
		ArrayList<Term> temp = new ArrayList<Term>(getData().size());
		if (textFilter.equals("[All Terms]")) {
			for (Term t : data) {
				if (t != null) {
					int status = t.getStatus().getStatusCode();
					String charStatus = "|" + String.valueOf(status) + "|";
					if (statusFilter.indexOf(charStatus) >= 0) {
						Matcher m = wordFilterPattern.matcher(t.getTerm());
						if (m.matches()) {
							temp.add(t);
						}
					}
				}
			}
		} else {
			boolean all = (textFilter.equals("[Terms In All Texts]"));
			File textDir = Application.getLanguage().getTextDir();
			Vector<String> texts = Utilities.getTextFileList(textDir);
			for (String fileName : texts) {
				String textName = fileName.substring(0, fileName.length()
						- Constants.TEXT_FILE_EXTENSION_LENGTH);
				if (all || textName.equals(textFilter)) {
					File textFile = new File(textDir, fileName);
					if (textFile.isFile()) {
						Text tempText = new Text(textFile);
						tempText.matchWithTerms();
						for (TextItem ti : tempText.getTextItems()) {
							Term t = ti.getLink();
							if (t != null) {
								if (!temp.contains(t)) {
									int status = t.getStatus().getStatusCode();
									String charStatus = "|"
											+ String.valueOf(status) + "|";
									if (statusFilter.indexOf(charStatus) >= 0) {
										Matcher m = wordFilterPattern.matcher(t
												.getTerm());
										if (m.matches()) {
											temp.add(t);
										}
									}
								}
							}
						}
						tempText = null;
					}
				}
			}
			texts = null;
		}
		temp.trimToSize();
		if (temp.size() > 0) {
			int sortOrder = Preferences.getCurrVocabSortOrder();
			switch (sortOrder) {
			case 0:
				Collections.sort(temp, new TermComparatorAZ());
				break;
			case 1:
				Collections.sort(temp, new TermComparatorStatusAZ());
				break;
			default:
				Collections.shuffle(temp);
			}
		}
		return temp;
	}

	public String getInfo() {
		HashMap<TermStatus, Integer> stats = new HashMap<TermStatus, Integer>();
		int total = 0;
		for (Term t : data) {
			if (t != null) {
				total++;
				TermStatus ts = t.getStatus();
				if (stats.containsKey(ts)) {
					stats.put(ts, stats.get(ts) + 1);
				} else {
					stats.put(ts, 1);
				}
			}
		}
		String r = "Complete " + Application.getLanguage().getLangName()
				+ " Vocabulary:\nTOTAL Saved Terms: " + String.valueOf(total)
				+ "\n";
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

	private int getMaxFilteredResults() {
		int maxResIndex = Preferences.getCurrVocabMaxResults();
		int[] maxResArray = { Integer.MAX_VALUE, 100, 50, 25, 12, 7 };
		int maxRes = maxResArray[0];
		if ((maxResIndex > 0) && (maxResIndex < maxResArray.length)) {
			maxRes = maxResArray[maxResIndex];
		}
		return maxRes;
	}

	public ArrayList<Term> getNearlyEquals(String string, int max) {
		ArrayList<char[]> stringBigram = FuzzySearch.calcBigram(string
				.toLowerCase());
		ArrayList<Term> temp = new ArrayList<Term>(data.size());
		for (Term t : data) {
			if (t != null) {
				temp.add(t);
			}
		}
		temp.trimToSize();
		Collections.sort(temp, new TermComparatorFuzzyIndex(stringBigram));
		ArrayList<Term> result = new ArrayList<Term>(max);
		for (int i = 0; i < Math.min(max, temp.size()); i++) {
			Term t = temp.get(i);
			if (FuzzySearch.calcEquality(stringBigram,
					FuzzySearch.calcBigram(t.getKey())) > Constants.FUZZY_THRESHOLD) {
				result.add(t);
			} else {
				break;
			}
		}
		result.trimToSize();
		return result;
	}

	public Term getTermFromIndex(int index) {
		if ((index < 0) || (index >= data.size())) {
			return null;
		}
		return data.get(index);
	}

	public Term getTermFromKey(String key) {
		Integer index = keyIndex.get(key);
		if (index == null) {
			return null;
		}
		return getTermFromIndex(index);
	}

	public String getTermsForExport() {
		String s = "";
		ArrayList<Term> temp = getFilteredTermList();
		if (temp.size() > 0) {
			int maxRes = getMaxFilteredResults();
			int count = 0;
			String exportTemplate = Application.getLanguage()
					.getExportTemplate();
			StringBuilder sb = new StringBuilder();
			String statusList = TermStatus.getAllStatuses();
			for (Term t : temp) {
				String ss = t
						.makeExportTemplateLine(statusList, exportTemplate);
				if (!ss.equals("")) {
					sb.append(ss);
					sb.append(Constants.EOL);
					count++;
				}
				if (count >= maxRes) {
					break;
				}
			}
			s = sb.toString();
		}
		return s;
	}

	public boolean isDirty() {
		return dirty;
	}

	public boolean isExportTermsToFileOK() {
		boolean r = false;
		Language lang = Application.getLanguage();
		if ((exportFile != null) && lang.getDoExport()
				&& (!lang.getExportTemplate().isEmpty())
				&& (!lang.getExportStatuses().isEmpty())) {
			String statusList = ("|" + lang.getExportStatuses() + "|")
					.replaceAll("\\s", "");
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(exportFile), Constants.ENCODING));
				for (Term t : data) {
					if (t != null) {
						String s = t.makeExportTemplateLine(statusList,
								lang.getExportTemplate());
						if (!s.equals("")) {
							out.print(s);
							out.print(Constants.EOL);
						}
					}
				}
				out.close();
				r = true;
			} catch (Exception e) {
			}
		}
		return r;
	}

	public boolean isLoadTermsFromFileOK(File file) {
		boolean r;
		this.file = file;
		String parent = file.getParent();
		String lang = Application.getLanguage().getLangName();
		exportFile = new File(parent, lang + Constants.EXPORT_WORDS_FILE_SUFFIX);
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), Constants.ENCODING));
			String line;
			while ((line = in.readLine()) != null) {
				String[] cols = line.split(Constants.TAB);
				int cnt = cols.length;
				if (cnt >= 1) {
					if (cnt >= 5) {
						String status = cols[4].trim();
						int intStatus = 1;
						if (!status.isEmpty()) {
							try {
								intStatus = Integer.parseInt(status);
							} catch (Exception e) {
								intStatus = 1;
							}
						}
						addTerm(new Term(cols[0].trim(), cols[1].trim(),
								cols[2].trim(), cols[3].trim(), intStatus));
					} else if (cnt == 4) {
						addTerm(new Term(cols[0].trim(), cols[1].trim(),
								cols[2].trim(), cols[3].trim(), 1));
					} else if (cnt == 3) {
						addTerm(new Term(cols[0].trim(), cols[1].trim(),
								cols[2].trim(), "", 1));
					} else if (cnt == 2) {
						addTerm(new Term(cols[0].trim(), cols[1].trim(), "",
								"", 1));
					} else { // cnt == 1
						addTerm(new Term(cols[0].trim(), "?", "", "", 1));
					}
				}
			}
			in.close();
			data.trimToSize();
			dirty = false;
			r = true;
		} catch (Exception e) {
			dirty = true;
			r = false;
		}
		return r;
	}

	public boolean isSaveTermsToFileOK() {
		boolean r = false;
		if (file != null) {
			try {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(file), Constants.ENCODING));
				for (Term t : data) {
					if (t != null) {
						out.print(t.toString());
						out.print(Constants.EOL);
					}
				}
				out.close();
				r = true;
				dirty = false;
			} catch (Exception e) {
			}
		}
		return r;
	}

	public int match(Text text, int index) {
		int nextIndex;
		TextItem ti = text.getTextItems().get(index);
		if (keyIndex2.containsKey(ti.getTextItemLowerCaseValue())) {
			TreeMap<Integer, ArrayList<Integer>> temp = keyIndex2.get(ti
					.getTextItemLowerCaseValue());
			for (Entry<Integer, ArrayList<Integer>> entry : temp.entrySet()) {
				ArrayList<Integer> value = entry.getValue();
				int count = entry.getKey().intValue();
				for (Integer index2 : value) {
					Term t = data.get(index2);
					String text2 = text.getTextRange(index,
							(index + count) - 1, false);
					if (t.getKey().equals(text2.toLowerCase())) {
						nextIndex = index + count;
						for (int i = index; i < nextIndex; i++) {
							TextItem ti2 = text.getTextItems().get(i);
							ti2.setLink(t);
							ti2.setLastWord(i == ((index + count) - 1));
						}
						return nextIndex;
					}
				}
			}
			nextIndex = index + 1;
			ti.setLink(null);
			ti.setLastWord(true);
		} else {
			nextIndex = index + 1;
			ti.setLink(null);
			ti.setLastWord(true);
		}
		return nextIndex;
	}

	public void removeTerm(Term t) {
		String key = t.getKey();
		if (keyIndex.containsKey(key)) {
			int index = keyIndex.get(key);
			data.set(index, null);
			keyIndex.remove(key);
			int l = t.getWordCount();
			String firstWord = t.getText().getTextItems().get(0)
					.getTextItemValue().toLowerCase();
			keyIndex2.get(firstWord).get(l).remove(Integer.valueOf(index));
			dirty = true;
		}
	}

	public boolean saveTermsToFileForReview(File f) {
		boolean result = false;
		if (f.canWrite()) {
			ArrayList<Term> temp = getFilteredTermList();
			if (temp.size() > 0) {
				int maxRes = getMaxFilteredResults();
				try {
					PrintWriter out = new PrintWriter(new OutputStreamWriter(
							new FileOutputStream(f), Constants.ENCODING));
					int count = 0;
					for (Term t : temp) {
						out.print(t.getTerm());
						out.print(Constants.TERMS_SEPARATOR);
						count++;
						if (count >= maxRes) {
							break;
						}
					}
					out.print(Constants.EOL);
					out.close();
					result = true;
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public boolean saveTermsToHTMLFileForReview(File f) {
		boolean result = false;
		if (f.canWrite()) {
			ArrayList<Term> temp = getFilteredTermList();
			if (temp.size() > 0) {
				int maxRes = getMaxFilteredResults();
				String s1 = Utilities
						.readFileFromJarIntoString(Constants.HEADER_HTML_PATH);
				String s2 = Utilities
						.readFileFromJarIntoString(Constants.VOCAB_HTML_PATH);
				if ((!s1.equals("")) && (!s2.equals(""))) {
					try {
						PrintWriter out = new PrintWriter(
								new OutputStreamWriter(new FileOutputStream(f),
										Constants.ENCODING));
						out.print(s1.replace(
								"$$$$TITLE$$$$",
								Utilities.escapeHTML(Preferences.getCurrLang()
										+ " Vocabulary")));
						out.print(Constants.EOL);
						out.print(s2.replace(
								"$$$$TITLE$$$$",
								Utilities.escapeHTML(Preferences.getCurrLang()
										+ " Vocabulary")));
						out.print(Constants.EOL);
						int count = 0;
						boolean rtl = Application.getLanguage()
								.getRightToLeft();
						for (Term t : temp) {
							out.print("<tr><td"
									+ (rtl ? " dir=\"rtl\" style=\"text-align:right\""
											: "") + ">");
							out.print(Utilities.escapeHTML(t.getTerm()));
							out.print("</td><td>");
							out.print(Utilities.escapeHTML(t.getRomanization()));
							out.print("</td><td>");
							String tr = Utilities
									.escapeHTML(t.getTranslation());
							out.print(tr.equals("") ? "—" : tr);
							out.print("</td><td"
									+ (rtl ? " dir=\"rtl\" style=\"text-align:right\""
											: "") + ">");
							out.print(Utilities.escapeHTML(t.getSentence()
									.replaceAll("\\{.+?\\}", " ____ ")));
							out.print("</td><td>");
							out.print(t.getStatus().getStatusCode());
							out.print("</td><tr>" + Constants.EOL);
							count++;
							if (count >= maxRes) {
								break;
							}
						}
						out.print("</tbody></table></div></body></html>"
								+ Constants.EOL);
						out.close();
						result = true;
					} catch (Exception e) {
					}
				} else {
					Utilities
							.showErrorMessage("Sorry - Unable to find HTML file.");
				}
			}
		}
		return result;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
