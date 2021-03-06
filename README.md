# CommunicationSystem

该项目是基于**Hyperledger Fabric**联盟区块链平台开发的安全通信系统，该系统结合**CP-ABE**基于属性加密技术以及**WeEvent Broker**中间件实现安全可靠且高效的数据加密解密、数据密文通信等操作，同时结合**CA公钥证书认证机制**以及部署在**Fabric Network**上的基于**Golang**语言编写的智能合约实现细粒度的身份验证功能。


## 该安全通信系统主要的创新点如下
  ### 1.基于WeEvent Broker以及CP-ABE数据加密技术实现在Fabric Network中进行密文数据以及密钥的传播。
  >我们采用微众银行开源的**WeEvent**（基于区块链的跨平台通信中间件机制）在部署的Fabric Network网络中传输密文以及通信方密钥。该密文采用CP-ABE基于属性加密技术加密而成，通过预设的控制访问策略可为属性集符合该策略的通信方提供细粒度的数据控制访问，确保属性集不符合该访问策略的恶意用户无法解密在区块链网络中传播的密文数据。同时通过**WeEvent Broker**中间件将通信方的密钥发布至区块链网络，确保只有符合身份的通信方才能获取对应的密钥。
  
  ### 2.基于CA公钥证书机制实现在Fabric Network中进行可信的身份认证机制。
  >该系统将通信方的证书、证书Hash以及签名信息传入分布式账本中进行可信存证，当其他通信方需要验证发起通信方的身份，可通过智能合约请求区块链节点检索本地账本并返回发起通信方的证书文件以及签名信息进行验证。



## Related Efforts
- [WeEvent](https://github.com/WeBankBlockchain/WeEvent) \- 通信中间件的底层系统。
