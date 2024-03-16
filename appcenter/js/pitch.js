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
		if(type == 'card'){
			$('.co-btn').removeClass('is-disabled');
			listStyleBtn.addClass('is-disabled');
			if(boardList.hasClass('board-list-default'))boardList.removeClass('board-list-default');
			$('.ncCommunityBoardList').addClass('board-list-card');
			$('.board-type-card').show();
			$('.board-type-list').hide();
			boardTypeMobileBtn.removeClass('list');
			boardTypeMobileBtn.addClass('card');
			var chagehtml = "<svg class='fe-svg fe-svg-card'><use xlink:href='#fe-svg-card'></use></svg>";
			boardTypeMobileBtn.html(chagehtml);
			boardTypeMobileBtn.removeClass('is-show');
		}else{
			$('.co-btn').removeClass('is-disabled');
			cardStyleBtn.addClass('is-disabled');
			if(boardList.hasClass('board-list-card'))boardList.removeClass('board-list-card');
			$('.ncCommunityBoardList').addClass('board-list-default');
			$('.board-type-list').show();
			$('.board-type-card').hide();
			boardTypeMobileBtn.removeClass('card');
			boardTypeMobileBtn.addClass('list');
			var chagehtml = "<svg class='fe-svg fe-svg-list'><use xlink:href='#fe-svg-list'></use></svg>";
			boardTypeMobileBtn.html(chagehtml);
			boardTypeMobileBtn.removeClass('is-show');
		}
	}
	
	listStyleBtn.on('click', function(event) {
		if(boardList.hasClass('board-list-card')){
			viewMode('list');
			setLocal('board_fitch_viewMode', 'list');
		}
	});
	
	cardStyleBtn.on('click', function(event) {
		if(boardList.hasClass('board-list-default')){
			viewMode('card');
			setLocal('board_fitch_viewMode', 'card');
		}
	});
	
	if(getLocal('board_fitch_viewMode') == 'card'){
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
	
	$('.contentWrite').on('click', function(event){
		if(!$(this).closest('.comment-form-contentWrite').hasClass('is-active')){
			eventStop(event);
			location.href='/login';
		}
	});
	
	$('.comment-toolbar > .right > button').on('click', function(event){
		boardCommentWrite();
	});
	
	$('#ncCommunityReport .ly-close').on('click', function(event){
		reportModalClose();
	});

	$('#ncCommunityReport .co-btn-finish').on('click', function(event){
		reportInsert($(this));
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

function commentLengthCheck(obj){
	// 제한된 길이보다 입력된 길이가 큰 경우 제한 길이만큼만 자르고 텍스트영역에 넣음
    if ($(obj).val().length > 300) {
        $(obj).val($(obj).val().substr(0, 300));
    }
    
    // 입력된 텍스트 길이를 #textCount 에 업데이트 해줌
    $('.count-word > em').text($(obj).val().length);
}

function reportBtn(obj){
	if (account == undefined) {
		//popupShow('로그인 후 이용하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Available after login.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	//reportModalOpen($(obj), '홍보_ID_' + $(obj).attr('data-boardid'));
	reportModalOpen($(obj), 'Promotion_ID_' + $(obj).attr('data-boardid'));
}

function commentReportBtn(obj){
	if (account == undefined) {
		//popupShow('로그인 후 이용하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Available after login.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	//reportModalOpen($(obj), '홍보_ID_' + $(obj).attr('data-boardid') + '_코멘트_ID_' + $(obj).attr('data-commentid'));
	reportModalOpen($(obj), 'Promotion_ID_' + $(obj).attr('data-boardid') + '_Comment_ID_' + $(obj).attr('data-commentid'));
}

function reportModalOpen(obj, log){
	$('#ncCommunityReport .report-target .target').html(obj.attr('data-name'));
	$('#reportLog').val(log);
	$('#ncCommunityReportModal').addClass('is-active');
	$('#ncCommunityReport').addClass('is-active');
}

function reportModalClose(){
	$('#ncCommunityReportModal').removeClass('is-active');
	$('#ncCommunityReport').removeClass('is-active');
}

function reportInsert(obj){
	if (account == undefined) {
		//popupShow('로그인 후 이용하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Available after login.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	
	const reportType	= $('input:radio[name=reportCase]:checked').val(),
		reprotLog		= $('#reportLog').val(),
		reportTarget	= $('#ncCommunityReport .report-target .target').html();
	const sendData		= {targetName: reportTarget, type: reportType, log: reprotLog};
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/report/insert',
		data:sendData,
		success:function(data){
			popupShow(data, '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			reportModalClose();
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function likeBtn(obj){
	if (account == undefined) {
		//popupShow('로그인 후 이용하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Available after login.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	boardLikeBtn($(obj), '/define/pitch/like', {id : $(obj).attr('data-id')});
}

function commentLikeBtn(obj){
	if (account == undefined) {
		//popupShow('로그인 후 이용하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Available after login.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	boardLikeBtn($(obj), '/define/pitch/commentLike', {id : $(obj).attr('data-commentid'), boardnum : $(obj).attr('data-boardnum')});
}

function boardLikeBtn(obj, url, sendData){
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		url,
		data:		sendData,
		success:function(data){
			obj.find('em').html(Number(obj.find('em').html()) + data);
			if(data === 1)		obj.addClass('is-active');
			else if(data === -1)obj.removeClass('is-active');
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

function boardCommentWrite(){
	const comment	= $('.comment-form-textarea > textarea');
	if(comment.val().length <= 0){
		//popupShow('댓글을 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Please write a comment.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	const boardKey	= $('#ncCommunityView').attr('data-board-id'),
		boardRownum	= $('#ncCommunityView').attr('data-board-num');
	const sendData	= { content : comment.val(), boardId : boardKey };
	$.ajax ({
		type:"post",
		datatype:"json",
		url:"/define/pitch/commentInsert",
		data:sendData,
		success:function(data){
			var commentHtml = 
				'<div class="comment-article" data-commentid="' + data.id + '">' +
					'<div class="comment-info">' +
						'<span class="thumb">' +
							(data.chasex == 0 ? '<img src="/img/char/char' + data.chatype + '_m.png" alt="">' : '<img src="/img/char/char' + data.chatype + '_f.png" alt="">') +
						'</span>' +
						'<span class="writer">' + data.name + '<span class="server">' + serverName + '</span></span>' +
						'<span class="date">' + moment(data.date).format(getLocaleDateString() + ' HH:mm:ss') + '</span>' +
						//'<button class="co-btn btn-declare" data-commentid="' + data.id + '" data-boardnum="' + boardRownum + '" data-name="' + data.name + '" aria-label="신고" onClick="commentReportBtn(this);">&nbsp;신고</button>' +
						'<button class="co-btn btn-declare" data-commentid="' + data.id + '" data-boardnum="' + boardRownum + '" data-name="' + data.name + '" aria-label="Report" onClick="commentReportBtn(this);">&nbsp;Report</button>' +
						//'<button class="co-btn btn-delete" aria-label="Delete" onClick="deleteComment(this, ' + data.id + ');">&nbsp;삭제</button>' +
						'<button class="co-btn btn-delete" aria-label="Delete" onClick="deleteComment(this, ' + data.id + ');">&nbsp;Delete</button>' +
					'</div>' +
					'<div class="comment-contents">' + data.content + '</div>' +
					'<div class="comment-utils">' +
						//'<button data-commentid="' + data.id + '" data-boardnum="' + boardRownum + '" class="co-btn co-btn-like" aria-label="좋아요수" onClick="commentLikeBtn(this);">' +
						'<button data-commentid="' + data.id + '" data-boardnum="' + boardRownum + '" class="co-btn co-btn-like" aria-label="Like" onClick="commentLikeBtn(this);">' +
							'<svg class="fe-svg fe-svg-like_s" style="width: 16px; height: 16px; vertical-align: middle; fill: rgba(0,0,0,.45)!important; color: rgba(0,0,0,.45)!important;"><use xlink:href="#fe-svg-like_s"></use></svg>' +
							'&nbsp;<em class="text">' + (data.likenames != undefined ? data.likenames.length : 0) + '</em>' +
						'</button>' +
					'</div>' +
				'</div>';
			$(commentHtml).appendTo('.commentThread');// 댓글 추가
			$('.comment-article-none').remove();// 제거
			comment.val('');// 인풋 리셋
			var commentCount = $('.commentTotalCount');
			commentCount.html(Number(commentCount.html()) + 1);// 댓글수 변경
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function deleteComment(obj, commentId){
	//popupShow('댓글을 삭제하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteCommentAction(' + commentId + ');" class="close">Yes</a></span>');
	popupShow('Do you want to delete the comment?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteCommentAction(' + commentId + ');" class="close">Yes</a></span>');
}

function deleteCommentAction(commentId){
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/pitch/commentDelete",
		data:		{id : commentId},
		success:function(data){
			if(data){
				var commentList = $('.comment-article');
				$(commentList).each(function (index, item) {
					if(commentList.eq(index).attr('data-commentid') == commentId){
						item.remove();
					}
				})
				popupClose();
				var commentCount = $('.commentTotalCount');
				commentCount.html(Number(commentCount.html()) - 1);// 댓글수 변경
				//if(Number(commentCount.html()) <= 0)$('.commentThread').html('<div class="comment-article-none"><p>첫 댓글을 남겨주세요.</p></div>');
				if (Number(commentCount.html()) <= 0) {
					$('.commentThread').html('<div class="comment-article-none"><p>Please leave the first comment.</p></div>');
				}
			}else{
				//popupShow('댓글 삭제에 실패하였습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Failed to delete the comment.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function boardMore(){
	var totsize		= $('#totSize').val(),
		currsize	= $('#cursize').val();
	$.ajax ({
		type:"post",
		datatype:"json",
		url:"/define/pitch/more",
		data:{size:currsize},//현재리스트수전송
		success:function(data){
			let addhtml="";
			$.each(data,function(key,element){//리스트수만큼 반복
				addhtml += createBoardHtml(element);
            });
            $(addhtml).appendTo(".ncCommunityBoardList");//뒤에 이어서 html넣기
            if(data.length > 0){
            	$('#cursize').val(Number(currsize) + Number(data.length));//리스트수갱신
            	$('#totSize').val(Number(totsize) - Number(data.length));//총길이 갱신
            	if($('#totSize').val() <= 0){
            		$('.wrap-community-more').html('');
            	}else{
            		$('.wrap-community-more').html('<button class="nc-comment-more" onClick="javascritp:boardMore();"><span class="txt">Show more</span></button>');
            	}
            }
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function createBoardHtml(element){
	var boardType	= $('.ncCommunityBoardList'),
		listtype	= "",
		cardtype	= "display:none;";
	if(boardType.hasClass('board-list-card')){
		listtype	= "display:none;";
		cardtype	= "";
	}
	var dateterm = moment(getTime).format('YYYYMMDD') - moment(element.date).format('YYYYMMDD');
	return '<li class="board-items" onClick=\'urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'>' +
				'<div class="title board-type-list" style=\"' + listtype + '\">' +
					//'<span class="category">홍보&nbsp;</span>' +
					'<span class="category">Promotion&nbsp;</span>' +
					'<a href=\'javascript:urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'>' + element.title + '</a>' +
					(element.mainImg != undefined ? '<svg class="fe-svg fe-svg-picture"><use xlink:href="#fe-svg-picture"></use></svg>' : '') +
					'<div class="count">' +
        			'<span class="count-like" data-count="' + (element.likenames != undefined ? element.likenames.length : 0) + '">' +
        				'&nbsp;<svg class="fe-svg fe-svg-like_s"><use xlink:href="#fe-svg-like_s"></use></svg>' +
        				'<em>' + (element.likenames != undefined ? element.likenames.length : 0) + '</em>' +
        			'</span>' +
        			'<span class="count-comment" data-count="' + (element.answerList != undefined ? element.answerList.length : 0) + '">' +
        				'&nbsp;<svg class="fe-svg fe-svg-comment"><use xlink:href="#fe-svg-comment"></use></svg>' +
        				'<em>' + (element.answerList != undefined ? element.answerList.length : 0) + '</em>' +
        			'</span>' +
        			'</div>' +
					(dateterm===0 ? '&nbsp<i class="fe-icon-new" data-diff=""></i>' : '') +
				'</div>' +
	
				'<div class="info board-type-list" style=\"' + listtype + '\">' +
				    '<span class="writer">' + (element.type!=undefined ? '<img src="/img/lineage_writer.png" alt="">' : element.name + '<span class="server">' + serverName + '</span>') + '</span>&nbsp' +
				    '<span class="date">' + moment(element.date).format(getLocaleDateString()) + '</span>&nbsp' +
				    '<span class="hit"><em class="txt">Views</em>' + element.readcount + '</span>' +
				    //'<span class="like">추천<em>' + (element.likenames != undefined ? element.likenames.length : 0) + '</em></span>' +
					'<span class="like">Like<em>' + (element.likenames != undefined ? element.likenames.length : 0) + '</em></span>' +
				'</div>' +
				
				'<div class="comment">' +
				'<a href=\'javascript:urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'>' +
					'<span><em class="number">' + (element.answerList != undefined ? element.answerList.length : 0) + '</em><em class="txt">Comment</em></span>' +
					'</a>' +
				'</div>' +

				'<div class="board-type-card" style=\"' + cardtype + '\">' +
				(element.mainImg != undefined ? '<div class="board-items-thumb"><a href=\'javascript:urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'><img src=\"' + element.mainImg + '\" alt=""></a></div>' : '') +
					'<div class="board-items-contents">' +
						'<div class="title">' +
							//'<span class="category">홍보&nbsp;</span>' +
							'<span class="category">Promotion&nbsp;</span>' +
							'<a href=\'javascript:urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'>' + element.title +
							(dateterm===0 ? '&nbsp<i class="fe-icon-new"></i></a>' : '') +
						'</div>' +
						'<div class="desc desc-overflow">' +
							'<a href=\'javascript:urlform(\"' + element.rownum + '\", "post", "/pitch/view");\'>' + element.content + '</a>' +
						'</div>' +
						'<span class="writer">' + (element.type!=undefined ? '<img src="/img/lineage_writer.png" alt="">' : element.name + '<span class="server">' + serverName + '</span>') + '</span>&nbsp' +
					'</div>' +
					'<div class="board-items-footer">' +
						'<div class="info">' +
							'<span class="writer">' + element.name + '</span>' +
							'<span class="date">' + moment(element.date).format(getLocaleDateString()) + '</span>&nbsp' +
							'<span class="hit-count"><span class="txt">Views </span><em>' + element.readcount + '</em></span>' +
						'</div>' +
						'<div class="count">' +
	        				'<span class="count-like" data-count="' + (element.likenames != undefined ? element.likenames.length : 0) + '">' +
	        					'<svg class="fe-svg fe-svg-like_s"><use xlink:href="#fe-svg-like_s"></use></svg><em>' + (element.likenames != undefined ? element.likenames.length : 0) + '</em>' +
	        				'</span>' +
	        				'<span class="count-comment" data-count="' + (element.answerList != undefined ? element.answerList.length : 0) + '">' +
	        					'<svg class="fe-svg fe-svg-comment"><use xlink:href="#fe-svg-comment"></use></svg><em>' + (element.answerList != undefined ? element.answerList.length : 0) + '</em>' +
	        				'</span>' +
        				'</div>' +
					'</div>' +
				'</div>' +
			'</li>';
}

function boardWriteFormCheck(){
	var f=document.boardWriteForm;
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

function delectConfirm(rownum){
	//popupShow('정말로 삭제하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteConfirmAction(' + rownum + ');" class="close">Yes</a></span>');
	popupShow('Are you sure you want to delete it?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteConfirmAction(' + rownum + ');" class="close">Yes</a></span>');
}

function deleteConfirmAction(rownum){
	const deleteImgList = $('.board-view .view-body').find('img');
	urlform(rownum, 'post', '/pitch/delete' + getDeleteImgSrc(deleteImgList));
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

function pitchCancel(cancelType){
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