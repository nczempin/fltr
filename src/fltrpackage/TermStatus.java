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

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Vector;

public enum TermStatus {

	None(0), Unknown(1), Learning2(2), Learning3(3), Learning4(4), Known(5), Ignored(
			98), WellKnown(99);

	public static Vector<TermStatus> getAllActive() {
		Vector<TermStatus> r = new Vector<TermStatus>(7);
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			if (status != None) {
				r.add(status);
			}
		}
		r.trimToSize();
		return r;
	}

	public static String getAllStatuses() {
		Vector<TermStatus> all = TermStatus.getAllActive();
		String s = "|";
		for (TermStatus ts : all) {
			s += String.valueOf(ts.statusCode) + "|";
		}
		return s;
	}

	public static TermStatus getStatusFromCode(int i) {
		if (TermStatus.lookup.containsKey(i)) {
			return TermStatus.lookup.get(i);
		} else {
			return Unknown;
		}
	}

	public static TermStatus getStatusFromOrdinal(int i) {
		if (TermStatus.lookupOrdinal.containsKey(i)) {
			return TermStatus.lookupOrdinal.get(i);
		} else {
			return Unknown;
		}
	}

	public static TermStatus getStatusFromText(String s) {
		int rightPar = s.lastIndexOf(')');
		int leftPar = s.lastIndexOf('(');
		int i = 1;
		if ((leftPar > 0) && (rightPar > leftPar)) {
			i = Integer.parseInt(s.substring(leftPar + 1, rightPar));
		}
		return TermStatus.getStatusFromCode(i);
	}

	private final int statusCode;

	private static final HashMap<Integer, TermStatus> lookup = new HashMap<Integer, TermStatus>();

	private static final HashMap<Integer, TermStatus> lookupOrdinal = new HashMap<Integer, TermStatus>();

	private static final HashMap<TermStatus, Color> colors = new HashMap<TermStatus, Color>();

	private static final HashMap<TermStatus, String> texts = new HashMap<TermStatus, String>();

	private static final HashMap<TermStatus, String> shorttexts = new HashMap<TermStatus, String>();

	static {
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			TermStatus.lookup.put(status.statusCode, status);
			TermStatus.lookupOrdinal.put(status.ordinal(), status);
		}

		TermStatus.colors.put(None, new Color(180, 188, 255)); // B4BCFF
		TermStatus.colors.put(Unknown, new Color(245, 184, 169)); // F5B8A9
		TermStatus.colors.put(Learning2, new Color(245, 204, 169)); // F5CCA9
		TermStatus.colors.put(Learning3, new Color(245, 225, 169)); // F5E1A9
		TermStatus.colors.put(Learning4, new Color(245, 243, 169)); // F5F3A9
		TermStatus.colors.put(Known, new Color(197, 255, 197)); // C5FFC5
		TermStatus.colors.put(Ignored, new Color(229, 229, 229)); // E5E5E5
		TermStatus.colors.put(WellKnown, new Color(229, 255, 229)); // E5FFE5

		TermStatus.texts.put(None, "No status (" + None.getStatusCode() + ")");
		TermStatus.texts.put(Unknown, "Unknown (" + Unknown.getStatusCode()
				+ ")");
		TermStatus.texts.put(Learning2,
				"Learning (" + Learning2.getStatusCode() + ")");
		TermStatus.texts.put(Learning3,
				"Learning (" + Learning3.getStatusCode() + ")");
		TermStatus.texts.put(Learning4,
				"Learning (" + Learning4.getStatusCode() + ")");
		TermStatus.texts.put(Known, "Known (" + Known.getStatusCode() + ")");
		TermStatus.texts.put(Ignored, "Ignored (" + Ignored.getStatusCode()
				+ ")");
		TermStatus.texts.put(WellKnown,
				"Well Known (" + WellKnown.getStatusCode() + ")");

		TermStatus.shorttexts.put(None, "No Status");
		TermStatus.shorttexts.put(Unknown, "Unknown/1");
		TermStatus.shorttexts.put(Learning2, "Learning/2");
		TermStatus.shorttexts.put(Learning3, "Learning/3");
		TermStatus.shorttexts.put(Learning4, "Learning/4");
		TermStatus.shorttexts.put(Known, "Known/5");
		TermStatus.shorttexts.put(Ignored, "Ignored");
		TermStatus.shorttexts.put(WellKnown, "Well Known");
	}

	public static Vector<ComboBoxItem> getComboBoxItemVector() {
		Vector<ComboBoxItem> r = new Vector<ComboBoxItem>(7);
		for (TermStatus status : EnumSet.allOf(TermStatus.class)) {
			if (status != None) {
				r.add(new ComboBoxItem(status.getStatusText(), 1000));
			}
		}
		r.trimToSize();
		return r;
	}

	TermStatus(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Color getStatusColor() {
		if (TermStatus.colors.containsKey(this)) {
			return TermStatus.colors.get(this);
		} else {
			return Color.WHITE;
		}
	}

	public String getStatusShortText() {
		if (TermStatus.texts.containsKey(this)) {
			return TermStatus.shorttexts.get(this);
		} else {
			return "???";
		}
	}

	public String getStatusText() {
		if (TermStatus.texts.containsKey(this)) {
			return TermStatus.texts.get(this);
		} else {
			return "???";
		}
	}

}
