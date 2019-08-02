package com.yelp.codegen.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import javax.inject.Inject

open class GenerateTaskConfiguration @Inject constructor(objects: ObjectFactory) {
    var specs: ListProperty<Map<String, *>> = objects.listProperty()

}
