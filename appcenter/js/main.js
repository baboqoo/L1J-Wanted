$(function() {
	let windWidth			= window.outerWidth;
	const promotionList		= $('.section-promotion__list > ul > li'),
		promoTitle			= $('.section-promotion__list .article .title'),
		promoSubTitle		= $('.section-promotion__list .article .desc'),
		promoUrl			= $('.section-promotion__list .article .outlink');
	
	// page-total
	$('.page-no .total').html(promoTitle.length);
	
	// listall
	let listallHtml = '';
	for (var i=0; i<promotionList.length; i++) {
		listallHtml += '<li><a href="' + promoUrl.eq(i).attr('href') + '"><img src="' + promotionList.eq(i).attr('data-subimg') + '" class="thumb"><div class="article"><strong class="subject">' + promoTitle.eq(i).html() + '</strong><span class="date">' + promoSubTitle.eq(i).html() + '</span></div></a></li>';
	}
	$('.wrap-listall ul').html(listallHtml);
	
	$promotionSlick = $('.section-promotion__list > ul').slick({
		slidesToShow:	1,
        slidesToScroll:	1,
        infinite:		true,
        dots:			true,
        arrows:			true,
        autoplay:		true,
        autoplaySpeed:	6000,
        pauseOnHover:	false,
        appendDots: 	'.pagination-pc',
        customPaging: function(slick, index) {
        	return '<a class="">' + promoTitle.eq(index).html() + '</a>';
        },
        prevArrow:		$('.pagination-pc .btn-prev'),
        nextArrow:		$('.pagination-pc .btn-next'),
        responsive: [ // 반응형 웹 구현 옵션
        	{  
				breakpoint: 640, // 화면 사이즈 640px
				settings: {
					appendDots: '.pagination-mobile .handle',
			        customPaging: function(slick, index) {
			        	return '<a class="">' + promoTitle.eq(index).html() + '</a>';
			        },
			        prevArrow: $('.btn-prev-mobile'),
			        nextArrow: $('.btn-next-mobile')
				}
			},
			{  
				breakpoint: 960, // 화면 사이즈 960px
				settings: {
					appendDots: '.pagination-mobile .handle',
			        customPaging: function(slick, index) {
			        	return '<a class="">' + promoTitle.eq(index).html() + '</a>';
			        },
			        prevArrow: $('.btn-prev-mobile'),
			        nextArrow: $('.btn-next-mobile')
				}
			}
		]
	});
	
	var promotionPcPager = function(){
		$('.pagination-pc .slick-dots').addClass('btns');
		$('.pagination-pc .btns li > a').eq($promotionSlick.slick('slickCurrentSlide')).addClass('on');
		promotionPcPagerWidth();
	}
	
	var promotionPcPagerWidth = function(){
		var pagerPcOutWidth = $('.pagination-pc').width() >> 2;
		$('.pagination-pc .btns > li').each(function(index){
			$(this).css({
				'width':		pagerPcOutWidth + 'px', 
				'transform':	'translateX(' + (index * pagerPcOutWidth) + 'px)',
				'display':		index >= 4 ? 'none' : ''
			});
		});
	}
	
	var promotionMobliePager = function(){
		$('.pagination-mobile .slick-dots').addClass('btns');
		$('.pagination-mobile .btns li > a').eq($promotionSlick.slick('slickCurrentSlide')).addClass('on');
		$('.pagination-mobile .btns > li').each(function(index){
			$(this).css({
				'width':	'50%',
				'display':	index >= 2 ? 'none' : ''
			});
		});
	}
	
	var promotionMoblieNonePager = function(){
		$('.pagination-mobile .slick-dots').addClass('btns');
		$('.pagination-mobile .btns li > a').eq($promotionSlick.slick('slickCurrentSlide')).addClass('on');
		$('.pagination-mobile .btns > li').each(function(index){
			$(this).css({
				'width':	'50%',
				'display':	index >= 2 ? 'none' : ''
			});
		});
		$('.btn-prev-mobile').css('display', 'none');
		$('.btn-next-mobile').css('display', 'none');
	}
	
	var devicePromo = windWidth <= 640 ? 2 : windWidth <= 960 ? 1 : 0;
	$promotionSlick.on('breakpoint', function(e, activeBreakpoint){
		var pointer = activeBreakpoint.activeBreakpoint;
		if (pointer === 960 && devicePromo !== 1) {
			$('.btn-prev-mobile').css('display', 'block');
			$('.btn-next-mobile').css('display', 'block');
			promotionMobliePager();
			devicePromo = 1;
		} else if (pointer === 640) {
			$('.btn-prev-mobile').css('display', 'none');
			$('.btn-next-mobile').css('display', 'none');
			promotionMobliePager();
			devicePromo = 2;
		} else if (devicePromo !== 0) {
			promotionPcPager();
			devicePromo = 0;
		}
	});
	
	jQuery(document).ready(function(){
		if (devicePromo === 0) {
			promotionPcPager();
		} else if (devicePromo === 1) {
			promotionMobliePager();
		} else {
			promotionMoblieNonePager();
		}
	});
	
	var paginationWidth = $('.pagination-pc').width();
	$(window).resize(function (){
		if (paginationWidth !== $('.pagination-pc').width()) {
			paginationWidth = $('.pagination-pc').width();
			promotionPcPagerWidth();
		}
	})
	
	// promotion change
	$promotionSlick.on('beforeChange', function(event, slick, currentSlide, nextSlide){
		const pageList = $('.pagination-wrap .btns li');
		$('.page-no .current').html(nextSlide + 1);
		pageList.children('a').removeClass('on');
		pageList.children('a').eq(nextSlide).addClass('on');
	    if (devicePromo === 1) {// 모바일
	    	if (pageList.eq(nextSlide).css('display') === 'none') {
	    		let pageCnt = 0;
	    		for (var i=0; i<pageList.length; i++) {
	    			if (i >= nextSlide) {
	    				pageList.eq(i).css('display', pageCnt < 2 ? '' : 'none');
	    				pageCnt++;
	    			}
	    		}
	    		let resCnt = pageList.length - pageCnt;
	    		for (var i=0; i<resCnt; i++) {
	    			pageList.eq(i).css('display', pageCnt < 2 ? '' : 'none');
	    			pageCnt++;
	    		}
	    	}
	    } else if (devicePromo === 0) {// pc
	    	if (pageList.eq(nextSlide).css('display') === 'none') {
	    		let pageCnt = 0;
	    		for (var i=0; i<pageList.length; i++) {
	    			if (i >= nextSlide) {
	    				pageList.eq(i).css({
	    					'transform':	'translateX(' + (pageList.width() * pageCnt) + 'px)',
	    					'display':		pageCnt < 4 ? '' : 'none'
	    				});
	    				pageCnt++;
	    			}
	    		}
	    		let resCnt = pageList.length - pageCnt;
	    		for (var i=0; i<resCnt; i++) {
	    			pageList.eq(i).css({
	    				'transform':	'translateX(' + (pageList.width() * pageCnt) + 'px)',
	    				'display':		pageCnt < 4 ? '' : 'none'
	    			});
	    			pageCnt++;
	    		}
	    	}
	    }
	    $promotionSlick.slick('slickPlay');
	});
	
	$('.btn-listall').on('click', function(event) {// 출현
		$('.section-promotion__listall').toggleClass('on');
	});
	
	$('.section-hotissue').on('click', function(){
		$(this).toggleClass('on');
	});

	$('#grade-layer-item').on('click', function(){
		$('#FooterGradeLayer').toggleClass('is-show-footer');
	});
	
	// slick Update
	$('#lineageworld').slick({
        dots:			true,
        arrows:			false,
        infinite:		true,
        slidesToShow:	6,
        slidesToScroll:	6,
        responsive: [
          {
            breakpoint: 960,
            settings: {
              slidesToShow: 3,
              slidesToScroll: 3
            }
          },
          {
            breakpoint: 480,
            settings: {
              slidesToShow: 2,
              slidesToScroll: 2
            }
          }
        ]
	});
	
	$('.section-powerbookcartoon').slick({
        dots:			true,
        arrows:			false,
        infinite:		true,
        slidesToShow:	2,
        slidesToScroll:	2,
        responsive: [
          {
            breakpoint: 640,
            settings: {
              slidesToShow: 1,
              slidesToScroll: 1
            }
          }
        ]
	});
	
	$('#nshopRoll').slick({
        dots:			true,
        arrows:			false,
        infinite:		true,
        slidesToShow:	1,
        slidesToScroll:	1,
        responsive: [
          {
            breakpoint: 640,
            settings: {
              slidesToShow: 1,
              slidesToScroll: 1
            }
          }
        ]
	});

	if (account !== undefined && account.ingame && account.gm) {
		$('.section-etcservice').prepend('<a href=\"javascript:L1_FUNCTION();\">View function</a>');
	}
	
});

