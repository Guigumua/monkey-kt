package com.github.guigumua.parser

import java.io.OutputStream

internal interface ByteCodes {
    companion object {
        val typecodeNames = arrayOf(
            "int",
            "long",
            "float",
            "double",
            "object",
            "byte",
            "char",
            "short",
            "void",
            "oops"
        )

        /** Byte code instruction codes.
         */
        const val illegal = -1
        const val nop = 0
        const val aconst_null = 1
        const val iconst_m1 = 2
        const val iconst_0 = 3
        const val iconst_1 = 4
        const val iconst_2 = 5
        const val iconst_3 = 6
        const val iconst_4 = 7
        const val iconst_5 = 8
        const val lconst_0 = 9
        const val lconst_1 = 10
        const val fconst_0 = 11
        const val fconst_1 = 12
        const val fconst_2 = 13
        const val dconst_0 = 14
        const val dconst_1 = 15
        const val bipush = 16
        const val sipush = 17
        const val ldc1 = 18
        const val ldc2 = 19
        const val ldc2w = 20
        const val iload = 21
        const val lload = 22
        const val fload = 23
        const val dload = 24
        const val aload = 25
        const val iload_0 = 26
        const val iload_1 = 27
        const val iload_2 = 28
        const val iload_3 = 29
        const val lload_0 = 30
        const val lload_1 = 31
        const val lload_2 = 32
        const val lload_3 = 33
        const val fload_0 = 34
        const val fload_1 = 35
        const val fload_2 = 36
        const val fload_3 = 37
        const val dload_0 = 38
        const val dload_1 = 39
        const val dload_2 = 40
        const val dload_3 = 41
        const val aload_0 = 42
        const val aload_1 = 43
        const val aload_2 = 44
        const val aload_3 = 45
        const val iaload = 46
        const val laload = 47
        const val faload = 48
        const val daload = 49
        const val aaload = 50
        const val baload = 51
        const val caload = 52
        const val saload = 53
        const val istore = 54
        const val lstore = 55
        const val fstore = 56
        const val dstore = 57
        const val astore = 58
        const val istore_0 = 59
        const val istore_1 = 60
        const val istore_2 = 61
        const val istore_3 = 62
        const val lstore_0 = 63
        const val lstore_1 = 64
        const val lstore_2 = 65
        const val lstore_3 = 66
        const val fstore_0 = 67
        const val fstore_1 = 68
        const val fstore_2 = 69
        const val fstore_3 = 70
        const val dstore_0 = 71
        const val dstore_1 = 72
        const val dstore_2 = 73
        const val dstore_3 = 74
        const val astore_0 = 75
        const val astore_1 = 76
        const val astore_2 = 77
        const val astore_3 = 78
        const val iastore = 79
        const val lastore = 80
        const val fastore = 81
        const val dastore = 82
        const val aastore = 83
        const val bastore = 84
        const val castore = 85
        const val sastore = 86
        const val pop = 87
        const val pop2 = 88
        const val dup = 89
        const val dup_x1 = 90
        const val dup_x2 = 91
        const val dup2 = 92
        const val dup2_x1 = 93
        const val dup2_x2 = 94
        const val swap = 95
        const val iadd = 96
        const val ladd = 97
        const val fadd = 98
        const val dadd = 99
        const val isub = 100
        const val lsub = 101
        const val fsub = 102
        const val dsub = 103
        const val imul = 104
        const val lmul = 105
        const val fmul = 106
        const val dmul = 107
        const val idiv = 108
        const val ldiv = 109
        const val fdiv = 110
        const val ddiv = 111
        const val imod = 112
        const val lmod = 113
        const val fmod = 114
        const val dmod = 115
        const val ineg = 116
        const val lneg = 117
        const val fneg = 118
        const val dneg = 119
        const val ishl = 120
        const val lshl = 121
        const val ishr = 122
        const val lshr = 123
        const val iushr = 124
        const val lushr = 125
        const val iand = 126
        const val land = 127
        const val ior = 128
        const val lor = 129
        const val ixor = 130
        const val lxor = 131
        const val iinc = 132
        const val i2l = 133
        const val i2f = 134
        const val i2d = 135
        const val l2i = 136
        const val l2f = 137
        const val l2d = 138
        const val f2i = 139
        const val f2l = 140
        const val f2d = 141
        const val d2i = 142
        const val d2l = 143
        const val d2f = 144
        const val int2byte = 145
        const val int2char = 146
        const val int2short = 147
        const val lcmp = 148
        const val fcmpl = 149
        const val fcmpg = 150
        const val dcmpl = 151
        const val dcmpg = 152
        const val ifeq = 153
        const val ifne = 154
        const val iflt = 155
        const val ifge = 156
        const val ifgt = 157
        const val ifle = 158
        const val if_icmpeq = 159
        const val if_icmpne = 160
        const val if_icmplt = 161
        const val if_icmpge = 162
        const val if_icmpgt = 163
        const val if_icmple = 164
        const val if_acmpeq = 165
        const val if_acmpne = 166
        const val goto_ = 167
        const val jsr = 168
        const val ret = 169
        const val tableswitch = 170
        const val lookupswitch = 171
        const val ireturn = 172
        const val lreturn = 173
        const val freturn = 174
        const val dreturn = 175
        const val areturn = 176
        const val return_ = 177
        const val getstatic = 178
        const val putstatic = 179
        const val getfield = 180
        const val putfield = 181
        const val invokevirtual = 182
        const val invokespecial = 183
        const val invokestatic = 184
        const val invokeinterface = 185
        const val invokedynamic = 186
        const val new_ = 187
        const val newarray = 188
        const val anewarray = 189
        const val arraylength = 190
        const val athrow = 191
        const val checkcast = 192
        const val instanceof_ = 193
        const val monitorenter = 194
        const val monitorexit = 195
        const val wide = 196
        const val multianewarray = 197
        const val if_acmp_null = 198
        const val if_acmp_nonnull = 199
        const val goto_w = 200
        const val jsr_w = 201
        const val breakpoint = 202
        const val ByteCodeCount = 203

        val mnem: Array<String> = arrayOf(
            "nop",
            "aconst_null",
            "iconst_m1",
            "iconst_0",
            "iconst_1",
            "iconst_2",
            "iconst_3",
            "iconst_4",
            "iconst_5",
            "lconst_0",
            "lconst_1",
            "fconst_0",
            "fconst_1",
            "fconst_2",
            "dconst_0",
            "dconst_1",
            "bipush",
            "sipush",
            "ldc1",
            "ldc2",
            "ldc2w",
            "iload",
            "lload",
            "fload",
            "dload",
            "aload",
            "iload_0",
            "lload_0",
            "fload_0",
            "dload_0",
            "aload_0",
            "iload_1",
            "lload_1",
            "fload_1",
            "dload_1",
            "aload_1",
            "iload_2",
            "lload_2",
            "fload_2",
            "dload_2",
            "aload_2",
            "iload_3",
            "lload_3",
            "fload_3",
            "dload_3",
            "aload_3",
            "iaload",
            "laload",
            "faload",
            "daload",
            "aaload",
            "baload",
            "caload",
            "saload",
            "istore",
            "lstore",
            "fstore",
            "dstore",
            "astore",
            "istore_0",
            "lstore_0",
            "fstore_0",
            "dstore_0",
            "astore_0",
            "istore_1",
            "lstore_1",
            "fstore_1",
            "dstore_1",
            "astore_1",
            "istore_2",
            "lstore_2",
            "fstore_2",
            "dstore_2",
            "astore_2",
            "istore_3",
            "lstore_3",
            "fstore_3",
            "dstore_3",
            "astore_3",
            "iastore",
            "lastore",
            "fastore",
            "dastore",
            "aastore",
            "bastore",
            "castore",
            "sastore",
            "pop",
            "pop2",
            "dup",
            "dup_x1",
            "dup_x2",
            "dup2",
            "dup2_x1",
            "dup2_x2",
            "swap",
            "iadd",
            "ladd",
            "fadd",
            "dadd",
            "isub",
            "lsub",
            "fsub",
            "dsub",
            "imul",
            "lmul",
            "fmul",
            "dmul",
            "idiv",
            "ldiv",
            "fdiv",
            "ddiv",
            "imod",
            "lmod",
            "fmod",
            "dmod",
            "ineg",
            "lneg",
            "fneg",
            "dneg",
            "ishl",
            "lshl",
            "ishr",
            "lshr",
            "iushr",
            "lushr",
            "iand",
            "land",
            "ior",
            "lor",
            "ixor",
            "lxor",
            "iinc",
            "i2l",
            "i2f",
            "i2d",
            "l2i",
            "l2f",
            "l2d",
            "f2i",
            "f2l",
            "f2d",
            "d2i",
            "d2l",
            "d2f",
            "int2byte",
            "int2char",
            "int2short",
            "lcmp",
            "fcmpl",
            "fcmpg",
            "dcmpl",
            "dcmpg",
            "ifeq",
            "ifne",
            "iflt",
            "ifge",
            "ifgt",
            "ifle",
            "if_icmpeq",
            "if_icmpne",
            "if_icmplt",
            "if_icmpge",
            "if_icmpgt",
            "if_icmple",
            "if_acmpeq",
            "if_acmpne",
            "goto_",
            "jsr",
            "ret",
            "tableswitch",
            "lookupswitch",
            "ireturn",
            "lreturn",
            "freturn",
            "dreturn",
            "areturn",
            "return_",
            "getstatic",
            "putstatic",
            "getfield",
            "putfield",
            "invokevirtual",
            "invokespecial",
            "invokestatic",
            "invokeinterface",
            "invokedynamic",
            "new_",
            "newarray",
            "anewarray",
            "arraylength",
            "athrow",
            "checkcast",
            "instanceof_",
            "monitorenter",
            "monitorexit",
            "wide",
            "multianewarray",
            "if_acmp_null",
            "if_acmp_nonnull",
            "goto_w",
            "jsr_w",
            "breakpoint",
        )
    }
}


