#ifndef RANDFLEAH
#define RANDFLEAH

// WARNING: the following includes may vary by OS, since these
// libraries may be in different places on different machines.
//#include <sys/time.h>
#include <time.h>
#include <unistd.h>
#include <math.h>

void initFlea();
void initFlea(unsigned int a, unsigned int b);
unsigned int flea();
double fleaUnit();
double fleaPMUnit();
int fleaMod(int m);
void fleaMod2(int m, int &a, int &b);
int fleaMask(unsigned int mask);
bool choose(double prob);
int flip();
bool choose8(int eigth);
bool chooseMask(unsigned int mask, int prob);
double fleaNorm(double stddev);
double fleaCauchy();
double fleaCauchy(double mean, double scale);

void initFlea64();
void initFlea64(unsigned long long int a, unsigned long long int b);
unsigned long long int flea64();
double fleaUnit64();
double fleaPMUnit64();
int fleaMod64(int m);
void fleaMod264(int m, int &a, int &b);
int fleaMask64(unsigned long long int mask);
bool choose64(double prob);
unsigned long long flip64();
bool choose864(int eigth);
bool chooseMask64(unsigned long long int mask, int prob);
double fleaNorm64(double stddev);
double fleaCauchy64();
double fleaCauchy64(double mean, double scale);
#endif
