import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class RSA {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        // legge da tastiera il messaggio da criptare
        System.out.print("Inserire un messaggio da criptare");
        String data = in.nextLine();
        
        if (data.isEmpty()) {
            System.out.println("Inserire posibilmente almeno un carattere");
            return;
        }
        
        rsaEncrypt(data);
    }
    
    private static void rsaEncrypt(String data) {
        Random rng = new Random();
        
          // Genera 2 numeri primi
        BigInteger firstPrime = BigInteger.probablePrime(128, rng);
        BigInteger secondPrime = BigInteger.probablePrime(128, rng);
        
        // Controllo 2 numeri primi
        while (firstPrime.equals(secondPrime)) {
            secondPrime = BigInteger.probablePrime(128, rng);
        }
        
        // n = p * q
        BigInteger n = firstPrime.multiply(secondPrime);
        
        // φ(n) = (p - 1) * (q - 1)
        BigInteger φn = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));
        
        // Choose e such that gcd(e, φ(n)) = 1
        BigInteger e = new BigInteger("65537");
        
        // Controllo e primo con φ(n)
        while (e.gcd(φn).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.TWO);
        }
        
        // d = e^(-1) mod φ(n)
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

    // Metodo per criptare data
    private static String encrypt(String data, BigInteger e, BigInteger n) {
        StringBuilder encrypted = new StringBuilder();
        String token = ";";  // Il token scelto per separare 
        
        // Cripta ogni carattere di data
        for (char character : data.toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) character);  // Converte i caratteri in BigInteger
            BigInteger c = m.modPow(e, n);  // Cripta usando la formula c = m^e mod n
            encrypted.append(c).append(token);  
        }
        
        return encrypted.toString();
    }
    
    // Method to decrypt the data
    private static void decrypt(String encryptedData, BigInteger d, BigInteger n) {
        StringTokenizer tokenizer = new StringTokenizer(encryptedData, ";");  
        StringBuilder decrypted = new StringBuilder();
        
        // Decrypt each token
        while (tokenizer.hasMoreTokens()) {
            BigInteger encryptedValue = new BigInteger(tokenizer.nextToken());
            BigInteger decryptedValue = encryptedValue.modPow(d, n);  // m = c^d mod n
            decrypted.append((char) decryptedValue.intValue()); // Converte i valori criptati in caratteric
        }
        
        System.out.println("Decrypted Data: " + decrypted.toString());
    }
}