class Instructions {
    private val instructions: MutableList<Instruction> = mutableListOf()
    private val labels: MutableMap<Label, Int> = mutableMapOf()
    private var codeLength = 0
    val length get() = codeLength

    fun add(instruction: Instruction) {
        instructions.add(instruction)
        codeLength += instruction.length
    }

    fun writeTo(outputStream: OutputStream) {
        for (instruction in instructions) {
            outputStream.write(instruction.code)
            outputStream.write(instruction.operands)
        }
    }

    val size: Int get() = instructions.size

    fun setLabel(label: Label) {
        labels[label] = length
        label.position = length
    }
}

class Constants {
    private val constantsIndexMap: MutableMap<Any, Int> = mutableMapOf()
    private val constants: MutableList<Constant> = mutableListOf()

    fun addIntConstant(data: Int): Int {
        val constant = IntConstant(data)
        return addConstant(constant)
    }

    fun addFloatConstant(data: Float): Int {
        val constant = FloatConstant(data)
        return addConstant(constant)
    }

    fun addStringConstant(data: String): Int {
        val utf8Index = addUtf8Constant(data)
        val constant = StringConstant(utf8Index)
        return addConstant(constant)
    }

    fun addClass(name: String): Int {
        val utf8Index = addUtf8Constant(name)
        val constant = ClassConstant(utf8Index)
        return addConstant(constant)
    }

