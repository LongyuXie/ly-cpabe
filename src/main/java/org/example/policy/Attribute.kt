package org.example.policy

import it.unisa.dia.gas.jpbc.Element

class Attribute(val name: String) {
//    var shareComponent: Element? = null
    private var id = 0

    fun withId(id: Int): Attribute {
        return Attribute(name).apply {
            this.id = id
        }
    }
    fun noId(): Attribute {
        return Attribute(name).apply {
            this.id = 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Attribute) {
            return name == other.name
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode() xor id
    }

    override fun toString(): String {
        return name + id
    }
}