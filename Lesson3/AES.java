package Lesson3;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public IvParameterSpec generateIV() {
        byte[] iv = new byte[128 / 8];
        SecureRandom sRandom = new SecureRandom();
        sRandom.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public void saveIv(String ivFileName, IvParameterSpec iv) throws IOException {
        FileOutputStream out = new FileOutputStream(ivFileName);
        out.write(iv.getIV());
        out.close();
    }

    public IvParameterSpec readIv(String ivFileName) throws IOException {
        byte[] iv = Files.readAllBytes(Paths.get(ivFileName));
        return new IvParameterSpec(iv);
    }

    public SecretKeySpec generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        return new SecretKeySpec(keygen.generateKey().getEncoded(), "AES");
    }

    public SecretKeySpec keyFromPassPhrase(String passPhrase)
            throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        byte[] salt = new String("1234567890ABC").getBytes();
        int iterationCount = 1024;
        int keyStrength = 256;
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount, keyStrength);
        SecretKey key = factory.generateSecret(spec);
        return new SecretKeySpec(key.getEncoded(), "AES");
    }

    public void encrypt(String plainText, String outFile, SecretKeySpec sKey, IvParameterSpec iv)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sKey, iv);

        //Spara till fil
        FileOutputStream out = new FileOutputStream(outFile);
        byte[] input = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] cipherOutput = cipher.doFinal(input);
        out.write(cipherOutput);
        out.close();

        }

    public String decrypt(String inFile, SecretKeySpec sKey, IvParameterSpec iv)
        throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{
        
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sKey, iv);
        byte[] cipherInput = Files.readAllBytes(Paths.get(inFile));
        return new String(cipher.doFinal(cipherInput), StandardCharsets.UTF_8);
        
    }   

}