    fun addMethodRef(className: String, name: String, type: String): Int {
        val classIndex = addClass(className)
        val nameAndTypeIndex = addNameAndType(name, type)
        val constant = MethodRefConstant(classIndex, nameAndTypeIndex)
        return addConstant(constant)
    }

    fun addFieldRef(className: String, name: String, type: String): Int {
        val classIndex = addClass(className)
        val nameAndTypeIndex = addNameAndType(name, type)
        val constant = FieldRefConstant(classIndex, nameAndTypeIndex)
        return addConstant(constant)
    }

    fun addInterfaceMethodRef(className: String, name: String, type: String): Int {
        val classIndex = addClass(className)
        val nameAndTypeIndex = addNameAndType(name, type)
        val constant = InterfaceMethodRefConstant(classIndex, nameAndTypeIndex)
        return addConstant(constant)
    }

    fun addNameAndType(name: String, type: String): Int {
        val utf8NameIndex = addUtf8Constant(name)
        val utf8TypeIndex = addUtf8Constant(type)
        val constant = NameAndTypeConstant(utf8NameIndex, utf8TypeIndex)
        return addConstant(constant)
    }

    fun addUtf8Constant(data: String): Int {
        val constant = Utf8Constant(data)
        return addConstant(constant)
    }


    private fun addConstant(constant: Constant): Int {
        if (constantsIndexMap.containsKey(constant.key)) {
            return constantsIndexMap[constant.key]!!
        }
        constants.add(constant)
        val index = constants.size
        constantsIndexMap[constant.key] = index
        return index
    }

    fun size(): Int {
        return constants.size
    }

    fun writeTo(outputStream: OutputStream) {
        outputStream.writeTwo(constants.size + 1)
        for (constant in constants) {
            constant.writeTo(outputStream)
        }
    }
}

interface Constant {
    val tag: Int
    val key: Any

    companion object {
        const val CONSTANT_Utf8 = 1
        const val CONSTANT_Unicode = 2
        const val CONSTANT_Integer = 3
        const val CONSTANT_Float = 4
        const val CONSTANT_Long = 5
        const val CONSTANT_Double = 6
        const val CONSTANT_Class = 7
        const val CONSTANT_String = 8
        const val CONSTANT_Fieldref = 9
        const val CONSTANT_Methodref = 10
        const val CONSTANT_InterfaceMethodref = 11
        const val CONSTANT_NameandType = 12
        const val CONSTANT_MethodHandle = 15
        const val CONSTANT_MethodType = 16
        const val CONSTANT_Dynamic = 17
        const val CONSTANT_InvokeDynamic = 18
        const val CONSTANT_Module = 19
        const val CONSTANT_Package = 20
    }

