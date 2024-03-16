$(function () {
   	var windowWidth = $(window).width();
   	var parmaeter = { charType: 'all' };
   	var paginationRender = function(param){
   		let container = $('.pagination');
   		container.pagination({
   			dataSource: function(done) {
           	    $.ajax({
           	        type: 'POST',
           	        url: '/define/rank',
           	        data: param,
           	        success: function(response) {
           	            done(response);
           	        }
           	    });
           	},
           	pageSize: 20,// 한 화면에 보여질 개수
           	showPrevious: false,// 처음버튼 삭제
           	showNext: false,// 마지막버튼 삭제
           	showPageNumbers: true,// 페이지 번호표시
           	callback: function (data, pagination) {// 화면 출력
           		var dataHtml = '';
           		$('.nodata').remove();
           		if (data.length > 0) {
           			$.each(data, function (index, item) {
				var rankFlag = item.curRank - item.oldRank;
                   		dataHtml +=
                   			'<tr>' +
                   				'<td class="rank"><span class="num">' + item.curRank + '</span><span class=\"ui-rank ' + (rankFlag > 0 ? 'up' : rankFlag < 0 ? 'down' : '') +'\">' + (rankFlag == 0 ? '-' : rankFlag) + '</span></td>' +
                   				'<td>' + item.name + '</td>' +
                   				'<td>' + GAME_CLASS_NAME[item.classId] + '</td>' +
                   				'<td class="grade"><span class="star ' + getGrade(param.charType === 'all' ? item.curRank : item.subCurRank) + '"></span></td>' +
                   			'</tr>';
                   		});
           			$('.table-list').css('display', 'block');
           		} else {
           			$('.table-list').css('display', 'none');
           			$('.section-ranking-list').append('<div class="nodata"><p>Ranking information does not exist!</p></div>');
           		}
		$(".table-ranking-area").html(dataHtml);// 렌더링
           	}
        });
    }

	const GAME_CLASS_NAME = [
		'Prince', 'Knight', 'Elf', 'Wizard', 'Dark Elf', 'Dragon Knight', 'Illusionist', 'Warrior', 'Fencer', 'Lancer'
	];
   	
   	var getGrade = function(rank){
		if (rank == 1)
			return 'top1';
		if (rank == 2)
			return 'top2';
		if (rank == 3)
			return 'top3';
   		if (rank >= 4 && rank <= 10)
			return 'grade4';
   		if (rank >= 11 && rank <= 20)
			return 'grade3';
   		if (rank >= 21 && rank <= 60)
			return 'grade2';
   		if (rank >= 61 && rank <= 80)
			return 'grade1';
   		return 'grade0';
   	}
   	
   	var selector = $('.ui-select-tab.select-tab-class');
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
   	
   	var select_tab = $('.select-tab.option > li > a');
   	select_tab.on('click', function(e){
   		var charType = $(this).attr('data-type');
   		if (parmaeter.charType === charType) {
   			if (windowWidth <= 960 && selector.find('.select').hasClass('open')) {
   	   			selectorClose();
			}
   			eventStop(e);
   			return;
   		}
   		select_tab.removeClass('selected');
   		$(this).addClass('selected');
   		parmaeter.charType = charType;
   		$('#rankingtop').html(charType === 'all' ? rank_total_range : rank_class_range);
   		selector.find('.select').html($(this).html());
   		$('.pagination').destory;
   		paginationRender(parmaeter);
   		
   		if (windowWidth <= 960 && selector.find('.select').hasClass('open')) {
   			selectorClose();
		}
   		eventStop(e);
   	});
   	
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
   	
   	// 영역밖 클릭
   	$('body').mouseup(function (e){
   		if (windowWidth <= 960 && selector.has(e.target).length === 0 && select_tab.has(e.target).length === 0) {
   			selectorClose();
		}
   		eventStop(e);
   	});

	$('.section-ranking-mycharacter .detail-info').slick({
        		dots:		true,
        		arrows:		false,
        		infinite:		true,
        		slidesToShow:	1,
        		slidesToScroll:	1
	});
   	
   	$(document).ready(function(){
   		if (windowWidth <= 960) {
			selectorClose();
   		} else {
			selectorOpen();
		}
		paginationRender(parmaeter);
   	});
});