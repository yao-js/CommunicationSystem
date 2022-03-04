package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/big"
	"reflect"
	"strconv"

	paillier "github.com/hiddenlayer-ai/go-paillier"
	shell "github.com/ipfs/go-ipfs-api"
)

type SimpleChaincode struct {
}

//交易结构体(未来的通道)
type User struct {
	W2 [][]float64 `json:"W2"`
}
type Original_gradient struct { //梯度数据结构体
	W2     [][]float64 `json:"W2"`
	W2cons []float64   `json:"b2"`
	W1     [][]float64 `json:"W1"`
	W1cons []float64   `json:"b1"`
	// W1cons []float64   `json:"b1"`
}
type Integer_gradient struct { //整型部分结构体
	W2     [][]int64 `json:"W2"`
	W2cons []int64   `json:"b2"`
	W1     [][]int64 `json:"W1"`
	W1cons []int64   `json:"b1"`
	// W1cons []float64   `json:"b1"`
}
type Pailli_gradient struct { //加密数据结构体
	W2      [][]string `json:"W2"`
	W2_cons []string   `json:"b2"`
	W1      [][]string `json:"W1"`
	W1_cons []string   `json:"b1"`
}

var sh *shell.Shell

// 数据上传到ipfs
func UploadIPFS(str string) string {
	sh = shell.NewShell("172.25.0.3:5001")

	hash, err := sh.Add(bytes.NewBufferString(str))
	if err != nil {
		fmt.Println("上传ipfs时错误：", err)
	}
	return hash
}

//从ipfs下载数据
func CatIPFS(hash string) string {
	sh = shell.NewShell("172.25.0.3:5001")

	read, err := sh.Cat(hash)
	if err != nil {
		fmt.Println(err)
	}
	body, err := ioutil.ReadAll(read)

	return string(body)
}

// //通道序列化
// func marshalStruct(transaction User) []byte {

// 	data, err := json.Marshal(&transaction)
// 	if err != nil {
// 		fmt.Println("序列化err=", err)
// 	}
// 	return data
// }

// //通道序列化处理梯度数据
//数据反序列化为通道
func unmarshalStruct(str []byte) User {
	var transaction User
	err := json.Unmarshal(str, &transaction)
	if err != nil {
		fmt.Println("unmarshal err=%v", err)
	}
	return transaction
}

//通道序列化处理梯度数据
func marshalStruct_gradient(transaction Pailli_gradient) []byte {

	data, err := json.Marshal(&transaction)
	if err != nil {
		fmt.Println("序列化err=", err)
	}
	return data
}

//数据反序列化为通道处理梯度数据
func unmarshalStruct_gradient(str []byte) Pailli_gradient {
	var transaction Pailli_gradient
	err := json.Unmarshal(str, &transaction)
	if err != nil {
		fmt.Println("unmarshal err=%v", err)
	}
	return transaction
}

//数据反序列化为通道处理梯度数据
func unmarshalStruct_gradient_float(str []byte) Original_gradient {
	var transaction Original_gradient
	err := json.Unmarshal(str, &transaction)
	if err != nil {
		fmt.Println("unmarshal err=%v", err)
	}
	return transaction
}

/*###############################################*/
/* 定义sign函数 */
func sign(x [][]float64) [][]float64 {
	var a float64 = 1.
	var b float64 = -1.
	var c float64 = 0.
	for i := 0; i < len(x); i++ {
		for j := 0; j < len(x[0]); j++ {
			if x[i][j] < 0 {
				x[i][j] = b
			} else if x[i][j] > 0 {
				x[i][j] = a
			} else {
				x[i][j] = c
			}
		}
	}
	return x
}
func sign_1D(x []float64) []float64 {
	var a float64 = 1.
	var b float64 = -1.
	var c float64 = 0.
	for i := 0; i < len(x); i++ {
		if x[i] < 0 {
			x[i] = b
		} else if x[i] > 0 {
			x[i] = a
		} else {
			x[i] = c
		}

	}
	return x
}

