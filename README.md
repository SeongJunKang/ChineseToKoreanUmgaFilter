# ChineseToKoreanUmgaFilter

## 한자를 한국어 독음으로 바꿔주는 음가 필터 (Solr Filter)<br>

WhitespaceTokenizer를 이용하여 공백으로 분리하고,<br>
NGramFilter를 이용하여 한글자씩 한자를 한글 독음으로 변환 시키는 Filter<br>


## How to use
- 해당 소스를 jar 파일로 생성하여 아래의 위치로 복사한다.
```
${solr-home}/server/solr-webapp/webapp/WEB-INF/lib
```


* Solr Managed-schema 설정 방법

```
 <fieldType name="chi_2_kor" class="solr.TextField">
   <analyzer type="index">
   	<tokenizer class="solr.WhitespaceTokenizerFactory"/>
	<filter class="com.sjkang.solr.analysis.CustomNGramTokenFilterFactory" minGramSize="1" maxGramSize="30" />
	<filter class="com.sjkang.solr.analysis.HanjaTokenFilterFactory"/> 
   </analyzer>
   <analyzer type="query">
  	<tokenizer class="solr.WhitespaceTokenizerFactory"/>
	<filter class="solr.SynonymFilterFactory" synonyms="synonyms.txt" ignoreCase="true" expand="true"/>
   </analyzer>
  </fieldType>
```


學而時習之 (학이시습지) 분석 결과

- 데이터량이 매우 많아짐
- 대신에 한글/한자로 중간단어를 검색해도 검색결과를 나타낼 수 있음

```
學
학
學而
학이
學而時
학이시
學而時習
학이시습
學而時習之
학이시습지
而
이
而時
이시
而時習
이시습
而時習之
이시습지
時
시
時習
시습
時習之
시습지
習
습
習之
습지
之
지
```