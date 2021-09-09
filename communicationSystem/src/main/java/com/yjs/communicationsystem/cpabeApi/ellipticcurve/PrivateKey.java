package com.yjs.communicationsystem.cpabeApi.ellipticcurve;
import com.starkbank.ellipticcurve.utils.ByteString;
import com.starkbank.ellipticcurve.utils.Der;
import com.starkbank.ellipticcurve.utils.BinaryAscii;
import com.starkbank.ellipticcurve.utils.RandomInteger;

import java.lang.Math;
import java.math.BigInteger;
import java.util.Arrays;


public class PrivateKey {

    public Curve curve;
    public BigInteger secret;

    /**
     *
     */
    public PrivateKey() {
        this(Curve.secp256k1, null);
        secret = RandomInteger.between(BigInteger.ONE, curve.N);
    }

    /**
     *
     * @param curve
     * @param secret
     */
    public PrivateKey(Curve curve, BigInteger secret) {
        this.curve = curve;
        this.secret = secret;
    }

    /**
     *
     * @return
     */
    public PublicKey publicKey() {
        Curve curve = this.curve;
        Point publicPoint = Math.multiply(curve.G, this.secret, curve.N, curve.A, curve.P);
        return new PublicKey(publicPoint, curve);
    }

    /**
     *
     * @return
     */
    public ByteString toByteString() {
        return BinaryAscii.stringFromNumber(this.secret, this.curve.length());
    }

    /**
     *
     * @return
     */
    public ByteString toDer() {
        ByteString encodedPublicKey = this.publicKey().toByteString(true);
        return Der.encodeSequence(
                Der.encodeInteger(BigInteger.valueOf(1)),
                Der.encodeOctetString(this.toByteString()),
                Der.encodeConstructed(0, Der.encodeOid(this.curve.oid)),
                Der.encodeConstructed(1, Der.encodeBitString(encodedPublicKey)));
    }

    /**
     *
     * @return
     */
    public String toPem() {
        return Der.toPem(this.toDer(), "EC PRIVATE KEY");
    }


    /**
     *
     * @param string
     * @return
     */
    public static PrivateKey fromPem(String string) {
        String privkeyPem = string.substring(string.indexOf("-----BEGIN EC PRIVATE KEY-----"));
        return fromDer(Der.fromPem(privkeyPem));
    }

    /**
     *
     * @param string
     * @return
     */
    public static PrivateKey fromDer(String string) {
        return fromDer(new ByteString(string.getBytes()));
    }

    /**
     *
     * @param string
     * @return
     */
    public static PrivateKey fromDer(ByteString string) {
        ByteString[] str = Der.removeSequence(string);
        ByteString s = str[0];
        ByteString empty = str[1];
        if (!empty.isEmpty()) {
            throw new RuntimeException(String.format("trailing junk after DER privkey: %s", BinaryAscii.hexFromBinary(empty)));
        }

        Object[] o = Der.removeInteger(s);
        long one = Long.valueOf(o[0].toString());
        s = (ByteString) o[1];
        if (one != 1) {
            throw new RuntimeException(String.format("expected '1' at start of DER privkey, got %d", one));
        }

        str = Der.removeOctetString(s);
        ByteString privkeyStr = str[0];
        s = str[1];
        Object[] t = Der.removeConstructed(s);
        long tag = Long.valueOf(t[0].toString());
        ByteString curveOidStr = (ByteString) t[1];
        s = (ByteString) t[2];
        if (tag != 0) {
            throw new RuntimeException(String.format("expected tag 0 in DER privkey, got %d", tag));
        }

        o = Der.removeObject(curveOidStr);
        long[] oidCurve = (long[]) o[0];
        empty = (ByteString) o[1];
        if (!"".equals(empty.toString())) {
            throw new RuntimeException(String.format("trailing junk after DER privkey curve_oid: %s", BinaryAscii.hexFromBinary(empty)));
        }
        Curve curve = (Curve) Curve.curvesByOid.get(Arrays.hashCode(oidCurve));
        if (curve == null) {
            throw new RuntimeException(String.format("Unknown curve with oid %s. I only know about these: %s", Arrays.toString(oidCurve), Arrays.toString(Curve.supportedCurves.toArray())));
        }

        if (privkeyStr.length() < curve.length()) {
            int l = curve.length() - privkeyStr.length();
            byte[] bytes = new byte[l + privkeyStr.length()];
            for (int i = 0; i < curve.length() - privkeyStr.length(); i++) {
                bytes[i] = 0;
            }
            byte[] privateKey = privkeyStr.getBytes();
            System.arraycopy(privateKey, 0, bytes, l, bytes.length - l);
            privkeyStr = new ByteString(bytes);
        }

        return PrivateKey.fromString(privkeyStr, curve);
    }

    /**
     *
     * @param string
     * @param curve
     * @return
     */
    public static PrivateKey fromString(ByteString string, Curve curve) {
        return new PrivateKey(curve, BinaryAscii.numberFromString(string.getBytes()));
    }

    /**
     *
     * @param string
     * @return
     */
    public static PrivateKey fromString(String string) {
        return fromString(new ByteString(string.getBytes()));
    }

    /**
     *
     * @param string
     * @return
     */
    public static PrivateKey fromString(ByteString string) {
        return PrivateKey.fromString(string, Curve.secp256k1);
    }
}
