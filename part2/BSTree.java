// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java
//@Abhinav Singhal - 2019CS50768

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.

    public BSTree() {
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }

    public BSTree(int address, int size, int key) {
        super(address, size, key);
    }

    public BSTree Insert(int address, int size, int key) {
        if (this == null) return null;
        if (this.parent == null) { //if we are calling on the sentinel node;
            if (this.right == null) { // if the tree is empty;
                BSTree insertingRoot = new BSTree(address, size, key);
                this.right = insertingRoot;
                insertingRoot.parent = this;
                return insertingRoot;
            } else { // if the tree was not empty;
                BSTree rootOfTree = this.right;
                BSTree toBeInsertedNode = new BSTree(address, size, key);
                BSTree leaderNode = rootOfTree; //
                BSTree followNode = rootOfTree; // initially both are assigned the same

                findingCorrectLeaf:
                while (leaderNode != null) {
                    followNode = leaderNode;
                    if (key < leaderNode.key) leaderNode = leaderNode.left;
                    else if (key > leaderNode.key) leaderNode = leaderNode.right;
                    else if (key == leaderNode.key) {
                        if (address < leaderNode.address) leaderNode = leaderNode.left;
                        else leaderNode = leaderNode.right;
                    }
                } //after this while loop has ended, follow node contains the leaf at which insertion needs to take place;

                toBeInsertedNode.parent = followNode;
                if (key > followNode.key) followNode.right = toBeInsertedNode;
                else if (key < followNode.key) followNode.left = toBeInsertedNode;
                else if (key == followNode.key) {
                    if (address < followNode.address) followNode.left = toBeInsertedNode;
                    else followNode.right = toBeInsertedNode;
                }
                return toBeInsertedNode;
            }
        } else if (this.parent != null) { // if we are not calling on sentinel node, we first need to find the root of the tree
            BSTree rootOfTree = this;
            findingRoot:
            while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent;
            rootOfTree = rootOfTree.right;
            // we have found the root of the tree; simply copy paste the code from line 530 to 551 as same method will be followed to find the correct leaf at which insertion takes place;
            BSTree toBeInsertedNode = new BSTree(address, size, key);
            BSTree leaderNode = rootOfTree; //
            BSTree followNode = rootOfTree; // initially both are assigned the same

            findingCorrectLeaf:
            while (leaderNode != null) {
                followNode = leaderNode;
                if (key < leaderNode.key) leaderNode = leaderNode.left;
                else if (key > leaderNode.key) leaderNode = leaderNode.right;
                else if (key == leaderNode.key) {
                    if (address < leaderNode.address) leaderNode = leaderNode.left;
                    else leaderNode = leaderNode.right;
                }
            } //after this while loop has ended, follow node contains the leaf at which insertion needs to take place;

            toBeInsertedNode.parent = followNode;
            if (key > followNode.key) followNode.right = toBeInsertedNode;
            else if (key < followNode.key) followNode.left = toBeInsertedNode;
            else if (key == followNode.key) {
                if (address < followNode.address) followNode.left = toBeInsertedNode;
                else followNode.right = toBeInsertedNode;
            }
            return toBeInsertedNode;

        }
        return null;
    }

    public boolean Delete(Dictionary e) {
        BSTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;
        if (rootOfTree == null) return false; //if tree is empty, return false or true??;
        // now search for the node e;
        BSTree foundNode = rootOfTree;

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
            if (foundNode.left == null && foundNode.right == null) { // both children are null;
                if (foundNode.parent.left == foundNode) foundNode.parent.left = null;
                else if (foundNode.parent.right == foundNode) foundNode.parent.right = null;
                return true;
            } else if (foundNode.left == null && foundNode.right != null) { //only left child is null;
                BSTree parentNode = foundNode.parent;
                BSTree childNode = foundNode.right;
                if (parentNode.left == foundNode) {
                    parentNode.left = childNode;
                    childNode.parent = parentNode;
                    return true;
                } else if (parentNode.right == foundNode) {
                    parentNode.right = childNode;
                    childNode.parent = parentNode;
                    return true;
                }
            } else if (foundNode.right == null && foundNode.left != null) {// only right child is null;
                BSTree parentNode = foundNode.parent;
                BSTree childNode = foundNode.left;
                if (parentNode.left == foundNode) {
                    parentNode.left = childNode;
                    childNode.parent = parentNode;
                    return true;
                } else if (parentNode.right == foundNode) {
                    parentNode.right = childNode;
                    childNode.parent = parentNode;
                    return true;
                }
            } else if (foundNode.right != null && foundNode.left != null) { // both are not null;
                BSTree successorNode = foundNode.getNext(); // successor has only right child if it has a child;
                foundNode.key = successorNode.key;
                foundNode.size = successorNode.size;
                foundNode.address = successorNode.address;
                if (successorNode.right == null) { // delete the successor accordingly;
                    if (successorNode.parent.left == successorNode) {
                        successorNode.parent.left = null;
                        return true;
                    } else if (successorNode.parent.right == successorNode) {
                        successorNode.parent.right = null;
                        return true;
                    }
                } else if (successorNode.right != null) { // delete the successor accordingly;
                    BSTree parentNode = successorNode.parent;
                    BSTree childNode = successorNode.right;
                    if (parentNode.left == successorNode) {
                        parentNode.left = childNode;
                        childNode.parent = parentNode;
                        return true;
                    } else if (parentNode.right == successorNode) {
                        parentNode.right = childNode;
                        childNode.parent = parentNode;
                        return true;
                    }
                }

            }

        }
        return false;
    }

    public BSTree Find(int key, boolean exact) {

        BSTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;

        if (rootOfTree == null) return null; //if tree is empty, return null;
        // uptil here code was common. now we divide the code depending on whether exact == true or false;
        if (exact) {
            BSTree findingNode = rootOfTree;
            while (findingNode != null) {
                if (key < findingNode.key) findingNode = findingNode.left;
                else if (key > findingNode.key) findingNode = findingNode.right;
                else if (key == findingNode.key) {
                    while (findingNode.getPrev() != null && findingNode.key == findingNode.getPrev().key)
                        findingNode = findingNode.getPrev(); //minimize address;
                    return findingNode;
                }
            }
            if (findingNode == null) return null;
        }

        if (!exact) { //we need to find the smallest node whose key >= given key;
            BSTree findingNode = rootOfTree;
            while (findingNode != null) {

                if (key > findingNode.key) findingNode = findingNode.right;
                else if (key == findingNode.key) {
                    while (findingNode.getPrev() != null && findingNode.key == findingNode.getPrev().key)
                        findingNode = findingNode.getPrev(); //minimize address;
                    return findingNode;
                } else if (key < findingNode.key) {
                    BSTree predecessor_findingNode = findingNode.getPrev();
                    if (predecessor_findingNode != null && predecessor_findingNode.key < key) return findingNode;
                    if (predecessor_findingNode == null) return findingNode;

                    if (findingNode.left != null) findingNode = findingNode.left;
                    else if (findingNode.left == null) findingNode = findingNode.getPrev();
                }
            }
            if (findingNode == null) return null;
        }
        return null;



    }

    public BSTree getFirst() {
        BSTree rootOfTree = this;
        while (rootOfTree.parent != null) rootOfTree = rootOfTree.parent; // we are at the head sentinel now;
        rootOfTree = rootOfTree.right; // now we have correctly assigned root to its value;

        if (rootOfTree == null) return null; // if tree is empty return null
        else { // if tree is not empty, go as left as possible;
            BSTree MinNode = rootOfTree;
            while (MinNode.left != null) MinNode = MinNode.left;
            return MinNode;
        }
    }

    public BSTree getNext() { // what if getNext is called on head sentinel? // As clarified by TA we should return null;
        if (this.parent == null) return null;
        BSTree x = this;
        if (x.right != null) { // this means that the right subtree is not empty. successor must be the leftmost node of right subtree;
            BSTree successorNode = x.right;
            while (successorNode.left != null) successorNode = successorNode.left;
            //after completion of this while loop, we have got the successor. return it;
            return successorNode;
        } else { // right subtree is empty, successor is the lowest ancestor of x whose left child is also an ancestor of x;
            BSTree successorNode = x.parent;
            while (successorNode != null && x == successorNode.right) {
                x = successorNode;
                successorNode = successorNode.parent;
            }//if while loop terminates because successorNode becomes null, that means there is no successor of this node, we should return null;
            return successorNode;

        }
    }
    
    public boolean sanity() {
        // first we check whether the the head sentinel's parent is null or not. how to we test that? we keep on doing
        // node.parent from the node on which node.sanity() is called and after a certain number of iterations, say 1,000,000 if
        // we didn't get parent = null, that means there is some kind of cycle where the head sentinel.parent is one
        // of the nodes in the BS tree;
        int i = 1000000;
        BSTree x = this;
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
        BSTree y = x;
        BSTree insertAndFindTree = new BSTree();
        boolean cycleExists = cycleIsPresent(y, insertAndFindTree);
        if (cycleExists) {
            //System.out.println("a cycle was caught without head sentinel being a part of it");
            return false;
        }

        ////////////////////////////////////////////////////////////////


        // now we check whether all the pointers are correctly assigned or not;
        boolean linkagesAreCorrect = checkLinkagesOfTree(x);
        if (!linkagesAreCorrect) {
            //System.out.println("linkages were not correct");
            return false;
        }
        /////////////////////////////////////////////////////////////////////


        // now we check whether the binary search property holds true or not;
        boolean BSPropertyHolds = checkBinarySearchProperty(x);  //if BSPropertyHolds is true, then the given tree is a BSTree;
        if (!BSPropertyHolds) {
            //System.out.println("BS property failed");
            return false;
        }

        ////////////////////////////////////////////////////////////////////////

        //if sanity method has still not returned false, that means given tree is a sane binary search tree, and we should return true;
        return true;
    }


