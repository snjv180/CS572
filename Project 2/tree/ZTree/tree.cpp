#include "tree.h"


Op::Op(char *name, int arity, double (*f)(double x, double y))
{
    name_ = name;
    arity_ = arity;
    f_ = f;
}


// the list of possible operators
int numOpsTotal;
int numOps0;
int numOps1;
int numOps2;

// the list of possible terminals
Op **opList0;
Op **opList1;
Op **opList2;


void initOps(int maxNumOps)
{
    numOps0 = numOps1 = numOps2 = 0;
    numOpsTotal = numOps0 + numOps1 + numOps2;
    opList0 = new Op * [maxNumOps];
    opList1 = new Op * [maxNumOps];
    opList2 = new Op * [maxNumOps];
}


void addOpOrTerm(char *name, int arity, double (*f)(double x, double y))
{
    switch (arity) {
    case 0:
	opList0[numOps0++] = new Op(name, arity, f);
	break;
    case 1:
	opList1[numOps1++] = new Op(name, arity, f);
	break;
    case 2:
	opList2[numOps2++] = new Op(name, arity, f);
	break;
    }
    numOpsTotal++;
}






// // // // // // // // // // // // // // // // // // // // // // // // 
// Tree Class
//
// Note: if a method has default parameters those parameters are
// not intended to be set by the user.  For example getRandTree should
// only be called with one parameter.  The others are for recursion.
//


// // // // // // // // // // // // // // // // // // // // // // // // 
// Tree Memory Allocation
//
// A free list is maintained of nodes.  This was allocation can be tracked
// and it is often faster than running the new and delete all the time.
// Someone should check that it is faster in this case.  It also provides
// some small amount of error checking.

int freeListInitSize;
int freeListSize, freeListUsed;
Tree *freeList;

// this must be called before any tree routines.
void initFreeList(int initSize)
{
    freeListInitSize = initSize;
    freeListSize = 1;
    freeListUsed = 0;
    freeList = new Tree(0);
}


// this gets a single node
Tree *get(Op *op)
{
    Tree *result;

    // are their preallocated nodes available?
    if (freeList==NULL) {
	for (int i=0; i<freeListInitSize; i++) {
	    Tree *tmp;

	    tmp = freeList;
	    freeList = new Tree(0);
	    freeListSize++;
	    freeList->left_ = tmp;
	}
	freeListInitSize = int(SQR2*freeListInitSize);
    }

    // get node from free list
    if (freeList) {
	result = freeList;
	freeList = result->left_;
    }
    else {
	printf("ERROR(get): unable to allocate more nodes.\n");
	exit(1);
    }

    // set up initial values for fetched tree node
    result->left_ = NULL;
    result->right_ = NULL;
    result->up_ = NULL;
    result->op_ = op;
    if (op->name_) result->value_ = BADDOUBLE;
    else result->value_ = op->f_(0.0, 0.0);       // must be terminal value
    result->size_ = 1;

    freeListSize--;
    freeListUsed++;
    return result;
}



// Gets a single node with a random operator chosen
Tree *getRandOp()
{
    int index;

    index = fleaMod64(numOps1+numOps2);
    if (index<numOps1) {
	return get(opList1[index]);
    }
    else {
	index -= numOps1;
	return get(opList2[index]);
    }
}



// Gets a single node with a random term
Tree *getRandTerm()
{
    return get(opList0[fleaMod64(numOps0)]);
}



// Gets a single node with random op or term
Tree *getRandOpOrTerm()
{
    int index;

    index = fleaMod64(numOpsTotal);
    if (index<numOps0) {
	return get(opList0[index]);
    }
    else {
	index -= numOps0;
	if (index<numOps1) {
	    return get(opList1[index]);
	}
	else {
	    index -= numOps1;
	    return get(opList2[index]);
	}
    }
}


