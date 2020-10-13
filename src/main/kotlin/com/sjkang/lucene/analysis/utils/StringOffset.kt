package com.sjkang.lucene.analysis.utils

class StringOffset {
    var string: String? = null
    var offset = 0

    constructor(string: String, offset: Int){
        this.string = string
        this.offset = offset
    }
}