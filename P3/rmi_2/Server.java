
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

/**
 * @author Pablo Jj
 */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;

public class Server implements I_Ejemplo2 {
    public Server() {
        super();
    }

    
    public void escribirMensaje(String mensaje) {
        System.out.println("\nEntra Hebra " + mensaje);

        // Buscamos los procesos 0, 10, 20,...
        if (mensaje.endsWith("0")) {
            try {
                System.out.println("Empezamos a dormir");
                Thread.sleep(5000);
                System.out.println("Terminamos de dormir");
            } catch (Exception e) {
                System.err.println("Ejemplo exception:");
                e.printStackTrace();
            }
        }
        System.out.println("Sale Hebra " + mensaje);
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String nombre_objeto_remoto = "Ejemplo_I";
            I_Ejemplo2 prueba = new Server();
            I_Ejemplo2 stub = (I_Ejemplo2) UnicastRemoteObject.exportObject(prueba, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(nombre_objeto_remoto, stub);
            System.out.println("Ejemplo bound");
        } catch (Exception e) {
            System.err.println("Ejemplo exception:");
            e.printStackTrace();
        }
    }
}