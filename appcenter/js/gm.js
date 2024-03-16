const update_interval_millis = 5000;// 5초 주기
let update_interval;

var user_container = [];
var support_container = [];
var support_request_container = [];
let support_tab_type = 'DEFAULT';

/*
 * 화면 렌더링 최초 호출 함수
 */
function init() {
	switch (tabType) {
	case "SERVER":
		includeHTML(document.querySelector('#page_container'), '/gm/gm_server.html');
		break;
	case 'USER':
		$('#page_container').html('<div id="user_data"><div class="user_top"><div class="user_search"><input type="text" id="user_search_name" name="user_search_name" placeholder="Search Character Name" onkeyup="if(window.event.keyCode==13){user_search();}"><button onClick="user_search();">Search</button></div></div><ul class="table-list"></ul><div class="pagination"></div></div>');
		break;
	case 'SUPPORT':
		let support_init = '';
		if (support_tab_type === 'DEFAULT') {
			support_init = '<div id="support_data">' + 
				'<div class="ui-select-tab select-tab-inventory"><ul class="select-tab option" style="display:block;"><li><a class="selected" href="javascript:support_tab_click(\'DEFAULT\');">Sponsorship details</a></li><li><a class="" href="javascript:support_tab_click(\'REQUEST\');">Account Request</a></li></ul></div>' +
				'<div class="support_tab_top"><div class="support-select"><div class="ui-dropdown" onClick="$(this).toggleClass(\'is-active\')"><div class="select"><span>All</span><input type="hidden" name="support_status" id="data_val" value="ALL"/></div><ul class="option">' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="All" data-value="ALL" onClick="dropdown_select(this);">All</li>' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="Deposit complete" data-value="STANBY" onClick="dropdown_select(this);">Deposit complete</li>' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="Settlement complete" data-value="FINISH" onClick="dropdown_select(this);">Settlement complete</li>' +
       				'</ul></div></div></div><ul class="table-list"></ul><div class="pagination"></div></div>';
		} else {
			support_init = '<div id="support_data">' + 
				'<div class="ui-select-tab select-tab-inventory"><ul class="select-tab option" style="display:block;"><li><a class="" href="javascript:support_tab_click(\'DEFAULT\');">Sponsorship details</a></li><li><a class="selected" href="javascript:support_tab_click(\'REQUEST\');">Account Request</a></li></ul></div>' +
				'<div class="support_tab_top"><div class="support-select"><div class="ui-dropdown" onClick="$(this).toggleClass(\'is-active\')"><div class="select"><span>All</span><input type="hidden" name="support_status" id="data_val" value="ALL"/></div><ul class="option">' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="All" data-value="ALL" onClick="dropdown_request_select(this);">All</li>' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="Not Sended" data-value="STANBY" onClick="dropdown_request_select(this);">Not Sended</li>' +
       				'<li class="ui-dropdown-custom_items support-select-items" data-textvalue="Send" data-value="FINISH" onClick="dropdown_request_select(this);">Sended</li>' +
       				'</ul></div></div></div><ul class="table-list"></ul><div class="pagination"></div>' + 
				'<div class="finish_form_div"><div class="finish_form"><span>Fill out the sponsorship account information </span><a class="close" href="javascript:$(\'.finish_form_div\').css(\'display\', \'none\');$(\'.dimmed\').css(\'display\', \'none\');$(\'.support_request_content\').val(\'\');">Close</a>' +
				'<div><div class="detail-item"><input type="hidden" id="support_request_id" name="support_request_id"><textarea class="support_request_content" name="support_request_content" placeholder="Enter the information you want to send to the applicant about sponsored accounts"></textarea></div><div class="requst_submit_btn"><button onClick="javascript:support_request_finish_submit();">Send</button></div></div>' +
				'</div></div>' +
				'</div>';
		}
		$('#page_container').html(support_init);
		break;
	case 'REPORT':
		$('#page_container').html('<div id="report_data"><ul class="table-list"></ul><div class="pagination"></div></div>');
		break;
	case 'CHAT':
		$('#page_container').html('<div id="chat_data"><div class="chat_top"><div><h4>Admin Chat (The result of the command is not output.)</h4></div><div id="chat_result"><div id="chat_result_box"><ul></ul></div></div><div class="chat_search"><input type="text" id="chat_param" name="chat_param" placeholder="Enter message." onkeyup="if(window.event.keyCode==13){chat_run();}"><button onClick="chat_run();">Search</button></div></div></div>');
		break;
	}
}

/*
 * 서버 정보 화면 렌더링
 */
