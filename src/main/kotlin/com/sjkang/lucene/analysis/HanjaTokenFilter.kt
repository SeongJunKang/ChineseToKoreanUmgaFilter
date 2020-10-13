package com.sjkang.lucene.analysis

import com.sjkang.lucene.analysis.exception.HanjaTokenFilterException
import com.sjkang.lucene.analysis.utils.StringOffset
import org.apache.lucene.analysis.TokenFilter
import org.apache.lucene.analysis.TokenStream
import java.io.IOException
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import com.sjkang.lucene.analysis.utils.Dictionary
import java.util.*


class HanjaTokenFilter(input: TokenStream?) : TokenFilter(input) {

    private val charTermAtt = addAttribute(CharTermAttribute::class.java) as CharTermAttribute
    private val offsetAtt = addAttribute(OffsetAttribute::class.java) as OffsetAttribute
    private val posiIncAtt = addAttribute(PositionIncrementAttribute::class.java) as PositionIncrementAttribute
    private var arrayOfChar: CharArray? = null
    private var startOffset: Int = 0
    private var termLength: Int = 0
    private val linkedList = LinkedList<StringOffset>()

    private fun statusCheck(paramBoolean: Boolean) {
        clearAttributes()
        val stringOffset = this.linkedList.removeFirst() as StringOffset
        val i = stringOffset.offset
        this.charTermAtt.copyBuffer(stringOffset.string!!.toCharArray(), 0, stringOffset.string!!.length)
        this.offsetAtt.setOffset(this.startOffset + i, this.startOffset + i + this.termLength)
        if (!paramBoolean && i == 0) {
            this.posiIncAtt.positionIncrement = 0
        }
    }

    @Throws(IOException::class)
    override fun incrementToken(): Boolean {
        if (this.arrayOfChar != null && this.linkedList.size > 0) {
            statusCheck(false)
            return true
        }

        if (!this.input.incrementToken()) {
            return false
        }

        this.arrayOfChar = this.charTermAtt.buffer().clone()
        this.startOffset = this.offsetAtt.startOffset()
        this.termLength = this.charTermAtt.length

        try {
            var targetString: String? = this.arrayOfChar!!.joinToString("")
            targetString = targetString!!.substring(0, this.charTermAtt.length)

            linkedList.add(StringOffset(targetString, 0))
            val returnArrayList = ArrayList<StringBuffer>()
            returnArrayList.add(StringBuffer())
            val strLength = targetString.length
            var charCode: Int
            var charCount: Int
            var resultCharSize = 0
            while (resultCharSize < strLength) {
                charCode = Character.codePointAt(targetString.toCharArray(), resultCharSize)
                charCount = Character.charCount(charCode)
                val key = String(Character.toChars(charCode))
                var value: String? = Dictionary.findDictionaryDataByKey(key)

                if (value.isNullOrEmpty()) {
                    continue
                }

                val arrayOfChar = value!!.toCharArray()
                val totalArraySize = returnArrayList.size
                for (i2 in 0 until totalArraySize) {
                    val localStringBuffer1 = returnArrayList[i2]
                    for (i3 in 1 until arrayOfChar.size) {
                        val localStringBuffer2 = StringBuffer(localStringBuffer1)
                        localStringBuffer2.append(arrayOfChar[i3])
                        returnArrayList.add(localStringBuffer2)
                    }
                    localStringBuffer1.append(arrayOfChar[0])
                }
                resultCharSize += charCount
            }
            resultCharSize = if (returnArrayList.size < 1000) returnArrayList.size else 1000
            for (i in 0 until resultCharSize) {
                val buff = returnArrayList[i]
                if (buff.toString() != targetString) {
                    linkedList.add(StringOffset(buff.toString(), 0))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw HanjaTokenFilterException("Hanja Token Filter Exception\n",e)
        }

        if (this.linkedList.size > 0) {
            statusCheck(true)
        } else {
            incrementToken()
        }
        return true
    }

}
