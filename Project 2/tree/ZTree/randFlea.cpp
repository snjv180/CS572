#include "randFlea.h"

///////////////////////////////////////////////////
//
// Simple 32 and 64 bit random number generators based on
// a RNG called flea by Bob Jenkins.   Thanks, Bob!
//
// http://burtleburtle.net/bob/rand/talksmall.html
//
// IMPORTANT: be sure to initialize the random number
// generator before you try to use it.  There is separate
// initialization for the 32 and 64 bit random number generators.
//

// these constants are supplied to make the RNG return numbers
// on the open interval [0, 1)
#define PI  	    3.141592653589793238462643383279502884197
#define PIDIV2	    1.570796326794896619231321691639751442099
#define PI64  	    3.141592653589793238462643383279502884197L
#define PIDIV264    1.570796326794896619231321691639751442099L

static double unitizer32 = 1.0/4294967296.0;    // for range [0, 1)
static double unitizer32_2 = 2.0/4294967296.0;  // for range [-1, 1)
static double unitizer32_pi = PI/4294967296.0;  // for range [0, pi)
static double unitizer64 = 1.0/18446744073709551616.0;
static double unitizer64_2 = 2.0/18446744073709551616.0;
static double unitizer64_pi = PI64/18446744073709551616.0;

///////////////////////////////////////////////////
//
// basic fast 32 bit random number generation
//
static unsigned int v, w, x, y, z;

// initialize the random number generator using a pair of seeds
void initFlea(unsigned int a, unsigned int b)
{
    v = 314159;
    w = 265358;
    x = 932384;
    y = 643383;
    z = 279502;
    z += a;
    x += b;
    for (unsigned int i=0; i<((a+b) % 1000)+1000; i++) flea();
}


// initialize the random number generator using process id and time
void initFlea()
{
    initFlea((unsigned int)getpid()*31091,
               (unsigned int)(time(NULL)<<1)|0x1);
}


// return a 32 bit unsigned uniformly distributed random number
unsigned int flea()
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return y;
}


// return a uniformly distributed random number between 0 and 1
double fleaUnit()
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return y*unitizer32;
}


// return a uniformly distributed random number between -1 and 1
double fleaPMUnit()
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return y*unitizer32_2-1.0;
}


// return a uniformly distributed random number between 0 and m-1
int fleaMod(int m) 
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return y%m;
}


// return two uniformly distributed random numbers in the rand 0 to m-1
// where they are not equal.
void fleaMod2(int m, int &a, int &b) 
{
    a = fleaMod(m);
    b = a + fleaMod(m-1) + 1;
    if (b>=m) b-=m;
}


// return a uniformly distributed random number whose bits lie in the 
//   masked bits.  If the mask is a right justified set of ones this
//   is far faster to generate mod of 2^k than using mod function.
int fleaMask(unsigned int mask) {
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return y&mask;
}


// return true with a probability of prob
bool choose(double prob)
{
    return fleaUnit()<prob;
}



// returns zero or nonzero 50% of the time
int flip()
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return (y&0x40);
}


// return true with a probability of eigth/8
// can be faster than using choose with a double probability
bool choose8(int eigth)
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return (y&0x7)<(unsigned int)eigth;
}


// for use in returning true with a probability of prob/(2^k)
bool chooseMask(unsigned int mask, int prob)
{
    v = w;
    w = x;
    x = ((y<<19) + (y>>13)) + z;
    y = z ^ w;
    z = v + x;

    return (y&mask)<(unsigned int)prob;
}




// Random number generator with normal (Gaussian) distribution
// from p 117 of Knuth vol 2 2nd ed.
// Note: this is slow.   Should use the Ziggurat method some day.
static bool gotSpare=false;
static double spare;
double fleaNorm(double stddev)
{
    double u, v, s;

    if (gotSpare) {
        gotSpare=false;
        return spare;
    }
    else {
        do {
            u = 2*fleaUnit() - 1;
            v = 2*fleaUnit() - 1;
            s = u*u + v*v;
        } while (s>=1.0 || s==0.0);
    }

    s = sqrt(-2*log(s)/s)*stddev;
    spare = v*s;
    gotSpare = true;

    return u*s;
}



// Random number generators with a Cauchy distribution
// based on the inversion method using CDF F(x) = .5 + atan(x)/pi
// which yeilds  tan(pi F(x) - .5) as a the tranformation.
// To incorporate a mean and scale: scale*fleaCauchy()+mean
// 

double fleaCauchy()
{
    unsigned int r;

    do r=flea(); while (r==0);

    return tan(unitizer32_pi*r - PIDIV2);
}

