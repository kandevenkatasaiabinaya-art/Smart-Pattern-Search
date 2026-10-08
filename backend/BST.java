
public class BST {

    // Node of Binary Search Tree
    static class Node {

        String topic;
        String file;
        int index;

        Node left;
        Node right;


        Node(String topic, String file, int index) {

            this.topic = topic;
            this.file = file;
            this.index = index;

        }
    }


    private Node root;


    // Insert a topic into BST
    public void insert(
            String topic,
            String file,
            int index) {

        root = insertNode(
            root,
            topic,
            file,
            index
        );
    }


    private Node insertNode(
            Node node,
            String topic,
            String file,
            int index) {

        if (node == null) {

            return new Node(
                topic,
                file,
                index
            );
        }


        int comparison =
            topic.compareToIgnoreCase(
                node.topic
            );


        if (comparison < 0) {

            node.left =
                insertNode(
                    node.left,
                    topic,
                    file,
                    index
                );

        } else if (comparison > 0) {

            node.right =
                insertNode(
                    node.right,
                    topic,
                    file,
                    index
                );
        }


        return node;
    }


    // Search topic in BST
    public Node search(String topic) {

        Node current = root;


        while (current != null) {

            int comparison =
                topic.compareToIgnoreCase(
                    current.topic
                );


            if (comparison == 0) {

                return current;

            } else if (comparison < 0) {

                current =
                    current.left;

            } else {

                current =
                    current.right;
            }
        }


        return null;
    }


    // Display BST traversal
    public void inorder() {

        inorderTraversal(root);

    }


    private void inorderTraversal(Node node) {

        if (node == null) {
            return;
        }


        inorderTraversal(
            node.left
        );


        System.out.println(
            node.topic +
            " -> " +
            node.file +
            " -> Index " +
            node.index
        );


        inorderTraversal(
            node.right
        );
    }


    // Test BST
    public static void main(String[] args) {

        BST tree = new BST();


        // DSA topics

        tree.insert(
            "tree",
            "DSA_Topics.pdf",
            27
        );


        tree.insert(
            "binary tree",
            "DSA_Topics.pdf",
            31
        );


        tree.insert(
            "avl tree",
            "Advanced_DSA.pdf",
            18
        );


        tree.insert(
            "graph",
            "DSA_Topics.pdf",
            42
        );


        tree.insert(
            "sorting",
            "Algorithms.pdf",
            61
        );


        tree.insert(
            "dynamic programming",
            "Algorithms.pdf",
            88
        );


        // Search

        String searchTopic =
            "tree";


        Node result =
            tree.search(
                searchTopic
            );


        System.out.println(
            "================================"
        );

        System.out.println(
            "       BST SEARCH RESULT"
        );

        System.out.println(
            "================================"
        );


        if (result != null) {

            System.out.println(
                "Topic : " +
                result.topic
            );

            System.out.println(
                "File  : " +
                result.file
            );

            System.out.println(
                "Index : " +
                result.index
            );

        } else {

            System.out.println(
                "Topic not found"
            );
        }


        System.out.println();
        System.out.println(
            "BST Inorder Traversal:"
        );


        tree.inorder();
    }
}
