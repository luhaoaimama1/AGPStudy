package artifact.otherArtifact/*
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
import com.android.build.api.artifact.MultipleArtifact
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant

import org.gradle.api.DefaultTask
import org.gradle.api.file.*
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*

abstract class ArtifactsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            //ALL_CLASSES_JARS github上无范例 自己也搞不出来 现在看起来没啥大用就是R文件
            artifactALL_CLASSES_DIR_Append(project, variant)
//            artifactALL_CLASSES_DIRS_Transform(project, variant)
            // MultipleArtifact.MULTIDEX_KEEP_PROGUARD 有空再弄把 貌似是主Dex 可以提速把
        }
    }

    private fun artifactALL_CLASSES_DIR_Append(
        project: Project,
        variant: Variant
    ) {
        val provider = project.tasks.register(
            variant.name + "ALL_CLASSES_DIRS_APPEND_Task_Provider",
            ALL_CLASSES_DIRS_APPEND_Task::class.java
        ) {
            println("ALL_CLASSES_DIRS_APPEND_Task action")
        }

        variant.artifacts.use(provider)
            .wiredWith(ALL_CLASSES_DIRS_APPEND_Task::outputDir)
            .toAppendTo(MultipleArtifact.ALL_CLASSES_DIRS)
    }

    private fun artifactALL_CLASSES_DIRS_Transform(
        project: Project,
        variant: Variant
    ) {
        val all_classes_dirs_provider = project.tasks.register(
            variant.name + "ALL_CLASSES_DIRS_Provider",
            ALL_CLASSES_DIRS_Task::class.java
        ) {
            println("artifact-transform task register action")
        }
        variant.artifacts.use(all_classes_dirs_provider)
            .wiredWith(
                ALL_CLASSES_DIRS_Task::allClasses,
                ALL_CLASSES_DIRS_Task::output
            )
            .toTransform(MultipleArtifact.ALL_CLASSES_DIRS)
    }

    //内部生成的类
    abstract class ALL_CLASSES_DIRS_APPEND_Task : DefaultTask() {

        @get:OutputDirectory
        abstract val outputDir: DirectoryProperty

        @TaskAction
        fun taskAction() {
            //注意根本不需要赋值 仅仅确保这个文件夹内部 结构正确即可
            println("---> ALL_CLASSES_DIRS_APPEND_Task")
            val outPutPath = outputDir.get().asFile.absolutePath
            Runtime.getRuntime().exec("cp  /Users/fuzhipeng/AndroidStudioProjects/AGPStudy/Hello233.class ${outPutPath} ")
        }
    }

    //内部生成的类
    abstract class ALL_CLASSES_DIRS_Task : DefaultTask() {

        @get:InputFiles
        abstract val allClasses: ListProperty<Directory>

        @get:OutputFiles
        abstract val output: DirectoryProperty

        @TaskAction
        fun taskAction() { //不知道为啥 第二次就不行

            //注意根本不需要赋值 仅仅确保这个文件夹内部 结构正确即可
            println("---> ALL_CLASSES_DIRS_Task")
            output.get().asFile.deleteRecursively()

            val outPutPath = output.get().asFile.absolutePath
            allClasses.get().forEach {
                Runtime.getRuntime().exec("cp -r ${it.asFile.absolutePath} ${outPutPath} ")
            }
        }
    }
}

