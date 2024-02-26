package org.example.secret

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Field
import org.example.policy.Threshold

// Element -> zr
class SecretHelper(val zr: Field<Element>) {

    /**
     * @param s zr secret
     * @param k threshold
     * @param n the number of shares
     * @return a map of shares, key in [1, n]
     */
    fun generateShares(s: Element, k: Int, n: Int): MutableMap<Element, Element> {
        require(k <= n)

        val p = randomPolynomial(k-1, constant = s)
        val shares = mutableMapOf<Element, Element>()
        for (i in 1..n) {
            val x = zr.newElement().set(i).immutable
            shares[x] = p.eval(x)
        }
        return shares
    }

    fun generateShares(s: Element, threshold: Threshold): MutableMap<Element, Element> {
        return generateShares(s, threshold.k, threshold.n)
    }

    fun lagrangeCoefficient(xi: Element, xList: List<Element>): Element {
        val cof = zr.newOneElement()

        for (xj in xList) {
            if (xi != xj) {
                val tmp = xj.duplicate()
                tmp.div(xj.duplicate().sub(xi))
                cof.mul(tmp)
            }
        }
        return cof
    }

    // 拉格朗日插值公式
    fun recoverShare(shares: Map<Element, Element>): Element {
        val xList = shares.keys.toList()
        val share = zr.newZeroElement()

        shares.forEach {
            x, y ->
            val cof = lagrangeCoefficient(x, xList)
            share.add(y.duplicate().mul(cof))
        }

        return share.immutable
    }

    fun randomPolynomial(deg: Int, constant: Element? = null): Polynomial {
        val p = Polynomial()
        if (constant != null) {
            p.coefficients.add(constant.immutable)
        } else {
            p.coefficients.add(zr.newRandomElement().immutable)
        }
        for (i in 1..deg) {
            p.coefficients.add(zr.newRandomElement().immutable)
        }
        return p
    }

}