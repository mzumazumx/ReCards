function showBack(button) {
	$('.schedule:last').show();
	$(button).hide();
}

function rate(button, id, difficulty) {
	$(button).addClass("correct");
	$.post(cards_rate({id:id}), {'difficulty': difficulty}, function(data) {
		location.reload();
	});
}