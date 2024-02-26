package org.example.policy

import it.unisa.dia.gas.jpbc.Element

class Attribute(val name: String) {
//    var shareComponent: Element? = null

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is Attribute) {
            return name == other.name
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return name
    }
}