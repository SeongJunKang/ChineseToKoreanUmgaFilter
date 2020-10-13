package com.sjkang.lucene.analysis.utils

import com.sjkang.lucene.analysis.exception.DictionaryLoadingException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


class Dictionary {

    companion object {
        var dicMap: MutableMap<String, String>? = null

        @Synchronized
        @Throws(Exception::class)
        fun loading() {
            try {
                println("Loading chinese to korean pronunciation Dictionary")
                val innerDictionaryPath = "/HanjaToHangle.dic"
                val inputStream = Dictionary::class.java.getResourceAsStream(innerDictionaryPath)
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val buff = BufferedReader(inputStreamReader)
                val arr = ArrayList<String>()

                while (true) {
                    var line: String? = buff.readLine() ?: break

                    line = line!!.replace(" $", "")
                    var countMat = DictionaryUtil.countMatches(line, " ")
                    while (countMat > 1) {
                        line = DictionaryUtil.replaceLast(line!!, " ", "")
                        countMat = DictionaryUtil.countMatches(line, " ")
                    }
                    arr.add(line!!)
                }
                inputStreamReader.close()
                inputStream.close()
                dicMap = HashMap()
                for (i in arr.indices) {
                    val arrOfStr = DictionaryUtil.lineFilter(arr[i], " ")
                    if (arrOfStr!!.size >= 2) {
                        dicMap!![DictionaryUtil.unicode2code(arrOfStr[0].trim({ it <= ' ' }))] = arrOfStr[1].trim({ it <= ' ' })
                    }
                }
            } catch (e: IOException) {
                throw DictionaryLoadingException("Dictionary IOException", e)
            } catch (e: NullPointerException) {
                throw DictionaryLoadingException("Dictionary File Not Found Exception",e)
            }

        }

        @JvmStatic
        fun findDictionaryDataByKey(key: String): String {
            // call inner dictionary umga
            if (dicMap == null) {
                loading()
            }
            val str : String? = dicMap!![key]
            return str ?: key
        }
    }

}
