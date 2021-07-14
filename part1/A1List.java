// Implements Dictionary using Doubly Linked List (DLL)
// Implement the following functions using the specifications provided in the class List

public class A1List extends List {

    private A1List  next; // Next Node
    private A1List prev;  // Previous Node 

    public A1List(int address, int size, int key) { 
        super(address, size, key);
    }
    
    public A1List(){
        super(-1,-1,-1);
        // This acts as a head Sentinel

        A1List tailSentinel = new A1List(-1,-1,-1); // Intiate the tail sentinel
        
        this.next = tailSentinel;
        tailSentinel.prev = this;
    }

    public A1List Insert(int address, int size, int key)
    {
        if(this == null) return null;           //corner case: if the node on which insert is called is null, we can't insert and return null;
        if(this.next == null) return null;          // corner case: we should not insert infront of the tail;

        A1List insertThisNode = new A1List(address,size,key);       // create the node to be inserted;
        A1List nextToCurrentNode = this.next;                  // next node before insertion;

        this.next = insertThisNode;                      //
        insertThisNode.prev = this;                     //      changing the appropriate pointers;
        insertThisNode.next = nextToCurrentNode;        //
        nextToCurrentNode.prev = insertThisNode;        //
        return insertThisNode;              // finally return the inserted node;
    }

    public boolean Delete(Dictionary d) 
    {
        if(this == null) return false;      //corner case: this = null makes no sense; return false;
        if(d == null) return false;         //corner case: node to be deleted is null --> makes no sense --> return false;
        A1List nextNode = this.next;            //
        A1List prevNode = this.prev;            // next and prev nodes will be used for traversing the DLL. flag is created
        boolean flag = false;                   // just to see if node to be deleted has been found or not; in the end we return flag;

        if ((this.key == d.key) && (this.size == d.size) && (this.address == d.address)) {      
            // if the current given node matches with d, then we delete the previous node but first we copy the previous node's data into this node.
            flag = true;

            if(this.prev != null && this.prev.prev != null) {
                int k = this.prev.key;
                int a = this.prev.address;
                int s = this.prev.size;
                this.prev.key = d.key;
                this.prev.address = d.address;
                this.prev.size = d.size;
                this.size = s;
                this.address = a;
                this.key = k;

                return this.Delete(this.prev);
            }
            else if (this.prev != null && this.prev.prev == null) { // "this" becomes the head sentinel
                this.key = this.prev.key;
                this.size = this.prev.size;
                this.address = this.prev.address;
                this.prev = null;
                return true;
            }

        }
        else {
            mySearchingLoop: while (!flag) {
                // first searching for d behind the current node
                // if one of the sentinel nodes needs to be deleted then throw an exception. not implemented yet;
                if (!flag && prevNode != null && prevNode.prev != null) {
                    if ((prevNode.key == d.key) && (prevNode.size == d.size) && (prevNode.address == d.address)) {
                        // then the node to be deleted is found and we make flag = true. we change the appropriate pointers;
                        flag = true;
                        prevNode.prev.next = prevNode.next;
                        prevNode.next.prev = prevNode.prev;
                        break mySearchingLoop;
                    } else prevNode = prevNode.prev;
                }
                // next we search ahead of the current node.
                if (!flag && nextNode != null && nextNode.next != null) {
                    if ((nextNode.key == d.key) && (nextNode.size == d.size) && (nextNode.address == d.address)) {
                        // then the node to be deleted is found and we make flag = true. we change the appropriate pointers;
                        flag = true;
                        nextNode.prev.next = nextNode.next;
                        nextNode.next.prev = nextNode.prev;
                        break mySearchingLoop;
                    } else nextNode = nextNode.next;
                }
                if ((prevNode == null || prevNode.prev == null) && (nextNode.next == null || nextNode == null)) break mySearchingLoop;
            }
        }
        return flag;
    }

