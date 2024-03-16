$(function(){
	var modal_close = function() {
		$('body').removeClass('modal-open');
		$('#NC-banner-movie-backdrop').remove();
		$('#NC-banner-movie').remove();
	}

	// 모달 닫기 버튼 클릭 이벤트
	$('.nc-modal__btn.nc-modal__btn--sec').on('click', function(e){
		modal_close();
	});

	// 모달 그만보기 버튼 클릭 이벤트
	$('.nc-modal__btn.nc-modal__btn--pri').on('click', function(e){
		const modal_data = index_data.find(v => v.key === 'MODAL').value;
		const modal_cookie_name = modal_data.COOKIE_NAME;
		const modal_cookie_value = modal_data.COOKIE_VALUE;
		const modal_cookie_max_age = modal_data.COOKIE_MAX_AGE;
		const modal_cookie_same_site = modal_data.COOKIE_SAME_SITE;
		SetCookie(modal_cookie_name, modal_cookie_value, modal_cookie_max_age, modal_cookie_same_site);
		modal_close();
	});
});