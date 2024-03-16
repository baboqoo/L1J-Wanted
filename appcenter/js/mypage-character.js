$(function() {
	if (account === undefined || account.firstChar === undefined) {
		$('.wrap-my-character').html('The default character has not been defined');
		return;
	}
		var character	= account.firstChar;
		var clan		= character.clan;
		if (clan !== undefined) {
			$('.section-my-character.char-info strong').html(clan.clanName + '&nbsp;-&nbsp;Prince(ss):&nbsp;' + clan.leaderName + '&nbsp;-&nbsp;Points:&nbsp;' + clan.clanExp + '&nbsp;-&nbsp;Pledge Members:&nbsp;' + clan.totalMember);
		} else {
			$('.section-my-character.char-info strong').html('Without Pledge');
		}
		if (account.gm) {
			$('#gm-info').html('<i style="color: #aa8060;">Manager</i>');
		}
		$('.section-my-character.char-level .box .level-info').html('<strong>Level ' + character.level + '</strong><span class="figure">' + character.expPercent + '%</span>');
		$('.section-my-character.char-level .box .stat .bar').css('width', character.expPercent + '%');
		$('.section-my-character.char-level .ranking .ranking-server dd').html(character.allRank);
		$('.section-my-character.char-level .ranking .ranking-class').html('<dt>' + character.className + ' Class Rank</dt><dd>' + character.classRank + '</dd>');
		$('.hpmp-info .hp .var').html(character.maxhp +' / ' + character.maxhp);
		$('.hpmp-info .mp .var').html(character.maxmp +' / ' + character.maxmp);
		$('#stat-str').html('<div class="graph"><span class=\"bar percent-' + (character.str << 1) + '\"></span></div><span class="var">' + character.str + '</span>');
		$('#stat-int').html('<div class="graph"><span class=\"bar percent-' + (character.intel << 1) + '\"></span></div><span class="var">' + character.intel + '</span>');
		$('#stat-dex').html('<div class="graph"><span class=\"bar percent-' + (character.dex << 1) + '\"></span></div><span class="var">' + character.dex + '</span>');
		$('#stat-wis').html('<div class="graph"><span class=\"bar percent-' + (character.wis << 1) + '\"></span></div><span class="var">' + character.wis + '</span>');
		$('#stat-con').html('<div class="graph"><span class=\"bar percent-' + (character.con << 1) + '\"></span></div><span class="var">' + character.con + '</span>');
		$('#stat-cha').html('<div class="graph"><span class=\"bar percent-' + (character.cha << 1) + '\"></span></div><span class="var">' + character.cha + '</span>');
		$('.pk-info').html('<dt>PK count</dt><dd>' + character.pk + '</dd><dt>Alignment</dt><dd>' + character.lawful + '</dd>');
});