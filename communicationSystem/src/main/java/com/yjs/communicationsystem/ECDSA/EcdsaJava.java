package com.yjs.communicationsystem.ECDSA;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class EcdsaJava {

    /* read byte[] from inputfile */
    public static byte[] suckFile(String inputfile) throws IOException {
        InputStream is = new FileInputStream(inputfile);
        int size = is.available();
        byte[] content = new byte[size];

        is.read(content);

        is.close();
        return content;
    }

    /* write byte[] into outputfile */
    public static void spitFile(String outputfile, byte[] b) throws IOException {
        OutputStream os = new FileOutputStream(outputfile);
        os.write(b);

        os.close();
    }

    public static void generateKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        keyGen.initialize(256, random); //256 bit key size

        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey priv = pair.getPrivate();
        ECPrivateKey ePriv = (ECPrivateKey) priv;
        PublicKey pub = pair.getPublic();

        spitFile("deviceOwnerEcdsaKey/ecdsa_priv", priv.getEncoded());
        spitFile("deviceOwnerEcdsaKey/ecdsa_pub", pub.getEncoded());


//        //https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
//        String encodedPrivateKey = Base64.getEncoder().encodeToString(priv.getEncoded());
//        byte[] pubEncoded = pub.getEncoded();
//        String encodedPublicKey = Base64.getEncoder().encodeToString(pubEncoded);
//        System.out.println(encodedPrivateKey);
//        System.out.println(encodedPublicKey);
//        return encodedPrivateKey;
    }

    public static BigInteger extractR(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startR + 2, startR + 2 + lengthR));
    }

    public static BigInteger extractS(byte[] signature) throws Exception {
        int startR = (signature[1] & 0x80) != 0 ? 3 : 2;
        int lengthR = signature[startR + 1];
        int startS = startR + 2 + lengthR;
        int lengthS = signature[startS + 1];
        return new BigInteger(Arrays.copyOfRange(signature, startS + 2, startS + 2 + lengthS));
    }

    public static String[] signMsg(String msg, PrivateKey priv) throws Exception {
        Signature ecdsa = Signature.getInstance("SHA1withECDSA");

        ecdsa.initSign(priv);

        byte[] strByte = msg.getBytes("UTF-8");
        ecdsa.update(strByte);

        byte[] realSig = ecdsa.sign();

        //this is the R and S we could also pass as the signature
        System.out.println("R: "+extractR(realSig));
        System.out.println("S: "+extractS(realSig));

        String[] res = new String[]{extractR(realSig).toString(),extractS(realSig).toString()};

        return res;
    }

    public static PrivateKey bytesToPrivateKey(byte[] pkcs8key) throws GeneralSecurityException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8key);
        KeyFactory factory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = factory.generatePrivate(spec);
        return privateKey;
    }

    public static PublicKey bytesToPublicKey(byte[] pkcs8key) throws GeneralSecurityException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pkcs8key);
        KeyFactory factory = KeyFactory.getInstance("EC");
        PublicKey publicKey = factory.generatePublic(spec);
//        PrivateKey privateKey = factory.generatePrivate(spec);
        return publicKey;
    }

    //https://stackoverflow.com/questions/40552688/generating-a-ecdsa-private-key-in-bouncy-castle-returns-a-public-key
    public static String getPrivateKeyAsHex(PrivateKey privateKey) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
        byte[] privateKeyBytes = ecPrivateKey.getS().toByteArray();
        System.out.println("S:"+ecPrivateKey.getS());

        String hex = bytesToHex(privateKeyBytes);

        System.out.println("Private key bytes: " + Arrays.toString(privateKeyBytes));
        System.out.println("Private key hex: " + hex);

        return hex;
    }

    private static String getPublicKeyAsHex(PublicKey publicKey) {
        ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
        String publicKeyString = ecPublicKey.getW().toString();
        String xText = ecPublicKey.getW().getAffineX().toString();
//        BigInteger x = ecPublicKey.getW().getAffineX();
        String yText = ecPublicKey.getW().getAffineY().toString();
//        BigInteger y = ecPublicKey.getW().getAffineY();

//        System.out.println("W:"+ecPublicKey.getW());

        byte[] publicKeyBytes = publicKeyString.getBytes();
        String hex = bytesToHex(publicKeyBytes);
//        String xTextHex = bytesToHex(xText.getBytes());
//        String yTextHex = bytesToHex(yText.getBytes());

        System.out.println("Public key bytes: " + xText + " " + yText);
        System.out.println("Public key bytes: " + Arrays.toString(publicKeyBytes));
        System.out.println("Public key hex: " + hex);

        return hex;
    }

//    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static char[] hexArray = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0 ; j < bytes.length ; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws Exception {
//        generateKey();
        byte[] priv = suckFile("deviceOwnerEcdsaKey/ecdsa_priv");
        PrivateKey pri = bytesToPrivateKey(priv);
        getPrivateKeyAsHex(pri);
//        byte[] pub = suckFile("deviceOwnerEcdsaKey/ecdsa_pub");
//        PublicKey publicKey = bytesToPublicKey(pub);
//        getPublicKeyAsHex(publicKey);
        String[] res = signMsg("hello world", pri);
        System.out.println(res[0] + "\n" + res[1]);
    }
}