/* 定义矩阵对应位置相乘的函数 */
func Corresponding(x, y [][]float64) [][]float64 {
	var outcome [][]float64
	var row = len(x)           // 行数
	var col = len(x[0])        // 列数
	for i := 0; i < row; i++ { // 循环为一维长度
		arr := make([]float64, col)    // 创建一个一维切片
		outcome = append(outcome, arr) // 把一维切片,当作一个整体传入二维切片中
	}
	for i := 0; i < row; i++ {
		for j := 0; j < col; j++ {
			// fmt.Println("i = " + strconv.Itoa(i) + ", j = " + strconv.Itoa(j))
			outcome[i][j] = x[i][j] * y[i][j]
		}
	}
	return outcome
}
func Corresponding_1D(x, y []float64) []float64 {
	var outcome []float64
	var row = len(x) // 行数
	outcome = make([]float64, row)
	for i := 0; i < row; i++ {
		outcome[i] = x[i] * y[i]
	}

	return outcome
}

/*相同符号数量之和*/
func samesign(x [][]float64) int {
	var sum int = 0
	for i := 0; i < len(x); i++ {
		for j := 0; j < len(x[0]); j++ {
			if x[i][j] >= 0 {
				sum++
			}
		}
	}
	return sum
}
func samesign_1D(x []float64) int {
	var sum int = 0
	for i := 0; i < len(x); i++ {
		if x[i] >= 0 {
			sum++
		}
	}
	return sum
}

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {

	return shim.Success(nil)
}
func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("ex2 Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function == "invoke" {
		return t.invoke(stub, args)
	} else if function == "query" {
		return t.query(stub, args)
	} else if function == "Creat" {
		return t.Creat(stub, args)
	} else if function == "aggregation" {
		return t.aggregation(stub, args)
	} else if function == "query_data" {
		return t.query_data(stub, args)
	}
	return shim.Error("Invalid invoke function name. Expecting \"invoke\" \"initLedger\" \"query\"")
}

// type Registration struct {
// 	W2 string `json:"W2"` //权重W2
// }

func (t *SimpleChaincode) Creat(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// 数据上链(这里需要做的有客户端的奖励系数上链，梯度数据ipfs的哈希上链)
	stub.PutState(args[0], []byte(args[1]))
	return shim.Success(nil)
}
func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// 用于查询存放在链上的奖励积累值
	var Client_float string
	Client, _ := stub.GetState(args[0])
	json.Unmarshal([]byte(Client), &Client_float) //数据格式的转换，用于后期SDK的查询
	return shim.Success(Client)
}
func (t *SimpleChaincode) query_data(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// 用于查询存放在链上的梯度数据的ipfs值，用于提取数据
	Client1, _ := stub.GetState(args[0]) //获取链上对应key的ipfs文件哈希
	data1 := string(Client1)
	fmt.Println(Client1) //因为获取ifps文件需要读取字符串类型的数据，所以转换成能读取文件的数据类型
	// json.Unmarshal((Client), &data1)
	str1 := CatIPFS(data1)                        //调用go-ipfs-api获取ipfs中存放的数据，这里读到的数据类型是string
	transaction2 := unmarshalStruct([]byte(str1)) //数据格式的转换，这里主要是便于后期的聚合计算
	fmt.Println(reflect.TypeOf(transaction2))
	// fmt.Println(transaction2)
	// // 数据上传
	// data2, _ := json.Marshal(transaction2)
	return shim.Success(nil)
}

