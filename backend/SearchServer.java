import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchServer {

    static class Topic {
        String name;
        String file;
        int index;
        String[] related;

        Topic(String name, String file, int index, String... related) {
            this.name = name;
            this.file = file;
            this.index = index;
            this.related = related;
        }
    }

    static class Document {
        String name;
        String text;

        Document(String name, String text) {
            this.name = name;
            this.text = text;
        }
    }

    static class Similarity {
        String name;
        double score;

        Similarity(String name, double score) {
            this.name = name;
            this.score = score;
        }
    }


    /* =====================================================
       DSA TOPIC DATABASE
       ===================================================== */

    static final List<Topic> TOPICS = Arrays.asList(

        new Topic(
            "tree",
            "DSA_Topics.pdf",
            27,
            "Binary Tree",
            "AVL Tree",
            "Binary Search Tree",
            "Tree Traversal"
        ),

        new Topic(
            "binary tree",
            "DSA_Topics.pdf",
            29,
            "Tree",
            "AVL Tree",
            "Binary Search Tree"
        ),

        new Topic(
            "avl tree",
            "DSA_Topics.pdf",
            31,
            "Tree",
            "Binary Tree",
            "Binary Search Tree"
        ),

        new Topic(
            "binary search tree",
            "DSA_Topics.pdf",
            33,
            "Tree",
            "Binary Tree",
            "AVL Tree"
        ),

        new Topic(
            "tree traversal",
            "Trees_and_Graphs.pdf",
            41,
            "Tree",
            "BFS",
            "DFS"
        ),

        new Topic(
            "graph",
            "DSA_Topics.pdf",
            54,
            "BFS",
            "DFS",
            "Directed Graph",
            "Weighted Graph"
        ),

        new Topic(
            "bfs",
            "Graphs_and_Trees.pdf",
            58,
            "Graph",
            "DFS",
            "Tree Traversal"
        ),

        new Topic(
            "dfs",
            "Graphs_and_Trees.pdf",
            60,
            "Graph",
            "BFS",
            "Tree Traversal"
        ),

        new Topic(
            "directed graph",
            "Graphs_and_Trees.pdf",
            63,
            "Graph",
            "DFS",
            "BFS"
        ),

        new Topic(
            "weighted graph",
            "Graphs_and_Trees.pdf",
            65,
            "Graph",
            "Shortest Path",
            "Dijkstra"
        ),

        new Topic(
            "sorting",
            "Algorithms_Basics.pdf",
            80,
            "Merge Sort",
            "Quick Sort",
            "Heap Sort"
        ),

        new Topic(
            "merge sort",
            "Algorithms_Basics.pdf",
            82,
            "Sorting",
            "Quick Sort",
            "Divide and Conquer"
        ),

        new Topic(
            "quick sort",
            "Algorithms_Basics.pdf",
            85,
            "Sorting",
            "Merge Sort",
            "Partition"
        ),

        new Topic(
            "binary search",
            "Algorithms_Basics.pdf",
            72,
            "Searching",
            "BST",
            "Divide and Conquer"
        ),

        new Topic(
            "linked list",
            "Data_Structures.pdf",
            18,
            "Doubly Linked List",
            "Stack",
            "Queue"
        ),

        new Topic(
            "stack",
            "Data_Structures.pdf",
            21,
            "Queue",
            "Linked List",
            "Recursion"
        ),

        new Topic(
            "queue",
            "Data_Structures.pdf",
            24,
            "Stack",
            "Linked List",
            "BFS"
        )
    );


    /* =====================================================
       DOCUMENT DATABASE
       ===================================================== */

    static final List<Document> DOCUMENTS = Arrays.asList(

        new Document(
            "DSA_Topics.pdf",
            "tree binary tree avl tree binary search tree graph searching sorting data structures algorithms"
        ),

        new Document(
            "Trees_and_Graphs.pdf",
            "tree traversal graph bfs dfs directed graph weighted graph spanning tree traversal"
        ),

        new Document(
            "Graphs_and_Trees.pdf",
            "graph bfs dfs directed graph weighted graph tree traversal shortest path dijkstra"
        ),

        new Document(
            "Algorithms_Basics.pdf",
            "sorting merge sort quick sort binary search divide conquer searching recursion algorithm"
        ),

        new Document(
            "Data_Structures.pdf",
            "linked list stack queue data structures recursion tree graph hash table"
        )
    );


    /* =====================================================
       KMP
       ===================================================== */

    static int[] buildLps(String pattern) {

        int[] lps =
            new int[pattern.length()];

        int len = 0;

        int i = 1;


        while (i < pattern.length()) {

            if (
                pattern.charAt(i) ==
                pattern.charAt(len)
            ) {

                lps[i++] =
                    ++len;

            } else if (len > 0) {

                len =
                    lps[len - 1];

            } else {

                lps[i++] =
                    0;
            }
        }


        return lps;
    }


    static int kmpSearch(
            String text,
            String pattern) {

        text =
            text.toLowerCase(Locale.US);

        pattern =
            pattern.toLowerCase(Locale.US);


        if (pattern.length() == 0) {
            return 0;
        }


        if (
            pattern.length() >
            text.length()
        ) {

            return -1;
        }


        int[] lps =
            buildLps(pattern);


        int i = 0;

        int j = 0;


        while (i < text.length()) {

            if (
                text.charAt(i) ==
                pattern.charAt(j)
            ) {

                i++;
                j++;


                if (
                    j ==
                    pattern.length()
                ) {

                    return i - j;
                }

            } else if (j > 0) {

                j =
                    lps[j - 1];

            } else {

                i++;
            }
        }


        return -1;
    }


    /* =====================================================
       EDIT DISTANCE + DYNAMIC PROGRAMMING
       ===================================================== */

    static int editDistance(
            String a,
            String b) {

        a =
            a.toLowerCase(Locale.US);

        b =
            b.toLowerCase(Locale.US);


        int n =
            a.length();

        int m =
            b.length();


        int[][] dp =
            new int[n + 1][m + 1];


        for (int i = 0; i <= n; i++) {

            dp[i][0] =
                i;
        }


        for (int j = 0; j <= m; j++) {

            dp[0][j] =
                j;
        }


        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int cost =
                    a.charAt(i - 1) ==
                    b.charAt(j - 1)
                    ? 0
                    : 1;


                dp[i][j] =
                    Math.min(
                        dp[i - 1][j] + 1,
                        Math.min(
                            dp[i][j - 1] + 1,
                            dp[i - 1][j - 1] + cost
                        )
                    );
            }
        }


        return dp[n][m];
    }


    static Topic closestTopic(
            String query) {

        Topic best =
            TOPICS.get(0);


        int bestDistance =
            Integer.MAX_VALUE;


        for (Topic topic : TOPICS) {

            int distance =
                editDistance(
                    query,
                    topic.name
                );


            if (
                distance <
                bestDistance
            ) {

                bestDistance =
                    distance;

                best =
                    topic;
            }
        }


        return best;
    }


    /* =====================================================
       BINARY SEARCH TREE
       ===================================================== */

    static class BSTNode {

        Topic topic;

        BSTNode left;
        BSTNode right;


        BSTNode(Topic topic) {

            this.topic =
                topic;
        }
    }


    static BSTNode insert(
            BSTNode root,
            Topic topic) {

        if (root == null) {

            return new BSTNode(
                topic
            );
        }


        if (
            topic.name.compareTo(
                root.topic.name
            ) < 0
        ) {

            root.left =
                insert(
                    root.left,
                    topic
                );

        } else if (
            topic.name.compareTo(
                root.topic.name
            ) > 0
        ) {

            root.right =
                insert(
                    root.right,
                    topic
                );
        }


        return root;
    }


    static BSTNode buildBST() {

        BSTNode root =
            null;


        for (Topic topic : TOPICS) {

            root =
                insert(
                    root,
                    topic
                );
        }


        return root;
    }


    static class BSTResult {

        Topic topic;

        List<String> path =
            new ArrayList<String>();
    }


    static BSTResult bstSearch(
            BSTNode root,
            String query) {

        BSTResult result =
            new BSTResult();


        BSTNode current =
            root;


        while (current != null) {

            result.path.add(
                current.topic.name
            );


            int compare =
                query.compareTo(
                    current.topic.name
                );


            if (compare == 0) {

                result.topic =
                    current.topic;

                return result;
            }


            if (compare < 0) {

                current =
                    current.left;

            } else {

                current =
                    current.right;
            }
        }


        return result;
    }


    /* =====================================================
       TF-IDF + COSINE SIMILARITY
       ===================================================== */

    static List<String> tokens(
            String text) {

        String cleaned =
            text
                .toLowerCase(Locale.US)
                .replaceAll(
                    "[^a-z0-9 ]",
                    " "
                )
                .trim();


        if (cleaned.length() == 0) {

            return new ArrayList<String>();
        }


        String[] raw =
            cleaned.split("\\s+");


        List<String> result =
            new ArrayList<String>();


        for (String word : raw) {

            if (word.length() > 0) {

                result.add(word);
            }
        }


        return result;
    }


    static double termFrequency(
            List<String> words,
            String term) {

        if (words.isEmpty()) {

            return 0;
        }


        int count =
            0;


        for (String word : words) {

            if (
                word.equals(term)
            ) {

                count++;
            }
        }


        return
            (double) count /
            words.size();
    }


    static double inverseDocumentFrequency(
            String term) {

        int docs =
            DOCUMENTS.size();


        int containing =
            0;


        for (Document doc : DOCUMENTS) {

            if (
                tokens(doc.text)
                    .contains(term)
            ) {

                containing++;
            }
        }


        return
            Math.log(
                (double) (docs + 1) /
                (containing + 1)
            )
            + 1.0;
    }


    static double cosineSimilarity(
            String query,
            Document doc) {

        List<String> queryWords =
            tokens(query);

        List<String> documentWords =
            tokens(doc.text);


        Set<String> vocabulary =
            new HashSet<String>();


        vocabulary.addAll(
            queryWords
        );

        vocabulary.addAll(
            documentWords
        );


        if (vocabulary.isEmpty()) {

            return 0;
        }


        double dot = 0;

        double queryNorm = 0;

        double documentNorm = 0;


        for (
            String term :
            vocabulary
        ) {

            double idf =
                inverseDocumentFrequency(
                    term
                );


            double queryWeight =
                termFrequency(
                    queryWords,
                    term
                ) * idf;


            double documentWeight =
                termFrequency(
                    documentWords,
                    term
                ) * idf;


            dot +=
                queryWeight *
                documentWeight;


            queryNorm +=
                queryWeight *
                queryWeight;


            documentNorm +=
                documentWeight *
                documentWeight;
        }


        if (
            queryNorm == 0 ||
            documentNorm == 0
        ) {

            return 0;
        }


        return
            (
                dot /
                (
                    Math.sqrt(queryNorm) *
                    Math.sqrt(documentNorm)
                )
            ) * 100.0;
    }


    static List<Similarity> getSimilarities(
            String query) {

        List<Similarity> result =
            new ArrayList<Similarity>();


        for (Document document :
             DOCUMENTS) {

            result.add(
                new Similarity(
                    document.name,
                    cosineSimilarity(
                        query,
                        document
                    )
                )
            );
        }


        Collections.sort(
            result,
            new Comparator<Similarity>() {

                public int compare(
                        Similarity a,
                        Similarity b) {

                    return Double.compare(
                        b.score,
                        a.score
                    );
                }
            }
        );


        return result;
    }


    /* =====================================================
       JSON HELPERS
       ===================================================== */

    static String esc(
            String value) {

        if (value == null) {

            return "";
        }


        return value
            .replace(
                "\\",
                "\\\\"
            )
            .replace(
                "\"",
                "\\\""
            )
            .replace(
                "\n",
                " "
            );
    }


    static String titleCase(
            String text) {

        if (
            text == null ||
            text.length() == 0
        ) {

            return text;
        }


        String[] parts =
            text.split(" ");


        StringBuilder result =
            new StringBuilder();


        for (String part : parts) {

            if (part.length() == 0) {
                continue;
            }


            if (result.length() > 0) {

                result.append(' ');
            }


            result.append(
                Character.toUpperCase(
                    part.charAt(0)
                )
            );


            if (part.length() > 1) {

                result.append(
                    part.substring(1)
                );
            }
        }


        return result.toString();
    }


    static String topicJson(
            Topic topic,
            String match) {

        return
            "{\"file\":\"" +
            esc(topic.file) +
            "\",\"name\":\"" +
            esc(titleCase(topic.name)) +
            "\",\"index\":" +
            topic.index +
            ",\"match\":\"" +
            esc(match) +
            "\"}";
    }


    /* =====================================================
       CREATE SEARCH JSON
       ===================================================== */

    static String buildJson(
            String rawQuery) {

        String query =
            rawQuery == null
            ? "tree"
            : rawQuery
                .trim()
                .toLowerCase(Locale.US);


        if (query.length() == 0) {

            query =
                "tree";
        }


        /* KMP */

        Topic exact =
            null;


        int exactPosition =
            -1;


        for (Topic topic :
             TOPICS) {

            int position =
                kmpSearch(
                    topic.name,
                    query
                );


            if (
                position >= 0 &&
                topic.name.equals(query)
            ) {

                exact =
                    topic;

                exactPosition =
                    position;

                break;
            }
        }


        /* Fuzzy */

        Topic fuzzy =
            exact != null
            ? exact
            : closestTopic(query);


        int distance =
            editDistance(
                query,
                fuzzy.name
            );


        /* BST */

        BSTResult bst =
            bstSearch(
                buildBST(),
                fuzzy.name
            );


        Topic located =
            bst.topic != null
            ? bst.topic
            : fuzzy;


        /* Similarity */

        List<Similarity> similarities =
            getSimilarities(query);


        StringBuilder json =
            new StringBuilder();


        json.append("{");


        json.append(
            "\"query\":\""
        )
        .append(
            esc(query)
        )
        .append(
            "\","
        );


        /* KMP */

        json.append(
            "\"kmp\":{"
        );

        json.append(
            "\"found\":"
        );

        json.append(
            exact != null
        );

        json.append(",");


        json.append(
            "\"message\":\""
        );


        if (exact != null) {

            json.append(
                "Exact topic found using KMP"
            );

        } else {

            json.append(
                "Exact topic not found; fuzzy matching was used"
            );
        }


        json.append(
            "\","
        );


        json.append(
            "\"position\":"
        );


        if (exactPosition >= 0) {

            json.append(
                exactPosition
            );

        } else {

            json.append(
                "null"
            );
        }


        json.append(
            "},"
        );


        /* FUZZY */

        json.append(
            "\"fuzzy\":{"
        );


        json.append(
            "\"topic\":\""
        )
        .append(
            esc(
                titleCase(
                    fuzzy.name
                )
            )
        )
        .append(
            "\","
        );


        json.append(
            "\"distance\":"
        )
        .append(
            distance
        );


        json.append(
            "},"
        );


        /* BST */

        json.append(
            "\"bst\":{"
        );


        json.append(
            "\"found\":"
        )
        .append(
            bst.topic != null
        )
        .append(
            ","
        );


        json.append(
            "\"topic\":\""
        )
        .append(
            esc(
                titleCase(
                    located.name
                )
            )
        )
        .append(
            "\","
        );


        json.append(
            "\"file\":\""
        )
        .append(
            esc(
                located.file
            )
        )
        .append(
            "\","
        );


        json.append(
            "\"index\":"
        )
        .append(
            located.index
        )
        .append(
            ","
        );


        json.append(
            "\"path\":["
        );


        for (
            int i = 0;
            i < bst.path.size();
            i++
        ) {

            if (i > 0) {

                json.append(',');
            }


            json.append(
                "\""
            )
            .append(
                esc(
                    bst.path.get(i)
                )
            )
            .append(
                "\""
            );
        }


        json.append(
            "]},"
        );


        /* RELATED TOPICS */

        String[] related =
            located.related;


        json.append(
            "\"relatedTopics\":["
        );


        for (
            int i = 0;
            i < related.length;
            i++
        ) {

            if (i > 0) {

                json.append(',');
            }


            json.append(
                "\""
            )
            .append(
                esc(
                    related[i]
                )
            )
            .append(
                "\""
            );
        }


        json.append(
            "],"
        );


        /* TF-IDF */

        json.append(
            "\"similarity\":["
        );


        for (
            int i = 0;
            i < similarities.size();
            i++
        ) {

            if (i > 0) {

                json.append(',');
            }


            Similarity similarity =
                similarities.get(i);


            json.append(
                "{\"topic\":\""
            )
            .append(
                esc(
                    titleCase(
                        similarity.name
                    )
                )
            )
            .append(
                "\",\"score\":"
            )
            .append(
                String.format(
                    Locale.US,
                    "%.2f",
                    similarity.score
                )
            )
            .append(
                "}"
            );
        }


        json.append(
            "],"
        );


        /* DOCUMENTS */

        json.append(
            "\"documents\":["
        );


        json.append(
            topicJson(
                located,
                exact != null
                ? "Exact Match"
                : "Fuzzy Match"
            )
        );


        for (
            String relatedTopic :
            related
        ) {

            for (Topic topic :
                 TOPICS) {

                if (
                    topic.name.equalsIgnoreCase(
                        relatedTopic
                    )
                ) {

                    json.append(
                        ","
                    );


                    json.append(
                        topicJson(
                            topic,
                            "Related Topic"
                        )
                    );


                    break;
                }
            }
        }


        json.append(
            "],"
        );


        /* ENGINE STATUS */

        json.append(
            "\"engine\":{"
        );


        json.append(
            "\"kmp\":true,"
        );

        json.append(
            "\"editDistance\":true,"
        );

        json.append(
            "\"bst\":true,"
        );

        json.append(
            "\"tfidf\":true,"
        );

        json.append(
            "\"cosineSimilarity\":true"
        );


        json.append(
            "}"
        );


        json.append(
            "}"
        );


        return json.toString();
    }


    /* =====================================================
       HTTP HANDLER
       ===================================================== */

    static class SearchHandler
            implements HttpHandler {

        public void handle(
                HttpExchange exchange)
                throws IOException {


            Headers headers =
                exchange.getResponseHeaders();


            headers.set(
                "Access-Control-Allow-Origin",
                "*"
            );


            headers.set(
                "Access-Control-Allow-Methods",
                "GET, OPTIONS"
            );


            headers.set(
                "Access-Control-Allow-Headers",
                "Content-Type"
            );


            headers.set(
                "Content-Type",
                "application/json; charset=UTF-8"
            );


            if (
                "OPTIONS".equalsIgnoreCase(
                    exchange.getRequestMethod()
                )
            ) {

                exchange.sendResponseHeaders(
                    204,
                    -1
                );

                return;
            }


            String query =
                "tree";


            String raw =
                exchange
                    .getRequestURI()
                    .getRawQuery();


            if (raw != null) {

                for (
                    String item :
                    raw.split("&")
                ) {

                    if (
                        item.startsWith("q=")
                    ) {

                        query =
                            URLDecoder.decode(
                                item.substring(2),
                                "UTF-8"
                            );
                    }
                }
            }


            byte[] response =
                buildJson(
                    query
                )
                .getBytes(
                    StandardCharsets.UTF_8
                );


            exchange.sendResponseHeaders(
                200,
                response.length
            );


            OutputStream output =
                exchange.getResponseBody();


            output.write(
                response
            );


            output.close();
        }
    }


    /* =====================================================
       MAIN SERVER
       ===================================================== */

    public static void main(
            String[] args)
            throws Exception {


        HttpServer server =
            HttpServer.create(
                new InetSocketAddress(8080),
                0
            );


        server.createContext(
            "/api/search",
            new SearchHandler()
        );


        server.start();


        System.out.println(
            "==============================================="
        );


        System.out.println(
            " Smart Pattern Search DSA Server is running!"
        );


        System.out.println(
            " http://localhost:8080/api/search?q=tree"
        );


        System.out.println(
            " Algorithms: KMP | Edit Distance | BST | TF-IDF"
        );


        System.out.println(
            "==============================================="
        );
    }
}