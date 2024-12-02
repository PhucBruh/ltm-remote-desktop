package com.ltm.rd.bus.rmi;

import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {
    private String url;
    private String password;
    private boolean is_binding;
    private RemoteDesktopImpl remote_obj;
    private Thread auth_thread;


    public RmiServer() {
        this.url = null;
        this.is_binding = false;
    }

    public void startBindingOnRmiServer(String host, int port, String password) throws RemoteException, MalformedURLException, AWTException {
        if(!this.is_binding) {
            try {
                this.url = "rmi://" + host + ":" + port + "/remote";
                this.password = password;
                this.is_binding = true;
                System.setProperty("java.rmi.server.hostname", host);
                LocateRegistry.createRegistry(port);

                this.remote_obj = new RemoteDesktopImpl();
                Naming.rebind(this.url, this.remote_obj); // only new object in here
                if (this.auth_thread==null || !this.auth_thread.isAlive()) {
                    this.auth_thread = init_auth_thread();
                }
                this.auth_thread.start();
            }
            catch(Exception e) {
                // TODO: rebind when port already bound
                this.remote_obj = new RemoteDesktopImpl();
                Naming.rebind(this.url, this.remote_obj);
                if (this.auth_thread==null) {
                    this.auth_thread = init_auth_thread();
                }
                this.auth_thread.start();
            }
        }
    }

    public void stopBindingOnRmiServer() throws RemoteException, MalformedURLException, NotBoundException {
        if(this.is_binding) {
            if (this.remote_obj!=null) UnicastRemoteObject.unexportObject(this.remote_obj, true);
            Naming.unbind(this.url);
            this.url = null;
            this.is_binding = false;
            this.auth_thread.interrupt();
            this.auth_thread = null;
        }
    }

    public boolean isBinding() {
        return this.is_binding;
    }

    public Thread init_auth_thread() {
        return new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (!this.password.equals(this.remote_obj.getPassword())) {
                        UnicastRemoteObject.unexportObject(this.remote_obj, true);
                        this.remote_obj = new RemoteDesktopImpl();
                        Naming.rebind(this.url, this.remote_obj);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                catch (RemoteException | AWTException | MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}