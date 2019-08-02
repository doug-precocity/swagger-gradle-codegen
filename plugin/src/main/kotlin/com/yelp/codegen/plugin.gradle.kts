package com.yelp.codegen

import com.yelp.codegen.plugin.GenerateTask
import com.yelp.codegen.plugin.GenerateTaskConfiguration

val config = extensions.create("generateSwagger", GenerateTaskConfiguration::class.java)



tasks {
    register<GenerateTask>("generateSwagger") {
        specs = config.specs
    }
}
