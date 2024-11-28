package com.ltm.rd.bus.common;

import com.ltm.rd.bus.rmi.RmiClient;
import com.ltm.rd.bus.rmi.RmiServer;

import java.awt.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.NotBoundException;
import java.util.Enumeration;
import java.util.Vector;

public class CommonBus {
    // TODO: for server
    private RmiServer rmi_server;

    // TODO: for client
    private RmiClient rmi_client;

    public CommonBus() {
        this.rmi_server = new RmiServer();
        this.rmi_client = new RmiClient();
    }

    public RmiServer getRmiServer() {
        return this.rmi_server;
    }

    public RmiClient getRmiClient() {
        return this.rmi_client;
    }

    // TODO: handle events of server
    public void startListeningOnServer(String host, int port, String password) throws IOException, AWTException {
        if(!this.rmi_server.isBinding()) {
            this.rmi_server.startBindingOnRmiServer(host, port, password);
        }
    }

    public void stopListeningOnServer() throws IOException, NotBoundException {
        if(this.rmi_server.isBinding()) {
            this.rmi_server.stopBindingOnRmiServer();
        }
    }

    public void startConnectingToServer(String host, int port, String password) throws Exception {
        // TODO: check server is listening?
        this.rmi_client.startConnectingToRmiServer(host, port, password);
    }

    public Vector<String> getAllIpv4AddressesOnLocal() throws SocketException {
        Vector<String> ipv4_addresses = new Vector<>();
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        while(networks.hasMoreElements()) {
            NetworkInterface sub_networks = (NetworkInterface) networks.nextElement();
            Enumeration<InetAddress> inet_addresses = sub_networks.getInetAddresses();
            while(inet_addresses.hasMoreElements()) {
                try {
                    Inet4Address ipv4 = (Inet4Address) inet_addresses.nextElement();
                    ipv4_addresses.add(ipv4.getHostAddress());
                }
                catch(Exception e) {
                    // TODO: pass ip version 6
                }
            }
        }
        return ipv4_addresses;
    }
}