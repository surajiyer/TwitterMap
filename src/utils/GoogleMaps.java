/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.teamdev.jxbrowser.chromium.Browser;

/**
 * Contains all methods necessary for executing JavaScript commands on google 
 * maps.
 * 
 * @author S.S.Iyer
 */
public class GoogleMaps {
    
    /** Map Zoom Range */
    public static final int MIN_ZOOM = 0;
    public static final int MAX_ZOOM = 21;
    
    /** In map.html file default zoom value is set to 4. */
    private static int zoomValue = 4;
    
    /**
     * Set a marker on the map at the given location coordinates.
     * 
     * @param browser reference to the browser in which the map is loaded.
     * @param latitude latitude of the marker location
     * @param longitude longitude of the marker location
     * @param title title of the marker (Tweet Message)
     * @param iconName Name of custom marker icon file
     * @param tag Loaded from file (file), user defined (user) or twitter 
     * generated (tweet)
     */
    public static void setMarker(Browser browser, double latitude, 
            double longitude, String title, String iconName, String tag) throws 
            NumberFormatException, IllegalArgumentException {
        
        if(latitude < -90d || latitude > 90d) {
            throw new IllegalArgumentException("Latitude out of range.");
        }
        
        if(longitude < -180d || longitude > 180d) {
            throw new IllegalArgumentException("Longitude out of range.");
        }
        
        // Wait until browser has finished loading.
        while(browser.isLoading()) {}
        
        browser.executeJavaScript("addMarker("+latitude+","+longitude+",'"+title+"','"+
                GoogleMaps.class.getClass().getResource("/res/").toString()+iconName+"','"+tag+"')");
    }
    
    /**
     * Set a marker on the map at the given location coordinates.
     * 
     * @param browser reference to the browser in which the map is loaded.
     * @param latitude latitude of the marker location
     * @param longitude longitude of the marker location
     * @param title title of the marker (Tweet Message)
     * @param tag Loaded from file (file), user defined (user) or twitter 
     * generated (tweet)
     */
    public static void setMarker(Browser browser, double latitude, 
            double longitude, String title, String tag) {
        setMarker(browser, latitude, longitude, title, "map_marker_24.png", tag);
    }
    
    /**
     * Zoom into maps.
     * 
     * @param browser reference to the browser in which the map is loaded.
     */
    public static void zoomIn(Browser browser) {
        if (zoomValue < MAX_ZOOM) {
            browser.executeJavaScript("map.setZoom(" + ++zoomValue + ")");
        }
    }
    
    /**
     * Zoom out of maps.
     * 
     * @param browser reference to the browser in which the map is loaded.
     */
    public static void zoomOut(Browser browser) {
        if (zoomValue > MIN_ZOOM) {
            browser.executeJavaScript("map.setZoom(" + --zoomValue + ")");
        }
    }
    
    /**
     * Remove all twitter generated markers from the map.
     * @param browser 
     */
    public static void clearTwitterMarkers(Browser browser) {
        browser.executeJavaScriptAndReturnValue("clearTwitterMarkers()");
    }
    
    /**
     * Remove all user generated markers.
     * @param browser 
     */
    public static void clearUserMarkers(Browser browser) {
        browser.executeJavaScriptAndReturnValue("clearUserMarkers()");
    }
    
    /**
     * Remove all markers on the map.
     * @param browser 
     */
    public static void clearAllMarkers(Browser browser) {
        browser.executeJavaScriptAndReturnValue("clearAllMarkers()");
    }
}
