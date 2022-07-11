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

//熟悉下其他的产物  但是都不太成功
apply<artifact.otherArtifact.ArtifactsPlugin>()

参考:https://github.com/android/gradle-recipes#readme
