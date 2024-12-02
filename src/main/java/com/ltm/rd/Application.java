package com.ltm.rd;

import com.ltm.rd.gui.MainFrame;
import com.ltm.rd.gui.remote.RemoteFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Application{
    public static void main(String[] args) {
        // TODO: point to start new program

        EventQueue.invokeLater(() -> {
            try {
                // Tải và đăng ký font ngay lập tức
                List<String> fontPaths = Arrays.asList(
                        "/fonts/FiraCode-Regular.ttf",
                        "/fonts/FiraCode-Bold.ttf",
                        "/fonts/FiraCode-Light.ttf",
                        "/fonts/FiraCode-Medium.ttf",
                        "/fonts/FiraCode-Retina.ttf",
                        "/fonts/FiraCode-SemiBold.ttf"
                );
                for (String fontPath : fontPaths) loadFont(fontPath);
                UIManager.setLookAndFeel("com.formdev.flatlaf.themes.FlatMacLightLaf");
                new MainFrame();
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(null, "Application error!\n" + e.getMessage());
            } catch (Exception ignored) {}
        });
    }

    private static void loadFont(String fontPath) throws Exception {
        InputStream fontStream = Application.class.getResourceAsStream(fontPath);
        if (fontStream != null) {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } else {
            System.err.println("Load font failed");
        }
    }
}
