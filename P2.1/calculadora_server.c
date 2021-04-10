#include "calculadora.h"

double *
suma_1_svc(double arg1, double arg2,  struct svc_req *rqstp)
{
	static double  result;

	result = arg1 + arg2;

	return &result;
}

double *
resta_1_svc(double arg1, double arg2,  struct svc_req *rqstp)
{
	static double  result;

	result = arg1 - arg2;

	return &result;
}

double *
multiplicacion_1_svc(double arg1, double arg2,  struct svc_req *rqstp)
{
	static double  result;

	result = arg1 * arg2;

	return &result;
}

double *
division_1_svc(double arg1, double arg2,  struct svc_req *rqstp)
{
	static double  result;

	result = arg1 / arg2;

	return &result;
}

double *
potencia_1_svc(double arg1, double arg2,  struct svc_req *rqstp)
{
	static double  result;
	result = 1;
	for(int i=0; i<arg2; i++)
		result *= arg1;

	return &result;
}

miArray *
fibonacci_2_svc(int arg1,  struct svc_req *rqstp)
{
	static miArray  result;

	xdr_free(xdr_miArray, &result);

	result.miArray_len = arg1;
	result.miArray_val = malloc(arg1 * sizeof(float));

	long siguiente = 1, actual = 0, temporal = 0, c=0;
    for (int x = 1; x <= arg1; x++) {
        temporal = actual;
        actual = siguiente;
        siguiente = siguiente + temporal;
		result.miArray_val[c] = actual;
		c++;
    }

	return &result;
}

miArray *
suma_v_2_svc(miArray arg1, miArray arg2,  struct svc_req *rqstp)
{
	static miArray  result;

	xdr_free(xdr_miArray, &result);
	
	result.miArray_len = arg1.miArray_len;
	result.miArray_val = malloc(arg2.miArray_len * sizeof(float));

	for(int i = 0; i < arg1.miArray_len; i++)
		result.miArray_val[i] = arg1.miArray_val[i] + arg2.miArray_val[i];

	for(int i = 0; i < result.miArray_len; i++)
		printf("%f, ", result.miArray_val[i]);


	return &result;
}

miArray *
resta_v_2_svc(miArray arg1, miArray arg2,  struct svc_req *rqstp)
{
	static miArray  result;

	xdr_free(xdr_miArray, &result);

	result.miArray_len = arg1.miArray_len;
	result.miArray_val = malloc(arg2.miArray_len * sizeof(float));

	for(int i = 0; i < arg1.miArray_len; i++)
		result.miArray_val[i] = arg1.miArray_val[i] - arg2.miArray_val[i];


	return &result;
}

miArray *
product_v_2_svc(miArray arg1, miArray arg2,  struct svc_req *rqstp)
{
	static miArray  result;

	xdr_free(xdr_miArray, &result);

	result.miArray_len = arg1.miArray_len;
	result.miArray_val = malloc(arg2.miArray_len * sizeof(float));

	for(int i = 0; i < arg1.miArray_len; i++)
		result.miArray_val[i] = arg1.miArray_val[i] * arg2.miArray_val[i];


	return &result;
}

int *
product_escalar_2_svc(miArray arg1, miArray arg2,  struct svc_req *rqstp)
{
	static int  result;
	
	result = 0;

	for(int i = 0; i < arg1.miArray_len; i++)
		result += arg1.miArray_val[i] * arg2.miArray_val[i];

	
	return &result;
}

float *
media_aritmetica_2_svc(miArray v,  struct svc_req *rqstp)
{
	static float  result;

	result =  v.miArray_val[0];
	if(v.miArray_len > 1) {
		for(int i = 1; i < v.miArray_len; i++)
			result += v.miArray_val[i]; 

		result = result / v.miArray_len;
	}

	return &result;
}