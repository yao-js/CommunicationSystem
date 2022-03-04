package paillier

import (
	"crypto/rand"
	"fmt"
	"math/big"
	"strings"
)

var zero = new(big.Int).SetInt64(0)
var one = new(big.Int).SetInt64(1)

type Keypair struct {
	p *big.Int
	q *big.Int
}

type PublicKey struct {
	n  *big.Int
	g  *big.Int
	n2 *big.Int
}

type PrivateKey struct {
	mu     *big.Int
	lambda *big.Int
	pk     *PublicKey
}

func GenerateKeypair(bitlen uint) *Keypair {
	if bitlen < 1024 {
		panic("keypair bitlen must be at least 1024")
	}

	p := getPrime(int(bitlen) / 2)
	q := getPrime(int(bitlen) / 2)

	return &Keypair{p, q}
}

func (kp *Keypair) ToKeys() (*PublicKey, *PrivateKey) {
	n := new(big.Int).Mul(kp.p, kp.q)
	n2 := new(big.Int).Mul(n, n)

	lambda := phi(kp.p, kp.q)
	mu := new(big.Int).ModInverse(lambda, n)
	g := new(big.Int).Add(n, one)

	pk := &PublicKey{n, g, n2}
	sk := &PrivateKey{mu, lambda, pk}

	return pk, sk
}

func (kp *Keypair) String() string {
	return fmt.Sprintf("%v;%v", kp.p.Text(16), kp.q.Text(16))
}

func KeypairFromString(s string) *Keypair {
	pieces := strings.Split(s, ";")

	if len(pieces) != 2 {
		panic("there aren't two pieces to the public key")
	}

	p, ok := new(big.Int).SetString(pieces[0], 16)

	if !ok {
		panic("invalid value for the prime p")
	}

	q, ok := new(big.Int).SetString(pieces[1], 16)

	if !ok {
		panic("invalid value for the prime q")
	}

	return &Keypair{p, q}
}

func (pk *PublicKey) String() string {
	return pk.n.Text(16)
}

func PublicKeyFromString(s string) *PublicKey {
	n, ok := new(big.Int).SetString(s, 16)

	if !ok {
		panic("invalid value for the modulus n")
	}

	g := new(big.Int).Add(n, one)
	n2 := new(big.Int).Mul(n, n)

	return &PublicKey{n, g, n2}
}

func Encrypt(pk *PublicKey, pt int64) *big.Int {
	m := new(big.Int).SetInt64(pt)

	if pt < 0 || m.Cmp(zero) == -1 || m.Cmp(pk.n) != -1 {
		panic("invalid plaintext")
	}

	r := getRandom(pk.n)
	r.Exp(r, pk.n, pk.n2)

	m.Exp(pk.g, m, pk.n2)

	c := new(big.Int).Mul(m, r)
	return c.Mod(c, pk.n2)
}

func Decrypt(sk *PrivateKey, ct *big.Int) int64 {
	if ct == nil || ct.Cmp(zero) != 1 {
		panic("invalid ciphertext")
	}

	m := L(new(big.Int).Exp(ct, sk.lambda, sk.pk.n2), sk.pk.n)
	m.Mul(m, sk.mu)
	m.Mod(m, sk.pk.n)

	return m.Int64()
}

func Add(pk *PublicKey, ct1, ct2 *big.Int) *big.Int {
	if ct1 == nil || ct2 == nil || ct1.Cmp(zero) != 1 || ct2.Cmp(zero) != 1 {
		panic("invalid add input")
	}
	z := new(big.Int).Mul(ct1, ct2)

	return z.Mod(z, pk.n2)
}

func Mul(pk *PublicKey, ct *big.Int, msg int64) *big.Int {
	if ct == nil || ct.Cmp(zero) != 1 {
		panic("invalid mul input")
	}
	return new(big.Int).Exp(ct, new(big.Int).SetInt64(msg), pk.n2)
}

func BatchAdd(pk *PublicKey, cts ...*big.Int) *big.Int {
	total := new(big.Int).SetInt64(1)
	for i, ct := range cts {
		total.Mul(total, ct)
		if i%5 == 0 {
			total.Mod(total, pk.n2)
		}
	}
	return total.Mod(total, pk.n2)
}

func L(x, n *big.Int) *big.Int {
	return new(big.Int).Div(new(big.Int).Sub(x, one), n)
}

func getPrime(bits int) *big.Int {
	p, err := rand.Prime(rand.Reader, bits)
	if err != nil {
		panic("Error reading crypto/rand")
	}

	return p
}

func getRandom(n *big.Int) *big.Int {
	gcd := new(big.Int)
	r := new(big.Int)
	err := fmt.Errorf("")

	for gcd.Cmp(one) != 0 {
		r, err = rand.Int(rand.Reader, n)
		if err != nil {
			panic("Error while reading crypto/rand")
		}

		gcd = new(big.Int).GCD(nil, nil, r, n)
	}
	return r
}

func lambda(p, q *big.Int) *big.Int {
	l := new(big.Int).GCD(nil, nil, p, q)
	return l.Mul(l.Div(p, l), q)
}

func phi(x, y *big.Int) *big.Int {
	p1 := new(big.Int).Sub(x, one)
	q1 := new(big.Int).Sub(y, one)
	return new(big.Int).Mul(p1, q1)
}
