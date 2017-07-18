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
public class GeneralSettingsTableModel extends AbstractTableModel {

	public GeneralSettingsTableModel() {
		super();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int col) {
		return col == 0 ? "Setting" : "Value (Double Click to Edit)";
	}

	@Override
	public int getRowCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (col == 0) {
			switch (row) {
			case 0:
				return "WidthTextPanel";
			case 1:
				return "HeightTextPanel";
			case 2:
				return "PopupMenusNested";
			case 3:
				return "DialogFontSize%";
			case 4:
				return "LookAndFeel";
			default:
				return "???";
			}
		} else {
			switch (row) {
			case 0:
				return Preferences.getCurrWidthTextPanel();
			case 1:
				return Preferences.getCurrHeightTextPanel();
			case 2:
				return Preferences.getCurrPopupMenusNested() ? 1 : 0;
			case 3:
				return Preferences.getCurrDialogFontSizePercent();
			case 4:
				return Preferences.getCurrLookAndFeel();
			default:
				return "???";
			}
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
		int v;
		try {
			v = Integer.parseInt((String) value);
		} catch (Exception e) {
			switch (row) {
			case 0:
				v = 600;
				break;
			case 1:
				v = 400;
				break;
			case 2:
				v = 0;
				break;
			case 3:
				v = 100;
				break;
			default:
				v = 0;
			}
		}
		switch (row) {
		case 0:
			if (v < 100) {
				v = 100;
			}
			Preferences.putCurrWidthTextPanel(v);
			break;
		case 1:
			if (v < 100) {
				v = 100;
			}
			Preferences.putCurrHeightTextPanel(v);
			break;
		case 2:
			Preferences.putCurrPopupMenusNested(v != 0);
			break;
		case 3:
			if ((v < 75) || (v > 150)) {
				Utilities
						.showInfoMessage("Wrong Value.\nAllowed Range: 75 ... 150 %.\nSet to default: 100 %.");
				v = 100;
			}
			if (Preferences.getCurrLookAndFeel().equals("nimbus") && (v != 100)) {
				Utilities
						.showInfoMessage("Wrong Value.\nWith 'nimbus' Look & Feel, value must be 100 %.");
				v = 100;
			}
			Preferences.putCurrDialogFontSizePercent(v);
			Utilities
					.showInfoMessage("Dialog Font Size Change will take effect after Restart of the Program.");
			break;
		case 4:
			String s = (String) value;
			if (s.equals("system") || s.equals("nimbus") || s.equals("metal")) {
				Preferences.putCurrLookAndFeel(s);
				if (s.equals("nimbus")) {
					Preferences.putCurrDialogFontSizePercent(100);
				}
			} else {
				Utilities
						.showInfoMessage("Value wrong.\nAllowed Values: 'system' / 'nimbus' / 'metal'.\nSet to default: 'system'.");
				Preferences.putCurrLookAndFeel("system");
			}
			Utilities
					.showInfoMessage("Look & Feel Change will take effect after Restart of the Program.");
			break;
		}
	}

}
