import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class Servidor1 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            GestionDonaciones servidor1 = new GestionDonaciones("servidor1", 1);
            Naming.rebind("servidor1", servidor1);
            System.out.println(C.BLUE_BACKGROUND_BRIGHT + "Servidor 1 preparado" + C.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
