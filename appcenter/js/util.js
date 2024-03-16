// html include
function includeHTML(divContainer, urlHTML) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
    	if (this.readyState == 4) {
    		if (this.status == 200) {
    			divContainer.innerHTML = this.responseText;
    		}
    		if (this.status == 404) {
    			divContainer.innerHTML = "Page not found.";
    		}
    	}
    }
    xhttp.open("GET", urlHTML, true);
    xhttp.send();
	return this.status == 200;
}

// script include
function includeJS(urlJs) {
	var element = document.createElement("script");
	element.src = urlJs;
	document.body.appendChild(element);
}

//숫자 컴마 추가
function commaAdd(amt) {
	let str = String(amt);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

//숫자 컴마 제거
function commaRemove(amt) {
    let str	= String(amt);
    return str.replace(/[^\d]+/g, '');
}

//숫자 입력값 포맷
function inputNumberFormat(obj) {
    obj.value=commaAdd(commaRemove(obj.value));
}

//하이픈 자동 입력
function OnCheckPhone(oTa){
    var oForm		= oTa.form;
    var sMsg		= oTa.value;
    var onlynum		= ""; 
    var imsi		= 0;
    onlynum			= RemoveDash2(sMsg);	// 하이픈 입력시 자동으로 삭제함 
    onlynum			= checkDigit(onlynum);	// 숫자만 입력받게 함
    var retValue	= ""; 
    if(event.keyCode != 12){ 
        if(onlynum.substring(0,2) == '02'){	//서울전화번호일 경우  10자리까지만 나타나교 그 이상의 자리수는 자동삭제
        	if(GetMsgLen(onlynum) <= 1) oTa.value = onlynum;
        	if(GetMsgLen(onlynum) == 2) oTa.value = onlynum + "-";
        	if(GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,3);
        	if(GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,4);
        	if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5);
        	if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6);
        	if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5) + "-" + onlynum.substring(5,7);
        	if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,8);
        	if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5) + "-" + onlynum.substring(5,9);
        	if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10);
        	if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10);
        	if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10); 
        } 
        if(onlynum.substring(0,2) == '05'){//05로 시작되는 번호 체크
        	if(onlynum.substring(2,3) == 0) {//050으로 시작되는지 따지기 위한 조건문
            	if(GetMsgLen(onlynum) <= 3) oTa.value = onlynum;
            	if(GetMsgLen(onlynum) == 4) oTa.value = onlynum + "-";
            	if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,5);
            	if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,6);
            	if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7);
            	if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8);
            	if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) + "-" + onlynum.substring(7,9);
            	if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,10);
            	if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) + "-" + onlynum.substring(7,11);
            	if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,12);
            	if(GetMsgLen(onlynum) == 13) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,12);
            }else{
            	if(GetMsgLen(onlynum) <= 2) oTa.value = onlynum; 
                if(GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-"; 
                if(GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4); 
                if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5); 
                if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6); 
                if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7); 
                if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,8);
                if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9); 
                if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10); 
                if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11); 
                if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11);
            } 
        }
        
        if(onlynum.substring(0,2) == '03' || onlynum.substring(0,2) == '04' || onlynum.substring(0,2) == '06' || onlynum.substring(0,2) == '07' || onlynum.substring(0,2) == '08'){// 서울전화번호가 아닌 번호일 경우(070,080포함 // 050번호가 문제군요) 
        	if(GetMsgLen(onlynum) <= 2) oTa.value = onlynum;
        	if(GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-";
        	if(GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4);
        	if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5);
        	if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6);
        	if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7);
        	if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,8);
        	if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9);
        	if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10);
        	if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11);
        	if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11); 

        } 
        if(onlynum.substring(0,2) == '01') {  //휴대폰일 경우 
            if(GetMsgLen(onlynum) <= 2) oTa.value = onlynum; 
            if(GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-"; 
            if(GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4); 
            if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5); 
            if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6); 
            if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7); 
            if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,8); 
            if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9); 
            if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10); 
            if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11); 
            if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11); 
        } 

        if(onlynum.substring(0,1) == '1') {  // 1588, 1688등의 번호일 경우 
            if(GetMsgLen(onlynum) <= 3) oTa.value = onlynum; 
            if(GetMsgLen(onlynum) == 4) oTa.value = onlynum + "-"; 
            if(GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,5); 
            if(GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,6); 
            if(GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7); 
            if(GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8); 
            if(GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8); 
            if(GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8); 
            if(GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8); 
            if(GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8); 
        }
    }
}
function RemoveDash2(sNo){
	let reNo = "";
	for(var i=0; i<sNo.length; i++){
		if(sNo.charAt(i) != "-")reNo += sNo.charAt(i);
	}
	return reNo;
}
function GetMsgLen(sMsg) { // 0-127 1byte, 128~ 2byte
	let count = 0;
	for(var i=0; i<sMsg.length; i++){
		if(sMsg.charCodeAt(i) > 127)count += 2;
		else						count++;
	}
	return count;
}

// 숫자외 제거
function checkDigit(num) { 
    var Digit	= "1234567890",
    	string	= num,
    	len		= string.length,
    	retVal	= "";
    for (i=0; i<len; i++) {
        if (Digit.indexOf(string.substring(i, i+1))>=0) {
	retVal=retVal+string.substring(i, i+1);
	}
    }
    return retVal;
}

