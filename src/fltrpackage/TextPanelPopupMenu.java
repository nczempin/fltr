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

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class TextPanelPopupMenu extends JPopupMenu {

	private ActionListener listener;
	private boolean menuNested;

	public TextPanelPopupMenu(ActionListener listener) {
		super();
		this.listener = listener;
		menuNested = Preferences.getCurrPopupMenusNested();
	}

	private void createAndAddAllKnownSubMenu(JComponent main) {
		JComponent submenu;
		if (menuNested) {
			submenu = new JMenu("I KNOW ALL! ");
		} else {
			submenu = main;
			createAndAddNonActiveMenuItem(submenu, "I KNOW ALL!");
		}
		String count = String.valueOf(Application.getText()
				.getUnlearnedWordCount());
		createAndAddMenuItem(submenu, "Set ALL " + count + " New Words To "
				+ TermStatus.Ignored.getStatusText(), "knowall", "status",
				TermStatus.Ignored, "", null);
		createAndAddMenuItem(submenu, "Set ALL " + count + " New Words To "
				+ TermStatus.WellKnown.getStatusText(), "knowall", "status",
				TermStatus.WellKnown, "", null);
		if (menuNested) {
			main.add(submenu);
		}
	}

	private void createAndAddDictSubMenu(JComponent main, String w) {
		JComponent submenu;
		if (menuNested) {
			submenu = new JMenu("Lookup Dictionary ");
		} else {
			submenu = main;
			createAndAddNonActiveMenuItem(submenu, "Lookup Dictionary");
		}
		int count = 0;
		Language lang = Application.getLanguage();
		for (int i = 1; i <= 3; i++) {
			if (lang.isURLset(i)) {
				String host = lang.getURLHost(i);
				if (!host.isEmpty()) {
					host = " (" + host + ")";
				}
				createAndAddMenuItem(submenu, String.valueOf(i) + host + " …",
						"dict", "word", w, "link", new Integer(i));
				count++;
			}
		}
		if ((count > 0) && menuNested) {
			main.add(submenu);
		}
	}

	private void createAndAddKnownSubMenu(JComponent main, TextItem ti) {
		JComponent submenu;
		if (menuNested) {
			submenu = new JMenu("Set Status ");
		} else {
			submenu = main;
			createAndAddNonActiveMenuItem(submenu, "Set Status");
		}
		createAndAddMenuItem(submenu,
				"Set This Word To " + TermStatus.Ignored.getStatusText(),
				"knowthis", "textitem", ti, "status", TermStatus.Ignored);
		createAndAddMenuItem(submenu, "Set This Word To "
				+ TermStatus.WellKnown.getStatusText(), "knowthis", "textitem",
				ti, "status", TermStatus.WellKnown);
		if (menuNested) {
			main.add(submenu);
		}
	}

	private void createAndAddMenuItem(JComponent main, String text,
			String action, String key1, Object obj1, String key2, Object obj2) {
		JMenuItem menuItem = new JMenuItem();
		menuItem.addActionListener(listener);
		menuItem.setText(text);
		menuItem.putClientProperty("action", action);
		if (!key1.equals("") && (obj1 != null)) {
			menuItem.putClientProperty(key1, obj1);
		}
		if (!key2.equals("") && (obj2 != null)) {
			menuItem.putClientProperty(key2, obj2);
		}
		main.add(menuItem);
	}

	private void createAndAddNonActiveMenuItem(JComponent main, String text) {
		JMenuItem menuItem = new JMenuItem();
		menuItem.setText("･･･ " + text + " ･･･");
		menuItem.setEnabled(false);
		main.add(menuItem);
	}

	private void createAndAddRadioButtonMenuItem(JComponent main, String text,
			String action, String key1, Object obj1, String key2, Object obj2,
			boolean checked) {
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem();
		menuItem.addActionListener(listener);
		menuItem.setText(text);
		menuItem.putClientProperty("action", action);
		if (!key1.equals("") && (obj1 != null)) {
			menuItem.putClientProperty(key1, obj1);
		}
		if (!key2.equals("") && (obj2 != null)) {
			menuItem.putClientProperty(key2, obj2);
		}
		if (checked) {
			menuItem.setSelected(checked);
		}
		main.add(menuItem);
	}

	private void createAndAddStatusSubMenu(JComponent main, Term t) {
		JComponent submenu;
		if (menuNested) {
			submenu = new JMenu("Set Status ");
		} else {
			submenu = main;
			createAndAddNonActiveMenuItem(submenu, "Set Status");
		}
		Vector<TermStatus> allTS = TermStatus.getAllActive();
		TermStatus termStatus = t.getStatus();
		for (TermStatus ts : allTS) {
			createAndAddRadioButtonMenuItem(submenu, ts.getStatusText(),
					"setstatus", "term", t, "status", ts, termStatus.equals(ts));
		}
		if (menuNested) {
			main.add(submenu);
		}
	}

	private JMenu createAndAddSubMenuItem(JComponent main, String text) {
		JMenu submenu = new JMenu(text);
		main.add(submenu);
		return submenu;
	}

	public void updateMenu(TextItem ti, String w2, String w3) {
		if (ti == null) {
			return;
		}
		removeAll();
		ArrayList<String> termsList = new ArrayList<String>(2);
		Terms terms = Application.getTerms();
		Term t = ti.getLink();
		boolean multi = false;
		if ((t != null) && (t.isMultiWord() == true)) {
			termsList.add(t.getKey());
			multi = true;
			JComponent submenu = this;
			createAndAddMenuItem(submenu, "Edit [" + t.getTerm() + "] …",
					"edit", "term", t, "", null);
			submenu.add(new JSeparator());
			createAndAddDictSubMenu(submenu, t.getTerm());
			submenu.add(new JSeparator());
			createAndAddStatusSubMenu(submenu, t);
			submenu.add(new JSeparator());
			createAndAddMenuItem(submenu, "Delete [" + t.getTerm() + "]",
					"delete", "term", t, "", null);
		}
		t = terms.getTermFromKey(ti.getTextItemValue().toLowerCase());
		if (t != null) {
			JComponent submenu;
			if (multi) {
				this.add(new JSeparator());
				createAndAddNonActiveMenuItem(this, "Underlying Term(s)");
				submenu = createAndAddSubMenuItem(this, "Term [" + t.getTerm()
						+ "] ");
			} else {
				submenu = this;
			}
			createAndAddMenuItem(submenu, "Edit [" + t.getTerm() + "] …",
					"edit", "term", t, "", null);
			submenu.add(new JSeparator());
			createAndAddDictSubMenu(submenu, t.getTerm());
			submenu.add(new JSeparator());
			createAndAddStatusSubMenu(submenu, t);
			submenu.add(new JSeparator());
			createAndAddMenuItem(submenu, "Delete [" + t.getTerm() + "]",
					"delete", "term", t, "", null);
		} else {
			JComponent submenu;
			if (multi) {
				this.add(new JSeparator());
				createAndAddNonActiveMenuItem(this, "Underlying Term(s)");
				submenu = createAndAddSubMenuItem(this,
						"New Word [" + ti.getTextItemValue() + "] ");
			} else {
				submenu = this;
			}
			createAndAddMenuItem(submenu, "Create [" + ti.getTextItemValue()
					+ "] …", "new", "textitem", ti, "", null);
			submenu.add(new JSeparator());
			createAndAddDictSubMenu(submenu, ti.getTextItemValue());
			submenu.add(new JSeparator());
			createAndAddKnownSubMenu(submenu, ti);
			submenu.add(new JSeparator());
			createAndAddAllKnownSubMenu(submenu);
		}
		String key = w2.toLowerCase();
		t = terms.getTermFromKey(key);
		if (multi && (t != null) && (!termsList.contains(key))) {
			termsList.add(key);
			JComponent submenu;
			submenu = createAndAddSubMenuItem(this, "Term [" + t.getTerm()
					+ "] ");
			createAndAddMenuItem(submenu, "Edit [" + t.getTerm() + "] …",
					"edit", "term", t, "", null);
			submenu.add(new JSeparator());
			createAndAddDictSubMenu(submenu, t.getTerm());
			submenu.add(new JSeparator());
			createAndAddStatusSubMenu(submenu, t);
			submenu.add(new JSeparator());
			createAndAddMenuItem(submenu, "Delete [" + t.getTerm() + "]",
					"delete", "term", t, "", null);
		}
		key = w3.toLowerCase();
		t = terms.getTermFromKey(key);
		if (multi && (t != null) && (!termsList.contains(key))) {
			JComponent submenu;
			submenu = createAndAddSubMenuItem(this, "Term [" + t.getTerm()
					+ "] ");
			createAndAddMenuItem(submenu, "Edit [" + t.getTerm() + "] …",
					"edit", "term", t, "", null);
			submenu.add(new JSeparator());
			createAndAddDictSubMenu(submenu, t.getTerm());
			submenu.add(new JSeparator());
			createAndAddStatusSubMenu(submenu, t);
			submenu.add(new JSeparator());
			createAndAddMenuItem(submenu, "Delete [" + t.getTerm() + "]",
					"delete", "term", t, "", null);
		}
		this.add(new JSeparator());
		createAndAddMenuItem(this, "View Text in Browser…", "browser", "",
				null, "", null);
		createAndAddMenuItem(this, "Text & Vocabulary Counts…", "info", "",
				null, "", null);
		int missSent = Application.getText().getMissingSentenceCount();
		if (missSent > 0) {
			createAndAddMenuItem(this, "Add " + String.valueOf(missSent)
					+ " missing Sentences in existent Terms", "miss", "", null,
					"", null);
		}
	}

}
