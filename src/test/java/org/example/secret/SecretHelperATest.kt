package org.example.secret

import it.unisa.dia.gas.jpbc.Element
import org.example.utils.PairingGroupHelper
import org.junit.jupiter.api.Test


class SecretHelperATest {

    @Test
    fun recoverShare() {
        val pg = PairingGroupHelper.getSS512()!!
        val secretHelper = SecretHelper(pg.zr)
        val pgHelper = PairingGroupHelper(pg)
        println("zr: ${pg.zr.nqr}")
        val s = pg.zr.newElement().set(1123)
        println(s)
        val shares = secretHelper.generateShares(s, 3, 6)
        println("shares: $shares")

        val ts = mutableMapOf<Element, Element>()
        val xList = shares.keys.toList()
        val recovered = secretHelper.recoverShare(shares)
        println("recovered: $recovered")
        assert(s.isEqual(recovered))
    }
}