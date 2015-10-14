#include<iostream>
#include<cstdlib>

using namespace std;

int main(){
	printf("Value for dRand: %lg",double(drand48() * 2.0 * 10.0) - (10.0/2.0));
	return 1;
}

