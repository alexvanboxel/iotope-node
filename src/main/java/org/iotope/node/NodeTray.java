package org.iotope.node;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.iotope.node.conf.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

@Singleton
public class NodeTray {
    private static Logger Log = LoggerFactory.getLogger(NodeTray.class);
    
    public NodeTray() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            //Dimension trayIconSize = tray.getTrayIconSize();
            
            initListeners();
            PopupMenu popup = popupMenu();
            
            Image image;
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/META-INF/app/img/iotope.png"));
            } catch (IOException e) {
                // Something has gone really wrong, dye!
                throw new RuntimeException(e);
            }
            
            trayIcon = new TrayIcon(image, "IOTOPE Node", popup);
            //            ActionListener actionListener = new ActionListener() {
            //                public void actionPerformed(ActionEvent e) {
            //                    trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
            //                }
            //            };
            
            trayIcon.setImageAutoSize(true);
            //            trayIcon.addActionListener(actionListener);
            trayIcon.addMouseListener(mouseListener);
            
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
    }

    @Subscribe
    public void onModeChange(Mode mode) {
        miLearnMode.setState(mode.isLearnMode());
        System.err.println("changed is learn mode: "+mode.isLearnMode());
    }

    private PopupMenu popupMenu() {
        PopupMenu popup = new PopupMenu();
        
        miNodeConsole = new MenuItem("Node Console");
        miNodeConsole.addActionListener(consoleListener);
        popup.add(miNodeConsole);
        
        miLearnMode = new CheckboxMenuItem("Learn Mode");
        miLearnMode.addItemListener(learnListener);
        popup.add(miLearnMode);
        
        miExit = new MenuItem("Exit");
        miExit.addActionListener(exitListener);
        popup.add(miExit);
        return popup;
    }
    
    public void message(String caption, String message, String type) {
        try {
            TrayIcon.MessageType messageType = TrayIcon.MessageType.NONE;
            if (type != null && type.length() != 0) {
                // Allowed: ERROR | WARNING | INFO | NONE
                messageType = TrayIcon.MessageType.valueOf(type);
            }
            trayIcon.displayMessage(caption, message, messageType);
        }
        catch(IllegalArgumentException e) {
            Log.error(e.getMessage());
        }
    }
    
    private void initListeners() {
        exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Log.info("Exiting IOTOPE Node... BYE");
                System.exit(0);
            }
        };
        
        learnListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                mode.setLearnMode(e.getStateChange() == ItemEvent.SELECTED);
            }
        };
        
        consoleListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(URI.create("http://localhost:4242/ui/"));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        
        mouseListener = new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        };
    }
    
    
    private ActionListener consoleListener;
    
    private ItemListener learnListener;
    
    private ActionListener exitListener;
    
    private MouseListener mouseListener;
    
    MenuItem miNodeConsole;
    CheckboxMenuItem miLearnMode;
    MenuItem miExit;
    
    
    
    TrayIcon trayIcon;
    
    @Inject
    Mode mode;
    
    @SuppressWarnings("unused")
    @Inject
    private NodeBus bus;
}
