// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the BSTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    public AVLTree Insert(int address, int size, int key) 
    {
        if (this == null) return null;
        AVLTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;
        AVLTree toBeInsertedNode = new AVLTree(address,size,key);
        if (rootOfTree == null) { // tree is empty;
            this.right = toBeInsertedNode;
            toBeInsertedNode.parent = this;
            return toBeInsertedNode;
        }
        //if tree was not empty;
        AVLTree followNode = rootOfTree, leaderNode = rootOfTree;
        while (leaderNode != null) {
            followNode = leaderNode;
            if (key < leaderNode.key || (key == leaderNode.key && address < leaderNode.address)) leaderNode = leaderNode.left;
            else if (key > leaderNode.key || (key == leaderNode.key && address > leaderNode.address)) leaderNode = leaderNode.right;
        } //after this while loop has ended, follow node contains the leaf at which insertion needs to take place;

        toBeInsertedNode.parent = followNode;
        if (key > followNode.key || (key == followNode.key && address > followNode.address)) followNode.right = toBeInsertedNode;
        else if (key < followNode.key || (key == followNode.key && address < followNode.address)) followNode.left = toBeInsertedNode;

        // we have inserted the node at the right position. now we need to check for height imbalances in the ancestors;
        // NOTE that grandparent:z cannot be null initially. at max, it could be head sentinel; this is so because if we were inserting
        // the root of the tree, then it was completed much before. we are inserting some other node other than the root;
        // so its grandparent cannot be null;

        AVLTree x = toBeInsertedNode, y = x.parent, z = y.parent;   // initially x has height 0; it is the correct height of a leaf;
        y.setHeight(); // assigning height of y = 1;
        //  note that grandparent z maybe imbalanced initially
        while(z.parent != null && z.ChildrenAreBalanced()) {
            z.setHeight();
            x = y;
            y = z;
            z = z.parent;
        }
        if(z.parent == null) return toBeInsertedNode; // means there was no imbalance anywhere from the leaf to the root;

        // now there is imbalance at z.
        if(x == y.left && y == z.left)  z.leftRotate();// we need to perform left rotation!!
        else if (x == y.right && y == z.right)  z.rightRotate();// we need to perform right rotation!!
        else if (x == y.right && y == z.left) z.leftRightRotate(); // we need to perform left-right rotation!!
        else if (x == y.left && y == z.right) z.rightLeftRotate();// we need to perform right-left rotation!!
        return toBeInsertedNode;
    }

    public boolean Delete(Dictionary e)
    {
        AVLTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;
        if (rootOfTree == null) return false; //if tree is empty, return false or true??;
        // now search for the node e;
        AVLTree foundNode = rootOfTree;

        while (foundNode != null) {
            if (e.key > foundNode.key) foundNode = foundNode.right;
            else if (e.key < foundNode.key) foundNode = foundNode.left;
            else if (e.key == foundNode.key) {
                if (e.address < foundNode.address) foundNode = foundNode.left;
                else if (e.address > foundNode.address) foundNode = foundNode.right;
                else break; // that means the right node to be deleted is being pointed by foundNode;
            }
        }
        if (foundNode == null) return false; //if e not found, return false;

        else { // three cases: two children, one child, no children;
            if (foundNode.left == null && foundNode.right == null) { // we are deleting a leaf;
                AVLTree z = foundNode.parent; // z could be imbalanced just after deletion;
                if (z.left == foundNode) z.left = null;
                else if (z.right == foundNode) z.right = null;

                while(z.parent != null) {
                    while(z.parent != null && z.ChildrenAreBalanced()) {
                        z.setHeight();
                        z = z.parent;
                    }
                    if(z.parent == null) return true; // means there was no imbalance anywhere from the leaf to the root;
                    // now there is imbalance at z.
                    //assuming they are not null, though i still have to give a formal proof that they are not null;
                    AVLTree y = z.getChildWithLargerHeight();
                    AVLTree x = y.getChildWithLargerHeight();
                    AVLTree tempNode = z;
                    z = z.parent;
                    if(x == y.left && y == tempNode.left)  tempNode.leftRotate();// we need to perform left rotation!!
                    else if (x == y.right && y == tempNode.right)  tempNode.rightRotate();// we need to perform right rotation!!
                    else if (x == y.right && y == tempNode.left) tempNode.leftRightRotate(); // we need to perform left-right rotation!!
                    else if (x == y.left && y == tempNode.right) tempNode.rightLeftRotate();// we need to perform right-left rotation!!
                }
                return true; // we have finally reached the head sentinel after correcting all of the ancestors of the deleted node;
            }

            // if one child is null then the other child has to be a leaf in a avl tree; swap elements with non-null child and delete that leaf;
            else if ((foundNode.left != null && foundNode.right == null) || (foundNode.left == null && foundNode.right != null)) {
                if(foundNode.left != null) { //implicitly implies that right is null;
                    foundNode.key = foundNode.left.key;
                    foundNode.address = foundNode.left.address;
                    foundNode.size = foundNode.left.size;
                    foundNode.left = null;
                }
                else if(foundNode.right != null) { //implicitly implies that left is null;
                    foundNode.key = foundNode.right.key;
                    foundNode.address = foundNode.right.address;
                    foundNode.size = foundNode.right.size;
                    foundNode.right = null;
                }
                foundNode.height = 0; // foundNode is a leaf now :: it is balanced :: no children;
                AVLTree z = foundNode.parent; // z could be imbalanced just after deletion;
                while(z.parent != null) {
                    while(z.parent != null && z.ChildrenAreBalanced()) {
                        z.setHeight();
                        z = z.parent;
                    }
                    if(z.parent == null) return true; // means there was no imbalance anywhere from the leaf to the root;
                    // now there is imbalance at z.
                    // are we guaranteed to have child, grandchild != null??
                    //assuming they are not null, though i still have to give a formal proof that they are not null;
                    AVLTree y = z.getChildWithLargerHeight();
                    AVLTree x = y.getChildWithLargerHeight();
                    AVLTree tempNode = z;
                    z = z.parent;
                    if(x == y.left && y == tempNode.left)  tempNode.leftRotate();// we need to perform left rotation!!
                    else if (x == y.right && y == tempNode.right)  tempNode.rightRotate();// we need to perform right rotation!!
                    else if (x == y.right && y == tempNode.left) tempNode.leftRightRotate(); // we need to perform left-right rotation!!
                    else if (x == y.left && y == tempNode.right) tempNode.rightLeftRotate();// we need to perform right-left rotation!!
                }
                return true; // we have finally reached the head sentinel after correcting all of the ancestors of the deleted node;
            }
            else if (foundNode.left != null && foundNode.right != null) {
                AVLTree successorNode = foundNode.getNext(); // successor cannot be null as found.right != null :: it has only right leaf if it has a child;
                foundNode.key = successorNode.key;
                foundNode.size = successorNode.size;
                foundNode.address = successorNode.address;
                AVLTree z = successorNode.parent;

                if(successorNode.right != null) {
                    successorNode.key = successorNode.right.key;
                    successorNode.address = successorNode.right.address;
                    successorNode.size = successorNode.right.size;
                    successorNode.right = null;
                    successorNode.height = 0;
                }
                else if (successorNode.right == null) {
                    if (z.left == successorNode) z.left = null;
                    else if (z.right == successorNode) z.right = null;
                }

                //z.setHeight();
                while(z.parent != null) {
                    while(z.parent != null && z.ChildrenAreBalanced()) {
                        z.setHeight();
                        z = z.parent;
                    }
                    if(z.parent == null) return true; // means there was no imbalance anywhere from the leaf to the root;
                    // now there is imbalance at z.
                    // are we guaranteed to have child, grandchild != null??
                    //assuming they are not null, though i still have to give a formal proof that they are not null;
                    AVLTree y = z.getChildWithLargerHeight();
                    AVLTree x = y.getChildWithLargerHeight();
                    AVLTree tempNode = z;
                    z = z.parent;
                    if(x == y.left && y == tempNode.left)  tempNode.leftRotate();// we need to perform left rotation!!
                    else if (x == y.right && y == tempNode.right)  tempNode.rightRotate();// we need to perform right rotation!!
                    else if (x == y.right && y == tempNode.left) tempNode.leftRightRotate(); // we need to perform left-right rotation!!
                    else if (x == y.left && y == tempNode.right) tempNode.rightLeftRotate();// we need to perform right-left rotation!!
                }
                return true; // we have finally reached the head sentinel after correcting all of the ancestors of the deleted node;
            }
        }
        return false;
    }
        
    public AVLTree Find(int k, boolean exact)
    {
        AVLTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;

        if (rootOfTree == null) return null; //if tree is empty, return null;
        // uptil here code was common. now we divide the code depending on whether exact == true or false;
        if (exact) {
            AVLTree findingNode = rootOfTree;
            while (findingNode != null) {
                if (k < findingNode.key) findingNode = findingNode.left;
                else if (k > findingNode.key) findingNode = findingNode.right;
                else if (k == findingNode.key) {
                    while (findingNode.getPrev() != null && findingNode.key == findingNode.getPrev().key)
                        findingNode = findingNode.getPrev(); //minimize address;
                    return findingNode;
                }
            }
            if (findingNode == null) return null;
        }

        if (!exact) { //we need to find the smallest node whose key >= given key;
            AVLTree findingNode = rootOfTree;
            while (findingNode != null) {

                if (k > findingNode.key) findingNode = findingNode.right;
                else if (k == findingNode.key) {
                    while (findingNode.getPrev() != null && findingNode.key == findingNode.getPrev().key)
                        findingNode = findingNode.getPrev(); //minimize address;
                    return findingNode;
                }
                else if (k < findingNode.key) {
                    AVLTree predecessor_findingNode = findingNode.getPrev();
                    if (predecessor_findingNode != null && predecessor_findingNode.key < k) return findingNode;
                    if (predecessor_findingNode == null) return findingNode;

                    if (findingNode.left != null) findingNode = findingNode.left;
                    else if (findingNode.left == null) findingNode = findingNode.getPrev();
                }
            }
            if (findingNode == null) return null;
        }
        return null;
    }

    public AVLTree getFirst()
    {
        AVLTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;

        if (rootOfTree == null) return null; // if tree is empty return null
        else { // if tree is not empty, go as left as possible;
            AVLTree MinNode = rootOfTree;
            while (MinNode.left != null) MinNode = MinNode.left;
            return MinNode;
        }
    }

    public AVLTree getNext() // what if getNext is called on head sentinel? // As clarified by TA we should return null;
    {
        if (this.parent == null) return null;
        AVLTree x = this;
        if (x.right != null) { // this means that the right subtree is not empty. successor must be the leftmost node of right subtree;
            AVLTree successorNode = x.right;
            while (successorNode.left != null) successorNode = successorNode.left;
            //after completion of this while loop, we have got the successor. return it;
            return successorNode;
        }
        else { // right subtree is empty, successor is the lowest ancestor of x whose left child is also an ancestor of x;
            AVLTree successorNode = x.parent;
            while (successorNode != null && x == successorNode.right) {
                x = successorNode;
                successorNode = successorNode.parent;
            }//if while loop terminates because successorNode becomes null, that means there is no successor of this node, we should return null;
            return successorNode;
        }
    }

    public boolean sanity()
    {
        // first we check whether the the head sentinel's parent is null or not. how to we test that? we keep on doing
        // node.parent from the node on which node.sanity() is called and after a certain number of iterations, say 1,000,000 if
        // we didn't get parent = null, that means there is some kind of cycle where the head sentinel.parent is one
        // of the nodes in the BS tree;
        int i = 1000000;
        AVLTree x = this;
        while (i > 0 && x.parent != null) {
            x = x.parent;
            i--;
        }
        // x should point to the head sentinel node;
        if (i == 0) {
            //System.out.println("head sentinel is reason of cycle");
            return false; // head sentinel's parent is not null;
        }
        // now we test if there is a cycle inside the binary tree or not; we have a pointer which points towards head sentinel;
        AVLTree y = x;
        AVLTree z = x;
        AVLTree insertAndFindTree = new AVLTree();
        boolean cycleExists = cycleIsPresent(y, insertAndFindTree);
        if (cycleExists) return false;
        ////////////////////////////////////////////////////////////////
        // now we check whether all the pointers are correctly assigned or not;
        boolean linkagesAreCorrect = checkLinkagesOfTree(x);
        if (!linkagesAreCorrect) return false;
        /////////////////////////////////////////////////////////////////////
        // now we check whether the binary search property holds true or not;
        boolean BSPropertyHolds = checkBinarySearchProperty(x);  //if BSPropertyHolds is true, then the given tree is a BSTree;
        if (!BSPropertyHolds) return false;
        ////////////////////////////////////////////////////////////////////////
        // now we check whether the tree satisfies the balance property of AVL tree or not;
        boolean BalancePropertyHolds = CheckTreeIsBalanced(z.right);
        if(!BalancePropertyHolds) return false;
        ////////////////////////////////////////////////////////////////////////////////////////
        //if sanity method has still not returned false, that means given tree is a sane avl tree, and we should return true;
        return true;
    }


