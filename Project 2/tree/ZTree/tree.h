#ifndef TREEH
#define TREEH

#include <stdlib.h>
#include <stdio.h>
#include "randFlea.h"
#include "opList.h"


// // // // // // // // // // // // // // // // // // // // // // // // 
// Side
//
enum Side {LEFT, RIGHT, SIDEERROR};


// // // // // // // // // // // // // // // // // // // // // // // // 
// Op class
//
// Operators are Op objects that contain a function that
// can be evaluated
//
// This class can be either an operator or a terminal
// 
class Op
{
// data
public:
    double (*f_)(double x, double y);   // function if arity>0
    char *name_;                        // printable name of operator
    int arity_;                         // arity of operator
// methods
public:
    Op(char *name, int arity, double (*f)(double x, double y));
};


void initOps(int maxNumOps);
void addOpOrTerm(char *name, int arity, double (*f)(double x, double y));


// // // // // // // // // // // // // // // // // // // // // // // // 
// Tree Class
//
// Note: if a method has default parameters those parameters are
// not intended to be set by the user.  For example getRandTree should
// only be called with one parameter.  The others are for recursion.
//

class Tree
{
// data
private:
    Tree *left_;   // the left child
    Tree *right_;  // the right child
    Tree *up_;     // the parent
    Op *op_;       // the operator for this node (could be a terminal)
    double value_; // the cached value for this node
    int size_;     // size of the tree beneath this node including this node

// methods
private:
    void printAux();                    // print helper routine
    int leftLinearize(Tree *appendix);  // used in free()

public:
    Tree(Op *op);               // create a tree
    bool check(bool hasParent=false);  // vet the tree
    Tree *copy(Tree *up=NULL);  // the only command that copies any nodes
    int size();                 // size accessor
    Tree *up();                 // parent accessor
    double value();             // value accessor
    double eval();              // evaluate the tree
    double evalUp();            // evaluate by going from this node up
    void printIndent(int depth=0);  // print internal form of tree
    void print();               // print in nice neat expression
    bool join(Side s, Tree *subtree);  // add a subtree to a tree
    Side remove();              // remove a subtree from its parent.
                                // THIS WILL NOT FREE THE SUBTREE
                                // returns side removed from in parent
    Tree *pickNode();           // uniformly any node but the root

// friends (memory allocation from free list)
public:    
    friend void free(Tree *&t); // free up the nodes in a tree
    friend Tree *get(Op *op);   // get a node for a tree setting its op
    friend Tree *getRandTree(int maxDepth, int depth=1, Tree *up=NULL);
};

void setX(double x);            // set the x variable
void initFreeList(int initSize);// init the memory allocation
Tree *get(Op *op);              // get a node
Tree *getRandOp();              // get random operator node
Tree *getRandTerm();            // get random terminal node
Tree *getRandOpOrTerm();        // get random operator or terminal node
Tree *getRandTree(int maxDepth, int depth, Tree *up); // get a random tree
void free(Tree *&freeMe);       // free up a whole tree with freeMe as root

#endif
