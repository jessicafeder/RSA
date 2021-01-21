package Exercise_RSA;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {

    /* Spara nycklar i filer */
    public static void saveKey(String fileName, KeyPair key) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(key);
            out.close();
            System.out.println("Saved key as " + fileName);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /* Läsa in sparade nycklar */
    public static KeyPair readKey(String fileName) {
        KeyPair key = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            key = (KeyPair) in.readObject();
            in.close();
            System.out.println("Read key from " + fileName);
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return key;
    }

    /* Generera nycklar */
    public static void generateKeys(String fileName, int bitLength) {
        SecureRandom rand = new SecureRandom();

        BigInteger p = new BigInteger(bitLength / 2, 100, rand);
        BigInteger q = new BigInteger(bitLength / 2, 100, rand);
        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = new BigInteger("3");
        while (phiN.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }

        BigInteger d = e.modInverse(phiN);
        KeyPair publicKey = new KeyPair(e, n);
        KeyPair privateKey = new KeyPair(d, n);
        saveKey(fileName + "_pub.key", publicKey);
        saveKey(fileName + "_priv.key", privateKey);

    }

    /*  Kunna kryptera och dekryptera strängar */
    public static String encrypt(String message, KeyPair key){
        return (new BigInteger(message.getBytes())).modPow(key.getKey(), key.getN()).toString();
    }

    public static String decrypt(String message, KeyPair key){
        return new String((new BigInteger(message)).modPow(key.getKey(), key.getN()).toByteArray());
    }

    public static void main(String[] args) {
        //int bitLength = 4096;
        KeyPair publicKey = readKey("Jessica_pub.key");
        KeyPair privateKey = readKey("Jessica_priv.key");
        String encrypted = encrypt("Encrypting is fun!", publicKey);
        System.out.println("Encrypted message is: " + encrypted);
        String clear = decrypt(encrypted, privateKey);
        System.out.println("Decrypted message is: " + clear);
    

    }
}