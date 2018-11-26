### 1.码值操作的函数

```java
//获取码值
String value = getCodeValue("AAC004", "男");
//获取码值描述值
String aac004 = getCodeDesc("AAC004", "2", user.getOrgId());
//获取aac004的所有码值
List<AppCode> codes = CodeTableLocator.getCodeList("AAC004", user.getYab003());
```

### 2.ta3框架拦截的异常

```java
AppException,PrcException,IllegalArgumentException,BaseException
```

```xml

<prop key="get*">PROPAGATION_REQUIRED,readOnly,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="query*">PROPAGATION_REQUIRED,readOnly,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="select*">PROPAGATION_REQUIRED,readOnly,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="save*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="insert*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="update*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="delete*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="remove*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
				<prop key="*">PROPAGATION_REQUIRED,-AppException,-PrcException,-IllegalArgumentException,-BaseException</prop>
```

### 3.ta3显示Service层的提示信息

在service层抛出异常，在Controller捕获异常

```java
//service层
throw new AppException("数据已存在，请不要重复导入!");
```

```java
//controller
try{
    ...
}catch(Exception e){
    setMsg("导入失败:"+e.getMessage(),"error");
}
```

### 4.在`<panel>`上添加按钮

```js
var htm = [];
htm.push("<div id='bottonGroup' style='position: absolute;right:20px;top:7px;'>");

<!-- 按钮 -->
htm.push("<button id=\"btnQuery\" class=\"sexybutton_163\" type=\"button\" onclick=\"fnAddAgency()\" style=\"margin-right:4px;\"><span class=\"button_span\">");
htm.push("<span style=\"height:16px;width:16px;padding-left: 0px;float: left;margin-top: 4px;margin-right:5px;\" class=\"icon-add\"></span>新增代理</span></button>");

htm.push("<button id=\"btnDisagent\" class=\"sexybutton_163\" type=\"button\" onclick=\"fnDisagent()\" style=\"margin-right:4px;\"><span class=\"button_span\">");
htm.push("<span style=\"height:16px;width:16px;padding-left: 0px;float: left;margin-top: 4px;margin-right:5px;\" class=\"icon-redo\"></span>已解除代理</span></button>");

htm.push("</div>");
$("#queryResult").children("div").after(htm.join(""));
```

### 5.RPC的使用

```sql
<!--查询代理得单位-->
<select id="getAgentUnit" parameterClass="map" resultClass="java.util.HashMap">
    SELECT DISTINCT
    (   ''''||a.yuj411||''''<!-- 单位代码-->
     ||','''||b.aab004||''''<!-- 单位名称 -->
     ||','''||a.aab001||''''<!-- 单位编号 -->
     ||','''||a.ycbs20||''''<!-- 代理协议流水号 -->
    ) as agentunit
    FROM uj41 a
    join ab01 b on a.aab001 = b.aab001
    WHERE b.aab004 like '%'||#aab004#||'%'
    <![CDATA[and rownum <= 20]]>
</select>
```

```java
//构建RPC的数据结构
@RequestMapping("personSelectController!getAgentUnit.do")
public String getAgentUnit() throws Exception{
    TaParamDto dto=getTaDto();
    StringBuilder sb =  new StringBuilder();
    sb.append("new Array(new Array('单位代码','代理单位','代理协议流水号','单位编号')");
    try{
        //查询人员信息
        List list=getDao().queryForList("agent.getAgentUnit",dto); // aab004 代理的单位名称
        //查询list集合，然后拼接二位数组
        if (ValidateUtil.isNotEmpty(list)){
            for (int i=0;i<list.size();i++) {
                sb.append(",new Array(");
                sb.append((String)((Map)list.get(i)).get("agentunit"));
                sb.append(")");
            }
        }
    } catch (Exception e){
        e.printStackTrace();
        setMessage("RPC数据构建异常","error");
    }
    sb.append(")");
    setData("data",sb.toString());
    return JSON;
}
```

```html
<ta:text  id="yuj443" key="转往单位"  required="true" onChange="getAgentUnitInfo()"  labelWidth="80" placeholder="请输入单位名称查询"/>
```

