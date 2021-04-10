/**
 * rm -rf *.o *.c *.h calculadora_server calculadora_client Makefile.calculadora
 * rpcgen -NCa calculadora.x
 */

typedef float miArray<>;

program CALCULADORA {

    version CALC_BASIC {
        double suma(double, double) = 1;
        double resta(double, double) = 2;
        double multiplicacion(double, double) = 3;
        double division(double, double) = 4;
        double potencia(double, double) = 5;
    } = 1;

    version CALC_COMP {
        miArray fibonacci(int) = 1;
        miArray suma_v(miArray, miArray) = 2;
        miArray resta_v(miArray, miArray) = 3;
        miArray product_v(miArray, miArray) = 4;
        int product_escalar(miArray, miArray) = 5;
        float media_aritmetica(miArray v) = 6;
    } = 2;

} = 0x20000001;
