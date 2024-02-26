package org.example.utils

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory

class PairingGroupHelper(val pg: Pairing) {

    fun randomZr(): Element {
        return pg.zr.newRandomElement().immutable
    }
    fun randomG1(): Element {
        return pg.g1.newRandomElement().immutable
    }
    fun randomG2(): Element {
        return pg.g2.newRandomElement().immutable
    }
    fun randomGT(): Element {
        return pg.gt.newRandomElement().immutable
    }

    fun randomZrArray(size: Int): Array<Element> {
        val arr = Array<Element>(size) { pg.zr.newRandomElement().immutable }
        return arr
    }

    fun pairing(g1: Element, g2: Element): Element {
        return pg.pairing(g1, g2).immutable
    }


    fun setInt(x: Int) : Element {
        return pg.zr.newElement().set(x).immutable
    }

    companion object {
        fun getSS512(): Pairing? {
            return PairingFactory.getPairing("src/main/resources/params/curves/a.properties")
        }

    }
}