#include "randFlea.h"
#include "opList.h"

// // // // // // // // // // // // // // // // // // // // // // // // 
// Operators
//
// Operators are Op objects that contain a function that
// can be evaluated
//

// this returns a random value for a terminal value (op * -> NULL (see below))
double initTermValueOp(double x, double y)
{
    return fleaUnit64()*5.0;
}

double addOp(double x, double y)
{
    return x+y;
}

double subOp(double x, double y)
{
    return x-y;
}

double mulOp(double x, double y)
{
    return x*y;
}

double divOp(double x, double y)
{
    const double cutoff=.000001;

    if (fabs(y)>cutoff) return x/y;
    else return x/cutoff;
}


double xValue;
double xOp(double x, double y)
{
    return xValue;
}


void setX(double x)
{
    xValue = x;
}


double piOp(double x, double y)
{
    return PI;
}

double absOp(double x, double y)
{
    return fabs(x);
}