    public A1List Find(int k, boolean exact)
    {
        if(this == null) return null;
        A1List nextNode = this.next;
        A1List prevNode = this.prev;
        if(exact) {
            // if the given node of the list has the key = k, then we simply return the current node;
            if (this.key == k) return this;
            // else, we try to find the k ahead or behind the given node;
            myFindingLoop: while(true) {
                if ((nextNode != null) && (nextNode.next != null)) {
                    if(nextNode.key == k) return nextNode;
                    else nextNode = nextNode.next;
                }

                if((prevNode != null) && (prevNode.prev != null)) {
                    if(prevNode.key == k) return prevNode;
                    else prevNode = prevNode.prev;
                }

                if((prevNode==null||prevNode.prev == null) && (nextNode.next == null||nextNode==null)) return null;
            }

        }
        else { //copy paste the above code and change just the == sign to >= sign and we are done;
            // if the given node of the list has the key >= k, then we simply return the current node;
            if (this.key >= k) return this;
            // else, we try to find a node with key greater than or equal to k, ahead or behind the given node;
            myFindingLoop: while(true) {
                if ((nextNode != null) && (nextNode.next != null)) {
                    if(nextNode.key >= k) return nextNode;
                    else nextNode = nextNode.next;
                }

                if((prevNode != null) && (prevNode.prev != null)) {
                    if(prevNode.key >= k) return prevNode;
                    else prevNode = prevNode.prev;
                }

                if((prevNode==null||prevNode.prev == null) && (nextNode.next == null||nextNode==null)) return null;
            }

        }
    }

    public A1List getFirst()
    {
        if(this == null) return null;
        //checking if list is empty or we have been given a sentinel node
        if(this.prev == null || this.next == null) {
            if((this.prev == null) && (this.next.next != null)) return this.next;
            else if ((this.prev == null) && (this.next.next == null)) return null;
            else if ((this.next == null) && (this.prev.prev == null)) return null;
            else if ((this.next == null) && (this.prev.prev != null)) return this.prev.getFirst();
        }

        A1List toBeReturnedNode = this;

        if (this.prev.prev == null) return this; // the given node is the first node in the list;
        //otherwise, go backwards till you find the first node;

        while(toBeReturnedNode.prev.prev != null) toBeReturnedNode = toBeReturnedNode.prev;
        return toBeReturnedNode;
    }
    
    public A1List getNext() 
    {
        if(this == null) return null;           //corner case: if node on which getnext is called is null --> return null
        if((this.next == null)) return null;    //corner case: if node on which getnext is called is tail node --> next is null;

        if (this.next != null) {                                //
            if (this.next.next == null) return null;            //  if the node is just behind the tail node, getnext function should return null;
        }                                                       //
        return this.next;       // otherwise, simply return the next node;
    }

    public boolean sanity()
    {
        if (this == null) return true;

        A1List firstNode = this.getFirst();
        if(firstNode == null) { // that means list is empty;
            if(this.prev == null) { // called on head sentinel
                if (this.next == null) return false; //if there is no tail node, return false because DLL should have a tail sentinel;
                if(this.next.prev != this) return false; // if tail is there and its prev doesn't point to head sentinel, return false;
                if(this.next.next != null) return false; // if tail's next pointer doesn't point to null, return false;
            }
            else if (this.next == null) { //called on tail sentinel
                if(this.prev == null) return false; // if there is no head sentinel node, return false because DLL should have a head sentinel;
                if(this.prev.next != this) return false; // if head is there and its next doesn't point to tail sentinel, return false;
                if(this.prev.prev != null) return false; // if head's prev pointer doesn't point to null, return false;
            }
        }
        else { //that means list has atleast one element other than sentinel nodes;

            //checking if there is a loop in the DLL by FLOYD algorithm -- use two pointers, one goes ahead one step and the other goes ahead two steps at a time;
            // if they land on the same node after some iterations, that means there is a cycle!
            A1List oneStepNode = this.getFirst();
            A1List twoStepNode = this.getFirst();
            checkingLoop: while(oneStepNode != null && twoStepNode != null && twoStepNode.next != null) {
                oneStepNode = oneStepNode.next;
                twoStepNode = twoStepNode.next.next;
                if(twoStepNode == oneStepNode) return false;
            }

            // now we check if all the pointers are assigned correctly or not; check prev and next pointers of all nodes;
            A1List aNode = this.getFirst();
            if(aNode.prev.prev != null) return false; // head sentinel's prev should be null; return false otherwise;
            while(aNode.getNext() != null) {
                if(aNode.next.prev != aNode) return false;
                if(aNode.prev.next != aNode) return false;
                aNode = aNode.getNext();
            }
            //after the end of this while loop, aNode is the pointer to node just before the tail sentinel; check pointers with tail sentinel also;
            if(aNode.next.prev != aNode) return false;
            if(aNode.next.next != null) return false;

        } //if DLL doesn't return false upto now, it is a correctly implemented DLL and is a sane DLL; so return true;
        return true;
    }

}


