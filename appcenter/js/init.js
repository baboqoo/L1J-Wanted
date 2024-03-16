/*!
 * project : init
 * author : LINOFFICE
 * 디바이스 환경 및 최초 스크립트 설정
 */

let device;
const uAgent	= navigator.userAgent.toLowerCase();
const mobiles	= /iphone|ipod|ipad|android|blackberry|windows ce|nokia|webos|opera mini|sonyericsson|opera mobi|iemobile|aarch/;// 모바일 디바이스
// 모바일 검증
if (mobiles.test(uAgent)) {
	device = 'mobile';
}
// 디바이스 검증
if (device !== 'mobile') {
	device = uAgent.indexOf('nc ingame') !== -1 ? 'ingame' : uAgent.indexOf('nc launcher') !== -1 ? 'launcher' : 'pc';
}
// 인게임 계정 검증
if (device === 'ingame' && !account) {
	location.href='/login_ingame';
}
// 런처 디바이스 내장 함수 설정
if (device === 'launcher') {
	// boundAsync 객채로 처리(예: boundAsync.logout(); 호출 -> 런처 폼 변경)
	// 런처 암호화 처리시 내장 함수명이 변경되므로 사용 불가
	//CefSharp.BindObjectAsync("boundAsync");
}

$('body').addClass(device);
initIncludeHTML(document.querySelector('#nc-cnb'), '/cnb.html');
initIncludeHTML(document.querySelector('#layer_alert'), '/popup.html');
createFooterHtml();
includeJS('/js/message.js');
// cnb 생성
function createCnbHtml(){
	let cnb_html = '<nav class="ncc-lnb ncc-lnb-type--main"><ul class="ncc-lnb-list">';
	let cnbs = '';
	let download_btn = '';
	$.each(cnb, function(index, item){
		var subs = '<ul class="ncc-lnb-item__sub">';
		$.each(item._subs, function(index, sub){
			subs += '<li class="s' + sub.id + '"><a href="' + sub.href + '" target="_self"><span>' + sub.desc + '</span></a></li>';
			if (sub.download) {
				download_btn = '<div class="ncc-gamestart"><div class="ncc-gamestart-btn"><a href="' + sub.href + '" class="btn-download ncc-gamestart-btn__start">' + sub.desc + '</a></div></div>';
			}
		});
		subs += '</ul>';

		cnbs += '<li class="ncc-lnb-item m' + item.id + '"><a href="' + item.href + '"><span>' + item.desc + '</span></a>' +
			subs +
			'</li>';
	});
	cnb_html += cnbs;
	cnb_html += '</ul><span class="ncc-lnb-hover" style="transform: translateX(50px) scaleX(0.1); opacity: 0;"></span></nav>';
	$('.ncc-lnb-wrap').html(download_btn + cnb_html);

	if (device !== 'ingame' && device !== 'launcher') {
		$('.ncc-gnb-wrap').prepend('<div class="ncc-gnb-wrap__bg"></div><a class="ncc-ncservice-btn">Open<span><i></i><i></i><i></i></span></a><a class="ncc-bi" data-type="image" href="/index" target="_self"><span>Lineage</span></a>');
		// left panel create
		$('.ncc-left-panel .ncc-lnb-list').html(cnbs);
	}
}

// 로그인 html 생성
function createLoginHtml(){
	let loginHtml = '';
	if (account != undefined) {
		// login
		if (account.firstChar != undefined) {
			loginHtml = '<div class="ncc-login--after">' +
			'<div class="ncc-login--mobile"><button class="ncc-login--mobile-btn"><img src="' + account.firstChar.profileUrl + '" class="ncc-login--info__thumb"></button><span class="ncc-login--info__noti"></span></div>' +
			'<div class="ncc-login--info"><a><img src="' + account.firstChar.profileUrl + '" class="ncc-login--info__thumb"><span class="ncc-login--info__char">' + account.firstChar.name + '</span><span class="ncc-login--info__server">' + serverName + ', lv ' + account.firstChar.level + '</span></a></div></div>';
		} else {
			loginHtml = '<div class="ncc-login--after">' +
			'<div class="ncc-login--mobile"><button class="ncc-login--mobile-btn"><img src="/img/user_unkown.jpg" class="ncc-login--info__thumb"></button><span class="ncc-login--info__noti"></span></div>' +
			//'<div class="ncc-login--info"><a><img src="/img/user_unkown.jpg" class="ncc-login--info__thumb"><span class="ncc-login--info__char">미설정</span><span class="ncc-login--info__server">' + serverName + ', ' + '0Lv</span></a></div></div>';
			'<div class="ncc-login--info"><a><img src="/img/user_unkown.jpg" class="ncc-login--info__thumb"><span class="ncc-login--info__char">Not configured</span><span class="ncc-login--info__server">' + serverName + ', ' + '0Lv</span></a></div></div>';
		}
		// 오른쪽 패널을 생성
		createRightPanel();
	} else {
		loginHtml = '<div class="ncc-login--before"><a href="javascript:login();" class="ncc-login__link ncc-login__link-login">Login</a><a class="ncc-login__link ncc-login__link-join">' + serverName + '</a></div>';
	}
	$('#nc-cnb .is-child-nav .ncc-login').html(loginHtml);
}

