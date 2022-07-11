package property/*
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
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.plugins.ExtensionAware

abstract class PropertyPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val android = project.extensions.getByType(ApplicationExtension::class.java)
        android.buildTypes.forEach {
            (it as ExtensionAware).extensions.add(
                "exampleDsl",
                BuildTypeExtension::class.java)
        }

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants {
            //解析 DSL中的属性 固定写法 因为他就是这么封装的框架 随着他写就可以了
            // get the associated DSL BuildType element from the variant name
            val buildTypeDsl = android.buildTypes.getByName(it.name)
            // find the extension on that DSL element.
            val findByName = (buildTypeDsl as ExtensionAware).extensions.findByName("exampleDsl")
            if (findByName != null) { //因为我在过程中添加的新变体 可能没有这个属性
                val buildTypeExtension = findByName as BuildTypeExtension
                println("新属性的值：${buildTypeExtension.invocationParameters ?: "default"}")
            }
        }

    }
}