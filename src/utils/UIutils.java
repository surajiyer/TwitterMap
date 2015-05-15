/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author s139662
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
    
}