func (t *SimpleChaincode) aggregation(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// 从ipfs读取公钥,因为公钥是不变的,所以公钥只需要上传一次到ifps,返回的哈希也是不变的
	pk_str := CatIPFS("Qmcvf5LZXD5hHvK79FfNYHs2NP49Ngf7hZ9eN1DSr5ZSjT") //利用存在ipfs的公钥哈希读取公钥
	//利用加密库函数将数据处理成公钥
	pk := paillier.PublicKeyFromString(pk_str)
	//从ipfs下载梯度数据
	//客户端数据
	cli_str1_hash, _ := stub.GetState(args[0])
	cli_str1 := CatIPFS(string(cli_str1_hash)) //在链上从ipfs中下载客户端数据进行聚合
	//数据反序列化
	cli_gradient1 := unmarshalStruct_gradient([]byte(cli_str1)) //数据反序列化成需要的结构体类型
	// fmt.Println(str2)
	aggregation_gradient_byte, _ := stub.GetState(args[1]) //从链上获得该轮未聚合完成的数据,继续聚合
	aggregation_gradient := unmarshalStruct_gradient(aggregation_gradient_byte)
	var aggregation_pailli_gradient Pailli_gradient
	value := reflect.ValueOf(cli_gradient1)
	for i := 0; i < value.NumField(); i++ { //利用reflect函数对结构体进行反射,依次对反射出来的数据依次进行处理.
		switch i { //我们需要对结构体进行遍历,对每个对象中的元素进行处理
		case 0: //对反射出来的对象进行处理,我们先对结构体中的数据进行反射,可以将数据对应到用for循环提取的形式,然后通过循环对数据进行处理
			n := new(big.Int) //创建大整数,用于存放用于解密的数据,因为解密的数据类型是大整数
			m := new(big.Int)
			paill := new(big.Int)
			paill_string := make([][]string, 50) //创建用于存储加密数据的对应的数据结构
			for i := 0; i < 50; i++ {
				paill_string[i] = make([]string, 10)
			}
			// 对数组中的数据分别进行加密并存入新数组中
			for i := 0; i < len(cli_gradient1.W2); i++ {
				for j := 0; j < len(cli_gradient1.W2[0]); j++ {
					cli_pa1, _ := m.SetString((cli_gradient1.W2[i][j]), 10)
					cli_pa2, _ := n.SetString((aggregation_gradient.W2[i][j]), 10)
					paill = paillier.Add(pk, cli_pa1, cli_pa2)
					paill_string[i][j] = paill.String()

				}
			}
			aggregation_pailli_gradient.W2 = paill_string //将获得的加密数据的数组加入到结构体中
			// fmt.Println(paill)
		case 1:
			n := new(big.Int) //创建大整数,用于存放用于解密的数据,因为解密的数据类型是大整数
			m := new(big.Int)
			// fmt.Println(data1)
			//数据加密
			paill := new(big.Int)
			paill_string := make([]string, 10) //创建用于存储加密数据的对应的数据结构(1*10形式的数组)
			// 对数组中的数据分别进行加密并存入新数组中
			for i := 0; i < len(cli_gradient1.W2_cons); i++ {
				cli_pa1, _ := m.SetString((cli_gradient1.W2_cons[i]), 10)
				cli_pa2, _ := n.SetString((aggregation_gradient.W2_cons[i]), 10)
				paill = paillier.Add(pk, cli_pa1, cli_pa2)
				paill = paillier.Add(pk, cli_pa1, cli_pa2)
				paill_string[i] = paill.String()
			}
			fmt.Println(reflect.TypeOf(paill))
			aggregation_pailli_gradient.W2_cons = paill_string //将获得的加密数据的数组加入到结构体中
		case 2:
			n := new(big.Int) //创建大整数,用于存放用于解密的数据,因为解密的数据类型是大整数
			m := new(big.Int)
			paill := new(big.Int)
			paill_string := make([][]string, 784) //创建用于存储加密数据的对应的数据结构
			for i := 0; i < 784; i++ {
				paill_string[i] = make([]string, 50)
			}
			// 对数组中的数据分别进行加密并存入新数组中
			for i := 0; i < len(cli_gradient1.W1); i++ {
				for j := 0; j < len(cli_gradient1.W1[0]); j++ {
					cli_pa1, _ := m.SetString((cli_gradient1.W1[i][j]), 10)
					cli_pa2, _ := n.SetString((aggregation_gradient.W1[i][j]), 10)
					paill = paillier.Add(pk, cli_pa1, cli_pa2)
					paill_string[i][j] = paill.String()

				}
			}
			aggregation_pailli_gradient.W1 = paill_string //将获得的加密数据的数组加入到结构体中
		case 3:
			n := new(big.Int) //创建大整数,用于存放用于解密的数据,因为解密的数据类型是大整数
			m := new(big.Int)
			//数据加密
			paill := new(big.Int)
			paill_string := make([]string, 50) //创建用于存储加密数据的对应的数据结构
			// 对数组中的数据分别进行加密并存入新数组中
			// fmt.Println(reflect.TypeOf(user.W2))
			for i := 0; i < len(cli_gradient1.W1_cons); i++ {
				cli_pa1, _ := m.SetString((cli_gradient1.W1_cons[i]), 10)
				cli_pa2, _ := n.SetString((aggregation_gradient.W1_cons[i]), 10)
				paill = paillier.Add(pk, cli_pa1, cli_pa2)
				paill_string[i] = paill.String()
			}
			fmt.Println(reflect.TypeOf(paill))
			aggregation_pailli_gradient.W1_cons = paill_string //将获得的加密数据的数组加入到结构体中
			fmt.Println(reflect.TypeOf(aggregation_pailli_gradient.W1_cons))
		}
	}
	fmt.Println(reflect.TypeOf(aggregation_pailli_gradient.W1[1][1]))
	//后续通过sdk将每一次的聚合数据上传到链上,作为下一次该轮计算的基础
	data := marshalStruct_gradient(aggregation_pailli_gradient) //数据序列化,用于上传到链上,便于进行聚合
	hash := UploadIPFS(string(data))                            //将序列化的数据进行转换成string形式进行上传到ipsf中
	fmt.Println("文件hash是", hash)

	return shim.Success([]byte(hash))
}

