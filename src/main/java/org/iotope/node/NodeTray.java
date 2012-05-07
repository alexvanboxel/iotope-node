package org.iotope.node;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class NodeTray {
    private static Logger Log = LoggerFactory.getLogger(NodeTray.class);
    
    public NodeTray() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Dimension trayIconSize = tray.getTrayIconSize();
            
            initListeners();
            PopupMenu popup = popupMenu();
            
            Image image;
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/META-INF/app/img/iotope.png"));
            } catch (IOException e) {
                // Something has gone really wrong, dye!
                throw new RuntimeException(e);
            }
            
            TrayIcon trayIcon = new TrayIcon(image, "IOTOPE Node", popup);
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
    
    private PopupMenu popupMenu() {
        PopupMenu popup = new PopupMenu();

        MenuItem consoleItem = new MenuItem("Node Console");
        consoleItem.addActionListener(consoleListener);
        popup.add(consoleItem);

        CheckboxMenuItem learnItem = new CheckboxMenuItem("Learn Mode");
        consoleItem.addActionListener(learnListener);
        popup.add(learnItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(exitListener);
        popup.add(exitItem);
        return popup;
    }
    
    public void message(String caption,String message,String type) {
        trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
        //      ERROR,
//      /** A warning message */
//      WARNING,
//      /** An information message */
//      INFO,
//      /** Simple message */
//      NONE

    }
    
    private void initListeners() {
        exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Log.info("Exiting IOTOPE Node... BYE");
                System.exit(0);
            }
        };

        learnListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             }
        };

        consoleListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(Desktop.isDesktopSupported()) {
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
    
    private ActionListener learnListener;
    
    private ActionListener exitListener;
    
    private MouseListener mouseListener;
    
    TrayIcon trayIcon;
    
    @Inject
    private NodeBus bus;
}
