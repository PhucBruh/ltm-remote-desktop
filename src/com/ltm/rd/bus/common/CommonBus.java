package com.ltm.rd.bus.common;

import com.ltm.rd.bus.rmi.RmiClient;
import com.ltm.rd.bus.rmi.RmiServer;
import com.ltm.rd.bus.tcp.TcpClient;
import com.ltm.rd.bus.tcp.TcpServer;
import java.awt.AWTException;
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
    private TcpServer tcp_server;
    private RmiServer rmi_server;

    // TODO: for client
    private TcpClient tcp_client;
    private RmiClient rmi_client;

    public CommonBus() {
        this.rmi_server = new RmiServer();
        this.rmi_client = new RmiClient();
    }

    public void initTcp() {
        this.tcp_server = new TcpServer();
        this.tcp_client = new TcpClient();
    }

    public TcpServer getTcpServer() {
        return this.tcp_server;
    }

    public RmiServer getRmiServer() {
        return this.rmi_server;
    }

    public TcpClient getTcpClient() {
        return this.tcp_client;
    }

    public RmiClient getRmiClient() {
        return this.rmi_client;
    }

    // TODO: handle events of server
    public void startListeningOnServer(String host, int port, String password) throws IOException, AWTException {
        if(!this.rmi_server.isBinding()) {
//        if(!this.tcp_server.isListening() && !this.rmi_server.isBinding()) {
            // Port rmi = port tcp + 1
//            this.tcp_server.startListeningOnTcpServer(host, port, password);
            this.rmi_server.startBindingOnRmiServer(host, port + 1);
        }
    }

    public void stopListeningOnServer() throws IOException, NotBoundException {
//        if(this.tcp_server.isListening() && this.rmi_server.isBinding()) {
        if(this.rmi_server.isBinding()) {
//            this.tcp_server.stopListeningOnTcpServer();
            this.rmi_server.stopBindingOnRmiServer();
        }
    }

    public void startConnectingToServer(String host, int port, String password) throws Exception {
        // TODO: check server is listening?
//        if(this.tcp_server.isListening()) {
//            String ip_server = this.tcp_server.getServer().getInetAddress().getHostAddress();
//            if(host.equals(ip_server)) throw new Exception("Can't remote yourself!");
//            System.out.println(ip_server);
//            System.out.println(host);
//        }
//        if(this.tcp_client.isConnectedServer()) throw new Exception("You are remoting!");
//        this.tcp_client.startConnectingToTcpServer(host, port, password);
        this.rmi_client.startConnectingToRmiServer(host, port + 1);
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