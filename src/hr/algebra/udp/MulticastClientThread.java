/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.udp;

import hr.algebra.model.Bomb;
import hr.algebra.model.UDPDataPackage;
import hr.algebra.utilities.ByteUtils;
import hr.algebra.utilities.ObjectUtil;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Scene;

/**
 *
 * @author dnlbe
 */
public class MulticastClientThread extends Thread {

    private static final String PROPERTIES_FILE = "socket.properties";
    private static final String CLIENT_PORT = "CLIENT_PORT";
    private static final String GROUP = "GROUP";
    private static final Properties PROPERTIES = new Properties();
    
    private UDPDataPackage udpPackage;

    public UDPDataPackage getUdpPackage() {
        return udpPackage;
    }

    static {
        try {
            PROPERTIES.load(new FileInputStream(PROPERTIES_FILE));
        } catch (IOException ex) {
            Logger.getLogger(MulticastClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        // we use new Socket for each client call
        try (MulticastSocket client = new MulticastSocket(Integer.valueOf(PROPERTIES.getProperty(CLIENT_PORT)))) {
            udpPackage = new UDPDataPackage();
            
            InetAddress groupAddress = InetAddress.getByName(PROPERTIES.getProperty(GROUP));
            client.joinGroup(groupAddress);
            
            long lastTime = System.nanoTime();
            final double ns = 1000000000.0 / 30.0;
            double delta = 0;
            while(true){
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while(delta >= 1){
                    
                    // first we read the payload length
                    byte[] numberOfUDPDataPackageBytes = new byte[4];
                    DatagramPacket packet = new DatagramPacket(numberOfUDPDataPackageBytes, numberOfUDPDataPackageBytes.length);
                    client.receive(packet);
                    int length = ByteUtils.byteArrayToInt(numberOfUDPDataPackageBytes);
                    
                    if (length < 1)
                        continue;

                    // we can read payload of that length
                    byte[] udpPackageBytes = new byte[length];
                    packet = new DatagramPacket(udpPackageBytes, udpPackageBytes.length);
                    client.receive(packet);
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(udpPackageBytes);
                            ObjectInputStream ois = new ObjectInputStream(bais)) {
                        udpPackage = (UDPDataPackage) ois.readObject();
                    } catch (Exception e){

                    }
                }
            }

        } catch (SocketException | UnknownHostException e) {
            Logger.getLogger(MulticastClientThread.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(MulticastClientThread.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
