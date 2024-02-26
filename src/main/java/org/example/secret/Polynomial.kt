package org.example.secret

import it.unisa.dia.gas.jpbc.Element


// Element -> zr
class Polynomial {
    val coefficients = mutableListOf<Element>()

    fun degree(): Int {
        return coefficients.size-1
    }

    // a0 + a1*x + a2*x^2 + ... + an*x^n
    fun eval(x: Element): Element {
        val result = coefficients[0].duplicate()
        val xPower = x.duplicate()
        for (i in 1..degree()) {
            result.add(coefficients[i].duplicate().mul(xPower))
            xPower.mul(x)
        }
        return result.immutable
    }

//    companion object {
//
//    }
}