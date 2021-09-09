package com.yjs.communicationsystem.IPFS;


import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.IOException;

public class util {
    public static String IpfsAdd(IPFS ipfs, byte[] content) throws IOException {
        ipfs.refs.local();
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(content);
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toBase58();
    }

    public static byte[] IpfsGet(IPFS ipfs, String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] fileContents = ipfs.cat(filePointer);
        return fileContents;
    }

    public static void main(String[] args) throws IOException {
        long startTime=System.currentTimeMillis(); //获取开始时间
        IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
//        IPFS ipfs = new IPFS("/ip4/8.134.63.102/tcp/5001");
//        String mess = "dsdsdasdsada";
//        byte[] content =mess.getBytes();
//        String hash = util.IpfsAdd(ipfs,content);
        String cphBufIpfsHash = "QmZkPK9Uba1KkJnopnKiYNWafSxHjFVH3BLANCUnSjJ8Tr";
//        String cphBufIpfsHash = "QmZkPK9Uba1KkJnopnKiYNWafSxHjFVH3BLANCUnSjJ8Tr";
        byte[] cphBuf = util.IpfsGet(ipfs, cphBufIpfsHash);
        String aa = new String(cphBuf);
        System.out.println(new String(cphBuf));
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//        ipfs.refs.local();
//        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("hello.txt", "G'day world! IPFS rocks!".getBytes());
//        MerkleNode addResult = ipfs.add(file).get(0);
//        Multihash filePointer = Multihash.fromBase58(addResult.hash.toBase58());
//        byte[] fileContents = ipfs.cat(filePointer);
    }
}

