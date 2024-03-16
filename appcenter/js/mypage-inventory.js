$(function() {
	if (account === undefined || account.firstChar === undefined) {
		//$('.wrap-section-inventory').html('캐릭터 정보를 찾을 수 없습니다.');
		$('.wrap-section-inventory').html('Unable to find character information.');
		return;
	}
		var character		= account.firstChar;
		var invenItem		= character.inventory,
			normalWareItem	= account.normalWarehouse,
			packageWareItem	= account.packageWarehouse;
		
		$('#inv-size').html(invenItem.length);
		$('#invs-size').html(invenItem.length);
		$('#normalware-size').html(normalWareItem.length);
		$('#packageware-size').html(packageWareItem.length);
		
		var windowWidth = $(window).width();
		var invParam = { 
			invType: 'char',
			sortType: 'ALL'
		};
		var paginationRender = function(data){
	   		let container = $('.pagination-container');
	   		container.pagination({
	   			dataSource: data,
	           	pageSize: 10,// 한 화면에 보여질 개수
	           	showPrevious: false,// 처음버튼 삭제
	           	showNext: false,// 마지막버튼 삭제
	           	showPageNumbers: true,// 페이지 번호표시
	           	callback: function (data, pagination) {// 화면 출력
	           		var dataHtml = '<ul>';
	           		if (data.length > 0) {
	           			$.each(data, function (index, item) {
	                   		dataHtml += '<li><img src=\"/img/item/' + item.item.invgfx + '.png\" alt="" onerror="this.src=\'/img/shop/noimg.gif\'" class="thumb"><strong class="name' + getBlessStatus(item.bless) + '">' + getItemName(item.item.name, item.enchant, item.attr) + ' (' + commaAdd(item.count) + ')</strong></li>';
	                   	});
	           		} else {
	           			dataHtml += '<div class="no-data"><p>There are no items.</p></div>';
	           		}
	           		dataHtml += '</ul>';
	           		$(".item-list").html(dataHtml);// 렌더링
	           	}
	        });
	    }
		
		var attrDesc = [
			"",
			/*"화령:1단의 ",	"화령:2단의 ",	"화령:3단의 ",	"화령:4단의 ",	"화령:5단의 ",
			"수령:1단의 ",	"수령:2단의 ",	"수령:3단의 ",	"수령:4단의 ",	"수령:5단의 ",
			"풍령:1단의 ",	"풍령:2단의 ",	"풍령:3단의 ",	"풍령:4단의 ",	"풍령:5단의 ",
			"지령:1단의 ",	"지령:2단의 ",	"지령:3단의 ",	"지령:4단의 ",	"지령:5단의 ",*/
			"Fire: 1st level", "Fire: 2nd level", "Fire: 3rd level", "Fire: 4th level", "Fire: 5th level",
			"Water: 1st level", "Water: 2nd level", "Water: 3rd level", "Water: 4th level", "Water: 5th level",
			"Wind: 1st level", "Wind: 2nd level", "Wind: 3rd level", "Wind: 4th level", "Wind: 5th level",
			"Earth: 1st level", "Earth: 2nd level", "Earth: 3rd level", "Earth: 4th level", "Earth: 5th level",			
		];
		
		var getItemName = function(name, enchant, attr){
			if (enchant < 0)
				return attrDesc[attr] + enchant + ' ' + name;
			if (enchant > 0)
				return attrDesc[attr] + '+' + enchant + ' ' + name;
			return attrDesc[attr] + name;
		}
		
		
		var getBlessStatus = function(bless){
			switch(bless){
			case 0:
				return ' status-bless';
			case 2:
				return ' status-curse';
			default:
				return '';
			}
		}
		
		$(window).resize(function (){// 브라우저 사이즈의 변화
	   		var width_size = window.outerWidth;// 사이즈 변화에 대한 넓이
	   		windowWidth = width_size;// 전역 변수 넓이
	   		if (width_size <= 960) {
	   			if (selector.find('.select').hasClass('open')) {
	   				selector.find('.select').removeClass('open');
	   				selector.find('.select-tab.option').css('display', 'none');
	   			}
	   		} else {
	   			if (!selector.find('.select').hasClass('open')) {
	   				selector.find('.select').addClass('open');
	   				selector.find('.select-tab.option').css('display', 'block');
	   			}
	   		}
	   	})
		
	   	var selector = $('.ui-select-tab.select-tab-inventory');
		selector.find('.select').on('click', function(){
	   		if (!$(this).hasClass('open')) {
				selectorOpen();
	   		} else {
				selectorClose();
			}
	   	});
		
		var selectorOpen = function(){
	   		selector.find('.select').addClass('open');
			selector.find('.select-tab.option').css('display', 'block');
	   	};
	   	var selectorClose = function(){
	   		selector.find('.select').removeClass('open');
			selector.find('.select-tab.option').css('display', 'none');
	   	};
		
	   	// 인벤토리 선택
		var invTaps = selector.find('li');
		invTaps.on('click', function(event){
			var invType = $(this).attr('data-value');
			if (invType !== invParam.invType) {
				invParam.invType = invType;
				switch(invType){
				case 'normal':
					paginationRender(normalWareItem);
					break;
				case 'package':
					paginationRender(packageWareItem);
					break;
				default:
					paginationRender(invenItem);
					break;
				}
				invTaps.children('a').removeClass('selected');
				$(this).children('a').addClass('selected');
				selector.find('.select > span').html($(this).children('a').html());
				if (windowWidth <= 960) {
					selectorClose();
				}
				sortor.find('.select > span').html('All');
				invParam.sortType = 'ALL';
				sortorClose();
			}
			eventStop(event);
		});
		
		
		var sortorOpen = function(){
			sortor.find('.select').addClass('open');
			sortor.find('.option').css('display', 'block');
	   	};
	   	var sortorClose = function(){
	   		sortor.find('.select').removeClass('open');
	   		sortor.find('.option').css('display', 'none');
	   	};
		
		var sortor = $('.ui-dropdown.ui-dropdown-sort1');
		sortor.find('.select').on('click', function(){
	   		if (!$(this).hasClass('open')) {
				sortorOpen();
	   		} else {
				sortorClose();
			}
	   	});
		
		// 정렬 선택
		var sortorTaps	= sortor.find('li');
		var sortData	= [];
		sortorTaps.on('click', function(event){
			var sortType = $(this).attr('data-value');
			if (sortType !== invParam.sortType) {
				invParam.sortType = sortType;
				if (sortType !== 'ALL') {
					sortData.length = 0;// 배열 초기화
					$.each(invParam.invType === 'normal' ? normalWareItem : invParam.invType === 'package' ? packageWareItem : invenItem, function (index, item) {
						switch(sortType){
						case 'EQUIP':
							if (item.item.itemType === 1 || item.item.itemType === 2) {
								sortData.push(item);
							}
							break;
						case 'ETC':
							if (item.item.itemType === 0) {
								sortData.push(item);
							}
							break;
						}
					});
					paginationRender(sortData);
				} else {
					paginationRender(invParam.invType === 'normal' ? normalWareItem : invParam.invType === 'package' ? packageWareItem : invenItem);
				}
				sortorTaps.removeClass('selected');
				$(this).addClass('selected');
				sortor.find('.select > span').html($(this).children('a').html());
			}
			sortorClose();
			eventStop(event);
		});
		
		$(document).ready(function(){
			if (windowWidth <= 960) {
				selectorClose();
	   		} else {
				selectorOpen();
			}
	   		paginationRender(invenItem);
	   	});
});