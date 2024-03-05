package org.example.abe.rouselakiswaters

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import org.example.policy.AccessTree
import org.example.policy.AccessTreeNode
import org.example.policy.Attribute
import org.example.utils.*
import java.util.*


/**
 * Rouselakis-Waters
 * New Constructions and Proof Methods for Large Universe Attribute-Based Encryption
 * https://eprint.iacr.org/2012/583
 */
class RWABE {


    class MasterPublicKey (
        val pg: Pairing,
        val g: Element,
        val u: Element,
        val w: Element,
        val v:Element,
        val h: Element,
        val e_gg_alpha: Element
    )

    lateinit var pk: MasterPublicKey
    lateinit var helper: PairingGroupHelper
    private lateinit var alpha: Element

    fun setup() {
        val pg = PairingGroupHelper.getSS512()!!
        helper = PairingGroupHelper(pg)
        val g = helper.randomG1()
        val u = helper.randomG1()
        val w = helper.randomG1()
        val v = helper.randomG1()
        val h = helper.randomG1()
        this.alpha = helper.randomZr()
        val e_gg_alpha = helper.pairing(g, g).powZn(alpha)
        pk = MasterPublicKey(pg, g, u, w, v, h, e_gg_alpha)
    }


    val attrMap = mutableMapOf<Attribute, Int>()


    fun regAttr(a: Attribute): Int {
        if (a in attrMap) {
            return attrMap[a]!!
        }
        val idx = attrMap.size + 10
        attrMap[a] = idx
        return idx
    }

    class SecretKey(
        val K0: Element,
        val K1: Element,
        val KtMap: Map<Attribute, Pair<Element, Element>>
    )

    fun keygen(attrs: Set<Attribute>): SecretKey {
        val r = helper.randomZr()
        val K0 = pk.g.powZn(alpha).mul(pk.w.powZn(r)).immutable
        val K1 = pk.g.powZn(r).immutable
        val KtMap = attrs.associate { attr ->
            val At = helper.setInt(attrMap[attr]!!)
            val rt = helper.randomZr()
            val Kt1 = pk.g.powZn(rt)
            val Kt2 = (pk.u.powZn(At).mul(pk.h)).powZn(rt).mul(pk.v.powZn(r.negate()))
            Pair(attr, Pair(Kt1, Kt2))
        }
        return SecretKey(K0, K1, KtMap)
    }

    /**
     * 同一个策略中可能出现相同的属性，因此需要对属性进行编号
     */
    class Ciphertext(
        val C: Element,
        val C0: Element,
        val CtMap: Map<Attribute, Triple<Element, Element, Element>>,
        val policy: String
    )


    /**
     * 访问矩阵与访问树的等价关系
     * 访问矩阵的行对应访问树的叶子节点
     */
    fun encrypt(msg: Element, policy: String): Ciphertext {

        val s = helper.randomZr()
        val C = msg.duplicate().mul(pk.e_gg_alpha.powZn(s))
        val C0 = pk.g.powZn(s)

        val tree = AccessTree.fromPolicyString(policy)

        val shares = tree.generateShares(s)

        val CtMap = shares.entries.associate{
            val tt = helper.randomZr()
            val ct1 = pk.w.powZn(it.value).mul(pk.v.powZn(tt))
            val ct2 = pk.u.powZn(helper.setInt(attrMap[it.key.noId()]!!)).mul(pk.h).powZn(tt.negate())
            val ct3 = pk.g.powZn(tt)
            Pair(it.key, Triple(ct1, ct2, ct3))
        }
        // 从树中获取全部的叶子节点，并且按照bsw中的处理方式计算秘密分量
        return Ciphertext(C, C0, CtMap, policy)
    }


    fun CsEncrypt(policy: String): Ciphertext {
        val tree = AccessTree.fromPolicyString(policy)
        val s = helper.randomZr()
        val C = pk.e_gg_alpha.powZn(s)
        val C0 = pk.g.powZn(s)
        val shares = tree.generateShares(s)
        val CtMap = shares.entries.associate {
            val tt = helper.randomZr()
            val ct1 = pk.w.powZn(it.value).mul(pk.v.powZn(tt))
            val ct2 = pk.u.powZn(helper.setInt(attrMap[it.key.noId()]!!)).mul(pk.h).powZn(tt.negate())
            val ct3 = pk.g.powZn(tt)
            Pair(it.key, Triple(ct1, ct2, ct3))
        }
        return Ciphertext(C, C0, CtMap, policy)
    }

