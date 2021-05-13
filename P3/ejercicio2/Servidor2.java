import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Servidor2 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            GestionDonaciones servidor1 = new GestionDonaciones("servidor1", 1);
            Naming.rebind("servidor1", servidor1);
            System.out.println("Servidor 1 preparado");

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}