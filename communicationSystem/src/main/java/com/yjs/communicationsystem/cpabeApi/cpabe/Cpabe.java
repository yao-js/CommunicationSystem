package com.yjs.communicationsystem.cpabeApi.cpabe;

import com.yjs.communicationsystem.cpabeApi.bswabe.*;
import it.unisa.dia.gas.jpbc.Element;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Cpabe {

	/**
	 * @param
	 * @author Junwei Wang(wakemecn@gmail.com)
	 */

	public static void setup(String pubfile, String mskfile) throws IOException,
			ClassNotFoundException {
		byte[] pub_byte, msk_byte;
		BswabePub pub = new BswabePub();
		BswabeMsk msk = new BswabeMsk();
		Bswabe.setup(pub, msk);

		/* store BswabePub into mskfile */
		pub_byte = SerializeUtils.serializeBswabePub(pub);
		Common.spitFile(pubfile, pub_byte);//把这个public key字节输入到pubfile中。

		/* store BswabeMsk into mskfile */
		msk_byte = SerializeUtils.serializeBswabeMsk(msk);
		Common.spitFile(mskfile, msk_byte);	//把这个master key字节输入到mskfile中。

	}

	public static byte[] keygen(String pubfile, String mskfile,
								String[] attr_str) throws NoSuchAlgorithmException, IOException {
		BswabePub pub;
		BswabeMsk msk;
		byte[] pub_byte, msk_byte, prv_byte;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);//去序列化成public key对象

		/* get BswabeMsk from mskfile */
		msk_byte = Common.suckFile(mskfile);
		msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);//去序列化成master key对象

//		String[] attr_arr = LangPolicy.parseAttribute(attr_str);
		BswabePrv prv = Bswabe.keygen(pub, msk, attr_str);

		/* store BswabePrv into prvfile */
		prv_byte = SerializeUtils.serializeBswabePrv(prv);
		return prv_byte;
	}

	public static byte[][] enc(String pubfile, String policy, String inputfile) throws Exception {
		BswabePub pub;
		BswabeCph cph;
		BswabeCphKey keyCph;
		byte[] plt;
		byte[] cphBuf;
		byte[] aesBuf;
		byte[] pub_byte;
		Element m;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);//利用group owner的public key

		keyCph = Bswabe.enc(pub, policy);//利用group owner的public key和policy（我认为是属性集合）来生成加密AES密钥。
		cph = keyCph.cph;
		m = keyCph.key;
		System.err.println("m = " + m.toString());

		if (cph == null) {
			System.out.println("Error happed in enc");
			System.exit(0);
		}

		cphBuf = SerializeUtils.bswabeCphSerialize(cph);

		/* read file to encrypted */
		plt = Common.suckFile(inputfile);
		aesBuf = AESCoder.encrypt(m.toBytes(), plt);
		// PrintArr("element: ", m.toBytes());
		byte[][] tmp = {aesBuf, cphBuf}; // aesBuf是Encrypted Message，cphBuf是Encrypted AES Key
		return tmp;
	}

	public static String dec(String pubfile, String prvfile, String encfile) throws Exception {
		byte[] aesBuf, cphBuf;
		byte[] plt;
		byte[] prv_byte;
		byte[] pub_byte;
		byte[][] tmp;
		BswabeCph cph;
		BswabePrv prv;
		BswabePub pub;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* read ciphertext */
		tmp = Common.readCpabeFile(encfile);
		aesBuf = tmp[0];
		cphBuf = tmp[1];
		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get BswabePrv form prvfile */
		prv_byte = Common.suckFile(prvfile);
		prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

		BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);//cph refers to cypher-text
		System.err.println("e = " + beb.e.toString());
		if (beb.b) {
			plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
			return new String(plt);
		} else {
			System.exit(0);
		}
		return null;
	}

	public static String dec(String pubfile, String prvfile, byte[] aesBuf, byte[] cphBuf,
							 String decfile) throws Exception {
		byte[] plt;
		byte[] prv_byte;
		byte[] pub_byte;
		byte[][] tmp;
		BswabeCph cph;
		BswabePrv prv;
		BswabePub pub;

		/* get BswabePub from pubfile */
		pub_byte = Common.suckFile(pubfile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		/* read ciphertext */

		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get BswabePrv form prvfile */
		prv_byte = Common.suckFile(prvfile);
		prv = SerializeUtils.unserializeBswabePrv(pub, prv_byte);

		BswabeElementBoolean beb = Bswabe.dec(pub, prv, cph);//cph refers to cypher-text
		System.err.println("e = " + beb.e.toString());
		if (beb.b) {
			plt = AESCoder.decrypt(beb.e.toBytes(), aesBuf);
			return new String(plt);
//			Common.spitFile(decfile, plt);
		} else {
			System.exit(0);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String pubParameterFile = "./cpabeKey/cpabePublicParameter/publicParameter";
		String masterKeyFile = "./cpabeKey/cpabePrivateKey/masterKey";
		String policy = "sn:student2 cn:student2 uid:student2 3of3";
		byte[][] abc = enc(pubParameterFile, policy, "./mess/test");
		byte[] cphBuf = abc[1];
		byte[] pub_byte = Common.suckFile(pubParameterFile);
		BswabePub pub = SerializeUtils.unserializeBswabePub(pub_byte);
		BswabeCph cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

	}

}