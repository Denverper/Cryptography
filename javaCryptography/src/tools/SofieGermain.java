package tools;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import rsa.RSA;

public class SofieGermain {
    public static BigInteger getSofieGermain(int num_digs, int reps_a) {
        // Example usage of RSA with Sofie Germain primes
        BigInteger q = getNDigitNumOdd(num_digs);
        BigInteger p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        int count = 1;

        // Ensure both p and q are Sofie Germain primes
        while (!RSA.SolvayStrassen(q, reps_a) || !RSA.SolvayStrassen(p, reps_a)) {
            count += 1;
            q = getNDigitNumOdd(num_digs);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
        }
        // return the Sofie Germain prime q
        System.out.println("Found Sofie Germain prime after " + count + " attempts.");
        return q;
    }

    public static BigInteger getNDigitNumOdd(int num_digs) {
        // Generate a random n-digit number
        Random rand = new SecureRandom();
        BigInteger r = new BigInteger(String.valueOf(rand.nextInt(1, 10)));
        ArrayList <Integer> odds = new ArrayList<>();
        odds.add(1); odds.add(3); odds.add(5); odds.add(7); odds.add(9);
        
        for (int i = 0; i < num_digs-2; i++) {
            r = r.multiply(BigInteger.TEN);
            r = r.add(new BigInteger(String.valueOf(rand.nextInt(0, 10))));
        }

        // Ensure the number is odd
        r = r.multiply(BigInteger.TEN);
        r = r.add(new BigInteger(String.valueOf(odds.get(rand.nextInt(odds.size())))));
        
        return r;
    }

    public static void main(String[] args) {
        // Example usage
        int num_digs = 20; // Number of digits for the prime
        int reps_a = 55; // Number of repetitions for the Solvay-Strassen test
        BigInteger sofieGermainPrime = getSofieGermain(num_digs, reps_a);
        System.out.println("Sofie Germain prime: " + sofieGermainPrime);
        System.out.println("Safe prime: " + sofieGermainPrime.multiply(BigInteger.TWO).add(BigInteger.ONE));
        System.out.println("Is Sofie Germain prime: " + RSA.SolvayStrassen(sofieGermainPrime, 100));
        System.out.println("Is Safe prime: " + RSA.SolvayStrassen(sofieGermainPrime.multiply(BigInteger.TWO).add(BigInteger.ONE), 100));
    }
}
