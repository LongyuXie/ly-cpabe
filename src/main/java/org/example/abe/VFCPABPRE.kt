package org.example.abe

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.abe.rouselakiswaters.RWABE
import org.example.policy.AccessTree
import org.example.policy.Attribute
import org.example.utils.*
import org.w3c.dom.Attr
import kotlin.experimental.xor

class VFCPABPRE {

    class PublicKey (
        val pg: Pairing,
        val g: Element,
        val g_a: Element,
        val attrMap: Map<Attribute, Element>,
        val e_gg_alpha: Element,
        val e_gg_beta: Element,
        val prg: (Element) -> ByteArray
    )

    class MasterSecretKey (val g_alpha: Element, val g_beta: Element)

    class Ciphertext (
        val c0: ByteArray,
        val c1: Element,
        val c2: Element,
        val d0: Element,
        val dMap: Map<Attribute, Pair<Element, Element>>
    )

    class SecretKey (
        val k:Element,
        val k0:Element,
        val kxMap: Map<Attribute, Element>,
        val b: Element,
        val b0: Element,
        val bxMap: Map<Attribute, Element>
    )

    lateinit var pk: PublicKey
    lateinit var msk: MasterSecretKey
    lateinit var helper: PairingGroupHelper
    var attrNum: Int = 0

    fun setup(): PublicKey {
        attrNum = 80
        val pg = PairingGroupHelper.getSS512()!!
        helper = PairingGroupHelper(pg)
        val hashHelper = HashHelper(pg)
        val g = helper.randomG1()
        val alpha = helper.randomZr()
        val beta = helper.randomZr()
        val e_gg_alpha = helper.pairing(g, g).powZn(alpha)
        val e_gg_beta = helper.pairing(g, g).powZn(beta)
        val prg = { it: Element -> HashHelper.sha512(it.toBytes()) }
        val g_a = g.powZn(helper.randomZr()).immutable
        val attrMap = mutableMapOf<Attribute, Element>()
        for (i in 0 until attrNum) {
            val attr = Attribute("attr$i")
            attrMap[attr] = hashHelper.hashFromStringToG1(attr.name)
        }

        this.msk = MasterSecretKey(g.powZn(alpha).immutable, g.powZn(beta).immutable)
        return PublicKey(pg, g, g_a, attrMap, e_gg_alpha, e_gg_beta, prg)
    }

    fun keygen(pk: PublicKey, msk: MasterSecretKey, attrs: List<Attribute>): SecretKey {
        val g = pk.g
        val g_a = pk.g_a

        val t1 = helper.randomZr()
        val t2 = helper.randomZr()

        val k = msk.g_alpha.mul(g_a.powZn(t1))
        val b = msk.g_beta.mul(g.powZn(t2))

        val k0 = g.powZn(t1)
        val b0 = g.powZn(t2)

        val kxMap = mutableMapOf<Attribute, Element>()
        val bxMap = mutableMapOf<Attribute, Element>()

        for (attr in attrs) {
            val hx = pk.attrMap[attr]!!
            kxMap[attr] = hx.powZn(t1).immutable
            bxMap[attr] = hx.powZn(t2).immutable
        }
        return SecretKey(k, k0, kxMap, b, b0, bxMap)
    }


    fun xorArray(ba: ByteArray, bb: ByteArray): ByteArray {
        val len = ba.size
        val ans = ByteArray(len)
        for (i in 0 until len) {
            ans[i] = ba[i] xor bb[i]
        }
        return ans
    }

    fun encrypt(pk: PublicKey, m: ByteArray, policy: String): Ciphertext {
        val tree = AccessTree.fromPolicyString(policy)
        val s = helper.randomZr()
        val K = helper.randomGT()
        val shares = tree.generateShares(s)

        val c0 = xorArray(m, pk.prg(K))
        val c1 = K.mul(pk.e_gg_alpha.powZn(s)).immutable
        val c2 = pk.e_gg_beta.powZn(s).immutable

        val d0 = pk.g.powZn(s).immutable


        for ((attr, share) in shares) {
//            println(attr)
            val zi = helper.randomZr()
            val d1i = pk.g_a.powZn(share).mul(pk.attrMap[attr.noId()]!!.powZn(zi.negate())).immutable
            val d2i = pk.g.powZn(zi).immutable
        }

        return Ciphertext(c0, c1, c2, d0, emptyMap())
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            benchmark()
        }


        @JvmStatic
        fun benchmark() {

            for (i in 5..20) {
                val abe = VFCPABPRE()
                val pk = abe.setup()
                val policy = randomPolicy(i)
//                println(policy)
//                println(pk.attrMap.keys.toList())
//                val m = abe.helper.randomGT()
                val message = HashHelper.sha512("hello world".toByteArray())

                var cph: VFCPABPRE.Ciphertext? = null

                val t1 = execTime { cph = abe.encrypt(pk, message, policy) }
                println("$i, $t1")
            }


        }
    }


}