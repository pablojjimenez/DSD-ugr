import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IDonaciones extends Remote {
    boolean registroEntidad(String nombre, String codigoAcceso) throws RemoteException;
    boolean donar(String nombre, double cantidad) throws RemoteException;
    Entidad entidadRegistrada(String nombre) throws RemoteException;
    IDonaciones getReplica() throws RemoteException;
    int getNumeroEntidades() throws RemoteException;
    void addEntidad(String nombre, String codigoAcceso) throws RemoteException;
    void incrementarSubtotal(double cantidad) throws RemoteException;
    double getSubtotal() throws RemoteException;
    double getTotal() throws RemoteException;
    boolean identificarse(String nombre, String codigoAcceso) throws RemoteException;
}