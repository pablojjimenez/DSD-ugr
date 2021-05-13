import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class GestionDonaciones extends UnicastRemoteObject implements I_Donaciones, I_Servidores {
    private String nombre;
    private int num;
    private String replica;
    private double subtotal;
    private HashMap<Usuario, Double> clientes;
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
     * Se encarga de registrar un usuario en el sistema. Comprueba que no existe ya el usuario en la réplica.
     * En caso de no existir, se añade en el servidor que esté menso cargado.
     */
    public Boolean registro(Usuario u) throws RemoteException {

        Boolean existe = false;

        if (clientes.get(u) == null) {

            I_Servidores replicaServ = getReplica();

            if (!replicaServ.existeUsuario(u)) {
                if (this.clientes.size() <= replicaServ.getSize()) {
                    this.introducirUsuario(u);
                    System.out.printf("%s %s %s %n",
                            C.YELLOW, "Se ha introducido un usuario en el", this.nombre, C.RESET);
                } else {
                    replicaServ.introducirUsuario(u);
                    System.out.printf("%s %s %s %n",
                            C.YELLOW, "Se ha introducido un usuario en el", this.replica, C.RESET);
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
            }
            else if (replicaServ.existeUsuario(usuario)) {
                existe = true;
                replicaServ.sumarDonacion(usuario, donacion);
            }
        }

        return existe;   
    }

    public Double getTotal () throws RemoteException {
        Double total = 0.0;
        total += this.subtotal;
        I_Servidores replicaServ = this.getReplica();
        total += replicaServ.getSubtotal();
        return total;
    }

    public Double getUsuario(Usuario usuario) throws RemoteException {
        Double donacion =0.0;
        I_Servidores replicaServ = this.getReplica();
        if (clientes.get(usuario) != null)
            donacion = clientes.get(usuario);
        else if (replicaServ.existeUsuario(usuario))
            donacion = replicaServ.getDonacionUsuario(usuario);

        return donacion;
    }

    /**
     *  G E S T I Ó N   D E   S E R V I D O R E S
     */

    public Boolean existeUsuario (Usuario usuario) throws RemoteException {
        Boolean existe = false;

        if (this.clientes.get(usuario) != null)
            existe = true;
            
        return existe;
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

    public void introducirUsuario (Usuario u) throws RemoteException {
        this.clientes.put(u, 0.0);
    }

    public int getSize() throws RemoteException {
        return this.clientes.size();
    }

    public Double getSubtotal()
    {
        return this.subtotal;
    }

    public void sumarDonacion(Usuario usuario, double donacion) {
        this.clientes.put(usuario, clientes.get(usuario) + donacion);
        this.subtotal += donacion;
        System.out.println(C.YELLOW + "Se han donado " + donacion + "€ al " + this.nombre + C.RESET);
    }

    public Double getDonacionUsuario(Usuario usuario) throws RemoteException {
        return this.clientes.get(usuario);
    }

}