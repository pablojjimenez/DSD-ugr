import sys
import glob

from Calculadora import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

def cargarVectores(ficheroName):
    with open(ficheroName, 'r') as f:
        l = f.readlines()[0].split(' ')
    return list(map(float, l))

def cargarMatriz(ficheroName):
    with open(ficheroName, 'r') as f:
        l = f.readlines()[0]
        l = l.split(',')
        v = []
        for s in l:
            v.append(list(map(float, s.split(' '))))
    return v



def switch(argument, x, y):
    
    res = 'OPERACIÓN INCORRECTA'

    if argument == '+':
        x = float(x)
        y = float(y)
        res = client.suma(x, y)
    elif argument == '-':
        x = float(x)
        y = float(y)
        res = client.resta(x, y)
    elif argument == 'x':
        x = float(x)
        y = float(y)
        res = client.multiplicacion(x, y)
    elif argument == '/':
        x = float(x)
        y = float(y)
        res = client.division(x, y)
    elif argument == '^':
        x = float(x)
        y = float(y)
        res = client.potencia(x, y)
    elif argument == '!':
        x = int(x)
        res = client.factorial(x)
    elif argument == '+v':
        res = client.sumaVectores(x, y)
    elif argument == 'xv':
        res = client.productoVectorial(x, y)
    elif argument == '.':
        
        res = float(client.productoEscalar(x, y))

    elif argument == 'm':
        res = client.mediaAritmetica(x)
    elif argument == '.m':
        y = float(y)
        res = client.productoEscalarMatrices(x, y)

    return res

# ------------------------------------------------------------------------
# ------------------------------------------------------------------------
transport = TSocket.TSocket('127.0.0.1',9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Client(protocol)

transport.open()
try:
    operador = sys.argv[2]
    if len(sys.argv) == 4:

        if operador == '.':
            op1 = sys.argv[1]
            v1 = cargarVectores(op1)
            op2 = sys.argv[3]
            v2 = cargarVectores(op1)
            
            print(f'{v1} {operador[0]} {v2} = {switch(operador, v1, v2)}')

        elif operador == '.m':
            op1 = sys.argv[1]
            v1 = cargarMatriz(op1)
            op2 = float(sys.argv[3])
            print(f'{v1} x {op2} = {switch(operador, v1, op2)}')


        elif len(operador) == 2:
            op1 = sys.argv[1]
            op2 = sys.argv[3]
            v1 = cargarVectores(op1)
            v2 = cargarVectores(op2)
            print(f'{v1} {operador[0]} {v2} = {switch(operador, v1, v2)}')

        else:
            op1 = float(sys.argv[1])
            op2 = float(sys.argv[3])
            print(f'{op1} {operador} {op2} = {switch(operador, op1, op2)}')

    elif len(sys.argv) == 3:
        if operador == 'm':
            op1 = sys.argv[1]
            v1 = cargarVectores(op1)
            print(f'{v1} {operador} = {switch(operador, v1, 0)}')

        elif operador == '!':
            op1 = float(sys.argv[1])
            print(f'{op1} {operador} = {switch(operador, op1, 0)}')


    transport.close()
except:
    print('instrucciones incorrectas...')
    print('\tPython3 cliente.py <n> + <n>')
    print('\tPython3 cliente.py <n> - <n>')
    print('\tPython3 cliente.py <n> x <n>')
    print('\tPython3 cliente.py <n> / <n>')
    print('\tPython3 cliente.py <n> !')
    print('\tPython3 cliente.py <file> +v <file>')
    print('\tPython3 cliente.py <file> xv <file>')
    print('\tPython3 cliente.py <file> . <file>')
    print('\tPython3 cliente.py <file> m')
    print('\tPython3 cliente.py <file> .m <n>')