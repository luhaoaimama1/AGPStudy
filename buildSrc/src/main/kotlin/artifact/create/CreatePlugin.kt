package artifact.create/*
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
import com.android.build.api.artifact.SingleArtifact
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension

//Create 这个是删除加创建  不依赖以前的文件 多个操作以最后面的为主
abstract class CreatePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val register = project.tasks.register(
                variant.name + "CreateManifestTask23",
                CreateManifestTask::class.java
            ) {
                println("artifact-create task register action")
            }

            //toCreate 等待产物MERGED_MANIFEST完成后 丢弃 生成新的产物register's updateManifest 传递下去
            variant.artifacts.use(register)
                .wiredWith(CreateManifestTask::updateManifest)
                .toCreate(SingleArtifact.MERGED_MANIFEST)
        }
    }
}