// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.
//@Abhinav Singhal - 2019CS50768

public class A2DynamicMem extends A1DynamicMem {
      
    public A2DynamicMem() {  super(); }

    public A2DynamicMem(int size) { super(size); }

    public A2DynamicMem(int size, int dict_type) { super(size, dict_type); }

    // In A2, you need to test your implementation using BSTrees and AVLTrees. 
    // No changes should be required in the A1DynamicMem functions. 
    // They should work seamlessly with the newly supplied implementation of BSTrees and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test using BSTrees and AVLTrees. 
    //Your BST (and AVL tree) implementations should obey the property that keys in the left subtree <= root.key < keys in the right subtree. How is this total order between blocks defined? It shouldn't be a problem when using key=address since those are unique (this is an important invariant for the entire assignment123 module). When using key=size, use address to break ties i.e. if there are multiple blocks of the same size, order them by address. Now think outside the scope of the allocation problem and think of handling tiebreaking in blocks, in case key is neither of the two. 
    public void Defragment() {
        // "this" keyword will be used for freeblk since we always have to defragment the freeblk only;
        // defragment will be implemented on freeBlk which has key = size. addresses may be randomly present in the tree;
        // create new tree having address as its key; so that inorder traversal of this tree results in getting addresses in sorted order;

        // in the end, we will change the pointer of freeblk to the newly created sizeAsKeyTree;
        if(freeBlk == null || freeBlk.getFirst() == null) return;
        Dictionary addressAsKeyTree = null,sizeAsKeyTree=null;
        if(this.type == 2) {
            addressAsKeyTree = new BSTree();
            sizeAsKeyTree = new BSTree();
        }
        else if(this.type == 3) {
            addressAsKeyTree = new AVLTree();
            sizeAsKeyTree = new AVLTree();
        }
        //first we will add all the nodes from the freeblk to the addressAsKeyTree by inorder traversal of freeblk;
        Dictionary traversalNode1 = freeBlk.getFirst();
        while(traversalNode1 != null) {
            if(addressAsKeyTree!=null) {
                addressAsKeyTree.Insert(traversalNode1.address,traversalNode1.size,traversalNode1.address);
                traversalNode1 = traversalNode1.getNext();
            }
        }
        // we know that addressAsKeyTree cannot be null because we have come till here only if freeBlk was not null;
        Dictionary traversalNode2 = addressAsKeyTree.getFirst();
        while(traversalNode2 != null) {
            Dictionary insightNode = traversalNode2;
            int a = traversalNode2.address;
            while((insightNode.getNext()!= null) && (insightNode.address+insightNode.size == insightNode.getNext().address)) insightNode = insightNode.getNext();
            traversalNode2 = insightNode;
            traversalNode2.size = traversalNode2.address-a + traversalNode2.size;
            traversalNode2.address = a;
            traversalNode2.key = traversalNode2.address;
            sizeAsKeyTree.Insert(traversalNode2.address, traversalNode2.size, traversalNode2.size);
            traversalNode2 = traversalNode2.getNext();
        }
        freeBlk = sizeAsKeyTree;
        return ;
    }
}