// Constructs a random tree that is no deeper than maxDepth
Tree *getRandTree(int maxDepth, int depth, Tree *up)
{
    Tree *t;

    if (depth==maxDepth) t = getRandTerm();
    else if (depth==1) t = getRandOp();
    else t = getRandOpOrTerm();

    if (t->op_->arity_>=1) {
	t->left_ = getRandTree(maxDepth, depth+1, t);
	t->size_ += t->left_->size_;
    }
    if (t->op_->arity_>=2) {
	t->right_ = getRandTree(maxDepth, depth+1, t);
	t->size_ += t->right_->size_;
    }
    t->up_ = up;

    return t;
}


// frees up a tree.  NOTE: it will set the tree pointer you give it
// to NULL.
void free(Tree *&freeMe)
{
    int delta;
    Tree *node;

    if (freeMe) {
	node = freeList;
	while (node) {
	    if (node==freeMe) {
		printf("ERROR(free): trying to free tree: 0x%016llx that is already free.\n", (unsigned long long int)freeMe&0xffffULL);
		return;
	    }
	    node = node->left_;
	}

	delta = freeMe->leftLinearize(freeList);
	freeListUsed -= delta;
	freeListSize += delta;
	freeList = freeMe;
	freeMe = NULL;   // prevent me from reusing pointer after freed
    }
}




// // // // // // // // // // // // // // // // // // // // // // // // 
// Tree Methods
//

// The tree constructor
Tree::Tree(Op *op)
{
    left_ = NULL;
    right_ = NULL;
    up_ = NULL;
    op_ = op;
    if (op_) {
	if (op_->name_) value_ = BADDOUBLE;
	else value_ = op_->f_(0.0, 0.0);     // must be terminal value
    }
    size_ = 1;
}



void Tree::printIndent(int indent)
{
    for (int i=0; i<indent; i++) printf("   ");
    printf("[%d, 0x%016llx]", size_, (unsigned long long int)this&0xffffULL);

    if (op_->name_) printf("%s\n", op_->name_);
    else printf("%lg\n", value_);  // terminal value

    if (left_) left_->printIndent(indent+1);
    if (right_) right_->printIndent(indent+1);
}




void Tree::printAux()
{
    if (op_->arity_==0) {
	if (op_->name_) printf("%s", op_->name_);
	else printf("%lg", value_);  // terminal value
    }
    else if (op_->arity_==1) {
	if (op_->name_) printf("%s", op_->name_);
	printf("(");
	if (left_) left_->printAux();
	printf(")");
    }
    else {
	printf("(");

	if (left_) left_->printAux();

	if (op_) {
	    printf(" %s ", op_->name_);
	}
	else {
	    printf("NO OP POINTER\n");
	}

	if (right_) right_->printAux();

	printf(")");
    }
}


Tree *Tree::copy(Tree *up)
{
    Tree *t;

    t = get(op_);
    t->up_ = up;
    t->value_ = value_;
    t->left_ = (left_ ? left_->copy(t) : NULL);
    t->right_ = (right_ ? right_->copy(t) : NULL);
    t->size_ = size_;

    return t;
}


int Tree::size()
{
    return size_;
}


Tree *Tree::up()
{
    return up_;
}


double Tree::value()
{
    return value_;
}


double Tree::eval()
{
    if (op_->name_) value_ = (op_->f_)((left_ ? left_->eval() : 0), (right_ ? right_->eval() : 0));
    return value_;
}


double Tree::evalUp()
{
    Tree *node;

    node = this;
    while (1) {
	if (op_->name_) value_ = (op_->f_)((left_ ? left_->eval() : 0), (right_ ? right_->eval() : 0));
	if (node->up_ == NULL) return node->value_;
	node = node->up_;
    }
}


void Tree::print()
{
    printAux();
    printf("\n");
}



