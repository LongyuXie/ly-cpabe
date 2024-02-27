package org.example.abe.bswabe

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.policy.AccessTree
import org.example.policy.AccessTreeNode
import org.example.policy.Attribute
import org.example.utils.SecretHelper
import org.example.utils.HashHelper
import org.example.utils.PairingGroupHelper
import java.util.*


/**
 * John Bethencourt, Amit Sahai, Brent Waters.
 * Ciphertext-Policy Attribute-Based Encryption.
 * 2007 IEEE Symposium on Security and Privacy (SP ’07), May 2007, Berkeley, France.
 * 10.1109/SP.2007.11. hal-01788815￿
 */
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
    fun enc(m: Element, policy: String): Ciphertext {
        val tree = AccessTree.fromPolicyString(policy)
        val s = helper.randomZr()
        val C_Tilde = m.duplicate().mul(e_gg_alpha.powZn(s)).immutable
        val C = h.powZn(s).immutable

        tree.fillPolicy(s, mpk)
        return Ciphertext(C_Tilde, C, tree)
    }


    /**
     * @property msgdata GT element: m * e(g, g)^{alpha*s}
     * @property gs G1 element: h^s
     * @property tree accesstree with shares component
     */
    class Ciphertext(val msgdata: Element, val hs: Element, val tree: AccessTree)


    fun dec(cph: Ciphertext, sk: SecretKey): Element? {
        val attrs = sk.dMap!!.keys
        val tree = cph.tree.minLeavesToSatisfy(attrs)
        if (tree == null) {
            println("no satisfying leaves")
            return null
        }

        val acc = pg.zr.newOneElement()
        val e_gg_rs = pg.gt.newOneElement()
        decNode(tree.root!!, sk, acc, e_gg_rs)
        val result = cph.msgdata.duplicate()
        val tmp = helper.pairing(cph.hs, sk.D!!).div(e_gg_rs)
        result.div(tmp)
        return result
    }

    /**
     * 优化了decryptNode的实现，减少了配对次数，现在只需要在每个匹配的叶子节点进行配对
     * @param node 访问树的节点
     * @param acc ZR 传递的累积值
     * @param ans GT decryptNode(root)的结果
     */
    private fun decNode(node: AccessTreeNode, sk: SecretKey, acc: Element, ans: Element) {
        if (node.isLeaf) {

            val di = sk.dMap!![node.attr!!]!!.first
            val diPrime = sk.dMap!![node.attr!!]!!.second
            val cx = node.cx!!
            val cxPrime = node.cxPrime!!

            val result = pg.pairing(di, cx)
            result.div(helper.pairing(diPrime, cxPrime))
            result.powZn(acc)

            ans.mul(result)

            return

        }
        val secretHelper = SecretHelper(acc.field)
        val satisfied = node.minSatisIndex!!
        val xList = satisfied.map { i -> secretHelper.zr.newElement().set(i + 1).immutable }

        for (i in satisfied) {
            val child = node.children[i]
            val cof = secretHelper.lagrangeCoefficient(i, xList)
            val tmp = acc.duplicate().mul(cof)
            decNode(child, sk, tmp, ans)
        }
    }

    companion object {

        fun hashAttr(attr: Attribute, pg: Pairing): Element {
            return HashHelper(pg).hashFromStringToG1(attr.name)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val bswabe = BSWABE()
            bswabe.setup()
            val attrs = setOf(Attribute("a"), Attribute("b"), Attribute("c"))
            val sk = bswabe.keygen(attrs)
//            val m = bswabe.helper.randomG()
            val m = bswabe.helper.randomGT()
            val policy = "(A or B) and (C or D) ".lowercase(Locale.getDefault())
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

                var result = helper.pairing(di, cx)
                result = result.div(helper.pairing(diPrime, cxPrime))
                result
            } else {
                null
            }

        }
        // 已经预处理过满足属性集的节点
        // 需要确保minSatisIndex不为空
        val satisfied = node.minSatisIndex!!
//
        val secretHelper = SecretHelper(pg.zr)
        val xList = satisfied.map { i -> secretHelper.zr.newElement().set(i + 1).immutable }
        val result = pg.gt.newOneElement()
        for (i in satisfied) {
            val c = node.children[i]
            var fz = decryptNode(sk, c)!!
            // TODO: 修改拉格朗日系数计算方式, 不再使用map
            val cof = secretHelper.lagrangeCoefficient(i, xList)
            fz = fz.duplicate().powZn(cof)
            result.mul(fz)
        }
        return result
    }
}