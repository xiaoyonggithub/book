//添加class样式
function addClass(ele,classname){
	if(!hasClass(ele,classname)){
		ele.className += ' '+classname; 
	}
}

//删除某一元素中指定的class属性
function removeClass(ele,classname){
	var reg = new RegExp('\\b'+classname+'\\b');
	ele.className = ele.className.replace(reg,"");
}

//若元素存在该class就删除，若不存在该class就添加
function toggleClass(ele,classname){
	if(hasClass(ele,classname)){
		removeClass(ele,classname);
	}else{
		addClass(ele,classname);
	}
}

//判断是否有某一个class样式
function hasClass(ele,classname){
	var reg = new RegExp('\\b'+classname+'\\b');
	return reg.test(ele.className);
}

//获取当前游览器的类型
function getBrowserType(){
    var ua = navigator.userAgent;
    if(/firefox/i.test(ua)){
        return "firefox";//火狐浏览器
    }else if(/chrome/i.test(ua)){
        return "chrome";//chrome浏览器
    }else if('ActiveXObject' in window){
        return "IE";//IE浏览器，通过判断浏览器的特有属性	
    }
}

//ele要获取样式的元素,name要获取的样式名
function getStyle(ele,name){
    //判断window对象中是否有此属性getComputedStyle
    if(window.getComputedStyle){
        return getComputedStyle(ele, null)[name];
    }else{
        return ele.currentStyle[name];
    }

    //return window.getComputedStyle?getComputedStyle(ele, null)[name]:ele.currentStyle[name];
    
    /*if(ele.currentStyle){//此时在IE9以上会优先使用currentStyle
		return ele.currentStyle[name];
	}else{
		return getComputedStyle(ele, null)[name];
	}*/
}

//obj 要绑定的元素
//eventStr 绑定事件的字符串
//callback 回调函数
function bind(obj,eventStr,callback){
    if(obj.addEventListener){
        obj.addEventListener(eventStr, callback,false);	
    }else{
        //兼容IE8,并统一this对象
        obj.attachEvent('on'+eventStr,function(){
            //自己在匿名函数中调用回调函数，并修改this
            callback.call(obj);
        });	
    }
}

//obj 事件绑定的元素
//eventStr 绑定事件的字符串
//callback 回调函数
function removeEvent(obj,eventStr,callback){
    if(obj.removeEventListener){
        obj.removeEventListener(eventStr, callback, false)
    }else{
        //兼容IE8,并统一this对象
        obj.detachEvent('on'+eventStr,function(){
            //自己在匿名函数中调用回调函数，并修改this
            callback.call(obj);
        });	
    }
}