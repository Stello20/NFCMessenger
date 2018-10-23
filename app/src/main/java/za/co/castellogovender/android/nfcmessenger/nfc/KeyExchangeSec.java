package za.co.castellogovender.android.nfcmessenger.nfc;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyExchangeSec {

    private PublicKey publickey;
    private PrivateKey privatekey;
    KeyAgreement keyAgreement;
    private byte[] sharedsecret;
    private boolean isEmpty;

    String ALGO = "AES";

    KeyExchangeSec() {
        publickey = null;
        privatekey = null;
        keyAgreement = null;
        sharedsecret = null;
        isEmpty=true;
    }
    KeyExchangeSec(String alg, int kSize){
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(256);
            KeyPair kp = kpg.generateKeyPair();
            publickey = kp.getPublic();
            keyAgreement = KeyAgreement.getInstance("ECDH");
            privatekey = kp.getPrivate();
            keyAgreement.init(kp.getPrivate());
            isEmpty=false;

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void setReceiverPublicKey(PublicKey publickey) {
        try {
            keyAgreement.doPhase(publickey, true);
            sharedsecret = keyAgreement.generateSecret();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String msg, byte[] ss, String al) {
        try {
            Key key = new SecretKeySpec(ss, al) ;
            Cipher c = Cipher.getInstance(al);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(msg.getBytes());
            return bytesToHex(encVal);
        } catch (BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static String decrypt(String encryptedData, byte[] ss, String al) {
        try {
            Key key = new SecretKeySpec(ss, al);
            Cipher c = Cipher.getInstance(al);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = hexStringToByteArray(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            return new String(decValue);
        } catch (BadPaddingException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedData;
    }

    public static PublicKey toPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey pKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(bytes));
        return pKey;
    }
    public static PrivateKey toPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey pKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(bytes));
        return pKey;
    }

    public PublicKey getPublickey() {
        return publickey;
    }
    public PrivateKey getPrivatekey() {
        return privatekey;
    }
    public Boolean getIsEmpty() {
        return isEmpty;
    }
    public byte[] getSharedSecret() {
        return sharedsecret;
    }

    protected Key generateKey() {
        return new SecretKeySpec(sharedsecret, ALGO);
    }


//example code emulation on key exchange and encryption using ECDH and AES
    /*public static void main(String[] args) throws IOException {
        KeyExchangeSec server = new KeyExchangeSec();
        KeyExchangeSec client = new KeyExchangeSec();

        server.setReceiverPublicKey(client.getPublickey());

        client.setReceiverPublicKey(server.getPublickey());

        String data = "hello";

        String enc = server.encrypt(data);

        System.out.println("hello is coverted to "+enc);

        System.out.println(enc+" is converted to "+client.decrypt(enc));
        System.out.println(server.getPrivatekey().getEncoded());
        System.out.println(client.getPrivatekey().getEncoded());
        System.out.println(server.getPublickey().getEncoded());
        System.out.println(client.getPublickey().getEncoded());

    }*/
}