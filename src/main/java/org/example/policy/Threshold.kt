package org.example.policy

class Threshold(val k: Int, val n: Int) {
    init {
        assert(k <= n)
    }

    companion object {
        val ONE = Threshold(1, 1)
        val AND = Threshold(2, 2)
        val OR = Threshold(1, 2)
    }

    override fun toString(): String {
        return "($k, $n)"
    }
}