/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.udp;

import hr.algebra.utilities.ByteUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel.bele
 */
public class UnicastClientThread extends Thread {
    private static String HOST = "localhost";
    private static int SERVER_PORT = 12345;
    
    private static int playerMovement;
    private static int oldPlayerMovement = -1;

    public UnicastClientThread(String host, int port) {
        HOST = host;
        SERVER_PORT = port;
    }

    public int getPlayerMovement() {
        return playerMovement;
    }
    
    public void setPlayerMovement(int playerMovement) {
        this.playerMovement = playerMovement;
    }

    @Override
    public void run() {
        while (true) {
            Calendar cal = Calendar.getInstance();
            int now = (int) cal.getTimeInMillis();
            int lastFrame = (int) cal.getTimeInMillis();
            try (DatagramSocket clientSocket = new DatagramSocket()) {
                //limiting the while loop to 30 times a second
                now = (int) cal.getTimeInMillis();
                int delta = now - lastFrame;
                lastFrame = now;

                if(delta < 33)
                {
                    Thread.sleep(33 - delta);
                }
                
                if (playerMovement != oldPlayerMovement) {
                    sendPackage(clientSocket);
                    oldPlayerMovement = playerMovement;
                }
            } catch (InterruptedException | SocketException | UnknownHostException ex) {
                Logger.getLogger(UnicastClientThread.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(UnicastClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private void sendPackage(DatagramSocket clientSocket) throws IOException {
        byte[] buffer = ByteUtils.intToByteArray(playerMovement);
        InetAddress serverAddress = InetAddress.getByName(HOST);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
        clientSocket.send(packet);
    }
}
