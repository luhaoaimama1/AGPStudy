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
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension

abstract class ProcessPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        println("流程:插件 apply")

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        //在此回调中，您可以访问和修改通过解析 build 文件中 android 代码块的信息而创建的 DSL 对象。
        //这些 DSL对象将用来在 build 的后续阶段中初始化和配置变体。 例如，您可以通过编程方式创建新配置或替换属性，
        androidComponents.finalizeDsl {
            println("流程:finalizeDsl")

            //it.buildTypes.names= debug、release
            it.buildTypes.create("variant3") {
                it.isJniDebuggable = false
            }
        }

        androidComponents.beforeVariants {
            println("流程:beforeVariants name:${it.name}")
        }

        androidComponents.onVariants {
            println("流程:onVariants  name:${it.name}")
        }
    }
}