int Tree::leftLinearize(Tree *appendix)
{
    Tree *follow, *bottomLeft;
    int size;

    follow = this;
    bottomLeft = this;
    size = 1;

    while (1) {
	// find bottom of the left tree
	while (bottomLeft->left_) {
	    bottomLeft = bottomLeft->left_;
	    size++;
	}

	// find next right branch
	while (follow->left_ && (! follow->right_)) follow = follow->left_;

	// move to bottom left
	if (follow->right_) {
	    bottomLeft->left_ = follow->right_;
	    follow->right_ = NULL;
	}

	// if at the end of the tree add appendix tree if needed
	if (follow->left_==NULL) {
	    follow->left_ = appendix;
	    break;
	}
    };

    return size;
}



bool Tree::join(Side s, Tree *node)
{
    if (node) {
	if (((s==LEFT) && left_) || ((s==RIGHT) && right_)) {
	    printf("ERROR(join): can't join on %s since there is a subtree there.\n", ((s==LEFT) ? "left" : "right"));
	    return false;
	}
	else {
	    int delta;

	    // attach it
	    if (s==LEFT) left_ = node;
	    else right_ = node;

	    // adjust the sizes
	    node->up_ = this;
	    delta = node->size_;
	    while ((node = node->up_)) node->size_ += delta;
	}
    }

    return true;
}


bool Tree::check(bool hasParent)
{
    int l, r;
    unsigned long long int loc;
    bool ok;
    
    ok = true;
    loc = (unsigned long long int)this & 0xffffULL;
//    printf("checking 0x%08x\n", this);

    // Is size positive?
    if (size_<0) {
	printf("ERROR(check): node: 0x%016llx has size: %d\n", loc, size_);
	ok = false;
    }

    // Is linkage consistant?
    if (up_) {
	if (!hasParent) {
	    printf("ERROR(check): node: 0x%016llx is not supposed to have a parent but does\n", loc);
	    ok = false;
	}
	if ((up_->left_!=this) && (up_->right_!=this)) {
	    printf("ERROR(check): parent does not have this node: 0x%016llx as a child\n", loc);
	    ok = false;
	}
    }
    else {
	if (hasParent) {
	    printf("ERROR(check): node: 0x%016llx is supposed to have a parent but doesn't\n", loc);
	    ok = false;
	}
    }

    // Is sum of sizes consistant?
    l = r = 0;
    if (left_) l = left_->size_;
    if (right_) r = right_->size_;
    if (l+r+1!=size_) {
	printf("ERROR(check): node: 0x%016llx size on left: %d and size on right: %d does not give size of parent: %d\n", loc, l, r, size_);
	ok = false;
    }

    if (op_->arity_ == 0) {
	if (left_!=0) printf("ERROR(check): node: 0x%016llx is a terminal but left side operand exists.\n", loc);
	if (right_!=0) printf("ERROR(check): node: 0x%016llx is a terminal but right side operand exists.\n", loc);
    }
    else if (op_->arity_ == 1) {
	if (left_==0) printf("ERROR(check): node: 0x%016llx operator is unary but there is no left side operand.\n", loc);
	if (right_!=0) printf("ERROR(check): node: 0x%016llx operator is unary but right side operand exists.\n", loc);
    }
    else if (op_->arity_ == 2) {
	if (left_==0) printf("ERROR(check): node: 0x%016llx operator is binary but there is no left side operand.\n", loc);
	if (right_==0) printf("ERROR(check): node: 0x%016llx operator is binary but there is no right side operand.\n", loc);
    }

    // Now check left and right
    if (left_) left_->check(true);
    if (right_) right_->check(true);

    return ok;
}


Side Tree::remove()
{
    Tree *node;
    int delta;

    node = this;
    delta = size_;
    while ((node = node->up_)) {
	node->size_ -= delta;
    }

    node = up_;
    up_ = NULL;

    if (node) {
	if (node->left_ == this) {
	    node->left_ = NULL;
	    return LEFT;
	}
	else if (node->right_ == this) {
	    node->right_ = NULL;
	    return RIGHT;
	}
	else printf("ERROR(remove): neither the left nor the right link of the parent of the removed node point to the node removed.\n");
    }

    return SIDEERROR;
}



