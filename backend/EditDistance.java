public class EditDistance {

    public static int calculate(String a, String b) {

        int rows = a.length() + 1;
        int cols = b.length() + 1;

        int[][] dp = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j < cols; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i < rows; i++) {

            for (int j = 1; j < cols; j++) {

                if (a.charAt(i - 1) == b.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];

                    dp[i][j] = 1 + Math.min(
                        insert,
                        Math.min(delete, replace)
                    );
                }
            }
        }

        return dp[rows - 1][cols - 1];
    }

    public static void main(String[] args) {

        String userInput = "tre";
        String actualTopic = "tree";

        int distance = calculate(
            userInput,
            actualTopic
        );

        System.out.println("Input: " + userInput);
        System.out.println("Closest Topic: " + actualTopic);
        System.out.println("Edit Distance: " + distance);
    }
}