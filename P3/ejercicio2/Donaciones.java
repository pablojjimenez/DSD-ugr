import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Donaciones extends UnicastRemoteObject implements I_Donaciones {
    private ArrayList<Entidad> entidades = new ArrayList<>();
    private double subtotal = 0;
    private String nombreReplica = "";

    /* Constructor */
    public Donaciones(String nombreReplica) throws RemoteException {
        this.nombreReplica = nombreReplica;
    }

    /* Registro de la entidad */
    @Override
    public boolean registroEntidad(String nombre, String codigoAcceso) throws RemoteException {
        /* Comprobamos que el cliente no exista en la replica */
        I_Donaciones replica = this.getReplica();

        if(replica != null) {
            if(replica.entidadRegistrada(nombre) != null)
                return false;
        }

        /* Comprobamos que la entidad no esté registrada en el servidor local */
        if(this.entidadRegistrada(nombre) != null)
            return false;

        /* Ahora escogemos en qué replica alojar la entidad */
        boolean replicaLocal = false;

        if(this.getNumeroEntidades() <= replica.getNumeroEntidades())
            replicaLocal = true;
        else
            replicaLocal = false;

        if(replicaLocal)
            this.addEntidad(nombre, codigoAcceso);
        else
            replica.addEntidad(nombre, codigoAcceso);

        System.out.println("Entidad " + nombre + " registrada con éxito");
        return true;
    }

    /* Donación por parte de una entidad */
    @Override
    public boolean donar(String nombre, double cantidad) throws RemoteException {
        /* Comprobamos que el cliente no exista en la replica */
        I_Donaciones replica = this.getReplica();
        boolean existeEnReplica = false, existeEnLocal = false;

        if(replica != null) {
            if(replica.entidadRegistrada(nombre) != null)
                existeEnReplica = true;
        }

        if(!existeEnReplica) {
            /* Comprobamos si existe en la réplica local */
            if(this.entidadRegistrada(nombre) != null)
                existeEnLocal = true;
        }

        if(existeEnReplica) {
            Entidad encontrada = replica.entidadRegistrada(nombre);
            encontrada.incrementarTotal(cantidad);
            replica.incrementarSubtotal(cantidad);

            System.out.println("Entidad " + nombre + " ha donado " + cantidad + "€");
            return true;
        }

        if(existeEnLocal) {
            Entidad encontrada = this.entidadRegistrada(nombre);
            encontrada.incrementarTotal(cantidad);
            this.incrementarSubtotal(cantidad);
            System.out.println("Entidad " + nombre + " ha donado " + cantidad + "€");
            return true;
        }

        return false;
    }

    /* Obtener una entidad registrada en la réplica actual */
    @Override
    public Entidad entidadRegistrada(String nombre) throws RemoteException {
        for(int i = 0; i < this.entidades.size(); i++) {
            if(this.entidades.get(i).getNombre().equals(nombre)) {
                return this.entidades.get(i);
            }
        }

        return null;
    }

    /* Obtener la réplica */
    @Override
    public I_Donaciones getReplica() throws RemoteException {
        I_Donaciones replica = null;

        try {
            Registry mireg = LocateRegistry.getRegistry("localhost", 1099);
            replica = (I_Donaciones)mireg.lookup(this.nombreReplica);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }

        return replica;
    }

    /* Obtener número de entidades */
    @Override
    public int getNumeroEntidades() throws RemoteException {
        return this.entidades.size();
    }

    /* Añadir Entidad a la lista de entidades */
    @Override
    public void addEntidad(String nombre, String codigoAcceso) throws RemoteException {
        this.entidades.add(new Entidad(nombre, codigoAcceso));
    }

    /* Incrementar el subtotal del servidor */
    @Override
    public void incrementarSubtotal(double cantidad) throws RemoteException {
        this.subtotal += cantidad;
    }

    /* Obtener el subtotal de la réplica actual */
    @Override
    public double getSubtotal() throws RemoteException {
        return this.subtotal;
    }

    /* Obtener el total donado de todas las réplicas */
    @Override
    public double getTotal() throws RemoteException {
        I_Donaciones replica = this.getReplica();
        boolean existeEnReplica = false, existeEnLocal = false;

        if(replica != null) {
            if(this.getSubtotal() == 0.0 && replica.getSubtotal() == 0.0) {
                System.out.println("Aún no se ha realizado ninguna donación");
                return 0.0;
            }
        }

        double total = this.getSubtotal() + replica.getSubtotal();

        return total;
    }

    /* Función para identificarse */
    @Override
    public boolean identificarse(String nombre, String codigoAcceso) throws RemoteException {
        I_Donaciones replica = this.getReplica();
        boolean existeEnReplica = false;

        if(replica != null) {
            /* ¿Está en la réplica? */
            if(replica.entidadRegistrada(nombre) != null)
                existeEnReplica = true;
        }

        /* Si está en la réplica, comprobamos que los datos de inicio de sesión sean válidos */
        if(existeEnReplica) {
            Entidad encontrada = replica.entidadRegistrada(nombre);

            if(encontrada.getNombre().equals(nombre) && encontrada.getCodigoAcceso().equals(codigoAcceso)) {
                return true;
            }
        }

        boolean existeLocal = false;

        /* ¿Existe en local? */
        if(this.entidadRegistrada(nombre) != null)
            existeLocal = true;

        /* Si está en local, comprobamos que los datos de inicio de sesión sean correctos */
        if(existeLocal) {
            Entidad encontrada = this.entidadRegistrada(nombre);

            if(encontrada.getNombre().equals(nombre) && encontrada.getCodigoAcceso().equals(codigoAcceso)) {
                return true;
            }
        }

        /* En cualquier otro caso devolvemos false */
        return false;
    }
}