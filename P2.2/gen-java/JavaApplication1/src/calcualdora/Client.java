package calcualdora;

import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import calcualdora.Calculadora;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author pablojj
 */
public class Client {

    private static Scanner in = new Scanner(System.in);

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        try {
            TTransport transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            Calculadora.Client client = new Calculadora.Client(protocol);

            System.out.println("Haciendo ping");
            client.ping();

            double a, b;
            String v_1, v_2;
            ArrayList v1, v2;
            switch (menu()) {
                case 1:
                    System.out.println("Introduce los dos operandos: ");
                    a = in.nextDouble();
                    b = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f + %.2f = %.2f%n", a, b, client.suma(a, b));
                    break;
                case 2:
                    System.out.println("Introduce los dos operandos: ");
                    a = in.nextDouble();
                    b = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f - %.2f = %.2f%n", a, b, client.resta(b, b));
                    break;
                case 3:
                    System.out.println("Introduce los dos operandos: ");
                    a = in.nextDouble();
                    b = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f x %.2f = %.2f%n", a, b, client.multiplicacion(a, b));
                    break;
                case 4:
                    System.out.println("Introduce los dos operandos: ");
                    a = in.nextDouble();
                    b = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f / %.2f = %.2f%n", a, b, client.division(a, b));
                    break;
                case 5:
                    System.out.println("Introduce número: ");
                    a = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f! = %d%n", a, client.factorial((int) a));
                    break;
                case 6:
                    System.out.println("Introduce base y potencia: ");
                    a = in.nextDouble();
                    b = in.nextDouble();
                    in.nextLine();
                    System.out.printf("%.2f ^ %.2f = %.2f%n", a, b, client.potencia(a, b));
                    break;
                case 7:
                    in.nextLine();
                    System.out.println("Introduce nombre de los ficheros de los vectores: ");
                    v_1 = in.nextLine();
                    v_2 = in.nextLine();
                    v1 = cargarVector(v_1);
                    v2 = cargarVector(v_2);
                    System.out.printf("%s + %s = %s%n", v1, v2, client.sumaVectores(
                            v1, v2
                    ));
                    break;
                case 8:
                    in.nextLine();
                    System.out.println("Introduce nombre de los ficheros de los vectores: ");
                    v_1 = in.nextLine();
                    v_2 = in.nextLine();
                    v1 = cargarVector(v_1);
                    v2 = cargarVector(v_2);
                    System.out.printf("%s * %s = %s%n", v1, v2, client.productoEscalar(
                            v1, v2
                    ));
                    break;
                case 9:
                    in.nextLine();
                    System.out.println("Introduce nombre de los ficheros de los vectores: ");
                    v_1 = in.nextLine();
                    v_2 = in.nextLine();
                    v1 = cargarVector(v_1);
                    v2 = cargarVector(v_2);
                    System.out.printf("%s x %s = %s%n", v1, v2, client.productoVectorial(
                            v1, v2
                    ));
                    break;
                case 10:
                    in.nextLine();
                    System.out.println("Introduce nombre del fichero con el vector: ");
                    v_1 = in.nextLine();
                    v1 = cargarVector(v_1);
                    System.out.printf("%s media = %s%n", v1, client.mediaAritmetica(v1));
                    break;
                case 11:
                    in.nextLine();
                    System.out.println("Introduce nombre con la matriz y el escalar: ");
                    v_1 = in.nextLine();
                    v1 = cargarMatriz(v_1);
                    a = in.nextDouble();
                    System.out.printf("%s * %.2f %n = %s%n", v1, a, client.productoEscalarMatrices(v1, a));
                    break;
            }

            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static int menu() {
        in = new Scanner(System.in);
        int opc = -1;
        do {
            System.out.println("Indique la operación que quiera realizar:");
            System.out.println("1. Sumar números");
            System.out.println("2. Restar números");
            System.out.println("3. Multiplicar números");
            System.out.println("4. Dividir números");
            System.out.println("5. Factorial de un número");
            System.out.println("6. Potencia números");
            System.out.println("7. Sumar vectores");
            System.out.println("8. Producto escalar de vectores");
            System.out.println("9. Producto vectorial de vectores");
            System.out.println("10. Media aritmética de vectores");
            System.out.println("11. Producto escalar de matriz y entero");
            System.out.print("~> ");
            opc = in.nextInt();
        } while (opc < 1 && opc > 11);
        return opc;
    }

    public static ArrayList cargarVector(String fichero) {
        ArrayList<Double> v = new ArrayList<>();
        FileReader fr = null;
        try {
            fr = new FileReader(new File(fichero));
            BufferedReader br = new BufferedReader(fr);
            String linea = null;
            String[] numeros = null;

            while ((linea = br.readLine()) != null) {
                numeros = linea.split(" ");
            }

            for (String i : numeros) {
                v.add(Double.parseDouble(i));
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return v;
    }

    private static ArrayList cargarMatriz(String v_1) {
        ArrayList<ArrayList<Double>> v = new ArrayList<>();
        ArrayList<Double> aux = new ArrayList<>();
        FileReader fr = null;
        try {
            fr = new FileReader(new File(v_1));
            BufferedReader br = new BufferedReader(fr);

            String linea = null;
            String[] numeros = null;

            while ((linea = br.readLine()) != null) {
                numeros = linea.split(","); //['1 2 3', '4 5 6', '7 8 9']
            }
            for (String i : numeros) {
                String[] _v = i.split(" ");
                for (String s : _v) {
                    aux.add(Double.parseDouble(s));
                }
                v.add(aux);
                aux = new ArrayList<>();
            }

            br.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return v;
    }
}
