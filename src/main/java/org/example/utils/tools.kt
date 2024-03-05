package org.example.utils

import it.unisa.dia.gas.jpbc.Element


fun product(vx: List<Element>, vy: List<Element>): Element {
    require(vx.isNotEmpty() and (vx.size == vy.size))
    val zr = vx[0].field
    val ans = zr.newZeroElement()
    for ((x, y) in vx.zip(vy)) {
        ans.add(x.mul(y))
    }
    return ans
}
