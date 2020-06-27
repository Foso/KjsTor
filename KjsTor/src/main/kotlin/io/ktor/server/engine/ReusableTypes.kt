/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.engine

import io.ktor.http.Parameters
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.response.ApplicationSendPipeline


private val ReusableTypes = arrayOf(ByteArray::class, String::class, Parameters::class)

/**
 * Default send transformation
 */

fun ApplicationReceivePipeline.installDefaultTransformations() {
    intercept(ApplicationReceivePipeline.Transform) { query ->
            console.log("Transform")
        }
}

fun ApplicationSendPipeline.installDefaultTransformations() {
    intercept(ApplicationSendPipeline.Render) { value ->
        console.log("Transform")

    }
}