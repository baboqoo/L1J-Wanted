$(function() {
	var dropdown		= $('.ui-dropdown');
	var dropdownSelect	= $('.ui-dropdown-custom_items');
	var topscrollBtn	= $('.co-btn-top_list');
	
	dropdown.on('click', function(event) {
		dropdown.toggleClass('is-actives');
		eventStop(event);
		return;
	});
	
	dropdownSelect.on('click', function(event) {
		const selectOption = $(this).attr('data-textvalue');
		if (selectOption === $('.ui-dropdown.is-active .select').find('span').html()) {
			return;
		}
		$('.ui-dropdown.is-active .select').find('span').html(selectOption);
		$('.ui-dropdown.is-active .select').find('#data_val').val($(this).attr('data-value'));
		
		parameter.status = $(this).attr('data-value');
		$('.pagination-container').destory;
		paginationRender(parameter);
	});
	
	topscrollBtn.on('click', function(e) {
		$('html, body').stop().animate({
			scrollTop: ($('body').offset().top)
		}, 600);
		e.preventDefault();
	});
	
	// 영역밖 클릭
	$('body').mouseup(function (e){
		if ($('.option').has(e.target).length === 0 && $('.trade-select').has(e.target).length === 0 && $('.ui-dropdown').hasClass('is-active')) {
			$('.ui-dropdown').removeClass('is-active');
		}
		eventStop(e);
	});
});

function tradeWriteFormCheck(){
	/*if (!tradeWriteForm.title.value) {
		popupShow('제목을 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.content.value) {
		popupShow('판매 물품을 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.bank.value) {
		popupShow('은행명을 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.bankNumber.value) {
		popupShow('계좌번호를 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.sellerName.value) {
		popupShow('계좌주를 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.sellerPhone.value) {
		popupShow('연락처를 작성해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}*/
	if (!tradeWriteForm.title.value) {
		popupShow('Please enter the title.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.content.value) {
		popupShow('Please enter the item for sale.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.bank.value) {
		popupShow('Please enter the bank name.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.bankNumber.value) {
		popupShow('Please enter the account number.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.sellerName.value) {
		popupShow('Please enter the account holder.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}
	if (!tradeWriteForm.sellerPhone.value) {
		popupShow('Please enter the contact number.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return false;
	}	
	return true;
}

function delecteTradeConfirm(rownum){
	//popupShow('정말로 삭제하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteTradeConfirmAction(' + rownum + ');" class="close">Yes</a></span>');
	popupShow('Are you sure you want to delete it?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="javascript:deleteTradeConfirmAction(' + rownum + ');" class="close">Yes</a></span>');
}

function deleteTradeConfirmAction(rownum){
	const senddata = {"rownum":rownum};
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/trade/status",
		data:		senddata,
		success:function(data){
			if (data === 1) {
				urlform(rownum, 'post', '/trade/delete');
			} else if (data === 2) {
				popupClose();
				//popupShow('거래가 진행중인 물품은 삭제할 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Items currently in progress cannot be deleted.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			} else {
				popupClose();
				//popupShow('물품 정보를 찾을 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Unable to find item information.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function modifyTradeAction(rownum){
	const senddata = {"rownum":rownum};
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/trade/status",
		data:		senddata,
		success:function(data){
			if (data === 1) {
				urlform(rownum, 'post', '/trade/modify');
			} else if (data === 2) {
				popupClose();
				//popupShow('거래가 진행중인 물품은 수정할 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Items currently in progress cannot be modified.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			} else {
				popupClose();
				//popupShow('물품 정보를 찾을 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Unable to find item information.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function buyPopOpen(){
	$('.trade-buy-pop').css('display', 'block');
	$('.trade-buy-pop-back').css('display', 'block');
}

function buyPopClose(){
	$('.trade-buy-pop').css('display', 'none');
	$('.trade-buy-pop-back').css('display', 'none');
}

function buyRegist(){
	let registForm = document.buyRegistForm;
	if (!registForm.buyerName.value) {
		//popupShow('입금명을 입력해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Please enter the deposit name.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (!registForm.buyerPhone.value || registForm.buyerPhone.value.length < 12 || registForm.buyerPhone.value.length > 13) {
		//popupShow('연락처를 입력해주세요.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		popupShow('Please enter the contact number.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	
	const senddata = {"rownum":registForm.rownum.value};
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/trade/status",
		data:		senddata,
		success:function(data){
			if (data === 1) {
				registForm.submit();
			} else if (data === 2) {
				//popupShow('이미 구매신청이 완료된 물품입니다..', '<span class="type2"><a href="javascript:popupCloseReload();" class="close">Close</a></span>', null);
				popupShow('This item has already been purchased.', '<span class="type2"><a href="javascript:popupCloseReload();" class="close">Close</a></span>', null);
			} else {
				//popupShow('물품 정보를 찾을 수 없습니다.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				popupShow('Unable to find item information.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

function tradingConfirm(type, rownum){
	if (type === 0) {
		//popupShow('정말로 인계하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="/trade/send?rownum=' + rownum + '" class="close">Yes</a></span>');
		popupShow('Are you sure you want to transfer ownership?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="/trade/send?rownum=' + rownum + '" class="close">Yes</a></span>');
	} else {
		//popupShow('정말로 인수하시겠습니까?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="/trade/receive?rownum=' + rownum + '" class="close">Yes</a></span>');
		popupShow('Are you sure you want to take over?', '<span class="type2"><a href="javascript:popupClose();" class="close">No</a></span>', '<span class="type1"><a href="/trade/receive?rownum=' + rownum + '" class="close">Yes</a></span>');
	}
}