```js
//获取RPC数据
function getAgentUnitInfo(){
    var url = '<%=basePath%>common/personSelectController!getAgentUnit.do';
    var param = {'dto.aab004':Base.getValue('yuj443')};
    fnRpcGetAgentUintInfo(url, param);
}

function fnRpcGetAgentUintInfo(url,param) {
    if(null == url || '' == url){
        Base.alert('URL地址不能为空！');
        return ;
    }
    suggestQuery('1',url,param);
}

//初使化面板
function initRpcAgentUintGrid(id,width,height,col,successCallBack){
    initializeSuggestFramework(1,"",id,width,height,col,successCallBack,1,false);
}

initRpcAgentUintGrid("yuj443",600,150,2,function (){
    //回调函数回写数据值
    Base.setValue("yuj411",g_Suggest[0]);
    Base.setValue("yuj443",g_Suggest[1]);
    Base.setValue("zwaab001",g_Suggest[2]);
    Base.setValue("zwycbs20",g_Suggest[3]);
});
```

```js
//获取RPC数据
function getAgentUnitInfo(){
    var url = '<%=basePath%>common/personSelectController!getAgentUnit.do';
    var param = {'dto.aab004':Base.getValue('yuj443')};
    suggestQuery('1','<%=basePath%>common/personSelectController!getAgentUnit.do',{'dto.aab004':Base.getValue('yuj443')});
}

//设置面板
initializeSuggestFramework(1,"","yuj443",600,150,2,function (){
    Base.setValue("yuj411",g_Suggest[0]);
    Base.setValue("yuj443",g_Suggest[1]);
    Base.setValue("zwaab001",g_Suggest[2]);
    Base.setValue("zwycbs20",g_Suggest[3]);
},1,false);
```

### 6.PL/SQL的码值转化函数

```plsql
-- 获取码值的描述值 prm_codetype：码值字段  prm_codevalue：码值的值
create or replace function fun_getcodedesc(prm_codetype  in aa10.aaa100%type,
                                           prm_codevalue in aa10.aaa102%type)
  return varchar2 is
  /*变量声明*/
  s_codedesc varchar2(1999); -- 代码值名称
begin
  /*初始化变量*/
  s_codedesc := null;

  if upper(prm_codetype) in ('AAA020','AAB078') then -- 行政区划
    select aaa021
      into s_codedesc
      from aa12
     where aaa020 = prm_codevalue;
  else
    select aaa103
      into s_codedesc
      from aa10a1
     where aaa100 = prm_codetype
       and aaa102 = prm_codevalue;
  end if;

  if s_codedesc is null or s_codedesc = '' then
    s_codedesc := prm_codevalue;
  end if;
  return s_codedesc;
exception
  when others then
    return prm_codevalue;
end fun_getcodedesc;
```

```plsql
-- 获取码值的描述值 prm_codetype：码值字段  prm_codevalue：码值的值
create or replace function fun_getcodevalue ( /*转码函数*/
      prm_codetype                       in       aa10.aaa100%type  ,
      prm_codevalue                      in       aa10.aaa103%type  )
   return varchar2
   is
      /*变量声明*/
      s_codedesc                       varchar2(1999)  ;   -- 代码值名称
   begin
      /*初始化变量*/
      s_codedesc     := null                   ;

      select aaa102
        into s_codedesc
        from aa10a1
       where aaa100 = prm_codetype
         and aaa103 = prm_codevalue;
      if s_codedesc is null or s_codedesc =''
         then  s_codedesc := prm_codevalue;
      end if;
      return s_codedesc;
   exception
      when others then
         return prm_codevalue;
   end fun_getcodevalue ;
```

### 7.存储过程的调用

```java
PrcDTO prcDtro = new PrcDTO();
//ycbs20代理协议流水号
prcDtro.put("prm_ycbs20",ycbs20);
//费款所属期
prcDtro.put("prm_aae002",aae002);
//缴纳开始年月
prcDtro.put("prm_aae041",aae041);
//缴纳结束年月
prcDtro.put("prm_aae042",aae042);
//UJ415单位开户费及综合服务费(元/年)
prcDtro.put("prm_yuj415",yuj415);
//合计金额
prcDtro.put("prm_aae019",aae019);
//备注
prcDtro.put("prm_aae013",aae013);
//经办人姓名
prcDtro.put("prm_aae011",aae011);
//经办人编号
prcDtro.put("prm_yae116",yae116);
//经办机构
prcDtro.put("prm_aae017",aae017);

dao.callPrc("agent.companyAccountPrc", prcDtro);
```

