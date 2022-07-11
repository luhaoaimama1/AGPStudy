package AsmTransformApi.newManager

import org.gradle.api.tasks.Input
import com.android.build.api.instrumentation.*
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import java.io.File
//visitor 交流的参数
interface BaseParams : InstrumentationParameters {
    @get:Input
    val additionalClassesDir: Property<File>

    @get:Input
    val generator: Property<Generator>
}