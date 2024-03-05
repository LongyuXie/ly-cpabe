package org.example.abe

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.policy.AccessTree
import org.example.policy.Attribute
import org.example.utils.*

class GSBABE2021 {

    val hxMap: MutableMap<Attribute, Element> = mutableMapOf()

    lateinit var g: Element
    lateinit var alpha: Element
    lateinit var a: Element
    lateinit var g_a: Element
    lateinit var e_gg_alpha: Element
    lateinit var pg: Pairing
    lateinit var helper: PairingGroupHelper
    lateinit var phi: Element
    lateinit var varPhi: Element

    fun regAttr(a: Attribute): Element{
        if (a in hxMap) {
            return hxMap[a]!!
        }
        val h = helper.randomG1()
        hxMap[a] = h
        return h
    }

    fun setup() {
        pg = PairingGroupHelper.getSS512()!!
        helper = PairingGroupHelper(pg)
        g = helper.randomG1()
        a = helper.randomZr()
        g_a = g.powZn(a).immutable
        alpha = helper.randomZr()
        e_gg_alpha = helper.pairing(g, g).powZn(alpha)
        phi = helper.randomZr()
        varPhi = helper.randomZr()
    }

    // gt -> zr
    fun H(gte: Element): Element {
        return HashHelper(pg).hashFromBytesToZp(gte.toCanonicalRepresentation())
    }

    fun keygen(attr: Set<Attribute>): Map<Any, Any> {
        val s = helper.randomZr()
        val k1 = g.powZn(alpha).mul(g_a.powZn(s)).immutable
        val k2 = g.powZn(s).immutable
        val kxMap = mutableMapOf<Attribute, Element>()
        for (a in attr) {
            val h = hxMap[a]!!
            val kx = h.powZn(s).immutable
            kxMap[a] = kx
        }
        return mapOf("k" to k1, "k" to k2, "kxMap" to kxMap)
    }

    fun encryption(m: Element, policy: String): Map<Any, Any> {
        val r = helper.randomZr()
        val r1 = helper.randomZr()

        val tree = AccessTree.fromPolicyString(policy)

        val sh1 = tree.generateShares(r)
        val sh2 = tree.generateShares(r1)

        val m1 = helper.randomGT()

        val c1 = m.mul(e_gg_alpha.powZn(r)).immutable
        val d1 = m1.powZn(r).immutable

        val c2 = g.powZn(r)
        val d2 = g.powZn(r1)

        val CjMap = mutableMapOf<Attribute, Pair<Element, Element>>()
        val DjMap = mutableMapOf<Attribute, Pair<Element, Element>>()

        for ((attr, share) in sh1) {
            val h = hxMap[attr.noId()]!!
            val rj = helper.randomZr()
            val c3j = g_a.powZn(share).mul(h.powZn(rj.negate()))
            val c4j = g.powZn(rj)
            CjMap[attr] = Pair(c3j, c4j)
        }

        for ((attr, share) in sh2) {
            val h = hxMap[attr.noId()]!!
            val rj = helper.randomZr()
            val d3j = g_a.powZn(share).mul(h.powZn(rj.negate()))
            val d4j = g.powZn(rj)
            DjMap[attr] = Pair(d3j, d4j)
        }

        return mapOf(
            "C1" to c1,
            "D1" to d1,
            "C2" to c2,
            "D2" to d2,
            "CjMap" to CjMap,
            "DjMap" to DjMap
        )
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            for (i in 5..20) {
                val abe = GSBABE2021()
                abe.setup()
                val policy = randomPolicy(i)
                extractAttrs(policy).forEach {
                    val a = Attribute(it)
                    abe.regAttr(a)
                }
                val m = abe.helper.randomGT()

                val t1 = execTime { abe.encryption(m, policy) }
                println("$i, $t1")
            }
        }
    }

}