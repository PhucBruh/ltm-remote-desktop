package com.ltm.rd.bus.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiClient {
    private IRemoteDesktop remote_obj;
    private boolean is_remote_server;

    public RmiClient() {
        this.remote_obj = null;
        this.is_remote_server = false;
    }

    public void startConnectingToRmiServer(String host, int port, String password) throws RemoteException, NotBoundException, MalformedURLException {
        if(!this.is_remote_server) {
            String url = "rmi://" + host + ":" + port + "/remote";
            this.remote_obj = (IRemoteDesktop) Naming.lookup(url);
            this.remote_obj.authenticate(password);
            this.is_remote_server = true;
        }
    }

    public void startConnectingToRmiServer(String host, int port, int password) throws RemoteException, NotBoundException, MalformedURLException {
        if(!this.is_remote_server) {
            String url = "rmi://" + host + ":" + port + "/remote";
            this.remote_obj = (IRemoteDesktop) Naming.lookup(url);
            this.is_remote_server = true;
        }
    }


    public void stopConnectingToRmiServer() {
        if(this.is_remote_server) {
            this.remote_obj = null;
            this.is_remote_server = false;
        }
    }

    public IRemoteDesktop getRemoteObject() {
        return this.remote_obj;
    }

    public boolean isRemoteServer() {
        return this.is_remote_server;
    }

    public void setRemoteServer(boolean b) {
        this.is_remote_server = b;
    }
}
