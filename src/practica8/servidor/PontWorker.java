/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica8.servidor;

import java.io.EOFException;
import practica8.Communication;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import practica8.PontInjust;

/**
 *
 * @author aniol
 */
public class PontWorker implements Runnable {
    Socket sc;
    PontInjust p;
    
    public PontWorker(Socket sc, PontInjust p) {
        this.sc = sc;
        this.p = p;
    }
    
    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(sc.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(sc.getInputStream());
            
            while (true) {
                int solicitud;
                try {
                    solicitud = ois.readInt();
                    System.out.println("Sol: " + solicitud);
                }
                catch (EOFException eof) {
                    break;
                }
                switch (solicitud) {
                    case (Communication.ENTRAR): {
                        boolean sentit = ois.readBoolean();
                        p.entrar(sentit);
                        oos.writeInt(Communication.OK);
                        oos.flush();
                        break;
                    }
                    case (Communication.SORTIR): {
                        p.sortir();
                        oos.writeInt(Communication.OK);
                        oos.flush();
                        break;
                    }
                    case (Communication.FIN): {
                        sc.close();
                        System.out.println("Ha sortit un client.");
                        return;
                    }
                    default:
                        break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