    fun writeTo(outputStream: OutputStream)
}

class ClassConstant(val nameIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Class

    override
    val key: Any get() = this
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(nameIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassConstant) return false

        if (nameIndex != other.nameIndex) return false

        return true
    }

    override fun hashCode(): Int {
        return nameIndex
    }

    override fun toString(): String {
        return "Class($nameIndex)"
    }

}

class MethodRefConstant(val classIndex: Int, val nameAndTypeIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Methodref

    override
    val key: Any get() = this
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(classIndex)
        outputStream.writeTwo(nameAndTypeIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MethodRefConstant) return false

        if (classIndex != other.classIndex) return false
        if (nameAndTypeIndex != other.nameAndTypeIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = classIndex
        result = 31 * result + nameAndTypeIndex
        return result
    }

    override fun toString(): String {
        return "MethodRefConstant($classIndex, $nameAndTypeIndex)"
    }

}

class FieldRefConstant(val classIndex: Int, val nameAndTypeIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Fieldref

    override
    val key: Any get() = this
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(classIndex)
        outputStream.writeTwo(nameAndTypeIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldRefConstant) return false

        if (classIndex != other.classIndex) return false
        if (nameAndTypeIndex != other.nameAndTypeIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = classIndex
        result = 31 * result + nameAndTypeIndex
        return result
    }

    override fun toString(): String {
        return "FieldRefConstant($classIndex, $nameAndTypeIndex)"
    }


}

class InterfaceMethodRefConstant(val classIndex: Int, val nameAndTypeIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_InterfaceMethodref

    override
    val key: Any get() = this
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(classIndex)
        outputStream.writeTwo(nameAndTypeIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InterfaceMethodRefConstant) return false

        if (classIndex != other.classIndex) return false
        if (nameAndTypeIndex != other.nameAndTypeIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = classIndex
        result = 31 * result + nameAndTypeIndex
        return result
    }

    override fun toString(): String {
        return "InterfaceMethodRefConstant($classIndex, $nameAndTypeIndex)"
    }


}

class Utf8Constant(val data: String) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Utf8

    override
    val key: Any get() = data
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(data.length)
        outputStream.write(data.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Utf8Constant

        return data == other.data
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return data
    }


}

class IntConstant(val data: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Integer

    override
    val key: Any get() = data
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeFour(data)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntConstant

        return data == other.data
    }

    override fun hashCode(): Int {
        return data
    }

    override fun toString(): String {
        return data.toString()
    }


}

class FloatConstant(val data: Float) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_Float

    override
    val key: Any get() = data
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeFour(data.toBits())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatConstant

        return data == other.data
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String {
        return data.toString()
    }
}

class StringConstant(val stringIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_String

    override
    val key: Any get() = stringIndex
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(stringIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringConstant

        return stringIndex == other.stringIndex
    }

    override fun hashCode(): Int {
        return stringIndex
    }

    override fun toString(): String {
        return "StringConstant($stringIndex)"
    }

}

class NameAndTypeConstant(val nameIndex: Int, val descriptorIndex: Int) : Constant {
    override val tag: Int
        get() = Constant.CONSTANT_NameandType

    override
    val key: Any get() = Pair(nameIndex, descriptorIndex)
    override fun writeTo(outputStream: OutputStream) {
        outputStream.write(tag)
        outputStream.writeTwo(nameIndex)
        outputStream.writeTwo(descriptorIndex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NameAndTypeConstant

        if (nameIndex != other.nameIndex) return false
        if (descriptorIndex != other.descriptorIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameIndex
        result = 31 * result + descriptorIndex
        return result
    }

    override fun toString(): String {
        return "NameAndTypeConstant($nameIndex, $descriptorIndex)"
    }


}

interface Instruction {

    val length: Int

    val code: Int

    val operands: ByteArray
}

class Label(private val base: Label? = null) {
    companion object {
        var labelCount = 0
    }

    private val id = labelCount++
    internal var position: Int = 0
    val offset get() = position - (base?.position ?: 0)

    override fun toString(): String {
        return "Label($id)"
    }
}

class LabelInstruction(override val code: Int, private val label: Label) : Instruction {
    override val operands: ByteArray
        get() = byteArrayOf((label.offset shr 8 and 0xFF).toByte(), (label.offset and 0xFF).toByte())
    override val length: Int
        get() = 1 + operands.size
}

class FixedInstruction(override val code: Int, override val operands: ByteArray) : Instruction {
    override fun toString(): String {
        return "${ByteCodes.mnem[code]} $operands"
    }

    override val length
        get() =
            operands.size + 1
}

