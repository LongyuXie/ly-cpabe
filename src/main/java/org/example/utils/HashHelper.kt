package org.example.utils

import it.unisa.dia.gas.jpbc.Element
import it.unisa.dia.gas.jpbc.Pairing
import java.security.MessageDigest


class HashHelper(val pg: Pairing) {

    companion object {
        private val sha512Instance: MessageDigest = MessageDigest.getInstance("SHA-512")
        fun sha512(bytes: ByteArray): ByteArray {
            return sha512Instance.digest(bytes)
        }
        fun sha512(s: String): ByteArray {
            return sha512(s.toByteArray())
        }
    }

    // string -> Zr
    fun hashFromStringToZp(str: String): Element {
        return hashFromBytesToZp(str.toByteArray())
    }
    // string -> G1
    fun hashFromStringToG1(str: String): Element {
        return hashFromBytesToG1(str.toByteArray())
    }

    // bytes -> Zr
    fun hashFromBytesToZp(bytes: ByteArray): Element {
        return pg.zr.newElement().setFromHash(bytes, 0, bytes.size).immutable
    }
    // bytes -> G1
    fun hashFromBytesToG1(bytes: ByteArray): Element {
        return pg.g1.newElement().setFromHash(bytes, 0, bytes.size).immutable
    }

    // G1 -> Zr
    fun hashFromG1ToZp(g1Element: Element): Element {
        val g1Bytes = g1Element.immutable.toCanonicalRepresentation()
        val zpBytes = Companion.sha512(g1Bytes)
        return hashFromBytesToZp(zpBytes)
    }

    // Gt -> Zr
    fun transformFromGtToZp(gtElement: Element): Element {
        val params = gtElement.toBigInteger()
        return pg.zr.newElement().set(params).immutable
    }

}