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
    private int SERVER_PORT = 12345;
    
    private static int playerAction;
    private static int oldPlayerAction = -1;

    public UnicastClientThread(String host, int port) {
        HOST = host;
        SERVER_PORT = port;
    }

    public int getPlayerAction() {
        return playerAction;
    }
    
    public void setPlayerAction(int playerMovement) {
        this.playerAction = playerMovement;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                try (DatagramSocket clientSocket = new DatagramSocket()) {

                    if (playerAction != oldPlayerAction) {
                        sendPackage(clientSocket);
                        oldPlayerAction = playerAction;
                    }
                } catch (SocketException | UnknownHostException ex) {
                    Logger.getLogger(UnicastClientThread.class.getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(UnicastClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void sendPackage(DatagramSocket clientSocket) throws IOException {
        byte[] buffer = ByteUtils.intToByteArray(playerAction);
        InetAddress serverAddress = InetAddress.getByName(HOST);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
        clientSocket.send(packet);
    }
}
