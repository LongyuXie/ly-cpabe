package org.example.policy

import it.unisa.dia.gas.jpbc.Element
import org.example.abe.bswabe.BSWABE
import org.example.utils.SecretHelper
import org.example.utils.HashHelper

class AccessTree {

    var root: AccessTreeNode? = null
        private set
    private var policy: String? = null


    fun satisfy(attrs: Set<Attribute>): Boolean {
        if (root == null) return false
        return root!!.satisfy(attrs)
    }

    /**
     * 生成秘密共享分量密文项，并填充到访问树中
     */
    fun fillPolicy(secret: Element, pk: BSWABE.MasterPublicKey) {
        generateSharesComponent(secret, root!!, pk)
    }


    /**
     * @param attrs attributes set
     * @return new access tree with min leaves to dec
     */
    fun minLeavesToSatisfy(attrs: Set<Attribute>): AccessTree? {
        // 删除不需要使用的属性节点，标记保留的节点的索引
        if (minLeavesToSatisfy(attrs, root!!) == null) {
            return null
        }

        return this
    }


    private fun minLeavesToSatisfy(attrs: Set<Attribute>, node: AccessTreeNode): AccessTreeNode? {
        if (node.isLeaf) {
            return if (attrs.contains(node.attr)) {
                node
            } else {
                null
            }
        }

        // 预处理其中的k个节点的下标
        val minSatIndex = mutableListOf<Int>()
        val minChild = node.threshold!!.k
        for (i in node.children.indices) {
            val child = node.children[i]
            val minChildNode = minLeavesToSatisfy(attrs, child)
            if (minChildNode != null) {
                minSatIndex.add(i)
            }
            if (minSatIndex.size == minChild) {
                break
            }
        }
        if (minSatIndex.size < minChild) {
            return null
        }
        node.minSatisIndex = minSatIndex.toIntArray()
        return node
    }


    fun generateShares(secret: Element): Map<Attribute, Element> {
        val root = this.root!!
        val ans = mutableMapOf<Attribute, Element>()
        _generateShares(secret, root, ans)
        return ans
    }

    private fun _generateShares(secret: Element, node: AccessTreeNode, ans: MutableMap<Attribute, Element>) {
        if (node.isLeaf) {
            val idx = ans.size
            val attr = node.attr!!.withId(idx)
            ans[attr] = secret
        } else {
            val helper = SecretHelper(secret.field)
            val p = helper.randomPolynomial(node.threshold!!.k-1, constant = secret)
            for (i in node.children.indices) {
                val x = helper.zr.newElement().set(i+1).immutable
                val y = p.eval(x)
                _generateShares(y, node.children[i], ans)
            }
        }
    }

    private fun generateSharesComponent(secret: Element, node: AccessTreeNode, pk: BSWABE.MasterPublicKey) {
        val helper = SecretHelper(secret.field)
        val H = {
            attr: Attribute ->
            HashHelper(pk.pg).hashFromStringToG1(attr.name)
        }
        if (node.isLeaf) {
            node.cx = pk.g.powZn(secret).immutable
            node.cxPrime = H(node.attr!!).powZn(secret).immutable

        } else {
            val p = helper.randomPolynomial(node.threshold!!.k-1, constant = secret)

            for (i in node.children.indices) {
                val x = helper.zr.newElement().set(i+1).immutable
                val y = p.eval(x)
                generateSharesComponent(y, node.children[i], pk)
            }
        }

    }



    fun collectLeafs(): Set<Attribute> {
        val ans = mutableSetOf<Attribute>()
        _collectLeafs(root!!, ans)
        return ans
    }

    private fun _collectLeafs(node: AccessTreeNode, ans: MutableSet<Attribute>) {
        if (node.isLeaf) {
            val attr = node.attr!!.withId(ans.size)
            ans.add(attr)
        } else {
            for (c in node.children) {
                _collectLeafs(c, ans)
            }
        }
    }






    companion object {
        fun fromPolicyString(policy: String): AccessTree {
            return AccessTree().apply {
                root = PolicyParser.parserString(policy)
            }
        }
    }
}