function getLocaleDateString() {
	const formats = {
	  "af-ZA": "YYYY/MM/DD",
	  "aM-ET": "D/M/YYYY",
	  "ar-AE": "DD/MM/YYYY",
	  "ar-BH": "DD/MM/YYYY",
	  "ar-DZ": "DD-MM-YYYY",
	  "ar-EG": "DD/MM/YYYY",
	  "ar-IQ": "DD/MM/YYYY",
	  "ar-JO": "DD/MM/YYYY",
	  "ar-KW": "DD/MM/YYYY",
	  "ar-LB": "DD/MM/YYYY",
	  "ar-LY": "DD/MM/YYYY",
	  "ar-MA": "DD-MM-YYYY",
	  "ar-OM": "DD/MM/YYYY",
	  "ar-QA": "DD/MM/YYYY",
	  "ar-SA": "DD/MM/YY",
	  "ar-SY": "DD/MM/YYYY",
	  "ar-TN": "DD-MM-YYYY",
	  "ar-YE": "DD/MM/YYYY",
	  "arn-CL": "DD-MM-YYYY",
	  "as-IN": "DD-MM-YYYY",
	  "az-CYrl-AZ": "DD.MM.YYYY",
	  "az-Latn-AZ": "DD.MM.YYYY",
	  "ba-RU": "DD.MM.YY",
	  "be-BY": "DD.MM.YYYY",
	  "bg-BG": "DD.M.YYYY",
	  "bn-BD": "DD-MM-YY",
	  "bn-IN": "DD-MM-YY",
	  "bo-CN": "YYYY/M/D",
	  "br-FR": "DD/MM/YYYY",
	  "bs-CYrl-BA": "D.M.YYYY",
	  "bs-Latn-BA": "D.M.YYYY",
	  "ca-ES": "DD/MM/YYYY",
	  "co-FR": "DD/MM/YYYY",
	  "cs-CZ": "D.M.YYYY",
	  "cY-GB": "DD/MM/YYYY",
	  "Da-DK": "DD-MM-YYYY",
	  "De-AT": "DD.MM.YYYY",
	  "De-CH": "DD.MM.YYYY",
	  "De-DE": "DD.MM.YYYY",
	  "De-LI": "DD.MM.YYYY",
	  "De-LU": "DD.MM.YYYY",
	  "Dsb-DE": "D. M. YYYY",
	  "Dv-MV": "DD/MM/YY",
	  "el-GR": "D/M/YYYY",
	  "en-029": "MM/DD/YYYY",
	  "en-AU": "D/MM/YYYY",
	  "en-BZ": "DD/MM/YYYY",
	  "en-CA": "DD/MM/YYYY",
	  "en-GB": "DD/MM/YYYY",
	  "en-IE": "DD/MM/YYYY",
	  "en-IN": "DD-MM-YYYY",
	  "en-JM": "DD/MM/YYYY",
	  "en-MY": "D/M/YYYY",
	  "en-NZ": "D/MM/YYYY",
	  "en-PH": "M/D/YYYY",
	  "en-SG": "D/M/YYYY",
	  "en-TT": "DD/MM/YYYY",
	  "en-US": "M/D/YYYY",
	  "en-ZA": "YYYY/MM/DD",
	  "en-ZW": "M/D/YYYY",
	  "es-AR": "DD/MM/YYYY",
	  "es-BO": "DD/MM/YYYY",
	  "es-CL": "DD-MM-YYYY",
	  "es-CO": "DD/MM/YYYY",
	  "es-CR": "DD/MM/YYYY",
	  "es-DO": "DD/MM/YYYY",
	  "es-EC": "DD/MM/YYYY",
	  "es-ES": "DD/MM/YYYY",
	  "es-GT": "DD/MM/YYYY",
	  "es-HN": "DD/MM/YYYY",
	  "es-MX": "DD/MM/YYYY",
	  "es-NI": "DD/MM/YYYY",
	  "es-PA": "MM/DD/YYYY",
	  "es-PE": "DD/MM/YYYY",
	  "es-PR": "DD/MM/YYYY",
	  "es-PY": "DD/MM/YYYY",
	  "es-SV": "DD/MM/YYYY",
	  "es-US": "M/D/YYYY",
	  "es-UY": "DD/MM/YYYY",
	  "es-VE": "DD/MM/YYYY",
	  "et-EE": "D.MM.YYYY",
	  "eu-ES": "YYYY/MM/DD",
	  "fa-IR": "MM/DD/YYYY",
	  "fi-FI": "D.M.YYYY",
	  "fil-PH": "M/D/YYYY",
	  "fo-FO": "DD-MM-YYYY",
	  "fr-BE": "D/MM/YYYY",
	  "fr-CA": "YYYY-MM-DD",
	  "fr-CH": "DD.MM.YYYY",
	  "fr-FR": "DD/MM/YYYY",
	  "fr-LU": "DD/MM/YYYY",
	  "fr-MC": "DD/MM/YYYY",
	  "fY-NL": "D-M-YYYY",
	  "ga-IE": "DD/MM/YYYY",
	  "gD-GB": "DD/MM/YYYY",
	  "gl-ES": "DD/MM/YY",
	  "gsw-FR": "DD/MM/YYYY",
	  "gu-IN": "DD-MM-YY",
	  "ha-Latn-NG": "D/M/YYYY",
	  "he-IL": "DD/MM/YYYY",
	  "hi-IN": "DD-MM-YYYY",
	  "hr-BA": "D.M.YYYY.",
	  "hr-HR": "D.M.YYYY",
	  "hsb-DE": "D. M. YYYY",
	  "hu-HU": "YYYY. MM. DD.",
	  "hY-AM": "DD.MM.YYYY",
	  "iD-ID": "DD/MM/YYYY",
	  "ig-NG": "D/M/YYYY",
	  "ii-CN": "YYYY/M/D",
	  "is-IS": "D.M.YYYY",
	  "it-CH": "DD.MM.YYYY",
	  "it-IT": "DD/MM/YYYY",
	  "iu-Cans-CA": "D/M/YYYY",
	  "iu-Latn-CA": "D/MM/YYYY",
	  "ja-JP": "YYYY/MM/DD",
	  "ka-GE": "DD.MM.YYYY",
	  "kk-KZ": "DD.MM.YYYY",
	  "kl-GL": "DD-MM-YYYY",
	  "kM-KH": "YYYY-MM-DD",
	  "kn-IN": "DD-MM-YY",
	  "ko-KR": "YYYY. MM. DD",
	  "kok-IN": "DD-MM-YYYY",
	  "kY-KG": "DD.MM.YY",
	  "lb-LU": "DD/MM/YYYY",
	  "lo-LA": "DD/MM/YYYY",
	  "lt-LT": "YYYY.MM.DD",
	  "lv-LV": "YYYY.MM.DD.",
	  "Mi-NZ": "DD/MM/YYYY",
	  "Mk-MK": "DD.MM.YYYY",
	  "Ml-IN": "DD-MM-YY",
	  "Mn-MN": "YY.MM.DD",
	  "Mn-Mong-CN": "YYYY/M/D",
	  "Moh-CA": "M/D/YYYY",
	  "Mr-IN": "DD-MM-YYYY",
	  "Ms-BN": "DD/MM/YYYY",
	  "Ms-MY": "DD/MM/YYYY",
	  "Mt-MT": "DD/MM/YYYY",
	  "nb-NO": "DD.MM.YYYY",
	  "ne-NP": "M/D/YYYY",
	  "nl-BE": "D/MM/YYYY",
	  "nl-NL": "D-M-YYYY",
	  "nn-NO": "DD.MM.YYYY",
	  "nso-ZA": "YYYY/MM/DD",
	  "oc-FR": "DD/MM/YYYY",
	  "or-IN": "DD-MM-YY",
	  "pa-IN": "DD-MM-YY",
	  "pl-PL": "DD.MM.YYYY",
	  "prs-AF": "DD/MM/YY",
	  "ps-AF": "DD/MM/YY",
	  "pt-BR": "D/M/YYYY",
	  "pt-PT": "DD-MM-YYYY",
	  "qut-GT": "DD/MM/YYYY",
	  "quz-BO": "DD/MM/YYYY",
	  "quz-EC": "DD/MM/YYYY",
	  "quz-PE": "DD/MM/YYYY",
	  "rM-CH": "DD/MM/YYYY",
	  "ro-RO": "DD.MM.YYYY",
	  "ru-RU": "DD.MM.YYYY",
	  "rw-RW": "M/D/YYYY",
	  "sa-IN": "DD-MM-YYYY",
	  "sah-RU": "MM.DD.YYYY",
	  "se-FI": "D.M.YYYY",
	  "se-NO": "DD.MM.YYYY",
	  "se-SE": "YYYY-MM-DD",
	  "si-LK": "YYYY-MM-DD",
	  "sk-SK": "D. M. YYYY",
	  "sl-SI": "D.M.YYYY",
	  "sMa-NO": "DD.MM.YYYY",
	  "sMa-SE": "YYYY-MM-DD",
	  "sMj-NO": "DD.MM.YYYY",
	  "sMj-SE": "YYYY-MM-DD",
	  "sMn-FI": "D.M.YYYY",
	  "sMs-FI": "D.M.YYYY",
	  "sq-AL": "YYYY-MM-DD",
	  "sr-CYrl-BA": "D.M.YYYY",
	  "sr-CYrl-CS": "D.M.YYYY",
	  "sr-CYrl-ME": "D.M.YYYY",
	  "sr-CYrl-RS": "D.M.YYYY",
	  "sr-Latn-BA": "D.M.YYYY",
	  "sr-Latn-CS": "D.M.YYYY",
	  "sr-Latn-ME": "D.M.YYYY",
	  "sr-Latn-RS": "D.M.YYYY",
	  "sv-FI": "D.M.YYYY",
	  "sv-SE": "YYYY-MM-DD",
	  "sw-KE": "M/D/YYYY",
	  "sYr-SY": "DD/MM/YYYY",
	  "ta-IN": "DD-MM-YYYY",
	  "te-IN": "DD-MM-YY",
	  "tg-CYrl-TJ": "DD.MM.YY",
	  "th-TH": "D/M/YYYY",
	  "tk-TM": "DD.MM.YY",
	  "tn-ZA": "YYYY/MM/DD",
	  "tr-TR": "DD.MM.YYYY",
	  "tt-RU": "DD.MM.YYYY",
	  "tzM-Latn-DZ": "DD-MM-YYYY",
	  "ug-CN": "YYYY-M-D",
	  "uk-UA": "DD.MM.YYYY",
	  "ur-PK": "DD/MM/YYYY",
	  "uz-CYrl-UZ": "DD.MM.YYYY",
	  "uz-Latn-UZ": "DD/MM YYYY",
	  "vi-VN": "DD/MM/YYYY",
	  "wo-SN": "DD/MM/YYYY",
	  "xh-ZA": "YYYY/MM/DD",
	  "Yo-NG": "D/M/YYYY",
	  "zh-CN": "YYYY/M/D",
	  "zh-HK": "D/M/YYYY",
	  "zh-MO": "D/M/YYYY",
	  "zh-SG": "D/M/YYYY",
	  "zh-TW": "YYYY/M/D",
	  "zu-ZA": "YYYY/MM/DD",
	};
  
	return formats[navigator.language] || "DD/MM/YYYY";
  }

