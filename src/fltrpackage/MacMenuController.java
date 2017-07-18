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

public class MacMenuController implements com.apple.eawt.AboutHandler,
		com.apple.eawt.QuitHandler {

	public MacMenuController() {
		try {
			final com.apple.eawt.Application application = com.apple.eawt.Application
					.getApplication();
			application.setAboutHandler(this);
			application.setQuitHandler(this);
		} catch (Throwable e) {
			Utilities
					.showErrorMessage("Program aborted. Reason: Setup of MacMenuController failed. "
							+ e.getMessage());
			Application.stop();
		}
	}

	@Override
	public void handleAbout(com.apple.eawt.AppEvent.AboutEvent e) {
		Utilities.showAboutDialog();
	}

	@Override
	public void handleQuitRequestWith(com.apple.eawt.AppEvent.QuitEvent e,
			com.apple.eawt.QuitResponse response) {
		if (Application.getStartFrame().isVisible()) {
			Application.stop();
			response.performQuit();
		} else {
			Utilities
					.showErrorMessage("'Quit application' only allowed in start window.\nClose text window first.");
			response.cancelQuit();
		}
	}

}
