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

同一个`execl`之间的克隆

```java
Sheet sheet = workbook.getSheetAt(0);
for (int i = 0; i < 3; i++) {
    Row row = sheet.getRow(1);
    Cell cell = row.getCell(0);
    cell.setCellValue("设置数据");
    //克隆sheet
    ((XSSFWorkbook) workbook).cloneSheet(0, String.valueOf((i + 1)));
    /*row = cloneSheet.getRow(1);
    cell = row.getCell(0);
    cell.setCellValue("测试写入值");*/
}
workbook.removeSheetAt(0);
FileOutputStream fos = new FileOutputStream("f:\\tmp.xlsx");
workbook.write(fos);
fos.close();
workbook.close();
```

`excel`支持克隆的工具类，用于不同文件之间的克隆

```java
public class ExcelOperationUtil {
    private static Logger logger = LoggerFactory.getLogger(ExcelOperationUtil.class);

    /**
     * sheet 复制，复制数据、如果同一个文件，复制样式，不同文件则只复制数据<br/>
     * 如果是同book中复制，建议使用workbook中的cloneSheet()方法<br/>
     *
     * <br/>建议用于 不同book间只复制数据
     *
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet) {
        copySheet(srcSheet, desSheet, true, true, null);
    }

    /**
     * sheet 复制，如果同一个文件，复制样式，不同文件则不复制<br/>
     *
     * <br/>建议用于 同book中，只复制样式，不复制数据<br/>
     * eg: copySheet(srcSheet, desSheet, false)
     *
     * @param copyValueFlag 控制是否复制数据
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyValueFlag) {
        copySheet(srcSheet, desSheet, copyValueFlag, true, null);
    }

    /**
     * sheet 复制，复制数据、样式<br/>
     *
     * <br/>建议用于 不同book间复制，同时复制数据和样式<br/>
     * eg: copySheet(srcSheet, desSheet, mapping)
     *
     * @param mapping 不同文件间复制时，如果要复制样式，必传，否则不复制样式
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, StyleMapping mapping) {
        copySheet(srcSheet, desSheet, true, true, mapping);
    }

    /**
     * sheet 复制,复制数据<br/>
     *
     *  <br/>建议用于 同book中，只复制数据，不复制样式<br/>
     *  eg: copySheet(srcSheet, desSheet, false, null)
     *
     * @param srcSheet
     * @param desSheet
     * @param copyStyleFlag
     * @param mapping
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyStyleFlag, StyleMapping mapping) {
        copySheet(srcSheet, desSheet, true, copyStyleFlag, mapping);
    }

    /**
     * sheet 复制, 灵活控制是否控制数据、样式<br/>
     *
     * <br/>不建议直接使用
     *
     * @param copyValueFlag 控制是否复制数据
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping       不同book中复制样式时，必传
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) {
        if (srcSheet.getWorkbook() == desSheet.getWorkbook()) {
            logger.warn("统一个workbook内复制sheet建议使用 workbook的cloneSheet方法");
        }

        //合并区域处理
        copyMergedRegion(srcSheet, desSheet);

        //行复制
        Iterator<Row> rowIterator = srcSheet.rowIterator();

        int areadlyColunm = 0;
        while (rowIterator.hasNext()) {
            Row srcRow = rowIterator.next();
            Row desRow = desSheet.createRow(srcRow.getRowNum());
            copyRow(srcRow, desRow, copyValueFlag, copyStyleFlag, mapping);

            //调整列宽(增量调整)
            if (srcRow.getPhysicalNumberOfCells() > areadlyColunm) {
                for (int i = areadlyColunm; i < srcRow.getPhysicalNumberOfCells(); i++) {
                    desSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
                }
                areadlyColunm = srcRow.getPhysicalNumberOfCells();
            }
        }
    }

    /**
     * 复制行
     */
    public static void copyRow(Row srcRow, Row desRow) {
        copyRow(srcRow, desRow, true, true, null);
    }

    /**
     * 复制行
     */
    public static void copyRow(Row srcRow, Row desRow, boolean copyValueFlag) {
        copyRow(srcRow, desRow, copyValueFlag, true, null);
    }

    /**
     * 复制行
     */
    public static void copyRow(Row srcRow, Row desRow, StyleMapping mapping) {
        copyRow(srcRow, desRow, true, true, mapping);
    }

    /**
     * 复制行
     */
    public static void copyRow(Row srcRow, Row desRow, boolean copyStyleFlag, StyleMapping mapping) {
        copyRow(srcRow, desRow, true, copyStyleFlag, mapping);
    }

    /**
     * 复制行
     */
    public static void copyRow(Row srcRow, Row desRow,boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) {
        Iterator<Cell> it = srcRow.cellIterator();
        while (it.hasNext()) {
            Cell srcCell = it.next();
            Cell desCell = desRow.createCell(srcCell.getColumnIndex());
            copyCell(srcCell, desCell, copyValueFlag, copyStyleFlag, mapping);
        }
    }

    /**
     * 复制区域（合并单元格）
     */
    public static void copyMergedRegion(Sheet srcSheet, Sheet desSheet) {
        int sheetMergerCount = srcSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            desSheet.addMergedRegion(srcSheet.getMergedRegion(i));
            CellRangeAddress cellRangeAddress = srcSheet.getMergedRegion(i);
        }
    }

    /**
     * 复制单元格，复制数据，如果同文件，复制样式，不同文件则不复制样式
     */
    public static void copyCell(Cell srcCell, Cell desCell) {
        copyCell(srcCell, desCell, true, true,null);
    }

    /**
     * 复制单元格， 如果同文件，复制样式，不同文件则不复制样式
     * @param copyValueFlag 控制是否复制数据
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyValueFlag) {
        copyCell(srcCell, desCell, copyValueFlag, true, null);
    }

    /**
     * 复制单元格，复制数据,复制样式
     * @param mapping       不同文件间复制时，如果要复制样式，必传，否则不复制样式
     */
    public static void copyCell(Cell srcCell, Cell desCell,  StyleMapping mapping) {
        copyCell(srcCell, desCell, true, true, mapping);
    }

    /**
     * 复制单元格，复制数据
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping       不同文件间复制时，如果要复制样式，必传，否则不复制样式
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyStyleFlag, StyleMapping mapping) {
        copyCell(srcCell, desCell, true, copyStyleFlag, mapping);
    }

    /**
     * 复制单元格
     * @param copyValueFlag 控制是否复制单元格的内容
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping 不同文件间复制时，如果需要连带样式复制，必传，否则不复制样式
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) {
        Workbook srcBook = srcCell.getSheet().getWorkbook();
        Workbook desBook = desCell.getSheet().getWorkbook();

        //复制样式
        //如果是同一个excel文件内，连带样式一起复制
        if (srcBook == desBook && copyStyleFlag) {
            //同文件，复制引用
            desCell.setCellStyle(srcCell.getCellStyle());
        } else if (copyStyleFlag) {
            //不同文件，通过映射关系复制
            if (null != mapping) {
                short desIndex = mapping.desIndex(srcCell.getCellStyle().getIndex());
                desCell.setCellStyle(desBook.getCellStyleAt(desIndex));
            }
        }

        //复制评论
        if (srcCell.getCellComment() != null) {
            desCell.setCellComment(srcCell.getCellComment());
        }

        //复制内容
        desCell.setCellType(srcCell.getCellTypeEnum());

        if (copyValueFlag) {
            switch (srcCell.getCellTypeEnum()) {
                case STRING:
                    desCell.setCellValue(srcCell.getStringCellValue());
                    break;
                case NUMERIC:
                    desCell.setCellValue(srcCell.getNumericCellValue());
                    break;
                case FORMULA:
                    desCell.setCellFormula(srcCell.getCellFormula());
                    break;
                case BOOLEAN:
                    desCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case ERROR:
                    desCell.setCellValue(srcCell.getErrorCellValue());
                    break;
                case BLANK:
                    //nothing to do
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * 把一个excel中的styleTable复制到另一个excel中<br>
     * 如果是同一个excel文件，就不用复制styleTable了
     * @return StyleMapping 两个文件中styleTable的映射关系
     * @see StyleMapping
     */
    public static StyleMapping copyCellStyle(Workbook srcBook, Workbook desBook){
        if (null == srcBook || null == desBook) {
            throw new ExcelException("源excel 或 目标excel 不存在");
        }
        if (srcBook.equals(desBook)) {
            throw new ExcelException("不要使用此方法在同一个文件中copy style，同一个excel中复制sheet不需要copy Style");
        }
        if ((srcBook instanceof HSSFWorkbook && desBook instanceof XSSFWorkbook) ||
                (srcBook instanceof XSSFWorkbook && desBook instanceof HSSFWorkbook)) {
            throw new ExcelException("不支持在不同的版本的excel中复制样式）");
        }

        logger.debug("src中style number:{}, des中style number:{}", srcBook.getNumCellStyles(), desBook.getNumCellStyles());
        short[] src2des = new short[srcBook.getNumCellStyles()];
        short[] des2src = new short[desBook.getNumCellStyles() + srcBook.getNumCellStyles()];

        for(short i=0;i<srcBook.getNumCellStyles();i++){
            //建立双向映射
            CellStyle srcStyle = srcBook.getCellStyleAt(i);
            CellStyle desStyle = desBook.createCellStyle();
            src2des[srcStyle.getIndex()] = desStyle.getIndex();
            des2src[desStyle.getIndex()] = srcStyle.getIndex();

            //复制样式
            desStyle.cloneStyleFrom(srcStyle);
        }


        return new StyleMapping(des2src, src2des);
    }

    /**
     * 存放两个excel文件中的styleTable的映射关系，以便于在复制表格时，在目标文件中获取到对应的样式
     */
    public static class StyleMapping {
        /**
         *
         */
        private short[] des2srcIndexMapping;
        /**
         *
         */
        private short[] src2desIndexMapping;

        /**
         * 不允许其他类创建此类型对象
         */
        private StyleMapping() {
        }

        public StyleMapping(short[] des2srcIndexMapping, short[] src2desIndexMapping) {
            this.des2srcIndexMapping = des2srcIndexMapping;
            this.src2desIndexMapping = src2desIndexMapping;
        }

        public short srcIndex(short desIndex) {
            if (desIndex < 0 || desIndex >= this.des2srcIndexMapping.length) {
                throw new ExcelException("索引越界：源文件styleNum=" + this.des2srcIndexMapping.length + " 访问位置=" + desIndex);
            }
            return this.des2srcIndexMapping[desIndex];
        }

        /**
         * 根据源文件的style的index,获取目标文件的style的index
         * @param srcIndex 源excel中style的index
         * @return desIndex 目标excel中style的index
         */
        public short desIndex(short srcIndex) {
            if (srcIndex < 0 || srcIndex >= this.src2desIndexMapping.length) {
                throw new ExcelException("索引越界：源文件styleNum=" + this.src2desIndexMapping.length + " 访问位置=" + srcIndex);
            }

            return this.src2desIndexMapping[srcIndex];
        }
    }

}
```