// func (t *SimpleChaincode) Reward(stub shim.ChaincodeStubInterface, args []string) pb.Response {
// 	var Client_float float64
// 	Client, _ := stub.GetState(args[0])
// 	json.Unmarshal([]byte(Client), &Client_float) //数据格式的转换，用于后期SDK的查询

// 	return shim.Success(nil)

// }
func (t *SimpleChaincode) invoke(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	// 从ipfs容器获取客户端的数据
	Client, _ := stub.GetState(args[0]) //获取链上对应key的ipfs文件哈希
	data1 := string(Client)             //因为获取ifps文件需要读取字符串类型的数据，所以转换成能读取文件的数据类型
	// json.Unmarshal((Client), &data1)
	str1 := CatIPFS(data1)                                      //调用go-ipfs-api获取ipfs中存放的数据，这里读到的数据类型是string
	Client_data := unmarshalStruct_gradient_float([]byte(str1)) //数据格式的转换，这里主要是便于后期的聚合计算
	// 从ipfs容器获取服务器的数据
	Server, _ := stub.GetState(args[1]) //获取链上对应key的ipfs文件哈希
	data2 := string(Server)             //因为获取ifps文件需要读取字符串类型的数据，所以转换成能读取文件的数据类型
	// json.Unmarshal((Client), &data1)
	str2 := CatIPFS(data2)                                      //调用go-ipfs-api获取ipfs中存放的数据，这里读到的数据类型是string
	Server_data := unmarshalStruct_gradient_float([]byte(str2)) //数据格式的转换，这里主要是便于后期的聚合计算
	// 相关性比较
	var sum int
	var numberofelements int
	value := reflect.ValueOf(Client_data)
	for i := 0; i < value.NumField(); i++ { //利用reflect函数对结构体进行反射,依次对反射出来的数据依次进行处理.
		switch i { //我们需要对结构体进行遍历,对每个对象中的元素进行处理
		case 0: //对反射出来的第一个对象进行处理
			var multiplication [][]float64
			row := len(Client_data.W2)
			col := len(Client_data.W2[0])
			numberofelements += row * col
			multiplication = Corresponding(sign(Server_data.W2), sign(Client_data.W2))
			sum += samesign(multiplication) /*相同符号数量之和*/
		case 1:
			var multiplication []float64
			row := len(Client_data.W2cons)
			numberofelements += row
			multiplication = Corresponding_1D(sign_1D(Server_data.W2cons), sign_1D(Client_data.W2cons))
			sum += samesign_1D(multiplication) /*相同符号数量之和*/
		case 2:
			var multiplication [][]float64
			row := len(Client_data.W1)
			col := len(Client_data.W1[0])
			numberofelements += row * col
			multiplication = Corresponding(sign(Server_data.W1), sign(Client_data.W1))
			sum += samesign(multiplication) /*相同符号数量之和*/
		case 3:
			var multiplication []float64
			row := len(Client_data.W1cons)
			numberofelements += row
			multiplication = Corresponding_1D(sign_1D(Server_data.W1cons), sign_1D(Client_data.W1cons))
			sum += samesign_1D(multiplication) /*相同符号数量之和*/
		}
	}
	fmt.Println(sum)
	e := float64(sum)/float64(numberofelements) + 0.000001 //计算相同相同符号所占的比例
	Threshold := strconv.FormatFloat(e, 'E', -1, 32)
	fmt.Println(Threshold)
	return shim.Success([]byte(Threshold))
}

func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Println("Error starting Simple chaincode : %s", err)
	}
}
