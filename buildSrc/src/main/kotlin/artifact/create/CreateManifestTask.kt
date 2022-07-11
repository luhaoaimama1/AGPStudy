package artifact.create

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class CreateManifestTask: DefaultTask() {

    @get:OutputFile
    abstract val updateManifest: RegularFileProperty

    @ExperimentalStdlibApi
    @TaskAction
    fun taskAction() {
        println("artifact-create CreateManifestTask:updateManifest path  " + updateManifest.get().asFile.getAbsolutePath())
        //copy以前的 仅仅更改 minSdkVersion 19
        updateManifest.get().asFile.writeText("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    package=\"com.android.build.example.minimal\" >\n" +
                "\n" +
                "    <uses-sdk\n" +
                "        android:minSdkVersion=\"19\"\n" +
                "        android:targetSdkVersion=\"30\" />\n" +
                "\n" +
                "    <application\n" +
                "        android:debuggable=\"true\"\n" +
                "        android:label=\"Minimal\" >\n" +
                "        <activity android:name=\"com.android.build.example.minimal.MainActivity\" >\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\" />\n" +
                "\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\" />\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "    </application>\n" +
                "\n" +
                "</manifest>")
    }

}