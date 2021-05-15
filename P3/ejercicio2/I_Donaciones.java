import java.rmi.Remote;
import java.rmi.RemoteException;


public interface I_Donaciones extends Remote {
    public Boolean registro(Usuario u) throws RemoteException;
    public Boolean iniciarSesion(Usuario u) throws RemoteException;
    public Boolean donar(Usuario usuario, double donacion) throws RemoteException;
    public Double getTotal() throws RemoteException;
    public Double getUsuario(Usuario u) throws RemoteException;
    public void volcarLogServer() throws RemoteException;
}