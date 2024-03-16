/*파일첨부 버튼 */
function fileLink(max){
	$(".preview img").each(function( index ) {
		if ($( this ).attr('src') == "") {
			$('#F' + index).click();
			return false;
		} else {
			if (index == max-1) {
				//var msg = '첨부가능한 이미지수는 최대' + max +'개 입니다.';
				var msg = 'The maximum number of attachable images is ' + max + '.';
				popupShow(msg, '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);// 팝업창
			}
		}
	});
}

/*썸네일 미리보기*/
function loadImg(img) {
	var isIE	= (navigator.appName == "Microsoft Internet Explorer");
	var path	= img.value;
	var ext		= path.substring(path.lastIndexOf('.') + 1).toLowerCase();
	if (ext == "gif" || ext == "jpeg" || ext == "jpg" || ext == "png") {
		if (isIE) {
			$( ".preview img" ).each(function( index ) {
				if($( this ).attr('src')==''){
					$('#' +index+ 'th').attr('src', path);
					$('.' +index+'th').css('display', 'inline');
					return false;
				}
			});
		} else {
			if (img.files[0]) {
				var reader = new FileReader();
				reader.onload = function (e) {
					$( ".preview img" ).each(function( index ) {
						if($( this ).attr('src')==""){
							$('#' +index+'th').attr('src', e.target.result);
							$('.' +index+'th').css('display', 'inline');
							return false;
						}
					});
				}
				reader.readAsDataURL(img.files[0]);
			}
		}
	}
}

/*썸네일 삭제*/
function deleteFile(index){
	$('img#'+index+'th').attr('src','');
	$('.' +index+'th').css('display', 'none');
	$('#oriFile').val('');
	/*파일 초기화*/
	if (/(MSIE|Trident)/.test(navigator.userAgent)) {
		// ie 일때 input[type=file] init.
		$("#F"+index).replaceWith( $("#F"+index).clone(true) );
	} else {
		// other browser 일때 input[type=file] init.
		$("#F"+index).val("");
	}
}