    fun DoEncrypt(message: Element, cph: Ciphertext): Ciphertext {
        val c = helper.randomZr()
        val C = message.duplicate().mul(cph.C.duplicate().powZn(c))
        val Co = cph.C0.duplicate().powZn(c)
        val CtMap = cph.CtMap.entries.associate {
            val ct1 = it.value.first.duplicate().powZn(c)
            val ct2 = it.value.second.duplicate().powZn(c)
            val ct3 = it.value.third.duplicate().powZn(c)
            Pair(it.key, Triple(ct1, ct2, ct3))
        }
        return Ciphertext(C, Co, CtMap, cph.policy)
    }


    fun decrypt(cph: Ciphertext, sk: SecretKey): Element? {
        val tree = cph.policy.let { AccessTree.fromPolicyString(it) }


        val checked = tree.satisfy(sk.KtMap.keys)

        if (!checked) {
            println("secret key does not satisfy")
            return null
        }

        val coefficients = generateCoefficients(sk.KtMap.keys, tree)

        val keys = coefficients.keys

        val P = helper.pg.gt.newOneElement()

        keys.forEach {
            val Ct = cph.CtMap[it]!!
            val Kt = sk.KtMap[it.noId()]!!
            val ct1 = Ct.first
            val ct2 = Ct.second
            val ct3 = Ct.third
            val kt1 = sk.K1
            val kt2 = Kt.first
            val kt3 = Kt.second

            val t1 = helper.pairing(ct1, kt1)
            val t2 = helper.pairing(ct2, kt2)
            val t3 = helper.pairing(ct3, kt3)

            val tmp = t1.mul(t2).mul(t3).powZn(coefficients[it])
            P.mul(tmp)
        }


        val B = helper.pairing(cph.C0, sk.K0).duplicate()
        B.div(P)

        return cph.C.div(B)
    }

    fun generateCoefficients(attrs: Set<Attribute>, tree: AccessTree): MutableMap<Attribute, Element> {
        val root = tree.root!!
        val acc = pk.pg.zr.newElement().setToOne().immutable
        val result = mutableMapOf<Attribute, Element>()
        _genCoeff(attrs, root, acc, result)
        return result
    }

    private fun _genCoeff(attrs: Set<Attribute>, node: AccessTreeNode, acc: Element, result: MutableMap<Attribute, Element>) {
        if (node.isLeaf) {
            if (attrs.contains(node.attr)) {
                val idx = result.size
                val newAttr = node.attr!!.withId(idx)
                result[newAttr] = acc
            }
        } else {
            val secretHelper = SecretHelper(acc.field)
            val satisfied = node.children.mapIndexedNotNull { index, attribute ->
                if (attribute.satifiable) index else null
            }
            val xList = satisfied.map { helper.setInt(it+1) }

            for (i in satisfied) {
                val child = node.children[i]
                val cof = secretHelper.lagrangeCoefficient(i, xList)
                val tmp = acc.duplicate().mul(cof)
                _genCoeff(attrs, child, tmp, result)
            }
            

        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val ret = benchmark()
            writeToCSV("RWABE.csv", ret, listOf("attrs", "rwabe", "myabe"))
        }

        @JvmStatic
        fun benchmark(): MutableList<MutableList<Int>> {
            val rec = mutableListOf<MutableList<Int>>()
            for (i in 5..20) {
                val r = mutableListOf<Int>()
                r.add(i)
                val abe = RWABE()
                abe.setup()
                val policy = randomPolicy(i)
                extractAttrs(policy).forEach {
                    val a = Attribute(it)
                    abe.regAttr(a)
                }
                val m = abe.helper.randomGT()

                var cph: Ciphertext? = null

                val t1 = execTime { cph = abe.encrypt(m, policy) }
                val t4 = execTime { cph = cph?.let { abe.DoEncrypt(m, it) } }
                r.add(t1)
                r.add(t4)
                rec.add(r)
            }
            rec.forEach(::println)
            return rec
        }
    }

}