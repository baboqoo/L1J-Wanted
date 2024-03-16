$(function() {
	includeHTML(document.querySelector('#svg-container'), '/svg.html');
	
	var dropdown			= $('.ui-dropdown'),
		dropdownSelect		= $('.ui-dropdown-custom_items'),
		resetTextBtn		= $('.co-btn-reset'),
		listStyleBtn		= $('.btn-list'),
		cardStyleBtn		= $('.btn-cards'),
		boardList			= $('.ncCommunityBoardList'),
		topscrollBtn		= $('.co-btn-top_list'),
		boardTypeMobileBtn	= $('.btn-type-selected');
	
	dropdown.on('click', function(event) {
		$(this).toggleClass('is-active');
	});
	
	dropdownSelect.on('click', function(event) {
		$('.ui-dropdown-community.is-active .select').find('span').html($(this).attr('data-textvalue'));
		$('.ui-dropdown-community.is-active .select').find('#data_val').val($(this).attr('data-value'));
	});
	
	resetTextBtn.on('click', function(event) {
		$('#ncCommunitySearch').val('');
	});
	
	var viewMode = function(type){
		$('.co-btn').removeClass('is-disabled');
		if(type == 'card'){
			listStyleBtn.addClass('is-disabled');
			if(boardList.hasClass('board-list-default'))boardList.removeClass('board-list-default');
			$('.ncCommunityBoardList').addClass('board-list-card');
			$('.board-type-card').show();
			$('.board-type-list').hide();
			boardTypeMobileBtn.removeClass('list');
			boardTypeMobileBtn.addClass('card');
			boardTypeMobileBtn.html("<svg class='fe-svg fe-svg-card'><use xlink:href='#fe-svg-card'></use></svg>");
		}else{
			cardStyleBtn.addClass('is-disabled');
			if(boardList.hasClass('board-list-card'))boardList.removeClass('board-list-card');
			$('.ncCommunityBoardList').addClass('board-list-default');
			$('.board-type-list').show();
			$('.board-type-card').hide();
			boardTypeMobileBtn.removeClass('card');
			boardTypeMobileBtn.addClass('list');
			boardTypeMobileBtn.html("<svg class='fe-svg fe-svg-list'><use xlink:href='#fe-svg-list'></use></svg>");
		}
		boardTypeMobileBtn.removeClass('is-show');
	}
	
	listStyleBtn.on('click', function(event) {
		if(boardList.hasClass('board-list-card')){
			viewMode('list');
			setLocal('board_notice_viewMode', 'list');
		}
	});
	
	cardStyleBtn.on('click', function(event) {
		if(boardList.hasClass('board-list-default')){
			viewMode('card');
			setLocal('board_notice_viewMode', 'card');
		}
	});
	
	if(getLocal('board_notice_viewMode') == 'card'){
		viewMode('card');
	}
	
	topscrollBtn.on('click', function(e) {
		$('html, body').stop().animate({
			scrollTop: ($('body').offset().top)
		}, 600);
		e.preventDefault();
	});
	
	boardTypeMobileBtn.on('click', function(event) {
		$(this).toggleClass('is-show');
	});
	
	$('.input-board-search').on('click', function(event) {
		$('#wrapSearch').addClass('is-show');
	});
	
	$('.co-btn-clear').on('click', function(event) {
		$('#wrapSearch').removeClass('is-show');
	});
	
	// 영역밖 클릭
	$('body').mouseup(function (e){
		if($('.option').has(e.target).length === 0 && $('.ui-dropdown.is-active').has(e.target).length === 0 && $('.ui-dropdown').hasClass('is-active')){
			$('.ui-dropdown').removeClass('is-active');
		}
		if($('#wrapSearch').has(e.target).length === 0 && $('#wrapSearch').hasClass('is-show')){
			$('#wrapSearch').removeClass('is-show');
			$('#ncCommunitySearch').val('');
		}
		eventStop(e);
	});
	
	$(document).ready(function(){
		var nowCheckDate = $('.nowCheckvalue');
		if(nowCheckDate != undefined && nowCheckDate.length > 0){
			$.each(nowCheckDate, function (index, item) {
				if(item.value == 0){
					var changeFormat = getNowDayTimeToFormat($(this).closest('.board-items').find('.nowCheckdate').val());
					$(this).closest('.board-items').find('.date').html(changeFormat);
				}
			});
		}
	})
});