```xml
<!-- 生成单位开户费及综合服务费收支通知单参数 -->
<parameterMap id="companyAccountPrcParam" class="java.util.Map">
<parameter property="prm_ycbs20" jdbcType="NUMERIC" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_aae002" jdbcType="NUMERIC" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_aae041" jdbcType="NUMERIC" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_aae042" jdbcType="NUMERIC" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_yuj415" jdbcType="NUMERIC" javaType="java.math.BigDecimal" mode="IN"/>
<parameter property="prm_aae019" jdbcType="NUMERIC" javaType="java.math.BigDecimal" mode="IN"/>
<parameter property="prm_aae013" jdbcType="VARCHAR" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_aae011" jdbcType="VARCHAR" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_yae116" jdbcType="NUMERIC" javaType="java.lang.String" mode="IN"/>
<parameter property="prm_aae017" jdbcType="VARCHAR" javaType="java.lang.String" mode="IN"/>
<parameter property="AppCode" jdbcType="VARCHAR" javaType="java.lang.String" mode="OUT" nullValue="NOERROR"/>
<parameter property="ErrorMsg" jdbcType="VARCHAR" javaType="java.lang.String" mode="OUT" nullValue=""/>
</parameterMap>
<!-- 生成单位开户费及综合服务费收支通知单 -->
<procedure id="companyAccountPrc" parameterMap="companyAccountPrcParam">
{call pkg_jyrc_dl.prc_dwzhfwf(?,?,?,?,?,?,?,?,?,?,?,?)}
</procedure>
```

> 注意：AppCode和ErrorMsg格式固定，且区分大小写。
>
> 注意：AppCode在过程中没有异常必须设置`NOERROR`

```sql
prm_AppCode := 'NOERROR';
```

### 8.设置表格（datagrid）显示不同的颜色

```js
<ta:datagrid id="companyAgencyDg" fit="true" haveSn="true" rowColorfn="fnColor">

function fnColor(data){
    if (data._row_ % 2 == 0) {
    	return 'red';
    }else{
    	return 'bule';
    }
}
```

### 9.Ta3配置文件的加载顺序

```xml
web.xml --> app-context.xml --> 1.spring-cfg-include.xml 2. spring-ibatis.xml 3.ta的配置文件 4.系统的配置文件 --> 加载SqlmapConfig-*.xml / spring-*.xml -->sqlmap-*.xml
```

### 10.设置`<ta:select>`的值

* 方式一

```xml
<select id="getSelectUnit" paramterClass="map" resultClass="hashmap">
	select
        a.aab001 as id,  <!-- 单位编号 -->
        a.aab004 as name <!-- 单位名称 -->
    from ab01
</select>
```

```java
RequestMapping("personageAgencyController!transferCompanies.do")
public String transferCompanies() throws IOException{
    TaParamDto dto = getTaDto();
    List list = personageAgencyService.queryForList("agent.getSelectUnit", dto);
    String json =JSONArray.fromObject(list).toString();
    writeJsonToClient(json);
    return null;
}
```

```js
function fnQueryCompany(){
    var yuj443 = Base.getValue("yuj443");
    Base.loadSelectInputData("yuj443","personageAgencyController!transferCompanies.do",{"dto.yuj443":yuj443});
}
```

* 方式二

```xml
<select id="getHtInfo" parameterClass="map" resultClass="java.util.HashMap">
    SELECT 
        ycbs01 as id,    <!-- id值 -->
        ycbs02 as name   <!-- 显示值 -->
    FROM cbs5
</select>
```

```java
@RequestMapping("socialDataInterfaceController!toImportPersonDetails.do")
public String toImportPersonDetails() throws Exception{
    TaParamDto dto = getTaDto();
    List list = getDao().queryForList("cbs5.getHtInfo");
    setSelectInputList("ycbs01",list);
    return "jyrc/si/Interface/fiveInsurance/socialPersonDetailsMain";
}
```

```html
<ta:selectInput id="ycbs01" key="户头名称" labelWidth="80"/>
```

### 11.移动表格的数据值

* 双击数据行移动数据

