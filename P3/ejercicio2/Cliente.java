import java.awt.Color;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Cliente {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if (args.length < 2) {
            System.out.println("Error al invocar el programa. Son necesarios 2 argumentos. <IP operación>");
            System.exit(0);
        }
// <IP> <0 | 1 | 2 | 3>
        try {
            switch (Integer.parseInt(args[1])) {
                case 0:
                    String nombre_objeto_remoto = "Original";
                    System.out.println("Buscando el objeto servidor...");
                    Registry registry = LocateRegistry.getRegistry(args[0]);
                    I_Donaciones instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");

                    System.out.println("Primer usuario");
                    instancia_local.registrar("Miguel");
                    instancia_local.donar(new Aportacion("Miguel", 3000.0));
                    System.out.println("El total donado por los usuarios es " + instancia_local.getTotal("Miguel") + " euros");

                    System.out.println("Segundo usuario");
                    instancia_local.registrar("Juan");
                    instancia_local.donar(new Aportacion("Juan", 200.0));
                    System.out.println("El total donado por los usuarios es " + instancia_local.getTotal("Juan") + " euros");

                    System.out.println("Tercer usuario");
                    instancia_local.registrar("Jesus");
                    instancia_local.donar(new Aportacion("Jesus", 1000.0));
                    System.out.println("El total donado por los usuarios es " + instancia_local.getTotal("Jesus") + " euros");
                    break;
                case 1:
                    nombre_objeto_remoto = "Replica";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");

                    System.out.println("Primer usuario");
                    instancia_local.registrar("Antonio");
                    instancia_local.donar(new Aportacion("Antonio",3000.0));
                    System.out.println("El total donado por los usuarios es "+instancia_local.getTotal("Antonio")+" euros");

                    System.out.println("Segundo usuario");
                    instancia_local.registrar("Luisa");
                    instancia_local.donar(new Aportacion("Luisa",200.0));
                    System.out.println("El total donado por los usuarios es "+instancia_local.getTotal("Luisa")+" euros");

                    System.out.println("Tercer usuario");
                    instancia_local.registrar("Maria");
                    instancia_local.donar(new Aportacion("Maria",1000.0));
                    System.out.println("El total donado por los usuarios es "+instancia_local.getTotal("Maria")+" euros");

                    break;
                case 2:
                    nombre_objeto_remoto = "Original";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    System.out.println("Comprobando los usuarios que se han registrado en el servidor original");
                    System.out.println(instancia_local.getUsuarios());
                    break;

                case 3:
                    nombre_objeto_remoto = "Replica";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    System.out.println("Comprobando los usuarios que se han registrado en el servidor replicado");
                    System.out.println(instancia_local.getUsuarios());
                    break;
                case 6:
                    nombre_objeto_remoto = "Original";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    System.out.println("Comprobando el registro de donaciones del servidor original");
                    for(int i = 0; i < instancia_local.getRegistro().size(); i++)
                    {
                        System.out.println(instancia_local.getRegistro().get(i).getUser());
                        System.out.println(instancia_local.getRegistro().get(i).getCantidad());
                    }
                    System.out.println("Comprobando los usuarios del servidor original");
                    System.out.println(instancia_local.getUsuarios().size());
                    System.out.println("Comprobando el total del servidor original");
                    System.out.println(instancia_local.getTotalLocal());
                    System.out.println("Comprobando el tamaño del vector replicas del servidor original");
                    break;
                case 7:
                    nombre_objeto_remoto = "Replica";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    System.out.println("Comprobando el registro de donaciones del servidor replicado");

                    for(int i = 0; i < instancia_local.getRegistro().size(); i++)
                    {
                        System.out.println(instancia_local.getRegistro().get(i).getUser());
                        System.out.println(instancia_local.getRegistro().get(i).getCantidad());
                    }
                    System.out.println("Comprobando los usuarios del servidor replicado");
                    System.out.println(instancia_local.getUsuarios().size());
                    System.out.println("Comprobando el total del servidor replicado");
                    System.out.println(instancia_local.getTotalLocal());
                    System.out.println("Comprobando el tamaño del vector replicas del servidor replicado");

                    System.out.println("El total donado por los usuarios es "+instancia_local.getTotal("Miguel")+" euros");
                    break;
                case 8:
                    nombre_objeto_remoto = "Replica2";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    System.out.println("Comprobando el registro de donaciones del servidor replicado2");

                    for(int i = 0; i < instancia_local.getRegistro().size(); i++)
                    {
                        System.out.println(instancia_local.getRegistro().get(i).getUser());
                        System.out.println(instancia_local.getRegistro().get(i).getCantidad());
                    }
                    System.out.println("Comprobando los usuarios del servidor replicado2");
                    System.out.println(instancia_local.getUsuarios().size());
                    System.out.println("Comprobando el total del servidor replicado2");
                    System.out.println(instancia_local.getTotalLocal());
                    System.out.println("Comprobando el tamaño del vector replicas del servidor replicado2");
                    break;
                case 9:
                    nombre_objeto_remoto = "Replica2";
                    System.out.println("Buscando el objeto servidor...");
                    registry = LocateRegistry.getRegistry(args[0]);
                    instancia_local = (I_Donaciones) registry.lookup(nombre_objeto_remoto);
                    System.out.println("Invocando el objeto servidor");
                    ArrayList<Aportacion> aux = instancia_local.getRegistroTotal();
                    for(int i= 0; i < aux.size(); i++)
                        System.out.println("Usuario: "+aux.get(i).getUser() +" Cantidad donada: "+aux.get(i).getCantidad());
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al invocar el servidor " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}