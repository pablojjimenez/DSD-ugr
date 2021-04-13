import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Client {

    @SuppressWarnings("empty-statement")
	public static void main(String[] args) {
	    try {
	      TTransport transport = new TSocket("localhost", 9090);
	      transport.open();

	      TProtocol protocol = new  TBinaryProtocol(transport);
	      Calculadora.Client client = new Calculadora.Client(protocol);
              
              System.out.println("Haciendo ping");
	      client.ping();
              
              System.out.println("5 + 5 = "+client.suma(5, 5));
              System.out.println("1 - 1 = "+client.resta(1, 1));
              System.out.println("7 * 8 = "+client.multiplicacion(7, 8));
              System.out.println("81/9 = "+client.division(81, 9));
              System.out.println("14^2 = "+client.potencia(14,2));
              System.out.println("!7 = "+client.factorial(7));
              

	
              ArrayList lista1 = new ArrayList();
              lista1.add(1.0);
              lista1.add(2.0);
              lista1.add(8.0);
              lista1.add(1.0);
              lista1.add(89.0);
              
              	
              ArrayList lista2 = new ArrayList();
              lista2.add(7.0);
              lista2.add(2.0);
              lista2.add(1.0);
              lista2.add(14.0);
              lista2.add(3.0);
              
              System.out.println("Suma de listas");
              System.out.println("Lista 1: "+lista1);
              System.out.println("Lista 2: "+lista2);
              ArrayList resultado = new ArrayList();
              resultado = (ArrayList) client.sumavectores(lista1, lista2);
              System.out.println("Lista resultado: "+resultado);
              
              System.out.println("Producto escalar de la lista resultado por 14");
              resultado = (ArrayList) client.productoescalar(resultado, 14.0);
              System.out.println("Lista resultado: "+resultado);
              
              System.out.println("Producto vectorial de la lista1 y la lista2");
              resultado = (ArrayList) client.productovectorial(lista1, lista2);
              System.out.println("Lista resultado: "+resultado);
              

              
	      transport.close();
	    } catch (TException e) {
	      e.printStackTrace();
	    } 
	}
}