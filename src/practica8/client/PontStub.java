/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica8.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import practica8.Communication;
import practica8.Pont;

/**
 *
 * @author aniol
 */
public class PontStub implements Pont {
    Socket sc;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    
    public PontStub() {
        try {
            sc = new Socket(Communication.HOST, Communication.PORT);
            ois = new ObjectInputStream(sc.getInputStream());
            oos = new ObjectOutputStream(sc.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            oos.writeInt(Communication.FIN);
            oos.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void entrar(boolean sentit) {
        try {
            oos.writeInt(Communication.ENTRAR);
            //oos.flush(); // Just send the integer, then the boolean.
            oos.writeBoolean(sentit);
            oos.flush();
            System.out.println("Entrar: " + ois.readInt());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void sortir() {
        try {
            oos.writeInt(Communication.SORTIR);
            oos.flush();
            System.out.println("Sortir: " + ois.readInt());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
