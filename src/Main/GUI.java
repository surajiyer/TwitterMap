/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FrameLoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadEvent;
import com.teamdev.jxbrowser.chromium.events.LoadListener;
import com.teamdev.jxbrowser.chromium.events.ProvisionalLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import utils.GoogleMaps;
import static utils.GoogleMaps.setMarker;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import twitterstream.TweetListener;
import utils.HintTextField;
import static utils.UIutils.createImageIcon;

/**
 *
 * @author s139662
 */
public class GUI extends javax.swing.JFrame implements TweetListener {

    /** Load the map.html file. */
    private final String map;
    
    /** Browser object from JxBrowser library */
    private final Browser browser;
    private final BrowserListener bListener;
    
    /** Image icons for the start/stop button. */
    private ImageIcon start, stop;
    
    /** time scale unit for setting running time. */
    private long timeScale;
    
    /**
     * Creates new form Map
     * @param bl browser listener object
     */
    public GUI(BrowserListener bl) {
        // Load certain necessary resources.
        map = getClass().getResource("/res/map.html").toString();
        start = createImageIcon("play_16.png");
        stop = createImageIcon("stop_16.png");
        
        // Create a browser, its associated UI view object and the browser listener.
        browser = new Browser();
        bListener = bl;
        browser.addLoadListener(new LoadListener() {

            @Override
            public void onStartLoadingFrame(StartLoadingEvent sle) {}

            @Override
            public void onProvisionalLoadingFrame(ProvisionalLoadingEvent ple) {}

            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent fle) {
                if (fle.isMainFrame()) {
                    bListener.onBrowserLoadSuccess();
                }
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent fle) {
                if (fle.isMainFrame()) {
                    bListener.onBrowserLoadFailed();
                    browser.loadURL(map);
                }
            }

            @Override
            public void onDocumentLoadedInFrame(FrameLoadEvent fle) {}

            @Override
            public void onDocumentLoadedInMainFrame(LoadEvent le) {}
        });
        
        /* Set the Windows look and feel and initialize all the GUI components. */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        initComponents();
        
        // Load certain variables.
        timeScale = 60000; // 1min = 60000ms
        
        // Add the map view to the GUI frame and load the map URL.
        mapPanel.add(new BrowserView(browser), BorderLayout.CENTER);
        browser.loadURL(map);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        keywordsDialog = new javax.swing.JDialog();
        loadedKeywordLabelScroller = new javax.swing.JScrollPane();
        loadedKeywordsLabel = new javax.swing.JLabel();
        clearAllKeywordsButton = new javax.swing.JButton();
        setMarkerDialog = new javax.swing.JDialog();
        enterLatitudeLabel = new javax.swing.JLabel();
        enterLongitudeLabel = new javax.swing.JLabel();
        latTextField = new javax.swing.JFormattedTextField();
        longTextField = new javax.swing.JFormattedTextField();
        setUserMarkerButton = new javax.swing.JButton();
        controlPanel = new javax.swing.JPanel();
        keywordPanel = new javax.swing.JPanel();
        enterKeywordTextField = new HintTextField("Enter Keyword here...");
        clearButton1 = new javax.swing.JButton();
        runtimePanel = new javax.swing.JPanel();
        enterRunTextField = new HintTextField("Enter Running time here...");
        clearButton2 = new javax.swing.JButton();
        timeSpinner = new javax.swing.JSpinner();
        startStopButton2 = new javax.swing.JButton();
        loggingCheckBox2 = new javax.swing.JCheckBox();
        jSeparator6 = new javax.swing.JSeparator();
        mapPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        keywordsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        markersMenu = new javax.swing.JMenu();
        setMarkerButton = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        loadFileMarkersButton = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        removeAllMarkersButton = new javax.swing.JMenuItem();
        removeTwitterMarkersButton = new javax.swing.JMenuItem();
        removeUserMarkersButton = new javax.swing.JMenuItem();
        removeFileMarkersButton = new javax.swing.JMenuItem();
        runMenu = new javax.swing.JMenu();
        startStopButton1 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        loggingCheckBox1 = new javax.swing.JCheckBoxMenuItem();

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Normal text file (*.txt)", "txt"));

