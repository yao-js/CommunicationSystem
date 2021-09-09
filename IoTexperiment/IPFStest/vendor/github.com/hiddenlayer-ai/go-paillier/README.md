# go-paillier

This is an implementation of the Paillier cryptosystem in Golang.

The code is heavilty adapted from [stefanomozart/pailler](https://github.com/stefanomozart/paillier) but with some minor changes to mirror syntax of that of [mortendahl/rust-paillier](https://github.com/mortendahl/rust-paillier). These changes include a more functional programming style, removal of error returning and removal of division / subtraction among other small details.


### Installation

Use `go get` to install the package

```
go get -u github.com/hiddenlayer-ai/go-paillier
```

### Usage

A snippet of code that sums up the entire functionality of go-paillier follows.

```go
package main

import (
	"fmt"

	"github.com/hiddenlayer-ai/go-paillier"
)

func main() {
	pk, sk := paillier.GenerateKeypair(2048).ToKeys()

	str := pk.String()

	pk = paillier.PublicKeyFromString(str)

	c1 := paillier.Encrypt(pk, 10)
	c2 := paillier.Encrypt(pk, 20)
	c3 := paillier.Encrypt(pk, 30)
	c4 := paillier.Encrypt(pk, 40)
	c5 := paillier.Encrypt(pk, 50)
	c6 := paillier.Encrypt(pk, 60)

	c7 := paillier.Add(pk,
		paillier.Add(pk, c1, c2),
		paillier.Add(pk, c3, c4),
	)

	c := paillier.BatchAdd(pk, c5, c6, c7)

	d := paillier.Mul(pk, c, 2)

	m := paillier.Decrypt(sk, d)

	fmt.Println(m) // 420
}
```

The bitlen for the keypair must be at least 1024, but it is recommended to be at least 2048 for real applications.

Note that `GenerateKeypair` does not take any arguments and by default creates 2048-bit keys. This will likely change in the future. Additionally, recognize that the string format of the public key's `String` is `"{n};{g}"` where `{n}` and `{g}` are the respective parameters of the public key. Futhurmore, the keypair struct returned from `GenerateKeypair` has a `String` method and a `KeypairFromString` method.