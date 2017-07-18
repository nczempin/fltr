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

import java.awt.Dimension;
import java.awt.Point;

public class TextItem {

	private String textItemValue;
	private String textItemLowerCaseValue;
	private String afterItemValue;
	private Point textItemPosition;
	private Dimension textItemDimension;
	private Point afterItemPosition;
	private Dimension afterItemDimension;
	private Term link;
	private boolean lastWord;

	public TextItem(String textItemValue, String afterItemValue) {
		super();
		this.textItemValue = textItemValue;
		textItemLowerCaseValue = textItemValue.toLowerCase();
		this.afterItemValue = afterItemValue;
		textItemPosition = null;
		textItemDimension = null;
		link = null;
		lastWord = true;
	}

	public Dimension getAfterItemDimension() {
		return afterItemDimension;
	}

	public Point getAfterItemPosition() {
		return afterItemPosition;
	}

	public String getAfterItemValue() {
		return afterItemValue;
	}

	public Term getLink() {
		return link;
	}

	public Dimension getTextItemDimension() {
		return textItemDimension;
	}

	public String getTextItemLowerCaseValue() {
		return textItemLowerCaseValue;
	}

	public Point getTextItemPosition() {
		return textItemPosition;
	}

	public String getTextItemValue() {
		return textItemValue;
	}

	public boolean isLastWord() {
		return lastWord;
	}

	public boolean isPointOnTextItem(Point p) {
		if ((textItemPosition != null) && (textItemDimension != null)) {
			if (p.x > textItemPosition.x) {
				if (p.y > textItemPosition.y) {
					if (p.x < (textItemPosition.x + textItemDimension.width + afterItemDimension.width)) {
						if (p.y < (textItemPosition.y + textItemDimension.height)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void setAfterItemDimension(Dimension afterItemDimension) {
		this.afterItemDimension = afterItemDimension;
	}

	public void setAfterItemPosition(Point afterItemPosition) {
		this.afterItemPosition = afterItemPosition;
	}

	public void setLastWord(boolean lastWord) {
		this.lastWord = lastWord;
	}

	public void setLink(Term link) {
		this.link = link;
	}

	public void setTextItemDimension(Dimension textItemDimension) {
		this.textItemDimension = textItemDimension;
	}

	public void setTextItemPosition(Point textItemPosition) {
		this.textItemPosition = textItemPosition;
	}

	@Override
	public String toString() {
		return "[" + textItemValue + " / " + afterItemValue + "]";
	}

}
