package Exercise_RSA.Java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main{

    Main(){
        System.out.println("-------------------Crypto-------------------");
        KeyPair publicKey = readKey("Jessica_pub.key");
        KeyPair privateKey = readKey("Jessica_priv.key");
        Scanner sc = new Scanner(System.in);
        System.out.print("Text to encrypt: ");
        String message = sc.nextLine();
        String encrypted = encrypt(message, publicKey);
        System.out.println("Encrypted message is: " + encrypted);
        String clear = decrypt(encrypted, privateKey);
        System.out.println("Decrypted message is: " + clear);
        sc.close();

        try {
            encryptToFile(message, publicKey, "file.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            decryptFromFile("file.txt", privateKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
       /* int bitLength = 4096;
        generateKeys("Jessica", bitLength); */
    }

    private void generateKeys(String fileName, int bitLength) {
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

    private void generateKeys(String fileName) {
        generateKeys(fileName, 2048);
    }

    private void saveKey(String fileName, KeyPair key) {
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

    private KeyPair readKey(String fileName) {
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

    public String encrypt(String message, KeyPair key){
        return (new BigInteger(message.getBytes())).modPow(key.getKey(), key.getN()).toString();
    }
    
    public void encryptToFile(String message, KeyPair key, String fileName) throws FileNotFoundException, IOException {
        String encrypted = encrypt(message, key);
        FileOutputStream out = new FileOutputStream(fileName);
        byte[] input = encrypted.getBytes(StandardCharsets.UTF_8);
        out.write(input);
        System.out.println("Encrypted message was sent to >> " + fileName);
        out.close();
    }

    public String decrypt(String message, KeyPair key){
        return new String((new BigInteger(message)).modPow(key.getKey(), key.getN()).toByteArray());
    }

    public String decryptFromFile(String fileName, KeyPair key) throws FileNotFoundException, IOException {
        if (!fileName.contains(".txt")) {
            fileName = fileName + ".txt";
        }
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine());
        }
        sc.close();
        System.out.println("Message read from file: " + sb);
        return decrypt(sb.toString(), key);

    }

    public static void main(String[] args) {
        new Main();

    }
}