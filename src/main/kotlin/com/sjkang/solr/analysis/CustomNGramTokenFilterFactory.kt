package com.sjkang.solr.analysis

import com.sjkang.lucene.analysis.CustomNGramTokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.util.TokenFilterFactory


class CustomNGramTokenFilterFactory : TokenFilterFactory {

    private var max: Int = 2
    private var min: Int = 1

    constructor(args: Map<String, String>): super(args) {
        if (!args.isEmpty()) {
            this.max = getInt(args, "maxGramSize", 2)
            this.min = getInt(args, "minGramSize", 1)
        } else {
            this.max = 2
            this.min = 1
        }
    }

    override fun create(arg0: TokenStream): TokenStream {
        try {
            return CustomNGramTokenFilter(arg0, this.min, this.max)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return arg0
    }
}
