import sys
import glob

from Calculadora import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol



transport = TSocket.TSocket('127.0.0.1',9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Client(protocol)

transport.open()

print("Hacemos ping al server")
client.ping()

resultado = client.suma(1,1)
print("1+1="+str(resultado))

resultado = client.resta(1,1)
print("1-1="+str(resultado))

resultado = client.multiplicacion(5,5)
print("5*5="+str(resultado))

resultado = client.division(5,5)
print("5/5="+str(resultado))

resultado = client.potencia(5,5)
print("5^5="+str(resultado))

resultado = client.factorial(5)
print("5! = "+str(resultado))

lista1 = [1,2,3,4,5]
lista2 = [1,2,3,4,5]

print('Sumando vectores'+ str(lista1)+' y '+str(lista2))
resultado = client.sumaVectores(lista1,lista2)
print("vector suma="+str(resultado))

print('Prodcuto escalar'+ str(lista1)+ ' por 5')
resultado = client.productoEscalar(lista1, lista1)
print("resultado="+str(resultado))

arr1 = [1,2,3]
arr2 = [4,9,5]

print('Producto vectorial'+ str(arr1)+' y '+str(arr2))
resultado = client.productoVectorial(arr1,arr2)
print("resultado="+str(resultado))

m1 = [[1,2,3],[4,5,6],[7,8,9]]
m2 = [[1,2,3],[4,5,6],[7,8,9]] 

m3 = [[7,4,8],[1,7,6],[7,20,3]]

print('Producto escalar matrices'+ str(m3)+' y  2')
resultado = client.productoEscalarMatrices(m3,2)
print("resultado="+str(resultado))

print('FIN DE LAS OPERACIONES')
transport.close()

