package com.sjkang.lucene.analysis

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.core.WhitespaceTokenizer

class HanjaAnalyzer : Analyzer() {

    override fun createComponents(fieldName: String?): TokenStreamComponents? {
        val wt = WhitespaceTokenizer()
        try {
            val yoonsNgram = CustomNGramTokenFilter(wt, 1, 30)
            val htf = HanjaTokenFilter(yoonsNgram)
            return Analyzer.TokenStreamComponents(wt, htf)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

}
