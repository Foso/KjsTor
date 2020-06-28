package de.jensklingenberg.kjsTor.express


import kotlin.js.Json

@JsModule("express")
external class Express {
    fun get(route: String, callback: (req: Request, res: Response) -> Unit)
    fun listen(port: Int, callbacl: (Unit) -> Unit)
}

open external class Request {
    var path: String
        get() = definedExternally
        set(value) = definedExternally
}


external class Response {
    fun send(data: String)
}