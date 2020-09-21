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

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

public class Utilities {

    public static String calcPercent(int part, int total) {
        if (total == 0) {
            return " (0.00 %)";
        }
        String r = String.valueOf(Math.round((part * 10000.0f) / total));
        if (r.length() == 1) {
            r = "00" + r;
        }
        if (r.length() == 2) {
            r = "0" + r;
        }
        return " (" + r.substring(0, r.length() - 2) + "."
                + r.substring(r.length() - 2) + " %)";
    }

    public static boolean checkFileNameOK(String textName) {
        return textName
                .equals(Utilities.replaceNonFileNameCharacters(textName));
    }

    public static void checkJavaVersion(int v1, int v2) {
        String version = System.getProperty("java.version");
        String[] v = version.split("\\.");
        boolean r = false;
        //TODO still a bit wrong, will do for now
        if (v.length > 1) {
            final int i = Integer.parseInt(v[0]);
            if ((i > v1)) {
                return;
            }
            if (i == v1) {
                if (Integer.parseInt(v[1]) >= v2){
                    r = true;
                }
            }
        }
        if (r) {
            return;
        }
        Utilities.showInfoMessage("This application needs Java "
                + String.valueOf(v1) + "." + String.valueOf(v2)
                + " or higher.\nYour Java Runtime Engine has version "
                + version + ".\nProgram aborted.");
        System.exit(0);
    }

    public static void checkSingleProgramInstance() {
        try {
            File lockFile = new File(Constants.LOCK_FILE_PATH);
            if (lockFile.exists()) {
                if (Utilities
                        .showYesNoQuestion(
                                "It seems that "
                                        + Constants.SHORT_NAME
                                        + " is already running,\n"
                                        + "as the lock file "
                                        + Constants.LOCK_FILE_PATH
                                        + " exists.\n\nContinue anyway (may cause data losses)?",
                                false)) {
                    lockFile.delete();
                    return;
                }
                System.exit(1);
            }
            lockFile.deleteOnExit();
            lockFile.createNewFile();
        } catch (Exception e) {
        }
    }