function promoFileChange(obj){
	$(obj).closest('.filebox').find(".upload-name").val($(obj).val());
}

function promotionUtilForm(type){
	var promoHtml = 
		'<div class="promotionUtilForm">' +
			'<div class="promotionUtilForm_container">' +
				'<form name="promoForm" onsubmit="return false;">' +
				//'<h1>프로모션</h1>' +
				'<h1>Promotion</h1>' +
				'<input type="hidden" name="promoId" id="promoId" />' +
				'<input type="hidden" name="dividPath" id="dividPath" value="PROMO_"/>' +
				/*'<div class="util_items"><label>Title</label><input type="text" name="promoTitle" id="promoTitle" placeholder="필수항목" /></div>' +
				'<div class="util_items"><label>Detail</label><input type="text" name="promoContent" id="promoContent" placeholder="필수항목" /></div>' +
				'<div class="util_items"><label>날짜</label><input type="text" name="promoDate" id="promoDate" placeholder="생략가능" /></div>' +
				'<div class="util_items"><label>링크</label><input type="text" name="promoLink" id="promoLink" placeholder="필수항목(가이드 제목)" /></div>' +
				'<div class="util_items filebox"><label>이미지</label><i><input class="upload-name" id="promoImgStr" name="promoImgStr" placeholder="필수항목(이미지 파일)" readonly><label class="file-label" for="promoImg">파일찾기</label><input type="file" name="promoImg" id="promoImg" class="file" onchange="promoFileChange(this);" ></i></div>' +
				'<div class="util_items filebox"><label>보조 이미지</label><i><input class="upload-name" id="promoSubImgStr" name="promoSubImgStr" placeholder="필수항목(이미지 파일)" readonly><label class="file-label" for="promoSubImg">파일찾기</label><input type="file" name="promoSubImg" id="promoSubImg" class="file" onchange="promoFileChange(this);" ></i></div>' +*/
				'<div class="util_items"><label>Title</label><input type="text" name="promoTitle" id="promoTitle" placeholder="Required" /></div>' +
				'<div class="util_items"><label>Detail</label><input type="text" name="promoContent" id="promoContent" placeholder="Required" /></div>' +
				'<div class="util_items"><label>Date</label><input type="text" name="promoDate" id="promoDate" placeholder="Optional" /></div>' +
				'<div class="util_items"><label>Link</label><input type="text" name="promoLink" id="promoLink" placeholder="Required (Guide Title)" /></div>' +
				'<div class="util_items filebox"><label>Image</label><i><input class="upload-name" id="promoImgStr" name="promoImgStr" placeholder="Required (Image File)" readonly><label class="file-label" for="promoImg">Browse</label><input type="file" name="promoImg" id="promoImg" class="file" onchange="promoFileChange(this);"></i></div>' +
				'<div class="util_items filebox"><label>Sub Image</label><i><input class="upload-name" id="promoSubImgStr" name="promoSubImgStr" placeholder="Required (Image File)" readonly><label class="file-label" for="promoSubImg">Browse</label><input type="file" name="promoSubImg" id="promoSubImg" class="file" onchange="promoFileChange(this);"></i></div>' +
				(type == 'update' ? '<div class="btns"><button onClick="promotionCancel();return false;">Cancel</button><button onClick="promotionUpdate();return false;">Modify</button></div>' : '<div class="btns"><button onClick="promotionCancel();return false;">Cancel</button><button onClick="promotionAdd();return false;">Add</button></div>') +
				'</form>' +
			'</div>' +
		'</div>';
	$('#container').append(promoHtml);
}

