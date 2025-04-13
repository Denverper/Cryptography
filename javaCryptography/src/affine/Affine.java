package affine;

import java.util.HashMap;

public class Affine {
    public static final HashMap<Integer, Integer> inverses = new HashMap<>();
    static {
        inverses.put(1, 1); 
        inverses.put(3, 9);
        inverses.put(5, 21);
        inverses.put(7, 15);
        inverses.put(9, 3);
        inverses.put(11, 19);
        inverses.put(15, 7);
        inverses.put(17, 23);
        inverses.put(19, 11);
        inverses.put(21, 5);
        inverses.put(23, 17);
        inverses.put(25, 25);
    }


    public static String encryptAffine(String plainText, int a, int b) {
        StringBuilder cipherText = new StringBuilder();
        plainText = plainText.toLowerCase();
        for (char c : plainText.toCharArray()){
            cipherText.append((char) ((((c - 'a')*a + b)%26) + 'A'));
        }
        return cipherText.toString();
    }

    public static String decryptAffine(String cipherText, int a, int b) {
        StringBuilder plaintext = new StringBuilder();
        int invA = inverses.get(a);
        cipherText = cipherText.toUpperCase();
        for (char c : cipherText.toCharArray()){
            int decoded = ((c - 'A' - b)*invA);
            if (decoded < 0) {
                decoded += 26 * (Math.floorDiv(Math.abs(decoded), 26)+1); // negative mod
            }
            plaintext.append((char) (((decoded)%26) + 'a'));
        }
        return plaintext.toString().toLowerCase();
    }
}