    public static File chooseFile(File dir, String title) {
        JFileChooser c = new JFileChooser(dir);
        c.setDialogTitle(title);
        int rVal = c.showOpenDialog(new JFrame());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            if (c.getSelectedFile().isFile() && c.getSelectedFile().canRead()) {
                return c.getSelectedFile();
            }
        }
        return null;
    }

    public static boolean copyFile(File inputFile, File outputFile) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean msg;
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            msg = true;
        } catch (Exception e) {
            msg = false;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
        return msg;
    }

    public static boolean createEmptyFile(File f) {
        boolean ok;
        try {
            ok = f.createNewFile();
        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }

    public static boolean createNewFile(File f) {
        try {
            return f.createNewFile();
        } catch (Exception e) {
            return false;
        }
    }

    public static String createRegExpFromWildCard(String s) {
        String r = s.replace("+", "\\+");
        r = r.replace(".", "\\.");
        r = r.replace("$", "\\$");
        r = r.replace("^", "\\^");
        r = r.replace("/", "\\/");
        r = r.replace("|", "\\|");
        r = r.replace("(", "\\(");
        r = r.replace(")", "\\)");
        r = r.replace("[", "\\[");
        r = r.replace("]", "\\]");
        r = r.replace("{", "\\{");
        r = r.replace("}", "\\}");
        r = r.replace("?", ".");
        r = r.replace("*", ".*");
        return r;
    }

    public static File CreateTempFile(String prefix, String suffix, File dir) {
        File f = null;
        try {
            f = File.createTempFile(prefix, suffix, dir);
            f.deleteOnExit();
        } catch (IOException e) {
            Utilities.showErrorMessage("Cannot create temporary file.");
            Application.stop();
        }
        return f;
    }

    public static String escapeHTML(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }

    public static File getApplicationDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static String getClipBoardText() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDownloadsDirectoryPath() {
        String r = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Downloads";
        File f = new File(r);
        if (f.exists() && f.isDirectory()) {
            return r;
        }
        r = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Download";
        f = new File(r);
        if (f.exists() && f.isDirectory()) {
            return r;
        }
        r = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Desktop"
                + System.getProperty("file.separator") + "Downloads";
        f = new File(r);
        if (f.exists() && f.isDirectory()) {
            return r;
        }
        r = System.getProperty("user.home")
                + System.getProperty("file.separator") + "Desktop";
        f = new File(r);
        if (f.exists() && f.isDirectory()) {
            return r;
        }
        return System.getProperty("user.home");
    }

    public static Vector<String> getSubDirectories(File dir) {
        Vector<String> dirnames = new Vector<String>();
        if (dir.isDirectory()) {
            String[] subdirs = dir.list(new FileFilterTextDirsOnly());
            for (String subdir : subdirs) {
                dirnames.add(subdir);
            }
        }
        Collections.sort(dirnames);
        return dirnames;
    }

    public static Vector<String> getTextFileList(File dir) {
        Vector<String> filenames = new Vector<String>();
        if (dir.isDirectory()) {
            String[] files = dir.list(new FileFilterTextFilesOnly());
            for (String file : files) {
                filenames.add(file);
            }
        }
        Collections.sort(filenames);
        return filenames;
    }

    public static void initializeFontSize(int percent) {
        if (percent != 100) {
            float multiplier = percent / 100.0f;
            UIDefaults defaults = UIManager.getDefaults();
            for (Enumeration<Object> e = defaults.keys(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                Object value = defaults.get(key);
                if (value instanceof Font) {
                    Font font = (Font) value;
                    int newSize = Math.round(font.getSize() * multiplier);
                    if (value instanceof FontUIResource) {
                        defaults.put(key,
                                new FontUIResource("Dialog", font.getStyle(),
                                        newSize));
                    } else {
                        defaults.put(key, new Font("Dialog", font.getStyle(),
                                newSize));
                    }
                }
            }
        }
    }

    public static boolean isMac() {
        return (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0);
    }

    public static String leftTrim(String s) {
        int i = 0;
        while ((i < s.length()) && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    public static String limitStringRight(int max, String s) {
        int l = s.length();
        if (l > (max - 2)) {
            return "… " + s.substring((l - max) + 2, l);
        } else {
            return s;
        }
    }

    public static void openDirectoryInFileExplorer(File dir) {
        try {
            Desktop.getDesktop().open(dir);
            return;
        } catch (Exception e) {
        }
        String os = System.getProperty("os.name").toLowerCase();
        if ((os.indexOf("nix") >= 0) || (os.indexOf("nux") >= 0)) {
            try {
                String[] fmgrs = {"nautilus", "thunar", "konqueror"};
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < fmgrs.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + fmgrs[i] + " \""
                            + dir.getAbsolutePath() + "\" ");
                }
                Runtime.getRuntime().exec(
                        new String[]{"sh", "-c", cmd.toString()});
            } catch (Exception e) {
                Utilities
                        .showErrorMessage("Opening a Directory in a File Explorer not possible\n(none found, try to install a newer Java).");
            }
        } else {
            Utilities
                    .showErrorMessage("Opening a Directory in a File Explorer not possible\n(desktop integration problem, try to install a newer Java).");
        }
    }

    public static void openTextFileInEditor(File textFile) {
        try {
            Desktop.getDesktop().edit(textFile);
            return;
        } catch (Exception e) {
        }
        String os = System.getProperty("os.name").toLowerCase();
        if ((os.indexOf("nix") >= 0) || (os.indexOf("nux") >= 0)) {
            try {
                String[] editors = {"leafpad", "gedit", "konqueror"};
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < editors.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + editors[i] + " \""
                            + textFile.getAbsolutePath() + "\" ");
                }
                Runtime.getRuntime().exec(
                        new String[]{"sh", "-c", cmd.toString()});
            } catch (Exception e) {
                Utilities
                        .showErrorMessage("Text file cannot be edited\n(no editor found, try to install a newer Java).");
            }
        } else {
            Utilities
                    .showErrorMessage("Text file cannot be edited\n(desktop integration problem, try to install a newer Java).");
        }
    }

    public static void openURLInDefaultBrowser(String urlstring) {
        URI u = null;
        try {
            u = new URI(urlstring);
        } catch (Exception e) {
            Utilities.showErrorMessage("URL cannot be opened (Malformed URL).");
            return;
        }
        String os = System.getProperty("os.name").toLowerCase();
        if ((os.indexOf("nix") >= 0) || (os.indexOf("nux") >= 0)) {
            try {
                String[] browsers = {"epiphany", "firefox", "mozilla",
                        "konqueror", "opera"};
                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
                            + urlstring + "\" ");
                }
                Runtime.getRuntime().exec(
                        new String[]{"sh", "-c", cmd.toString()});
            } catch (Exception e) {
                Utilities
                        .showErrorMessage("URL cannot be opened\n(no browser found, try to install a newer Java).");
            }
        } else {
            try {
                Desktop.getDesktop().browse(u);
            } catch (Exception e) {
                Utilities
                        .showErrorMessage("URL cannot be opened\n(desktop integration problem, try to install a newer Java).");
            }
        }
    }

    public static String readFileFromJarIntoString(String s) {
        try {
            InputStream is = Utilities.class.getResourceAsStream(s);
            InputStreamReader isr = new InputStreamReader(is,
                    Constants.ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String text = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                line = line.trim();
                text += line + Constants.EOL;
            }
            in.close();
            isr.close();
            is.close();
            return text.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String readFileIntoString(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis,
                    Constants.ENCODING);
            BufferedReader in = new BufferedReader(isr);
            String text = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                line = line.trim();
                text += line + Constants.UNIX_EOL;
            }
            in.close();
            isr.close();
            fis.close();
            return text.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean renameFile(File from, File to) {
        boolean msg;
        try {
            from.renameTo(to);
            msg = true;
        } catch (Exception e) {
            msg = false;
        }
        return msg;
    }

    public static String replaceControlCharactersWithSpace(String s) {
        return s.replaceAll("[\\u0000-\\u001F]+", " ").trim();
    }

    public static String replaceNonFileNameCharacters(String s) {
        if (s.startsWith(".")) {
            s = "-" + s.substring(1);
        }
        return Utilities.replaceControlCharactersWithSpace(s).replaceAll(
                "[\\\\\\/\\:\\\"\\*\\?\\<\\>\\|]+", "-");
    }

    public static String reverseString(String s) {
        return (new StringBuffer(s)).reverse().toString();
    }

    public static File saveFileDialog(JFrame frame, String title,
                                      String initialDirectoryPath) {
        if (!Utilities.isMac()) {
            JFileChooser chooser;
            File f = new File(initialDirectoryPath);
            if (f.isDirectory()) {
                chooser = new JFileChooser(f);
            } else {
                chooser = new JFileChooser();
            }
            chooser.setDialogTitle(title);
            int returnVal = chooser.showSaveDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile().exists()) {
                    if (Utilities.showYesNoQuestion("File "
                            + chooser.getSelectedFile().getAbsolutePath()
                            + " exists.\nOverwrite?", false)) {
                        return chooser.getSelectedFile();
                    } else {
                        return Utilities.saveFileDialog(frame, title, chooser
                                .getSelectedFile().getParent());
                    }
                } else {
                    return chooser.getSelectedFile();
                }
            } else {
                return null;
            }
        } else {
            FileDialog chooser = new FileDialog(frame, title, FileDialog.SAVE);
            File f = new File(initialDirectoryPath);
            if (f.isDirectory()) {
                chooser.setDirectory(f.getPath());
            } else {
                chooser.setDirectory(System.getProperty("user.home"));
            }
            chooser.setVisible(true);
            if (chooser.getFile() != null) {
                return new File(chooser.getDirectory() + chooser.getFile());
            } else {
                return null;
            }
        }
    }

    public static File selectDirectory(JFrame frame, String title,
                                       String initialDirectoryPath) {
        JFileChooser chooser;
        File f = new File(initialDirectoryPath);
        if (f.isDirectory()) {
            chooser = new JFileChooser(f);
        } else {
            chooser = new JFileChooser();
        }
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static void setComponentOrientation(Component c) {
        boolean rightToLeft = Application.getLanguage().getRightToLeft();
        if (rightToLeft) {
            c.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        } else {
            c.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    }

    public static void setHorizontalAlignment(JLabel c) {
        boolean rightToLeft = Application.getLanguage().getRightToLeft();
        if (rightToLeft) {
            c.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            c.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    public static void showAboutDialog() {
        Object[] options = {"Close", "Open Website"};
        int result = JOptionPane
                .showOptionDialog(
                        null,
                        Constants.SHORT_NAME
                                + " - "
                                + Constants.LONG_NAME
                                + " - Version "
                                + Constants.VERSION
                                + "\n"
                                + Constants.COPYRIGHT
                                + "\n"
                                + "Website: "
                                + Constants.WEBSITE
                                + "\n\n"
                                + "This program is available free of charge.\n"
                                + "All liability shall be excluded. Use at your own risk!\n"
                                + "Any commercial use is prohibited.\n\n"
                                + "Code license: MIT License. Please read the full text \n"
                                + "at http://opensource.org/licenses/mit-license.php\n\n"
                                + Constants.SHORT_NAME
                                + " is inspired from LingQ (http://lingq.com) and\n"
                                + "contains code from 'Learning With Texts' (http://lwt.sf.net).\n"
                                + Constants.SHORT_NAME
                                + " uses MigLayout (http://miglayout.com).\n\n"
                                + "Currently used Java Runtime Engine: Version "
                                + System.getProperty("java.version") + ".",
                        "About " + Constants.SHORT_NAME,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                                (new JFrame()).getClass().getResource(
                                        Constants.ICONPATH))), options,
                        options[0]);
        if (result == 1) {
            Utilities.openURLInDefaultBrowser(Constants.WEBSITE);
        }
    }

    public static void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE,
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        (new JFrame()).getClass().getResource(
                                Constants.ICONPATH))));
    }

    public static void showInfoMessage(String msg) {
        final URL url = (new JFrame()).getClass().getResource(Constants.ICONPATH);
        final ImageIcon icon = null; //new ImageIcon(Toolkit.getDefaultToolkit().getImage(url));

        JOptionPane.showMessageDialog(
                null,
                msg,
                "Information",
                JOptionPane.INFORMATION_MESSAGE,
                icon
        );
    }

    public static String showInputDialog(String msg) {
        String r = (String) JOptionPane.showInputDialog(
                null,
                msg,
                "Input",
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        (new JFrame()).getClass().getResource(
                                Constants.ICONPATH))), null, null);
        return (r == null ? "" : r);
    }

    public static boolean showYesNoQuestion(String msg, boolean dft) {
        Object[] options = {"Yes", "No"};
        return JOptionPane.showOptionDialog(
                null,
                msg,
                "Question",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(Toolkit.getDefaultToolkit().getImage(
                        (new JFrame()).getClass().getResource(
                                Constants.ICONPATH))), options,
                options[(dft ? 0 : 1)]) == 0;
    }

    public static boolean writeStringIntoFile(File f, String s) {
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(f), Constants.ENCODING));
            out.print(s);
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
