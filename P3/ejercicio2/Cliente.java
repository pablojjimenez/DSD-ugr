import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String servidor1 = "ddonaciones1";
        String servidor2 = "ddonaciones2";
        int servidorEscogido = 0;
        String newLine = System.getProperty("line.separator");
        Scanner in = new Scanner(System.in);
        boolean isIdentificado = false;

        // Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            // Crea el stub para el cliente especificando el nombre del servidor
            IDonaciones donaciones1 = (IDonaciones)Naming.lookup(servidor1);
            IDonaciones donaciones2 = (IDonaciones)Naming.lookup("ddonaciones2");
            boolean comienzo = true;

            /* Variables para dentro de los bucles */
            String nombreInicioSesion = "";
            String codigoInicioSesion = "";

            while(comienzo) {
                System.out.println("Bienvenido al Sistema de Donaciones. Por favor, selecciona una opción:" + newLine +
                                "   R: Registrarse" + newLine +
                                "   I: Iniciar Sesión" + newLine +
                                "   S: Salir");
            
                String opcionInicial = in.nextLine();
                
                switch(opcionInicial) {
                    case "R":
                        System.out.println("Introduzca un nombre para la Entidad: ");
                        String nombreEntidad = "in.nextLine()";

                        System.out.println("Introduzca un código de acceso: ");
                        String codigoAcceso = "in.nextLine()";

                        System.out.println("¿En qué servidor desea registrarse? (1 o 2): ");
                        servidorEscogido = 2;

                        if(servidorEscogido == 1) {
                            if(donaciones1.registroEntidad(nombreEntidad, codigoAcceso)) {
                                System.out.println("Registro completado en el servidor " + servidorEscogido);
                            }

                            else {
                                System.out.println("La entidad ya está registrada. Pruebe con otro nombre.");
                            }
                        }

                        else if(servidorEscogido == 2) {
                            if(donaciones2.registroEntidad(nombreEntidad, codigoAcceso)) {
                                System.out.println("Registro completado en el servidor " + servidorEscogido);
                            }

                            else {
                                System.out.println("La entidad ya está registrada. Pruebe con otro nombre.");
                            }
                        }

                        else {
                            System.out.println("El número de servidor no es correcto. Debe de ser 1 o 2.");
                        }
                    break;

                    case "I":
                        System.out.println("Menú de Inicio de Sesión. Introduzca su nombre de Entidad: ");
                        nombreInicioSesion = in.nextLine();

                        System.out.println("Introduzca su código de acceso: ");
                        codigoInicioSesion = in.nextLine();

                        /* Comprobamos en cualquiera de los dos servidores */
                        if(donaciones1.identificarse(nombreInicioSesion, codigoInicioSesion)) {
                            System.out.println("Identificado correctamente. Bienvenido.");
                            isIdentificado = true;
                        }

                        else {
                            System.out.println("No existe ninguna Entidad con los datos proporcionados. Inténtelo de nuevo.");
                            isIdentificado = false;
                        }
                    break;

                    case "S":
                        System.out.println("Saliendo del sistema.");
                        comienzo = false;
                        isIdentificado = false;
                    break;

                    default:
                        System.out.println("La opción no es válida. Pruebe de nuevo.");
                    break;
                }

                while(isIdentificado) {
                    System.out.println("Bienvenido al Sistema de Donaciones. Usted ha Iniciado Sesión. Por favor, selecciona una opción:" + newLine +
                            "   D: Donar" + newLine +
                            "   T: Obtener Total Donado" + newLine +
                            "   S: Salir");
                    
                    String opcionIdentificado = in.nextLine();

                    switch(opcionIdentificado) {
                        case "D":
                            System.out.println("Introduzca la cantidad a donar: ");

                            int cantidad = Integer.parseInt(in.nextLine());
                            while(cantidad < 0.0) {
                                if(cantidad < 0.0)
                                    System.out.println("La cantidad debe de ser mayor que 0.0€");

                                cantidad = Integer.parseInt(in.nextLine()); 
                            }

                            
                            if(donaciones1.donar(nombreInicioSesion, cantidad)) {
                                System.out.println("La donación se completado con éxito");
                            }
                        break;

                        case "T":
                            System.out.println("Obteniendo cantidad total donada por todas las entidades...");
                            System.out.println("Total donado: " + donaciones1.getTotal() + "€");
                        break;

                        case "S":
                            System.out.println("Saliendo de la sesión iniciada...");
                            isIdentificado = false;
                            comienzo = true;
                        break;
                    }    
                }
            }

            
        } catch(NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        System.exit(0);
    }
}