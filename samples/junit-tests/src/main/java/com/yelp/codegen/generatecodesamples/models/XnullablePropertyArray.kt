/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: JUnit Tests
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package com.yelp.codegen.generatecodesamples.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yelp.codegen.generatecodesamples.tools.XNullable
import java.math.BigDecimal

/**
 *
 * @property numberArray
 * @property stringArray
 */
@JsonClass(generateAdapter = true)
data class XnullablePropertyArray(
    @Json(name = "number_array") @field:Json(name = "number_array") @XNullable var numberArray: List<BigDecimal?>? = null,
    @Json(name = "string_array") @field:Json(name = "string_array") @XNullable var stringArray: List<String?>? = null
)