function noticeMore(){
	var noticetype	= $('#noticeType').val(),
		totsize		= $('#totSize').val(),
		currsize	= $('#cursize').val();
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/notice/more",
		data:		{size:currsize, noticeType:noticetype},//현재리스트수전송
		success:function(data){
			var addhtml = '';
			$.each(data,function(key,element){//리스트수만큼 반복
				addhtml += createNoticeHtml(element);
            });
            $(addhtml).appendTo(".ncCommunityBoardList");//뒤에 이어서 html넣기
            if(data.length > 0){
            	$('#cursize').val(Number(currsize) + Number(data.length));//리스트수갱신
            	$('#totSize').val(Number(totsize) - Number(data.length));//총길이 갱신
            	if($('#totSize').val() <= 0){
            		$('.wrap-community-more').html('');
            	}else{
            		$('.wrap-community-more').html('<button class="nc-comment-more" onClick="javascritp:noticeMore();"><span class="txt">Show more</span></button>');
            	}
            }
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
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

function createNoticeHtml(element){
	var boardType	= $('.ncCommunityBoardList'),
		listtype	= "",
		cardtype	= "display:none;";
	if(boardType.hasClass('board-list-card')){
		listtype	="display:none;";
		cardtype	= "";
	}
	
	var noticeType = '';
	switch(element.type){
	case 1:noticeType = 'Updates';break;
	case 2:noticeType = 'Event';break;
	default:noticeType = 'Announcements';break;
	}	
	
	var dateterm = moment(getTime).format('YYYYMMDD') - moment(element.date).format('YYYYMMDD');
	return '<li class="board-items" onClick=\'urlTypeform(\"' + (element.type===1 ? '1' : (element.type===2 ? '2' : '0')) + '\", \"' + element.rownum + '\", "post", "/notice/view");\'>' +
				'<div class="title board-type-list" style=\"' + listtype + '\">' +
					'<span class="category">' + noticeType + '&nbsp</span>' +
					'<a href=\'javascript:urlTypeform(\"' + (element.type===1 ? '1' : (element.type===2 ? '2' : '0')) + '\", \"' + element.rownum + '\", "post", "/notice/view");\'>' + element.title + '</a>' +
					(element.mainImg != undefined ? '<svg class="fe-svg fe-svg-picture"><use xlink:href="#fe-svg-picture"></use></svg>' : '') +
					(dateterm === 0 ? '&nbsp<i class="fe-icon-new" data-diff=""></i>' : '') +
				'</div>' +
	
				'<div class="info board-type-list" style=\"' + listtype + '\">' +
				    '<span class="writer">' + (element.type!=undefined ? '<img src="/img/lineage_writer.png" alt="">' : element.name + '<span class="server">' + serverName + '</span>') + '</span>&nbsp' +
				    '<span class="date">' + moment(element.date).format(getLocaleDateString()) + '</span>&nbsp' +
				    '<span class="hit"><em class="txt">Views</em>' + element.readcount + '</span>' +
				'</div>' +

				'<div class="board-type-card" style=\"' + cardtype + '\">' +
				(element.mainImg != undefined ? '<div class="board-items-thumb"><a href=\'javascript:urlTypeform(\"' + (element.type===1 ? '1' : (element.type===2 ? '2' : '0')) + '\", \"' + element.rownum + '\", "post", "/notice/view");\'><img src=\"' + element.mainImg + '\" alt=""></a></div>' : '') +
					'<div class="board-items-contents">' +
						'<div class="title">' +
							'<span class="category">' + noticeType + '&nbsp</span>' +
							'<a href=\'javascript:urlTypeform(\"' + (element.type===1 ? '1' : (element.type===2 ? '2' : '0')) + '\", \"' + element.rownum + '\", "post", "/notice/view");\'>' + element.title + 
							(dateterm===0 ? '&nbsp<i class="fe-icon-new"></i></a>' : '') +
						'</div>' +
						'<div class="desc desc-overflow">' +
							'<a href=\'javascript:urlTypeform(\"' + (element.type===1 ? '1' : (element.type===2 ? '2' : '0')) + '\", \"' + element.rownum + '\", "post", "/notice/view");\'>' + element.content + '</a>' +
						'</div>' +
						'<span class="writer">' + (element.type!=undefined ? '<img src="/img/lineage_writer.png" alt="">' : element.name + '<span class="server">' + serverName + '</span>') + '</span>&nbsp' +
					'</div>' +
					'<div class="board-items-footer">' +
						'<div class="info">' +
							'<span class="writer">' + element.name + '</span>' +
							'<span class="date">' + moment(element.date).format(getLocaleDateString()) + '</span>&nbsp' +
							'<span class="hit-count"><span class="txt">Views </span><em>' + element.readcount + '</em></span>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</li>';
}

function noticeWriteFormCheck(){
	var f=document.boardWriteForm;
	if(!f.noti_type.value){
		popupShow('Please select a type.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if(!f.title.value){
		popupShow('Please enter the subject.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if(!f.content.value){
		popupShow('Please enter the detail.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if(maxByteCheck(f.content.value, 1)){// 용량 체크
		return true;
	}
	return false;
}

function delectNoticeConfirm(rownum, type){
	//popupShow('정말로 삭제하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteNoticeConfirmAction(' + rownum + ', ' + type + ');" class="close">Yes</a></span>');
	popupShow('Are you sure you want to delete it?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteNoticeConfirmAction(' + rownum + ', ' + type + ');" class="close">Yes</a></span>');
}

function deleteNoticeConfirmAction(rownum, type){
	const deleteImgList = $('.board-view .view-body').find('img');
	urlTypeform(type, rownum, 'post', '/notice/delete' + getDeleteImgSrc(deleteImgList));
}

function getDeleteImgSrc(obj){// 삭제할 이미지 리스트 취득
	let imgSrc			= '';
	if(obj != undefined && obj.length > 0){
		imgSrc			+= '?deleteImgList='
		for(var i=0; i<obj.length; i++){
			if(i > 0)imgList += ',';
			imgSrc		+= obj.eq(i).attr('src');
		}
	}
	return imgSrc;
}

function boardCancel(cancelType){
	const tempval	= $('#tempList').val(),
		contentval	= $('#oriContent').val(),
		orival		= $('#oriList').val();
	if(!tempval && !orival){
		history.back();
		return;
	}
	const sendData = {'tempList':tempval, 'oriContent':contentval, 'oriList':orival};
	$.ajax({
        data:	sendData,
        type:	'POST',
        url:	cancelType == 'update' ? '/define/editor/updatecancel' : '/define/editor/insertcancel',
        cache:	false,
        success: function(res) {
        	history.back();
        }
    });
}