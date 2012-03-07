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

import com.google.common.eventbus.EventBus;

public class NodeTray {
    
    public NodeTray(EventBus bus) {
        this.bus = bus;
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
            
            final TrayIcon trayIcon = new TrayIcon(image, "IOTOPE Node", popup);
            ActionListener actionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
                }
            };
            
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);
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
    
    private void initListeners() {
        exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting...");
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
    
    private EventBus bus;
}
