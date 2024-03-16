function popupShowSimple(str){
	const layer_alert	= $('#layer_alert');
	$('.con').html(str);
	$('.wrapper .btn_modal').html('<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>');
	$(layer_alert).css('display', 'block');
	$('.dimmed').fadeIn();
}

function popupShow(html, btn_1, btn_2){
	const layer_alert	= $('#layer_alert');
	$('.con').html(html);
	let modal			= $('.btn_modal'),
		btn_html		= '';
	if (btn_1 !== null && btn_1 !== undefined) {
		btn_html += btn_1;
	}
	if (btn_2 !== null && btn_2 !== undefined) {
		btn_html += btn_2;
	}
	$(modal).html(btn_html);
	$(layer_alert).css('display', 'block');
	$('.dimmed').fadeIn();
}

function popupClose(){
	let layer_alert = $('#layer_alert');
	layer_alert.css('display', 'none');
	$('.dimmed').fadeOut();
}

function popupCloseReload(){
	location.reload();
}