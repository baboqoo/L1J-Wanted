$(function () {
   	var windowWidth = $(window).width();
   	var parmaeter = {
   			query: '',
   			sortType: 0
   	};
   	var paginationRender = function(param){
   		let container = $('.pagination');
   		container.pagination({
           	dataSource: function(done) {
           	    $.ajax({
           	        type: 'POST',
           	        url: '/define/pledge',
           	        data: param,
           	        success: function(response) {
           	        	if (response !== null && response.length > 0) {
           	        		var topHtml;
               	        	if (account !== undefined && account.firstChar !== undefined && account.firstChar.clan !== undefined) {
				//var ingame_btn = account.ingame ? '<div class="wrap-button"><a class="button selected btn-myblood" href="javascript:L1.OpenPledgeUI()">내 혈맹</a></div>' : '';// 인게임 팅김
				var ingame_btn = '';
               	        		topHtml = 
                   	        		'<h2><strong>My Pledge</strong></h2>' +
                   					'<div class="detail-info">' +
               							'<div class="blood-title">' +
               								'<span class="blood-icon"><i class="icon-pledge"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 43 41"><path fill="#C69C7C" d="M36.797 6.243c-.523-.561-1.446-.29-1.446-.29-4.355.54-7.672-.004-10.167-.984-3.122-1.227-4.954-3.14-5.92-4.47a1.191 1.191 0 0 0-1.898-.053c-1.9 2.351-6.699 6.61-15.991 5.504A1.213 1.213 0 0 0 .017 7.118C-.133 11.43.318 28.484 17.994 40c3.39-1.786 6.531-4.488 8.794-6.741 5.662-5.638 8.146-12.278 9.203-17.62 0 0 1.552-8.595.806-9.396" mask="url(#b)"></path><path fill="#FFF" d="M28.878 27.143l.002-.003.01-.014c.078-.122.168-.345.061-.434-.115-.096-.264.023-.4.13l-.114.086c-1.077.746-2.079 1.066-3.189 1.107-1.373.04-2.244-.346-2.39-1.06-.046-.223.036-.716.097-.975a8.704 8.704 0 0 0 .123-3.375c-.388-2.268-1.545-4.091-2.475-5.555l-.211-.333c-.311-.501-.861-1.515-.889-1.617l-.002-.007-.004-.012c-.313-.664-.266-.984-.172-1.134a.344.344 0 0 1 .252-.159s.174-.02.32.039c.047-.249-.087-.39-.087-.39-.23-.237-.574-.408-.704-.472l-.028-.013c-.809-.413-2.736-.933-3.833-.951-.11 0-.2-.013-.233.057-.03.064.006.2.012.226.344 1.587.71 2.718 3.776 5.8.076.073.462.47.726.753.48.53 1.946 2.148 2.243 3.253.479 1.754-.824 4.385-2.326 6.214-.365.444-.524.773-.487 1.008.024.15.12.225.19.27.267.183 2.46.601 5.692-.257.049-.01.306-.074.336-.082 2.367-.683 3.336-1.61 3.704-2.1"></path></svg></i></span>' +
               								'<span class="blood-name"><strong class="name">' + account.firstChar.clan.pledgeName + '</strong>' + ingame_btn + '</span>' +
               							'</div>' +
               							'<div class="blood-info">' +
               								/*'<ul>' +
               									'<li><span>혈맹군주</span><strong class="leader">&nbsp;&nbsp;&nbsp;&nbsp;' + account.firstChar.clan.leaderName + '</strong></li>' +
               									'<li><span>혈맹원수 (최근/전체)</span><strong><strong class="count">' + account.firstChar.clan.totalMember + '</strong>/50명</strong></li>' +
               								'</ul>' +
               								'<ul>' +
               									'<li><span>혈맹공헌도</span><strong><strong class="exp">' + commaAdd(account.firstChar.clan.exp) + '</strong>P</strong></li>' +
               									'<li><span>아지트</span><strong class="has">' + (account.firstChar.clan.hasCastle != null && account.firstChar.clan.hasCastle != '' ? account.firstChar.clan.hasCastle : account.firstChar.clan.hasHouse) + '</strong></li>' +
               								'</ul>' +*/
										   '<ul>' +
											   '<li><span>Pledge Monarch</span><strong class="leader">&nbsp;&nbsp;&nbsp;&nbsp;' + account.firstChar.clan.leaderName + '</strong></li>' +
											   '<li><span>Pledge Members (Recent/Total)</span><strong><strong class="count">' + account.firstChar.clan.totalMember + '</strong>/50 members</strong></li>' +
										   '</ul>' +
										   '<ul>' +
											   '<li><span>Pledge Contribution</span><strong><strong class="exp">' + commaAdd(account.firstChar.clan.exp) + '</strong>P</strong></li>' +
											   '<li><span>Hideout</span><strong class="has">' + (account.firstChar.clan.hasCastle != null && account.firstChar.clan.hasCastle != '' ? account.firstChar.clan.hasCastle : account.firstChar.clan.hasHouse) + '</strong></li>' +
										   '</ul>' +
										   
               							'</div>' +
               						'</div>';
               	        		$('.wrap-detail-info').html(topHtml);
               	        	} else {
               	        		$('.wrap-detail-info').remove();
               	        	}
           	        		//$('.wrap-detail-info').remove();
           	        	}
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
				if (data.length > 0) {
					var is_ingame = account !== undefined && account.ingame;
					$.each(data, function (index, item) {
						dataHtml +=
                   			'<div class="tr">' +
		        				'<div class="td-row"><span class="td-blood">' + item.pledgeName + '</span><span class="td-master">' + item.leaderName + '</span><span class="td-member">' + item.totalMember + '/50 members</span></div>' +
		        				'<div class="td-row">' +
		        					'<span class="td-point"><strong>' + commaAdd(item.exp) + '</strong>P</span>' +
		        					'<span class="td-azit">' + (item.hasCastle != null && item.hasCastle != '' ? item.hasCastle : item.hasHouse) + '</span>' +
		        					'<span class="td-join">' + (is_ingame ? item.joinBtn : '') + '</span>' +
		        				'</div>' +
		        			'</div>';
					});
				} else {
					dataHtml += '<div class="nodata"><p>Pledge information does not exist!</p></div>';
				}
				$(".table-list").html(dataHtml);// 렌더링
			}
        });
    }
   	
   	var sortor = $('.section-blood-list.section-blood-info > .tab > li');
   	sortor.on('click', function(event) {
   		var sortValue = $(this).children('a').attr('data-sort-type');
   		if (parmaeter.sortType !== sortValue) {
   			parmaeter.sortType = sortValue;
   			sortor.removeClass('active');
   			$(this).addClass('active');
   			paginationRender(parmaeter);
   		}
	});
   	
   	// 검색 폼
   	var searchClanForm = suggest_search;
   	var searchInput = $('#suggestInput');
   	var searchResetBtn = $('.icon-delete');
   	var searchBtn = $('#suggestSubmit');
	searchInput.keyup(function(event){
		if (searchInput.val().length > 0) {
			searchResetBtn.css('display', '');
		} else {
			searchResetBtn.css('display', 'none');
		}
		if (event.keyCode === 13) {
			if (searchInput.val().length > 0) {
				parmaeter.query = searchInput.val();
				paginationRender(parmaeter);
			} else {
				parmaeter.query = '';
				paginationRender(parmaeter);
			}
	    }
	});
	searchResetBtn.on('click', function(event) {
		searchInput.val('');
		searchResetBtn.css('display', 'none');
		searchInput.focus();
	});
	searchBtn.on('click', function(event) {
		if (searchInput.val().length > 0) {
			parmaeter.query = searchInput.val();
			paginationRender(parmaeter);
		} else {
			parmaeter.query = '';
			paginationRender(parmaeter);
		}
	});
   	
   	$(document).ready(function(){
		paginationRender(parmaeter)
   	});
});