import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RSA {

    // Configura il logger
    private static final Logger logger = Logger.getLogger(RSA.class.getName());

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        // legge da tastiera il messaggio da criptare
        System.out.print("Inserire un messaggio da criptare:");
        String data = in.nextLine();
        
        while (data.isEmpty()) {
            System.out.println("Inserire posibilmente almeno un carattere");
            data = in.nextLine();
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
        
        // Scegli e tale che mcd(e, φ(n)) = 1
        BigInteger e = new BigInteger("65537");
        
        // Controllo e primo con φ(n)
        while (e.gcd(φn).compareTo(BigInteger.ONE) != 0) {
            e = e.add(BigInteger.TWO);
        }
        
        // d = e^(-1) mod φ(n)
        BigInteger d = e.modInverse(φn);
        
        
        logger.log(Level.INFO, "firstPrime: " + firstPrime);
        logger.log(Level.INFO, "secondPrime: " + secondPrime);
        logger.log(Level.INFO, "n: " + n);
        logger.log(Level.INFO, "φ(n): " + φn);
        logger.log(Level.INFO, "e: " + e);
        logger.log(Level.INFO, "d: " + d);
        
        // Cripta data
        String encryptedData = encrypt(data, e, n);
        logger.log(Level.INFO, "Encrypted Data: " + encryptedData);
        
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
    
    // Metodo per decriptare data
    private static void decrypt(String encryptedData, BigInteger d, BigInteger n) {
        StringTokenizer tokenizer = new StringTokenizer(encryptedData, ";");  // Utilizza il token per dividere i valori criptati
        StringBuilder decrypted = new StringBuilder();
        
        // decripta ogni token
        while (tokenizer.hasMoreTokens()) {
            BigInteger encryptedValue = new BigInteger(tokenizer.nextToken());
            BigInteger decryptedValue = encryptedValue.modPow(d, n);  // m = c^d mod n
            decrypted.append((char) decryptedValue.intValue());  // // Converte i valori criptati in caratteri
        }
        
        logger.log(Level.INFO, "Decrypted Data: " + decrypted.toString());
    }
}
