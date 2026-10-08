
import java.util.*;

public class TFIDF {

    // Calculate Term Frequency
    public static double termFrequency(
            String word,
            String document) {

        String[] words =
            document.toLowerCase()
                    .split("\\s+");

        int count = 0;

        for (String w : words) {

            if (w.equals(word.toLowerCase())) {
                count++;
            }
        }

        if (words.length == 0) {
            return 0;
        }

        return (double) count / words.length;
    }


    // Calculate Inverse Document Frequency
    public static double inverseDocumentFrequency(
            String word,
            String[] documents) {

        int count = 0;

        for (String document : documents) {

            String[] words =
                document.toLowerCase()
                        .split("\\s+");

            for (String w : words) {

                if (w.equals(word.toLowerCase())) {

                    count++;
                    break;
                }
            }
        }

        if (count == 0) {
            return 0;
        }

        return Math.log(
            (double) documents.length / count
        );
    }


    // Create TF-IDF vector
    public static double[] createVector(
            String document,
            String[] documents,
            List<String> vocabulary) {

        double[] vector =
            new double[vocabulary.size()];


        for (int i = 0;
             i < vocabulary.size();
             i++) {

            String word =
                vocabulary.get(i);


            double tf =
                termFrequency(
                    word,
                    document
                );


            double idf =
                inverseDocumentFrequency(
                    word,
                    documents
                );


            vector[i] = tf * idf;
        }


        return vector;
    }


    // Cosine Similarity
    public static double cosineSimilarity(
            double[] vectorA,
            double[] vectorB) {

        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;


        for (int i = 0;
             i < vectorA.length;
             i++) {

            dotProduct +=
                vectorA[i] * vectorB[i];

            magnitudeA +=
                vectorA[i] * vectorA[i];

            magnitudeB +=
                vectorB[i] * vectorB[i];
        }


        magnitudeA =
            Math.sqrt(magnitudeA);

        magnitudeB =
            Math.sqrt(magnitudeB);


        if (magnitudeA == 0 ||
            magnitudeB == 0) {

            return 0;
        }


        return dotProduct /
            (magnitudeA * magnitudeB);
    }


    // Build vocabulary
    public static List<String> buildVocabulary(
            String[] documents) {

        Set<String> words =
            new LinkedHashSet<String>();


        for (String document : documents) {

            String[] tokens =
                document.toLowerCase()
                        .split("\\s+");


            for (String token : tokens) {

                words.add(token);
            }
        }


        return new ArrayList<String>(
            words
        );
    }


    // Test TF-IDF
    public static void main(String[] args) {

        String[] topics = {

            "tree binary tree traversal searching",

            "binary tree data structure traversal",

            "avl tree balanced binary search tree",

            "graph traversal bfs dfs",

            "sorting algorithms quick sort merge sort"
        };


        List<String> vocabulary =
            buildVocabulary(topics);


        double[] searchVector =
            createVector(
                topics[0],
                topics,
                vocabulary
            );


        System.out.println(
            "========================================"
        );

        System.out.println(
            "       TF-IDF + COSINE SIMILARITY"
        );

        System.out.println(
            "========================================"
        );

        System.out.println(
            "Search Topic: Tree"
        );

        System.out.println();


        for (int i = 1;
             i < topics.length;
             i++) {

            double[] documentVector =
                createVector(
                    topics[i],
                    topics,
                    vocabulary
                );


            double similarity =
                cosineSimilarity(
                    searchVector,
                    documentVector
                );


            double percentage =
                similarity * 100;


            System.out.println(
                "Document " +
                (i + 1) +
                " Similarity: " +
                String.format(
                    "%.2f",
                    percentage
                ) +
                "%"
            );
        }
    }
}
