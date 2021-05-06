
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Pablo Jj
 */
public interface Ejemplo_I extends Remote {
    void escribirMensaje (int id_proceso) throws RemoteException;
}
