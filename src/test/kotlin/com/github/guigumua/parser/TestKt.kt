package com.github.guigumua.parser

import java.util.random.RandomGenerator
import kotlin.experimental.or

fun main() {
    // int(-1)
    RandomGenerator.getDefault().longs(1, Long.MAX_VALUE).limit(200).forEach {
        if (int(it).contentEquals(int0(it)).not()) {
            println(
                "${int(it).joinToString(",")} != ${int0(it).joinToString(",")}, \tbinary: ${
                    it.toString(2).padStart(64, '0')
                }, highest: ${it.takeHighestOneBit().countTrailingZeroBits()}"
            )
        }
    }
}

fun int(data: Long): ByteArray {
    return buildList {
        var num = data
        // 取出首位
        var b = (num and 0x3F).toByte()
        //正负
        if (data < 0) {
            b = b or 0x40
        }
        // 如果还有下一位
        if (num shr 6 != 0.toLong()) {
            b = b or 0x80.toByte()
        }
        add(b)
        num = num shr 6
        while (num != 0.toLong()) {
            b = (num and 0x7F).toByte()
            if (num shr 7 != 0.toLong()) {
                b = b or 0x80.toByte()
            }
            add(b)
            num = num shr 7
        }
    }.toByteArray()
}

fun int0(data: Long): ByteArray {
    // 6 7 7 7 7 7 7 7 7 7
    val length = (data.takeHighestOneBit().countTrailingZeroBits() + 1) / 7 + 1
    val bytes = ByteArray(length)
    bytes[0] = (data and 0x3F).toByte() or (if (data < 0) 0x40 else 0) or (if (data shr 6 != 0L) 0x80 else 0).toByte()
    var num = data shr 6
    for (i in 1..<length) {
        val b = (num and 0x7F).toByte()
        bytes[i] = if (num shr 7 != 0L) (b or 0x80.toByte()) else b
        num = num shr 7
    }
    return bytes
}

