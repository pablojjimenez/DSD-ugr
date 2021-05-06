import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Servidor {
    public static void main(String[] args) {

        // Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            // Crea una instancia de contador

            //System.setProperty("java.rmi.server.hostname","192.168.1.107");
            Registry reg = LocateRegistry.createRegistry(2099);
            Contador micontador = new Contador();
            Naming.rebind("micontador", micontador);
            
            System.out.println("Servidor preparado");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}