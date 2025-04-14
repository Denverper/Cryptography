package affine;
import caesar.*;
import tools.Tuple;

public class AffineCryptanalysis {

    public static Tuple<Integer, Integer> getKey(String ciphertext) {
        Tuple<Double, Tuple<Integer, Integer>> best = new Tuple<>((Double) 1000.0, new Tuple<>((Integer) 0, (Integer) 0));
        for (int i : Affine.inverses.keySet()) {
            for (int j = 0; j < 26; j++) {
                String curr = Affine.decryptAffine(ciphertext, i, j);
                double mic = CaesarCryptanalysis.getMIC(curr, true);
                double dist = Math.abs(0.065 - mic);
                if (dist < best.getFirst()) {
                    best.setFirst(dist);
                    best.setSecond(new Tuple<>(i, j));
                }
            }
        }
        return best.getSecond();
    }
    

    public static String decryptAffineNoKey(String ciphertext) {
    	Tuple<Integer, Integer> key = getKey(ciphertext);
    	return Affine.decryptAffine(ciphertext, key.getFirst(), key.getSecond());
    }
    
    public static void main(String[] args) {
    	String cipher = "PFIMKZZIXWQIDFWJIMDJWPPEQAARWVWXIZITAXAPSXTWJMPKXTPFWUKPFNWFIXTIPKPPIUWMNSPIZWWRNKTNWQKSMWRKMPPIUWKRAPAZDWADRWTITKRAPAZPWBPKXTIZWWRRIOWITITXPTAKRAPAZPWBPMAXAYIUTAIXCKRAPPAUKOWMSJWIPMWXASCFNWQKSMWEASMKITIPMMSDDAMWTPANWKQASDRWFSXTJWTQFKJKQPWMYFIQFUKOWMMWXMWNWQKSMWEASDJANKNREXWWTKRAPAZQFKJKQPWJMIXAJTWJPANWKNRWPATWQJEDPIPWMDWIQKRREYIPFKXWYQIDFWJIPMDJANKNRENWPPWJPAFKVWYKEUAJWQFKJKQPWJMKXTUKOWIPWKMIWJAXYFAWVWJIMKPPWUDPIXCPATWQJEDPIPNWQKSMWIPMKYFARWXWYQIDFWJKXTIPQKXNWJWKRREQAXZSMIXCKPPFWMPKJPPAKPPWUDPPAZICSJWASPYFKPPAWVWXTAKXTYFWJWPAWVWXNWCIX";
    	System.out.println(decryptAffineNoKey(cipher));
    }
}
