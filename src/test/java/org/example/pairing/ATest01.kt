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
}