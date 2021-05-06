import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteEjemploMultiHebra implements Runnable {

    public String nombre_objeto_remoto = "Ejemplo_I";
    public String server;

    public ClienteEjemploMultiHebra(String server) {
        this.server = server;
    }

    public void run() {
        System.out.println("Buscando el objeto remoto");
        try {
            Registry registry = LocateRegistry.getRegistry(server);
            I_Ejemplo2 instancia_local = (I_Ejemplo2) registry.lookup(nombre_objeto_remoto);
            System.out.println("Invocando el objeto remoto");
            instancia_local.escribirMensaje(Thread.currentThread().getName());
        } catch (Exception e) {
            System.err.println("Ejemplo2 exception:");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        int n_hebras = Integer.parseInt(args[1]);
        ClienteEjemploMultiHebra[] v_clientes = new ClienteEjemploMultiHebra[n_hebras];
        Thread[] v_hebras = new Thread[n_hebras];

        for (int i = 0; i < n_hebras; i++) {
            v_clientes[i] = new ClienteEjemploMultiHebra(args[0]);
            v_hebras[i] = new Thread(v_clientes[i], "Cliente " + i);
            v_hebras[i].start();
        }
    }
}