# 二、`POI-TL`

[]: http://deepoove.com/poi-tl/



## 2.1.模板内容替换

```java
//针对表格数据内容的填充
public class CustomTableRenderPolicy extends AbstractRenderPolicy {

    @Override
    protected boolean validate(Object data) {
        return data != null;
    }

    @Override
    public void doRender(RunTemplate runTemplate, Object data, XWPFTemplate xwpfTemplate) {
        NiceXWPFDocument document = xwpfTemplate.getXWPFDocument();
        XWPFRun run = runTemplate.getRun();
        //获取表格数据
        MiniTableRenderData tableData = (MiniTableRenderData) data;
        List<RowRenderData> datas = tableData.getDatas();
        //没有数据，清空表格
        if (data == null || datas == null || datas.size() <= 0) {
            run.setText("", 0);
            run.removeTab();
            XWPFHelper.clearTable(runTemplate.getTagName(), document.getTables(), run);
            return;
        }
        ///获取需要填充数据的表格
        Map<String, Object> map = XWPFHelper.getTable(runTemplate.getTagName(), document.getTables());
        XWPFTable table = (XWPFTable) map.get("table");
        //设置数据填充的开始位置
        int startRow = 1;
        int startCell = 0;
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 0; i < rows.size(); i++) {
            XWPFTableRow row = rows.get(i);
            List<XWPFTableCell> cells = row.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                if (runTemplate.toString().equals(cell.getText())) {
                    startRow = i;
                    startCell = j;
                    break;
                }
            }
        }
        XWPFTableCell cell = null;
        XWPFRun r = null;
        if (table != null) {
            if (datas.size() > 0) {
                //数据遍历
                for (int i = 0; i < datas.size(); i++) {
                    RowRenderData rowData = datas.get(i);
                    int size = rowData.size();
                    if (rowData == null) {
                        continue;
                    }
                    List<TextRenderData> text = rowData.getRowData();
                    for (int j = startCell; j < startCell + size; j++) {
                        cell = table.getRow(startRow).getCell(j);
                        if (cell != null) {
                            r = XWPFHelper.getRun(cell, run);
                            //r = cell.getParagraphs().get(0).getRuns().get(0);
                            r.setText(text.get(j - startCell).getText(), 0);
                        }
                    }
                    if (i != datas.size() - 1) {
                        table.addRow(table.getRow(startRow), startRow++);
                    }
                }
            }
        }
    }
}
```

```java
List segments = getFfSegments(param);
String tmpPath = path + File.separator + UUID.randomUUID() + ".docx";
File file = new File(tmpPath);
URL url = new URL(getTemplatePath(ZjglConstants.YAE960_101102));
InputStream is = url.openStream();
FileUtils.inputstreamtoFile(is, file);
data.put("segments", new DocxRenderData(file, segments));
Configure.ConfigureBuilder builder = Configure.newBuilder();
builder = builder.addPlugin('&', new CustomTableRenderPolicy());
Configure configure = builder.buildGramer("${", "}").build();
url = new URL(templatePath);
XWPFTemplate doc = XWPFTemplate.compile(url.openStream(), configure).render(handlerClob(data));
ByteArrayOutputStream baos = new ByteArrayOutputStream();
doc.write(baos);
byte[] bytes = fileConverterService.CoverFile2Pdf(baos.toByteArray(), "WORD");
FileUtils.byte2File(bytes, tmpPath);
File tmpFile = new File(tmpPath);
List<Map> list = FileStore.fileUpload(tmpFile);
tmpFile.delete();
```

