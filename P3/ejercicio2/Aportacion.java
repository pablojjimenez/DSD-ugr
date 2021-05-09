
import java.io.Serializable;

/**
 * Clase modelo para representar las aportaciones.
 */
public class Aportacion implements Serializable {
    private String usuario;
    private double cantidad;

    Aportacion(String user, double cant) {
        this.usuario = user;
        this.cantidad = cant;
    }

    public String getUser() {
        return this.usuario;
    }

    public void setUser(String user) {
        this.usuario = user;
    }

    public double getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(double apor) {
        this.cantidad = apor;
    }

    @Override
    public String toString() {
        return String.format("Aportacion{usuario=%s, aportacion=%s}", this.usuario, this.cantidad);
    }
}
