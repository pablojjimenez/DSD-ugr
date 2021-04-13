import glob
import sys


from Calculadora import *

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import logging
logging.basicConfig(level=logging.DEBUG)

class CalculadoraHandler:
    def __init__(self):
        self.log = {}
    
    def ping(self):
        print('haciendo ping....')
    
    def suma(self, n1, n2):
        print(f'{n1} + {n2}')
        return n1 + n2
    
    def resta(self, n1, n2):
        print(f'{n1} - {n2}')
        return n1-n2
    
    def multiplicacion(self,n1,n2):
        print(f'{n1} * {n2}')
        return n1 * n2

    def division(self,n1,n2):
        print(f'{n1} / {n2}')
        return n1 / n2

    def potencia(self,n1,n2):
        print('{n1}^{n2}')
        return n1 ** n2
    
    def factorial(self, n):
        print(f'{n}!')
        if n < 0:
            resultado = 0
        else:
            if n == 0:
                resultado = 1
            else:
                resultado = 1
                for x in range(1,n+1):
                    resultado *= x
        
        return resultado
   

    def sumaVectores(self,arr1,arr2):
        print('Sumando vectores...')
        res = []
        for i in range(len(arr1)):
            res.append(arr1[i] + arr2[i])
        return res
    
    def productoEscalar(self, arr1, arr2):
        print('Producto escalar de vectores...')
        print(arr1, arr2)
        res = 0
        if len(arr1) == len(arr2):
            for i in range(len(arr1)):
                res += arr1[i] * arr2[i]

        return float(res)
    
    def productoVectorial(self,arr1,arr2):
        print('Producto vectorial')
        res = []
        if len(arr1) == len(arr2):
            for i in range(len(arr1)):
                res.append(arr1[i] * arr2[i])
        return res
    
    def mediaAritmetica(self, m1):
        print('Media aritmeta')
        res = 0
        for i in m1:
            res += i
        res = float(res / len(m1))
        return float(res)
        

    def productoEscalarMatrices(self,m1,escalar):
        mResultado = []
        
        for i in range(len(m1)):
            mResultado.append([])
        
        for a in range(len(m1)):
            for b in range(len(m1)):
                mResultado[a].append(m1[a][b] * escalar)
    
        return mResultado

if __name__ == '__main__':
    handler = CalculadoraHandler()
    processor = Processor(handler)
    transport = TSocket.TServerSocket(host='127.0.0.1', port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print('Iniciando servidor')
    server.serve()
    print('Done')
