package org.example.abe

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.abe.rouselakiswaters.RWABE
import org.example.policy.AccessTree
import org.example.policy.Attribute
import org.example.utils.*
import java.security.SecureRandom

class SACS {


    lateinit var pg: Pairing
    lateinit var g: Element
    lateinit var g_a: Element
    lateinit var e_gg_alpha: Element
    lateinit var alpha: Element
    lateinit var g_alpha: Element
    lateinit var phi: Element
    lateinit var varPhi: Element
    lateinit var Q: Element

    lateinit var helper: PairingGroupHelper

    private val attrHash = mutableMapOf<Attribute, Element>()

    fun regAttr(a: Attribute): Int {
        if (a in attrHash) {
            return attrHash[a]!!.toBigInteger().toInt()
        }
        val idx = attrHash.size + 10
        val h = helper.randomG1()
        attrHash[a] = h
        return idx
    }

    fun keygen(attr: Set<Attribute>): Map<Any, Any> {
        val s = helper.randomZr()
        val k1 = g_alpha.mul(g_a.powZn(s)).immutable
        val k2 = g.powZn(s).immutable
        val kxMap = mutableMapOf<Attribute, Element>()
        for (a in attr) {
            val h = attrHash[a]!!
            val kx = h.powZn(s).immutable
            kxMap[a] = kx
        }
        return mapOf("k1" to k1, "k2" to k2, "kxMap" to kxMap)
    }

    fun setup() {
        pg = PairingGroupHelper.getSS512()!!
        helper = PairingGroupHelper(pg)
        g = helper.randomG1()
        val a = helper.randomZr()
        g_a = g.powZn(a).immutable
        alpha = helper.randomZr()
        g_alpha = g.powZn(alpha).immutable
        e_gg_alpha = helper.pairing(g, g).powZn(alpha)

        phi = helper.randomG1()
        varPhi = helper.randomG1()
        Q = helper.randomG1()
    }

    fun H1(gte: Element): ByteArray {
        val bytes = gte.toCanonicalRepresentation()
        val h = HashHelper.sha512(bytes)
        return h
    }
    fun H2(bytes: ByteArray): Element {
        return HashHelper(pg).hashFromBytesToZp(bytes)
    }

    fun MLE(bytes: ByteArray): ByteArray{
        return HashHelper.sha512(bytes)
    }

    val xorArray = {a: ByteArray, b: ByteArray -> a.zip(b).map { (x, y) -> (x.toInt() xor y.toInt()).toByte() }.toByteArray()}
    val orArray = {a: ByteArray, b: ByteArray -> a.zip(b).map { (x, y) -> (x.toInt() or y.toInt()).toByte() }.toByteArray()}

    fun randomBytes(size: Int): ByteArray {
        val ret = ByteArray(size)
        val r = SecureRandom()
        ret.let { r.nextBytes(it) }
        return ret
    }


    fun encrypt(m: ByteArray, policy: String): Map<Any, Any> {
        val R = randomBytes(64)
        val r = H2(MLE(orArray(m, R)))
        val tree = AccessTree.fromPolicyString(policy)
        val shares = tree.generateShares(r)

        val egg_alpha_r = e_gg_alpha.powZn(r)
        val C = xorArray(orArray(m, R), H1(egg_alpha_r))
        val C1 = g.powZn(r)
        val C2 = Q.powZn(r)

        val C3Map = mutableMapOf<Attribute, Pair<Element, Element>>()
        for ((a, y) in shares) {
            val rj = helper.randomZr()
            val h = attrHash[a.noId()]!!
            val c3j = g_a.powZn(y).mul(h.powZn(rj.negate())).immutable
            val c4j = g.powZn(rj).immutable
            C3Map[a] = Pair(c3j, c4j)
        }
        return mapOf("C" to C, "C1" to C1, "C2" to C2, "C3Map" to C3Map)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val rec = mutableListOf<MutableList<Int>>()
            for (i in 5..20) {
                val r = mutableListOf<Int>()
                r.add(i)
                val abe = SACS()
                abe.setup()
                val policy = randomPolicy(i)
                extractAttrs(policy).forEach {
                    val a = Attribute(it)
                    abe.regAttr(a)
                }
//                val m = abe.helper.randomGT()
                val message = HashHelper.sha512("hello world".toByteArray())

                var cph: Map<Any, Any>

                val t1 = execTime { cph = abe.encrypt(message, policy) }
                r.add(t1)
                rec.add(r)
            }
            rec.forEach(::println)
        }
    }
}
