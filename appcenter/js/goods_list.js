var goods_items = [];
$(function() {
	if (query === undefined) {
		query = 'ALL';
	}
	let recommendSearch	= '100 million adena coupon';
	let defaultSearch	= query !== null && query.trim() !== '' && query.trim() !== 'ALL' ? query : recommendSearch;
	$('#searchForm #SearchInput').val(defaultSearch);
	
	let container = $('.pagination-container');
    container.pagination({
    	dataSource: function(done) {
    	    $.ajax({
    	        type: 'POST',
    	        url: '/define/goods',
    	        data: { 'query' : query },
    	        success: function(response) {
    	            done(response);
    	        }
    	    });
    	},
    	pageSize:			18,// 한 화면에 보여질 개수
    	showPrevious:		false,// 처음버튼 삭제
        showNext:			false,// 마지막버튼 삭제
        showPageNumbers:	true,// 페이지 번호표시
        callback: function (data, pagination) {// 화면 출력
            let dataHtml = '';
	goods_items = [];
            if (data.length > 0) {
            	$.each(data, function (index, item) {

					/*dataHtml +=
					'<div class="sub_card">'+
					'<div class="box" id="displayGoodsData" data-goods-ids=\"' + item.id + '\" data-goods-itemids=\"' + item.itemid + '\" data-goods-count=\"' + (item.pack > 0 ? item.pack : 1) + '\" data-goods-price=\"' + item.price + '\">' +
					'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin" id="ncoina">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
					'</div>' +
					'</div>'+ 

					'<div class="sub_card">'+
					'<div class="box" id="displayGoodsData" data-goods-ids=\"' + item.id + '\" data-goods-itemids=\"' + item.itemid + '\" data-goods-count=\"' + (item.pack > 0 ? item.pack : 1) + '\" data-goods-price=\"' + item.price + '\">' +
						'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');" class="lnk_detail">' +
							'<div class="item_info">' +
								'<div class="v_align">' +
									'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
								'</div>' +
							'</div>' +
						'</a>' +
					'</div>'+						
				'</div>';	*/				


				dataHtml +=
					'<div class="sub_card">'+
						'<div class="box" id="displayGoodsData" data-goods-ids=\"' + item.id + '\" data-goods-itemids=\"' + item.itemid + '\" data-goods-count=\"' + (item.pack > 0 ? item.pack : 1) + '\" data-goods-price=\"' + item.price + '\">' +
							'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');" class="lnk_detail">' +
								item.flagTag_1 +								
								'<div class="img_area"><img src=\"/img/item/' + item.invgfx + '.png\" alt="" class="item_img"></div>' +
								'<div class="item_info">' +
									'<div class="v_align">' +
										'<div class="tit_area"><em class="tit">' + item.itemname + (item.pack > 0 ? '&nbsp' + item.pack + 'units' : '') + '</em></div>' +
										//'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
									'</div>' +
								'</div>' +
							'</a>' +
							'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin" id="ncoina">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
							//'<div class="btn_area"><a href="javascript:buyListCheck(\'' + item.id + '\', \'' + (item.pack > 0 ? item.pack : 1) + '\', \'' + item.price + '\');" class="btn direct fn_orderform">Buy now</a></div>' +
							'<div class="box sub_card_over">' +
							'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');" class="lnk_detail">' +							
								'<div class="sub_card_over_text_div">' +
									'<h5>' + item.itemname + '</h5>' +
								'</div>'+
								'<div class="sub_card_over_button_div">' +
									'<a href="javascript:buyListCheck(\'' + item.id + '\', \'' + (item.pack > 0 ? item.pack : 1) + '\', \'' + item.price + '\');" class="btn direct fn_orderform lnk_button_buy">'+
									'<button class="sub_card_over_button"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">'+
									'Buy now'+
									'</font></font></button>'+
									'</a>'+
									'</div>'+
							'</a>'+
							'</div>' +
							//style="position: absolute; top:0; left:0; margin:0; display:none; background: white; z-index: 999;"
						'</div>'+						
					'</div>'; /*NEW */
			goods_items.push(item);
                });
            } else {
            	dataHtml += '<div class="search_not_found"><em>No results found</em><p>Please check that you have spelled your search object correctly.</p></div>';
            }
            $(".sub_wrap").html(dataHtml);// 렌더링
        }
    });
});

/*
dataHtml +=
'<div class="sub_card">'+
	'<div class="box" id="displayGoodsData" data-goods-ids=\"' + item.id + '\" data-goods-itemids=\"' + item.itemid + '\" data-goods-count=\"' + (item.pack > 0 ? item.pack : 1) + '\" data-goods-price=\"' + item.price + '\">' +
		'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');" class="lnk_detail">' +
			item.flagTag_1 +								
			'<div class="img_area"><img src=\"/img/item/' + item.invgfx + '.png\" alt="" class="item_img"></div>' +
			'<div class="item_info">' +
				'<div class="v_align">' +
					'<div class="tit_area"><em class="tit">' + item.itemname + (item.pack > 0 ? '&nbsp' + item.pack + 'units' : '') + '</em></div>' +
					//'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
				'</div>' +
			'</div>' +
		'</a>' +
		'<div class="coin_area">' + (item.price_type === 'NCOIN' ? '<span class="ncoin">' : '<span class="npoint">') + commaAdd(item.price) + '</span></div>' +
		//'<div class="btn_area"><a href="javascript:buyListCheck(\'' + item.id + '\', \'' + (item.pack > 0 ? item.pack : 1) + '\', \'' + item.price + '\');" class="btn direct fn_orderform">Buy now</a></div>' +
		'<div class="box sub_card_over">' +
		'<a href="javascript:urlform(\'' + item.id + '\', \'post\', \'/goods/view\');" class="lnk_detail_over">' +							
			'<div class="sub_card_over_text_div">' +
				'<h5>' + item.itemname + '</h5>' +
			'</div>'+
			'<div class="sub_card_over_button_div">' +
				'<button class="sub_card_over_button"><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">'+
				'<a href="javascript:buyListCheck(\'' + item.id + '\', \'' + (item.pack > 0 ? item.pack : 1) + '\', \'' + item.price + '\');" class="btn direct fn_orderform">Buy now</a>'+
				'</font></font></button></div>'+
		'</a>'+
		'</div>' +
		//style="position: absolute; top:0; left:0; margin:0; display:none; background: white; z-index: 999;"
	'</div>'+						
'</div>';
*/