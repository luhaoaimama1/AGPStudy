package artifact.append
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
import com.android.build.api.artifact.MultipleArtifact
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension

//Create 这个是删除加创建  不依赖以前的文件 多个操作以最后面的为主
abstract class AppendPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val taskProvider =
                project.tasks.register(variant.name + "AddAssetTask23", AppendAssetTask::class.java)
                {
                    println("artifact-append task register action")
                    it.content.set("AppendAssetTask test")
                }

            //toAppendTo  等待产物ok后 在对应的目录创建 任务名文件夹  在文件夹内添加文件即可。然后把这个目录传递下去
            variant.artifacts.use(taskProvider)
                .wiredWith(AppendAssetTask::outputDir)
                .toAppendTo(MultipleArtifact.ASSETS)
        }
    }
}