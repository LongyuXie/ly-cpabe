package org.example.policy

/**
 * 一个内联结构体，用于保存语义变量
 */
class ParserVal {
    @JvmField
    var ival = 0
    @JvmField
    var sval: String? = null
    @JvmField
    var obj: Any? = null

    constructor(ival: Int) {
        this.ival = ival
    }

    constructor(obj: Any?) {
        this.obj = obj
    }

    constructor(sval: String?) {
        this.sval = sval
    }
}