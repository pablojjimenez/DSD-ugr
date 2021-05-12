import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class Servidor2 {
    public static void main(String[] args) {

    // Crea e instala el gestor de seguridad
    if (System.getSecurityManager() == null) {
        System.setSecurityManager(new SecurityManager());
    }

    try {
        // Crea una instancia de donaciones
        String nombreReplica = "ddonaciones1";
        Donaciones donaciones2 = new Donaciones(nombreReplica);
        Naming.rebind("ddonaciones2", donaciones2);
        System.out.println("Servidor Donaciones 2 preparado");

        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}