
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Pablo Jj
 */
public interface I_Ejemplo2 extends Remote {
    void escribirMensaje(String mensaje) throws RemoteException;
}
