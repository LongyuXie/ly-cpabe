package org.example.access

import org.example.policy.AccessTree
import org.example.policy.AccessTreeNode
import org.example.policy.Attribute
import org.example.policy.Threshold

class BinaryAccessTree {
    enum class NodeType {
        LEAF, AND, OR, EMPTY
    }

    class Node(val type: NodeType) {
        var left: Node? = null
        var right: Node? = null
        var attr: Attribute? = null
    }

    var root: Node = Node(NodeType.EMPTY)
        private set


    companion object {
        /**
         * convert a threshold access tree to a binary access tree
         */
        fun fromAccessTree(tree: AccessTree): BinaryAccessTree {
            val root = tree.root!!
            val newRoot = fromAccessTreeNode(root)

            return BinaryAccessTree().apply {
                this.root = newRoot
            }
        }

        private fun fromAccessTreeNode(atNode: AccessTreeNode): Node {
            if (atNode.isLeaf) {
                val node = Node(NodeType.LEAF)
                node.attr = atNode.attr
                return node
            }

            val type = if (atNode.threshold!! == Threshold.OR) {
                NodeType.OR
            } else if (atNode.threshold!! == Threshold.AND) {
                NodeType.AND
            } else {
                // TODO: trans (k, n) node to binary tree node
                throw IllegalArgumentException("Invalid threshold type")
            }

            return Node(type).apply {
                left = fromAccessTreeNode(atNode.children[0])
                right = fromAccessTreeNode(atNode.children[1])
            }
        }
    }
}