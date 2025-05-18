package tools;

import java.math.BigInteger;

public class ModularExponentiation {
    public static BigInteger mod_exp(BigInteger x, BigInteger a, BigInteger m) {
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

    public static void main(String[] args) {
        System.out.println(mod_exp(BigInteger.TWO, new BigInteger("10203"), new BigInteger("101")));
    }
}