function server_render(data) {
	// cpu
	var cpu_percent = data.cpu_usage;
	var cpu_tooltip = '';
	if (cpu_percent > 50) {
		cpu_tooltip = 'The usage is too high. A system upgrade or patch is needed.';
	} else if (cpu_percent > 30) {
		cpu_tooltip = 'Usage is a bit high but within normal parameters.';
	} else {
		cpu_tooltip = 'The usage is correct.';
	}
	$('.cpu .graph-text-view .var').html('Used CPU: ' + cpu_percent + '%');	
	$('.cpu .percent-wrap .bar').css('width', cpu_percent + '%');
	$('.cpu .percent-wrap .info-tooltip span').html(cpu_tooltip);

	// memory
	var memory_percent = calculatePercent(data.used_memory, data.max_memory);
	var memory_tooltip = '';
	if (memory_percent > 80) {
		memory_tooltip = 'The usage is too high. A system upgrade or patch is needed.';
	} else if (memory_percent > 70) {
		memory_tooltip = 'Usage is a bit too high. Some attention is needed.';
	} else if (memory_percent > 60) {
		memory_tooltip = 'Usage is high.';
	} else if (memory_percent > 50) {
		memory_tooltip = 'Usage is a bit high but within normal parameters.';
	} else {
		memory_tooltip = 'The usage is correct.';
	}
	$('.memory .graph-text-view .var').html('Used Memory: ' + memory_percent + '% (' + parseInt(data.used_memory / 1024) + 'mb / ' +  data.used_memory + 'kb)');	
	$('.memory .percent-wrap .bar').css('width', memory_percent + '%');
	$('.memory .percent-wrap .info-tooltip span').html(memory_tooltip);

	// total_thread
	var total_thread_percent = calculatePercent(data.thread_count, 1000);
	var total_thread_tooltip = '';
	if (data.thread_count > 900) {
		total_thread_tooltip = 'The usage is too high. A system upgrade or patch is needed.';
	} else if (data.thread_count > 800) {
		total_thread_tooltip = 'Usage is a bit too high. Some attention is needed.';
	} else if (data.thread_count > 700) {
		total_thread_tooltip = 'Usage is a bit high but within normal parameters.';
	} else {
		total_thread_tooltip = 'The usage is correct.';
	}
	$('.thread-total .graph-text-view .var').html('Total Threads:' + total_thread_percent + '% (' + data.thread_count + ')');	
	$('.thread-total .percent-wrap .bar').css('width', total_thread_percent + '%');
	$('.thread-total .percent-wrap .info-tooltip span').html(total_thread_tooltip);

	// thread-executor
	var excutor_percent = calculatePercent(data.executor.active, data.executor.poolSize);
	var excutor_tooltip = '';
	if (excutor_percent > 70) {
		excutor_tooltip = 'The excutor thread pool activity is too high. A pool size recheck is required.';
	} else if (excutor_percent < 10) {
		excutor_tooltip = 'The excutor thread pool activity is too low. A pool size recheck is required.';
	} else {
		excutor_tooltip = 'The executor thread pool is correct.';
	}
	$('.thread-executor .graph-text-view .var').html('General Thread Pool:' + excutor_percent + '% (Active: ' + data.executor.active + ', Pool Size: ' + data.executor.poolSize + ', Waiting: ' + data.executor.queue + ', Completed: ' + data.executor.complete + ', Total: ' + data.executor.task + ')');	
	$('.thread-executor .percent-wrap .bar').css('width', excutor_percent + '%');
	$('.thread-executor .percent-wrap .info-tooltip span').html(excutor_tooltip);
	
	// thread-scheduler
	var scheduler_percent = calculatePercent(data.scheduler.active, data.scheduler.poolSize);
	var scheduler_tooltip = '';
	if (scheduler_percent > 70) {
		scheduler_tooltip = 'Scheduler thread pool activity too high. A pool size recheck is required.';
	} else if (scheduler_percent < 10) {
		scheduler_tooltip = 'The scheduler thread pool activity is too low. A pool size recheck is required.';
	} else {
		scheduler_tooltip = 'The scheduler thread pool is correct.';
	}
	$('.thread-scheduler .graph-text-view .var').html('Schedule Thread Pool:' + scheduler_percent + '% (Active: ' + data.scheduler.active + ', Pool Size: ' + data.scheduler.poolSize + ', Waiting: ' + data.scheduler.queue + ', Completed: ' + data.scheduler.complete + ', Total: ' + data.scheduler.task + ')');	
	$('.thread-scheduler .percent-wrap .bar').css('width', scheduler_percent + '%');
	$('.thread-scheduler .percent-wrap .info-tooltip span').html(scheduler_tooltip);

	// thread-pc-scheduler
	var pc_scheduler_percent = calculatePercent(data.pcScheduler.active, data.pcScheduler.poolSize);
	var pc_scheduler_tooltip = '';
	if (pc_scheduler_percent > 70) {
		pc_scheduler_tooltip = 'pc_scheduler thread pool activity too high. A pool size recheck is required.';
	} else if (pc_scheduler_percent < 10) {
		pc_scheduler_tooltip = 'The pc_scheduler thread pool activity is too low. A pool size recheck is required.';
	} else {
		pc_scheduler_tooltip = 'The pc_scheduler thread pool is correct.';
	}
	$('.thread-pc-scheduler .graph-text-view .var').html('PC-Schedule Thread Pool:' + pc_scheduler_percent + '% (Active: ' + data.pcScheduler.active + ', Pool Size: ' + data.pcScheduler.poolSize + ', Waiting: ' + data.pcScheduler.queue + ', Completed: ' + data.pcScheduler.complete + ', Total: ' + data.pcScheduler.task + ')');	
	$('.thread-pc-scheduler .percent-wrap .bar').css('width', pc_scheduler_percent + '%');
	$('.thread-pc-scheduler .percent-wrap .info-tooltip span').html(pc_scheduler_tooltip);

	// clients
	var clients_percent = calculatePercent(data.clients, data.clientsLimit);
	var clients_tooltip = '';
	if (clients_percent > 80) {
		clients_tooltip = 'Too many connections. Possible attack or few configured connections.';
	} else {
		clients_tooltip = 'Everything works correctly';
	}
	$('.clients .graph-text-view .var').html('Clients: ' + clients_percent + '% (Nº of active Clients: ' + data.clients + ', maximum nº of clients supported: ' + data.clientsLimit + ')');	
	$('.clients .percent-wrap .bar').css('width', clients_percent + '%');
	$('.clients .percent-wrap .info-tooltip span').html(clients_tooltip);

	// npcs
	var npc_percent = calculatePercent(data.npcs, 10000);
	var npc_tooltip = '';
	if (npc_percent > 80) {
		npc_tooltip = 'There are too many NPCs. Please reduce NPC or check if NPC is leaking.';
	} else if (npc_percent > 70) {
		npc_tooltip = 'The number of NPCs is high. Management is required.';
	} else if (npc_percent > 50) {
		npc_tooltip = 'It can be further optimized by adjusting the number of NPCs.';
	} else {
		npc_tooltip = 'The number of NPC is correct.';
	}
	$('.npcs .graph-text-view .var').html('NPC Spawn Rate: ' + npc_percent + '% (Nº of active NPC: ' + data.npcs + ')');	
	$('.npcs .percent-wrap .bar').css('width', npc_percent + '%');
	$('.npcs .percent-wrap .info-tooltip span').html(npc_tooltip);

	// items
	var item_percent = calculatePercent(data.items, 10000);
	var item_tooltip = '';
	if (item_percent > 80) {
		item_tooltip = 'There are too many active items. Reduce the number of items or check for item leaks.';
	} else if (item_percent > 70) {
		item_tooltip = 'The number of active items is high. Management is required.';
	} else if (item_percent > 50) {
		item_tooltip = 'This can be further optimized by adjusting the number of active items.';
	} else {
		item_tooltip = 'The number of active items is correct.';
	}
	$('.items .graph-text-view .var').html('Item Active Rate: ' + item_percent + '% (Nº of active Items: ' + data.items + ')');	
	$('.items .percent-wrap .bar').css('width', item_percent + '%');
	$('.items .percent-wrap .info-tooltip span').html(item_tooltip);
}

