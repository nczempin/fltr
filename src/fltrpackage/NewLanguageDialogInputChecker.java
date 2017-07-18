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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewLanguageDialogInputChecker implements DocumentListener,
		ActionListener {

	private NewLanguageDialog frame;

	public NewLanguageDialogInputChecker(NewLanguageDialog frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		check();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		check();
	}

	private void check() {
		int l1 = frame.getCbLang1().getSelectedIndex();
		int l2 = frame.getCbLang2().getSelectedIndex();
		String text = frame.getTfLangName().getText().trim();
		boolean ok = ((l1 > 0) && (l2 > 0) && (!text.isEmpty()));
		if (ok && (l1 == l2)) {
			if (l1 > 1) {
				ok = false;
			}
		}
		frame.getButCreate().setEnabled(ok);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		check();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		check();
	}

}
