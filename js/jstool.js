//���class��ʽ
function addClass(ele,classname){
	if(!hasClass(ele,classname)){
		ele.className += ' '+classname; 
	}
}

//ɾ��ĳһԪ����ָ����class����
function removeClass(ele,classname){
	var reg = new RegExp('\\b'+classname+'\\b');
	ele.className = ele.className.replace(reg,"");
}

//��Ԫ�ش��ڸ�class��ɾ�����������ڸ�class�����
function toggleClass(ele,classname){
	if(hasClass(ele,classname)){
		removeClass(ele,classname);
	}else{
		addClass(ele,classname);
	}
}

//�ж��Ƿ���ĳһ��class��ʽ
function hasClass(ele,classname){
	var reg = new RegExp('\\b'+classname+'\\b');
	return reg.test(ele.className);
}

//��ȡ��ǰ������������
function getBrowserType(){
    var ua = navigator.userAgent;
    if(/firefox/i.test(ua)){
        return "firefox";//��������
    }else if(/chrome/i.test(ua)){
        return "chrome";//chrome�����
    }else if('ActiveXObject' in window){
        return "IE";//IE�������ͨ���ж����������������	
    }
}

//eleҪ��ȡ��ʽ��Ԫ��,nameҪ��ȡ����ʽ��
function getStyle(ele,name){
    //�ж�window�������Ƿ��д�����getComputedStyle
    if(window.getComputedStyle){
        return getComputedStyle(ele, null)[name];
    }else{
        return ele.currentStyle[name];
    }

    //return window.getComputedStyle?getComputedStyle(ele, null)[name]:ele.currentStyle[name];
    
    /*if(ele.currentStyle){//��ʱ��IE9���ϻ�����ʹ��currentStyle
		return ele.currentStyle[name];
	}else{
		return getComputedStyle(ele, null)[name];
	}*/
}

//obj Ҫ�󶨵�Ԫ��
//eventStr ���¼����ַ���
//callback �ص�����
function bind(obj,eventStr,callback){
    if(obj.addEventListener){
        obj.addEventListener(eventStr, callback,false);	
    }else{
        //����IE8,��ͳһthis����
        obj.attachEvent('on'+eventStr,function(){
            //�Լ������������е��ûص����������޸�this
            callback.call(obj);
        });	
    }
}

//obj �¼��󶨵�Ԫ��
//eventStr ���¼����ַ���
//callback �ص�����
function removeEvent(obj,eventStr,callback){
    if(obj.removeEventListener){
        obj.removeEventListener(eventStr, callback, false)
    }else{
        //����IE8,��ͳһthis����
        obj.detachEvent('on'+eventStr,function(){
            //�Լ������������е��ûص����������޸�this
            callback.call(obj);
        });	
    }
}