plugins{id("com.android.application");id("org.jetbrains.kotlin.android")}
android{
 namespace="com.delzai.app"; compileSdk=34
 defaultConfig{applicationId="com.delzai.app";minSdk=26;targetSdk=34;versionCode=1;versionName="1.0"}
 androidResources{noCompress+=listOf("task")}
 compileOptions{sourceCompatibility=JavaVersion.VERSION_17;targetCompatibility=JavaVersion.VERSION_17}
 kotlinOptions{jvmTarget="17"}
 buildTypes{release{signingConfig=signingConfigs.getByName("debug")}}
}
dependencies{
 implementation("androidx.webkit:webkit:1.11.0")
 implementation("com.google.mediapipe:tasks-genai:0.10.24")
 implementation("com.google.guava:listenablefuture:1.0")
}
// Qwen скачивается при сборке и попадает внутрь APK
val modelFile=file("src/main/assets/qwen.task")
val downloadModel by tasks.registering{
 outputs.file(modelFile); onlyIf{!modelFile.exists()}
 doLast{
  modelFile.parentFile.mkdirs()
  val u="https://huggingface.co/litert-community/Qwen2.5-0.5B-Instruct/resolve/main/Qwen2.5-0.5B-Instruct_multi-prefill-seq_q8_ekv1280.task"
  java.net.URL(u).openStream().use{i->modelFile.outputStream().use{i.copyTo(it)}}
 }
}
tasks.matching{it.name=="preBuild"}.configureEach{dependsOn(downloadModel)}