// 오른쪽 패널 생성
function createRightPanel(){
	var alimHtml = '';
	var alimCheck = account.alimList != undefined && account.alimList.length > 0;
	var firstCharCheck = account.firstChar != undefined;
	if (alimCheck) {
		$.each(account.alimList, function(index, item){
			alimHtml += 
				'<li class="on" id=\"alim_' + item.id + 
				'\"><div class="wrapNotice"><a><p class="noticeMsg">' + 
				item.logContent + 
				'&nbsp;-&nbsp;' + 
				moment(item.insertTime).format(getLocaleDateString() + ' HH:mm:ss') + 
				'</p><a href="javascript:alimDelete(' + 
				item.id + 
				');" title="Delete">Delete</a><div class="thumb"><img class="icon" src="/img/' + 
				(item.type == 1 ? 30005 : item.type == 3 ? 29001 : 27009) + 
				'.png" alt="nickname"></div></a></div></li>';
		});
	} else {
		//alimHtml = '<li class="on"><div class="wrapNotice"><a href="javascript:;"><p class="noticeMsg">알림이 없습니다.</p><div class="thumb"><img class="icon" src="/img/27009.png" alt="nickname"></div></a></div></li>';
		alimHtml = '<li class="on"><div class="wrapNotice"><a href="javascript:;"><p class="noticeMsg">No notifications.</p><div class="thumb"><img class="icon" src="/img/27009.png" alt="nickname"></div></a></div></li>';
	}
	var rightPanelHtml = 
		'<div class="ncc-userinfo" style="background-image: url(\'' + (firstCharCheck ? account.firstChar.profileUrl : '/img/user_unkown.jpg') + '\');">' +
			'<a class="ncc-right-panel-close">Close</a>' +
			'<div class="ncc-profile is-nc-account">' +
				'<div class="ncc-profile-wrap">' +
					'<a class="ncc-profile-img ic-home" href="/account/mypage" target="_self"><img src="' + (firstCharCheck ? account.firstChar.profileUrl : '/img/user_unkown.jpg') + '"></a>' +
					'<div class="ncc-profile-info">' +
						//'<span class="ncc-profile-info__char">' + (!firstCharCheck || account.objId == 0 ? '대표 케릭터 미설정' : account.firstChar.name) + '</span>' +
						'<span class="ncc-profile-info__char">' + (!firstCharCheck || account.objId == 0 ? 'Main character not configured' : account.firstChar.name) + '</span>' +
						'<span class="ncc-profile-info__server">' + serverName + '</span>' +
						'<span class="ncc-profile-info__level">Lv ' + (firstCharCheck ? account.firstChar.level : 0) + '</span>' +
					'</div>' +
					'<div class="ncc-profile-links"><a class="ncc-profile-charchange">Select Character</a><a class="ncc-profile-logout" href="javascript:logout();">Logout</a></div>' +
				'</div>' +
				'<div class="nc-account-info"><p>' + account.name + '</p></div>' +
			'</div>' +
			'<nav class="ncc-shortcut">' +
				//'<strong class="blind">서비스 바로가기</strong>' +
				'<strong class="blind">Go to Service</strong>' +
				'<ul id="ncc-shortcut-list" class="ncc-shortcut-list ncc-shortcut-list-length4">' +
					'<li class="ncc-shortcut-item"><a href="/account/mypage" class="ncc-shortcut-item__link"><i class="icon-shortcut icon-shortcut--mypage"></i><span class="desc" id="ncc-shortcut-databind-mypage">LinWeb</span></a></li>' +
					'<li class="ncc-shortcut-item"><a href="/goods" class="ncc-shortcut-item__link"><i class="icon-shortcut icon-shortcut--ncoin"></i><span class="desc" id="ncc-shortcut-databind-ncoin">' + commaAdd(account.ncoin) + '</span></a></li>' +
					'<li class="ncc-shortcut-item"><a href="/goods/coupon" class="ncc-shortcut-item__link"><i class="icon-shortcut icon-shortcut--entercoupon"></i><span class="desc" id="ncc-shortcut-databind-entercoupon">Enter Coupon</span></a></li>' +
					'<li class="ncc-shortcut-item"><a href="javascript:urlTypeform(\'1\', \'0\', \'post\', \'/customer\');" class="ncc-shortcut-item__link"><i class="icon-shortcut icon-shortcut--cs"></i><span class="desc" id="ncc-shortcut-databind-cs">Support</span></a></li>' +
				'</ul>' +
			'</nav>' +
		'</div>' +
		'<div class="ncc-noti-wrap">' +
			'<ul class="ncc-noti-tab"><li class="is-active"><a onClick="return false;">Notifications ' + 
			'<span class="is-on" style=\"background-color: #dc4141; ' + (alimCheck ? '' : 'display: none;') + '\">' + (alimCheck ? account.alimList.length : 0) + '</span>' +
			//'</a></li><li><a>접속친구</a></li></ul>' +
			'</a></li><li><a>Connected Friends</a></li></ul>' +
			'<div class="ncc-noti">' +
				'<div class="ncc-noti-list" id="notiList">' +
					'<ul>' + alimHtml + '</ul>' +
					'<div class="ncc-noti-setting" id="notiSetting" style="display: none;"></div>' +
				'</div>' +
			'</div>' +
		'</div>';
	$('.ncc-right-panel > .ncc-right-panel-wrap').html(rightPanelHtml);
		
	if (account.charList.length > 0) {
		var charListPanelHtml = '';
		$.each(account.charList, function(index, item){
			charListPanelHtml += '<li><span>' + item.name + '</span><input type="hidden" name="select_charter_choice" id="select_charter_choice" value=\"' + item.objId + '\"/></li>';
		});
		$('.ncc-character-panel .ncc-character-list-wrap .ncc-character-list').html(charListPanelHtml);
	}
}

