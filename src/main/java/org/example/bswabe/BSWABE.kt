package org.example.bswabe

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.policy.AccessTree
import org.example.policy.AccessTreeNode
import org.example.policy.Attribute
import org.example.secret.SecretHelper
import org.example.utils.HashHelper
import org.example.utils.PairingGroupHelper
import java.util.*

class BSWABE() {


    data class MasterPublicKey(val pg: Pairing, val g: Element, val h: Element, val f: Element, val e_gg_alpha: Element)

    lateinit var mpk: MasterPublicKey

    private lateinit var pg: Pairing
    private lateinit var helper: PairingGroupHelper

    private lateinit var alpha: Element // zr
    private lateinit var beta: Element  // zr
    private lateinit var g: Element // G1
    private lateinit var h: Element // G1
    private lateinit var f: Element // G1
    private lateinit var e_gg_alpha: Element //G2

    class SecretKey {
        var D: Element? = null
        var dMap: Map<Attribute, Pair<Element, Element>>? = null
    }


    fun setup() {
        this.pg = PairingGroupHelper.getSS512()!!
        this.helper = PairingGroupHelper(this.pg)
        this.beta = helper.randomZr()
        this.alpha = helper.randomZr()
        this.g = helper.randomG1()
        this.h = g.powZn(beta).immutable
        this.f = g.powZn(beta.invert()).immutable
        this.e_gg_alpha = helper.pairing(g, g).powZn(alpha)
        this.mpk = MasterPublicKey(pg, g, h, f, e_gg_alpha)
    }

    // to G1
    fun hashAttr(attr: Attribute): Element {
        return HashHelper(pg).hashFromStringToG1(attr.name)
    }

    fun keygen(attrs: Set<Attribute>): SecretKey {
        val secretKey = SecretKey()
//        val r = helper.randomZr()
        val r = helper.setInt(10)
        secretKey.D = g.powZn((alpha.add(r).div(beta)))
        secretKey.dMap = mutableMapOf<Attribute, Pair<Element, Element>>().apply {
            for (attr in attrs) {
                val rj = helper.randomZr()
                val dj = g.powZn(r).mul(hashAttr(attr).powZn(rj)).immutable
                val djPrime = g.powZn(rj).immutable
                this[attr] = Pair(dj, djPrime)
            }
        }
        return secretKey
    }


    // m: Gt
    fun enc(m: Element, policy: String): Map<Any, Any> {
        val tree = AccessTree.fromPolicyString(policy)
//        val s = helper.randomZr()
        val s = helper.setInt(20)
        val r = helper.setInt(10)
        println("e_gg_rs: ${helper.pairing(g, g).powZn(r).powZn(s)}")
        val C_Tilde = m.duplicate().mul(e_gg_alpha.powZn(s)).immutable
        val C = h.powZn(s).immutable

        tree.fillPolicy(s, mpk)
        return mutableMapOf<Any, Any>().apply {
            put("C", C)
            put("C_Tilde", C_Tilde)
            put("tree", tree)
        }
    }

    fun dec(ciphertext: Map<Any, Any>, sk: SecretKey): Element? {
        val C_Tilde = ciphertext["C_Tilde"] as Element // m e(g,g)^{alpha*s}
        val C = ciphertext["C"] as Element //
        val tree = ciphertext["tree"] as AccessTree

        val e_gg_rs = decryptNode(sk, tree.getRoot()!!) ?: return null
        println("e_gg_rs: $e_gg_rs")


        // 计算明文
        val result = C_Tilde.duplicate()
        val t = helper.pairing(C, sk.D!!).div(e_gg_rs)
        result.div(t)

        return result

    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val bswabe = BSWABE()
            bswabe.setup()
            val attrs = setOf(Attribute("a"), Attribute("b"), Attribute("c"))
            val sk = bswabe.keygen(attrs)
//            val m = bswabe.helper.randomG()
            val m = bswabe.helper.randomGT()
            val policy = "(A or B) and (C or D)".lowercase(Locale.getDefault())
//            val policy = "(a or b)".lowercase(Locale.getDefault())

            println("policy: $policy")
            println("enc")
            val ciphertext = bswabe.enc(m, policy)
            println("message: $m")
            println("dec")
            val result = bswabe.dec(ciphertext, sk)

//            println(bswabe.pg.pairing(bswabe.mpk.g, bswabe.mpk.g).powZn(bswabe.pg.zr.newElement().set(200)))

            if (result != null) {
                println("result: $result")
                if (m.isEqual(result)) {
                    println("Decryption success")
                } else {
                    println("Decryption failed")
                }
            } else {
                println("result is null")
                println("Decryption failed")
            }

        }
    }

    /**
     * @return GT element
     */
    private fun decryptNode(sk: SecretKey, node: AccessTreeNode): Element? {
        if (node.isLeaf) {
            return if (sk.dMap?.contains(node.attr!!) == true) {
                val di = sk.dMap!![node.attr!!]!!.first
                val diPrime = sk.dMap!![node.attr!!]!!.second
                val cx = node.cx!!
                val cxPrime = node.cxPrime!!
                println("node ${node.attr!!.name}")

                var result = helper.pairing(di, cx)
                result = result.div(helper.pairing(diPrime, cxPrime))
                println("e(g, g)^{r q_x(0)} = $result")
                result
            } else {
                null
            }

        }
        val k = node.threshold!!.k
        val children = node.children
        val satisfied = mutableListOf<Int>()
        for (i in children.indices) {
            val c = children[i]
            if (c.satisfy(sk.dMap!!.keys)) {
                satisfied.add(i)
            }
        }
        if (satisfied.size < k) {
            return null
        }

        val secretHelper = SecretHelper(pg.zr)

        val xList = satisfied.map { i -> secretHelper.zr.newElement().set(i + 1).immutable }
        val result = pg.gt.newOneElement()
        for (i in satisfied) {
            val c = children[i]
            var fz = decryptNode(sk, c)!!
            val cof = secretHelper.lagrangeCoefficient(xList[i], xList)
            fz = fz.duplicate().powZn(cof)
            result.mul(fz)
        }
        return result
    }
}