plugins {
        id("com.android.application")
        kotlin("android")
}

println ("流程:dsl读取")
apply<ProcessPlugin>() //流程 更改 增加 变体属性
apply<property.PropertyPlugin>() //增加DSL 中的语法
////artifact产物中的四种语法
apply<artifact.transform.TransformPlugin>()
apply<artifact.create.CreatePlugin>()
apply<artifact.append.AppendPlugin>()
////插件 asm
////读取类内容 更改类内容
//apply<AsmTransformApi.AsmTransformPlugin>()
////读取所有类   生成一个控制类
apply<AsmTransformApi.newManager.AsmNewManagerPlugin>()
apply<artifact.otherArtifact.ArtifactsPlugin>()

android {

    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
    compileOptions {
        sourceCompatibility (JavaVersion.VERSION_11)
        targetCompatibility (JavaVersion.VERSION_11)
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildTypes {
        release {
            the<property.BuildTypeExtension>().invocationParameters = "what?"
        }
    }
}
