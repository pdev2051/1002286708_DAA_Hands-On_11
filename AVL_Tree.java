// TreeNode.java (shared for BST and AVL)
class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    int height;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}

// AVLTree.java
class AVLTree {
    TreeNode root;

    public AVLTree() {
        this.root = null;
    }

    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private void updateHeight(TreeNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        // Return new root
        return x;
    }

    private TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Return new root
        return y;
    }

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private TreeNode insertRecursive(TreeNode node, int value) {
        if (node == null) {
            return new TreeNode(value);
        }

        if (value < node.value) {
            node.left = insertRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = insertRecursive(node.right, value);
        } else {
            return node; // Duplicate keys not allowed
        }

        // Update height of current node
        updateHeight(node);

        // Get the balance factor of this node
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && value < node.left.value) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && value > node.right.value) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // Return unchanged node pointer
    }

    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(TreeNode node, int value) {
        if (node == null) {
            return false;
        }
        if (value == node.value) {
            return true;
        } else if (value < node.value) {
            return searchRecursive(node.left, value);
        } else {
            return searchRecursive(node.right, value);
        }
    }

    private TreeNode minValueNode(TreeNode node) {
        TreeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private TreeNode deleteRecursive(TreeNode node, int value) {
        if (node == null) {
            return node;
        }

        if (value < node.value) {
            node.left = deleteRecursive(node.left, value);
        } else if (value > node.value) {
            node.right = deleteRecursive(node.right, value);
        } else {
            if ((node.left == null) || (node.right == null)) {
                TreeNode temp = null;
                if (node.left != null) {
                    temp = node.left;
                } else {
                    temp = node.right;
                }

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp; // Copy the contents of the non-empty child
                }
            } else {
                TreeNode temp = minValueNode(node.right);
                node.value = temp.value;
                node.right = deleteRecursive(node.right, temp.value);
            }
        }

        if (node == null) {
            return node;
        }

        // Update height of current node
        updateHeight(node);

        // Get the balance factor of this node
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void inorderTraversal() {
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(TreeNode node) {
        if (node != null) {
            inorderRecursive(node.left);
            System.out.print(node.value + " ");
            inorderRecursive(node.right);
        }
    }

    public static void main(String[] args) {
        AVLTree avl = new AVLTree();

        avl.insert(50);
        avl.insert(30);
        avl.insert(70);
        avl.insert(20);
        avl.insert(40);
        avl.insert(60);
        avl.insert(80);

        System.out.print("AVL Tree Inorder Traversal: ");
        avl.inorderTraversal(); // Expected: 20 30 40 50 60 70 80
        System.out.println("AVL Tree Search 40: " + avl.search(40)); // Expected: true
        System.out.println("AVL Tree Search 90: " + avl.search(90)); // Expected: false

        avl.delete(30);
        System.out.print("AVL Tree Inorder Traversal after deleting 30: ");
        avl.inorderTraversal(); // Expected: 20 40 50 60 70 80

        avl.delete(50);
        System.out.print("AVL Tree Inorder Traversal after deleting 50: ");
        avl.inorderTraversal(); // Expected: 20 40 60 70 80

        System.out.println("\nMore AVL Tree Tests:");
        AVLTree avl2 = new AVLTree();
        avl2.insert(9);
        avl2.insert(5);
        avl2.insert(10);
        avl2.insert(0);
        avl2.insert(6);
        avl2.insert(11);
        avl2.insert(-1);
        avl2.insert(1);
        avl2.insert(2);

        System.out.print("AVL Tree Inorder Traversal 2: ");
        avl2.inorderTraversal(); // Expected: -1 0 1 2 5 6 9 10 11

        avl2.delete(10);
        System.out.print("AVL Tree Inorder Traversal 2 after deleting 10: ");
        avl2.inorderTraversal(); // Expected: -1 0 1 2 5 6 9 11

        avl2.delete(5);
        System.out.print("AVL Tree Inorder Traversal 2 after deleting 5: ");
        avl2.inorderTraversal(); // Expected: -1 0 1 2 6 9 11
    }
}