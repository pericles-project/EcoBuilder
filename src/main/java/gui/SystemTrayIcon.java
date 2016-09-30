/*
 * Copyright 2016 Anna Eggers - Göttingen State and University Library
 * The work has been developed in the PERICLES Project by Members of the PERICLES Consortium.
 * This project has received funding from the European Union’s Seventh Framework Programme for research, technological
 * development and demonstration under grant agreement no FP7- 601138 PERICLES.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including without
 * limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTIBITLY, or FITNESS FOR A PARTICULAR
 * PURPOSE. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise,
 * unless required by applicable law or agreed to in writing, shall any Contributor be liable for damages, including
 * any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this
 * License or out of the use or inability to use the Work.
 * See the License for the specific language governing permissions and limitation under the License.
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * This class manages the system tray icon, which has a PERICLES logo and offers a user menu.
 */
public final class SystemTrayIcon implements ActionListener {
    private static String PERICLES_LOGO = "logo.png";

    private EcoBuilder gui;

    protected TrayIcon icon = null;
    private PopupMenu menu = new PopupMenu();

    private MenuItem saveScenario;
    private MenuItem generateExamples;
    private MenuItem generateDEM;
    private MenuItem exitItem;

    /**
     * This is where the system tray icon is added at tool start, and removed at tool exit:
     */
    public SystemTrayIcon(EcoBuilder gui) {
        this.gui = gui;
        createMenu();
        createTrayIcon();
        if (icon != null) {
            addTayIconToSystemTray(icon);
        }
    }

    private void createMenu() {
        saveScenario = addItem("Save scenario");
        generateExamples = addItem("Generate the scenario examples");
        generateDEM = addItem("Generate abstract Digital Ecosystem Model");
        exitItem = addItem("Exit EcoBuilder");
    }

    private void createTrayIcon() {
        if (!SystemTray.isSupported()) {
            return;
        }
        try {
            URL url = getClass().getResource("/" + PERICLES_LOGO);
            if(url == null){
                url = getClass().getResource(PERICLES_LOGO);
                if(url == null){
                    System.err.println("Couldn't load image resource.");
                    return;
                }
            }
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            if (image == null) {
                System.err.println("Couldn't load system tray icon.");
                return;
            }
            ImageIcon imageIcon = new ImageIcon(image, "EcoBuilder");
            icon = new TrayIcon(imageIcon.getImage(), "EcoBuilder", menu);
            icon.setImageAutoSize(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MenuItem addItem(String text) {
        MenuItem item = new MenuItem(text);
        item.addActionListener(this);
        menu.add(item);
        return item;
    }

    private void addTayIconToSystemTray(TrayIcon trayIcon) {
        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveScenario) {
            gui.saveScenario();
        } else if (e.getSource() == generateExamples) {
            gui.saveExamples();
        } else if (e.getSource() == generateDEM) {
            gui.saveDEM();
        } else if (e.getSource() == exitItem) {
            gui.closeDialog();
        }
    }
}
