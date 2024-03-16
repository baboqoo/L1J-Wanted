$(function(){
    var IMP = window.IMP; // 생략가능
    IMP.init('imp97580643'); // 'iamport' 대신 부여받은 "가맹점 식별코드"를 사용
    let msg;
    IMP.request_pay({
        pg:				'kakaopay',
        pay_method:		'card',
        merchant_uid:	'merchant_' + new Date().getTime(),
        name:			'N Coin ' + cashCount + ' Point payment',
        amount:			cashPrice,
        buyer_email:	'123456@naver.com',
        buyer_name:		'John Doe',
        buyer_tel:		'010-1234-5678',
        buyer_addr:		'Seoul City',
        buyer_postcode:	'123-456',
        //m_redirect_url : 'http://www.naver.com'
    }, function(response) {
        if (response.success) {
            //[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
            jQuery.ajax({
	    		url:		'/define/cash/charge', //cross-domain error가 발생하지 않도록 동일한 도메인으로 전송
	    		type:		'POST',
	    		dataType:	'json',
	    		data: {
		    		imp_uid:		response.imp_uid,
		    		merchant_uid:	response.merchant_uid,
                    card_auth_id:	response.apply_num,
                    price:			response.paid_amount,
                    count:			cashCount
		    		//기타 필요한 데이터가 있으면 추가 전달
	    		}
	    	}).done(function(data) {
	    		//[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우
	    		if (data === 1) {
	    			/*msg = '결제가 완료되었습니다.';
	    			msg += '\n고유ID : ' + response.imp_uid;
	    			msg += '\n상점 거래ID : ' + response.merchant_uid;
	    			msg += '\n결제 금액 : ' + response.paid_amount;
	    			msg += '\n카드 승인번호 : ' + response.apply_num;*/
					msg = 'Payment completed.';
					msg += '\nUnique ID: ' + response.imp_uid;
					msg += '\nStore Transaction ID: ' + response.merchant_uid;
					msg += '\nPayment Amount: ' + response.paid_amount;
					msg += '\nCard Approval Number: ' + response.apply_num;					
	
	    			alert(msg);
	    			
	                //성공시 이동할 페이지
	                let f = document.authCompleteform;
	                f.price.value = cashPrice;
	                f.count.value = cashCount;
	                f.action = '/cash/complete';
	                f.method = 'post';
	                f.submit();
	    		} else {
	    			//[3] 아직 제대로 결제가 되지 않았습니다.
	    			//[4] 결제된 금액이 요청한 금액과 달라 결제를 자동취소처리하였습니다.
					var failMsg = '';
					/*if (data === 2) {
						failMsg = '계정을 찾을 수 없습니다.';
					} else if (data === 3) {
						failMsg = '대표 캐릭터를 찾을 수 없습니다.';
					} else if (data === 4) {
						failMsg = '월드 내 캐릭터가 존재하지 않습니다.';
					} else if (data === 0) {
						failMsg = '인게임에서 구매하실 수 있습니다..';
					}*/
					if (data === 2) {
						failMsg = 'Account not found.';
					} else if (data === 3) {
						failMsg = 'Main character not found.';
					} else if (data === 4) {
						failMsg = 'Character does not exist in the world.';
					} else if (data === 0) {
						failMsg = 'You can purchase in-game.';
					}					
	                alert(failMsg);
	                //실패시 이동할 페이지
	                location.href="/index";
	    		}
	    	}).fail(function(){
	    		console.log("faile");
	    	}).always(function(){
	    		console.log("always");
	    	});
        } else {
            //msg = '결제에 실패하였습니다.';
            //msg += '에러내용 : ' + response.error_msg;
			msg = 'Payment failed.';
			msg += ' Error Message: ' + response.error_msg;			
            alert(msg);
            //실패시 이동할 페이지
            location.href="/index";
        }
    });
});