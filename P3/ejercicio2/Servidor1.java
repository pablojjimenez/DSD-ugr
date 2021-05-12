import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Servidor1 {
    public static void main(String[] args) {

    // Crea e instala el gestor de seguridad
    if (System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
    }

    try {
        // Crea una instancia de donaciones
        Registry reg = LocateRegistry.createRegistry(1091);
        String nombreReplica = "ddonaciones2";
        Donaciones donaciones1 = new Donaciones(nombreReplica);
        Naming.rebind("ddonaciones1", donaciones1);
        System.out.println("Servidor Donaciones 1 preparado");

        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}