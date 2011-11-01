var SearchHighlighter = new Class({
	initialize: function(searchTerm) {
		if (searchTerm.length <= 0) return;
		var searchRe = new RegExp(searchTerm, "i");

		var t = $$("table.searchTable");
		var tlen = t.length;
		while(tlen--) {
		    var searchTable = t[tlen];
		    var trs = searchTable.getElement('tbody').getElements("tr");
		    var trlen = trs.length;
		    while(trlen--) {
			    var tds = trs[trlen].getChildren("td");
			    var tdlen = tds.length;
			    while(tdlen--) {
			        var td = tds[tdlen];
			        var text = td.get("text");        
			        if (searchRe.test(text)) {
			        	var match = searchRe.exec(text);
			            text = text.replace(searchRe, '<span class="match">' + match[0] + '</span>');
			            td.set('html', text);
			        }
			    }
		    }
		}
	}
});