//// EXTRA HELPER FUNCTIONS THAT I HAVE DEFINED

    private BSTree getPrev() {// getPrev will automatically return null if called on head sentinel;
        BSTree x = this;
        if (x.left != null) { // this means that the left subtree is not empty. predecessor must be the rightmost node of left subtree;
            BSTree predecessorNode = x.left;
            while (predecessorNode.right != null) predecessorNode = predecessorNode.right;
            //after completion of this while loop, we have got the predecessor. return it;
            return predecessorNode;
        } else { // left subtree is empty, predecessor is the lowest ancestor of x whose right child is also an ancestor of x;
            BSTree predecessorNode = x.parent;
            while (predecessorNode != null && x == predecessorNode.left) {
                x = predecessorNode;
                predecessorNode = predecessorNode.parent;
            } //if while loop terminates because predecessorNode becomes null, that means there is no predecessor of this node, we should return null;
            return predecessorNode;
        }
    }

    private BSTree getMinofTree(BSTree root) {
        if (root == null) return null;
        BSTree x = root;
        while (x.left != null) x = x.left;
        return x;
    }


    private BSTree getMaxofTree(BSTree root) {
        if (root == null) return null;
        BSTree x = root;
        while (x.right != null) x = x.right;
        return x;
    }


    private boolean isLessThan(BSTree node1, BSTree node2) {
        if (node1.key < node2.key) return true;
        if (node1.key > node2.key) return false;
        // code continues only if keys are equal;
        if (node1.address < node2.address) return true;
        else if (node1.address > node2.address) return false;
        return true; // if both key and address are same, return true; this case also should give false in checkBSTProperty function;
    }


    private boolean isGreaterThan(BSTree node1, BSTree node2) {
        return isLessThan(node2, node1);
    }


    private boolean checkBinarySearchProperty(BSTree root) {
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

    private boolean checkLinkagesOfTree(BSTree root) {
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

    private boolean isPresentInTree(BSTree headSentinel) {
        BSTree x = this;
        BSTree y = headSentinel.right;
        while (y != null) {
            if ((y.key < x.key) || (y.key == x.key && y.address < x.address)) y = y.left;
            if ((y.key > x.key) || (y.key == x.key && y.address > x.address)) y = y.right;
            if (y.key == x.key && y.address == x.address) return true;
        }
        return false;
    }

    private boolean cycleIsPresent(BSTree root, BSTree insertInThisTree) { // it takes in root of the tree as argument;
        if (root == null) return false;
        boolean rootIsPresent = root.isPresentInTree(insertInThisTree);
        if (rootIsPresent) return true;
        // if node didn't already exist, we do preorder traversal and insert a node in this tree;
        insertInThisTree.Insert(root.address, root.size, root.key);
        return (cycleIsPresent(root.left, insertInThisTree) || cycleIsPresent(root.right, insertInThisTree));
    }


}