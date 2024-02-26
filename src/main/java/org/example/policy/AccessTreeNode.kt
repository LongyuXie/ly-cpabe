package org.example.policy

import it.unisa.dia.gas.jpbc.Element
import org.example.secret.SecretHelper

/**
 *
 */
class AccessTreeNode {
    var threshold: Threshold? = null
    // 只考虑字符串属性
    var attr: Attribute? = null
    val children = mutableListOf<AccessTreeNode>()

    // 密文项直接存储在访问树中，因此解密时递归根节点即可
    var cx: Element? = null
    var cxPrime: Element? = null
    var minSatisIndex: IntArray? = null

    val isLeaf: Boolean
        get() = children.isEmpty() && attr != null

    // 对于一个阈值门访问控制树，如何生成密文？
    // 生成密文的一个重要过程就是对访问控制树的每个叶子节点生成秘密共享分量
    // 如果叶子节点的属性不同
    // 如果也自己节点的属性相同

    fun satisfy(attrs: Set<Attribute>): Boolean {
        if (isLeaf) {
            return attrs.contains(attr)
        }
        val k = this.threshold!!.k
        var cnt = 0
        for (c in children) {
            if (c.satisfy(attrs)) {
                cnt += 1
            }
        }
        return cnt >= k
    }


}