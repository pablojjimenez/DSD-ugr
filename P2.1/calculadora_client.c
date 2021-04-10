#include <ctype.h>
#include <stdio.h>
#include "calculadora.h"

double
calculadora_1(char *host, double arg1, char *ope, double arg2)
{
	CLIENT *clnt;
	double *result_1;

#ifndef DEBUG
	clnt = clnt_create(host, CALCULADORA, CALC_BASIC, "udp");
	if (clnt == NULL)
	{
		clnt_pcreateerror(host);
		exit(1);
	}
#endif /* DEBUG */
	char o = ope[0];
	
	switch (o)
	{
	case '+':
		result_1 = suma_1(arg1, arg2, clnt);
		break;

	case '-':
		result_1 = resta_1(arg1, arg2, clnt);
		break;

	case 'x':
		result_1 = multiplicacion_1(arg1, arg2, clnt);
		break;

	case '/':
		result_1 = division_1(arg1, arg2, clnt);
		break;

	case '^':
		result_1 = potencia_1(arg1, arg2, clnt);
		break;

	default:
		printf("No está contemplada esta opción\n");
		exit(1);
		break;
	}

	return *result_1;

#ifndef DEBUG
	clnt_destroy(clnt);
#endif /* DEBUG */
}

void
calculadora_2(char *host, miArray arg1, char * ope, miArray arg2, int a)
{
	CLIENT *clnt;
	miArray *result_1;
	float *r;
	int *i;

#ifndef DEBUG
	clnt = clnt_create(host, CALCULADORA, CALC_COMP, "udp");
	if (clnt == NULL)
	{
		clnt_pcreateerror(host);
		exit(1);
	}
#endif /* DEBUG */
	char z = ope[0];
	switch (z)
	{
	case 'f':
		result_1 = fibonacci_2(a, clnt);
		
		printf("Solución: [");
		for(int i=0; i<result_1->miArray_len; i++)
			printf("%.0f, ", result_1->miArray_val[i]);
		printf("]\n");
		break;

	case '+':
		result_1 = suma_v_2(arg1, arg2, clnt);
		printf("Solución: [");
		for(int i=0; i<result_1->miArray_len; i++)
			printf("%.2f, ", result_1->miArray_val[i]);
		printf("]\n");
		break;

	case '-':
		result_1 = resta_v_2(arg1, arg2, clnt);
		printf("Solución: [");
		for(int i=0; i<result_1->miArray_len; i++)
			printf("%.2f, ", result_1->miArray_val[i]);
		printf("]\n");
		break;

	case 'x':
		result_1 = product_v_2(arg1, arg2, clnt);
		printf("Solución: [");
		for(int i=0; i<result_1->miArray_len; i++)
			printf("%.2f, ", result_1->miArray_val[i]);
		printf("]\n");
		break;

	case '.':
		printf(" ");
		int * h = product_escalar_2(arg1, arg2, clnt);
		printf("El producto escalar es: %d\n", *h);
		break;

	case 'm':
		printf(" calculando la media ");
		float * w = media_aritmetica_2(arg1, clnt);
		printf("La media es: %f\n", *w);
		break;

	default:
		printf("No está contemplada esta opción\n");
		exit(1);
		break;
	}

#ifndef DEBUG
	clnt_destroy(clnt);
#endif /* DEBUG */
}

void informarUso()
{
	printf("Uso: <servidor> <argumento> <operador> <argumento>\n");
	exit(1);
}

miArray cargarVector(char *n)
{
	FILE *fich;
	miArray v;

	fich = fopen(n, "r");
	fscanf(fich, "%d", &v.miArray_len);
	v.miArray_val = malloc(v.miArray_len * sizeof(float));

	for (int i = 0; i < v.miArray_len; i++)
		fscanf(fich, "%f", &v.miArray_val[i]);

	return v;
}

int opeSinVectores(char * operador) {
	int salida = 0;
	if(strcmp(operador, "+") == 0) {
		salida = 1;
	} else if(strcmp(operador, "-") == 0) {
		salida = 1;
	} else if(strcmp(operador, "x") == 0) {
		salida = 1;
	} else if(strcmp(operador, "/") == 0) {
		salida = 1;
	} else if(strcmp(operador, "r") == 0) {
		salida = 1;
	} else if(strcmp(operador, "^") == 0) {
		salida = 1;
	}
	return salida;
}

int main(int argc, char *argv[])
{
	char *host;
	char *arg1;
	char *arg2;
	char* operador;

	host = argv[1];
	arg1 = argv[2];
	operador = argv[3];
	arg2 = argv[4];
	miArray null;

	if( argc == 5 ) {
		if(opeSinVectores(operador) == 1) {
			double resultado = calculadora_1(host, atof(arg1), operador, atof(arg2));
			printf("%.2f %s %.2f = %.2f\n", atof(arg1), operador, atof(arg2), resultado);
		} else {
			miArray v1, v2;

			v1 = cargarVector(arg1);
			v2 = cargarVector(arg2);
			calculadora_2(host, v1, operador, v2, 0);
			
		}
	} else if (argc == 4) {
		
		if(strcmp(operador, "m") == 0) {
			miArray v1;
			
			v1 = cargarVector(arg1);
		
			calculadora_2(host, v1, operador, null, 0);
		} else if(strcmp(operador, "f") == 0) {
			
			calculadora_2(host, null, operador, null, atof(arg1));
		}
	} else {
		informarUso();
	}
	
	


	exit(0);
}
