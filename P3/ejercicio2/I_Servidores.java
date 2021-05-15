import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface I_Servidores extends Remote {
    public I_Servidores getReplica() throws RemoteException;
    public Boolean existeUsuario(Usuario u) throws RemoteException;
    public int getSize() throws RemoteException;
    public void introducirUsuario(Usuario u) throws RemoteException;
    public Double getSubtotal() throws RemoteException;
    public void sumarDonacion(Usuario usuario, double donacion) throws RemoteException;
    public Double getDonacionUsuario(Usuario u) throws RemoteException;
    public HashMap getClientes() throws RemoteException;
}