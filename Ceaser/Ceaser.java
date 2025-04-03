import java.util.Arrays;

public class Ceaser {
    public String intsToStr(int[] nums) {
        String result = "";
        for (int i=0; i<nums.length; i++) {
            result += (char)(nums[i] + 'a');
        }
        return result;
    }

    public int[] strToInts(String s) {
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
        Ceaser pre1 = new Ceaser();
        String s = "hello world";
        int[] nums = pre1.strToInts(s);
        System.out.println(Arrays.toString(nums));
        System.out.println(pre1.intsToStr(nums));
    }
}

