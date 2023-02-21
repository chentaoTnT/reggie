import java.util.*;

public class Solution {


    public static void main(String[] args) {
        Solution ss = new Solution();
        String s = "applepenapple";
        List<String> wordDict = new ArrayList<>(Arrays.asList("apple", "pen"));
        System.out.println(ss.wordBreak(s, wordDict));
    }


    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = 0; i <= s.length(); i++) {
            for (int j = 0; j < wordDict.size(); j++) {
                if (i - wordDict.get(j).length() >= 0 && set.contains(s.substring(i - wordDict.get(j).length(), i))) {
                    dp[i] = dp[i] || dp[i - wordDict.get(j).length()];
                }
            }
        }
        return dp[s.length()];
    }
}
