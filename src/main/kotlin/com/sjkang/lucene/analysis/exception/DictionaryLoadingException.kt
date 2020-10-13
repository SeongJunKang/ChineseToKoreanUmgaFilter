package com.sjkang.lucene.analysis.exception

import java.io.IOException

class DictionaryLoadingException : IOException {
    constructor(error:String, throwable: Throwable): super(error, throwable)
}
