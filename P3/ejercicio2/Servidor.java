import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servidor implements I_Donaciones {

    // El total global será la suma del total local de las dos réplicas
    private ArrayList<String> usuarios;

    private double totalLocal;

    // Todas las réplicas
    private ArrayList<I_Donaciones> replicas;

    // aportaciones realizadas
    private ArrayList<Aportacion> registro;

    public Servidor() {
        super();
        this.totalLocal = 0;
        this.usuarios = new ArrayList<>();
        this.registro = new ArrayList<>();
        this.replicas = new ArrayList<>();
    }

    public void registrar(String usuario) throws RemoteException {
        I_Donaciones replica = obtenerReplica(OPC.REGISTRAR_USUARIO, usuario);
        if (replica != null)
            replica.addUsuario(usuario);
    }

    public void donar(Aportacion aportacion) throws RemoteException {
        I_Donaciones replica = obtenerReplica(OPC.DONAR, aportacion.getUser());
        if (replica != null) {
            replica.incrementar(aportacion.getCantidad());
            replica.addToRegistro(aportacion);
        }
    }

    public double getTotal(String usuario) throws RemoteException {
        double suma = 0;
        for (I_Donaciones replica : this.replicas) {
            suma += replica.getTotalLocal();
        }
        return suma;
    }

    public ArrayList<String> getUsuarios() {
        return this.usuarios;
    }

    public ArrayList<Aportacion> getRegistro() {
        return this.registro;
    }

    public void addUsuario(String usuario) {
        this.usuarios.add(usuario);
    }

    public double getTotalLocal() {
        return this.totalLocal;
    }

    public void incrementar(double cantidad) {
        this.totalLocal += cantidad;
    }

    public void addToRegistro(Aportacion donacion) {
        this.registro.add(donacion);
    }

    public ArrayList<I_Donaciones> obtenerReplicas() throws RemoteException {
        return this.replicas;
    }

    /**
     * Une en un array las aportaciones realizadas en todos los servidores
     *
     * @return Lista con todas las aportaciones
     */
    public ArrayList<Aportacion> getRegistroTotal() throws RemoteException {
        throw new UnsupportedOperationException("No implmentado getRegistroTotal()...");
    }

    /**
     * Se encarga de enlazar una nueva replica con las existentes.
     * @param ubicacion
     * @param nombre
     * @param nReplicas
     */
    public void enlazar(String ubicacion, String nombre, int nReplicas) {
        try {
            System.out.println("Estoy llamando a enlazar replicas");
        } catch (Exception e) {
            System.err.println("Error al enlazar los servidores");
            e.printStackTrace();
        }
    }

    /**
     * Esta función se encarga de obtener la replica del servidor dependiendo la
     * operación que se vaya a realizar.
     *
     * @param opcion  puede ser @see {enum OPC}
     * @param usuario
     * @return Retorna la réplica a usar o null en caso contrario.
     * @throws RemoteException excepción lanzada cuando no se encuentra el objeto
     */
    private I_Donaciones obtenerReplica(OPC opcion, String usuario) throws RemoteException {
        I_Donaciones salida = null;
        if (this.replicas.size() > 0) {
            switch (opcion) {
                case REGISTRAR_USUARIO:
                    salida = getReplicaForRegistro(usuario);
                    break;

                case DONAR:
                    salida = getReplicaForDonacion(usuario);
                    break;

                case CONSULTAR:
                    salida = getReplicaForConsulta(usuario);
                    break;
            }
        } else {
            System.out.println("estoy en el else que hay que hacer");
        }
        return salida;
    }

    private I_Donaciones getReplicaForRegistro(String usuario) throws RemoteException {
        // Busco la réplica que tiene el nombre del usuario
        boolean encontrado = false;
        I_Donaciones _replica = this;
        for (I_Donaciones replica : this.replicas) {
            if (replica.getUsuarios().contains(usuario)) {
                encontrado = true;
                _replica = replica;
            }
        }
        if (encontrado) {
            System.err.println("Error al registrar, el usuario ya se encuentra registrado en alguna réplica");
        }

        if (!encontrado) {
            if (!this.usuarios.contains(usuario)) {
                // Busco la que menos usuarios tenga
                for (I_Donaciones replica : this.replicas) {
                    if (_replica.getUsuarios().size() >= replica.getUsuarios().size()) {
                        _replica = replica;
                    }
                }
            }
        }
        return _replica;
    }

    private I_Donaciones getReplicaForDonacion(String usuario) throws RemoteException {
        boolean encontrado = this.usuarios.contains(usuario);
        I_Donaciones _replica = this;
        for (I_Donaciones replica : this.replicas) {
            if (replica.getUsuarios().contains(usuario)) {
                _replica = replica;
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Usuari no encontradoo");
            _replica = null;
        }
        return _replica;
    }

    private I_Donaciones getReplicaForConsulta(String usuario) throws RemoteException {
        boolean valido = false;
        I_Donaciones _replica = null;

        for (int j = 0; j < this.getRegistro().size() && !valido; j++) {
            if (this.getRegistro().get(j).getUser().equals(usuario)) {
                valido = true;
                _replica = this;
            }

        }

        for (int i = 0; i < this.replicas.size() && !valido; i++) {
            for (int j = 0; j < this.replicas.get(i).getRegistro().size() && !valido; j++) {
                if (this.replicas.get(i).getRegistro().get(j).getUser().equals(usuario)) {
                    valido = true;
                    _replica = this.replicas.get(i);
                }

            }
        }
        if (!valido)
            _replica = null;
        return _replica;
    }

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Error al invocar el programa. Son necesarios 2 argumentos. <IP> <0> u <1>");
            System.exit(0);
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            String nombre_objeto_remoto = "";
            if(Integer.parseInt(args[1]) == 0)
            {
                nombre_objeto_remoto = "Original";
            }
            if(Integer.parseInt(args[1]) == 1)
            {
                nombre_objeto_remoto = "Replica";
            }
            if(Integer.parseInt(args[1]) == 2)
            {
                nombre_objeto_remoto = "Replica2";
            }

            I_Donaciones servidor = new Servidor();
            I_Donaciones  stub = (I_Donaciones ) UnicastRemoteObject.exportObject(servidor, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(nombre_objeto_remoto, stub);

            if(Integer.parseInt(args[1]) == 1)
            {
                ((Servidor) servidor).enlazar(args[0],"Original",2);
            }

            if(Integer.parseInt(args[1]) == 2)
            {
                ((Servidor) servidor).enlazar(args[0],"Replica",3);
            }

            System.out.println("Servidor iniciado");
        } catch (Exception e) {
            System.err.println("Ejemplo exception:");
            e.printStackTrace();
        }


    }
}