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
import java.awt.AWTException;
import utils.GoogleMaps;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import twitterstream.TweetListener;
import utils.HintTextField;
import utils.MySQL4j;
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
    private final BrowserView browserView;
    private final GUIListener guiListener;
    
    /** GUI frame width and height. */
    private final int MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT;
    
    /** Image icons for the start/stop button. */
    private ImageIcon start, stop;
    
    /** time scale unit for setting running time. */
    private long timeScale;
    
    /** System tray icon stuff. */
    private final PopupMenu popup;
    private final TrayIcon trayIcon;
    
    /**
     * Creates new form Map
     * @param bl browser listener object
     */
    public GUI(GUIListener bl) {
        // Load certain necessary resources.
        map = getClass().getResource("/res/map.html").toString();
        start = createImageIcon("play_16.png");
        stop = createImageIcon("stop_16.png");
        MIN_FRAME_WIDTH = 960;
        MIN_FRAME_HEIGHT = 540;
        popup = new PopupMenu();
        trayIcon = new TrayIcon(createImageIcon("map_16.png").getImage());
        languageCodes.put("English", "en");
        languageCodes.put("Dutch", "nl");
        languageCodes.put("German", "de");
        languageCodes.put("French", "fr");
        languageCodes.put("Spanish", "es");
        languageCodes.put("Italian", "it");
        languageCodes.put("Russian", "ru");
        languageCodes.put("Lithuanian", "lt");
        languageCodes.put("Hindi", "hi");
        languageCodes.put("Tamil", "ta");
        languageCodes.put("Arabic", "ar");
        languageCodes.put("Chinese", "zh");
        languageCodes.put("Japanese", "ja");
        languageCodes.put("Korean", "ko");
        languageCodes.put("Vietnamese", "vi");


        // Create a browser, its associated UI view object and the browser listener.
        browser = new Browser();
        browserView = new BrowserView(browser);
        guiListener = bl;
        browser.addLoadListener(new LoadListener() {

            @Override
            public void onStartLoadingFrame(StartLoadingEvent sle) {}

            @Override
            public void onProvisionalLoadingFrame(ProvisionalLoadingEvent ple) {}

            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent fle) {
                mapPanel.remove(loadingErrorPanel);
                mapPanel.add(browserView, BorderLayout.CENTER);
                revalidate();
                repaint();
            }

            @Override
            public void onFailLoadingFrame(FailLoadingEvent fle) {
                //if (fle.isMainFrame()) {
                    guiListener.onBrowserLoadFailed();
                    mapPanel.remove(browserView);
                    mapPanel.add(loadingErrorPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                //}
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
        browser.loadURL(map);
        
        // Adding support for minimizing window to system tray if supported.
        if (SystemTray.isSupported()) {
            systrayCheckBox.setEnabled(true);
            systrayCheckBox.setToolTipText("Enable/Disable minimizing to system tray.");
            
            // Context menu items to system tray icon.
            final MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener((ActionEvent e) -> {
                GUI.this.exitMenuItemActionPerformed(e);
            });
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);
            trayIcon.addActionListener((ActionEvent e) -> {
                GUI.this.setVisible(true);
                GUI.this.setExtendedState(GUI.NORMAL);
                SystemTray.getSystemTray().remove(trayIcon);
            });
        } else {
            systrayCheckBox.setEnabled(false);
            systrayCheckBox.setToolTipText("OS does not support this function.");
        }
        
        // Center the frame to the screen
        setLocationRelativeTo(null);
        
        // Display the keywords dialog at start
        keywordsDialog.setVisible(true);
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
        removeKeywordsButton = new javax.swing.JButton();
        clearAllKeywordsButton = new javax.swing.JButton();
        loadedKeywordsPanel = new javax.swing.JPanel();
        loadedKeywordsScrollPane = new javax.swing.JScrollPane();
        loadedKeywordsList = new javax.swing.JList();
        langSelectionComboBox = new javax.swing.JComboBox();
        addLangButton = new javax.swing.JButton();
        selectLangPanel = new javax.swing.JPanel();
        selectedLangScrollPane = new javax.swing.JScrollPane();
        selectedLangTable = new javax.swing.JTable();
        removeLangButton = new javax.swing.JButton();
        setMarkerDialog = new javax.swing.JDialog();
        enterLatitudeLabel = new javax.swing.JLabel();
        enterLongitudeLabel = new javax.swing.JLabel();
        latTextField = new javax.swing.JFormattedTextField();
        longTextField = new javax.swing.JFormattedTextField();
        setUserMarkerButton = new javax.swing.JButton();
        loadingErrorPanel = new javax.swing.JPanel();
        tryAgainButton = new javax.swing.JButton();
        no_internet_icon_label = new javax.swing.JLabel();
        twitterKeysInputDialog = new javax.swing.JDialog();
        consumerKeyLabel = new javax.swing.JLabel();
        consumerKeyTextField = new javax.swing.JTextField();
        consumerSecretLabel = new javax.swing.JLabel();
        consumerSecretTextField = new javax.swing.JTextField();
        apiKeyTextField = new javax.swing.JTextField();
        apiKeyLabel = new javax.swing.JLabel();
        apiSecretLabel = new javax.swing.JLabel();
        apiSecretTextField = new javax.swing.JTextField();
        keysControlPanel = new javax.swing.JPanel();
        applyKeysButton = new javax.swing.JButton();
        clearAllKeysButton = new javax.swing.JButton();
        OkKeysButton = new javax.swing.JButton();
        databaseKeysInputDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        sqlUserTextField = new javax.swing.JTextField();
        sqlLinkTextField = new javax.swing.JTextField();
        sqlButtonsPanel = new javax.swing.JPanel();
        sqlApplyButton = new javax.swing.JButton();
        sqlCancelButton = new javax.swing.JButton();
        connectingLabel = new javax.swing.JLabel();
        sqlPasswordField = new javax.swing.JPasswordField();
        controlPanel = new javax.swing.JPanel();
        keywordPanel = new javax.swing.JPanel();
        enterKeywordTextField = new HintTextField("Enter Keyword here...");
        clearButton1 = new javax.swing.JButton();
        runtimePanel = new javax.swing.JPanel();
        enterRunTextField = new HintTextField("Enter Running time here...");
        clearButton2 = new javax.swing.JButton();
        timeSpinner = new javax.swing.JSpinner();
        displayMapButton = new javax.swing.JButton();
        startStopButton2 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        mapPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        keywordsMenuItem = new javax.swing.JMenuItem();
        twitterKeysMenuItem = new javax.swing.JMenuItem();
        databaseKeysMenuItem = new javax.swing.JMenuItem();
        systrayCheckBox = new javax.swing.JCheckBoxMenuItem();
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

        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Normal text file (*.txt)", "txt"));

        keywordsDialog.setTitle("Twitter Keywords");
        keywordsDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/res/keyboard_24.png")).getImage());
        keywordsDialog.setResizable(false);

        removeKeywordsButton.setText("Remove Keyword(s)");
        removeKeywordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeKeywordsButtonActionPerformed(evt);
            }
        });

        clearAllKeywordsButton.setText("Clear all keywords");
        clearAllKeywordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllKeywordsButtonActionPerformed(evt);
            }
        });

        loadedKeywordsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Loaded Keywords"));

        loadedKeywordsList.setModel(keywordsListModel);
        loadedKeywordsScrollPane.setViewportView(loadedKeywordsList);

        javax.swing.GroupLayout loadedKeywordsPanelLayout = new javax.swing.GroupLayout(loadedKeywordsPanel);
        loadedKeywordsPanel.setLayout(loadedKeywordsPanelLayout);
        loadedKeywordsPanelLayout.setHorizontalGroup(
            loadedKeywordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadedKeywordsScrollPane)
        );
        loadedKeywordsPanelLayout.setVerticalGroup(
            loadedKeywordsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadedKeywordsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );

        langSelectionComboBox.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "English", "Dutch", "German", "French", "Spanish", "Italian", "Lithuanian", "Russian", "Hindi", "Tamil", "Chinese", "Japanese", "Korean", "Vietnamese", "Arabic" }));

        addLangButton.setText("Add");
        addLangButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLangButtonActionPerformed(evt);
            }
        });

        selectLangPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Translation"));

        selectedLangTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Language", "Code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        selectedLangTable.setColumnSelectionAllowed(true);
        selectedLangTable.getTableHeader().setReorderingAllowed(false);
        selectedLangScrollPane.setViewportView(selectedLangTable);
        selectedLangTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (selectedLangTable.getColumnModel().getColumnCount() > 0) {
            selectedLangTable.getColumnModel().getColumn(0).setResizable(false);
            selectedLangTable.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout selectLangPanelLayout = new javax.swing.GroupLayout(selectLangPanel);
        selectLangPanel.setLayout(selectLangPanelLayout);
        selectLangPanelLayout.setHorizontalGroup(
            selectLangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(selectLangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectLangPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectedLangScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(184, 184, 184)))
        );
        selectLangPanelLayout.setVerticalGroup(
            selectLangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 159, Short.MAX_VALUE)
            .addGroup(selectLangPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectLangPanelLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectedLangScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        removeLangButton.setText("Clear");
        removeLangButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeLangButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout keywordsDialogLayout = new javax.swing.GroupLayout(keywordsDialog.getContentPane());
        keywordsDialog.getContentPane().setLayout(keywordsDialogLayout);
        keywordsDialogLayout.setHorizontalGroup(
            keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keywordsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selectLangPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, keywordsDialogLayout.createSequentialGroup()
                        .addComponent(removeKeywordsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearAllKeywordsButton))
                    .addGroup(keywordsDialogLayout.createSequentialGroup()
                        .addComponent(langSelectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addLangButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeLangButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(loadedKeywordsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        keywordsDialogLayout.setVerticalGroup(
            keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, keywordsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadedKeywordsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(removeKeywordsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearAllKeywordsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectLangPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(keywordsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addLangButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeLangButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(langSelectionComboBox))
                .addContainerGap())
        );

        keywordsDialog.pack();
        keywordsDialog.setLocationRelativeTo(this);

        setMarkerDialog.setTitle("Set Marker");
        setMarkerDialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/res/marker_24.png")).getImage());
        setMarkerDialog.setResizable(false);

        enterLatitudeLabel.setText("Enter Latitude:");

        enterLongitudeLabel.setText("Enter Longitude:");

        latTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000000"))));

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
        setMarkerDialog.setLocationRelativeTo(this);

        loadingErrorPanel.setPreferredSize(new java.awt.Dimension(1280, 650));

        tryAgainButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tryAgainButton.setText("Try Again");
        tryAgainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tryAgainButtonActionPerformed(evt);
            }
        });

        no_internet_icon_label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/no_internet.png"))); // NOI18N

        javax.swing.GroupLayout loadingErrorPanelLayout = new javax.swing.GroupLayout(loadingErrorPanel);
        loadingErrorPanel.setLayout(loadingErrorPanelLayout);
        loadingErrorPanelLayout.setHorizontalGroup(
            loadingErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingErrorPanelLayout.createSequentialGroup()
                .addGap(570, 570, 570)
                .addComponent(tryAgainButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingErrorPanelLayout.createSequentialGroup()
                .addContainerGap(395, Short.MAX_VALUE)
                .addComponent(no_internet_icon_label)
                .addGap(373, 373, 373))
        );
        loadingErrorPanelLayout.setVerticalGroup(
            loadingErrorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loadingErrorPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(no_internet_icon_label)
                .addGap(42, 42, 42)
                .addComponent(tryAgainButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95))
        );

        twitterKeysInputDialog.setTitle("Enter twitter credentials");
        twitterKeysInputDialog.setResizable(false);
        twitterKeysInputDialog.setType(java.awt.Window.Type.POPUP);

        consumerKeyLabel.setText("Enter your twitter Consumer key:");

        consumerKeyTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        consumerSecretLabel.setText("Enter your twitter Consumer secret:");

        apiKeyLabel.setText("Enter your twitter API key:");

        apiSecretLabel.setText("Enter your twitter API secret:");

        applyKeysButton.setText("Apply");
        applyKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyKeysButtonActionPerformed(evt);
            }
        });
        keysControlPanel.add(applyKeysButton);

        clearAllKeysButton.setText("Clear All");
        clearAllKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllKeysButtonActionPerformed(evt);
            }
        });
        keysControlPanel.add(clearAllKeysButton);

        OkKeysButton.setText("Ok");
        OkKeysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkKeysButtonActionPerformed(evt);
            }
        });
        keysControlPanel.add(OkKeysButton);

        javax.swing.GroupLayout twitterKeysInputDialogLayout = new javax.swing.GroupLayout(twitterKeysInputDialog.getContentPane());
        twitterKeysInputDialog.getContentPane().setLayout(twitterKeysInputDialogLayout);
        twitterKeysInputDialogLayout.setHorizontalGroup(
            twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(consumerSecretLabel)
                                    .addComponent(consumerKeyLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(consumerKeyTextField)
                                    .addComponent(consumerSecretTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)))
                            .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(apiKeyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apiKeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))))
                    .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(apiSecretLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apiSecretTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                    .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(keysControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        twitterKeysInputDialogLayout.setVerticalGroup(
            twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(twitterKeysInputDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consumerKeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(consumerKeyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consumerSecretLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(consumerSecretTextField))
                .addGap(18, 18, 18)
                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apiKeyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(apiKeyTextField))
                .addGap(18, 18, 18)
                .addGroup(twitterKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apiSecretLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(apiSecretTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keysControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        twitterKeysInputDialog.pack();
        twitterKeysInputDialog.setLocationRelativeTo(this);

        jLabel1.setText("Username:");

        jLabel2.setText("Password:");

        jLabel3.setText("URL link:");

        sqlApplyButton.setText("Apply");
        sqlApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlApplyButtonActionPerformed(evt);
            }
        });
        sqlButtonsPanel.add(sqlApplyButton);

        sqlCancelButton.setText("OK/Cancel");
        sqlCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sqlCancelButtonActionPerformed(evt);
            }
        });
        sqlButtonsPanel.add(sqlCancelButton);

        connectingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/loading_spinner_24.gif"))); // NOI18N
        sqlButtonsPanel.add(connectingLabel);
        connectingLabel.setVisible(false);

        sqlPasswordField.setText("jPasswordField1");

        javax.swing.GroupLayout databaseKeysInputDialogLayout = new javax.swing.GroupLayout(databaseKeysInputDialog.getContentPane());
        databaseKeysInputDialog.getContentPane().setLayout(databaseKeysInputDialogLayout);
        databaseKeysInputDialogLayout.setHorizontalGroup(
            databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseKeysInputDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sqlButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(databaseKeysInputDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sqlUserTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(sqlLinkTextField)
                            .addComponent(sqlPasswordField))))
                .addContainerGap())
        );
        databaseKeysInputDialogLayout.setVerticalGroup(
            databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(databaseKeysInputDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqlUserTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(sqlPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(databaseKeysInputDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqlLinkTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sqlButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        databaseKeysInputDialog.pack();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Twitter Map");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(new ImageIcon(getClass().getResource("/res/twitter_icon.png")).getImage());
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setName("TwitterMap"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        controlPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        controlPanel.setMinimumSize(new java.awt.Dimension(563, 30));
        controlPanel.setPreferredSize(new java.awt.Dimension(563, 30));

        keywordPanel.setBackground(enterKeywordTextField.getBackground());
        keywordPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 102), null));
        keywordPanel.setPreferredSize(new java.awt.Dimension(205, 30));

        enterKeywordTextField.setToolTipText("<html>\nInput the keywords to search for in tweets here and hit ENTER. Each keyword <br>\nentered will be strung together using commas. You can think of commas as <br>\nlogical ORs, while spaces within keywords are equivalent to logical ANDs (e.g.<br>\n ‘the twitter’ is the AND twitter, and ‘the,twitter’ is the OR twitter). <br>");
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
                .addComponent(enterKeywordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
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
                .addComponent(enterRunTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(clearButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        runtimePanelLayout.setVerticalGroup(
            runtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clearButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(runtimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(enterRunTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(timeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        displayMapButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/map_24.png"))); // NOI18N
        displayMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayMapButtonActionPerformed(evt);
            }
        });

        startStopButton2.setIcon(start);
        startStopButton2.setText("Start");
        startStopButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStopButtonPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(keywordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runtimePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayMapButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 710, Short.MAX_VALUE)
                .addComponent(startStopButton2)
                .addGap(0, 0, 0))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(runtimePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(keywordPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(displayMapButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(startStopButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mapPanel.setLayout(new java.awt.BorderLayout());

        menuBar.setMinimumSize(new java.awt.Dimension(0, 26));

        fileMenu.setText("File");

        keywordsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/keyboard_16.png"))); // NOI18N
        keywordsMenuItem.setText("Keywords");
        keywordsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keywordsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(keywordsMenuItem);

        twitterKeysMenuItem.setText("Edit twitter credentials");
        twitterKeysMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                twitterKeysMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(twitterKeysMenuItem);

        databaseKeysMenuItem.setText("Edit MySQL database");
        databaseKeysMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databaseKeysMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(databaseKeysMenuItem);

        systrayCheckBox.setSelected(true);
        systrayCheckBox.setText("Minimize to system tray");
        systrayCheckBox.setToolTipText("Enable/Disable minimizing to system tray.");
        fileMenu.add(systrayCheckBox);
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
                startStopButtonPerformed(evt);
            }
        });
        runMenu.add(startStopButton1);

        menuBar.add(runMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator6)
            .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(controlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1266, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mapPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addGap(0, 0, 0))
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
        guiListener.setRunningTime((long)l*timeScale);
        enterRunTextField.setText("");
        String s = enterRunTextField.getToolTipText();
        s = s.substring(0, s.lastIndexOf(":")+2);
        enterRunTextField.setToolTipText(s+(l == 0 ? "forever" : l*timeScale/1000d+"s"));
    }//GEN-LAST:event_enterRunTextFieldActionPerformed

    private void clearButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton2ActionPerformed
        enterRunTextField.setText("");
    }//GEN-LAST:event_clearButton2ActionPerformed

    private void clearButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton1ActionPerformed
        // Clear the keywords text field.
        enterKeywordTextField.setText("");
    }//GEN-LAST:event_clearButton1ActionPerformed

    private void clearAllKeywordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllKeywordsButtonActionPerformed
        for(int i = keywordsListModel.getSize()-1; i >=0; i--) {
            guiListener.removeKeyword(keywordsListModel.remove(i));
        }
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
        GoogleMaps.setMarker(browser, latitude, longitude, "User defined Marker", "user");
        longTextField.setText("");
        latTextField.setText("");
    }//GEN-LAST:event_setUserMarkerButtonActionPerformed

    private void enterKeywordTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterKeywordTextFieldActionPerformed
        String keyword = enterKeywordTextField.getText();
        guiListener.addKeyword(keyword);
        keywordsListModel.addElement(keyword);
        enterKeywordTextField.setText("");
    }//GEN-LAST:event_enterKeywordTextFieldActionPerformed

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

    private void removeKeywordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeKeywordsButtonActionPerformed
        int[] selectedIndices = loadedKeywordsList.getSelectedIndices();
        for(int i = selectedIndices.length-1; i >= 0; i--) {
            guiListener.removeKeyword(keywordsListModel.remove(selectedIndices[i]));
        }
    }//GEN-LAST:event_removeKeywordsButtonActionPerformed

    private void tryAgainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tryAgainButtonActionPerformed
        browser.loadURL(map);
    }//GEN-LAST:event_tryAgainButtonActionPerformed

    private void displayMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayMapButtonActionPerformed
        boolean isVisible = mapPanel.isVisible();
        setSize(0, 0);
        setMinimumSize(null);
        mapPanel.setVisible(isVisible = !isVisible);
        revalidate();
        repaint();
        pack();
        if(isVisible) {
            setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        } else {
            System.out.println(controlPanel.getMinimumSize().height + menuBar.getMinimumSize().height);
            setMinimumSize(new Dimension(controlPanel.getMinimumSize().width, 
                    controlPanel.getMinimumSize().height + menuBar.getMinimumSize().height));
        }
        setResizable(isVisible);
    }//GEN-LAST:event_displayMapButtonActionPerformed

    private void startStopButtonPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStopButtonPerformed
        // Start/Stop the twitter stream.
        switch (startStopButton1.getText()) {
            case "Stop":
                guiListener.stopTwitterStream();
                break;
            case "Start":
                //if(isConnected())
                guiListener.translate(selectedLanguages.values().toArray(new String[0]));
                guiListener.startTwitterStream();
                break;
        } 
    }//GEN-LAST:event_startStopButtonPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // Shut down the connection to twitter.
        guiListener.stopTwitterStream();

        // Exit the program.
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        if(systrayCheckBox.isSelected()) {
            // minimize the window
            this.setVisible(false);
            
            // add tray icon
            try {
                SystemTray.getSystemTray().add(trayIcon);
                trayIcon.displayMessage("Attention", "TwitterMap has been minimized to the system tray. ", 
                        TrayIcon.MessageType.INFO);
            } catch (AWTException ex) {
                JDialog.setDefaultLookAndFeelDecorated(true);
                JOptionPane.showMessageDialog(null, "Oops! Something went wrong. "
                        + "Could not minimize to system tray.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_formWindowIconified

    private void addLangButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLangButtonActionPerformed
        String selected = (String)langSelectionComboBox.getSelectedItem();
        if(!selectedLanguages.containsKey(selected)) {
            String[] sel = new String[] {selected, languageCodes.get(selected)};
            selectedLanguages.put(sel[0], sel[1]);
            ((DefaultTableModel)selectedLangTable.getModel()).addRow(sel);
        }
    }//GEN-LAST:event_addLangButtonActionPerformed

    private void removeLangButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeLangButtonActionPerformed
        int[] selectedIndices = selectedLangTable.getSelectedRows();
        for(int i = selectedIndices.length-1; i >= 0; i--) {
            selectedLanguages.remove(selectedLangTable.getValueAt(selectedIndices[i], 0));
            ((DefaultTableModel)selectedLangTable.getModel()).removeRow(selectedIndices[i]);
        }
    }//GEN-LAST:event_removeLangButtonActionPerformed

    private void applyKeysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyKeysButtonActionPerformed
        String cKey = consumerKeyTextField.getText();
        String cSecret = consumerSecretTextField.getText();
        String apiKey = apiKeyTextField.getText();
        String apiSecret = apiSecretTextField.getText();
        
        if(cKey.equals("")||cKey.equals("\t")
                || cSecret.equals("")||cSecret.equals("\t")
                || apiKey.equals("")||apiKey.equals("\t")
                || apiSecret.equals("")||apiSecret.equals("\t")) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.", 
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(cKey.matches("^[a-zA-Z0-9]+$")
                && cSecret.matches("^[a-zA-Z0-9]+$")
                && apiKey.matches("^[a-zA-Z0-9]+\\-[a-zA-Z0-9]+$")
                && apiSecret.matches("^[a-zA-Z0-9]+$")) {
            guiListener.setTwitterCredentials(cKey, cSecret, apiKey, apiSecret);
        } else {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "1 or more keys/secrets uses invalid characters.", 
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_applyKeysButtonActionPerformed

    private void twitterKeysMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_twitterKeysMenuItemActionPerformed
        twitterKeysInputDialog.setVisible(true);
    }//GEN-LAST:event_twitterKeysMenuItemActionPerformed

    private void databaseKeysMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databaseKeysMenuItemActionPerformed
        sqlApplyButton.setEnabled(true);
        sqlCancelButton.setEnabled(true);
        databaseKeysInputDialog.setVisible(true);
    }//GEN-LAST:event_databaseKeysMenuItemActionPerformed

    private void sqlApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlApplyButtonActionPerformed
        String user = sqlUserTextField.getText();
        String pass = sqlPasswordField.getPassword().toString();
        String link = sqlLinkTextField.getText();
        if(user.equals("")||user.equals("\t")
                ||pass.equals("")||pass.equals("\t")
                ||link.equals("")||link.equals("\t")) {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.", 
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        MySQL4j test = new MySQL4j(user, pass, link);
        try {
            connectingLabel.setVisible(true);
            test.connect();
            sqlApplyButton.setEnabled(false);
            sqlCancelButton.setEnabled(false);
            connectingLabel.setVisible(false);
            test.close();
            
            // Pass the new database link to the twitter stream.
            guiListener.setMySQLDatabase(test);
        } catch (Exception ex) {
            connectingLabel.setVisible(false);
            JDialog.setDefaultLookAndFeelDecorated(true);
            JOptionPane.showMessageDialog(null, "Oops! Unable to connect to database. Make sure the "
                    + "username/password/url provided are correct and that the link is reachable.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_sqlApplyButtonActionPerformed

    private void sqlCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sqlCancelButtonActionPerformed
        databaseKeysInputDialog.setVisible(false);
    }//GEN-LAST:event_sqlCancelButtonActionPerformed

    private void clearAllKeysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllKeysButtonActionPerformed
        consumerKeyTextField.setText("");
        consumerSecretTextField.setText("");
        apiKeyTextField.setText("");
        apiSecretTextField.setText("");
    }//GEN-LAST:event_clearAllKeysButtonActionPerformed

    private void OkKeysButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkKeysButtonActionPerformed
        twitterKeysInputDialog.setVisible(false);
    }//GEN-LAST:event_OkKeysButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OkKeysButton;
    private javax.swing.JButton addLangButton;
    private javax.swing.JLabel apiKeyLabel;
    private javax.swing.JTextField apiKeyTextField;
    private javax.swing.JLabel apiSecretLabel;
    private javax.swing.JTextField apiSecretTextField;
    private javax.swing.JButton applyKeysButton;
    private javax.swing.JButton clearAllKeysButton;
    private javax.swing.JButton clearAllKeywordsButton;
    private javax.swing.JButton clearButton1;
    private javax.swing.JButton clearButton2;
    private javax.swing.JLabel connectingLabel;
    private javax.swing.JLabel consumerKeyLabel;
    private javax.swing.JTextField consumerKeyTextField;
    private javax.swing.JLabel consumerSecretLabel;
    private javax.swing.JTextField consumerSecretTextField;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JDialog databaseKeysInputDialog;
    private javax.swing.JMenuItem databaseKeysMenuItem;
    private javax.swing.JButton displayMapButton;
    private javax.swing.JTextField enterKeywordTextField;
    private javax.swing.JLabel enterLatitudeLabel;
    private javax.swing.JLabel enterLongitudeLabel;
    private javax.swing.JTextField enterRunTextField;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel keysControlPanel;
    private javax.swing.JPanel keywordPanel;
    private javax.swing.JDialog keywordsDialog;
    private javax.swing.JMenuItem keywordsMenuItem;
    private javax.swing.JComboBox langSelectionComboBox;
    private javax.swing.JFormattedTextField latTextField;
    private javax.swing.JMenuItem loadFileMarkersButton;
    private DefaultListModel<String> keywordsListModel = new DefaultListModel();
    private javax.swing.JList loadedKeywordsList;
    private javax.swing.JPanel loadedKeywordsPanel;
    private javax.swing.JScrollPane loadedKeywordsScrollPane;
    private javax.swing.JPanel loadingErrorPanel;
    private javax.swing.JFormattedTextField longTextField;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JMenu markersMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel no_internet_icon_label;
    private javax.swing.JMenuItem removeAllMarkersButton;
    private javax.swing.JMenuItem removeFileMarkersButton;
    private javax.swing.JButton removeKeywordsButton;
    private javax.swing.JButton removeLangButton;
    private javax.swing.JMenuItem removeTwitterMarkersButton;
    private javax.swing.JMenuItem removeUserMarkersButton;
    private javax.swing.JMenu runMenu;
    private javax.swing.JPanel runtimePanel;
    private javax.swing.JPanel selectLangPanel;
    private javax.swing.JScrollPane selectedLangScrollPane;
    private HashMap<String, String> selectedLanguages = new HashMap<>();
    private HashMap<String, String> languageCodes = new HashMap<>();
    private javax.swing.JTable selectedLangTable;
    private javax.swing.JMenuItem setMarkerButton;
    private javax.swing.JDialog setMarkerDialog;
    private javax.swing.JButton setUserMarkerButton;
    private javax.swing.JButton sqlApplyButton;
    private javax.swing.JPanel sqlButtonsPanel;
    private javax.swing.JButton sqlCancelButton;
    private javax.swing.JTextField sqlLinkTextField;
    private javax.swing.JPasswordField sqlPasswordField;
    private javax.swing.JTextField sqlUserTextField;
    private javax.swing.JMenuItem startStopButton1;
    private javax.swing.JButton startStopButton2;
    private javax.swing.JCheckBoxMenuItem systrayCheckBox;
    private javax.swing.JSpinner timeSpinner;
    private javax.swing.JButton tryAgainButton;
    private javax.swing.JDialog twitterKeysInputDialog;
    private javax.swing.JMenuItem twitterKeysMenuItem;
    // End of variables declaration//GEN-END:variables

    /**
     * Create a new map marker when a new tweet with Geo-location comes in.
     */
    @Override
    public void newTweet(String lat, String lon, String title) {
        //int commaIndex = coordinates.indexOf(",");
        //String lat = coordinates.substring(1, commaIndex);
        //String lon = coordinates.substring(commaIndex+1, coordinates.length()-1);
        GoogleMaps.setMarker(browser, lat, lon, title, "tweet");
    }

    /**
     * Enables, disables and modifies certain UI elements based on the running 
     * status of the twitter stream.
     */
    @Override
    public void setRunningStatus(boolean isRunning) {
        if(isRunning) {
            startStopButton1.setIcon(stop);
            startStopButton1.setText("Stop");
            startStopButton2.setIcon(stop);
            startStopButton2.setText("Stop");
            enterKeywordTextField.setEnabled(false);
            enterRunTextField.setEnabled(false);
            removeKeywordsButton.setEnabled(false);
            clearAllKeywordsButton.setEnabled(false);
            removeAllMarkersButton.setEnabled(false);
            removeTwitterMarkersButton.setEnabled(false);
            twitterKeysMenuItem.setEnabled(false);
            databaseKeysMenuItem.setEnabled(false);
        }
        else {
            startStopButton1.setIcon(start);
            startStopButton1.setText("Start");
            startStopButton2.setIcon(start);
            startStopButton2.setText("Start");
            enterKeywordTextField.setEnabled(true);
            enterRunTextField.setEnabled(true);
            removeKeywordsButton.setEnabled(true);
            clearAllKeywordsButton.setEnabled(true);
            removeAllMarkersButton.setEnabled(true);
            removeTwitterMarkersButton.setEnabled(true);
            twitterKeysMenuItem.setEnabled(true);
            databaseKeysMenuItem.setEnabled(true);
        }
    }

    /**
     * Once logging is completed, we ask the user to select a location to save 
     * the file in. Once user clicks save, it will automatically save the file
     * to the user selected directory. If the user cancels the save operation,
     * the program, will ask him to confirm his choice. If the user confirms,
     * it will delete the temporary file otherwise it will again ask him to 
     * select a save directory.
     */
    @Override
    public void loggingCompleted(File tweets_file, File users_file) {
        boolean confirmed;
        do{
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retValue = fileChooser.showSaveDialog(null);
            if(retValue == JFileChooser.APPROVE_OPTION) {
                tweets_file.renameTo(new File(fileChooser.getSelectedFile()+"\\"+tweets_file.getName()));
                users_file.renameTo(new File(fileChooser.getSelectedFile()+"\\"+users_file.getName()));
                confirmed = true;
            } else {
                JDialog.setDefaultLookAndFeelDecorated(true);
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you"
                        + " don't want to save the file?", "Confirm", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                confirmed = choice == JOptionPane.YES_OPTION;
                if(confirmed) {
                    tweets_file.delete();
                    users_file.delete();
                }
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
                        GoogleMaps.setMarker(browser, lat, lon, "Marker loaded from "+f.getName(), "file");
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
    
    /**
     * Checks if basic network connection is present and returns true if it does.
     * @return true if network connection exists, otherwise false.
     */
    public boolean isConnected() {
        boolean reachable = false;
        try {
            reachable = InetAddress.getByName("www.google.com").isReachable(500);
        } catch (UnknownHostException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!reachable) {
            JOptionPane.showMessageDialog(null, "No Internet Conenction.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return reachable;
    }
}
