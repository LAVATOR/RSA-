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

        // Genera 2 numeri primi
        BigInteger firstPrime = BigInteger.probablePrime(128, rng);
        BigInteger secondPrime = BigInteger.probablePrime(128, rng);

        // Controllo 2 numeri primi
        while (firstPrime.equals(secondPrime)) {
            secondPrime = BigInteger.probablePrime(128, rng);
        }

        //  n = p * q
        BigInteger n = firstPrime.multiply(secondPrime);

        //  φ(n) = (p - 1) * (q - 1)
        BigInteger φn = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));

        //  gcd(e, φ(n)) = 1
        BigInteger e = new BigInteger("65537");

        // Controllo e primo con φ(n)
        while (e.gcd(φn).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.TWO);  
        }

        //  d = (d * e) % φ(n) = 1 
        BigInteger d = e.modInverse(φn);

        System.out.println("firstPrime: " + firstPrime);
        System.out.println("secondPrime: " + secondPrime);
        System.out.println("n: " + n);
        System.out.println("φ(n): " + φn);
        System.out.println("e: " + e);
        System.out.println("d: " + d);

        // Cripta data
        String encryptedData = encrypt(data, e, n);
        System.out.println("Encrypted Data: " + encryptedData);

        // Decripta data
        decrypt(encryptedData, d, n);
    }

    private static String encrypt(String data, BigInteger e, BigInteger n) {
        StringBuilder encrypted = new StringBuilder();

        // Cripta ogni carattere di data
        for (char character : data.toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) character);  // Converte i caratteri in BigInteger
            BigInteger c = m.modPow(e, n);  // Cripta usando la formula c = m^e mod n
            encrypted.append(c).append(token);
        }

        return encrypted.toString();
    }

    private static void decrypt(String encryptedData, BigInteger d, BigInteger n) {
        StringTokenizer tokenizer = new StringTokenizer(encryptedData, token);
        StringBuilder decrypted = new StringBuilder();

        // Decripta ogni token
        while (tokenizer.hasMoreTokens()) {
            BigInteger encryptedValue = new BigInteger(tokenizer.nextToken());
            BigInteger decryptedValue = encryptedValue.modPow(d, n);  // m = c^d mod n
            decrypted.append((char) decryptedValue.intValue());  // Converte i valori criptati in caratteri
        }

        System.out.println("Decrypted Data: " + decrypted.toString());
    }
}