        keywordsDialog.setTitle("Twitter Keywords");
        keywordsDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/res/keyboard_24.png")).getImage());
        keywordsDialog.setResizable(false);

        loadedKeywordLabelScroller.setViewportBorder(javax.swing.BorderFactory.createTitledBorder("Loaded Keywords"));
        loadedKeywordLabelScroller.getVerticalScrollBar().setUnitIncrement(16);

        loadedKeywordsLabel.setText("<html><ul></ul>");
        loadedKeywordsLabel.setMinimumSize(new java.awt.Dimension(256, 128));
        loadedKeywordsLabel.setPreferredSize(new java.awt.Dimension(256, 128));
        loadedKeywordLabelScroller.setViewportView(loadedKeywordsLabel);

        keywordsDialog.getContentPane().add(loadedKeywordLabelScroller, java.awt.BorderLayout.CENTER);

        clearAllKeywordsButton.setText("Clear all keywords");
        clearAllKeywordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllKeywordsButtonActionPerformed(evt);
            }
        });
        keywordsDialog.getContentPane().add(clearAllKeywordsButton, java.awt.BorderLayout.PAGE_END);

        keywordsDialog.pack();

        setMarkerDialog.setTitle("Set Marker");
        setMarkerDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/res/marker_24.png")).getImage());
        setMarkerDialog.setResizable(false);

        enterLatitudeLabel.setText("Enter Latitude:");

        enterLongitudeLabel.setText("Enter Longitude:");

        latTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000000"))));
        latTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latTextFieldActionPerformed(evt);
            }
        });

        longTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000000"))));

        setUserMarkerButton.setText("Set");
        setUserMarkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setUserMarkerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout setMarkerDialogLayout = new javax.swing.GroupLayout(setMarkerDialog.getContentPane());
        setMarkerDialog.getContentPane().setLayout(setMarkerDialogLayout);
        setMarkerDialogLayout.setHorizontalGroup(
            setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setMarkerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(enterLongitudeLabel)
                    .addComponent(enterLatitudeLabel))
                .addGap(18, 18, 18)
                .addGroup(setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(setUserMarkerButton)
                    .addComponent(latTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(longTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        setMarkerDialogLayout.setVerticalGroup(
            setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setMarkerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterLatitudeLabel)
                    .addComponent(latTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(setMarkerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterLongitudeLabel)
                    .addComponent(longTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(setUserMarkerButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setMarkerDialog.pack();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Twitter Map");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getClass().getResource("/res/twitter_icon.png")).getImage());
        setMinimumSize(new java.awt.Dimension(400, 300));

        keywordPanel.setBackground(enterKeywordTextField.getBackground());
        keywordPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), null));
        keywordPanel.setPreferredSize(new java.awt.Dimension(205, 30));

        enterKeywordTextField.setToolTipText("<html>\nInput the keywords to search for in tweets here and hit ENTER.<br/>\nLoaded Keywords:<br/>\n");
        enterKeywordTextField.setBorder(null);
        enterKeywordTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterKeywordTextFieldActionPerformed(evt);
            }
        });

        clearButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        clearButton1.setToolTipText("clear field");
        clearButton1.setMaximumSize(new java.awt.Dimension(37, 37));
        clearButton1.setMinimumSize(new java.awt.Dimension(37, 37));
        clearButton1.setName(""); // NOI18N
        clearButton1.setPreferredSize(new java.awt.Dimension(37, 37));
        clearButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout keywordPanelLayout = new javax.swing.GroupLayout(keywordPanel);
        keywordPanel.setLayout(keywordPanelLayout);
        keywordPanelLayout.setHorizontalGroup(
            keywordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keywordPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(enterKeywordTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        keywordPanelLayout.setVerticalGroup(
            keywordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(enterKeywordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(clearButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        runtimePanel.setBackground(enterKeywordTextField.getBackground());
        runtimePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), null));
        runtimePanel.setToolTipText("");
        runtimePanel.setPreferredSize(new java.awt.Dimension(205, 30));

        enterRunTextField.setToolTipText("<html>\nEnter running time of twitter stream (in milliseconds). (0 = infinite) <br/>\n<b>DEFAULT: 0</b><br/>\nCurrent Running time: forever");
        enterRunTextField.setBorder(null);
        enterRunTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterRunTextFieldActionPerformed(evt);
            }
        });

        clearButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        clearButton2.setToolTipText("clear field");
        clearButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton2ActionPerformed(evt);
            }
        });

        javax.swing.SpinnerListModel timeUnits = new javax.swing.SpinnerListModel(new String[]{"min","s","ms"});
        timeSpinner.setModel(timeUnits);
        ((javax.swing.JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
        timeSpinner.setToolTipText("Select minute (min), seconds (s) or milliseconds (ms).");
        timeSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout runtimePanelLayout = new javax.swing.GroupLayout(runtimePanel);
        runtimePanel.setLayout(runtimePanelLayout);
        runtimePanelLayout.setHorizontalGroup(
            runtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(runtimePanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(enterRunTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        runtimePanelLayout.setVerticalGroup(
            runtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clearButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(runtimePanelLayout.createSequentialGroup()
                .addGroup(runtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enterRunTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        startStopButton2.setIcon(start);
        startStopButton2.setText("Start");
        startStopButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopButton2ActionPerformed(evt);
            }
        });

        loggingCheckBox2.setText("Enable Logging to file");
        loggingCheckBox2.setToolTipText("Output the file?");
        loggingCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggingCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(keywordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(258, 258, 258)
                .addComponent(loggingCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 533, Short.MAX_VALUE)
                .addComponent(startStopButton2)
                .addGap(4, 4, 4))
            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlPanelLayout.createSequentialGroup()
                    .addGap(212, 212, 212)
                    .addComponent(runtimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(754, Short.MAX_VALUE)))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(startStopButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loggingCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(keywordPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlPanelLayout.createSequentialGroup()
                    .addComponent(runtimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        mapPanel.setLayout(new java.awt.BorderLayout());

        fileMenu.setText("File");

        keywordsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/keyboard_16.png"))); // NOI18N
        keywordsMenuItem.setText("Keywords");
        keywordsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keywordsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(keywordsMenuItem);
        fileMenu.add(jSeparator1);

        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/1427240871_exit-to-app-128_16x16.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        markersMenu.setText("Markers");

        setMarkerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/marker_16.png"))); // NOI18N
        setMarkerButton.setText("Set a marker");
        setMarkerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setMarkerButtonActionPerformed(evt);
            }
        });
        markersMenu.add(setMarkerButton);
        markersMenu.add(jSeparator2);

        loadFileMarkersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/open_file_16.png"))); // NOI18N
        loadFileMarkersButton.setText("Load Markers from file");
        loadFileMarkersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFileMarkersButtonActionPerformed(evt);
            }
        });
        markersMenu.add(loadFileMarkersButton);
        markersMenu.add(jSeparator3);

        removeAllMarkersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        removeAllMarkersButton.setText("Remove all markers");
        removeAllMarkersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllMarkersButtonActionPerformed(evt);
            }
        });
        markersMenu.add(removeAllMarkersButton);

        removeTwitterMarkersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        removeTwitterMarkersButton.setText("Remove Twitter markers");
        removeTwitterMarkersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTwitterMarkersButtonActionPerformed(evt);
            }
        });
        markersMenu.add(removeTwitterMarkersButton);

        removeUserMarkersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        removeUserMarkersButton.setText("Remove User markers");
        removeUserMarkersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeUserMarkersButtonActionPerformed(evt);
            }
        });
        markersMenu.add(removeUserMarkersButton);

        removeFileMarkersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/cross_16.png"))); // NOI18N
        removeFileMarkersButton.setText("Remove File markers");
        removeFileMarkersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFileMarkersButtonActionPerformed(evt);
            }
        });
        markersMenu.add(removeFileMarkersButton);

        menuBar.add(markersMenu);

        runMenu.setText("Run");

        startStopButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/play_16.png"))); // NOI18N
        startStopButton1.setText("Start");
        startStopButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopButton1ActionPerformed(evt);
            }
        });
        runMenu.add(startStopButton1);
        runMenu.add(jSeparator4);

        loggingCheckBox1.setText("Enable Logging");
        loggingCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggingCheckBox1ActionPerformed(evt);
            }
        });
        runMenu.add(loggingCheckBox1);

        menuBar.add(runMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator6)
            .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void keywordsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keywordsMenuItemActionPerformed
        keywordsDialog.setVisible(true);
    }//GEN-LAST:event_keywordsMenuItemActionPerformed

    private void loadFileMarkersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFileMarkersButtonActionPerformed
        int retValue = fileChooser.showOpenDialog(null);
        if(retValue == JFileChooser.APPROVE_OPTION) {
            // Clear markers already loaded from a previous file.
            GoogleMaps.clearFileMarkers(browser);
            // Load the coordinates from the selected file.
            loadCoordinatesFromFile(fileChooser.getSelectedFile());
        }
    }//GEN-LAST:event_loadFileMarkersButtonActionPerformed

    private void removeTwitterMarkersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTwitterMarkersButtonActionPerformed
        // clear all twitter markers from the map.
        GoogleMaps.clearTwitterMarkers(browser);
    }//GEN-LAST:event_removeTwitterMarkersButtonActionPerformed

    private void removeAllMarkersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllMarkersButtonActionPerformed
        // clear all markers from the map.
        GoogleMaps.clearAllMarkers(browser);
    }//GEN-LAST:event_removeAllMarkersButtonActionPerformed

    private void removeFileMarkersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFileMarkersButtonActionPerformed
        // clear all markers loaded from a file from the map.
        GoogleMaps.clearFileMarkers(browser);
    }//GEN-LAST:event_removeFileMarkersButtonActionPerformed

    private void removeUserMarkersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeUserMarkersButtonActionPerformed
        // clear all user defined markers from the map.
        GoogleMaps.clearUserMarkers(browser);
    }//GEN-LAST:event_removeUserMarkersButtonActionPerformed

    private void startStopButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStopButton1ActionPerformed
        // Start/Stop the twitter stream.
        switch (startStopButton1.getText()) {
            case "Stop":
                bListener.stopTwitter();
                break;
            case "Start":
                boolean reachable = false;
                try {
                    reachable = InetAddress.getByName("http://www.google.com").isReachable(500);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(!reachable) {
                    JOptionPane.showMessageDialog(null, "No Internet Conenction.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                bListener.startTwitter();
                break;
        }
    }//GEN-LAST:event_startStopButton1ActionPerformed

    private void setMarkerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setMarkerButtonActionPerformed
        setMarkerDialog.setVisible(true);
    }//GEN-LAST:event_setMarkerButtonActionPerformed

    private void enterRunTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterRunTextFieldActionPerformed
        double l;
        try{
            l = Double.parseDouble(enterRunTextField.getText());
        } catch(NumberFormatException e) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Please enter a positive number.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(l < 0) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Negative input not allowed. Please enter a positive number.", 
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        bListener.setRunningTime(l*timeScale);
        enterRunTextField.setText("");
        String s = enterRunTextField.getToolTipText();
        s = s.substring(0, s.lastIndexOf(":")+2);
        enterRunTextField.setToolTipText(s+(l == 0 ? "forever" : l*timeScale/1000d+"s"));
    }//GEN-LAST:event_enterRunTextFieldActionPerformed

    private void clearButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton2ActionPerformed
        enterRunTextField.setText("");
    }//GEN-LAST:event_clearButton2ActionPerformed

    private void startStopButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStopButton2ActionPerformed
        // Start/Stop the twitter stream.
        switch (startStopButton2.getText()) {
            case "Stop":
                bListener.stopTwitter();
                break;
            case "Start":
                bListener.startTwitter();
                break;
        }
    }//GEN-LAST:event_startStopButton2ActionPerformed

    private void loggingCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggingCheckBox2ActionPerformed
        boolean checked = loggingCheckBox2.isSelected();
        // enable/disable logging to output file.
        bListener.setLogging(checked);
        // set the same selected value for the same checkbox in the Run menu.
        loggingCheckBox1.setSelected(checked);
    }//GEN-LAST:event_loggingCheckBox2ActionPerformed

    private void loggingCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggingCheckBox1ActionPerformed
        boolean checked = loggingCheckBox1.isSelected();
        // enable/disable logging to output file.
        bListener.setLogging(checked);
        // set the same selected value for the same checkbox in the control panel.
        loggingCheckBox2.setSelected(checked);
    }//GEN-LAST:event_loggingCheckBox1ActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // Exit the program.
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void clearButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton1ActionPerformed
        // Clear the keywords text field.
        enterKeywordTextField.setText("");
    }//GEN-LAST:event_clearButton1ActionPerformed

    private void clearAllKeywordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllKeywordsButtonActionPerformed
        bListener.clearAllKeywords();
        loadedKeywordsLabel.setText("<html><ul></ul>");
        enterKeywordTextField.setToolTipText("<html>\n" +
            "Input the keywords to search for in tweets here and hit ENTER.<br/>\n" +
            "Loaded Keywords:<br/>\n");
    }//GEN-LAST:event_clearAllKeywordsButtonActionPerformed

    private void setUserMarkerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setUserMarkerButtonActionPerformed
        try {
            latTextField.commitEdit();
            longTextField.commitEdit();
        } catch (ParseException ex) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Please only enter a number", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(latTextField.getValue());
        String latitude = latTextField.getValue().toString();
        String longitude = longTextField.getValue().toString();
        setMarker(browser, latitude, longitude, "User defined Marker", "user");
        longTextField.setText("");
        latTextField.setText("");
    }//GEN-LAST:event_setUserMarkerButtonActionPerformed

    private void enterKeywordTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterKeywordTextFieldActionPerformed
        String s = loadedKeywordsLabel.getText();
        String t = enterKeywordTextField.getToolTipText();
        s = s.substring(0,s.length()-5) + "<li><b>" + enterKeywordTextField.getText()+"</b></ul>";
        t = t.substring(0, t.lastIndexOf(":")+7) + s.substring(6,s.length());
        bListener.addKeyword(enterKeywordTextField.getText());
        loadedKeywordsLabel.setText(s);
        enterKeywordTextField.setText("");
        enterKeywordTextField.setToolTipText(t);
    }//GEN-LAST:event_enterKeywordTextFieldActionPerformed

    private void latTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_latTextFieldActionPerformed

    private void timeSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeSpinnerStateChanged
        switch((String)timeSpinner.getValue()) {
            case "min" :
                timeScale = 60000;
                break;
            case "s" :
                timeScale = 1000;
                break;
            case "ms" :
                timeScale = 1;
                break;
        }
    }//GEN-LAST:event_timeSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearAllKeywordsButton;
    private javax.swing.JButton clearButton1;
    private javax.swing.JButton clearButton2;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTextField enterKeywordTextField;
    private javax.swing.JLabel enterLatitudeLabel;
    private javax.swing.JLabel enterLongitudeLabel;
    private javax.swing.JTextField enterRunTextField;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel keywordPanel;
    private javax.swing.JDialog keywordsDialog;
    private javax.swing.JMenuItem keywordsMenuItem;
    private javax.swing.JFormattedTextField latTextField;
    private javax.swing.JMenuItem loadFileMarkersButton;
    private javax.swing.JScrollPane loadedKeywordLabelScroller;
    private javax.swing.JLabel loadedKeywordsLabel;
    private javax.swing.JCheckBoxMenuItem loggingCheckBox1;
    private javax.swing.JCheckBox loggingCheckBox2;
    private javax.swing.JFormattedTextField longTextField;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JMenu markersMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem removeAllMarkersButton;
    private javax.swing.JMenuItem removeFileMarkersButton;
    private javax.swing.JMenuItem removeTwitterMarkersButton;
    private javax.swing.JMenuItem removeUserMarkersButton;
    private javax.swing.JMenu runMenu;
    private javax.swing.JPanel runtimePanel;
    private javax.swing.JMenuItem setMarkerButton;
    private javax.swing.JDialog setMarkerDialog;
    private javax.swing.JButton setUserMarkerButton;
    private javax.swing.JMenuItem startStopButton1;
    private javax.swing.JButton startStopButton2;
    private javax.swing.JSpinner timeSpinner;
    // End of variables declaration//GEN-END:variables

    /**
     * GUI the coordinates of the new tweet on the map.
     * 
     * @param coordinates coordinates of the location from where the tweet
     * was sent.
     * @param title the tweet message
     */
    @Override
    public void newTweet(String coordinates, String title) {
        int commaIndex = coordinates.indexOf(",");
        String lat = coordinates.substring(1, commaIndex);
        String lon = coordinates.substring(commaIndex+1, coordinates.length()-1);
        setMarker(browser, lat, lon, title, "tweet");
    }

    /**
     * Enables, disables and modifies certain UI elements based on the running 
     * status of the twitter stream.
     * 
     * @param isRunning true if the twitter stream is running otherwise false.
     */
    @Override
    public void setRunningStatus(boolean isRunning) {
        isRunning = !isRunning;
        if(isRunning) {
            startStopButton1.setIcon(stop);
            startStopButton1.setText("Stop");
            startStopButton2.setIcon(stop);
            startStopButton2.setText("Stop");
            enterKeywordTextField.setEnabled(false);
            clearAllKeywordsButton.setEnabled(false);
            removeTwitterMarkersButton.setEnabled(false);
            removeAllMarkersButton.setEnabled(false);
            loggingCheckBox1.setEnabled(false);
            loggingCheckBox2.setEnabled(false);
            enterRunTextField.setEnabled(false);
        }
        else {
            startStopButton1.setIcon(start);
            startStopButton1.setText("Start");
            startStopButton2.setIcon(start);
            startStopButton2.setText("Start");
            enterKeywordTextField.setEnabled(true);
            clearAllKeywordsButton.setEnabled(true);
            removeTwitterMarkersButton.setEnabled(true);
            removeAllMarkersButton.setEnabled(true);
            loggingCheckBox1.setEnabled(true);
            loggingCheckBox2.setEnabled(true);
            enterRunTextField.setEnabled(true);
        }
    }

    /**
     * Once logging is completed, we ask the user to select a location to save 
     * the file in. Once user clicks save, it will automatically save the file
     * to the user selected directory. If the user cancels the save operation,
     * the program, will ask him to confirm his choice. If the user confirms,
     * it will delete the temporary file otherwise it will again ask him to 
     * select a save directory.
     * 
     * @param f the file to save.
     */
    @Override
    public void loggingCompleted(File f) {
        boolean confirmed;
        do{
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retValue = fileChooser.showSaveDialog(null);
            if(retValue == JFileChooser.APPROVE_OPTION) {
                f.renameTo(new File(fileChooser.getSelectedFile()+"\\"+f.getName()));
                confirmed = true;
            } else {
                JDialog.setDefaultLookAndFeelDecorated(true);
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you"
                        + " don't want to save the file?", "Confirm", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                confirmed = choice == JOptionPane.YES_OPTION;
                if(confirmed)
                    f.delete();
            }
        }while(!confirmed);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }
    
    /**
     * Load coordinates from the given file.
     * 
     * @param f file containing coordinates to load.
     */
    public void loadCoordinatesFromFile(File f) {
        
        // Load the file
        new Thread(new Runnable() {
            @Override
            public void run() {
                final BufferedReader in;
                try {
                    in = new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException ex) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JOptionPane.showMessageDialog(null, "Oops! No such file exists.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Read and set markers.
                String coordinate;
                try {
                    while(((coordinate = in.readLine()) != null)) {
                        int commaIndex = coordinate.indexOf(",");
                        String lat = coordinate.substring(0, commaIndex);
                        String lon = coordinate.substring(commaIndex+1, coordinate.length());
                        setMarker(browser, lat, lon, "Marker loaded from "+f.getName(), "file");
                    }
                } catch (IOException ex) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JOptionPane.showMessageDialog(null, "Cannot read file.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    JOptionPane.showMessageDialog(null, "Unknown error occured. "
                            + "Possibly loaded an incorrectly parsed file.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try { in.close(); } catch (IOException ignore) { }
                }
                
            }
        }).start();
        
    }
}
