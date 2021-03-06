/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "calculadora.h"

bool_t
xdr_miArray (XDR *xdrs, miArray *objp)
{
	register int32_t *buf;

	 if (!xdr_array (xdrs, (char **)&objp->miArray_val, (u_int *) &objp->miArray_len, ~0,
		sizeof (float), (xdrproc_t) xdr_float))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_suma_1_argument (XDR *xdrs, suma_1_argument *objp)
{
	 if (!xdr_double (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_resta_1_argument (XDR *xdrs, resta_1_argument *objp)
{
	 if (!xdr_double (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_multiplicacion_1_argument (XDR *xdrs, multiplicacion_1_argument *objp)
{
	 if (!xdr_double (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_division_1_argument (XDR *xdrs, division_1_argument *objp)
{
	 if (!xdr_double (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_potencia_1_argument (XDR *xdrs, potencia_1_argument *objp)
{
	 if (!xdr_double (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_suma_v_2_argument (XDR *xdrs, suma_v_2_argument *objp)
{
	 if (!xdr_miArray (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_miArray (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_resta_v_2_argument (XDR *xdrs, resta_v_2_argument *objp)
{
	 if (!xdr_miArray (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_miArray (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_product_v_2_argument (XDR *xdrs, product_v_2_argument *objp)
{
	 if (!xdr_miArray (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_miArray (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_product_escalar_2_argument (XDR *xdrs, product_escalar_2_argument *objp)
{
	 if (!xdr_miArray (xdrs, &objp->arg1))
		 return FALSE;
	 if (!xdr_miArray (xdrs, &objp->arg2))
		 return FALSE;
	return TRUE;
}
