package rsa;
import java.math.BigInteger;
import java.util.ArrayList;

public class RSA {
    public static ArrayList<BigInteger> encryptRSA(String m, BigInteger e, BigInteger n) {
        // Pass in the message and the public key
        // Encrypt each 214 byte block using the public key and return the resulting array of ciphertext blocks
        ArrayList<BigInteger> res = new ArrayList<>();
        ArrayList<BigInteger> blocks = getASCIIBlocks(m);
        for (BigInteger block : blocks) {
            res.add(mod_exp(block, e, n));
        }
        return res;
    }

    public static String decrytRSA(ArrayList<BigInteger> c, BigInteger d, BigInteger n) {
        // Pass in the encrypted blocks and the private key
        // Decrypt each block using the private key and concatenate the results
        StringBuilder s = new StringBuilder();
        for (BigInteger i : c) {
            BigInteger decryptedBlock = mod_exp(i, d, n);
            byte[] bytes = decryptedBlock.toByteArray();
            StringBuilder blockString = new StringBuilder();
            for (byte b : bytes) {
                blockString.append((char) b);
            }
            s.append(blockString);
        }
        return s.toString();
     }

    private static ArrayList<BigInteger> getASCIIBlocks(String m) {
        // helper function to convert the message into ASCII BigInt blocks of length 214 bytes, per assignment description
        ArrayList<BigInteger> res = new ArrayList<>();

        for (int i = 0; i < m.length(); i += 214) {
            if ((i + 214) >= m.length()) {
                res.add(getAsciiVal(m.substring(i)));
            } else {
                res.add(getAsciiVal(m.substring(i, i + 214)));
            }
        }
        return res;
    }

    private static BigInteger getAsciiVal(String s) {
        // helper function to convert a string into a BigInt
        byte[] bytes = s.getBytes(); 
        return new BigInteger(1, bytes);
    }
    
    private static BigInteger mod_exp(BigInteger x, BigInteger a, BigInteger m) {
        // my modular exponentiation function from the previous homework
        if (m == BigInteger.ONE) {
            return BigInteger.ZERO;
        }

        BigInteger result = new BigInteger("1");
        while (a != BigInteger.ZERO) {
            if (! a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                result = result.multiply(x).mod(m);
            }
            x = x.multiply(x).mod(m);
            a = a.shiftRight(1);
        }
        return result;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static String decryptFromFile(String encryptedTextFile, String publicKeyFile, String privateKeyFile) {
        // Given a file with the encrypted text, a public key file, and a private key file, read the keys and the encrypted text in blocks
        // and decrypt the text using the private key, returning the decrypted text
        // The public key file should contain the modulus n and the exponent e on separate lines, n first
        // The private key file should contain the exponent d on a single line
        // The encrypted text file should contain the ciphertext blocks, one per line

        ArrayList<BigInteger> c = new ArrayList<>();
        BigInteger d = BigInteger.ZERO;
        BigInteger n = BigInteger.ZERO;

        // Read public key values from file (each line is a different key)
        try (java.io.BufferedReader keyReader = new java.io.BufferedReader(new java.io.FileReader(publicKeyFile))) {
            String nLine = keyReader.readLine(); // first line has n
            if (nLine != null) {
                n = new BigInteger(nLine.trim());
            } else {
                throw new IllegalArgumentException("Public key file must contain n and e on separate lines.");
            }
        } catch (java.io.IOException keyErr) {
            keyErr.printStackTrace();
            return "";
        }

        // Read private key value from file
        try (java.io.BufferedReader keyReader = new java.io.BufferedReader(new java.io.FileReader(privateKeyFile))) {
            String dLine = keyReader.readLine();
            if (dLine != null) {
                d = new BigInteger(dLine.trim()); // d is the only line in the file
            } else {
                throw new IllegalArgumentException("Private key file must contain a line with d.");
            }
        } catch (java.io.IOException keyErr) {
            keyErr.printStackTrace();
            return "";
        }

        // Read the encrypted text from the file, each line is a different block to be decrypted and concatenated
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(encryptedTextFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                c.add(new BigInteger(line));
            }
        } catch (java.io.IOException err) {
            err.printStackTrace();
        }

        return decrytRSA(c, d, n);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static ArrayList<BigInteger> encryptFromFile(String plainTextFile, String publicKeyFile) {
        // Given a file with the plaintext message and a public key file, read the keys and the plaintext message
        // Then encrypt the message using the public keys, returning the encrypted text as an array of BigIntegers for each encrypted block

        // read the plaintext message 
        StringBuilder message = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(plainTextFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line);
            }
        } catch (java.io.IOException err) {
            err.printStackTrace();
            return null;
        }
        
        // get the public key values from the file
        BigInteger e = BigInteger.ZERO;
        BigInteger n = BigInteger.ZERO;
        try (java.io.BufferedReader keyReader = new java.io.BufferedReader(new java.io.FileReader(publicKeyFile))) {
            String nLine = keyReader.readLine(); // first line has n
            String eLine = keyReader.readLine(); // second line has e
            if (nLine != null && eLine != null) {
                e = new BigInteger(eLine.trim());
                n = new BigInteger(nLine.trim());
            } else {
                throw new IllegalArgumentException("Public key file must contain n and e on separate lines.");
            }
        } catch (java.io.IOException keyErr) {
            keyErr.printStackTrace();
            return null;
        }
        return encryptRSA(message.toString(), e, n);
    }
    
    public static void main(String[] args) {
        // this main method uses the given public and private keys put into files in the inputFiles directory
        // it uses the given plaintext and encrypted text files to test the encrypt and decrypt functions

        // changing the plaintext file will give different encrypted text, but will not change the encrypted text file
        // changing the encrypted text file will give different decrypted text, but will not change the plaintext file

        // regardless of current working directory, as long as the files are in the project root it works
        String projectRoot = System.getProperty("user.dir");
        String plainTextFile = projectRoot + "/inputFiles/plainText.txt";
        String publicKeyFile = projectRoot + "/inputFiles/publicKeys.txt";
        String privateKeyFile = projectRoot + "/inputFiles/privateKeys.txt";
        String encryptedTextFile = projectRoot + "/inputFiles/encryptedText.txt";

        // Encrypt the message from the plaintext file using the public key
        ArrayList<BigInteger> encryptedBlocks = encryptFromFile(plainTextFile, publicKeyFile);
        System.out.println("Encrypted blocks from plaintext file:");
        for (BigInteger block : encryptedBlocks) {
            System.out.println(block);
        }

        // Decrypt the message from the encrypted text file using the private key and public key
        String decryptedMessage = decryptFromFile(encryptedTextFile, publicKeyFile, privateKeyFile);
        System.out.println("Decrypted message from encrypted text file:");
        System.out.println(decryptedMessage);
    }
}
