$(function() {
	var couponInput			= $('.coupon_input'),
		couponInputReset	= $('.coupon_div .btn_reset');
	couponInput.focus(function(){
		$(this).addClass('on');
	})
	
	couponInputReset.on('click', function(e){
		couponInput.val('');
		couponInput.focus();
	})
	
	var dropdown		= $('.ui-dropdown'),
		dropdownSelect	= $('.ui-dropdown-custom_items');
	dropdown.on('click', function(event) {
		if ($(this).hasClass('is-active')) {
			dropdown.removeClass('is-active');
		} else {
			dropdown.removeClass('is-active');
			$(this).addClass('is-active');
		}
	});
	
	dropdownSelect.on('click', function(event) {
		let selectOption = $(this).attr('data-textvalue');
		if (selectOption === $('.ui-dropdown.is-active .select').find('span').html()) {
			return;
		}
		$('.ui-dropdown.is-active .select').find('span').html(selectOption);
		$('.ui-dropdown.is-active .select').find('#data_val').val($(this).attr('data-value'));
		
		if ($(this).hasClass('coupon-select-items')) {
			parameter.status = $(this).attr('data-value');
			$('.pagination-container').destory;
			paginationRender(parameter);
		}
	});
	
	// 영역밖 클릭
	$('body').mouseup(function (e){
		if (couponInput.has(e.target).length === 0 && $('.coupon_div').has(e.target).length === 0 && couponInput.hasClass('on')) {
			couponInput.removeClass('on');
		}
		if (dropdown.has(e.target).length === 0 && dropdownSelect.has(e.target).length === 0 && dropdown.hasClass('is-active')) {
			dropdown.removeClass('is-active');
		}
	});
});

var couponLock = false;// 인게임과의 통신시 딜레이 방지
function couponFormCheck(){
	if (couponLock) {
		return false;
	}
	if (!couponForm.coupon_input.value) {
		//popupShow('쿠폰번호를 입력하세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		popupShow('Enter the coupon code.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		return false;
	}
	if (couponForm.coupon_input.value.length != 10) {
		//popupShow('쿠폰번호가 잘못되었습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		popupShow('Invalid coupon code.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		return false;
	}
	if (account == undefined) {
		//popupShow('계정을 찾을 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		popupShow('Account not found.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		return false;
	}
	couponLock = true;// 잠금
	const couponNum	= couponForm.coupon_input.value.toUpperCase();
	const senddata	= {"number" : couponNum};
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/coupon/use",
		data:		senddata,
		success:function(data){
			switch(data){
			/*case 1:
				popupShow('정상적으로 완료되었습니다.', '<span class="type2"><a href="javascript:popupCloseReload();" class="close">Close</a></span>', null);
				break;
			case 2:
				popupShow('이미 사용이 완료된 쿠폰입니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 3:
				popupShow('인게임에서 등록하실 수 있습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 4:
				popupShow('계정을 찾을 수 없습니다.', '<span class="type2"><a href="javascript:loginIngame();" class="close">설정</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 5:
				popupShow('대표 캐릭터를 찾을 수 없습니다.', '<span class="type2"><a href="javascript:loginIngame();" class="close">설정</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 6:
				popupShow('월드 내 캐릭터를 찾을 수 없습니다.', '<span class="type2"><a href="javascript:loginIngame();" class="close">설정</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 7:
				popupShow('일치하는 쿠폰번호가 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			default:
				popupShow('등록에 실패하였습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;*/
			case 1:
				popupShow('Successfully completed.', '<span class="type2"><a href="javascript:popupCloseReload();" class="close">Close</a></span>', null);
				break;
			case 2:
				popupShow('Coupon has already been used.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 3:
				popupShow('You can register in-game.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 4:
				popupShow('Account not found.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Settings</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 5:
				popupShow('Main character not found.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Settings</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 6:
				popupShow('Character not found in the Aden world.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Settings</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 7:
				popupShow('No matching coupon number found.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			default:
				popupShow('Registration failed.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;				
			}
			couponLock = false;// 해재
		}, error: function(request, status, error){
			couponLock = false;// 해재
		}
	});
	return false;
}