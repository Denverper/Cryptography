public class CeaserCryptanalysis {

    final double[] ENGLISH_FREQ = {
        0.082, /* a */
        0.015, /* b */
        0.028, /* c */
        0.043, /* d */ 
        0.127, /* e */
        0.022, /* f */
        0.020, /* g */
        0.061, /* h */
        0.070, /* i */
        0.002, /* j */
        0.008, /* k */
        0.040, /* l */
        0.024, /* m */
        0.067, /* n */
        0.075, /* o */
        0.019, /* p */
        0.001, /* q */
        0.060, /* r */
        0.063, /* s */
        0.091, /* t */
        0.028, /* u */
        0.010, /* v */
        0.023, /* w */
        0.001, /* x */
        0.020, /* y */
        0.001  /* z */
    };

    public double getMIC(String text, boolean english) {
        if (!english) { // default param
            english = false; 
        }

        int[] freq = new int[26]; // initialize frequency array
        for (int i = 0; i < freq.length; i++) {
            freq[i] = 0;
        }
    
        for (char c : text.toCharArray()) { // get the frequency of each letter
            c = Character.toLowerCase(c);
            if (c >= 'a' && c <= 'z') {
                freq[c - 'a']++;
            } 
        }

        double mic = 0;
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                double f = (double) freq[i] / text.length();
                if (english) {
                    mic += f * ENGLISH_FREQ[i];
                } else { // not MIC with english, so find the index of coincidence of the text
                    mic += f * f;
                }
            }
        }
        return mic;
    }

    public String _shiftText(String text, int shift) {
        StringBuilder shiftedText = new StringBuilder(); // hold the shifted text
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toLowerCase(c);
                char shiftedChar = (char) ((c - 'a' + shift) % 26 + 'a');
                shiftedText.append(shiftedChar);
            } 
        }
        return shiftedText.toString();
    }

    public char getKey(String text) {
        String[] shiftedTexts = new String[26];
        for (int i=0; i<26;i++) {
            shiftedTexts[i] = _shiftText(text, i);
        }
        char bestKey = 'a';
        double bestMIC = 100;

        for (int i=0; i<26;i++) {
            double mic = getMIC(shiftedTexts[i], true);
            double dist = Math.abs(mic - 0.065); // distance from the expected english MIC
            if (dist < bestMIC) {
                bestMIC = dist;
                bestKey = (char) ('A' + i);
            }
        }
        return bestKey;
    }

    public String decryptCeaser(String text) {
        char key = getKey(text);
        return _shiftText(text, key-'A');
    }

    public String encryptCeaser(String text, char key) {
        return _shiftText(text, key-'A');
    }

    public static void main(String[] args) {
        CeaserCryptanalysis ca = new CeaserCryptanalysis();
        String text = "Hello, this is a, test to see if the decryption works!";
        System.out.println("Original text: " + text);
        String encryptedText = ca.encryptCeaser(text, 'C');
        System.out.println("Encrypted text: " + encryptedText);
        String decryptedText = ca.decryptCeaser(encryptedText);
        System.out.println("Decrypted text: " + decryptedText);
    }
}
