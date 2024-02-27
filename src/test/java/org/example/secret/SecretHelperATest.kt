package org.example.secret

import org.example.utils.PairingGroupHelper
import org.example.utils.SecretHelper
import org.junit.jupiter.api.Test


class SecretHelperATest {

    @Test
    fun recoverShare() {
        val pg = PairingGroupHelper.getSS512()!!
        val secretHelper = SecretHelper(pg.zr)
        println("zr: ${pg.zr.nqr}")
        val s = pg.zr.newElement().set(1123)
        val n = 6
        val k = 3
        println(s)
        val shares = secretHelper.generateShares(s, k, n)
        println("shares: $shares")

        val recovered = secretHelper.recoverShare(shares)
        println("recovered: $recovered")
        assert(s.isEqual(recovered))
    }
}