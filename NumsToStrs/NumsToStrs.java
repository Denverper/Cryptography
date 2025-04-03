import java.util.Arrays;

public class NumsToStrs {
    public String intsToStr(int[] nums) {
        // Convert an array of integers to a string
        // Each integer is converted to a character by adding 'a' to it
        // The result is a string of characters
        String result = "";
        for (int i=0; i<nums.length; i++) {
            result += (char)(nums[i] + 'a');
        }
        return result;
    }

    public int[] strToInts(String s) {
        // Convert a string to an array of integers
        // Each character is converted to an integer by subtracting 'a' from it
        // The result is an array of integers
        int[] result = new int[s.length()];
        int index = 0;
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') {
                result[index++] = (int)s.charAt(i) - 'a';
            }
        }
        return java.util.Arrays.copyOf(result, index);
    }

    public static void main(String[] args) {
        // Test the conversion methods
        NumsToStrs pre1 = new NumsToStrs();
        String s = "hello world";
        int[] nums = pre1.strToInts(s);
        System.out.println(Arrays.toString(nums));
        System.out.println(pre1.intsToStr(nums));
    }
}

