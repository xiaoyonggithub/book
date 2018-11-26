# 一、POI操作word模板替换

## 1.1.`word2003`

针对word2003及以前版本的.doc格式的文档

```java
@Test
public void test02() throws IOException {
    Map<String, Object> param = new HashMap<>();
    param.put("$name$", "肖永");
    param.put("$sex$", "男");
    param.put("$address$", "成都市靖江区");
    param.put("$phone$", "15181278720");
    param.put("$email$", "15181278720@163.com");

    File file = new File("f:\\template_1.doc");
    HWPFDocument document = new HWPFDocument(new FileInputStream(file));
    Range range = document.getRange();
    for (int i = 0; i < range.numParagraphs(); i++) {
        //获取段落
        Paragraph paragraph = range.getParagraph(i);
        if (paragraph.text().indexOf("$") > -1) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                paragraph.replaceText(entry.getKey(), entry.getValue().toString());
            }
        }
    }

    document.write(new FileOutputStream("f:\\tmp.doc"));
}
```

## 1.2.`word2017`

针对word2017及以后的版本的.docx格式的文档

```java
/**
 * 替换段落里面的变量
 * @param doc 要替换的文档
 * @param params 参数
 */
private void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
    Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
    XWPFParagraph para;
    while (iterator.hasNext()) {
        para = iterator.next();
        this.replaceInPara(para, params);
    }
}

/**
 * 替换段落里面的变量
 * @param para 要替换的段落
 * @param params 参数
 */
private void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
    List<XWPFRun> runs;
    Matcher matcher;
    if (this.matcher(para.getParagraphText()).find()) {
        runs = para.getRuns();
        for (int i=0; i<runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String runText = run.toString();
            matcher = this.matcher(runText);
            if (matcher.find()) {
                while ((matcher = this.matcher(runText)).find()) {
                    runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                }
                //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                para.removeRun(i);
                XWPFRun newRun = para.insertNewRun(i);
                newRun.setFontSize(16);
                newRun.setText(runText);
            }
        }
    }
}

/**
 * 替换表格里面的变量
 * @param doc 要替换的文档
 * @param params 参数
 */
private void replaceInTable(XWPFDocument doc, Map<String, Object> params) {
    Iterator<XWPFTable> iterator = doc.getTablesIterator();
    XWPFTable table;
    List<XWPFTableRow> rows;
    List<XWPFTableCell> cells;
    List<XWPFParagraph> paras;
    while (iterator.hasNext()) {
        table = iterator.next();
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                paras = cell.getParagraphs();
                for (XWPFParagraph para : paras) {
                    this.replaceInPara(para, params);
                }
            }
        }
    }
}

/**
 * 正则匹配字符串
 * @param str
 * @return
 */
private Matcher matcher(String str) {
    Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
    //Pattern pattern = Pattern.compile("\\$.+?\\$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(str);
    return matcher;
}

/**
 * 关闭输入流
 * @param is
 */
private void close(InputStream is) {
    if (is != null) {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 关闭输出流
 * @param os
 */
private void close(OutputStream os) {
    if (os != null) {
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

```java
/**
 * 用一个docx文档作为模板，然后替换其中的内容，再写入目标文档中。
 * @throws Exception
 */
@Test
public void testTemplateWrite() throws Exception {
    Map<String, Object> testMap = new HashMap<String, Object>();

    testMap.put("name", "小明");
    testMap.put("sex", "男");
    testMap.put("address", "软件园");
    testMap.put("phone", "88888888");
    testMap.put("email", "15181278720@163.com");

    String filePath = "f:\\template.docx";
    InputStream is = new FileInputStream(filePath);
    XWPFDocument doc = new XWPFDocument(is);
    //替换段落里面的变量,模板${name}
    this.replaceInPara(doc, testMap);
    //替换表格里面的变量
    this.replaceInTable(doc, testMap);
    OutputStream os = new FileOutputStream("F:\\write.docx");
    doc.write(os);
    this.close(os);
    this.close(is);
}
```

注意：在声明${xx}这个的时候，要从左到右按照顺序来写，不能先{} 之后在写xx ，这样写，解析不出来

## 1.3.[poi-tl](https://github.com/Sayi/poi-tl)

```xml
<dependency>
    <groupId>com.deepoove</groupId>
    <artifactId>poi-tl</artifactId>
    <version>1.3.1</version>
</dependency>
```

```java
@Test
public void test01() throws Exception {
    Map<String, Object> param = new HashMap<>();
    param.put("name", "肖永");
    param.put("sex", "男");
    param.put("address", "成都市靖江区");
    param.put("phone", "15181278720");
    param.put("email", "15181278720@163.com");

    //替换的模板{{name}}
    XWPFTemplate template = XWPFTemplate.compile("f:\\template.docx").render(param);
    FileOutputStream out = new FileOutputStream("f:\\out_template.docx");
    template.write(out);
    out.flush();
    out.close();
    template.close();
}
```

![1537254621450](E:\typora\images\1537254621450.png)



## 1.4.`excel`的克隆

