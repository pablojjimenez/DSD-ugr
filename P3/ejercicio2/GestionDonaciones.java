import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;

public class GestionDonaciones extends UnicastRemoteObject implements I_Donaciones, I_Servidores {
    private String nombre;
    private int num;
    private String replica;
    private double subtotal;
    private HashMap<Usuario, Pair<Double, String>> clientes;
    private static final int PUERTO = 1099;

    public GestionDonaciones(String nombre, int num) throws RemoteException {
        super();
        this.nombre = nombre;
        this.num = num;
        if (this.num == 0) {
            this.replica = "servidor1";
        } else {
            this.replica = "servidor0";
        }

        subtotal = 0.0;
        clientes = new HashMap<>();
    }

    /**
     * Se encarga de registrar un usuario en el sistema. Comprueba que no existe ya
     * el usuario en la réplica. En caso de no existir, se añade en el servidor que
     * esté menso cargado.
     */
    public Boolean registro(Usuario u) throws RemoteException {

        Boolean existe = false;

        if (clientes.get(u) == null) {

            I_Servidores replicaServ = getReplica();

            if (!replicaServ.existeUsuario(u)) {
                if (this.clientes.size() <= replicaServ.getSize()) {
                    this.introducirUsuario(u);
                } else {
                    replicaServ.introducirUsuario(u);
                }
            } else {
                existe = true;
            }
        } else {
            existe = true;
        }

        return existe;
    }

    public Boolean iniciarSesion(Usuario u) throws RemoteException {
        Boolean existe = false;
        I_Servidores replicaServ = getReplica();
        if (clientes.get(u) != null || replicaServ.existeUsuario(u))
            existe = true;

        return existe;
    }

    public Boolean donar(Usuario usuario, double donacion) throws RemoteException {
        Boolean existe = false;
        I_Servidores replicaServ = getReplica();
        if (donacion > 0) {
            if (clientes.get(usuario) != null) {
                existe = true;
                this.sumarDonacion(usuario, donacion);
            } else if (replicaServ.existeUsuario(usuario)) {
                existe = true;
                replicaServ.sumarDonacion(usuario, donacion);
            }
        }

        return existe;
    }

    public Double getTotal() throws RemoteException {
        Double total = 0.0;
        total += this.subtotal;
        I_Servidores replicaServ = this.getReplica();
        total += replicaServ.getSubtotal();
        return total;
    }

    public Double getUsuario(Usuario usuario) throws RemoteException {
        Pair donacion = new Pair<Double, String>(0.0, "");
        double d = -1;
        I_Servidores replicaServ = this.getReplica();
        if (clientes.get(usuario) != null)
            return clientes.get(usuario).getFirst();
        else if (replicaServ.existeUsuario(usuario))
            return replicaServ.getDonacionUsuario(usuario);
        return -1.1;
    }

    /**
     * G E S T I Ó N D E S E R V I D O R E S
     */

    public Boolean existeUsuario(Usuario usuario) throws RemoteException {
        Boolean existe = false;

        if (this.clientes.get(usuario) != null)
            existe = true;

        return existe;
    }

    public void volcarLogServer() throws RemoteException {
        System.out.println(C.YELLOW + "Se ha solicitado fichero de log "+ C.RESET);
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter("INFORMACION_LOG_SERVIDOR.txt");
            pw = new PrintWriter(fichero);
            pw.println("----Servidor0:");
            pw.println(this.clientes);
            I_Servidores replicaServ = this.getReplica();
            pw.println("----Servidor1:");
            pw.print(replicaServ.getClientes().toString());
            pw.print("\n");
            pw.print("Total recaudado: " + this.getTotal());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public I_Servidores getReplica() throws RemoteException {

        I_Servidores servReplica = null;
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", PUERTO);
            servReplica = (I_Servidores) registry.lookup(this.replica);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return servReplica;
    }

    public void introducirUsuario(Usuario u) throws RemoteException {
        this.clientes.put(u, new Pair<Double, String>(0.0, this.replica));
        System.out.printf("%s %s %s %n", C.YELLOW, "Se ha introducido un usuario en el ", this.nombre, C.RESET);
    }

    public int getSize() throws RemoteException {
        return this.clientes.size();
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public void sumarDonacion(Usuario usuario, double donacion) {
        this.clientes.put(usuario, new Pair<Double, String>(clientes.get(usuario).getFirst() + donacion,
                clientes.get(usuario).getSecond()));
        this.subtotal += donacion;
        System.out.println(C.YELLOW + "Se han donado " + donacion + "€ al " + this.nombre + C.RESET);
    }

    public Double getDonacionUsuario(Usuario usuario) throws RemoteException {
        System.out.println(C.YELLOW + "Se solicita lo donado por el usuario: " + usuario.getNombre() + C.RESET);
        return this.clientes.get(usuario).getFirst();
    }

    public HashMap getClientes() throws RemoteException {
        return this.clientes;
    }

}