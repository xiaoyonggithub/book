# 一、金额转大写函数

```plsql
Create Or Replace Function Money2Chinese(Money In Number) Return Varchar2 Is
  strYuan       Varchar2(150);
  strYuanFen    Varchar2(152);
  numLenYuan    Number;
  numLenYuanFen Number;
  strRstYuan    Varchar2(600);
  strRstFen     Varchar2(200);
  strRst        Varchar2(800);
  Type typeTabMapping Is Table Of Varchar2(2) Index By Binary_Integer;
  tabNumMapping  typeTabMapping;
  tabUnitMapping typeTabMapping;
  numUnitIndex   Number;
  i              Number;
  j              Number;
  charCurrentNum Char(1);
Begin
  If Money Is Null Then
    Return Null;
  End If;
  strYuan := TO_CHAR(FLOOR(Money));
  If strYuan = '0' Then
    numLenYuan := 0;
    strYuanFen := lpad(TO_CHAR(FLOOR(Money * 100)), 2, '0');
  Else
    numLenYuan := length(strYuan);
    strYuanFen := TO_CHAR(FLOOR(Money * 100));
  End If;
  If strYuanFen = '0' Then
    numLenYuanFen := 0;
  Else
    numLenYuanFen := length(strYuanFen);
  End If;
  If numLenYuan = 0 Or numLenYuanFen = 0 Then
    strRst := '零圆整';
    Return strRst;
  End If;
  tabNumMapping(0) := '零';
  tabNumMapping(1) := '壹';
  tabNumMapping(2) := '贰';
  tabNumMapping(3) := '叁';
  tabNumMapping(4) := '肆';
  tabNumMapping(5) := '伍';
  tabNumMapping(6) := '陆';
  tabNumMapping(7) := '柒';
  tabNumMapping(8) := '捌';
  tabNumMapping(9) := '玖';
  tabUnitMapping(-2) := '分';
  tabUnitMapping(-1) := '角';
  tabUnitMapping(1) := '';
  tabUnitMapping(2) := '拾';
  tabUnitMapping(3) := '佰';
  tabUnitMapping(4) := '仟';
  tabUnitMapping(5) := '万';
  tabUnitMapping(6) := '拾';
  tabUnitMapping(7) := '佰';
  tabUnitMapping(8) := '仟';
  tabUnitMapping(9) := '亿';
  For i In 1 .. numLenYuan Loop
    j            := numLenYuan - i + 1;
    numUnitIndex := Mod(i, 8);
    If numUnitIndex = 0 Then
      numUnitIndex := 8;
    End If;
    If numUnitIndex = 1 And i > 1 Then
      strRstYuan := tabUnitMapping(9) || strRstYuan;
    End If;
    charCurrentNum := substr(strYuan, j, 1);
    If charCurrentNum <> 0 Then
      strRstYuan := tabNumMapping(charCurrentNum) ||
                    tabUnitMapping(numUnitIndex) || strRstYuan;
    Else
      If (i = 1 Or i = 5) Then
        If substr(strYuan, j - 3, 4) <> '0000' Then
          strRstYuan := tabUnitMapping(numUnitIndex) || strRstYuan;
        End If;
      Else
        If substr(strYuan, j + 1, 1) <> '0' Then
          strRstYuan := tabNumMapping(charCurrentNum) || strRstYuan;
        End If;
      End If;
    End If;
  End Loop;
  For i In -2 .. -1 Loop
    j              := numLenYuan - i;
    charCurrentNum := substr(strYuanFen, j, 1);
    If charCurrentNum <> '0' Then
      strRstFen := tabNumMapping(charCurrentNum) || tabUnitMapping(i) ||
                   strRstFen;
    End If;
  End Loop;
  If strRstYuan Is Not Null Then
    strRstYuan := strRstYuan || '圆';
  End If;
  If strRstFen Is Null Then
    strRstYuan := strRstYuan || '整';
  Elsif length(strRstFen) = 2 And substr(strRstFen, 2) = '角' Then
    strRstFen := strRstFen || '整';
  End If;
  strRst := strRstYuan || strRstFen;
  --strRst := Replace(strRst, '亿零', '亿');
  --strRst := Replace(strRst, '万零', '万');
  Return strRst;
End Money2Chinese;
```