double fleaCauchy(double mean, double scale)
{
    unsigned int r;

    do r=flea(); while (r==0);

    return scale*tan(unitizer32_pi*r - PIDIV2) + mean;
}



///////////////////////////////////////////////////
//
// basic fast 64 bit random number generation
//
static unsigned long long int V, W, X, Y, Z;

// initialize the random number generator using a pair of seeds
void initFlea64(unsigned long long int a, unsigned long long int b)
{
    V = 1415926535897932ULL;
    W = 3846264338327950ULL;
    X = 2884197169399375ULL;
    Y = 105820974944592ULL;
    Z = 3078164062862089ULL;
    Z += a;
    X += b;
    for (unsigned long long int i=0; i<((a+b) % 1000)+1000; i++) flea64();
}


// initialize the random number generator using process id and time
void initFlea64()
{
    initFlea64((unsigned long long int)getpid()*1405321245300013ULL,
               (unsigned long long int)(time(NULL)<<1)|0x1ULL);
}


// return a 64 bit unsigned uniformly distributed random number
unsigned long long int flea64()
{
    V = W;
    W = X;
    X = ((Y<<41) + (Y>>23)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return Y;
}

// return a uniformly distributed random number between 0 and 1
// NOTE: this can be relatively slow since the multiply is a 64 bit
// multiply.
double fleaUnit64()
{
    V = W;
    W = X;
    X = ((Y<<41) + (Y>>23)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return Y*unitizer64;
}



// return a uniformly distributed random number between -1 and 1
// NOTE: this can be relatively slow since the multiply is a 64 bit
// multiply.
double fleaPMUnit64()
{
    V = W;
    W = X;
    X = ((Y<<41) + (Y>>23)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return Y*unitizer64_2 - 1.0;
}


// return a uniformly distributed random number between 0 and m-1
int fleaMod64(int m) {
    V = W;
    W = X;
    X = ((Y<<19) + (Y>>13)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return Y%m;
}


// return two uniformly distributed random numbers in the rand 0 to m-1
// where they are not equal.
void fleaMod264(int m, int &a, int &b) 
{
    a = fleaMod64(m);
    b = a + fleaMod64(m-1) + 1;
    if (b>=m) b-=m;
}


// return a uniformly distributed random number whose bits lie in the 
//   masked bits.  If the mask is a right justified set of ones this
//   is far faster to generate mod of 2^k than using mod function.
int fleaMask64(unsigned long long int mask) {
    V = W;
    W = X;
    X = ((Y<<19) + (Y>>13)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return Y&mask;
}


// return true with a probability of prob
bool choose64(double prob)
{
    return fleaUnit64()<prob;
}


// returns zero or nonzero 50% of the time
unsigned long long flip64()
{
    V = W;
    W = X;
    X = ((Y<<19) + (Y>>13)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return (y&0x40ULL);
}


// return true with a probability of eigth/8
// can be faster than using choose with a double probability
bool choose864(int eigth)
{
    V = W;
    W = X;
    X = ((Y<<19) + (Y>>13)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return (Y&0x7ULL)<(unsigned long long int)eigth;
}

// for use in returning true with a probability of prob/(2^k)
bool chooseMask64(unsigned long long int mask, int prob)
{
    V = W;
    W = X;
    X = ((Y<<19) + (Y>>13)) + Z;
    Y = Z ^ W;
    Z = V + X;

    return (Y&mask)<(unsigned long long int)prob;
}


// Random number generator with normal (Gaussian) distribution
// from p 117 of Knuth vol 2 2nd ed.
// Note: this is slow.   Should use the Ziggurat method some day.
static bool gotSpare64=false;
static double spare64;
double fleaNorm64(double stddev)
{
    double u, v, s;

    if (gotSpare64) {
        gotSpare64=false;
        return spare64;
    }
    else {
        do {
            u = 2*fleaUnit64() - 1;
            v = 2*fleaUnit64() - 1;
            s = u*u + v*v;
        } while (s>=1.0 || s==0.0);
    }

    s = sqrt(-2*log(s)/s)*stddev;
    spare64 = v*s;
    gotSpare64 = true;

    return u*s;
}



// Random number generators with a Cauchy distribution
// based on the inversion method using CDF F(x) = .5 + atan(x)/pi
// which yeilds  tan(pi F(x) - .5) as a the tranformation.
// To incorporate a mean and scale: scale*fleaCauchy()+mean
// 

double fleaCauchy64()
{
    unsigned long long int r;

    do r=flea64(); while (r==0);

    return tan(unitizer64_pi*r - PIDIV264);
}

double fleaCauchy64(double mean, double scale)
{
    unsigned long long int r;

    do r=flea64(); while (r==0);

    return scale*tan(unitizer64_pi*r - PIDIV264) + mean;
}
