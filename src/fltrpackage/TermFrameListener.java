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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

public class TermFrameListener implements ActionListener,
		HierarchyBoundsListener {

	private TermFrame frame;

	public TermFrameListener(TermFrame frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(frame.getButSave())) {
			String term = Utilities.replaceControlCharactersWithSpace(frame
					.getTfTerm().getTextArea().getText());
			String translation = Utilities
					.replaceControlCharactersWithSpace(frame.getTfTranslation()
							.getTextArea().getText());
			String romanization = Utilities
					.replaceControlCharactersWithSpace(frame
							.getTfRomanization().getTextArea().getText());
			String sentence = Utilities.replaceControlCharactersWithSpace(frame
					.getTfSentence().getTextArea().getText());
			TermStatus status = frame.getRbStatus();
			if (term.equals("")) {
				Utilities
						.showErrorMessage("Mandatory field.\nTerm must not be empty.");
				frame.getTfTerm().getTextArea().requestFocusInWindow();
				return;
			}
			if (!(status.equals(TermStatus.Ignored) || status
					.equals(TermStatus.WellKnown))) {
				if (translation.equals("")) {
					Utilities
							.showErrorMessage("Mandatory field.\nTranslation must not be empty, unless status is 'Ignored' or 'Well Known'.");
					frame.getTfTranslation().getTextArea()
							.requestFocusInWindow();
					return;
				}
			}
			String key = term.toLowerCase();
			boolean changedTerm = (!frame.getOriginalKey().equals(key));
			Terms terms = Application.getTerms();
			Term t = terms.getTermFromKey(key);
			boolean exists = (t != null);
			if (!exists) {
				if (changedTerm) {
					if (!Utilities.showYesNoQuestion(
							"You have changed the Term from\n["
									+ frame.getOriginalKey()
									+ "] to the NEW Term [" + key
									+ "].\n\nAre you sure?", true)) {
						return;
					}
				}
				terms.addTerm(new Term(term, translation, sentence,
						romanization, status.getStatusCode()));
			} else {
				if (changedTerm) {
					if (!Utilities
							.showYesNoQuestion(
									"You have changed the Term from\n["
											+ frame.getOriginalKey()
											+ "] to the EXISTENT Term ["
											+ key
											+ "].\n\nAre you sure to OVERWRITE?",
									false)) {
						return;
					}
				}
				t.setTerm(term);
				t.setTranslation(translation);
				t.setRomanization(romanization);
				t.setSentence(sentence);
				t.setStatus(status);
				terms.setDirty(true);
			}
			frame.setVisible(false);
			Application.getText().matchWithTerms();
			Application.getTextFrame().getTextPanel().repaint();
			Application.getTextFrame().getTextPanel().requestFocus();
		} else if (o.equals(frame.getButDelete())) {
			String term = Utilities.replaceControlCharactersWithSpace(frame
					.getTfTerm().getTextArea().getText());
			if (term.equals("")) {
				Utilities
						.showErrorMessage("Mandatory field.\nTerm must not be empty.");
				frame.getTfTerm().getTextArea().requestFocusInWindow();
				return;
			}
			String key = term.toLowerCase();
			Terms terms = Application.getTerms();
			Term t = terms.getTermFromKey(key);
			boolean exists = (t != null);
			if (!exists) {
				Utilities.showErrorMessage("Term '" + term
						+ "' does not exist.");
				frame.getTfTerm().getTextArea().requestFocusInWindow();
				return;
			}
			terms.removeTerm(t);
			frame.setVisible(false);
			Application.getText().matchWithTerms();
			Application.getTextFrame().getTextPanel().repaint();
			Application.getTextFrame().getTextPanel().requestFocus();
		} else if (o.equals(frame.getButLookup1())) {
			Application.getLanguage().lookupWordInBrowser(
					Utilities.replaceControlCharactersWithSpace(frame
							.getTfTerm().getTextArea().getText()), 1, true);
		} else if (o.equals(frame.getButLookup2())) {
			Application.getLanguage().lookupWordInBrowser(
					Utilities.replaceControlCharactersWithSpace(frame
							.getTfTerm().getTextArea().getText()), 2, true);
		} else if (o.equals(frame.getButLookup3())) {
			Application.getLanguage().lookupWordInBrowser(
					Utilities.replaceControlCharactersWithSpace(frame
							.getTfTerm().getTextArea().getText()), 3, true);
		}
	}

	@Override
	public void ancestorMoved(HierarchyEvent e) {
		Point p = frame.getLocation();
		Preferences.putCurrXPosTermWindow(p.x);
		Preferences.putCurrYPosTermWindow(p.y);
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
	}

}