function promotionInsert(id, obj){
	const promoMax	= 16;
	const promoSize	= $('.section-promotion__listall .wrap-listall > ul > li').length;
	if (promoSize >= promoMax) {
		//alert('프로모션은 최대 ' + promoMax + '개까지 등록할 수 있습니다.');
		alert('You can register up to ' + promoMax + ' promotions.');
		return;
	}
	promotionUtilForm('insert');
}

function promotionAdd(){
	var f = document.promoForm;
	if (!f.promoTitle.value || !f.promoContent.value || !f.promoLink.value || !f.promoImg.value || !f.promoSubImg.value) {
		//alert('필수항목을 입력하십시오.');
		alert('Please enter the required information.');
		return;
	}
	const limitSize = 1 * 1024 * 1024;// 1MB
	if ($('#promoImg').prop('files')[0].size > limitSize || $('#promoSubImg').prop('files')[0].size > limitSize) {
		//alert('이미지는 1MB이하로 등록하십시오.');
		alert('Please upload an image under 1MB.');
		return;
	}
	
	var data = new FormData(f);
	$.ajax ({
		data:			data,
		type:			'POST',
		url:			"/define/promotion/insert",
		contentType:	false,
		processData:	false,
		cache:			false,
		timeout:		600000,
		success:function(res){
			if(res)location.href='/index';
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function promotionModify(id, obj){
	promotionUtilForm('update');
	const promoTarget = $(obj).closest('li');
	$('.promotionUtilForm #promoId').val(id);
	$('.promotionUtilForm #promoTitle').val(promoTarget.attr('data-title'));
	$('.promotionUtilForm #promoContent').val(promoTarget.attr('data-content'));
	$('.promotionUtilForm #promoDate').val(promoTarget.attr('data-date'));
	$('.promotionUtilForm #promoLink').val(promoTarget.attr('data-link'));
	$('.promotionUtilForm #promoImg').closest('.filebox').find(".upload-name").val(promoTarget.attr('data-img'));
	$('.promotionUtilForm #promoSubImg').closest('.filebox').find(".upload-name").val(promoTarget.attr('data-subimg'));
}

function promotionUpdate(){
	var f = document.promoForm;
	if (!f.promoId.value || !f.promoTitle.value || !f.promoContent.value || !f.promoLink.value || !f.promoImgStr.value || !f.promoSubImgStr.value) {
		//alert('필수항목을 입력하십시오.');
		alert('Please enter the required information.');
		return;
	}
	const limitSize		= 1 * 1024 * 1024;// 1MB
	const mainPromoImg	= $('#promoImg').prop('files')[0];
	const subPromoImg	= $('#promoSubImg').prop('files')[0];
	if ((mainPromoImg != undefined && mainPromoImg.size > limitSize) || (subPromoImg != undefined && subPromoImg.size > limitSize)) {
		//alert('이미지는 1MB이하로 등록하십시오.');
		alert('Please upload an image under 1MB.');
		return;
	}
	var data = new FormData(f);
	$.ajax ({
		type:			'POST',
		processData:	false,
		contentType:	false,
		cache:			false,
		url:			"/define/promotion/update",
		data:			data,
		timeout:		600000,
		success:function(res){
			if (res) {
				location.href='/index';
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function promotionDelete(id){
	//if (confirm('정말로 삭제 하시겠습니까?')) {
	if (confirm('Are you sure you want to delete it?')) {
		const sendData = {'id':id};
		$.ajax ({
			type:		"POST",
			datatype:	"json",
			url:		"/define/promotion/delete",
			data:		sendData,
			success:function(data){
				if (data) {
					location.href='/index';
				}
			}, error: function(request, status, error){
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
}

function promotionCancel(){
	$('.promotionUtilForm').remove();
}

// 인게임 브라우저 내장 함수 보기
function L1_FUNCTION() {
	var property_names = Object.getOwnPropertyNames(L1);
	var property_html = '<div id=\"l1_functions\" style=\"position: fixed; left: 35%; top: 10%; z-index: 111111111; padding: 10px; background: #000; color: #fff; max-height: 400px;overflow: auto;\"><ul>';
	for (var i=0; i<property_names.length; i++) {
		property_html += '<li><strong>' + property_names[i] + '</strong></li>';
	}
	property_html += '</ul><a href=\"javascript:$(\'#l1_functions\').remove();\" style=\"margin: auto; display: table; padding: 10px; background: cadetblue; color: #fff; font-weight: 600;\">Close</a></div>';
	$('body').prepend(property_html);
}
