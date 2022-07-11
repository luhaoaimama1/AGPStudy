package artifact.transform/*
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

//Transformation即变换操作，它可以把原有产物作为输入值，进行变换后将结果作为输出值，并传递给下一个变换
abstract class TransformPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val register = project.tasks.register(
                variant.name + "TransManifestProvider2333",
                TransManifestTask::class.java
            ) {
                println("artifact-transform task register action")
            }

            //transform 把产物合并中的mainfest文件传递给register对象也就是TransManifestTask类中的mergedManifest
            //而根据传递过来的文件 把内容变换后 把文件TransManifestTask::updateManifest作为输出结果传递下去。
            variant.artifacts.use(register)
                .wiredWithFiles(
                    TransManifestTask::mergedManifest,
                    TransManifestTask::updateManifest
                )
                .toTransform(SingleArtifact.MERGED_MANIFEST)
        }
    }
}