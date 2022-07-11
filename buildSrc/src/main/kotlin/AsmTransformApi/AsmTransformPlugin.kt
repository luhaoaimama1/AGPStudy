/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package AsmTransformApi

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes.*

//./gradlew transformDebugClassesWithAsm  保证过程被执行
abstract class AsmTransformPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            variant.transformClassesWith(
                ExampleClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.writeToStdout.set(true)//给visitor传递参数
            }
            variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }

        project.afterEvaluate {
            println("AsmTransformPlugin  afterEvaluate")
        }


    }
    //visitor 交流的参数
    interface ExampleParams : InstrumentationParameters {
        @get:Input
        val writeToStdout: Property<Boolean>
    }

    abstract class ExampleClassVisitorFactory :
        AsmClassVisitorFactory<ExampleParams> {

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor
        ): ClassVisitor {
            //instrumentationContext.apiVersion.get()
            val value = object : ClassVisitor(ASM5, nextClassVisitor) {
                override fun visitSource(source: String?, debug: String?) {
                    super.visitSource(source, debug)
                }
                override fun visitEnd() {
                    println("AsmTransformPlugin visitEnd")
                    val methodVisitor =
                        super.visitMethod(ACC_PUBLIC or ACC_FINAL, "abc", "()V", null, null)
                    methodVisitor.visitCode()
                    methodVisitor.visitLdcInsn("insert ...")
                    methodVisitor.visitVarInsn(ASTORE, 1)
                    methodVisitor.visitInsn(ICONST_0)
                    methodVisitor.visitVarInsn(ISTORE, 2)
                    methodVisitor.visitFieldInsn(
                        GETSTATIC,
                        "java/lang/System",
                        "out",
                        "Ljava/io/PrintStream;"
                    )
                    methodVisitor.visitVarInsn(ALOAD, 1)
                    methodVisitor.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/Object;)V",
                        false
                    )
                    methodVisitor.visitInsn(RETURN)
                    methodVisitor.visitMaxs(2, 3)
                    methodVisitor.visitEnd()
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
}