function calculatePercent(current, limit) {
	return current <= 0 ? 0 : Math.max(Math.min(parseInt((current / limit) * 100), 100), 0);
}

var attrDesc = [
	"",
	/*"화령:1단의 ",	"화령:2단의 ",	"화령:3단의 ",	"화령:4단의 ",	"화령:5단의 ",
	"수령:1단의 ",	"수령:2단의 ",	"수령:3단의 ",	"수령:4단의 ",	"수령:5단의 ",
	"풍령:1단의 ",	"풍령:2단의 ",	"풍령:3단의 ",	"풍령:4단의 ",	"풍령:5단의 ",
	"지령:1단의 ",	"지령:2단의 ",	"지령:3단의 ",	"지령:4단의 ",	"지령:5단의 ",*/
	"Fire: 1st level", "Fire: 2nd level", "Fire: 3rd level", "Fire: 4th level", "Fire: 5th level",
	"Water: 1st level", "Water: 2nd level", "Water: 3rd level", "Water: 4th level", "Water: 5th level",
	"Wind: 1st level", "Wind: 2nd level", "Wind: 3rd level", "Wind: 4th level", "Wind: 5th level",
	"Earth: 1st level", "Earth: 2nd level", "Earth: 3rd level", "Earth: 4th level", "Earth: 5th level",	
];
		
var getItemName = function(name, enchant, attr){
	if(enchant < 0)		return attrDesc[attr] + enchant + ' ' + name;
	else if(enchant > 0)return attrDesc[attr] + '+' + enchant + ' ' + name;
	else				return attrDesc[attr] + name;
}

var getBlessStatus = function(bless){
	switch(bless){
	case 0:return ' status-bless';
	case 2:return ' status-curse';
	default:return '';
	}
}

/*
 * 유저 인벤토리 아이템 삭제 이벤트
 */
