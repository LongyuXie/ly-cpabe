package org.example.utils

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Field

// Element -> zr
class SecretHelper(val zr: Field<Element>) {

    /**
     * @param s zr secret
     * @param k threshold
     * @param n the number of shares
     * @return a map of shares, key in [1, n]
     */
    fun generateShares(s: Element, k: Int, n: Int): List<Element> {
        require(k in 1..n)

        val p = randomPolynomial(k-1, constant = s)
        val shares = mutableListOf<Element>()
        for (i in 1..n) {
            val x = zr.newElement().set(i).immutable
            shares.add(p.eval(x))
        }
        return shares
    }

    /**
     * @param index the index of the share
     * @param xList the list of x values
     * @return the lagrange coefficient with x = 0
     */
    fun lagrangeCoefficient(index: Int, xList: List<Element>): Element {
        val cof = zr.newOneElement()
        val xi = xList[index]

        for (xj in xList) {
            if (xi != xj) {
                val tmp = xj.duplicate()
                tmp.div(xj.duplicate().sub(xi))
                cof.mul(tmp)
            }
        }
        return cof
    }

    /**
     * assume the xList is [1, 2, 3, ..., n]
     * @param yList the list of shares
     * @return the recovered secret
     */
    // 拉格朗日插值公式
    fun recoverShare(yList: List<Element>): Element {
        val xList = 1.rangeTo(yList.size).map { zr.newElement().set(it).immutable }
        val share = zr.newZeroElement()

        for (i in xList.indices) {
            val yi = yList[i]
            val cof = lagrangeCoefficient(i, xList)
            share.add(yi.duplicate().mul(cof))
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