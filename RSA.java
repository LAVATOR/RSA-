import java.math.BigInteger;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class RSA {

    public static String data = "ciao";
    private static String token = ";";

    public static void main(String[] args) {
        rsaEncrypt();
    }

    private static void rsaEncrypt() {
        Random rng = new Random();

        // Generate two distinct large primes
        BigInteger firstPrime = BigInteger.probablePrime(512, rng);
        BigInteger secondPrime = BigInteger.probablePrime(512, rng);

        // Ensure the primes are different
        while (firstPrime.equals(secondPrime)) {
            secondPrime = BigInteger.probablePrime(512, rng);
        }

        // Calculate n = p * q
        BigInteger n = firstPrime.multiply(secondPrime);

        // Calculate Euler's totient φ(n) = (p - 1) * (q - 1)
        BigInteger φn = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));

        // Choose e such that gcd(e, φ(n)) = 1. Typically, e is chosen as 65537.
        BigInteger e = new BigInteger("65537");

        // Ensure e is coprime with φ(n)
        while (e.gcd(φn).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.TWO);  // Increment to try the next odd number
        }

        // Calculate d such that (d * e) % φ(n) = 1 (modular inverse of e mod φ(n))
        BigInteger d = e.modInverse(φn);

        System.out.println("firstPrime: " + firstPrime);
        System.out.println("secondPrime: " + secondPrime);
        System.out.println("n: " + n);
        System.out.println("φ(n): " + φn);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

        // Encrypt the data
        String encryptedData = encrypt(data, e, n);
        System.out.println("Encrypted Data: " + encryptedData);

        // Decrypt the data
        decrypt(encryptedData, d, n);
    }

    private static String encrypt(String data, BigInteger e, BigInteger n) {
        StringBuilder encrypted = new StringBuilder();

        // Encrypt each character of the data
        for (char character : data.toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) character);  // Convert char to BigInteger
            BigInteger c = m.modPow(e, n);  // Encrypt using the formula c = m^e mod n
            encrypted.append(c).append(token);
        }

        return encrypted.toString();
    }

    private static void decrypt(String encryptedData, BigInteger d, BigInteger n) {
        StringTokenizer tokenizer = new StringTokenizer(encryptedData, token);
        StringBuilder decrypted = new StringBuilder();

        // Decrypt each token
        while (tokenizer.hasMoreTokens()) {
            BigInteger encryptedValue = new BigInteger(tokenizer.nextToken());
            BigInteger decryptedValue = encryptedValue.modPow(d, n);  // m = c^d mod n
            decrypted.append((char) decryptedValue.intValue());  // Convert decrypted value to char
        }

        System.out.println("Decrypted Data: " + decrypted.toString());
    }
}
