package com.github.guigumua.parser

import java.io.OutputStream

fun OutputStream.writeTwo(int: Int) {
    this.write(int shr 8 and 0xFF)
    this.write(int and 0xFF)
}

fun OutputStream.writeFour(int: Int) {
    this.write(int shr 24 and 0xFF)
    this.write(int shr 16 and 0xFF)
    this.write(int shr 8 and 0xFF)
    this.write(int and 0xFF)
}
