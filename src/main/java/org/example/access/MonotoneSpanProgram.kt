package org.example.access

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Field
import it.unisa.dia.gas.jpbc.Vector
import org.ejml.simple.SimpleMatrix
import org.example.policy.AccessTree
import org.example.policy.Attribute
import org.example.utils.product
import org.w3c.dom.Attr
import java.math.BigInteger
import java.util.LinkedList
import java.util.Queue
import kotlin.math.max
import kotlin.math.min


/**
 * MonotoneSpanProgram
 */
class MonotoneSpanProgram() {

    var msp: List<IntArray> = listOf()
        private set
    var rho: List<Attribute> = listOf()
        private set

    fun computeShares(vec: List<Element>): List<Element> {
        require(msp.isNotEmpty() && rho.isNotEmpty())
        require(vec.size == msp[0].size)
        val zr = vec[0].field

        val elementMSP = msp.map { arr -> arr.map { zr.newElement().set(it) } }
        val ret = mutableListOf<Element>()
        for (rows in elementMSP) {
            val tmp = product(rows, vec)
            ret.add(tmp)
        }
        return ret
    }

    fun computeOmega(attrs: Set<Attribute>) {

    }


    companion object {
        fun createMSPM(tree: BinaryAccessTree) {

            // msp的参数
            var rows = 0
            var cols = 0
            var cnt = 1

            // msp矩阵以及其行映射
            val matrix = mutableMapOf<Attribute, MutableList<Int>>()
            val queue: Queue<Pair<BinaryAccessTree.Node, List<Int>>> = LinkedList()

            queue.offer(Pair(tree.root, listOf(1)))

            while (!queue.isEmpty()) {
                val (node, vec) = queue.poll()
                when (node.type) {
                    BinaryAccessTree.NodeType.LEAF -> {
                        rows += 1
                        cols = max(cols, vec.size)
                        // TODO: 重构Attribute类, 这里matrix不允许重复属性
                        matrix[node.attr!!] = vec.toMutableList()
                    }
                    BinaryAccessTree.NodeType.AND -> {
                        val newVec = vec.toMutableList()
                        while (newVec.size < cnt) {
                            newVec.add(0)
                        }

                        // (v|1)
                        val right = node.right!!
                        val rightVec = newVec.toMutableList().apply { add(1) }
                        queue.offer(Pair(right, rightVec))

                        // (0|-1)
                        val left = node.left!!
                        val leftVec = MutableList(newVec.size) { 0 }.apply { add(-1) }
                        queue.offer(Pair(left, leftVec))

                        cnt += 1
                    }
                    BinaryAccessTree.NodeType.OR -> {
                        // copy vec for left and right
                        val leftVec = vec.toList()
                        val rightVec = vec.toList()
                        queue.offer(Pair(node.left!!, leftVec))
                        queue.offer(Pair(node.right!!, rightVec))
                    }
                    else -> {
                        throw IllegalArgumentException("Invalid node type")
                    }
                }
            }

            for ((_, vec) in matrix) {
                while (vec.size < cols) {
                    vec.add(0)
                }
            }

            val msp = SimpleMatrix(rows, cols)
            val rhoMap = matrix.keys.toMutableList()
            for (i in 0 until rows) {
                val attr = rhoMap[i]
                val vec = matrix[attr]!!
                println(vec.joinToString(" ") + " ${attr.name}")
                val dv = vec.map { it.toDouble() }.toDoubleArray()
                msp.setRow(i, 0, *dv)
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val policy = "(a and b) or (c and d)"
            val tree = AccessTree.fromPolicyString(policy)
            val btree = BinaryAccessTree.fromAccessTree(tree)
            createMSPM(btree)
        }
    }


}