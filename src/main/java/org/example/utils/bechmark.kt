package org.example.utils

import java.io.File


fun writeToCSV(fileName: String,data: List<List<Int>>, title: List<String>? = null) {
    val file = File(fileName)
    val writer = file.bufferedWriter()
    if (title != null) {
        writer.write(title.joinToString(","))
        writer.newLine()
    }
    for (row in data) {
        writer.write(row.joinToString(","))
        writer.newLine()
    }
    writer.close()
}

fun execTime(block: () -> Unit): Int {
    val start = System.currentTimeMillis()
    block()
    val end = System.currentTimeMillis()
    return (end - start).toInt()
}


fun connectWithAnd(attrs: List<String>): String {
    return attrs.joinToString(" AND ")
}

/**
 * 生成随机策略
 * @param n 属性个数
 */
fun randomPolicy(n: Int): String {
    val attrs = mutableListOf<String>()
    for (i in 0 until n) {
        attrs.add("attr$i")
    }
    return attrs.joinToString(" AND ")
}

fun extractAttrs(policy: String): List<String> {
    return policy.split(" AND ")
}

fun main() {
    randomPolicy(10)
}