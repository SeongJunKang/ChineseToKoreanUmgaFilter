package com.sjkang.lucene.analysis

import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import java.io.StringReader


class HanjaAnalyzerTest {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            val analyzer = HanjaAnalyzer()
            var stream = analyzer.tokenStream("field", StringReader("學而時習之"))
            val termAtt = stream.addAttribute(OffsetAttribute::class.java) as OffsetAttribute
            try {
                stream.reset()
                while (stream.incrementToken()) {
                    println(termAtt.toString())
                    println("token start offset: " + termAtt.startOffset())
                    println("token end offset: " + termAtt.endOffset())
                }
                stream.end()
            } finally {
                stream.close()
            }
            analyzer.close()
        }
    }
}