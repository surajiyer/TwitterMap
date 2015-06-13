/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Main.GUI;
import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author S.S.Iyer
 */
public class UIutils {

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @param filename the name of the image file to load the icon from.
     * @return ImageIcon object
     */
    public static ImageIcon createImageIcon(String filename) {
        URL url = null;
        try {
            url = new URL(UIutils.class.getClass().getResource("/res/").toString()+filename);
        } catch (MalformedURLException ex) {
            Logger.getLogger(UIutils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ImageIcon(url);
    }

    /**
     * Checks if basic network connection is present and returns true if it
     * does.
     *
     * @return true if network connection exists, otherwise false.
     */
    public static boolean isConnected() {
        boolean reachable = false;
        try {
            Process p1 = Runtime.getRuntime().exec("ping www.google.com");
            reachable = p1.waitFor() == 0;
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!reachable) {
            JOptionPane.showMessageDialog(null, "No Internet Connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return reachable;
    }

    /**
     * Find the {@code n}th occurrence of a string {@code s} in a string {@code str}.
     * @param str the string in which to search for.
     * @param s the string to search for.
     * @param n the nth occurrence of said string.
     * @return the position of the nth occurrence of {@code s}
     */
    public static int nthOccurrence(String str, String s, int n) {
        int pos = str.indexOf(s, 0);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(s, pos + 1);
        }
        return pos;
    }
    
    /**
     * Open the given URL in the default web browser of the system.
     * @param uri the link address to load in the browser.
     */
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open the given URL in the default web browser of the system.
     * @param url the link address to load in the browser.
     */
    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
}
