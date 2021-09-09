package main

import (
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"crypto/x509"
	"encoding/pem"
	"io/ioutil"
	"math/big"
)

//------------ECDSA私匙执行数字签名------------
func EccSignature(plainText, priKey []byte)  (rText, sText []byte){
	//------1.获取私匙------

	//Step2:私匙的反pem编码化
	block,_:=pem.Decode(priKey)
	//Step3:私匙的反x509序列化
	privKey,err:=x509.ParseECPrivateKey(block.Bytes)
	if err!=nil{
		panic(err)
	}
	//------求取明文的散列值------
	//Step1:创建基于sha256哈希函数的hash接口
	myHash:=sha256.New()
	//Step2:写入数据
	myHash.Write(plainText)
	//Step:求出明文的散列值
	hashText:=myHash.Sum(nil)
	//------对明文的散列值进行数字签名
	/*
			Step1:基于ECDSA实现数字签名
			函数：func Sign(rand io.Reader, priv *PrivateKey, hash []byte) (r, s *big.Int, err error)
			作用：使用私钥对任意长度的hash值（必须是较大信息的hash结果）进行签名，返回签名结果（一对大整数），
			私钥的安全性取决于密码读取器的熵度（随机程度）
			返回参数1：一对大整数,表示点的x和y轴坐标值，需要转换为[]byte
				转换函数:func (z *Int) MarshalText() (text []byte, err error)  本方法实现了encoding.TextMarshaler接口
						type TextMarshaler interface {
		  					MarshalText() (text []byte, err error)
								}
						实现了TextMarshaler接口的类型可以将自身序列化为utf-8编码的textual格式
			返回参数2：error
			参数1：rand.Reader
			参数2：私匙
			参数3：明文的散列值
	*/
	r,s,err:=ecdsa.Sign(rand.Reader,privKey,hashText)
	if err!=nil{
		panic(err)
	}
	//Step2:序列化
	rText,err=r.MarshalText()
	if err!=nil{
		panic(err)
	}
	sText,err=s.MarshalText()
	if err!=nil{
		panic(err)
	}
	return
}
//------------ECDSA公匙验证数字签名------------
func EccVerify(plainText, rText, sText , pubKey []byte) bool {

	//------1.获取公钥------
	//Step1:打开文件获取公匙
	//Step2：将公匙反pem码化
	block,_:=pem.Decode(pubKey)
	//Step3:将公匙反x509序列化
	pubInterface,_:=x509.ParsePKIXPublicKey(block.Bytes)
	//Step4:执行公匙的类型断言
	publicKey:=pubInterface.(*ecdsa.PublicKey)
	//------2.获取明文的散列值------
	//Step1:创建hash接口，指定采用的哈希函数
	myHash:=sha256.New()
	//Step2:向myHash中写入内容
	myHash.Write(plainText)
	//Step3:生成明文的散列值
	hashText:=myHash.Sum(nil)
	//------3.对数字签名后的内容进行解密------
	/*
		Step1:对rText和sText解序列化
		函数：func (z *Int) UnmarshalText(text []byte) error
	*/
	var r,s big.Int							//空指针，指向了系统的0x00地址，不能对这个地址进行操作
	r.UnmarshalText(rText)
	s.UnmarshalText(sText)
	/*
		Step2:采用ECDSA的公匙验证数字签名的正确性
		函数：func Verify(pub *PublicKey, hash []abyte, r, s *big.Int) bool
		作用：使用公钥验证hash值和两个大整数r、s构成的签名，并返回签名是否合法
		返回参数：验证数字签名是否正确
		参数1：公匙
		参数2：明文的散列值
		参数3,4:rText、sText的序列化
	*/
	return ecdsa.Verify(publicKey,hashText,&r,&s)
}

func parseCertFile(filename string) []byte{
	result,error := ioutil.ReadFile(filename)
	if error != nil{
		panic(error)
	}
	return result
}
