package org.example.pairing

import it.unisa.dia.gas.jpbc.Element
import org.example.utils.PairingGroupHelper
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals

class ATest01 {

    @Test
    fun testOp() {
        val pg = PairingGroupHelper.getSS512()!!
        val nqr = pg.zr.nqr
        val x = pg.zr.newElement().set(100)
        assert(x != x.duplicate().negate())
//        assert(x.duplicate().negate().toBigInteger() == BigInteger.valueOf(-100) % nqr.toBigInteger())
//        assert(x != x.duplicate().invert())
    }

    @Test
    fun testZr() {
        val pg = PairingGroupHelper.getSS512()!!
        val zr = pg.zr
        val x: Element = zr.newElement().set(100)
        val y = zr.newElement().set(100)

        // 两个相同数字的hash值不一样
        assertEquals(x, y)
        assertEquals(x.hashCode(), y.hashCode())

    }
    @Test
    fun testG1() {
        val pg = PairingGroupHelper.getSS512()!!
        val g1 = pg.g1
        val x = g1.newRandomElement()
        val y = pg.zr.newZeroElement()
//        println(x.powZn(y))
        assertEquals(g1.newOneElement(), x.powZn(y))
    }

    @Test
    fun testGTZero() {
        val pg = PairingGroupHelper.getSS512()!!
        val zero = pg.zr.newZeroElement()
        val g = pg.g1.newRandomElement()
        // g^x = 1
        // x = 0 or x = order(g)
        val egg = pg.pairing(g, g).immutable
        val eggZero = egg.powZn(zero).immutable
        val one = pg.gt.newOneElement().immutable
        assertEquals(one, eggZero)
//        assertEquals(one, zero)

    }
}