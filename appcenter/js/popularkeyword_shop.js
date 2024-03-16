$(function() {
	let keywordRollingHtml	= '',
	keywordlistHtml		= '';
	$.each(popularKeyword.list, function(index, item){
		keywordRollingHtml +=
			'<li style="top: 0px; left: 0px; transform: translateY(' + (index === 0 ? '0' : '-20') + 'px); position: absolute; transition: transform 900ms cubic-bezier(0.19, 1, 0.22, 1) 0s;">'
			+ (item[0] === '-' ? '<a href="javascript:;">' : '<a href="/my/item-search?keyword=' + item[0] + '">') + item[0] + '</a>'
			+ '<span class="ui-rank ' + item[1] + '">' + item[2] + '</span>'
			+ '</li>';
	    			
		keywordlistHtml += '<li>' + (item[0] === '-' ? '<a href="javascript:;">' : '<a href="/my/item-search?keyword=' + item[0] + '">') + item[0] + '</a><span class="ui-rank ' + item[1] + '">' + item[2] + '</span></li>';
	});
	$('.keyword__rolling .keyword').html(keywordRollingHtml);
	$('.keyword__list .keyword').html(keywordlistHtml);
	    		
	// rolling
	let $keywordRolling	= $('.keyword__rolling .keyword'),
	keywordList		= $('.keyword__rolling .keyword > li'),
	keywordListCnt	= $('.keyword__rolling .keyword > li').length;
	let rollingInterval;
	let rollingIndex = 0;
	var playKeyword=function(){
		keywordList.css("transform", 'translateY(-20px)');
		rollingIndex++
		if (rollingIndex >= keywordListCnt) {
			rollingIndex = 0;
		}
		keywordList.eq(rollingIndex).css("transform", 'translateY(0px)');
	};

	// 자동슬라이드
	var rollingStart = function(){
		rollingInterval=setInterval(function(){ playKeyword() }, 3000);
	};
	if (keywordListCnt > 1) {
		rollingStart();
	}
	$('.section-keyword .btn-toggle').on('click', function(e){
		if ($('.section-keyword').hasClass('on')) {
			$('.section-keyword').removeClass('on');
			$('.keyword__list').css('display', 'none');
		} else {
			$('.section-keyword').addClass('on');
			$('.keyword__list').css('display', 'block');
		}
		$('.suggest_wrap').css('display', 'none');
		eventStop(e);
	});
	
	$('body').on('click', function (e){
		if ($('.keyword__list').has(e.target).length === 0 && $('.section-keyword.on .btn-toggle').has(e.target).length === 0 && $('.section-keyword').hasClass('on')) {
			$('.section-keyword').removeClass('on');
			$('.keyword__list').css('display', 'none');
		}
	});
});