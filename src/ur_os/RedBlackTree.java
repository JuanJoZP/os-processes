package ur_os;

/**
 *
 * @author juanjozp
 */
enum Color {
    RED, BLACK
}

// Node Class
class Node {
    Double data;
    Node left;
    Node right;
    Node parent;
    Color color;
    Process id;

    // Constructor to create a new node
    Node(Process p) {
        if (p == null) {
            this.data = Double.MAX_VALUE; // Un valor arbitrario para nodos TNULL
            this.id = null;
        } else {
            this.data = p.getVruntime();
            this.id = p;
        }
        this.color = Color.RED;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public Double GetData(){
            return this.data;
    }
    public Process GetProcess(){
        return this.id;
    }
}


public class RedBlackTree {
    private Node root;
    private final Node TNULL; // Sentinel node for null references
    public int size = 0;
    public boolean allVrun = true;
    // Constructor to initialize the Red-Black Tree
    public RedBlackTree() {
        TNULL = new Node (null);
        TNULL.color = Color.BLACK;
        root = TNULL;
    }

    // Preorder traversal helper function
    private void preOrderHelper(Node node) {
        if (node != TNULL) {
            System.out.print(node.data + " ");
            preOrderHelper(node.left);
            preOrderHelper(node.right);
        }
    }

    // Function to start preorder traversal
    public void preorder() {
        preOrderHelper(this.root);
    }

    // Inorder traversal helper function
    private void inOrderHelper(Node node) {
        if (node != TNULL) {
            inOrderHelper(node.left);
            System.out.print(node.data + " ");
            inOrderHelper(node.right);
        }
    }

    // Function to start inorder traversal
    public void inorder() {
        inOrderHelper(this.root);
    }

    // Postorder traversal helper function
    private void postOrderHelper(Node node) {
        if (node != TNULL) {
            postOrderHelper(node.left);
            postOrderHelper(node.right);
            System.out.print(node.data + " ");
        }
    }

    // Function to start postorder traversal
    public void postorder() {
        postOrderHelper(this.root);
    }

    // Function to perform left rotation
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Function to perform right rotation
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
    public boolean isEmpty(){
        return root==TNULL;
    }
    // Function to insert a new node
    public void insert(Process key) {
        
        Node node = new Node (key);
        node.parent = null;
        node.left = TNULL;
        node.right = TNULL;
        node.color = Color.RED; // New node must be red

        Node y = null;
        Node x = this.root;

        // Find the correct position to insert the new node
        while (x != TNULL) {
            y = x;
            if (node.data.compareTo(x.data) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data.compareTo(y.data) < 0) {
            y.left = node;
        } else {
            y.right = node;
        }
        size++; //size of the tree
        // Fix the tree if the properties are violated
        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }
        
        fixInsert(node);
    }

    // Function to fix violations after insertion
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;

                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = Color.BLACK;
    }
    public Node getLeftmostNode() {
        if (isEmpty()) {
            return null;  // Retornar null si el árbol está vacío
        }
        return getLeftmostNodeHelper(this.root);
    }

    private Node getLeftmostNodeHelper(Node node) {
        while (node.left != TNULL) {  // Usar TNULL en lugar de null
            node = node.left;
        }
        return node;
    }

    public void deleteLeftmostNode() {
        if (isEmpty()) {
            System.out.println("El árbol está vacío.");
            return;
        }
        Node nodeToDelete = getLeftmostNode();
        if (nodeToDelete != null) {
            size--;
            deleteNode(nodeToDelete);  // Eliminar el nodo más a la izquierda
        }
    }


    private void deleteNode(Node node) {
        Node y = node;
        Node x;
        Color originalColor = y.color;

        if (node.left == TNULL) {
            x = node.right;
            transplant(node, node.right);
        } else if (node.right == TNULL) {
            x = node.left;
            transplant(node, node.left);
        } else {
            y = getMinimum(node.right);
            originalColor = y.color;
            x = y.right;

            if (y.parent == node) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = node.right;
                y.right.parent = y;
            }

            transplant(node, y);
            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        if (originalColor == Color.BLACK) {
            fixDelete(x);
        }
    }

// Método auxiliar para reemplazar un nodo por otro en el árbol
    private void transplant(Node target, Node with) {
        if (target.parent == null) {
            root = with;
        } else if (target == target.parent.left) {
            target.parent.left = with;
        } else {
            target.parent.right = with;
        }
        with.parent = target.parent;
    }

// Método auxiliar para encontrar el nodo mínimo de un subárbol
    private Node getMinimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

// Método para corregir el árbol después de una eliminación
    private void fixDelete(Node x) {
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if (w.left.color == Color.BLACK && w.right.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Color.BLACK) {
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == Color.RED) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if (w.right.color == Color.BLACK && w.left.color == Color.BLACK) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Color.BLACK) {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }
    private boolean GetAllRuntimeHelper(Node node, double n){
        if(node!=TNULL){
            if(node.GetData()< n){
            allVrun = false;
            }
            GetAllRuntimeHelper(node.left,n);
            GetAllRuntimeHelper(node.right,n);
        }
        return allVrun;
    }
    public boolean GetAllRuntime(double n){
        return GetAllRuntimeHelper(this.root,n);
    }
        
    private void restartVruntimeHelper(Node node){
        if(node != TNULL){
            node.GetProcess().setVruntime(0);
            restartVruntimeHelper(node.left);
            restartVruntimeHelper(node.right);
            
        }
    }
    public void restartVruntime(){
        restartVruntimeHelper(this.root);
    }

}
