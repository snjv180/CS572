#ifndef OPLISTH
#define OPLISTH
#include <stdlib.h>
#include <stdio.h>
#include "constants.h"

// // // // // // // // // // // // // // // // // // // // // // // // 
// Operators
//
// Operators are Op objects that contain a function that
// can be evaluated
//

double initTermValueOp(double x, double y);
double addOp(double x, double y);
double subOp(double x, double y);
double mulOp(double x, double y);
double divOp(double x, double y);
double xOp(double x, double y);
void setX(double x);
double piOp(double x, double y);
double absOp(double x, double y);

#endif
