$(function() {
	$('#summernote').summernote();
});

$('#summernote').summernote({
	height:			500,	// set editor height
	minHeight:		null,	// set minimum height of editor
	maxHeight:		null,	// set maximum height of editor
	focus:			false,	// set focus to editable area after initializing summernote
	lang:			'en-US',// default: 'en-US'
	placeholder:	'Detail',	// placeholder name
	tabDisable:		true,
	toolbar: [
		['style', 
			['style']
		], 
		['fontstyle', 
			['bold','underline','color']
		], 
		['paragraph', 
			['paragraph','height','ul','ol']
		], 
		['insert', 
			['link', 'table','picture','video']
		]
	],
	callbacks: {
		onImageUpload: function(files, editor, welEditable){
			uploadFile(files, this);
		},
		onMediaDelete: function(target){
			deleteFile(target[0].src);
		}
	}
});

function uploadFile(files, editor){
	const limitSize = 1 * 1024 * 1024;// 1MB
	const dividPath = $('#dividPath').val();
	for (var i=0; i<files.length; i++) {
		if (files[i].size >= limitSize) {
			//alert('이미지는 1MB이하로 등록하십시오.');
			alert('Please upload an image under 1MB.');
			return;
		} else {
			(function(i) {
				var data = new FormData();
				data.append('uploadFile', files[i]);
				data.append('dividPath', dividPath);
				$.ajax({
					data:			data,
					type:			'POST',
					url:			'/define/editor/upload',
					contentType:	false,
		 	        processData:	false,
		 	        cache:			false,
					success: function(res) {
						if (!res) {
							//alert('이미지는 1MB이하로 등록하십시오.');
							alert('Please upload an image under 1MB.');
						} else {
							res = res.replaceAll('\\', '/');
							$(editor).summernote('editor.insertImage', res);
							let mainImg = $('#mainImg').val();
							if (mainImg !== undefined && !mainImg.length) {
								$('#mainImg').val(res);
							}
							let oriList = $('#oriList').val();
							if (oriList !== undefined) {
								$('#oriList').val(!oriList ? res : oriList + "," + res);
							}
						}
					}
				});
			})(i);
		}
	}
}

function deleteFile(src){
	src = decodeString(src);
	$.ajax({
        data:	{'src' : src},
        type:	'POST',
        url:	'/define/editor/delete',
        cache:	false,
        success: function(res) {
        	if (res) {
        		let fileSrc	= src.substring(src.indexOf(location.host) + location.host.length);
        		let mainImg = $('#mainImg').val();
        		if (mainImg !== undefined && $('#mainImg').val() === fileSrc) {
			$('#mainImg').val('');
		}
        		var tempList = $('#tempList').val();
        		$('#tempList').val(!tempList ? fileSrc : tempList + "," + fileSrc);
        		let oriList = $('#oriList').val();
        		if (oriList !== undefined) {
			$('#oriList').val(oriList.replace(fileSrc, ''));
		}
        	}
        }
    });
}

function decodeString(str){
	return decodeURIComponent(decodeURI(str));
}