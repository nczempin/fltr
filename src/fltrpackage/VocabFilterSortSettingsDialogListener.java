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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class VocabFilterSortSettingsDialogListener implements ActionListener {

	private VocabFilterSortSettingsDialog frame;

	public VocabFilterSortSettingsDialogListener(
			VocabFilterSortSettingsDialog frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(frame.getRootPane())) {
			frame.setResult(0);
			frame.setVisible(false);
			frame.dispose();
		} else if (o.equals(frame.getButAllStatusFilter())) {
			for (JCheckBox cb : frame.getCbStatusFilter()) {
				cb.setSelected(true);
			}
		} else if (o.equals(frame.getButNoStatusFilter())) {
			for (JCheckBox cb : frame.getCbStatusFilter()) {
				cb.setSelected(false);
			}
		} else if (o.equals(frame.getButClearWordFilter())) {
			frame.getTfWordFilter().setText("");
		} else if (o.equals(frame.getButCancel())) {
			frame.setResult(0);
			frame.setVisible(false);
			frame.dispose();
		} else if (o.equals(frame.getButGo())
				|| o.equals(frame.getButGoBrowser())
				|| o.equals(frame.getButGoExport())) {
			String statuses = "";
			for (int i = 0; i < frame.getCbStatusFilter().length; i++) {
				JCheckBox cb = frame.getCbStatusFilter()[i];
				String cbText = String.valueOf((i < 5 ? i + 1 : (i == 5 ? 98
						: 99)));
				if (cb.isSelected()) {
					statuses += "|" + cbText;
				}
			}
			if (!statuses.isEmpty()) {
				statuses = statuses.substring(1);
			}
			Preferences.putCurrVocabStatusFilter(statuses);
			Preferences.putCurrVocabWordFilter(frame.getTfWordFilter()
					.getText().trim());
			Preferences.putCurrVocabTextFilter(((ComboBoxItem) frame
					.getCbTextFiles().getSelectedItem()).getText());
			int currSort = 0;
			for (int i = 0; i < frame.getRbSortOptions().length; i++) {
				if (frame.getRbSortOptions()[i].isSelected()) {
					currSort = i;
					break;
				}
			}
			Preferences.putCurrVocabSortOrder(currSort);
			int currMax = 0;
			for (int i = 0; i < frame.getRbMaxResults().length; i++) {
				if (frame.getRbMaxResults()[i].isSelected()) {
					currMax = i;
					break;
				}
			}
			Preferences.putCurrVocabMaxResult(currMax);
			frame.setResult(o.equals(frame.getButGo()) ? 1 : (o.equals(frame
					.getButGoBrowser()) ? 2 : 3));
			frame.setVisible(false);
			frame.dispose();
		}
	}

}
