// TreeNode.java (for basic BST)
class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}

// BST.java
public class BST {
    TreeNode root;

    public BST() {
        this.root = null;
    }

    public void insert(int value) {
        root = insertRecursive(root, value);
    }

    private TreeNode insertRecursive(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }

        if (value < root.value) {
            root.left = insertRecursive(root.left, value);
        } else if (value > root.value) {
            root.right = insertRecursive(root.right, value);
        }
        return root;
    }

    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(TreeNode root, int value) {
        if (root == null) {
            return false;
        }
        if (value == root.value) {
            return true;
        } else if (value < root.value) {
            return searchRecursive(root.left, value);
        } else {
            return searchRecursive(root.right, value);
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

    private TreeNode deleteRecursive(TreeNode root, int value) {
        if (root == null) {
            return root;
        }

        if (value < root.value) {
            root.left = deleteRecursive(root.left, value);
        } else if (value > root.value) {
            root.right = deleteRecursive(root.right, value);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            TreeNode minNode = minValueNode(root.right);
            root.value = minNode.value;
            root.right = deleteRecursive(root.right, minNode.value);
        }
        return root;
    }

    public void inorderTraversal(TreeNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print(node.value + " ");
            inorderTraversal(node.right);
        }
    }

    public static void main(String[] args) {
        BST bst = new BST();
        bst.insert(55);
        bst.insert(35);
        bst.insert(75);
        bst.insert(25);
        bst.insert(45);
        bst.insert(65);
        bst.insert(85);

        System.out.print("BST Inorder Traversal: ");
        bst.inorderTraversal(bst.root); // Expected: 20 30 40 50 60 70 80
        System.out.println();

        System.out.println("BST Search 40: " + bst.search(45)); // Expected: true
        System.out.println("BST Search 90: " + bst.search(95)); // Expected: false

        bst.delete(35);
        System.out.print("BST Inorder Traversal after deleting 30: ");
        bst.inorderTraversal(bst.root); // Expected: 20 40 50 60 70 80
        System.out.println();

        bst.delete(55);
        System.out.print("BST Inorder Traversal after deleting 50: ");
        bst.inorderTraversal(bst.root); // Expected: 20 40 60 70 80
        System.out.println();
    }
}