package com.yelp.codegen.plugin

import com.yelp.codegen.main
import io.swagger.parser.SwaggerParser
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.kotlin.dsl.listProperty
import java.io.File

const val DEFAULT_PLATFORM = "kotlin"
const val DEFAULT_VERSION = "0.0.0"
const val DEFAULT_NAME = "defaultname"
const val DEFAULT_PACKAGE = "com.codegen.default"
const val DEFAULT_OUTPUT_DIR = "/gen"

open class GenerateTask : DefaultTask() {

    init {
        description = "Run the Swagger Code Generation tool"
        group = BasePlugin.BUILD_GROUP
    }

    @Input
    @Option(option = "platform", description = "Configures the platform that is used for generating the code.")
    var specs: ListProperty<Map<String, *>> = project.objects.listProperty()

    @TaskAction
    fun swaggerGenerate() {
        specs.get().forEach{
            val spec = SpecConfiguration()
            spec.specVersion = it["specVersion"] as String?
            spec.platform = it["platform"] as String?
            spec.packageName = it["packageName"] as String?
            spec.specName = it["specName"] as String?
            spec.inputFile = it["inputFile"] as File
            spec.outputDir = it["outputDir"] as File?
            spec.features = it["features"] as Map<String,*>?
            spec.extraFiles = it["extraFiles"] as File?

            if (spec.specVersion == null) { readVersionFromSpecfile(spec.inputFile, spec) }
            val defaultOutputDir = File(project.buildDir, DEFAULT_OUTPUT_DIR)

            var headersToRemove:ArrayList<String>? = spec.features?.get("headersToRemove") as ArrayList<String>?

            println("""
            ####################
            Yelp Swagger Codegen
            ####################
            Platform ${'\t'} ${spec.platform ?: "[ DEFAULT ] $DEFAULT_PLATFORM"}
            Package ${'\t'} ${spec.packageName ?: "[ DEFAULT ] $DEFAULT_PACKAGE"}
            specName ${'\t'} ${spec.specName ?: "[ DEFAULT ] $DEFAULT_NAME"}
            specVers ${'\t'} ${spec.specVersion ?: "[ DEFAULT ] $DEFAULT_VERSION"}
            input ${"\t\t"} $spec.inputFile
            output ${"\t\t"} ${spec.outputDir ?: "[ DEFAULT ] $defaultOutputDir"}
            groupId ${'\t'} ${spec.packageName ?: "[ DEFAULT ] default"}
            artifactId ${'\t'} ${spec.packageName ?: "[ DEFAULT ] com.codegen"}
            features ${'\t'} ${headersToRemove?.joinToString(", ")?.ifEmpty { "[  EMPTY  ]" }}
        """.trimIndent())

            val packageName = spec.packageName ?: DEFAULT_PACKAGE

            val params = mutableListOf<String>()
            params.add("-p")
            params.add(spec.platform ?: DEFAULT_PLATFORM)
            params.add("-s")
            params.add(spec.specName ?: DEFAULT_NAME)
            params.add("-v")
            params.add(spec.specVersion ?: DEFAULT_VERSION)
            params.add("-g")
            params.add(packageName.substringBeforeLast('.'))
            params.add("-a")
            params.add(packageName.substringAfterLast('.'))
            params.add("-i")
            params.add(spec.inputFile.toString())
            params.add("-o")
            params.add((spec.outputDir ?: defaultOutputDir).toString())

            headersToRemove?.let{
                if(it.size > 0){
                    params.add("-ignoreheaders")
                    params.add(it?.joinToString(","))
                }
            }

            // Running the Codegen Main here
            main(params.toTypedArray())

            // Copy over the extra files.
            val source = spec.extraFiles
            val destin = spec.outputDir
            if (source != null && destin != null) {
                source.copyRecursively(destin, overwrite = true)
            }
        }
    }

    private fun readVersionFromSpecfile(specFile: File?, specConfig:SpecConfiguration) {
        println("specFile = "+specFile?.absolutePath)
        val swaggerSpec = SwaggerParser().readWithInfo(specFile?.absolutePath, listOf(), false).swagger
        specConfig.specVersion = when (val version = swaggerSpec.info.version) {
            is String -> {
                println("Successfully read version from Swagger Spec file: $version")
                version
            }
            else -> {
                println("Issue in reading version from Swagger Spec file. Falling back to $DEFAULT_VERSION")
                DEFAULT_VERSION
            }
        }
    }
}
