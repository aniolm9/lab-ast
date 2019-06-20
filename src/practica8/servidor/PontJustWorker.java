/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica8.servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import practica8.Comms;
import practica8.PontJust;

/**
 *
 * @author aniol
 */
public class PontJustWorker implements Runnable {
    Socket sc;
    BufferedReader br;
    PrintWriter pw;
    PontJust p;
    
    public PontJustWorker(Socket sc, PontJust p) {
        this.sc = sc;
        this.p = p;
    }
    
    @Override
    public void run() {
        try {
            pw = new PrintWriter(sc.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            while (true) {
                String line = br.readLine();
                switch (line) {
                    case Comms.ENTRAR_S:
                        System.out.println("Abertis :)");
                        int sentit = Integer.parseInt(br.readLine());
                        p.entrar(sentit);
                        pw.println(Comms.OK_S);
                        break;
                    case Comms.SORTIR_S:
                        System.out.println("Abertis :(");
                        p.sortir();
                        pw.println(Comms.OK_S);
                        break;
                    case Comms.FIN_S:
                        System.out.println("Abertis :(((((((((");
                        if (sc != null) sc.close();
                        return;
                }
            }
        }
        catch (Exception e) {}
    }
}
