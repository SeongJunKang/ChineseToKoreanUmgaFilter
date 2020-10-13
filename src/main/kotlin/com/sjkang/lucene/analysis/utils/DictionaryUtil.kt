package com.sjkang.lucene.analysis.utils

import org.apache.commons.lang.StringEscapeUtils
import java.util.ArrayList
import java.util.regex.Pattern


class DictionaryUtil {
    companion object {
        @JvmStatic var stringArr = arrayOf<String>()

        @JvmStatic
        fun lineFilter(str: String?, separatorChars: String?): Array<String>? {
            if (str == null) {
                return null
            }
            val i: Int = str.length
            if (i == 0) {
                return stringArr
            }
            val localArrayList = ArrayList<String>()
            var j = 1
            var k = 0
            var m = 0
            var n = 0
            if (separatorChars == null) {
                while (k < i) {
                    if (Character.isWhitespace(str[k])) {
                        if (n != 0) {
                            if (j++ == -1) {
                                k = i
                            }
                            localArrayList.add(str.substring(m, k))
                            n = 0
                        }
                        k++
                        m = k
                    } else {
                        n = 1
                        k++
                    }
                }
            } else if (separatorChars.length == 1) {
                val separatorChar = separatorChars[0]
                while (k < i) {
                    if (str[k] == separatorChar) {
                        if (n != 0) {
                            if (j++ == -1) {
                                k = i
                            }
                            localArrayList.add(str.substring(m, k))
                            n = 0
                        }
                        k++
                        m = k
                    } else {
                        n = 1
                        k++
                    }
                }
            } else {
                while (k < i) {
                    if (separatorChars.indexOf(str[k]) >= 0) {
                        if (n != 0) {
                            if (j++ == -1) {
                                k = i
                            }
                            localArrayList.add(str.substring(m, k))
                            n = 0
                        }
                        k++
                        m = k
                    } else {
                        n = 1
                        k++
                    }
                }
            }
            if (n != 0) {
                localArrayList.add(str.substring(m, k))
            }
            return localArrayList.toTypedArray()
        }

        @JvmStatic
        fun unicode2code(unicode: String): String {
            if (unicode.contains("U+") || unicode.contains("\\u")) {
                var str = unicode.replace("U+", "\\u")
                if (str.length > 6 && str.startsWith("\\u2")) {
                    str = str.replace("\\u", "")
                    val codePoint = Integer.parseInt(str, 16)
                    str = String(Character.toChars(codePoint))
                }
                return StringEscapeUtils.unescapeJava(str)
            } else {
                return unicode
            }
        }

        @JvmStatic
        fun countMatches(str: String, sub: String): Int {
            if (str.isEmpty() || sub.isEmpty())
                return 0

            val pattern = Pattern.compile(sub)
            val matcher = pattern.matcher(str)

            var count = 0
            while (matcher.find()) {
                count++
            }
            return count
        }

        @JvmStatic
        fun replaceLast(string: String, toReplace: String, replacement: String): String {
            val pos = string.lastIndexOf(toReplace)
            return if (pos > -1) {
                (string.substring(0, pos) + replacement
                        + string.substring(pos + toReplace.length, string.length))
            } else {
                string
            }
        }
    }
}