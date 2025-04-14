package viginere;

import caesar.CaesarCryptanalysis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class VigenereCryptanalysis {
    private ArrayList<Integer> getKeyLength(String ciphertext, boolean verbose) {
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
        int window = 20;
        for (int i = 0; i < distances.size(); i++) {
            for (int j = i + 1; j < min(distances.size(), i+window); j++) {
                int gcd = gcd(distances.get(i), distances.get(j));
                GCDMap.put(gcd, GCDMap.getOrDefault(gcd, 0) + 1);
            }
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> GCDMap.get(b) - GCDMap.get(a));
        pq.addAll(GCDMap.keySet());
        int count = 0;
        ArrayList<Integer> keyLengths = new ArrayList<>();
        while (!pq.isEmpty() && count < 3) {
            int topKeyLength = pq.poll();
            keyLengths.add(topKeyLength);
            count++;
        }
        if (verbose) {
            System.out.println("Top 3 Key Lengths Found: " + keyLengths);
        }
        return keyLengths;
    }

    private int min(int i, int j) {
        return i < j ? i : j;
    }

    private ArrayList<String> getCeaserStrings(String ciphertext, int length) {
        ArrayList<StringBuilder> ceaserStringBuilders = new ArrayList<>();
        for (int i = 0; i <= ciphertext.length()-1; i++) {
            if (i < length) {
                ceaserStringBuilders.add(new StringBuilder());
            } 
            ceaserStringBuilders.get((i%length)).append(ciphertext.charAt(i));
        }
        ArrayList<String> ceaserStrings = new ArrayList<>();
        for (StringBuilder sb : ceaserStringBuilders) {
            ceaserStrings.add(sb.toString());
        }
        return ceaserStrings;
    }

    private String getVigKey(String ciphertext, boolean verbose) {
        StringBuilder bestKey = new StringBuilder();
        ArrayList<Integer> bestKeyLengths = getKeyLength(ciphertext, verbose);
        double bestDist = 1000;
        for (Integer key : bestKeyLengths) {
            if (verbose) {
                System.out.println("Trying Key of Length " + key);
            }
            StringBuilder currKey = new StringBuilder();
            ArrayList<String> ceaserStrings = getCeaserStrings(ciphertext, key);
            for (String ceaserString : ceaserStrings) {
                currKey.append(CaesarCryptanalysis.getKey(ceaserString));
                if (verbose) {
                    String shiftedStr = CaesarCryptanalysis._shiftText(ceaserString, (26 - (currKey.charAt(currKey.length() - 1) - 'A')) % 26);
                    System.out.println("Substring: " + ceaserString + " -- " + "found key: " + currKey.charAt(currKey.length() - 1) + " -- " + "shifted substring: " + shiftedStr + " -- " + "MIC for key" + " -- " + CaesarCryptanalysis.getMIC(shiftedStr, true));
                }
            }
            String decrypted = Vigenere.decryptVigenere(ciphertext, currKey.toString());
            double currDist = Math.abs(0.065 - CaesarCryptanalysis.getMIC(decrypted, true));
            if (currDist < bestDist) {
                bestDist = currDist;
                bestKey = currKey;
                if (currDist < 0.005) { // close enough to english already
                    return bestKey.toString();
                }
            }
        }
        return bestKey.toString();
    }

    public String decryptVigenere(String ciphertext, boolean verbose) {
        if (verbose) {
            System.out.println("Ciphertext: " + ciphertext);
        }
        String vigKey = getVigKey(ciphertext, verbose);
        if (verbose) {
            System.out.println("Key: " + vigKey);
        }
        String plainText = Vigenere.decryptVigenere(ciphertext, vigKey);
        if (verbose) {
            System.out.println("Plaintext: " + plainText);
        }
        return plainText;
    } 

    private int gcd(int a, int b) {
        return b==0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        // Test the Vigenere cipher
        // Encrypt and decrypt a message
        VigenereCryptanalysis vig = new VigenereCryptanalysis();
        boolean verbose = true;
        String cipherText = "MLNTJKVPIWCJQYPPPOCYSIDOPTJQYEELOTLUQYEVMALHKVONQYENDDMOYPLJOPZIEWTWENSAZSWQCSIDOBTHTCALSAZQKTVOBSAGDWQOKCCHQGAAEKOPPPPNAFNMHWARKVYWJPNWFCPEDMJJMGAZOELWESPIIXWASKUPIIOALCAADLIAWAMWVVXWZGELOVEXQRAVZQOSWVOEELOAEWVOEVZJBSAATZMZBBSAZZWLCWQYBIWHQYKVXUASKMDDMLZQYCWFPNZNBSAMLOBNKIDPTZNLVJWHOQGAXLELDKUPZCPOOPPBTJBSNWFCPEWVRHMOQXTJJWQMDDMHWAXWZCEMOSPPJEPBQCOBXABDKWYPWMALTRWCYMOEPPHXPZPPNWFPWQWRLIQRQMDOJFPQFOMOWTTPBWABZKUFYPQKZNAEPZZZRMEDIEYICWAQWZLOEPYWFHLLXIYZWYALTPWFPEPOBDLTTPCAKVLZICGALZVTCPEXWEDIRNMPEVREBHWAMAAEOPPPCCJMOWZZQVOPWWKWVWBXAIDEELOELHSTJIHWGTDMLNLSAZDWGZRMCIGDDWFHLPNEPHTXAMEWOLEVDKUPZIJKVEDMLRMYQMEWVRHMOQXTJJWQMTDIOWRZXQYPPPCZPWBYKZEDEZKLDSWCGQYCIDWKZKSQKZLOXPHTMQBTJMGAZOELWESPEBLHTEDIEICNDIYZWYALLUBSAIIFCDPNPHTDKQONQQPMOZWHJBZJMHKZWAIYOESAZPEPLLXPJMOPWMAMXLTZUMOSWCGQYBWCWESETPKVLBQDDQYXWLPZTCPEKCEOQOAWQZMWWKCKQIXCEWTWPPPSPTHMTSIDWTZJMEDMAWAESIDYTZOMMAPTJLTOMPJIWKBZBEZIMYXCEOPPJMGAZPOKLLMOIGXEVOWVOERFOBRNMHPIYCTPZCAEVMHCPOPPSIDSWCGQYEVLPWAHMDOXWWKPWVOEAEKXAALTJNZNIMAMCERFOBVAXEHWZGQYWBEDMDELPKNSAZQWKPEVEDMDLWEHQRDBDKKWAICWVOHIEAZZJIDPPPYZZSLEDQYJMOKCEEAUQAEWJZQBEKLZPPPOIXAASAELOAEWVOEVRPPPNMTJJLYSZBUJYPLEZDWQOPWXALZJBTGVZSGZQZYWUPEUFPBPNMOOWXABSEVFJLPNVPWBSIGMNMLPPDDMDPCOEMOPPPHQYAAZJUJBINAQXQAEWLXEBTBMWPIWEBEHMFJMLOGHDMYOPPXMYPLZSVEKBTABSATLYMDKNXUASKMEWVRHMOQXTJJWQMDDMWEBLXCCJMCKVEDMDPWGAIYZWQBMCALXAIAEXPEBSKCRDBJKCOJMGAZDWGSATWKASAALELJKCWKWVHQVABSAATHMYPBJLMEDMYOPPKXPJMOQXLXWZGWQLWPIALJLSWVOALTPBZIMHNQEPMYXGLJQEWTTWVAKMEBZZIBSABSEZEAMYPPNAVEQZJWVOADPNGZJMZBBSAUHKZOOZLJOENCPWVOCTZSMOHQVAJFNVTJKZWTAKCCEVZBNZBMGAZJLIRATTGMTPELOECEBEAVTJUJOWFHNCKUXABZUWFPIYCTPZCAEVMHCPETTRMOSQEDBSAUZJUZJBLCCPOBCAMEEVLXIDAUPJBOKEYPPPOBLEZDPPPNMHWAXQATYQYPPPYIQQCDWBYEOSPIYZZPRWWQBTKVTJBSAITNBSAVSAAEWZEALTJBZZMLHQYCETPPDHIGAALJLDKUPPPTJOTJATZMZBPTILTALDDMSWLEKAPHTPRMCUBSEVROPPKEYALLJLQNWKACAEVDELPWVOSPPJNTJIWHGEDMMKBEKUQATWKCEEJPYIXAETPPONIHJBSAWYHGEDQYCQVJMHDWHPWOKELOBZGMPLWYGMPLQYKVWESPWJTNLEDIEBTPSBLJOWALFLQYXTFAAZJWHEURKQYXINGIRWQYEOZPBZCMEPWSAZDKUPDWHWTWPPPLMZLTPSMFOMOPWVJWHPPPUZPWVTHTFOQZJBZIMYKEDKUPWZPIIEDMXWBTYQLJADKUPWZPYICLMYPMCOETRMDZWYPSYKESKETPIWHOZPAEWZEALTZWYPSYKEHDIEPPPUZPZWTJETPPEDMTNTTRMDXCEIMTIAEETWKVEDMCKIODMLZQYBWCWVZPPPNRZEVESMLHELUAOELQAMWPPPOIXAEPFCDPALSQEBZZIIOENQAZPJBAKQYPWQRQPSBLJOWALFLQYXTFA";
        vig.decryptVigenere(cipherText, verbose);
    }
}
