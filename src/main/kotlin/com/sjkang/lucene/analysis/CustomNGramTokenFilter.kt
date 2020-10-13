package com.sjkang.lucene.analysis

import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.miscellaneous.CodepointCountFilter
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute
import java.io.IOException


class CustomNGramTokenFilter(input: TokenStream?) : TokenFilter(input) {

    val DEFAULT_MIN_NGRAM_SIZE = 1
    val DEFAULT_MAX_NGRAM_SIZE = 2

    private var minGram: Int = 1
    private var maxGram: Int = 2

    private var curTermBuffer: CharArray? = null
    private var curTermLength: Int = 0
    private var curTermCodePointCount: Int = 0
    private var curGramSize: Int = 0
    private var curPosIncr: Int = 0
    private var curPos: Int = 0
    private var curPosLength: Int = 0
    private var endOffset: Int = 0
    private var startOffset: Int = 0

    private val termAtt = addAttribute(CharTermAttribute::class.java)
    private val posIncrAtt = addAttribute(PositionIncrementAttribute::class.java)

    private val offsetAtt = addAttribute(OffsetAttribute::class.java)
    private val posLengAtt = addAttribute(PositionLengthAttribute::class.java)

    @Throws(IllegalAccessException::class)
    constructor(input: TokenStream, minGram: Int, maxGram: Int): this(input) {
        (CodepointCountFilter(input, minGram, 2147483647))
        if (minGram < 1) {
            throw IllegalAccessException("minGram must be greater than zero")
        } else if (minGram > maxGram) {
            throw IllegalAccessException("minGram must not be greater than maxGram")
        }
        this.minGram = minGram
        this.maxGram = maxGram
    }

    @Throws(IllegalAccessException::class)
    fun CustomNGramTokenFilter(input: TokenStream) {
        CustomNGramTokenFilter(input, 1, 2)
    }

    override fun incrementToken(): Boolean {
        while(true) {

            if (curTermBuffer == null) {
                if (!input.incrementToken()) {
                    return false;
                }
                curTermBuffer = termAtt.buffer().clone()
                curTermLength = termAtt.length
                curTermCodePointCount = Character.codePointCount(this.termAtt, 0, this.termAtt.length)
                curPosIncr = posIncrAtt.positionIncrement
                curPosLength = posLengAtt.positionLength
                curPos = 0
                startOffset = offsetAtt.startOffset()
                endOffset = offsetAtt.endOffset()
                curGramSize = minGram
            }
            if ((curGramSize > maxGram) || (curPos + curGramSize > curTermCodePointCount)) {
                ++curPos
                curGramSize = minGram
            }

            if (curPos + curGramSize <= curTermCodePointCount ) {
                clearAttributes()
                var start: Int = Character.offsetByCodePoints(curTermBuffer, 0, curTermLength, 0, curPos)
                var end: Int = Character.offsetByCodePoints(curTermBuffer, 0, curTermLength, start, curGramSize)
                termAtt.copyBuffer(curTermBuffer, start, end - start)
                posIncrAtt.positionIncrement = curPosIncr
                posLengAtt.positionLength = curPosLength
                curPosIncr = 0
                offsetAtt.setOffset(start + startOffset, end + endOffset)
                curGramSize++
                return true
            }
            curTermBuffer = null
        }
    }


    @Throws(IOException::class)
    override fun reset() {
        super.reset()
        curTermBuffer = null
    }
}
