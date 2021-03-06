@file:JsQualifier("fs.readdir")
@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "EXTERNAL_DELEGATION")
package fs.readdir

import kotlin.js.*
import kotlin.js.Json
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*
import Buffer

external fun __promisify__(path: String, options: fs.`T$36`? = definedExternally): Promise<Array<String>>

external fun __promisify__(path: Buffer, options: fs.`T$36`? = definedExternally): Promise<Array<String>>

external fun __promisify__(path: URL, options: fs.`T$36`? = definedExternally): Promise<Array<String>>

external fun __promisify__(path: String, options: String /* "buffer" */): Promise<Array<Buffer>>

external fun __promisify__(path: String, options: fs.`T$37`): Promise<Array<Buffer>>

external fun __promisify__(path: Buffer, options: String /* "buffer" */): Promise<Array<Buffer>>

external fun __promisify__(path: Buffer, options: fs.`T$37`): Promise<Array<Buffer>>

external fun __promisify__(path: URL, options: String /* "buffer" */): Promise<Array<Buffer>>

external fun __promisify__(path: URL, options: fs.`T$37`): Promise<Array<Buffer>>

external fun __promisify__(path: String, options: fs.`T$38`? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: String, options: String? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: Buffer, options: fs.`T$38`? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: Buffer, options: String? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: URL, options: fs.`T$38`? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: URL, options: String? = definedExternally): Promise<dynamic /* Array<String> | Array<Buffer> */>

external fun __promisify__(path: String, options: fs.`T$39`): Promise<Array<fs.Dirent>>

external fun __promisify__(path: Buffer, options: fs.`T$39`): Promise<Array<fs.Dirent>>

external fun __promisify__(path: URL, options: fs.`T$39`): Promise<Array<fs.Dirent>>

external fun __promisify__(path: String): Promise<Array<String>>

external fun __promisify__(path: Buffer): Promise<Array<String>>

external fun __promisify__(path: URL): Promise<Array<String>>