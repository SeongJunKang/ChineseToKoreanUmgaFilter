package com.sjkang.solr.analysis

import com.sjkang.lucene.analysis.HanjaTokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.util.TokenFilterFactory


class HanjaTokenFilterFactory : TokenFilterFactory {


    constructor(args: Map<String, String>) : super(args)

    override fun create(arg0: TokenStream): TokenStream {
        return HanjaTokenFilter(arg0)
    }
}
