package com.ltm.rd.gui.server;

import com.ltm.rd.bus.common.CommonBus;
import com.ltm.rd.gui.MainFrame;
import com.ltm.rd.gui.client.ClientPanel;
import com.ltm.rd.gui.common.CommonLabel;
import com.ltm.rd.gui.common.CommonPanel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.SocketException;
import java.util.Vector;

public class ServerPanel extends JPanel {
    public final static String STOPPED_FOREGROUND = "0xF50016";
    public final static String LISTENING_FOREGROUND = "0x16a085";

    private CommonPanel main_panel;
    private JLabel status_label;
    private CommonLabel listen_label;
    private CommonLabel stop_label;

    private CommonBus common_bus;

    public ServerPanel(CommonBus common_bus) {
        // TODO: style ClientPanel
        this.setLocation(0, MainFrame.HEIGHT_TASKBAR);
        this.setSize(MainFrame.WIDTH_FRAME, MainFrame.HEIGHT_FRAME - MainFrame.HEIGHT_TASKBAR);
        this.setBackground(Color.decode(ClientPanel.BACKGROUND));
        this.setLayout(null);

        // TODO: class for handle events
        this.common_bus = common_bus;

        // TODO: add components
        this.initComponents();
    }

    private void initComponents() {
        // TODO: constructor
        this.main_panel = new CommonPanel();
        this.status_label = new JLabel();
        this.listen_label = new CommonLabel();
        this.stop_label = new CommonLabel();

        // TODO: add main_panel
        this.add(this.main_panel);

        // TODO: add items for host_combo in main_panel
        this.main_panel.getHostCombo().addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                try {
                    main_panel.getHostCombo().removeAllItems();
                    main_panel.getHostCombo().addItem("0.0.0.0");
                    Vector<String> ipv4_addresses = common_bus.getAllIpv4AddressesOnLocal();
                    for(String ipv4 : ipv4_addresses) {
                        main_panel.getHostCombo().addItem(ipv4);
                    }
                }
                catch(SocketException exception) {}
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        // TODO: style status_label
        this.status_label.setText("Status: Stopped");
        this.status_label.setBounds(30, 200, 150, 20);
        this.status_label.setFont(new Font(MainFrame.GLOBAL_FONT, Font.ITALIC | Font.BOLD, 12));
        this.status_label.setForeground(Color.decode(ServerPanel.STOPPED_FOREGROUND));
        this.main_panel.add(this.status_label);

        // TODO: style listen_label
        this.listen_label.setText("Start remote");
        this.listen_label.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/listen_icon.png")));
        this.listen_label.setBounds(50, 290, 150, 50);
        this.listen_label.setForeground(Color.decode(ClientPanel.FOREGROUND));
        this.listen_label.setFont(new Font(MainFrame.GLOBAL_FONT, Font.PLAIN, 14));
        this.listen_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listenLabelMousePressed(e);
            }
        });
        this.add(this.listen_label);

        // TODO: style stop_label
        this.stop_label.setText("Stop remote");
        this.stop_label.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/stop_icon.png")));
        this.stop_label.setBounds(220, 290, 150, 50);
        this.stop_label.setForeground(Color.decode(ClientPanel.FOREGROUND));
        this.stop_label.setFont(new Font(MainFrame.GLOBAL_FONT, Font.PLAIN, 14));
        this.stop_label.setEnabled(false);
        this.stop_label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                stopLabelMousePressed(e);
            }
        });
        this.add(this.stop_label);
    }

    // TODO: handle events of listen_label
    private void listenLabelMousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && this.listen_label.isEnabled()) {
            try {
                String host = this.main_panel.getHostCombo().getSelectedItem().toString().trim();
                int port = Integer.parseInt(this.main_panel.getPortText().getText().trim());
                String password = this.main_panel.getPassText().getText().trim();
                this.common_bus.startListeningOnServer(host, port, password);

                // TODO: set status
                this.main_panel.setEnabled(false);
                this.listen_label.resetFont();
                this.listen_label.setEnabled(false);
                this.stop_label.setEnabled(true);
                this.status_label.setText("Status: Listening...");
                this.status_label.setForeground(Color.decode(ServerPanel.LISTENING_FOREGROUND));
            }
            catch(Exception exception) {
                JOptionPane.showMessageDialog(this, "Can't start listening on server:\n" + exception.getMessage());
            }
        }
    }

    // TODO: handle events of stop_label
    private void stopLabelMousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1 && this.stop_label.isEnabled()) {
            try {
                this.common_bus.stopListeningOnServer();

                // TODO: set status
                this.main_panel.setEnabled(true);
                this.stop_label.resetFont();
                this.stop_label.setEnabled(false);
                this.listen_label.setEnabled(true);
                this.status_label.setText("Status: Stopped");
                this.status_label.setForeground(Color.decode(ServerPanel.STOPPED_FOREGROUND));
            }
            catch(Exception exception) {
                System.out.println(exception.getMessage());
                JOptionPane.showMessageDialog(this, "Can't stop listening on server:\n" + exception.getMessage());
            }
        }
    }
}
