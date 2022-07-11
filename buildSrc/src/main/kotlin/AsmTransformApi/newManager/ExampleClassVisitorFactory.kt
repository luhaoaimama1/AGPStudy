package AsmTransformApi.newManager

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor

abstract class ExampleClassVisitorFactory :
    AsmClassVisitorFactory<BaseParams> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val additionalClassesDir = parameters.get().additionalClassesDir.get()
        val generator = parameters.get().generator.get()
        val api = instrumentationContext.apiVersion.get()
        val value = object : ClassVisitor(api, nextClassVisitor) {
            override fun visit(
                version: Int,
                access: Int,
                name: String?,
                signature: String?,
                superName: String?,
                interfaces: Array<out String>?
            ) {
                super.visit(version, access, name, signature, superName, interfaces)
                generator.collection(api, name, additionalClassesDir)
            }

            override fun visitEnd() {
                println("AsmTransformPlugin visitEnd")
                super.visitEnd()
            }
        }
        return value
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        println("AsmTransformPlugin isInstrumentable:${classData.className}")
        return classData.className.startsWith("com.example")
    }
}