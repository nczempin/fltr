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

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;

public class MultiLineTextFieldListener implements KeyListener {

	private JFrame frame;

	public MultiLineTextFieldListener(JFrame frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Tab + ShiftTab to next/prev. field, Enter = default key of frame or
		// disabled
		if ((e.getKeyCode() == KeyEvent.VK_TAB) && (!e.isShiftDown())) {
			e.consume();
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.focusNextComponent();
		} else if ((e.getKeyCode() == KeyEvent.VK_TAB) && e.isShiftDown()) {
			e.consume();
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.focusPreviousComponent();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JRootPane rootPane = frame.getRootPane();
			JButton dftBtn = rootPane.getDefaultButton();
			if (dftBtn != null) {
				dftBtn.doClick();
			} else {
				e.consume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

}
