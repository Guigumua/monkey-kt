package com.github.guigumua.parser

import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.FileOutputStream

class TestClassVisitor : ClassVisitor(Opcodes.ASM9){

    @Test
    fun test() {
        val classWriter = ClassWriter(Opcodes.ASM9)
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "TestClassVisitor", null, "java/lang/Object", null)
        val methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        methodVisitor.visitCode()
        classWriter.visitEnd()
        val bytes = classWriter.toByteArray()
        FileOutputStream("D:\\Documents\\IdeaProjects\\monkey-kt\\src\\test\\kotlin\\com\\github\\guigumua\\parser\\TestClassVisitor.class").use { it.write(bytes) }
    }
}

