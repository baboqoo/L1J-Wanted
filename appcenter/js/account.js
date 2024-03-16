$(function() {
	$('.keypad-btn').on('click', function(event) {
		$(this).toggleClass('on');
		if ($('.contents').hasClass('moveDown')) {
			$('.contents').removeClass('moveDown');
			$('.contents').addClass('moveUp');
			$('.keypad').removeClass('moveShow');
			$('.keypad').addClass('moveHide');
		} else {
			$('.contents').removeClass('moveUp');
			$('.contents').addClass('moveDown');
			$('.keypad').removeClass('moveHide');
			$('.keypad').addClass('moveShow');
		}
	});
	
	$('.btn-delete').on('click', function(event) {
		var del = $(this).closest('div').find('input');
		del.val('');
		$(this).css('display', 'none');
	});
	
	$('.my-character-list .list .wrap-items').slick({
        dots:			true,
        arrows:			false,
        infinite:		false,
        slidesToShow:	6,
        slidesToScroll:	6,
        responsive: [
          {
            breakpoint: 960,
            settings: {
              slidesToShow: 3,
              slidesToScroll: 3
            }
          },
          {
            breakpoint: 480,
            settings: {
              slidesToShow: 2,
              slidesToScroll: 2
            }
          }
        ]
	});
	
});

$(window).resize(function (){
	var width_size = window.outerWidth;
	if (width_size > 972) {
		$('.keypad-btn').removeClass('on');
		$('.contents').removeClass('moveDown');
		$('.keypad').removeClass('moveShow');
	}
})

function loginWriteCheck(o){
	if ($(o).focus()) {
		var write = $(o).val();
		if (!write) {
			$(o).closest('div').find('.btn-delete').css('display', 'none');
		} else {
			$(o).closest('div').find('.btn-delete').css('display', 'inline');
			$(o).closest('div').find('.msg').html('');
		}
	}
	
	$(o).focus(function(){
		var write = $(o).val();
		$(o).closest('div').find('.btn-delete').css('display', !write ? 'none' : 'inline');
	});
}

function loginCheck(urlType){
	if (!loginForm.login_name.value) {
		$('.input-email').find('.msg').html('Please enter your account ID.');
		loginForm.login_name.focus();
		return false;
	}
	if (!loginForm.password.value) {
		$('.input-pwd').find('.msg').html('Please enter a password.');
		loginForm.password.focus();
		return false;
	}
	var senddata = {"loginname":loginForm.login_name.value, "password":loginForm.password.value};
	$.ajax ({
		type:"post",
		datatype:"json",
		url:"/define/account/loginReCheck",
		data:senddata,
		success:function(data){
            		if (!data) {
				$('.input-email').find('.msg').html('The account does not exist or the password is incorrect');
			} else {
				location.href = !urlType ? '/index' : urlType;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
	return false;
}