```js
//移动表格的数据行
function fnMovwDatagridRow(e,rowdata){
    //删除属性
    delete rowdata.grid;
    var originGrid = "agencyMemberDg";
    var targetGrid = "companyAgencyDg";
    //向目标表格添加数据
    Base.addGridRowDown(targetGrid,rowdata);
    //删除原始表格的数据
    Base.deleteGridRow(originGrid,rowdata.row);
    //刷新表格
    Base.refreshGrid(originGrid);
    Base.refreshGrid(targetGrid);
}
```

* 批量移动所有选中的数据行

```js
//移动表格所有选中的数据
function fnMoveAllSelected(){
    var originGrid = "agencyMemberDg";
    var targetGrid = "companyAgencyDg";
    //获取所有选中的数据
    var originRows = Base.getGridSelectedRows(originGrid); 
    //将所有选中的数据添加到目标表格
    for(var i=0;i<originRows.length;i++){
    	Base.addGridRowDown(targetGrid,originRows[i]);
    }
    //删除原始表格的数据
    Base.deleteGridSelectedRows(originGrid);
    //刷新数据
    Base.refreshGrid(originGrid);
    Base.refreshGrid(targetGrid);
}
```

### 12.回车自动查询

查询条件中回车默认查询数据并跳转到下一个条件

```js
 //进入页面是先查出默认数据
 function initFocusAndEnterQuery2(queryFn,focusIndex,needFileNowDate){
     queryFn();
     initFocusAndEnterQuery(queryFn,focusIndex,needFileNowDate);
 }

/**
 * @param queryFn    回车需要触发的查询函数
 * @param focusIndex  需要聚焦第几个可见输入框,不传默认为第一个可见输入框
 * @param needFileNowDate 是否需要填写找到的第一个日期框(可为年份和期号)为当前日期,默认为否
 */
function initFocusAndEnterQuery(queryFn,focusIndex,needFileNowDate){
	var noFill=true;
    //设置光标的默认位置
	if(focusIndex!=null && !isNaN(focusIndex)){
		//聚焦第一个
		$('input:visible').get(Number(focusIndex)-1).focus();
	}else{
		//聚焦第一个
		$('input:visible').get(0).focus();
	}
    
	try{
		if(needFileNowDate==null || needFileNowDate==false){
			
		}else{
			//年份
			if($('.textinput.dateYearfield').eq(0).attr("readonly")!=true){
				if($('.textinput.dateYearfield').size()>0){
					$('.textinput.dateYearfield').eq(0).val(Ta.util.getCurDateYear());
					noFill=false;
				}
			}
			//期号
			if(noFill && $('.textinput.issuefield').eq(0).attr("readonly")!=true){
				if($('.textinput.issuefield').size()>0){
					$('.textinput.issuefield').eq(0).val(Ta.util.getCurIssue());
					noFill=false;
				}
			}

			//日期
			if(noFill && $('.textinput.datefield').eq(0).attr("readonly")!=true){
				if($('.textinput.datefield').size()>0){
					$('.textinput.datefield').eq(0).val(Ta.util.getCurDate());
					noFill=false;
				}
			}	
		}
	}catch(err){
		
	}
    
	//绑定回车查询事件
	$("input").keydown(function(e) {  
          if (e.keyCode == '13') {  
        	  if(queryFn!=null){
        		  queryFn();
        	  }
          }  
     });  
}
```

```js
initFocusAndEnterQuery2(fnQuery,2);
```

### 13.在窗口中再开窗口

在窗口中再开窗口，新开的窗口不是在窗口内部，通过在父窗口开启

```html
<ta:text id="yuj031" key="档案号" labelWidth="80" onChange="getPrepareAgent()" 
placeholder="输入档案号按回车查询" clickIcon="icon-search" clickIconFn="selectPerson()"/>
```

```js
//在父窗口打开窗口，放在当前窗口的顶层
function selectPerson(){
    var param={"dto.yuj031":Base.getValue("yuj031")};
    parent.Base.openWindow("selectPerson","待代理的人员<span style='color:red;'>(双击选择数据)</span>","personageAgencyController!toToTheAgent.do",param,"1100","460",null,filldata,true);
}
```

```js
//回显的值
function filldata(){
    data=parent.window.person;
    if(data != null ){
        Base.setValue("yuj031",data.yuj031);
        Base.setValue("aac003",data.aac003);
        Base.setValue("aac002",data.aac002);
        Base.setValue("aac004",data.aac004);
        Base.setValue("aac024",data.aac024);
        Base.setValue("yac145",data.yac145);
        Base.setValue("aac011",data.aac011);
        Base.setValue("yuj215",data.yuj215);
        Base.setValue("aac183",data.aac183);
        Base.setValue("yuj217",data.yuj217);
        Base.setValue("yuj210",data.yuj210);
        Base.setValue("aac001",data.aac001);
    }
}
```

