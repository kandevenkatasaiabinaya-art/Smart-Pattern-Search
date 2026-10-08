public class KMP {

    public static int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {

                    length = lps[length - 1];

                } else {

                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }


    public static int search(String text, String pattern) {

        if (pattern == null || pattern.length() == 0) {
            return 0;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {

                i++;
                j++;

                if (j == pattern.length()) {
                    return i - j;
                }

            } else {

                if (j != 0) {

                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return -1;
    }


    public static void main(String[] args) {

        String text = "tree binary tree AVL tree";

        String pattern = "tree";

        int result = search(text, pattern);

        if (result != -1) {

            System.out.println(
                "KMP: Pattern found at index " + result
            );

        } else {

            System.out.println(
                "KMP: Pattern not found"
            );
        }
    }
}