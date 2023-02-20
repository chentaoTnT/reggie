import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Solution {



    public static void main(String[] args) {
        Solution ss = new Solution();
        int n = 14;
        System.out.println(ss.numSquares(n));
    }




    public int numSquares(int n) {
        int[]dp = new int[n + 1];
        for (int i = 0; i < n + 1; i++) {
            dp[i] = i;
        }
        int maxNum = (int) Math.floor(Math.sqrt(n));
        for (int i = 1; i < maxNum + 1; i++) {
            int square = i * i;
            for (int j = 1; j <= n; j++) {
                if(j >= square) {
                    dp[j] = Math.min(dp[j], dp[j - square] + 1);
                }
            }
        }
        return dp[n];
    }
}
