package com.ltm.rd.bus.rmi;

import com.ltm.rd.gui.remote.ComputerInfo;
import com.ltm.rd.gui.remote.DriveInfo;
import com.sun.management.OperatingSystemMXBean;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteDesktopImpl extends UnicastRemoteObject implements IRemoteDesktop {
    public final static int GB = 1024 * 1024 * 1024;

    private String password;
    private boolean waitingForAuthentication;
    private Robot mr_robot;
    private OperatingSystemMXBean os;

    public RemoteDesktopImpl() throws RemoteException, AWTException {
        super();
        this.waitingForAuthentication = false;
        this.password = "";
        this.mr_robot = new Robot();
        this.os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public boolean isWaitingForAuthentication() throws RemoteException {
        return waitingForAuthentication;
    }

    @Override
    public String getPassword() throws RemoteException{
        return password;
    }

    @Override
    public void setPassword(String password) throws RemoteException{
        this.password = password;
        this.waitingForAuthentication = true;
    }

    @Override
    public byte[] takeScreenshotServer(String quality) throws Exception {
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screen_size);
        BufferedImage screenshot = this.mr_robot.createScreenCapture(bounds);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.setUseCache(false); // TODO: not using disk cache (using ram)
        ImageIO.write(screenshot, quality, bos);
        return bos.toByteArray();
    }

    // TODO: mouse
    @Override
    public void mouseMovedServer(int x, int y) throws RemoteException {
        this.mr_robot.mouseMove(x, y);
    }

    @Override
    public void mousePressedServer(int buttons) throws RemoteException {
        this.mr_robot.mousePress(buttons);
    }

    @Override
    public void mouseReleasedServer(int buttons) throws RemoteException {
        this.mr_robot.mouseRelease(buttons);
    }

    @Override
    public void mouseWheelServer(int wheel_amt) throws RemoteException {
        this.mr_robot.mouseWheel(wheel_amt);
    }

    // TODO: keyboard
    @Override
    public void keyPressedServer(int keycode) throws RemoteException {
        this.mr_robot.keyPress(keycode);
    }

    @Override
    public void keyReleasedServer(int keycode) throws RemoteException {
        this.mr_robot.keyRelease(keycode);
    }

    // TODO: for get hardware info

    @Override
    public double getCpuLoadServer() throws RemoteException {
        return this.os.getCpuLoad();
    }

    @Override
    public double getRamUsageServer() throws RemoteException {
        double ratio = (double) (this.os.getTotalMemorySize() - this.os.getFreeMemorySize()) / this.os.getTotalMemorySize();
        return ratio;
    }

    @Override
    public long[] getRamMemories() throws RemoteException {
        return new long[] {
            1 + this.os.getTotalMemorySize() / RemoteDesktopImpl.GB,
            1 + this.os.getTotalSwapSpaceSize() / RemoteDesktopImpl.GB
        };
    }

    @Override
    public int getCpus() throws RemoteException {
        return this.os.getAvailableProcessors();
    }

    @Override
    public ComputerInfo getComputerInformation() throws RemoteException {
        ComputerInfo pc_info = new ComputerInfo(this.os.getName());
        for(File file : File.listRoots()) {
            pc_info.getDrives().add(
                new DriveInfo(
                    FileSystemView.getFileSystemView().getSystemDisplayName(file),
                    file.getFreeSpace() / RemoteDesktopImpl.GB,
                    file.getTotalSpace() / RemoteDesktopImpl.GB
                )
            );
        }
        return pc_info;
    }

}
