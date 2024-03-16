$(function() {
	var createPoromotion = function(promotion_data){
		const isGm = account != undefined && account.gm;
		let promotion_list = promotion_data.map((element, i) => {
			return '<li data-title="' + element.title + '" data-content="' + element.subText + '" data-date="' + element.promotionDate + '" data-link="' + element.targetLink + '" data-img="' + element.promotionImg + '" data-subimg="' + element.listallImg + '">' +
			'<div class="bg-mobile slick-loading" style="background-image: url(\'' + element.promotionImg + '\'); opacity: 1; transition: opacity 500ms cubic-bezier(0.19, 1, 0.22, 1) 0s;"></div>' +
			'<div class="bg slick-loading" style="background-image: url(\'' + element.promotionImg + '\'); opacity: 1; transition: opacity 500ms cubic-bezier(0.19, 1, 0.22, 1) 0s;"></div>' +
			'<div class="article"><a href="/powerbook/search?searchType=4&query=' + element.targetLink + '" class="outlink"><span class="type"></span><strong class="title">' + element.title + '</strong><span class="desc">' + element.subText + '</span><span class="date">' + element.promotionDate + '</span></a></div>' +
			(isGm ? '<div class="promotion_admin_util"><button onClick="promotionModify(' + element.id + ', this);">Modify</button><button onClick="promotionDelete(' + element.id + ');">Delete</button><button onClick="promotionInsert();">Add</button></div>' : '') +
			'</li>';
		}).join('');
		$(".section-promotion__list > ul").html(promotion_list);
	}
	
	var createNotice = function(type, element){
		var noticeHtml = '';
		switch(type){
		case 'NOTICE':
			if (element.length <= 0) {
				noticeHtml = '<div class="mainContent_empty"><p>No announcements available.</p></div>';
			} else {
				var nowTime = moment(getTime).format('YYYYMMDD');
				$.each(element, function(key, notice){
					if (key == 0) {
						noticeHtml += '<a href="javascript:urlTypeform(\'0\', \'' + notice.rownum + '\', \'post\', \'/notice/view\');"><dl><dd class="thumb" style="background-image:url(\'' + (notice.mainImg != undefined ? notice.mainImg : '/img/mainContent/notice.jpg') + '\');"><dt>' + notice.title + '</dt></dl></a><ul>';
					} else {
						var dateterm = nowTime - moment(notice.date).format('YYYYMMDD');
						noticeHtml += '<li class="' + (dateterm == 0 ? 'is-new-true' : 'is-new-false') + '"><a href="javascript:urlTypeform(\'0\', \'' + notice.rownum + '\', \'post\', \'/notice/view\');">' + notice.title + '</a>' + (dateterm == 0 ? '<span class="icon-new"></span>' : '') + '</li>';
					}
				});
				noticeHtml += '</ul>'
			}
			$('.section-notice > article > ul').html(noticeHtml);
			break;
		case 'UPDATE':
			if (element.length <= 0) {
				//noticeHtml = '<div class="mainContent_empty"><p>업데이트가 없습니다.</p></div>';
				noticeHtml = '<div class="mainContent_empty"><p>No updates available.</p></div>';
			} else {
				noticeHtml = '<a href="javascript:urlTypeform(\'1\', \'' + element[0].rownum + '\', \'post\', \'/notice/view\');"><dl>' +
					'<dd class="thumb" style="background-image:url(\'' + (element[0].mainImg != undefined ? element[0].mainImg : '/img/mainContent/update.jpg') + '\');"></dd>' +
					'<dt>' + element[0].title + '</dt><dd class="desc" style="max-height: 24px; overflow: hidden;">' + element[0].content + '</dd></dl></a>';
			}
			$('.section-update > article').html(noticeHtml);
			break;
		default:
			if (element.length <= 0) {
				//noticeHtml = '<div class="mainContent_empty"><p>이벤트가 없습니다.</p></div>';
				noticeHtml = '<div class="mainContent_empty"><p>No events available.</p></div>';
			} else {
				noticeHtml = '<a href="javascript:urlTypeform(\'2\', \'' + element[0].rownum + '\', \'post\', \'/notice/view\');"><dl>' +
					'<dd class="thumb" style="background-image:url(\'' + (element[0].mainImg != undefined ? element[0].mainImg : '/img/mainContent/event.jpg') + '\');"></dd>' + 
					'<dt>' + element[0].title + '</dt><dd class="desc" style="max-height: 24px; overflow: hidden;">' + element[0].content + '</dd></dl></a>';
			}
			$('.section-event > article').html(noticeHtml);
			break;
		}
	}
	
	var createKeyword = function(element){
		let hotissue		= $('.section-hotissue .keyword'),
		hotissueHtml	= '';
		$.each(element, function(index, item){
			hotissueHtml += '<li>';
			if (item[0] == '-')	hotissueHtml += '<a href="javascript:;">';
			else				hotissueHtml += '<a href="/search?query=' + item[0] + '">';
			hotissueHtml += item[0] + '</a><span class="ui-rank ' + item[1] + '">' + item[2] + '</span></li>';
		});
		hotissue.html(hotissueHtml);
	}
	
	var createGuide = function(element){
		let guide_list = element.map((guide, i) => {
			//return '<div class="slick-item"><a href="/powerbook/search?searchType=4&query=' + guide.name + '"><dl><dd class="thumb"><span class="flag 파워북">Powerbook</span><img src="' + guide.info.mainImg + '" class="thumb"></dd><dt><strong>Powerbook</strong></dt><dd class="desc">' + guide.name + '</dd></dl></a></div>';
			return '<div class="slick-item"><a href="/powerbook/search?searchType=4&query=' + guide.name + '"><dl><dd class="thumb"><span class="flag powerbook">Powerbook</span><img src="' + guide.info.mainImg + '" class="thumb"></dd><dt><strong>Powerbook</strong></dt><dd class="desc">' + guide.name + '</dd></dl></a></div>';
		}).join('');
		$("#lineageworld").html(guide_list);
	}
	
	var createCommunity = function(element){
		var addhtml = '';
		if (element.length <= 0) {
			//addhtml = '<div class="mainContent_empty"><p>계시판 글이 없습니다.</p></div>';
			addhtml = '<div class="mainContent_empty"><p>No posts in the bulletin board.</p></div>';

		} else {
			$.each(element, function(key, item){
				addhtml += 
					'<div class="slick-item"><a href="javascript:urlform(\'' + item.rownum + '\', \'post\', \'/board/view\');">' +
					'<dl><dd class="thumb" style="background-image:url(\'' + (item.mainImg != undefined ? item.mainImg : '/img/updateThum/server_00' + (key + 1) + '.jpg') + '\');"><span class="flag">' + item.name + '</span></dd>' +
					'<dt><strong>' + item.title + '</strong></dt></dl></a></div>';
			});
		}
		$("#board_user").html(addhtml);
	}
	
	var createContent = function(element){
		var addhtml = '';
		if (element.length <= 0) {
			//addhtml = '<div class="mainContent_empty"><p>컨텐츠 공모가 없습니다.</p></div>';
			addhtml = '<div class="mainContent_empty"><p>No content submissions available.</p></div>';
		} else {
			$.each(element, function(key, item){
				addhtml += 
					'<div class="slick-item"><a href="javascript:urlform(\'' + item.rownum + '\', \'post\', \'/contents/view\');">' +
					'<dl><dd class="thumb" style="background-image:url(\'' + (item.mainImg != undefined ? item.mainImg : '/img/eventThum/event_00' + (key + 1) + '.jpg') + '\');"><span class="flag">' + item.name + '</span></dd>' +
					'<dt><strong>' + item.title + '</strong></dt></dl></a></div>';
			});
		}
		$("#content_user").html(addhtml);
	}
	
	var createGoods = function(element){
		if (element.length <= 0) {
			//$("#nshopRoll .slick-item").html('<div class="mainContent_empty"><p>N-SHOP 상품이 없습니다.</p></div>');
			$("#nshopRoll .slick-item").html('<div class="mainContent_empty"><p>No N-SHOP items available.</p></div>');
		} else {
			let nshop_list = element.map((item, i) => {
				return '<section class="nshop__item">' +
						'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');">' +
							'<span class="flag flag-none"></span>' +
							'<dl>' +
								'<dd class="thumb"><img src=\"/img/item/' + item.invgfx + '.png\" alert=\"' + item.itemname + '\"></dd>' +
								//'<dt>' + item.itemname + (item.pack > 0 ? '&nbsp;' + item.pack + '개' : '') + '</dt>' +
								'<dt>' + item.itemname + (item.pack > 0 ? '&nbsp;' + item.pack + ' units' : '') + '</dt>' +
								'<dd class="price">' +
									(item.price_type === 'NCOIN' ? '<i class="price_ncoin"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path fill="#F16126" d="M18.965 12.51l-6.455 6.455a3.56 3.56 0 0 1-5.02 0L1.035 12.51a3.56 3.56 0 0 1 0-5.02L7.49 1.035a3.56 3.56 0 0 1 5.02 0l6.455 6.455a3.56 3.56 0 0 1 0 5.02"></path><path fill="#FFF" d="M12.5 6.989s-2.374-.279-2.809 0c0 0-1.02.471-1.02 1.957v2.108c-.018 1.486 1.02 1.957 1.02 1.957.435.279 2.809 0 2.809 0v1.829c-.877.277-2.68.26-2.68.26-3.038-.002-3.32-3.394-3.32-3.394V8.294S6.782 4.902 9.82 4.9c0 0 1.803-.017 2.68.261V6.99z"></path></svg></i>'
									: '<i class="price_npoint"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path fill="#F16126" d="M18.965 12.51l-6.455 6.455a3.56 3.56 0 0 1-5.02 0L1.035 12.51a3.56 3.56 0 0 1 0-5.02L7.49 1.035a3.56 3.56 0 0 1 5.02 0l6.455 6.455a3.56 3.56 0 0 1 0 5.02"></path><path fill="#FFF" d="M12.5 6.989s-2.374-.279-2.809 0c0 0-1.02.471-1.02 1.957v2.108c-.018 1.486 1.02 1.957 1.02 1.957.435.279 2.809 0 2.809 0v1.829c-.877.277-2.68.26-2.68.26-3.038-.002-3.32-3.394-3.32-3.394V8.294S6.782 4.902 9.82 4.9c0 0 1.803-.017 2.68.261V6.99z"></path></svg></i>') +
									'<strong>' + commaAdd(item.price) + '</strong>' +
								'</dd>' +
							'</dl>' +
						'</a>' +
					'</section>';
			}).join('');
			$("#nshopRoll .slick-item").html(nshop_list);
		}
	}

	var createModal = function(element){
		const modal_title = element.TITLE;
		const modal_src = element.SRC;
		const modal_cookie_name = element.COOKIE_NAME;
		const modal_cookie_value = element.COOKIE_VALUE;
		const modal_cookie = GetCookie(modal_cookie_name);
		if (modal_cookie != modal_cookie_value) {
			const modal_html = '<div id="NC-banner-movie-backdrop" class="nc-backdrop nc-backdrop--blur nc-backdrop--show"></div><div id="NC-banner-movie" class="nc-modal nc-modal--lineage nc-modal--center nc-modal--show" tabindex="0">' +
			'<div class="nc-modal__dialog" role="document"><div class="nc-modal__content">' +
			'<div class="nc-modal__header"><div class="nc-modal__title"></div><button type="button" class="nc-modal__close"><svg viewBox="0 0 21 20" xmlns="http://www.w3.org/2000/svg"><g fill="none" fill-rule="evenodd" stroke-linecap="square"><path d="m1.238 0.238 19.145 19.145m-19.145 0 9.549-9.55 7.398-7.397 2.198-2.198"></path></g></svg></button></div>' +
			'<div class="nc-modal__body"><iframe width="100%" height="100%" src="' + modal_src + '" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen="" title="' + modal_title + '"></iframe></div>' +
			'<div class="nc-modal__footer"><div><button type="button" class="nc-modal__btn nc-modal__btn--pri"><span class="nc-modal__btn-text">Stop</span></button><button type="button" class="nc-modal__btn nc-modal__btn--sec"><span class="nc-modal__btn-text">Close</span></button></div></div>' +
			'</div></div></div>';
			$('#container').after(modal_html);
			$('body').addClass('modal-open');
			includeJS('/js/modal.js');
		}
	}
	
	// 데이터 바인드후 스크립트 로드
	function indexJSOnload() {
		var element = document.createElement("script");
		element.src = "/js/main.js";
		document.body.appendChild(element);
	}

	// index 페이지 데이터 로드
	$.each(index_data, function(key, element){
		switch(element.key){
		case 'PROMOTIONS':
			createPoromotion(element.value);
			break;
		case 'KEYWORD':
			createKeyword(element.value);
			break;
		case 'GOUIDE':
			createGuide(element.value);
			break;
		case 'COMMUNITY':
			createCommunity(element.value);
			break;
		case 'CONTENT':
			createContent(element.value);
			break;
		case 'GOODS':
			createGoods(element.value);
			break
		case 'NOTICE':
		case 'UPDATE':
		case 'EVENT':
			createNotice(element.key, element.value);
			break;
		case 'MODAL':
			createModal(element.value);
			break;
		default:
			console.log('UNDEFINED_INDEX_DATA_KEY : ' + element.key);
			break;
		}
            });
	indexJSOnload();
	
});