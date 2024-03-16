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

$(function() {
	includeHTML(document.querySelector('#svg-container'), '/svg.html');
	if (account === undefined || account.firstChar === undefined) {
		//$('.ncCommunityBoardList').html('캐릭터 정보를 찾을 수 없습니다.');
		$('.ncCommunityBoardList').html('Unable to find character information.');
		return;
	}
		var boardParam = {
				boardType: 'board'	
		};
		var paginationRender = function(){
	   		let container = $('.pagination-container');
	   		container.pagination({
	   			dataSource: function(done) {
	           	    $.ajax({
	           	        type: 'POST',
	           	     	data: boardParam,
	           	        url: '/define/account/board',
	           	        success: function(response) {
	           	            done(response);
	           	        }
	           	    });
	           	},
	           	pageSize: 10,// 한 화면에 보여질 개수
	           	showPrevious: false,// 처음버튼 삭제
	           	showNext: false,// 마지막버튼 삭제
	           	showPageNumbers: true,// 페이지 번호표시
	           	callback: function (data, pagination) {// 화면 출력
	           		var dataHtml = '';
	           		if (data.length > 0) {
	           			$.each(data, function (index, item) {
	           				dataHtml += createBoard(item);
	           			});
	           			$('.ncCommunityBoardList').addClass('board-list-default');
	           		} else {
	           			dataHtml += '<li>No registered posts.</li>';
	           			$('.ncCommunityBoardList').addClass('board-list-none');
	           		}
	           		$(".ncCommunityBoardList").html(dataHtml);// 렌더링
			$('.nc-community-loader').css('display', 'none');
	           	}
	        });
	    }
		
		var createBoard = function(item){
			var readCount = item.board.readcount !== undefined ? item.board.readcount : 0;
			var likeCount = item.board.likenames !== undefined && item.board.likenames.length > 0 ? item.board.likenames.length : 0;
			var commentCount = item.board.answerList !== undefined && item.board.answerList.length > 0 ? item.board.answerList.length : 0;
			var url = getBoardUrl(item);
			return '<li class="board-items">' +
	       				'<div class="title">' +
	   					'<span class="category">' + item.boardType + '</span>' +
	   					'<a href="' + url + '">' + item.board.title + '</a>' +
	   					'<div class="count">' +
	   						getMainImage(item.board) + 
	   						getLikeCount(item.board) +
	   						getCommentCount(item.board) + 
	   					'</div>' +
	   				'</div>' +
	   				'<div class="info">' +
	   					'<span class="writer">' + getWriter(item) + '<span class="server">' + serverName + '</span></span>' +
	   					'<span class="date">' + getDate(item) + '</span>' +
	   					'<span class="hit"><em class="txt">Views</em>' + readCount + '</span>' +
	   					//'<span class="like" data-count="3">추천<em>' + likeCount + '</em></span>' +
						   '<span class="like" data-count="3">Recommendations<em>' + likeCount + '</em></span>' +
	   				'</div>' +
	   				'<div class="comment"><a href="' + url + '"><span><em class="number">' + commentCount + '</em><em class="txt">Comment</em></span></a>' +
	   				'</div>' +
	   			'</li>';
		}
		
		var getWriter = function(item){
			if (item.boardType === 'Exchange (Marketplace)')
				return item.board.sellerCharacter;
			return item.board.name;
		}
		
		var getDate = function(item){
			if (item.boardType === 'Exchange (Marketplace)')
				return moment(item.board.writeTime).format(getLocaleDateString());
			return moment(item.board.date).format(getLocaleDateString());
		}
		
		var getBoardUrl = function(item){
			return 'javascript:urlform(\'' + item.board.rownum + '\', \'post\', \'' + item.boardUrl + '\');';
		}
		
		var getLikeCount = function(board){
			if (board.likenames !== undefined && board.likenames.length > 0)
				return '&nbsp;<span class="count-like"><svg class="fe-svg fe-svg-like_s"><use xlink:href="#fe-svg-like_s"></use></svg><em>' + board.likenames.length + '</em></span>';
			return '';
		}
		
		var getCommentCount = function(board){
			if (board.answerList !== undefined && board.answerList.length > 0)
				return '&nbsp;<span class="count-comment"><svg class="fe-svg fe-svg-comment"><use xlink:href="#fe-svg-comment"></use></svg><em>' + board.answerList.length + '</em></span>';
			return '';
		}
		
		var getMainImage = function(board){
			if (board.mainImg !== undefined)
				return '&nbsp;<svg class="fe-svg fe-svg-picture"><use xlink:href="#fe-svg-picture"></use></svg>';
			return '';
		}
		
		var topscrollBtn		= $('.co-btn-top_list');
	 	topscrollBtn.on('click', function(e) {
			$('html, body').stop().animate({
				scrollTop: ($('body').offset().top)
			}, 600);
			e.preventDefault();
		});
	 	
	 	$(document).ready(function(){
	 		paginationRender();
	 	});
});