//인풋 입력값 숫자 체크
function inputNumberAll(obj){
	let inpurNumber		= obj.value;// 입력 숫자
	let changeNumber	= checkDigit(inpurNumber);// 숫자체크
	obj.value			= changeNumber;// 재입력
}

//인풋 입력값 숫자 체크(0 허용안함)
function inputNumber(obj){
	let inpurNumber		= obj.value;// 입력 숫자
	let changeNumber	= checkDigit(inpurNumber);// 숫자체크
	if(changeNumber == 0)changeNumber = "";// 0이 되면 공백처리
	obj.value			= changeNumber;// 재입력
}

// 인풋 입력값 숫자 체크
function inputCheckNumber(obj){
	let inpurNumber		= obj.value;// 입력 숫자
	let changeNumber	= checkDigit(commaRemove(inpurNumber));// 컴마 제거후 숫자체크후 컴마추가
	changeNumber		= commaAdd(Number(changeNumber));
	if(changeNumber == 0)changeNumber = "";// 0이 되면 공백처리
	obj.value			= changeNumber;// 재입력
}

// 이벤트 전달 중단
function eventStop(event){
	event.preventDefault();				//현재 이벤트의 기본 동작을 중단
	event.stopPropagation();			//현재 이벤트가 상위로 전파되지 않도록 중단
	event.stopImmediatePropagation();	//현재 이벤트가 상위뿐 아니라 현재 레벨에 걸린 다른 이벤트도 동작 중단
}

/**
 * 바이트 문자 입력가능 문자수 체크
 * 
 * @param id : tag id 
 * @param title : tag title
 * @param mb : 최대 입력가능 수 (MB)
 * @returns {Boolean}
 */
function maxByteCheck(str, mb){
	if (Number(byteCheck(str)) > Number(mb) * 1024 * 1024) {
		//popupShow('최대 용량을 초과 하였습니다.(최대 용량 : ' + mb + 'MB)', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Exceeded maximum capacity. (Maximum capacity: ' + mb + 'MB)', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	} else {
		return true;
	}
}
 
/**
 * 바이트수 반환  
 * 
 * @param el : tag jquery object
 * @returns {Number}
 */
function byteCheck(str){
    let codeByte = 0;
    for (var idx = 0; idx < str.length; idx++) {
    	var oneChar = escape(str.charAt(idx));
    	if (oneChar.length == 1) {
		codeByte++;
    	} else if (oneChar.indexOf("%u") != -1) {
		codeByte += 2;
    	} else if (oneChar.indexOf("%") != -1) {
		codeByte++;
	}
    }
    return codeByte;
}

/**
 * contextPath
 * 
 * @returns
 */
function getContextPath(){
	var hostIndex = location.href.indexOf(location.host) + location.host.length;
	return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

/**
 * %d전 데이트타입 포멧
 * 
 * @param dt
 * @returns
 */
function getNowDayTimeToFormat(dt) {
	var min		= 60000;
	var c		= new Date();// 현재 시간
	var d		= new Date(dt);// 체크할 시간
	var minsAgo	= Math.floor((c - d) / (min));

	var result = moment(dt).format('YYYY-MM-DD');
	if (minsAgo == 0) {
		//return '방금 전';
		return 'Just now';
	}
	if (minsAgo < 60) {
		//return minsAgo + '분 전';// 1시간 내
		return minsAgo + ' minutes ago'; // Within the last hour
	}
	if (minsAgo < 1440) {
		//return Math.floor(minsAgo / 60) + '시간 전';// 하루 내
		return Math.floor(minsAgo / 60) + ' hours ago'; // Within the last day
	}
	return result;
};

function getCookie(cookie_name) {
  var x, y;
  var val = document.cookie.split(';');

  for (var i = 0; i < val.length; i++) {
    x = val[i].substr(0, val[i].indexOf('='));
    y = val[i].substr(val[i].indexOf('=') + 1);
    x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기
    if (x == cookie_name) {
      return unescape(y); // unescape로 디코딩 후 값 리턴
    }
  }
}

/** 쿠키를 설정한다.
 *
 * @param name 쿠키 이름
 * @param value 쿠키 값
 * @param max_age null 을 입력하면 일시적인 쿠키를 생성한다. 0 보다 큰 값을 입력하면 쿠키 지속 시간을 포함한 쿠키를 생성한다. 0 을 입력하면 쿠키를 삭제한다.
 * @same_site 쿠키 보안
 */
function SetCookie( name, value, max_age, same_site )
{
	var strCookie = name + "=" + encodeURIComponent(value);
	if( typeof max_age === "number" )
	{
		strCookie += "; max-age=" + max_age;
	}
	if ( typeof same_site === 'string')
	{
		strCookie += "; SameSite=" +same_site;
	}

	// QQQ: path, domain 유효범위를 설정하고 싶으면 여기서 strCookie 변수에 추가해 주어야 한다.
	document.cookie = strCookie;
}

/** 쿠키를 가져온다.
 *
 * @param name 쿠키 이름
 */
function GetCookie( name ) {
	var value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
	return value? value[2] : null;
}

/** 쿠키를 삭제한다.
 *
 * @param name 쿠키 이름
 */
function DelCookie( name ) {
	document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
}
