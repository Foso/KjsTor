/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package de.jensklingenberg.kjsTor.ktor

import io.ktor.response.ApplicationSendPipeline
import de.jensklingenberg.kjsTor.MyApplicationResponse


/**
 * Base class for implementing an [ApplicationResponse]
 */
abstract class MyBaseApplicationResponse(override val call: MyApplicationCall) :
    MyApplicationResponse {

    final override val pipeline = ApplicationSendPipeline().apply {
       //TODO merge(call.application.sendPipeline)
    }

}
