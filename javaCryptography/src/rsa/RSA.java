package rsa;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

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
        while (a.compareTo(BigInteger.ZERO) != 0) {
            if (!a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                result = result.multiply(x).mod(m);
            }
            x = x.multiply(x).mod(m);
            a = a.shiftRight(1);
        }
        return result;
    }

    private static BigInteger jacobi(BigInteger a, BigInteger n) {
        // find the Jacobi symbol (a/n) using the rules of the Jacobi symbol described in class
        // return 1, 0, or -1

        if (n.compareTo(BigInteger.ZERO) <= 0 || n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("n must be a positive and odd");
        }

        a = a.mod(n); 

        if (a.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }
        if (a.equals(BigInteger.ONE) || n.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        }
        if (!a.gcd(n).equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }

        int result = 1;
        while (a.compareTo(BigInteger.ZERO) > 0) {
            while (a.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                a = a.divide(BigInteger.TWO);
                BigInteger nMod8 = n.mod(BigInteger.valueOf(8));
                if (nMod8.equals(BigInteger.valueOf(3)) || nMod8.equals(BigInteger.valueOf(5))) {
                    result = -result;
                }
            }
            // Swap a and n
            BigInteger temp = a;
            a = n;
            n = temp;
            if (a.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) &&
                n.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                result = -result;
            }
            a = a.mod(n);
        }
        return n.equals(BigInteger.ONE) ? BigInteger.valueOf(result) : BigInteger.ZERO;
    }

    private static BigInteger get1024Num() {
        // This function will create and return a pseudo-random 1024 bit large odd number
        // we shift a BigInteger left 1022 times, generating a random bit, 0 or 1, to set in the BigInteger in each iteration
        BigInteger primeCandidate = new BigInteger("1");
        for (int i = 0; i < 1022; i++) {
            // Generate a random bit and set it in the prime candidate
            int bit = (int) (Math.random() * 2);
            primeCandidate = primeCandidate.shiftLeft(1);
            if (bit == 1) {
                // set the first bit to 1 if we generated a 1, 50% of the time probably
                primeCandidate = primeCandidate.setBit(0);
            }
        }
        primeCandidate = primeCandidate.setBit(1023); // set the last bit to 1 to make it 2048 bits long, should already be a 1 there, but just in case
        primeCandidate = primeCandidate.setBit(0); // set the first bit to 1 to make it odd

        return primeCandidate;
    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        return a.abs(); 
    }

    private static BigInteger modInverse(BigInteger A, BigInteger M)
    {
        BigInteger m0 = M;
        BigInteger y = BigInteger.ZERO, x = BigInteger.ONE;

        if (M.equals(BigInteger.ONE))
            return BigInteger.ZERO;

        BigInteger a = A;
        BigInteger m = M;

        while (a.compareTo(BigInteger.ONE) > 0) {
            // q is quotient
            BigInteger q = a.divide(m);

            BigInteger t = m;

            // m is remainder now, process same as Euclid's algo
            m = a.mod(m);
            a = t;
            t = y;

            // Update x and y
            y = x.subtract(q.multiply(y));
            x = t;
        }

        // Make x positive
        if (x.compareTo(BigInteger.ZERO) < 0)
            x = x.add(m0);

        return x;
    }

    public static boolean SolvayStrassen(BigInteger p) {
        // This function will check if a number is prime using the Solovay-Strassen primality test
        // It returns true if the number is prime, false otherwise
        if (p.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false; // ensure not even, even though we generate odd numbers
        }

        BigInteger a;
        for (int i = 0; i < 55; i++) { // use 55 different a's for 1 in 1000000000000 of false positives
            // generate a random a in the range [1, p-1]
            a = new BigInteger(p.bitLength(), new Random());

            while (a.compareTo(p) >= 0 || a.compareTo(BigInteger.ONE) <= 0) {
                a = new BigInteger(p.bitLength(), new java.util.Random());
            }
            if (gcd(a, p).compareTo(BigInteger.ONE) != 0) {
                return false; // a and p are not coprime, p is composite
            }
            BigInteger eulerCritereon = mod_exp(a, p.subtract(BigInteger.ONE).divide(BigInteger.TWO), p);
            if (eulerCritereon.compareTo(BigInteger.ONE) != 0 && eulerCritereon.compareTo((p.subtract(BigInteger.ONE))) != 0) {
                return false; // not prime, fails Eulers criterion
            }

            BigInteger jacobi = jacobi(a, p);
            if (!jacobi.equals(eulerCritereon)) {
                return false; // not prime
            }
        }
        return true; // probably prime, 999999999999/1000000000000 likelihood
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

    public static void generateKeys(boolean verbose, boolean writeToFiles) {
        BigInteger p = RSA.get1024Num();
        if (verbose){
            System.out.println("Generating 1024 bit primes. 1 in 1000000000000 chance of composite.");
        }

        int count = 1;
        while (!RSA.SolvayStrassen(p)) {
            p = RSA.get1024Num(); // generate a new p until we find a prime
            count++;

        }
        if (verbose){
            System.out.println("Found probable prime p after " + count + " iterations");
        }
        count = 1;
        BigInteger q = RSA.get1024Num();

        while (!RSA.SolvayStrassen(q)) {
            q = RSA.get1024Num(); // generate a new q until we find a prime
            count++;
        }
        if (verbose){
            System.out.println("Found probable prime q after " + count + " iterations");
            System.out.println("Finding n and phiN from p and q");
        }
        BigInteger n = p.multiply(q);

        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); 
        if (verbose){
            System.out.println("Finding an e such that gcd(e, phiN) = 1 and 1 < e < phiN");
        }
        BigInteger e = BigInteger.ZERO;
        while (gcd(phiN, e).compareTo(BigInteger.ONE) != 0 || e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phiN) >= 0) {
            // generate a new e until we find one that is coprime to phiN and in the range (1, phiN)
            e = new BigInteger(phiN.bitLength(), new Random());
        }
        if (verbose){
            System.out.println("Finding a d such that (e * d) mod phiN = 1");
        }
        BigInteger d = modInverse(e, phiN); // ensure e is coprime to phiN

        if (verbose) {
            System.out.println("e = " + e);
            System.out.println("d = " + d);
            System.out.println("phiN = " + phiN);
            if (e.multiply(d).mod(phiN).compareTo(BigInteger.ONE) != 0) {
                System.out.println("(e * d) mod phiN != 1, uh oh, keys are not valid :O");
            } else {
                System.out.println("(e * d) mod phiN = 1, keys are valid :)");
            }
        }
        if (writeToFiles){
            System.out.println("Writing keys to files...");
            String projectRoot = System.getProperty("user.dir");
            String publicKeyFile = projectRoot + "/inputFiles/publicKeys.txt";
            String privateKeyFile = projectRoot + "/inputFiles/privateKeys.txt";

            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(publicKeyFile))) {
                writer.write(n.toString());
                writer.newLine();
                writer.write(e.toString());
            } catch (java.io.IOException eErr) {}

            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(privateKeyFile))) {
                writer.write(d.toString());
            } catch (java.io.IOException dErr) {}
        }
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        // this main method uses the given public and private keys put into files in the inputFiles directory
        // it uses the given plaintext and encrypted text files to test the encrypt and decrypt functions
        // changing the plaintext file will give different encrypted text and replace the encrypted text file
        // regardless of current working directory, as long as the files are in the project root it works

        String projectRoot = System.getProperty("user.dir");
        String plainTextFile = projectRoot + "/inputFiles/plainText.txt";
        String publicKeyFile = projectRoot + "/inputFiles/publicKeys.txt";
        String privateKeyFile = projectRoot + "/inputFiles/privateKeys.txt";
        String encryptedTextFile = projectRoot + "/inputFiles/encryptedText.txt";

        // if you have personal keys you want to use, set the supersecretmode to true and put them in a supersecretkeys directory
        String superSecretPublicKeys = projectRoot + "/supersecretkeys/publicKeys.txt";
        String superSecretPrivateKeys = projectRoot + "/supersecretkeys/privateKeys.txt";
        boolean supersecretmode = false;

        boolean generateKeys = true; // set to true to generate new keys, false to use existing keys
        boolean verbose = true; // set to true to print out key generation details, such as amount of primes tested, false to keep it quiet
        boolean writeToFiles = false; // set to true to write the keys to files (in the inputFiles directory), false to just print them out

        boolean encrypt = false; // set to true to encrypt the message
        boolean decrypt = false; // set to true to decrypt the message

        if (supersecretmode) {
            // if supersecretmode is true, use the super secret keys (my personal keys) instead of the input files, which are the keys I generated for tests
            publicKeyFile = superSecretPublicKeys;
            privateKeyFile = superSecretPrivateKeys;
        } 

        if (generateKeys) {
            // generate new keys and write them to files
            generateKeys(verbose, writeToFiles);
            return; // stop after generating keys
        }

        if (encrypt) {
            // Encrypt the message from the plaintext file using the public key
            ArrayList<BigInteger> encryptedBlocks = encryptFromFile(plainTextFile, publicKeyFile);

            // Write each encrypted block to the encryptedTextFile, one per line
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(encryptedTextFile))) {
                for (BigInteger block : encryptedBlocks) {
                    writer.write(block.toString());
                    writer.newLine();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        if (decrypt) {
            // Decrypt the message from the encrypted text file using the private key and public key
            String decryptedMessage = decryptFromFile(encryptedTextFile, publicKeyFile, privateKeyFile);
            System.out.println("Decrypted message from encrypted text file:");
            System.out.println(decryptedMessage);
        }
    }
}