//// EXTRA HELPER FUNCTIONS THAT I HAVE DEFINED

    private AVLTree getPrev() {// getPrev will automatically return null if called on head sentinel;
        AVLTree x = this;
        if (x.left != null) { // this means that the left subtree is not empty. predecessor must be the rightmost node of left subtree;
            AVLTree predecessorNode = x.left;
            while (predecessorNode.right != null) predecessorNode = predecessorNode.right;
            //after completion of this while loop, we have got the predecessor. return it;
            return predecessorNode;
        }
        else { // left subtree is empty, predecessor is the lowest ancestor of x whose right child is also an ancestor of x;
            AVLTree predecessorNode = x.parent;
            while (predecessorNode != null && x == predecessorNode.left) {
                x = predecessorNode;
                predecessorNode = predecessorNode.parent;
            } //if while loop terminates because predecessorNode becomes null, that means there is no predecessor of this node, we should return null;
            return predecessorNode;
        }
    }

    private boolean CheckTreeIsBalanced(AVLTree root) {
        if(root == null) return true;
        if(root.ChildrenAreBalanced()) {
            if(root.left == null && root.right == null && root.height != 0) return false;
            if(root.left == null && root.right != null && root.height <= root.right.height) return false;
            if(root.left != null && root.right == null && root.height <= root.left.height) return false;
            if(root.left != null && root.right != null && (root.height <= root.left.height || root.height <= root.right.height)) return false;
            return CheckTreeIsBalanced(root.left) && CheckTreeIsBalanced(root.right);
        }
        return false;
    }

    private void CorrectlySetHeightsOfTree(AVLTree root) {
        if(root == null) return;
        CorrectlySetHeightsOfTree(root.left);
        CorrectlySetHeightsOfTree(root.right);
        root.setHeight();
        return;
    }

    private boolean ChildrenAreBalanced() { // checking whether the children have height difference of atmost 1.
        AVLTree node = this;
        if(node.left == null) {
            if(node.right == null) return true;
            else if(node.right != null) {
                if(node.right.height == 0) return true;
                return false;
            }
        }
        else if(node.left != null){
            if(node.right == null) {
                if(node.left.height == 0) return true;
                return false;
            }
            else if (node.right != null) {
                if((node.left.height - node.right.height < 2) && (node.left.height - node.right.height > -2) ) return true;
                return false;
            }
        }
        return true;
    }

    private void setHeight() { // Assumption is that given node's children were balanced;
        AVLTree node = this;
        if(node.left == null) {
            if(node.right == null) node.height = 0;
            else node.height = node.right.height + 1; // if the node was balanced, then node.right.height has to be 0;
        }
        else {
            if(node.right == null) node.height = node.left.height + 1; // if the node was balanced, then node.left.height has to be 0;
            else {
                int maxChildrenHeight = 0;
                if(node.left.height >= node.right.height) maxChildrenHeight = node.left.height;
                else maxChildrenHeight = node.right.height;
                node.height = maxChildrenHeight + 1;
            }
        }
        return;
    }

    private void leftRotate() {
        // note that z.parent is not null; since that case was handled when height of y didn't change before and after y.setHeight();

        AVLTree z = this, y = z.left, x = y.left;
        AVLTree t0 = x.left;
        AVLTree t1 = x.right;
        AVLTree t2 = y.right;
        AVLTree t3 = z.right;
        AVLTree parentOfz = z.parent;
        boolean ZisLeftChild = false;
        if (z.parent.left == z) ZisLeftChild = true;

        y.right = z;
        z.parent = y;
        z.left = t2;
        if(t2 != null) t2.parent = z;

        y.parent = parentOfz;
        if(ZisLeftChild) parentOfz.left = y;
        else parentOfz.right = y;

        // now modify the height of z;
        if(t0 != null) t0.setHeight();
        if(t1 != null) t1.setHeight();
        if(t2 != null) t2.setHeight();
        if(t3 != null) t3.setHeight();
        z.setHeight();
        x.setHeight();
        y.setHeight();
        return;
    }

    private void rightRotate() {
        // note that z.parent is not null; since that case was handled when height of y didn't change before and after y.setHeight();

        AVLTree z = this, y = z.right, x = y.right;
        AVLTree t0 = z.left, t1 = y.left, t2 = x.left, t3 = x.right;
        AVLTree parentOfz = z.parent;
        boolean ZisLeftChild = false;
        if (z.parent.left == z) ZisLeftChild = true;

        y.left = z;
        z.parent = y;
        z.right = t1;
        if(t1 != null) t1.parent = z;

        y.parent = parentOfz;
        if(ZisLeftChild) parentOfz.left = y;
        else parentOfz.right = y;

        // now modify the height of z;
        if(t0 != null) t0.setHeight();
        if(t1 != null) t1.setHeight();
        if(t2 != null) t2.setHeight();
        if(t3 != null) t3.setHeight();
        z.setHeight();
        x.setHeight();
        y.setHeight();
        return;
    }

    private void leftRightRotate() {
        // note that z.parent is not null; since that case was handled when height of y didn't change before and after y.setHeight();

        AVLTree z = this, y = z.left, x = y.right;
        AVLTree t0 = z.right, t1 = x.right, t2 = x.left, t3 = y.left;
        AVLTree parentOfz = z.parent;
        boolean ZisLeftChild = false;
        if (z.parent.left == z) ZisLeftChild = true;

        y.parent = x;
        x.left = y;
        z.parent = x;
        x.right = z;

        y.right = t2;
        if(t2 != null) t2.parent = y;
        z.left = t1;
        if(t1 != null) t1.parent = z;

        x.parent = parentOfz;
        if (ZisLeftChild) parentOfz.left = x;
        else parentOfz.right = x;

        //now modify the heights of x,y,z;
        if(t0 != null) t0.setHeight();
        if(t1 != null) t1.setHeight();
        if(t2 != null) t2.setHeight();
        if(t3 != null) t3.setHeight();
        y.setHeight();
        z.setHeight();
        x.setHeight();
        return;
    }

    private void rightLeftRotate() {
        // note that z.parent is not null; since that case was handled when height of y didn't change before and after y.setHeight();

        AVLTree z = this, y = z.right, x = y.left;
        AVLTree t0 = z.left, t1 = x.left, t2 = x.right, t3 = y.right;
        AVLTree parentOfz = z.parent;
        boolean ZisLeftChild = false;
        if (z.parent.left == z) ZisLeftChild = true;

        y.parent = x;
        x.right = y;
        z.parent = x;
        x.left = z;

        y.left = t2;
        if(t2 != null) t2.parent = y;
        z.right = t1;
        if(t1 != null) t1.parent = z;

        x.parent = parentOfz;
        if(ZisLeftChild) parentOfz.left = x;
        else parentOfz.right = x;

        //now modify the heights of x,y,z;
        if(t0 != null) t0.setHeight();
        if(t1 != null) t1.setHeight();
        if(t2 != null) t2.setHeight();
        if(t3 != null) t3.setHeight();
        y.setHeight();
        z.setHeight();
        x.setHeight();
        return;
    }

    private AVLTree getChildWithLargerHeight() { // convention :: if both children are of same height, return right child;
        AVLTree parentNode = this;
        AVLTree leftChild = this.left, rightChild = this.right;
        if(leftChild == null) {
            if(rightChild == null) return null; // we are returning a null only in this case;
            if(rightChild != null) return rightChild;
        }
        else if(leftChild != null) {
            if(rightChild == null) return leftChild;
            if(rightChild != null) {
                if(leftChild.height > rightChild.height) return leftChild;
                else if (leftChild.height < rightChild.height) return rightChild;
                else {
                    if(parentNode.parent.left == parentNode) return leftChild;
                    else if (parentNode.parent.right == parentNode) return  rightChild;
                }
            }
        }
        return null;
    }

    private AVLTree getMinofTree(AVLTree root) {
        if (root == null) return null;
        AVLTree x = root;
        while (x.left != null) x = x.left;
        return x;
    }

    private AVLTree getMaxofTree(AVLTree root) {
        if (root == null) return null;
        AVLTree x = root;
        while (x.right != null) x = x.right;
        return x;
    }

    private boolean isLessThan(AVLTree node1, AVLTree node2) {
        if (node1.key < node2.key) return true;
        if (node1.key > node2.key) return false;
        // code continues only if keys are equal;
        if (node1.address < node2.address) return true;
        else if (node1.address > node2.address) return false;
        return true; // if both key and address are same, return true; this case also should give false in checkBSTProperty function;
    }


    private boolean isGreaterThan(AVLTree node1, AVLTree node2) {
        return isLessThan(node2, node1);
    }

    private boolean checkBinarySearchProperty(AVLTree root) {
        if (root == null) return true;
        if (root.left != null) {
            if (root.right != null) {
                if (isGreaterThan(getMaxofTree(root.left), root) || isLessThan(getMinofTree(root.right), root))
                    return false;
                else return (checkBinarySearchProperty(root.left) && checkBinarySearchProperty(root.right));
            } else { // left is not null and right is null;
                if (isGreaterThan(getMaxofTree(root.left), root)) return false;
                else return checkBinarySearchProperty(root.left);
            }
        } else {
            if (root.right != null) {
                if (isLessThan(getMinofTree(root.right), root)) return false;
                else return checkBinarySearchProperty(root.right);
            }

        }
        return true; // if both are null, return true;
    }

    private boolean checkLinkagesOfTree(AVLTree root) {
        if (root == null) return true;
        if (root.left == null && root.right == null) return true;
        if (root.left != null && root.right == null) {
            if (root.left.parent != root) return false;
            return checkLinkagesOfTree(root.left);
        }
        if (root.left == null && root.right != null) {
            if (root.right.parent != root) return false;
            return checkLinkagesOfTree(root.right);
        }
        if (root.left != null && root.right != null) {
            if (root.left == root.right) return false; // if both children are same there is some error; return false;
            if (root.left.parent != root || root.right.parent != root) return false;
            return (checkLinkagesOfTree(root.left) && checkLinkagesOfTree(root.right));
        }
        return true;
    }

    private boolean isPresentInTree(AVLTree headSentinel) {
        AVLTree x = this;
        AVLTree y = headSentinel.right;
        while (y != null) {
            if ((y.key < x.key) || (y.key == x.key && y.address < x.address)) y = y.left;
            else if ((y.key > x.key) || (y.key == x.key && y.address > x.address)) y = y.right;
            else if (y.key == x.key && y.address == x.address) return true;
        }
        return false;
    }

    private boolean cycleIsPresent(AVLTree root, AVLTree insertInThisTree) { // it takes in root of the tree as argument;
        if (root == null) return false;
        boolean rootIsPresent = root.isPresentInTree(insertInThisTree);
        if (rootIsPresent) return true;
        // if node didn't already exist, we do preorder traversal and insert a node in this tree;
        insertInThisTree.Insert(root.address, root.size, root.key);
        return (cycleIsPresent(root.left, insertInThisTree) || cycleIsPresent(root.right, insertInThisTree));
    }
}


