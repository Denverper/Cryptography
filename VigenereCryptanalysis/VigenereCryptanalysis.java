
import java.util.ArrayList;
import java.util.HashMap;

public class VigenereCryptanalysis {
    public int getKeyLength(String ciphertext) {
        int keyLength = 0;
        ArrayList<Integer> distances = new ArrayList<>();
        HashMap<String, Integer> tripletMap = new HashMap<>();
        int length = ciphertext.length();
        for (int i = 0; i < length - 2; i++) {
            String triplet = ciphertext.substring(i, i + 3);
            if (tripletMap.containsKey(triplet)) {
                int distance = i - tripletMap.get(triplet); // distance from last triplet
                distances.add(distance);
            }
            tripletMap.put(triplet, i);
        }

        HashMap<Integer, Integer> GCDMap = new HashMap<>(); //count the prevalance of GCDs
        for (int i = 0; i < distances.size(); i++) {
            for (int j = i + 1; j < distances.size(); j++) {
                int gcd = gcd(distances.get(i), distances.get(j));
                GCDMap.put(gcd, GCDMap.getOrDefault(gcd, 0) + 1);
            }
        }
        int maxCount = 0;
        for (Integer i : GCDMap.keySet()) {
            if (GCDMap.get(i) > maxCount) {
                maxCount = GCDMap.get(i);
                keyLength = i;
            }
        }

        return keyLength;
    }

    public ArrayList<String> getCeaserStrings(String ciphertext, int length) {
        ArrayList<StringBuilder> ceaserStringBuilders = new ArrayList<>();
        for (int i = 1; i <= ciphertext.length(); i++) {
            if (i < length) {
                ceaserStringBuilders.add(new StringBuilder());
            } else {
                ceaserStringBuilders.get(i%length).append(ciphertext.charAt(i));
            }
        }
        ArrayList<String> ceaserStrings = new ArrayList<>();
        for (StringBuilder sb : ceaserStringBuilders) {
            ceaserStrings.add(sb.toString());
        }
        return ceaserStrings;
    }

    public String getVigKey(String ciphertext) {
        StringBuilder vigKey = new StringBuilder();
        int keyLength = getKeyLength(ciphertext);
        ArrayList<String> ceaserStrings = getCeaserStrings(ciphertext, keyLength);
        for (String ceaserString : ceaserStrings) {
            vigKey.append((ceaserString));
        }
        return vigKey.toString();
    }

    public int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b,a%b);
    }
}
