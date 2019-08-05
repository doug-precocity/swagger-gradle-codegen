package com.yelp.codegen.plugin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.options.Option
import java.io.File

class SpecConfiguration {

	@Input
	@Optional
	@Option(option = "platform", description = "Configures the platform that is used for generating the code.")
	var platform: String? = null

	@Input
	@Optional
	@Option(option = "packageName", description = "Configures the package name of the resulting code.")
	var packageName: String? = null

	@Input
	@Optional
	@Option(option = "specName", description = "Configures the name of the service for the Swagger Spec.")
	var specName: String? = null

	@Input
	@Optional
	@Option(option = "specVersion", description = "Configures the version of the Swagger Spec.")
	var specVersion: String? = null

	@InputFile
	@Option(option = "inputFile", description = "Configures path of the Swagger Spec.")
	var inputFile: File? = null

	@OutputDirectory
	@Optional
	@Option(option = "outputDir", description = "Configures path of the Generated code directory.")
	var outputDir: File? = null

	@InputFiles
	@Optional
	@Option(option = "extraFiles",
			description = "Configures path of the extra files directory to be added to the Generated code.")
	var extraFiles: File? = null

	@Nested
	@Optional
	@Option(option = "features", description = "")
	var features: Map<String,*>? = null


}
