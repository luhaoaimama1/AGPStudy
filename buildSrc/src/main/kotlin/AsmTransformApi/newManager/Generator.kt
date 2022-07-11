package AsmTransformApi.newManager

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import java.io.*

// TODO:  由于非空问题 总与解决  现在就剩多次编译问题
class Generator : Serializable {
    var ManagerName = "AsmGenerate"

    fun collection(api: Int, name: String?, outFile: File) {
        name?.let {
            println("---> AsmTransformPlugin")
            println("Generator__ new =>$it")
            val finalFile = File(outFile, ManagerName + ".class")
            if (!finalFile.exists()) { //添加类
                addClass(it, finalFile)
            } else { //添加方法
                addMethod(it, finalFile, api)
            }
        }
    }

    private fun addMethod(name: String, finalFile: File, api: Int) {
        println("Generator__ insert =>$name")
        var classReader: ClassReader? = null
        try {
            classReader = ClassReader(FileInputStream(finalFile))
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES)
            //（3）串连ClassVisitor
            val visitor = MethodEnterClassVisitor(api, classWriter, name)

            //（4）结合ClassReader和ClassVisitor
            val parsingOptions = ClassReader.SKIP_DEBUG or ClassReader.SKIP_FRAMES
            classReader.accept(visitor, parsingOptions)

            val bytes = classWriter.toByteArray()
            val fileOutputStream = FileOutputStream(finalFile)
            fileOutputStream.write(bytes)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun addClass(name: String?, finalFile: File) {
        //添加类
        val classWriter = ClassWriter(0)
        var fieldVisitor: FieldVisitor
        var methodVisitor: MethodVisitor
        var annotationVisitor0: AnnotationVisitor

        classWriter.visit(
            V1_7,
            ACC_PUBLIC or ACC_SUPER,
            "hook/asm/cls/AsmGenerate",
            null,
            "java/lang/Object",
            null
        )

        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        methodVisitor.visitCode()
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(
            INVOKESPECIAL,
            "java/lang/Object",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()

        methodVisitor = classWriter.visitMethod(0, "log", "()V", null, null)
        methodVisitor.visitCode()
        methodVisitor.visitFieldInsn(
            GETSTATIC,
            "java/lang/System",
            "out",
            "Ljava/io/PrintStream;"
        )
        methodVisitor.visitLdcInsn("class:$name")
        methodVisitor.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )
        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitMaxs(2, 1)
        methodVisitor.visitEnd()

        classWriter.visitEnd()
        val bytes = classWriter.toByteArray()

        try {
            val fileOutputStream = FileOutputStream(finalFile)
            fileOutputStream.write(bytes)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

class MethodEnterClassVisitor(api: Int, classVisitor: ClassVisitor, private val clasName: String) :
    ClassVisitor(api, classVisitor) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        var mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        //它说明如何生成一个静态块 static { ... }，也就是用<clinit>方法(用于 CLass INITializer)
        if (mv != null && "<init>" != name && "<clinit>" != name) {
            mv = MethodEnterMethodVisitor(api, mv, clasName)
        }
        return mv
    }

    class MethodEnterMethodVisitor(
        api: Int,
        methodVisitor: MethodVisitor,
        private val clasName: String
    ) :
        MethodVisitor(api, methodVisitor) {
        override fun visitCode() {
            super.visitCode()
        }

        override fun visitInsn(opcode: Int) {
            if (opcode == Opcodes.ATHROW || opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                super.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;"
                )
                super.visitLdcInsn("class:$clasName")
                super.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V",
                    false
                )
            }
            super.visitInsn(opcode)
        } //        那就是得知道我们会在 onMethodEnter 中存一个方法开始时间，再在 onMethodExit 中存一个方法结束时间
    }
}