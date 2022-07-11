package artifact.transform

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class TransManifestTask: DefaultTask() {
    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @get:OutputFile
    abstract val updateManifest: RegularFileProperty

    @ExperimentalStdlibApi
    @TaskAction
    fun taskAction() {
        println("artifact-transform  TransManifestTask:mergedManifest path  " + mergedManifest.get().asFile.getAbsolutePath())
        println("artifact-transform  TransManifestTask:updateManifest path  " + updateManifest.get().asFile.getAbsolutePath())
        val manifest = mergedManifest.get().asFile.readText()
        // ... verify manifest file content ...
        val replace = manifest.replace("android:minSdkVersion=\"21\"", "android:minSdkVersion=\"20\"")
        updateManifest.get().asFile.writeText(replace)
    }

}