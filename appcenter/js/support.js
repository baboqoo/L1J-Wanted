var sending_msg = false;// 전송 딜레이 체크를 위한 변수

function agree_progres(){
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return;
	}
	if ($('#agree_check').is(':checked')) {
		$.ajax ({
			type:		"POST",
			datatype:	"json",
			url:		"/define/support/agree",
			success:function(data){
				if (data) {
					$('.account-binding').after('<span class="agree_txt">&nbsp;[<i class="xi-shield-checked-o"></i>&nbsp;Agree to terms and conditions]</span>');
					$('.support_container').html(data);
					popupShow('You have agreed to the terms and conditions.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				}
			}, error: function(request, status, error){
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	} else {
		popupShow('You can proceed after agreeing to the terms and conditions.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
	}
}

function support_bank_request(){
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return;
	}
	if (!account.firstChar) {
		popupShow('The default character cannot be found.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (!account.ingame) {
		popupShow('Only available in-game.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (sending_msg) {
		return;
	}
	sending_msg = true;
	$('.dimmed').css('display', 'block');
	$('#send_delay').css('display', 'block');
	$.ajax ({
		type:		"POST",
		datatype:	"json",
		url:		"/define/support/request",
		success:function(data){
			$('#send_delay').css('display', 'none');
			switch (data) {
			case 1:
				popupShow('The request has been made successfully.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 2:
				popupShow('Only available in-game.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 3:
				popupShow('We couldn\'t find your account information.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 4:
				popupShow('The default character cannot be found.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 5:
				popupShow('The selected character cannot be found in the game.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			default:
				popupShow('Request failed.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			}
			sending_msg = false;
		}, error: function(request, status, error){
			$('.dimmed').css('display', 'none');
			$('#send_delay').css('display', 'none');
			sending_msg = false;
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function sendGmMessage(){
	const sendMsg = $('#support_complete_msg').val();
	if (sendMsg.length <= 0) {
		popupShow('Enter the deposit amount.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return;
	}
	if (!account.firstChar) {
		popupShow('The default character cannot be found.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (!account.ingame) {
		popupShow('Only available in-game.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (sending_msg) {
		return;
	}
	sending_msg = true;
	$('.dimmed').css('display', 'block');
	$('#send_delay').css('display', 'block');
	$.ajax ({
		type:		"POST",
		datatype:	"json",
		url:		"/define/support/msg",
		data:		{"msg":sendMsg},
		success:function(data){
			$('#send_delay').css('display', 'none');
			switch (data) {
			case 1:
				$('#support_complete_msg').val('');
				popupShow('Your request has been registered successfully.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 2:
				popupShow('Only available in-game.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 3:
				popupShow('Account information not found.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 4:
				popupShow('The default character cannot be found.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 5:
				popupShow('The selected character cannot be found in the game.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Close</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 6:
				popupShow('Data has been successfully saved.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			default:
				popupShow('The request has failed.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			}
			sending_msg = false;
		}, error: function(request, status, error){
			$('.dimmed').css('display', 'none');
			$('#send_delay').css('display', 'none');
			sending_msg = false;
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

$(function () {
	$('#agree_checkbox').on('click', function(e){
		const agreeCheck = $('#agree_check');
		agreeCheck.prop('checked', agreeCheck.is(':checked') ? false : true);
	});
});