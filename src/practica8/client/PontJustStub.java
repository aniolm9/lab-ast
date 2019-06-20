package practica8.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import practica8.Pont;
import practica8.Comms;

/**
 *
 * @author aniol
 */
public class PontJustStub implements Pont {
    Socket sc;
    BufferedReader br;
    PrintWriter pw;
    
    public PontJustStub() {
        try {
            sc = new Socket(Comms.HOST, Comms.PORT);
            br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            pw = new PrintWriter(sc.getOutputStream(), true);
        }
        catch (Exception e) {}
    }
    
    @Override
    public void entrar (int sentit) {
        try {
            pw.println(Comms.ENTRAR_S);
            pw.println(sentit);
            String resultat = br.readLine();
            //System.out.println(resultat);
        }
        catch (Exception e) {} 
    }
    
    @Override
    public void sortir () {
        try {
            pw.println(Comms.SORTIR_S);
            String resultat = br.readLine();
            //System.out.println(resultat);
        }
        catch (Exception e) {}
    }
    
    public void close () {
        try {
            pw.println(Comms.FIN_S);
            sc.close();
        }
        catch (Exception e) {}
    }
}