function delete_user_item(name, id) {
	if (!confirm('Are you sure you want to delete the selected item?')) {
		return;
	}
	var user;
	for (var i=0; i < user_container.length; i++) {
		if (user_container[i].name === name) {
			user = user_container[i];
			break;
		}
	}
	if (user === undefined) {
		alert('Failed to identify user data!');
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/deleteitem',
		data:{user_name: name, item_id: id},
		success:function(data){
			if (data) {
				for (var i=0; i < user.inventory.length; i++) {
					if (user.inventory[i].id === id) {
						user.inventory.splice(i, 1); 
						break;
					}
				}
				const target_inv_cnt = $('#item_' + id).closest('.inv_info').find('.inv_count');
				target_inv_cnt.html(user.inventory.length);
				$('#item_' + id).remove();
				alert('It has been successfully deleted.');
			} else {
				alert('Delete failed!');
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 유저 추방 이벤트
 */
function ban_user(name, id) {
	if (!confirm('The account will be banned.\r\nAre you sure you want to ban the selected user?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/banuser',
		data:{user_name: name},
		success:function(data){
			switch (data) {
			case 1:
				alert('The ban has been executed correctly.');
				break;
			case 2:
				alert('The character is not online.');
				break;
			case 5:
				alert('The selected character is already currently banned.');
				break;
			default:
				alert('The ban has failed!');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 유저 삭제 이벤트
 */
function del_user(account_name, name, id) {
	if (!confirm('Are you sure you want to delete the selected user?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/deleteuser',
		data:{acc_name: account_name, user_name: name},
		success:function(data){
			switch (data) {
			case 1:
				for (var i = 0; i < user_container.length; i++) {
					if (user_container[i].objId === id) { 
    						user_container.splice(i, 1); 
    						break;
  					}
				}
				$('#user_' + id).remove();
				alert('The user has been successfully deleted.');
				break;
			default:
				alert('Delete failed!');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 유저 검색 이벤트
 */
function user_search() {
	const search_name = $('#user_search_name').val();
	if (!search_name || search_name === '') {
		$('.pagination').destory;
		user_render(user_container);
		return;
	}
	$('.pagination').destory;
	let search_data = [];
	for (var i = 0; i < user_container.length; i++) {
		if (user_container[i].name.includes(search_name)) {
			search_data.push(user_container[i]);
		}
	}

	user_render(search_data);
	$('#user_search_name').val('');
}

function get_user_detail_inv(user) {
	if (user.inventory === undefined || user.inventory === '') {
		return '<div class="nodata">The selected character does not have any items.</div>';
	}
	var result = user.inventory.map((item, i) => {
		return '<li id="item_' + item.id + '"><img src=\"/img/item/' + item.item.invgfx + '.png\" alt="" onerror="this.src=\'/img/shop/noimg.gif\'" class="thumb"><strong class="name' + getBlessStatus(item.bless) + '">' + getItemName(item.item.name, item.enchant, item.attr) + ' (' + commaAdd(item.count) + ')</strong><a class="del" href="javascript:delete_user_item(\'' + user.name + '\', ' + item.id + ')">Delete</a></li>';
	}).join('');
	return result;
}

function get_user_detail(user) {
	return '<div class="user_detail">' + 
		'<button class="close" onClick="$(this).parent(\'.user_detail\').css(\'display\', \'none\'); $(\'.dimmed\').css(\'display\', \'none\');">X</button>' +
		'<div class="detail_img"><img src="' + user.profileUrl +'"></div>' + 
		'<div class="detail_box">' +
			'<div class="detail_title"><strong>' + user.name + '</strong> | <strong> Level ' + user.level + '</strong> | <strong>' + user.expPercent + '%</strong><span>' + (user.gm ? 'Manager' : '') + '</span></div>' +
			'<div class="detail_pledge"><strong>Pledge:</strong><span>' + (user.clanName !== undefined && user.clanName !== '' ? user.clanName : '-') + '</span></div>' +
			'<div class="detail_account"><strong>Account:</strong><span>' + user.accountName + '</span></div>' +
			'<div class="detail_inv"><strong>Inventory:</strong><span class="inv_info"><i class="inv_count">' + user.inventory.length + '</i> items' +
			'<div class="detail_inv_list">' +
				'<div class="inv_box"><ul>' +
				get_user_detail_inv(user) +
				'</ul></div>' +
			'</div>' +
			'</span></div>' +
			'<div class="detail_stat"><strong>Stats</strong><div class="stat_box"><ul>' + 
				'<li><strong class="stat_key">STR:</strong><span class="stat_val">'+ user.str + '</span></li>' +
				'<li><strong class="stat_key">CON:</strong><span class="stat_val">'+ user.con + '</span></li>' +
				'<li><strong class="stat_key">DEX:</strong><span class="stat_val">'+ user.dex + '</span></li>' +
				'<li><strong class="stat_key">INT:</strong><span class="stat_val">'+ user.intel + '</span></li>' +
				'<li><strong class="stat_key">WIS:</strong><span class="stat_val">'+ user.wis + '</span></li>' +
				'<li><strong class="stat_key">CHA:</strong><span class="stat_val">'+ user.cha + '</span></li>' +
			'</ul></div></div>' +
			'<div class="detail_event">' +
				'<button class="ban_btn" onClick="javascript:ban_user(\'' + user.name + '\', ' + user.objId + ');">Ban</button>' +
				'<button class="del_btn" onClick="javascript:del_user(\'' + user.accountName + '\', \'' + user.name + '\', ' + user.objId + ');">Delete</button>' +
			'</div>' +
		'</div>' +
	'</div>';
}

/*
 * 유저 정보 화면 렌더링
 */
function user_render(user_data) {
	let container = $('.pagination');
	container.pagination({
           		dataSource: user_data,
           		pageSize: 20,// 한 화면에 보여질 개수
           		showPrevious: false,// 처음버튼 삭제
		showNext: false,// 마지막버튼 삭제
		showPageNumbers: true,// 페이지 번호표시
		callback: function (data, pagination) {// 화면 출력
			console.log(data);
			var dataHtml = '';
			if (data !== undefined && data.length > 0) {
				$.each(data, function (index, user) {
					dataHtml +=
                   				'<li class="users" id="user_' + user.objId + '">' +
						'<div class="simple" onClick="$(this).parent(\'.users\').children(\'.user_detail\').css(\'display\', \'block\'); $(\'.dimmed\').css(\'display\', \'block\');">' +
						'<span class="profileUrl"><img src="' + user.profileUrl +'"></span>' +
						'<span class="name">' + user.name + '</span>' +
						'<span class="level">Level ' + user.level + ' | ' + user.expPercent + '%</span>' +
						'<span class="pledge"><strong>' + (user.clanName !== undefined && user.clanName !== '' ? user.clanName : '-') + '</strong></span>' +
						'<span class="online">' + (user.onlineStatus ? 'Connected' : 'Not connected') + '</span>' +
						'</div>' +
						get_user_detail(user) +
		        			'</li>';
				});
			} else {
				dataHtml = '<div class="nodate">There are no users.</div>';
			}
			$(".table-list").html(dataHtml);// 렌더링
		}
        	});
}

const support_status = {
	"STANBY" : "Deposit complete",
	"FINISH" : "Settlement complete"
};

/*
 * 후원 내역 정렬 이벤트
 */
function dropdown_select(obj) {
	let selectOption = $(obj).attr('data-textvalue');
	if (selectOption === $('.ui-dropdown.is-active .select').find('span').html()) {
		return;
	}
	$('.ui-dropdown.is-active .select').find('span').html(selectOption);
	$('.ui-dropdown.is-active .select').find('#data_val').val($(obj).attr('data-value'));
		
	const status = $(obj).attr('data-value');
	$('.pagination').destory;
	let sort_data = [];
	for (var i =0; i < support_container.length; i++) {
		if (status === 'ALL' || support_container[i].status === status) {
			sort_data.push(support_container[i]);
		}
	}
	support_render(sort_data);
}

/*
 * 후원 계좌 요청 정렬 이벤트
 */
function dropdown_request_select(obj) {
	let selectOption = $(obj).attr('data-textvalue');
	if (selectOption === $('.ui-dropdown.is-active .select').find('span').html()) {
		return;
	}
	$('.ui-dropdown.is-active .select').find('span').html(selectOption);
	$('.ui-dropdown.is-active .select').find('#data_val').val($(obj).attr('data-value'));
		
	const status = $(obj).attr('data-value');
	$('.pagination').destory;
	let sort_data = [];
	for (var i =0; i < support_request_container.length; i++) {
		if (status === 'ALL' || (status === 'STANBY' && !support_request_container[i].response) || (status === 'FINISH' && support_request_container[i].response)) {
			sort_data.push(support_request_container[i]);
		}
	}
	support_render(sort_data);
}

/*
 * 후원 보상 정산
 */
function support_finish(id) {
	var support_req;
	for(var i = 0; i < support_container.length; i++){ 
  		if (support_container[i].id === id) { 
			support_req = support_container[i];
    			break;
  		}
	}
	if (!support_req) {
		alert('The data has not been entered correctly.');
		return;
	}
	if (support_req.status === 'FINISH') {
		alert('The information has already been established previously.');
		return;
	}
	if (!confirm('Are you sure you want to continue?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/supportfinish',
		data:{support_id: id},
		success:function(data){
			switch (data) {
			case 1:
				support_container[i].status = 'FINISH';
				$('#support_' + id + ' .item.status .val').text('Settlement');
				$('#support_' + id + ' .item.status .val').addClass('on');
				$('#support_' + id + ' .event .finish').remove();
				alert('It was settled normally.');
				break;
			case 2:
				alert('Could not find data to settle.');
				break;
			case 3:
				alert('This is data that has already been settled.');
				break;
			case 4:
				alert('The amount to be settled is less than the minimum amount.');
				break;
			case 5:
				alert('Customer does not exist in-game.');
				break;
			case 6:
				alert('Failed to register customer\'s supplementary service warehouse.');
				break;
			case 7:
				alert('Settlement was completed, but sponsorship data could not be updated.');
				break;
			default:
				alert('Settlement failed.');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 후원 계좌번호 입력 폼 생성
 */
function support_request_finish(id) {
	var support_req;
	for(var i = 0; i < support_request_container.length; i++){ 
  		if (support_request_container[i].id === id) { 
			support_req = support_request_container[i];
    			break;
  		}
	}
	if (!support_req) {
		alert('Information not entered.');
		return;
	}
	if (support_req.response !== undefined) {
		alert('This request has already been sent.');
		return;
	}
	$('#support_request_id').val(id);
	$('.dimmed').css('display', 'block');
	$('.finish_form_div').css('display', 'block');
}

/*
 * 후원 계좌번호 전송
 */
function support_request_finish_submit() {
	const support_request_id = $('#support_request_id').val();
	if (!support_request_id || support_request_id === 0) {
		alert('Request number could not be found.');
		return;
	}
	var support_req;
	for (var i=0; i<support_request_container.length; i++) {
		if (support_request_container[i].id === Number(support_request_id)) {
			support_req = support_request_container[i];
			break;
		}
	}
	if (!support_req) {
		alert('Information not entered');
		return;
	}

	const support_request_content = $('.support_request_content').val();
	if (!support_request_content || support_request_content.length <= 0) {
		alert('Please enter the account information.');
		return;
	}
	if (!confirm('Would you like to send the account information to that customer?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/supportBankResponse',
		data:{request_id: support_request_id, request_content: support_request_content},
		success:function(data){
			$('.finish_form_div').css('display', 'none');
			$('.dimmed').css('display', 'none');
			$('.support_request_content').val('');
			switch (data) {
			case 1:
				support_req.response = support_request_content;
				$('#support_' + support_request_id + ' .item.status .val').text('Transaction complete');
				$('#support_' + support_request_id + ' .item.status .val').addClass('on');
				$('#support_' + support_request_id + ' .event .finish').remove();
				alert('Sent normally.');
				break;
			case 2:
				alert('Could not find data to send.');
				break;
			case 3:
				alert('Data that has already been sent.');
				break;
			case 4:
				alert('Customer does not exist in-game.');
				break;
			case 5:
				alert('Failed to update data.');
				break;
			case 6:
				alert('Failed to register mail data to be sent.');
				break;
			default:
				alert('Transaction failed.');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 후원 관련 데이터 삭제
 */
function support_delete(id) {
	if (!confirm('Are you sure you want to delete the selected item?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/supportdelete',
		data:{support_id: id, type: support_tab_type},
		success:function(data){
			switch (data) {
			case 1:
				if (support_tab_type === 'DEFAULT') {
					for(var i = 0; i < support_container.length; i++){ 
  						if (support_container[i].id === id) { 
    							support_container.splice(i, 1); 
    							break;
  						}
					}
				} else {
					for(var i = 0; i < support_request_container.length; i++){ 
  						if (support_request_container[i].id === id) { 
    							support_request_container.splice(i, 1); 
    							break;
  						}
					}
				}
				
				$('#support_' + id).remove();
				alert('Deleted successfully.');
				break;
			case 2:
				alert('Could not find data to delete.');
				break;
			default:
				alert('Deletion failed.');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

/*
 * 후원 정보 관련 탭 클릭 이벤트
 */
function support_tab_click(type) {
	if (type === support_tab_type) {
		return;
	}
	support_tab_type = type;
	init();
	support_render(support_tab_type === 'DEFAULT' ? support_container : support_request_container);
}

function getLocaleDateString() {
	const formats = {
	  "af-ZA": "YYYY/MM/DD",
	  "aM-ET": "D/M/YYYY",
	  "ar-AE": "DD/MM/YYYY",
	  "ar-BH": "DD/MM/YYYY",
	  "ar-DZ": "DD-MM-YYYY",
	  "ar-EG": "DD/MM/YYYY",
	  "ar-IQ": "DD/MM/YYYY",
	  "ar-JO": "DD/MM/YYYY",
	  "ar-KW": "DD/MM/YYYY",
	  "ar-LB": "DD/MM/YYYY",
	  "ar-LY": "DD/MM/YYYY",
	  "ar-MA": "DD-MM-YYYY",
	  "ar-OM": "DD/MM/YYYY",
	  "ar-QA": "DD/MM/YYYY",
	  "ar-SA": "DD/MM/YY",
	  "ar-SY": "DD/MM/YYYY",
	  "ar-TN": "DD-MM-YYYY",
	  "ar-YE": "DD/MM/YYYY",
	  "arn-CL": "DD-MM-YYYY",
	  "as-IN": "DD-MM-YYYY",
	  "az-CYrl-AZ": "DD.MM.YYYY",
	  "az-Latn-AZ": "DD.MM.YYYY",
	  "ba-RU": "DD.MM.YY",
	  "be-BY": "DD.MM.YYYY",
	  "bg-BG": "DD.M.YYYY",
	  "bn-BD": "DD-MM-YY",
	  "bn-IN": "DD-MM-YY",
	  "bo-CN": "YYYY/M/D",
	  "br-FR": "DD/MM/YYYY",
	  "bs-CYrl-BA": "D.M.YYYY",
	  "bs-Latn-BA": "D.M.YYYY",
	  "ca-ES": "DD/MM/YYYY",
	  "co-FR": "DD/MM/YYYY",
	  "cs-CZ": "D.M.YYYY",
	  "cY-GB": "DD/MM/YYYY",
	  "Da-DK": "DD-MM-YYYY",
	  "De-AT": "DD.MM.YYYY",
	  "De-CH": "DD.MM.YYYY",
	  "De-DE": "DD.MM.YYYY",
	  "De-LI": "DD.MM.YYYY",
	  "De-LU": "DD.MM.YYYY",
	  "Dsb-DE": "D. M. YYYY",
	  "Dv-MV": "DD/MM/YY",
	  "el-GR": "D/M/YYYY",
	  "en-029": "MM/DD/YYYY",
	  "en-AU": "D/MM/YYYY",
	  "en-BZ": "DD/MM/YYYY",
	  "en-CA": "DD/MM/YYYY",
	  "en-GB": "DD/MM/YYYY",
	  "en-IE": "DD/MM/YYYY",
	  "en-IN": "DD-MM-YYYY",
	  "en-JM": "DD/MM/YYYY",
	  "en-MY": "D/M/YYYY",
	  "en-NZ": "D/MM/YYYY",
	  "en-PH": "M/D/YYYY",
	  "en-SG": "D/M/YYYY",
	  "en-TT": "DD/MM/YYYY",
	  "en-US": "M/D/YYYY",
	  "en-ZA": "YYYY/MM/DD",
	  "en-ZW": "M/D/YYYY",
	  "es-AR": "DD/MM/YYYY",
	  "es-BO": "DD/MM/YYYY",
	  "es-CL": "DD-MM-YYYY",
	  "es-CO": "DD/MM/YYYY",
	  "es-CR": "DD/MM/YYYY",
	  "es-DO": "DD/MM/YYYY",
	  "es-EC": "DD/MM/YYYY",
	  "es-ES": "DD/MM/YYYY",
	  "es-GT": "DD/MM/YYYY",
	  "es-HN": "DD/MM/YYYY",
	  "es-MX": "DD/MM/YYYY",
	  "es-NI": "DD/MM/YYYY",
	  "es-PA": "MM/DD/YYYY",
	  "es-PE": "DD/MM/YYYY",
	  "es-PR": "DD/MM/YYYY",
	  "es-PY": "DD/MM/YYYY",
	  "es-SV": "DD/MM/YYYY",
	  "es-US": "M/D/YYYY",
	  "es-UY": "DD/MM/YYYY",
	  "es-VE": "DD/MM/YYYY",
	  "et-EE": "D.MM.YYYY",
	  "eu-ES": "YYYY/MM/DD",
	  "fa-IR": "MM/DD/YYYY",
	  "fi-FI": "D.M.YYYY",
	  "fil-PH": "M/D/YYYY",
	  "fo-FO": "DD-MM-YYYY",
	  "fr-BE": "D/MM/YYYY",
	  "fr-CA": "YYYY-MM-DD",
	  "fr-CH": "DD.MM.YYYY",
	  "fr-FR": "DD/MM/YYYY",
	  "fr-LU": "DD/MM/YYYY",
	  "fr-MC": "DD/MM/YYYY",
	  "fY-NL": "D-M-YYYY",
	  "ga-IE": "DD/MM/YYYY",
	  "gD-GB": "DD/MM/YYYY",
	  "gl-ES": "DD/MM/YY",
	  "gsw-FR": "DD/MM/YYYY",
	  "gu-IN": "DD-MM-YY",
	  "ha-Latn-NG": "D/M/YYYY",
	  "he-IL": "DD/MM/YYYY",
	  "hi-IN": "DD-MM-YYYY",
	  "hr-BA": "D.M.YYYY.",
	  "hr-HR": "D.M.YYYY",
	  "hsb-DE": "D. M. YYYY",
	  "hu-HU": "YYYY. MM. DD.",
	  "hY-AM": "DD.MM.YYYY",
	  "iD-ID": "DD/MM/YYYY",
	  "ig-NG": "D/M/YYYY",
	  "ii-CN": "YYYY/M/D",
	  "is-IS": "D.M.YYYY",
	  "it-CH": "DD.MM.YYYY",
	  "it-IT": "DD/MM/YYYY",
	  "iu-Cans-CA": "D/M/YYYY",
	  "iu-Latn-CA": "D/MM/YYYY",
	  "ja-JP": "YYYY/MM/DD",
	  "ka-GE": "DD.MM.YYYY",
	  "kk-KZ": "DD.MM.YYYY",
	  "kl-GL": "DD-MM-YYYY",
	  "kM-KH": "YYYY-MM-DD",
	  "kn-IN": "DD-MM-YY",
	  "ko-KR": "YYYY. MM. DD",
	  "kok-IN": "DD-MM-YYYY",
	  "kY-KG": "DD.MM.YY",
	  "lb-LU": "DD/MM/YYYY",
	  "lo-LA": "DD/MM/YYYY",
	  "lt-LT": "YYYY.MM.DD",
	  "lv-LV": "YYYY.MM.DD.",
	  "Mi-NZ": "DD/MM/YYYY",
	  "Mk-MK": "DD.MM.YYYY",
	  "Ml-IN": "DD-MM-YY",
	  "Mn-MN": "YY.MM.DD",
	  "Mn-Mong-CN": "YYYY/M/D",
	  "Moh-CA": "M/D/YYYY",
	  "Mr-IN": "DD-MM-YYYY",
	  "Ms-BN": "DD/MM/YYYY",
	  "Ms-MY": "DD/MM/YYYY",
	  "Mt-MT": "DD/MM/YYYY",
	  "nb-NO": "DD.MM.YYYY",
	  "ne-NP": "M/D/YYYY",
	  "nl-BE": "D/MM/YYYY",
	  "nl-NL": "D-M-YYYY",
	  "nn-NO": "DD.MM.YYYY",
	  "nso-ZA": "YYYY/MM/DD",
	  "oc-FR": "DD/MM/YYYY",
	  "or-IN": "DD-MM-YY",
	  "pa-IN": "DD-MM-YY",
	  "pl-PL": "DD.MM.YYYY",
	  "prs-AF": "DD/MM/YY",
	  "ps-AF": "DD/MM/YY",
	  "pt-BR": "D/M/YYYY",
	  "pt-PT": "DD-MM-YYYY",
	  "qut-GT": "DD/MM/YYYY",
	  "quz-BO": "DD/MM/YYYY",
	  "quz-EC": "DD/MM/YYYY",
	  "quz-PE": "DD/MM/YYYY",
	  "rM-CH": "DD/MM/YYYY",
	  "ro-RO": "DD.MM.YYYY",
	  "ru-RU": "DD.MM.YYYY",
	  "rw-RW": "M/D/YYYY",
	  "sa-IN": "DD-MM-YYYY",
	  "sah-RU": "MM.DD.YYYY",
	  "se-FI": "D.M.YYYY",
	  "se-NO": "DD.MM.YYYY",
	  "se-SE": "YYYY-MM-DD",
	  "si-LK": "YYYY-MM-DD",
	  "sk-SK": "D. M. YYYY",
	  "sl-SI": "D.M.YYYY",
	  "sMa-NO": "DD.MM.YYYY",
	  "sMa-SE": "YYYY-MM-DD",
	  "sMj-NO": "DD.MM.YYYY",
	  "sMj-SE": "YYYY-MM-DD",
	  "sMn-FI": "D.M.YYYY",
	  "sMs-FI": "D.M.YYYY",
	  "sq-AL": "YYYY-MM-DD",
	  "sr-CYrl-BA": "D.M.YYYY",
	  "sr-CYrl-CS": "D.M.YYYY",
	  "sr-CYrl-ME": "D.M.YYYY",
	  "sr-CYrl-RS": "D.M.YYYY",
	  "sr-Latn-BA": "D.M.YYYY",
	  "sr-Latn-CS": "D.M.YYYY",
	  "sr-Latn-ME": "D.M.YYYY",
	  "sr-Latn-RS": "D.M.YYYY",
	  "sv-FI": "D.M.YYYY",
	  "sv-SE": "YYYY-MM-DD",
	  "sw-KE": "M/D/YYYY",
	  "sYr-SY": "DD/MM/YYYY",
	  "ta-IN": "DD-MM-YYYY",
	  "te-IN": "DD-MM-YY",
	  "tg-CYrl-TJ": "DD.MM.YY",
	  "th-TH": "D/M/YYYY",
	  "tk-TM": "DD.MM.YY",
	  "tn-ZA": "YYYY/MM/DD",
	  "tr-TR": "DD.MM.YYYY",
	  "tt-RU": "DD.MM.YYYY",
	  "tzM-Latn-DZ": "DD-MM-YYYY",
	  "ug-CN": "YYYY-M-D",
	  "uk-UA": "DD.MM.YYYY",
	  "ur-PK": "DD/MM/YYYY",
	  "uz-CYrl-UZ": "DD.MM.YYYY",
	  "uz-Latn-UZ": "DD/MM YYYY",
	  "vi-VN": "DD/MM/YYYY",
	  "wo-SN": "DD/MM/YYYY",
	  "xh-ZA": "YYYY/MM/DD",
	  "Yo-NG": "D/M/YYYY",
	  "zh-CN": "YYYY/M/D",
	  "zh-HK": "D/M/YYYY",
	  "zh-MO": "D/M/YYYY",
	  "zh-SG": "D/M/YYYY",
	  "zh-TW": "YYYY/M/D",
	  "zu-ZA": "YYYY/MM/DD",
	};
  
	return formats[navigator.language] || "DD/MM/YYYY";
  }

/*
 * 후원 정보 리스트 화면 렌더링
 */
function support_render(support_data) {
	let container = $('.pagination');
	container.pagination({
           		dataSource: support_data,
           		pageSize: 20,// 한 화면에 보여질 개수
           		showPrevious: false,// 처음버튼 삭제
		showNext: false,// 마지막버튼 삭제
		showPageNumbers: true,// 페이지 번호표시
		callback: function (data, pagination) {// 화면 출력
			var dataHtml = '';
			if (data == undefined || data.length == 0) {
				if (support_tab_type === 'DEFAULT') {
					$(".table-list").html('<div class="nodate">There is no sponsorship history.</div>');// 렌더링
				} else {
					$(".table-list").html('<div class="nodate">There is no account request.</div>');// 렌더링
				}
			} else {
				if (support_tab_type === 'DEFAULT') {
					let support_list = data.map((item, i) => {
						return '<li class="supports" id="support_' + item.id + '">' +
						'<span class="item account"><span class="key">ACCOUNT</span><span class="val">' + item.account_name + '</span></span>' +
						'<span class="item character"><span class="key">CHARACTER</span><span class="val">' + item.character_name + '</span></span>' +
						'<span class="item pay"><span class="key">PAY</span><span class="val">' + commaAdd(item.pay_amount) + '</span></span>' +
						'<span class="item date"><span class="key">DATE</span><span class="val">' + moment(item.write_date).format(getLocaleDateString() + ' HH:mm') + '</span></span>' +
						'<span class="item status"><span class="key">STATUS</span><span class="val ' + (item.status === 'FINISH' ? 'on' : '') + '">' + support_status[item.status] + '</span></span>' +
						'<span class="event">' + (item.status !== 'FINISH' ? '<button class="finish" onClick="support_finish(' + item.id + ')">Finish</button>' : '') + '<button onClick="support_delete(' + item.id + ')">Delete</button></span>'
		        				'</li>';
					}).join('');
					$(".table-list").html(support_list);// 렌더링
				} else {
					let support_list = data.map((item, i) => {
						return '<li class="supports" id="support_' + item.id + '">' +
						'<span class="item account"><span class="key">ACCOUNT</span><span class="val">' + item.account_name + '</span></span>' +
						'<span class="item character"><span class="key">CHARACTER</span><span class="val">' + item.character_name + '</span></span>' +
						'<span class="item date"><span class="key">REQUEST_DATE</span><span class="val">' + moment(item.request_date).format(getLocaleDateString() + ' HH:mm') + '</span></span>' +
						'<span class="item status"><span class="key">STATUS</span><span class="val ' + (item.response ? 'on' : '') + '">' + (item.response ? 'Transaction Complete' : 'Incomplete') + '</span></span>' +
						'<span class="event">' + (item.response === undefined ? '<button class="finish" onClick="support_request_finish(' + item.id + ')">Finish</button>' : '') + '<button onClick="support_delete(' + item.id + ')">Delete</button></span>'
		        				'</li>';
					}).join('');
					$(".table-list").html(support_list);// 렌더링
				}

			}

		}
        	});
}

/*
 * 신고 항목 게시물 이동 이벤트
 */
function report_move(type, rownum) {
	if (!confirm('Do you want to move to the selected item?')) {
		return;
	}
	if (type === 'BOARD') {
		urlform(rownum, 'post', '/board/view');
		return;
	}
	if (type === 'TRADE') {
		urlform(rownum, 'post', '/trade/view');
		return;
	}
	if (type === 'CONTENT') {
		urlform(rownum, 'post', '/contents/view');
		return;
	}
	if (type === 'PTICH') {
		urlform(rownum, 'post', '/pitch/view');
		return;
	}
}

/*
 * 신고 데이터 삭제
 */
function report_delete(id) {
	if (!confirm('Are you sure you want to delete the selected item?')) {
		return;
	}
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm/reportdelete',
		data:{report_id: id},
		success:function(data){
			switch (data) {
			case 1:
				$('#report_' + id).remove();
				alert('Deleted successfully.');
				break;
			case 2:
				alert('Could not find data to delete.');
				break;
			default:
				alert('Deletion failed.');
				break;
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

const report_type = {
	"LEWDNESS" : "Sexual",
	"ABUSIVE" : "Abuse",
	"ADVERTISE" : "Advertisement",
	"ILLEGALITY_PROGRAM" : "illegal program",
	"OVER_CONTENT" : "Content",
	"PERSONAL_INFORMATION" : "Privacy",
	"SPEAK_ILL" : "Lies",
	"ETC" : "Others"
};

/*
 * 신고 내역 화면 렌더링
 */
function report_render(report_data) {
	let container = $('.pagination');
	container.pagination({
           		dataSource: report_data,
           		pageSize: 20,// 한 화면에 보여질 개수
           		showPrevious: false,// 처음버튼 삭제
		showNext: false,// 마지막버튼 삭제
		showPageNumbers: true,// 페이지 번호표시
		callback: function (data, pagination) {// 화면 출력
			var dataHtml = '';
			if (data == undefined || data.length == 0) {
				dataHtml = '<div class="nodate">There is no report history.</div>';
			} else {
				$.each(data, function (index, item) {
					dataHtml +=
                   					'<li class="reports" id="report_' + item.id + '">' +
						'<span class="item name"><span class="key">NAME</span><span class="val">' + item.name + '</span></span>' +
						'<span class="item targetName"><span class="key">TARGET</span><span class="val">' + item.targetName + '</span></span>' +
						'<span class="item type"><span class="key">TYPE</span><span class="val">' + report_type[item.type] + '</span></span>' +
						'<span class="item log"><span class="key">LOG</span><span class="val">' + item.log + '</span></span>' +
						'<span class="item date"><span class="key">DATE</span><span class="val">' + moment(item.date).format(getLocaleDateString() + ' HH:mm') + '</span></span>' +
						'<span class="event"><button onClick="report_move(\'' + item.obj_type + '\', ' + item.obj.rownum + ')">Move</button><button onClick="report_delete(' + item.id + ')">Delete</button></span>'
		        				'</li>';
				});
			}
			$(".table-list").html(dataHtml);// 렌더링
		}
        	});
}

/*
 * 채팅 입력 이벤트
 */
function chat_run() {
	const chat_text = $('#chat_param').val();
	if (!chat_text) {
		return;
	}
	call_chat_data(chat_text);
	$('#chat_param').val('');
}

/*
 * 채팅 내역 화면 렌더링
 */
function chat_render(chat_data) {
	if (!chat_data) {
		return;
	}
	const box = $('#chat_result_box ul');
	if (box.prop('scrollHeight') > 10000) {
		box.text('');
	}
	$.each(chat_data, function (index, item) {
		box.append('<li>' + item + '</li>');
	});
	box.scrollTop(box.prop('scrollHeight'));
}

let call_chat_flag = false;
function call_chat_data(command_param) {
	if (call_chat_flag === true) {
		return;
	}
	const sendData = {
		typeFlag: 'CHAT_COMMAND',
		command: command_param
	};
	call_chat_flag = true;
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm',
		data:sendData,
		success:function(data){
			chat_render(data);
			call_chat_flag = false;
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			clearInterval(update_interval);
			call_chat_flag = false;
		}
	});
}

let call_flag = false;
function call_data() {
	if (call_flag === true) {
		return;
	}
	const sendData = {
		typeFlag: tabType
	};
	call_flag = true;
	$.ajax ({
		type:"post",
		datatype:"json",
		url:'/define/gm',
		data:sendData,
		success:function(data){
			switch (tabType) {
			case "SERVER":
				server_render(data);
				break;
			case 'USER':
				user_render(data);
				user_container = data;
				break;
			case 'SUPPORT':
				support_render(data.DEFAULT);
				support_container = data.DEFAULT;
				support_request_container = data.REQUEST;
				break;
			case 'REPORT':
				report_render(data);
				break;
			case 'CHAT':
				chat_render(data);
				break;
			}
			call_flag = false;
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			clearInterval(update_interval);
			call_flag = false;
		}
	});
}

/*
 * 주기적 실시간 갱신 항목
 */
function is_interval() {
	return tabType === 'SERVER' || tabType === 'CHAT';
}

$(function () {
   	if (tabType == '') {
		tabType = 'SERVER'
	}
	$('a[data-type=' + tabType + ']').addClass('on');
	init();
	call_data();
	if (is_interval()) {
		update_interval = setInterval(call_data, update_interval_millis);
	}

	var myTap = $('#mypageTab');
	var myTaps = myTap.find('li');
	myTaps.on('click', function(event){
		var seleteType = $(this).children('a').attr('data-type');
		if (tabType != seleteType) {
			if (update_interval !== undefined) {
				clearInterval(update_interval);
			}
			$('a[data-type=' + tabType + ']').removeClass('on');
			tabType = seleteType;
			$('a[data-type=' + tabType + ']').addClass('on');
			support_tab_type = 'DEFAULT';
			init();
			call_data();
			if (is_interval()) {
				update_interval = setInterval(call_data, update_interval_millis);
			}
		}
	});
});