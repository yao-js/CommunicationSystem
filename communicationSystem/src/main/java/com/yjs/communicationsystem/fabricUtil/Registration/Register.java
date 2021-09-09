package com.yjs.communicationsystem.fabricUtil.Registration;

public class Register {

    // Device Unique ID (adopt hash function to hash device's certificate)
    public String Uid;
    // Device owner certificate which contains device owner certificate and other needed information
    public String DeviceOwnerPub;
    // The signature of device certificate which is signed by device owner ECDSA public key and using ECDSA algorithm
    public String rPubSign;
    // The signature of device certificate which is signed by device owner ECDSA public key and using ECDSA algorithm
    public String sPubSign;
    // The certificate of device generated by device owner using the csr file which generated by device.
    public String DevicePub;
    // The ECDH public key of device which is used to create share secret key adopted by ECDH encryption
    public String DeviceEcdhPub;
    // The valid time of device's cpabe key
    public String KeyValidTimeStamp;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getDeviceOwnerPub() {
        return DeviceOwnerPub;
    }

    public void setDeviceOwnerPub(String deviceOwnerPub) {
        DeviceOwnerPub = deviceOwnerPub;
    }

    public String getsPubSign() {
        return sPubSign;
    }

    public void setsPubSign(String sPubSign) {
        this.sPubSign = sPubSign;
    }

    public String getrPubSign() {
        return rPubSign;
    }

    public void setrPubSign(String rPubSign) {
        this.rPubSign = rPubSign;
    }

    public String getDevicePub() {
        return DevicePub;
    }

    public void setDevicePub(String devicePub) {
        DevicePub = devicePub;
    }

    public String getDeviceEcdhPub() {
        return DeviceEcdhPub;
    }

    public void setDeviceEcdhPub(String deviceEcdhPub) {
        DeviceEcdhPub = deviceEcdhPub;
    }

    public String getKeyValidTimeStamp() {
        return KeyValidTimeStamp;
    }

    public void setKeyValidTimeStamp(String keyValidTimeStamp) {
        this.KeyValidTimeStamp = keyValidTimeStamp;
    }
}