### 14.Ta3的IDEA模板定义

```java

```

### 15.在`<tabs>`上新增按钮

```xml
var buttonStr = "<div style='position: absolute;right:20px;top:7px;'>";
buttonStr+="<button id='btnNew' type='button' onclick='fnAddPerson()' class='sexybutton_163'>";
buttonStr+="<span class='button_span'>新增代理人员</span>";
buttonStr+="</button>";
buttonStr+="</div>";
$($("#tabs").children("div").get(0)).after(buttonStr);
```

### 16.设置grid奇数行为红色，偶数行为蓝色

```js
<ta:datagrid id="companyAgencyDg" fit="true" haveSn="true" rowColorfn="fnColor">

function fnColor(data){
    if (data._row_ % 2 == 0) {
    	return 'red';
    }else{
    	return 'bule';
    }
}
```

### 17.Ta3配置文件的加载顺序

```
web.xml --> app-context.xml --> 1.spring-cfg-include.xml 2. spring-ibatis.xml 3.ta的配置文件 4.系统的配置文件 --> 加载SqlmapConfig-*.xml / spring-*.xml -->
sqlmap-*.xml
```

### 18.权限控制

- 建立的`xxxController`必须添加菜单，才会有权限
- 若是在页面内部，没有单独的菜单，可配置为隐藏菜单
- 一些不需要权限检验的资源或请求，可在配置文件中配置

### 19.菜单ID（MeunId）

- 后台获取MenuId

```java
WebUtil.getCurrentMenu(getRequest()).getMenuid()
```

- 页面获取MenuId

```js
Base.globvar.currentMenuId
```

- 若是`window.location`或`window.open()`请求时，需重新传入`MenuId`，更新到session中

```js
//根据ids拼接传递的条件字符串
if (queryStr == "") {
    queryStr += "___businessId=" + Base.globvar.currentMenuId;
} else {
    queryStr += "&___businessId=" + Base.globvar.currentMenuId;
}
```

```js
/**
 * window.location请求添加上菜单id
 * @param url
 */
function winLocation(url) {
    if(url.indexOf("?") != -1){
        url += "&___businessId=" + Base.globvar.currentMenuId;
    }else{
        //没有?
        url += "?___businessId=" + Base.globvar.currentMenuId;
    }
    location.href = url;
}
```

### 20.下拉树`<selectTree>`

```sql
SELECT
   a.id     as id    ,   <!--id-->
   a.pid    as pid   ,   <!--父id-->
   a.name   as name  ,   <!--名称-->
   a.isparent as isparent,   <!--是否是父节点-->
   a.type   as type  ,   <!--类型-->
   a.icon   as icon  ,   <!--图标-->
   a.open   as open      <!--是否展开-->
FROM apptreecode a
```

```java
@RequestMapping("commonSelectController!getTreeData.do")
@ResponseBody
public String getTreeData() throws Exception {
    //从缓存中获取机构数
    List list = getTreeListDataByCodeType("CAB003");
    List treeList = new ArrayList();
    for (int i = 0; i < list.size(); i++) {
        ApptreecodeDomain domain = (ApptreecodeDomain) list.get(i);
        //设置展开
        domain.setOpen("true");
        treeList.add(domain);
    }
    String json = JSonFactory.bean2json(treeList);
    writeJsonToClient(json);
    return null;
}
```

```jsp
<ta:selectTree url="${basePath}archive/common/commonSelectController!getTreeData.do" treeId="t1" targetDESC="cab003_desc" targetId="cab003" key="经办机构"/>
```

- `treeId`树的id
- `targetId`向后台传参的key的名称
- `targetDESC`向后台传参的value的名称

### 21.设置`rediogroup`的默认选中一个

```html
<ta:radiogroup cols="10" span="7" id="ycbs12" key="收费方式" collection="ycbs12"/>
```

```js
 Base.getObj("ycbs12").setValue("1");
```

### 22.`window.location.href`重定向还是当前页

```
去除<ta：form>标签
```

