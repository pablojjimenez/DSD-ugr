import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {

    public static final long serialVersionUID = 42L;

    private String nombre;
    private String pass;

    public Usuario(String nombre, String pass) {
        this.nombre = nombre;
        this.pass = pass;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (!Objects.equals(nombre, usuario.nombre)) return false;
        return Objects.equals(pass, usuario.pass);
    }

    @Override
    public int hashCode() {
        int result = nombre != null ? nombre.hashCode() : 0;
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
