package splendor.common.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class AESSecurityCap {

    private PublicKey publicKey;
    private KeyAgreement keyAgreement;
    private byte[] sharedSecret;
    private final int keySize = 1024;
    private String cryptoAlgorithm = "AES";

    AESSecurityCap() {
        makeKeyExchangeParams();
    }

    private void makeKeyExchangeParams() {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(keySize);
            KeyPair kp = kpg.generateKeyPair();
            publicKey = kp.getPublic();
            keyAgreement = KeyAgreement.getInstance("ECDH");
            keyAgreement.init(kp.getPrivate());

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public void setReceiverPublicKey(PublicKey publickey) {
        try {
            keyAgreement.doPhase(publickey, true);
            sharedSecret = keyAgreement.generateSecret();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String msg) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(cryptoAlgorithm);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(msg.getBytes());
            return Base64.getEncoder().encode(encVal);
        } catch (BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] encryptedData) {
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(cryptoAlgorithm);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue);
        } catch (BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private Key generateKey() {
        return new SecretKeySpec(sharedSecret, cryptoAlgorithm);
    }
}
