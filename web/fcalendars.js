/**
 * The purpose is add cssClass to a calendar, if if date is in specialDays
 * @param specialDays json dict of dates
 * @param date date to verify if is in specialDays
 * @param cssClass class to be added
 * @returns {Array} refer to http://jqueryui.com/demos/datepicker/
 */
function highlightCalendar(specialDays, date, cssClass) {
	var d = date.getDate();
	var m = date.getMonth() + 1;
	var y = date.getFullYear();
	
	if (specialDays[y] && specialDays[y][m]
		&& specialDays[y][m][d]) {
		var s = specialDays[y][m][d];
		return [ true, cssClass, '' ];
	}
	
	return [ true, '' ]; // no change
}

/**
 * Binds the event beforeShowDay to every calendar found
 */
function bindEventsHighlights() {
    jQuery(".hasDatepicker").datepicker("option", "beforeShowDay", function (date) {
        return highlightCalendar(specialDays, date, 'highlight-calendar');
    });
}