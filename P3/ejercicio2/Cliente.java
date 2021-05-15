import java.rmi.registry.Registry;
import java.util.Scanner;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Cliente {

    public static Usuario usuario;
    private static boolean userInTheSys = false;
    private final Scanner teclado;
    private final I_Donaciones gestor;
    private static final int FIN = 8;

    public Cliente(I_Donaciones gestor) {
        this.usuario = null;
        this.gestor = gestor;
        teclado = new Scanner(System.in);
    }

    public void cerrarTeclado() {
        this.teclado.close();
    }

    public static void iniciaSesion(Usuario u) {
        userInTheSys = true;
        Cliente.usuario = u;
    }

    public static void cerrarSesion() {
        userInTheSys = false;
        Cliente.usuario = null;
    }

    public void registro() throws RemoteException {
        System.out.print("Escriba su nombre de usuario: ");
        String usuarioT = teclado.nextLine();
        System.out.print("Escriba su contraseña: ");
        String pass = teclado.nextLine();
        iniciaSesion(new Usuario(usuarioT, pass));
        Boolean existe = gestor.registro(Cliente.usuario);

        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));

        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));
    }

    public void iniciarSesion() throws RemoteException {
        System.out.print("Escriba su nombre de usuario: ");
        String usuarioT = teclado.nextLine();
        System.out.print("Escriba su contraseña: ");
        String pass = teclado.nextLine();
        Usuario u = new Usuario(usuarioT, pass);
        Boolean existe = gestor.iniciarSesion(u);
        if (existe) {
            iniciaSesion(u);
            System.out.println(C.mgeInformativo("Has iniciado sesión en el sistema"));
        } else
            System.out.println(C.mgeErr("El usuario no se encuentra registrado"));
    }

    public void donar() throws RemoteException {
        System.out.print("Escriba la cantidad de dinero a donar: ");
        String donacion = teclado.nextLine();
        double donacionD = -1;
        try {
            donacionD = Double.parseDouble(donacion);
        } catch (NumberFormatException e) {
            System.err.println(C.mgeErr("Debe de ser un número real reconocible"));
        }

        Boolean existe = gestor.donar(Cliente.usuario, donacionD);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));

    }

    public void consultarTotal() throws RemoteException {
        Double totalDonado = gestor.getTotal();
        System.out.println(C.mgeInformativo("El total donado hasta ahora es de " + totalDonado.toString() + " €"));
    }

    public void consultarUsuario() throws RemoteException {
        if (Cliente.usuario != null) {
            Double donacionUsuario = gestor.getUsuario(Cliente.usuario);
            System.out.println(C.mgeInformativo(Cliente.usuario + " ha donado " + donacionUsuario.toString() + " €"));
        } else {
            System.out.println(C.mgeErr("Debe estar registrado en el sistema"));
        }
    }

    public void obtenerLogServidor() throws RemoteException {
        System.out.println(C.mgeInformativo("Se ha volcado la información en un fichero de texto"));
        gestor.volcarLogServer();
    }

    private static int menu() {
        final Scanner teclado = new Scanner(System.in);
        int opcion;
        final StringBuilder menu = new StringBuilder();
        menu.append(C.CYAN_BOLD).append("\nELIGA UNA OPCIÓN:\n");
        if (userInTheSys) {
            menu.append(C.RESET).append(C.BLUE_BACKGROUND_BRIGHT).append("El usuario: ").append(usuario.getNombre())
                    .append(" esta iniciado en el sistema").append(C.RESET);
        }
        menu.append("\n\t1: Registro de usuario");
        menu.append("\n\t2: Iniciar sesión");
        menu.append("\n\t3: Donar");
        menu.append("\n\t4: Consultar total donado");
        menu.append("\n\t5: Consultar donacion del usuario");
        menu.append("\n\t6: Obtener información servidor");
        menu.append("\n\t7: Cerrar sesión");
        menu.append("\n\t8: Salir");
        menu.append(C.RESET);
        do {
            System.out.println(menu.toString());
            System.out.print(C.CYAN_BOLD + "~~>" + C.RESET);
            opcion = Integer.parseInt(teclado.nextLine());

        } while (!(opcion >= 1 && opcion <= FIN));
        return opcion;
    }

    public static void main(final String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {

            final Registry registry = LocateRegistry.getRegistry("localhost");
            final I_Donaciones gestor = (I_Donaciones) registry.lookup("servidor0");

            Cliente cliente = new Cliente(gestor);

            if (args[0] != null) {
                Cliente.modoLotes(cliente, gestor);
            } else {
                Cliente.modoUsuario(cliente);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void modoUsuario(Cliente cliente) throws RemoteException {
        System.out.printf("%s %s %s %n", C.GREEN_UNDERLINED, "GESTIÓN DE DONACIONES", C.RESET);
        int opc = -1;
        do {
            opc = Cliente.menu();
            switch (opc) {
                case 1:
                    cliente.registro();
                    break;
                case 2:
                    cliente.iniciarSesion();
                    break;
                case 3:
                    cliente.donar();
                    break;
                case 4:
                    cliente.consultarTotal();
                    break;
                case 5:
                    cliente.consultarUsuario();
                    break;
                case 6:
                    cliente.obtenerLogServidor();
                    break;
                case 7:
                    Cliente.cerrarSesion();
                    System.out.println(C.mgeInformativo("Se ha cerrado sesión"));
                case 8:
                    System.out.println(C.mgeInformativo("Gracias por usar nuestros servicios"));
                    break;
                default:
                    System.out.println(C.mgeErr("Debe elegir una opcion correcta"));
                    break;
            }
        } while (opc != FIN);

        cliente.cerrarTeclado();
    }

    public static void modoLotes(Cliente cliente, I_Donaciones gestor) throws RemoteException {
        System.out.println(C.GREEN_BACKGROUND_BRIGHT + " INICIADO EL MODO POR LOTES DE PRUEBA " + C.RESET);

        iniciaSesion(new Usuario("Usuario 1", "123"));
        Boolean existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 45);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));

        iniciaSesion(new Usuario("Usuario 2", "123"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 67);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));

        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 3", "123"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 12);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 4", "123"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 120);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 5", "123"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 57);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 6", "345g"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 46);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 7", "gdr"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 46);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        // nuevo usuario-----------------------------------------
        iniciaSesion(new Usuario("Usuario 8", "1243653"));
        existe = gestor.registro(Cliente.usuario);
        if (existe)
            System.out.println(C.mgeErr("El usuario ya existe"));
        System.out.println(C.mgeInformativo("Se ha iniciado sesión con el usuario: " + usuario));

        existe = gestor.donar(Cliente.usuario, 112);
        if (existe)
            System.out.println(C.mgeInformativo("La donacion se ha realizado con éxito"));
        else
            System.out.println(C.mgeErr("No está registrado en el sistema"));
        // -----------------------------------------------------------------------
        System.out.println(C.mgeInformativo("Se ha volcado la información en un fichero de texto"));
        gestor.volcarLogServer();

        System.out.println(C.PURPLE_BACKGROUND_BRIGHT + "se continua con el modo interactivo...." + C.RESET);
        modoUsuario(cliente);
    }
}