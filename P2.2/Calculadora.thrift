service Calculadora {
    void ping (),
    double suma(1: double num1 , 2: double num2 ) ,
    double resta(1: double num1 , 2: double num2 ) ,
    double multiplicacion (1: double num1,2: double num2),
    double division(1: double num1,2: double num2),
    i32 factorial(1: i32 n),
    double potencia(1: double base, 2: double exponente),

    list<double> sumaVectores(1: list<double> arr1, 2:list<double> arr2), 
    double productoEscalar(1: list<double> arr1, 2: list<double> arr2),
    list<double> productoVectorial(1: list<double> arr1, 2:list<double> arr2),
    double mediaAritmetica(1: list<double> arr1),
    list<list<double>> productoEscalarMatrices(1: list<list<double>> m1 ,2:  double escalar),
}