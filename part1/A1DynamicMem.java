// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {
      
    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).
    // While inserting into the list, only call insert at the head of the list
    // Please note that ALL insertions in the DLL (used either in A1DynamicMem or used independently are to be made at the HEAD (from the front).
    // Also, the find-first should start searching from the head (irrespective of the use for A1DynamicMem). Similar arguments will follow with regards to the ROOT in the case of trees (specifying this in case it was not so trivial to anyone of you earlier)
    public int Allocate(int blockSize) {
        if(blockSize > 0) {
            Dictionary greater_than_blkSize_Node = freeBlk.Find(blockSize, false); // make it into dictionary node! it has to be general;
            if (greater_than_blkSize_Node == null) return -1;
            else {
                int address_of_found_node = greater_than_blkSize_Node.address;
                if (greater_than_blkSize_Node.key == blockSize) {
                    allocBlk.Insert(address_of_found_node, blockSize, address_of_found_node);
                    freeBlk.Delete(greater_than_blkSize_Node);
                    return address_of_found_node;
                } else if (greater_than_blkSize_Node.key > blockSize) {
                    allocBlk.Insert(address_of_found_node, blockSize, address_of_found_node);
                    freeBlk.Insert(address_of_found_node + blockSize, greater_than_blkSize_Node.size - blockSize, greater_than_blkSize_Node.size - blockSize);
                    freeBlk.Delete(greater_than_blkSize_Node);
                    return address_of_found_node;
                }
            }
        }
        return -1;
    } 
    // return 0 if successful, -1 otherwise
    public int Free(int startAddr) {
        if(startAddr < 0) return -1; // if start address = negative, case is invalid---- return -1;
        Dictionary foundNode = allocBlk.Find(startAddr,true);
        if(foundNode == null) return -1;
        else {
            freeBlk.Insert(startAddr, foundNode.size, foundNode.size);
            allocBlk.Delete(foundNode);
            return 0;
        }
    }
}