// 하단 생성
function createFooterHtml(){
	const footerHtml = 
		'<div class="footer footer-kr"><div class="footer-links"><ul class="footer-links-list footer-links-list-kr"><li class="footer-links-items item1">' + serverName + 
		'</li></ul></div><div class="footer-copyright"><div class="copyright-studio">Lineage ® is a registered trademark of NCSOFT Corporation.&nbsp;'+ 
		'</div><div class="copyright-company">This web is property of ' + serverName + '.' +
		'<br><span class="reserved"> © All Rights Reserved.</span></div></div><div class="footer-logo">' + serverName + '</div></div>';
	$('.wrap-footer').html(footerHtml);
}
	
// init html include
function initIncludeHTML(divContainer, urlHTML) {
        let xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
        	if (this.readyState == 4) {
        		if (this.status == 200) {
        			divContainer.innerHTML = this.responseText;
        			if (urlHTML == '/cnb.html') {
		createCnbHtml();
		if (device !== 'ingame') {
			createLoginHtml();
		}
		includeJS('/js/common.js');
        	        }
        		}
        		if (this.status == 404) {
        			divContainer.innerHTML = "Page not found.";
        		}
        	}
        }
        xhttp.open("GET", urlHTML, true);
        xhttp.send();
}

// 에러페이지 출력
function showError(request, status, error) {
	var doc = '<link rel="stylesheet" href="/css/error.css"><div class="error-container" id="container"><div class="logo"><a class="logo-link" href="/index"><img src="/img/plaync.png" alt="plaync icon"/></a></div><div class="error-contents-wrap">' +
		    //'<header class="error-header"><h1 class="title">일시적으로 페이지를 불러올 수 없습니다.</h1><p class="subcopy">동일한 문제가 지속적으로 발생할 경우, 고객지원으로 문의해 주시기 바랍니다.</p></header><div class="error-contents">' +
			'<header class="error-header"><h1 class="title">Unable to load the page.</h1><p class="subcopy">If the same problem persists, please contact customer support.</p></header><div class="error-contents">' +
		        //'<div class="error-btn-wrap"><button class="btn btn-error btn-error--o btn-back" onclick="history.go(-1);">이전 페이지</button><a href="javascript:customerLocation();" class="btn btn-error btn-inquiry">고객지원</a></div>' +
				'<div class="error-btn-wrap"><button class="btn btn-error btn-error--o btn-back" onclick="history.go(-1);">Previous Page</button><a href="javascript:customerLocation();" class="btn btn-error btn-inquiry">Customer Support</a></div>' +
		        '<div class="links"><a href="/index"><span>Home</span></a></div>' +
		    '</div></div><footer id="footer" class="error-footer-wrap"><p class="copyright">Ⓒ {SERVER_NAME}. All Rights Reserved.</p></footer></div>' +
		'<script type="text/javascript">function customerLocation(){' +
		'var form = document.createElement("form");form.setAttribute(\'charset\', \'UTF-8\');form.setAttribute(\'method\', \'POST\');form.setAttribute(\'action\', \'/customer\');' +
		'var hiddenField_1 = document.createElement(\'input\');hiddenField_1.setAttribute(\'type\', \'hidden\');hiddenField_1.setAttribute(\'name\', \'type\');hiddenField_1.setAttribute(\'value\', \'1\');form.appendChild(hiddenField_1);' +
		'var hiddenField_2 = document.createElement(\'input\');hiddenField_2.setAttribute(\'type\', \'hidden\');hiddenField_2.setAttribute(\'name\', \'num\');hiddenField_2.setAttribute(\'value\', \'0\');form.appendChild(hiddenField_2);' +
		'document.body.appendChild(form);form.submit();}';
	$('body').html(doc);
	console.log('request: ' + request);
	console.log('status: ' + status);
	console.log('error: ' + error);
}
