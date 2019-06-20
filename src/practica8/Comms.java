package practica8;

/**
 *
 * @author aniol
 */
public interface Comms {
    // Generic.
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 2000;
    
    // Version 0.
    public static final int ENTRAR = 1;
    public static final int SORTIR = 2;
    public static final int OK  = 3;
    public static final int FIN = 4;
    
    // Version 1.
    public static final String ENTRAR_S = "ENTRAR";
    public static final String SORTIR_S = "SORTIR";
    public static final String OK_S = "OK";
    public static final String FIN_S = "FIN";
}
