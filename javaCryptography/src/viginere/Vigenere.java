package viginere;

public class Vigenere {
    public static String encryptVigenere(String plainText, String key) { 
        // Encrypt the Code using a passed key
        // encrypt p_i with k_(i%26) like a standard shift with that key value
        String text = plainText.toUpperCase();
        String keyUpper = key.toUpperCase();
        StringBuilder encryptedText = new StringBuilder();
        for (int i=0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                char k = keyUpper.charAt(i % keyUpper.length());
                int shift = (k - 'A');
                encryptedText.append((char) ((c - 'A' + shift) % 26 + 'A'));
            }
        }
        return encryptedText.toString();
    }

    public static String decryptVigenere(String cipherText, String key) { 
        // Uses the encryption method to decrypt the code
        // The key is reversed to decrypt the code
        StringBuilder newKey = new StringBuilder();
        key = key.toUpperCase();
        for (char c : key.toCharArray()) {
            newKey.append((char) (((26 - (c - 'A')) + 'A')));
        }
        String decryptedString = encryptVigenere(cipherText, newKey.toString()).toLowerCase();
        return decryptedString;
    }
}
