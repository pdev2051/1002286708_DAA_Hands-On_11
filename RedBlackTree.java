// RBNode.java (for Red-Black Tree)
class RBNode {
    int value;
    RBNode left;
    RBNode right;
    RBNode parent;
    boolean color; // true for RED, false for BLACK

    public RBNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = true; // New nodes are always RED
    }
}

// RedBlackTree.java
public class RedBlackTree {
    private RBNode root;
    private final RBNode NIL;

    public RedBlackTree() {
        NIL = new RBNode(0);
        NIL.color = false; // NIL node is always BLACK
        root = NIL;
    }

    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != NIL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RBNode y) {
        RBNode x = y.left;
        y.left = x.right;
        if (x.right != NIL) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        x.right = y;
        y.parent = x;
    }

    public void insert(int value) {
        RBNode z = new RBNode(value);
        z.left = NIL;
        z.right = NIL;
        RBNode y = null;
        RBNode x = root;

        while (x != NIL) {
            y = x;
            if (z.value < x.value) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y == null) {
            root = z;
        } else if (z.value < y.value) {
            y.left = z;
        } else {
            y.right = z;
        }

        insertFixUp(z);
    }

    private void insertFixUp(RBNode z) {
        while (z.parent != null && z.parent.color) { // While parent is RED
            if (z.parent == z.parent.parent.left) {
                RBNode y = z.parent.parent.right;
                if (y.color) { // Case 1: Uncle y is RED
                    z.parent.color = false; // Parent becomes BLACK
                    y.color = false;       // Uncle becomes BLACK
                    z.parent.parent.color = true; // Grandparent becomes RED
                    z = z.parent.parent;
                } else { // Case 2: Uncle y is BLACK
                    if (z == z.parent.right) { // Case 2a: z is right child
                        z = z.parent;
                        leftRotate(z);
                    }
                    // Case 2b: z is left child
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    rightRotate(z.parent.parent);
                }
            } else { // Same as then clause with "left" and "right" exchanged
                RBNode y = z.parent.parent.left;
                if (y.color) { // Case 1: Uncle y is RED
                    z.parent.color = false; // Parent becomes BLACK
                    y.color = false;       // Uncle becomes BLACK
                    z.parent.parent.color = true; // Grandparent becomes RED
                    z = z.parent.parent;
                } else { // Case 2: Uncle y is BLACK
                    if (z == z.parent.left) { // Case 2a: z is left child
                        z = z.parent;
                        rightRotate(z);
                    }
                    // Case 2b: z is right child
                    z.parent.color = false;
                    z.parent.parent.color = true;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = false; // Root is always BLACK
    }

    public boolean search(int value) {
        RBNode node = root;
        while (node != NIL) {
            if (value == node.value) {
                return true;
            } else if (value < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    private RBNode minimum(RBNode node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }

    private void transplant(RBNode u, RBNode v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    public void delete(int value) {
        RBNode z = searchNode(value);
        if (z == NIL) {
            return;
        }

        RBNode y = z;
        boolean yOriginalColor = y.color;
        RBNode x;

        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (!yOriginalColor) {
            deleteFixUp(x);
        }
    }

    private void deleteFixUp(RBNode x) {
        while (x != root && !x.color) { // While x is not root and x is BLACK
            if (x == x.parent.left) {
                RBNode w = x.parent.right;
                if (w.color) { // Case 1: w is RED
                    w.color = false;
                    x.parent.color = true;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.color && !w.right.color) { // Case 2: w's children are BLACK
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.right.color) { // Case 3: w's right child is BLACK
                        w.left.color = false;
                        w.color = true;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Case 4: w's right child is RED
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.right.color = false;
                    leftRotate(x.parent);
                    x = root;
                }
            } else { // Same as then clause with "left" and "right" exchanged
                RBNode w = x.parent.left;
                if (w.color) { // Case 1: w is RED
                    w.color = false;
                    x.parent.color = true;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (!w.right.color && !w.left.color) { // Case 2: w's children are BLACK
                    w.color = true;
                    x = x.parent;
                } else {
                    if (!w.left.color) { // Case 3: w's left child is BLACK
                        w.right.color = false;
                        w.color = true;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    // Case 4: w's left child is RED
                    w.color = x.parent.color;
                    x.parent.color = false;
                    w.left.color = false;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = false;
    }

    private RBNode searchNode(int value) {
        RBNode current = root;
        while (current != NIL) {
            if (value == current.value) {
                return current;
            } else if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return NIL;
    }

    public void inorderTraversal() {
        inorderTraversalRecursive(root);
        System.out.println();
    }

    private void inorderTraversalRecursive(RBNode node) {
        if (node != NIL) {
            inorderTraversalRecursive(node.left);
            System.out.print(node.value + (node.color ? "(R) " : "(B) ") + " ");
            inorderTraversalRecursive(node.right);
        }
    }

    public static void main(String[] args) {
        RedBlackTree rbt = new RedBlackTree();
        rbt.insert(50);
        rbt.insert(30);
        rbt.insert(70);
        rbt.insert(20);
        rbt.insert(40);
        rbt.insert(60);
        rbt.insert(80);

        System.out.print("Red-Black Tree Inorder Traversal: ");
        rbt.inorderTraversal(); // Expected (values should be in order, colors may vary)

        System.out.println("Red-Black Tree Search 40: " + rbt.search(40)); // Expected: true
        System.out.println("Red-Black Tree Search 90: " + rbt.search(90)); // Expected: false

        rbt.delete(30);
        System.out.print("Red-Black Tree Inorder Traversal after deleting 30: ");
        rbt.inorderTraversal(); // Expected (values should be in order, colors may vary)

        rbt.delete(50);
        System.out.print("Red-Black Tree Inorder Traversal after deleting 50: ");
        rbt.inorderTraversal(); // Expected (values should be in order, colors may vary)
    }
}