// randomly and uniformly pick any node in the tree but the root
Tree *Tree::pickNode()
{
    Tree *node;
    int loc, split;
    
    // pick a node number
    loc = fleaMod64(size_-1)+1;
    if (loc>=size_) loc++;  // prevent choosing the root

    // find the node
    node = this;
    while (1) {
//	printf("L: %d\n", loc);

	if (node->size_ == loc) return node;

	if (node->left_) split = node->left_->size_;
	else split = 0;

	if (loc<=split) {
	    node = node->left_;
	}
	else {
	    node = node->right_;
	    loc -= split;
	}
    }
}




int main()
{
    initFlea64();
    initFreeList(5);
    initOps(10);

    addOpOrTerm((char *)"+", 2, addOp);
    addOpOrTerm((char *)"-", 2, subOp);
    addOpOrTerm((char *)"abs", 1, absOp);
    addOpOrTerm((char *)"x", 0, xOp);
    addOpOrTerm((char *)"pi", 0, piOp);
    addOpOrTerm(0, 0, initTermValueOp);  // terminal value


    // BEGIN TESTS


    // ALLOCATE A SINGLE NODE AND FREE IT
    Tree *s, *t;

    printf("SIMPLE TREE\n");
    // one node
    t = getRandOp();
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
    t->printIndent();
    free(t);
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);

    // build a simple tree by hand
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
    t = getRandOp();
    t->join(LEFT, getRandTerm());
    t->join(RIGHT, getRandTerm());
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
    t->check();
    t->printIndent();
    free(t);  // t is now NULL
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);

    // RANDOM TREE
    printf("\nRANDOM TREE\n");
    t = getRandTree(5);
    t->print();
    t->printIndent();
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
    setX(10);
    printf("eval: %lg\n", t->eval());
	
    // COPY
    printf("\nCOPY\n");
    s = t->copy();
    free(t);
    if (!t) printf("NULL\n");  // t should be set to NULL
    else t->print();
    t = getRandTree(5);
    printf("copied tree\n");
    s->printIndent();
    printf("eval: %lg\n", s->eval());
    printf("new tree\n");
    t->print();
    printf("eval: %lg\n", t->eval());

    // some tree data
    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
    printf("new size: %d  old size %d\n", t->size(), s->size());

    // release the tree space
    free(t);
    free(s);

    // PICKING NODES
    printf("\nPICK A NODE\n");
    t = getRandTree(3);
    t->printIndent();
    printf("root node: 0x%016llx   size: %d\n", (unsigned long long int)t, t->size());
    for (int i=0; i<10; i++) {
	s = t->pickNode();
	printf("random node: 0x%016llx\n", (unsigned long long int)s);
    }

    // release the tree space
    free(t);
    free(s);  // should generate an "already freed" error

    // REMOVE A RANDOM SUBTREE
    printf("\nREMOVE\n");
    Tree *parent;
    Side side;

    printf("target tree:\n");
    t = getRandTree(5);            // this is the tree we will mess with
    t->check();                    // tree ok?
    t->printIndent();              // what does it look like?
    s = t->pickNode();             // pick a subtree
    printf("remove this:\n");
    s->printIndent();              // what does the subtree look like?
    parent = s->up();              // get its parent
    side = s->remove();            // remove it from the tree (this probably
                                   // does not necessarily leave a legal 
                                   // expression to evaluate)
    printf("removal gives this tree:\n");
    t->printIndent();              // print the mutilated tree
    t->check();                    // this check SHOULD FIND PROBLEMS
    free(s);                       // free up the part you cut out

    // attach a small tree to where you removed tree
    printf("\nATTACH\n");
    s = getRandTree(3);            // create a random subtree
    s->printIndent();             
    parent->join(side, s);         // attach it where the cut out part was

    printf("now you have:\n");
    t->printIndent();              // this should be able to be evaluated
    t->check();

    printf("unused: %d  used: %d\n", freeListSize, freeListUsed);
}

