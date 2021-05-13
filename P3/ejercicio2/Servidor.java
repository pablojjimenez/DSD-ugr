import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Servidor{
    public static void main(String[] args) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {

            LocateRegistry.createRegistry(1091);
            GestionDonaciones servidor0 = new GestionDonaciones("servidor0", 0);
            Naming.rebind("servidor0", servidor0);
            System.out.println("Servidor 0 preparado");

        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}