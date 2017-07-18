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

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class LangSettingsTableModel extends AbstractTableModel {

	private Language lang;

	public LangSettingsTableModel() {
		super();
		lang = Application.getLanguage();
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int arg0) {
		return arg0 == 0 ? "Setting" : "Value (Double Click to Edit)";
	}

	private String getData(int index) {
		String value = "";
		switch (index) {
		case 0:
			value = lang.getCharSubstitutions();
			break;
		case 1:
			value = lang.getWordCharRegExp();
			break;
		case 2:
			value = (lang.getMakeCharacterWord() ? "1" : "0");
			break;
		case 3:
			value = (lang.getRemoveSpaces() ? "1" : "0");
			break;
		case 4:
			value = (lang.getRightToLeft() ? "1" : "0");
			break;
		case 5:
			value = lang.getFontName();
			break;
		case 6:
			value = String.valueOf(lang.getFontSize());
			break;
		case 7:
			value = lang.getStatusFontName();
			break;
		case 8:
			value = String.valueOf(lang.getStatusFontSize());
			break;
		case 9:
			value = lang.getDictionaryURL1();
			break;
		case 10:
			value = lang.getWordEncodingURL1();
			break;
		case 11:
			value = (lang.getOpenAutomaticallyURL1() ? "1" : "0");
			break;
		case 12:
			value = lang.getDictionaryURL2();
			break;
		case 13:
			value = lang.getWordEncodingURL2();
			break;
		case 14:
			value = (lang.getOpenAutomaticallyURL2() ? "1" : "0");
			break;
		case 15:
			value = lang.getDictionaryURL3();
			break;
		case 16:
			value = lang.getWordEncodingURL3();
			break;
		case 17:
			value = (lang.getOpenAutomaticallyURL3() ? "1" : "0");
			break;
		case 18:
			value = lang.getExportTemplate();
			break;
		case 19:
			value = lang.getExportStatuses();
			break;
		case 20:
			value = (lang.getDoExport() ? "1" : "0");
			break;
		}
		return value;
	}

	private int getDataCount() {
		return 21;
	}

	private String getKey(int index) {
		String value = "";
		switch (index) {
		case 0:
			value = Language.KEYcharSubstitutions;
			break;
		case 1:
			value = Language.KEYwordCharRegExp;
			break;
		case 2:
			value = Language.KEYmakeCharacterWord;
			break;
		case 3:
			value = Language.KEYremoveSpaces;
			break;
		case 4:
			value = Language.KEYrightToLeft;
			break;
		case 5:
			value = Language.KEYfontName;
			break;
		case 6:
			value = Language.KEYfontSize;
			break;
		case 7:
			value = Language.KEYstatusFontName;
			break;
		case 8:
			value = Language.KEYstatusFontSize;
			break;
		case 9:
			value = Language.KEYdictionaryURL1;
			break;
		case 10:
			value = Language.KEYwordEncodingURL1;
			break;
		case 11:
			value = Language.KEYopenAutomaticallyURL1;
			break;
		case 12:
			value = Language.KEYdictionaryURL2;
			break;
		case 13:
			value = Language.KEYwordEncodingURL2;
			break;
		case 14:
			value = Language.KEYopenAutomaticallyURL2;
			break;
		case 15:
			value = Language.KEYdictionaryURL3;
			break;
		case 16:
			value = Language.KEYwordEncodingURL3;
			break;
		case 17:
			value = Language.KEYopenAutomaticallyURL3;
			break;
		case 18:
			value = Language.KEYexportTemplate;
			break;
		case 19:
			value = Language.KEYexportStatuses;
			break;
		case 20:
			value = Language.KEYdoExport;
			break;
		}
		return value;
	}

	@Override
	public int getRowCount() {
		return getDataCount();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return getKey(row).substring(0, 1).toUpperCase()
					+ getKey(row).substring(1);
		} else {
			return getData(row);
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 1);
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (col != 1) {
			return;
		}
		String key = getKey(row);
		if (key.isEmpty()) {
			return;
		}
		lang.putPref(key, (String) value);
		lang.saveFile();
	}

}
