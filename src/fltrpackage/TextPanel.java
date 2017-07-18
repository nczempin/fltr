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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class TextPanel extends JPanel {

	private JScrollPane scrollPane;
	private JViewport viewPort;
	private Font font;

	public TextPanel(Dimension size, Font font) {
		super();
		this.font = font;
		setBackground(Color.WHITE);
		scrollPane = new JScrollPane(this);
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(50);
		scrollPane.setPreferredSize(size);
		scrollPane.setMaximumSize(size);
		scrollPane.setMinimumSize(size);
		viewPort = scrollPane.getViewport();
		viewPort.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		viewPort.setView(this);
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JViewport getViewPort() {
		return viewPort;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Text text = Application.getText();
		Language lang = Application.getLanguage();
		String langName = lang.getLangName();
		boolean rtl = lang.getRightToLeft();
		String fileName = Application.getText().getFile().getName();
		String textTitle = fileName.substring(0, fileName.length()
				- Constants.TEXT_FILE_EXTENSION_LENGTH);
		Application.getTextFrame().setTitle(
				Constants.SHORT_NAME + " - [NEW: "
						+ String.valueOf(text.getUnlearnedWordCount()) + "] - "
						+ langName + " - " + textTitle);
		boolean marked = text.isRangeMarked();
		int indexStart = Math.min(text.getMarkIndexStart(),
				text.getMarkIndexEnd());
		int indexEnd = Math.max(text.getMarkIndexStart(),
				text.getMarkIndexEnd());
		Graphics2D g2d = (Graphics2D) g;
		int width = viewPort.getWidth() - (rtl ? 15 : 20);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout textLayout = new TextLayout("X", font, frc);
		int h = (int) (textLayout.getAscent() + textLayout.getDescent());
		new Color(230, 230, 230);
		new Color(250, 250, 210);
		float asc = textLayout.getAscent();
		if (text.isCoordSet()) {
			Point viewPos = viewPort.getViewPosition();
			int top = viewPos.y - (h * 2);
			int bot = viewPos.y + viewPort.getHeight() + (h * 2);
			for (int i = 0; i < text.getTextItems().size(); i++) {
				TextItem item = text.getTextItems().get(i);
				Point p = item.getTextItemPosition();
				Dimension d = item.getTextItemDimension();
				if (p != null) {
					if ((p.y > top) && (p.y < bot)) {
						Term term = item.getLink();
						boolean notLastWord = !item.isLastWord();
						Color c;
						if (term == null) {
							c = TermStatus.None.getStatusColor();
						} else {
							c = term.getStatus().getStatusColor();
						}
						g2d.setPaint(c);
						if (notLastWord) {
							g2d.fill(new Rectangle(p.x, p.y, d.width, d.height));
						} else {
							if (rtl) {
								g2d.fill(new Rectangle(p.x + 1, p.y,
										d.width - 1, d.height));
							} else {
								g2d.fill(new Rectangle(p.x, p.y, d.width - 1,
										d.height));
							}
						}
						if (notLastWord) {
							Point p2 = item.getAfterItemPosition();
							Dimension d2 = item.getAfterItemDimension();
							g2d.fill(new Rectangle(p2.x, p2.y, d2.width,
									d2.height));
						}
						g2d.setPaint(Color.BLACK);
						if (marked && (i >= indexStart) && (i <= indexEnd)) {
							Rectangle2D rect = new Rectangle2D.Float(p.x, p.y,
									d.width - 1, d.height);
							g2d.draw(rect);
						}
						String s = item.getTextItemValue();
						if (!s.isEmpty()) {
							textLayout = new TextLayout((rtl ? '\u200F' : "")
									+ s, font, frc);
							textLayout.draw(g2d, p.x, p.y + asc);
						}
						s = item.getAfterItemValue();
						if (!s.isEmpty()) {
							p = item.getAfterItemPosition();
							textLayout = new TextLayout((rtl ? '\u200F' : "")
									+ s, font, frc);
							textLayout.draw(g2d, p.x, p.y + asc);
						}
					}
				}
			}
		} else {
			float x = 10.0f;
			float y = 6.0f;
			int lines = 1;
			for (TextItem item : text.getTextItems()) {
				if (item.getTextItemValue().equals(Constants.PARAGRAPH_MARKER)
						&& item.getAfterItemValue().equals("")) {
					x = 10.0f;
					y += 1.3f * (textLayout.getAscent()
							+ textLayout.getDescent() + textLayout.getLeading() + 6);
					lines++;
				} else {
					String t = (rtl ? '\u200F' : "") + item.getTextItemValue()
							+ item.getAfterItemValue();
					textLayout = new TextLayout(t, font, frc);
					if ((x + textLayout.getAdvance()) > width) {
						x = 10.0f;
						y += textLayout.getAscent() + textLayout.getDescent()
								+ textLayout.getLeading() + 6;
						lines++;
					}
					float l = 0.0f;

					if (!item.getTextItemValue().isEmpty()) {
						l = (new TextLayout((rtl ? '\u200F' : "")
								+ item.getTextItemValue(), font, frc))
								.getAdvance();
					}
					float l2 = 0.0f;
					if (!item.getAfterItemValue().isEmpty()) {
						l2 = (new TextLayout((rtl ? '\u200F' : "")
								+ item.getAfterItemValue(), font, frc))
								.getAdvance();
					}
					item.setTextItemDimension(new Dimension((int) l, h));
					item.setAfterItemDimension(new Dimension((int) l2, h));
					if (rtl) {
						item.setTextItemPosition(new Point(width
								- (int) (x + l), (int) y));
						item.setAfterItemPosition(new Point(width
								- (int) (x + l + l2), (int) y));
					} else {
						item.setTextItemPosition(new Point((int) x, (int) y));
						item.setAfterItemPosition(new Point((int) (x + l),
								(int) y));
					}
					x += textLayout.getAdvance();
				}
			}
			y += textLayout.getAscent() + textLayout.getDescent()
					+ textLayout.getLeading() + 6;
			this.setSize(new Dimension(this.getSize().width - 20, (int) y));
			setPreferredSize(this.getSize());
			scrollPane.getVerticalScrollBar().setUnitIncrement((int) y / lines);
			text.setCoordSet(true);
		}
	}

}
