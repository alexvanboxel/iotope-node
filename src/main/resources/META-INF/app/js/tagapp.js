var subscription1;
var infoChannel;
var tagChannel;
var readerChannel;
var actionChannel;

function setupDialog() {
	var buttons = {
		"Select" : function() {
			//			$("#tagId","#context").text(tag);
			//			$("#readerId","#context").text(reader.terminalId);
			$.cometd.publish("/service/rest/app", {
				type : "assignApplication",
				tagId : $("#tagId", "#context").text(),
				readerId : $("#readerId", "#context").text(),
				appId : $("#appId", "#context").text()
			});
			$(this).dialog("close");
		},
		"Cancel" : function() {
			$(this).dialog("close");
		}
	}

	$("#app-dialog").dialog({
		title : "Select Application",
		autoOpen : false,
		modal : true,
		buttons : buttons
	});
}

function selectApp(obj) {
	var appId = $(obj.srcElement).attr("appId");
	var dialog = $("#app-dialog");
	var links = $("a", dialog);
	for( i = 0; i < links.length; i++) {
			$(links[i]).attr("class", "appLink");
	}
	$(obj.srcElement).attr("class", "appSelected");
	$("#appId", "#context").text(appId);
}

function loadDialog() {
	var dialog = $("#app-dialog");
	var links = $("a", dialog);
	for( i = 0; i < links.length; i++) {
		$(links[i]).click(selectApp);
		if( i == 0) {
			$(links[i]).attr("class", "appSelected");
		} else {
			$(links[i]).attr("class", "appLink");
		}
	}
	$("#appId", "#context").text("1");
}

$(function() {
	subscription1 = $.cometd.addListener('/meta/connect', function() {
		infoChannel = $.cometd.subscribe('/info', function(message) {
			if(message.data.type == "ReadersInfo") {
				var readers = message.data.readers;
				for(var i = 0; i < readers.length; i++)addReader(readers[i]);
			} else if(message.data.type == "CorrelationMode") {
				var mode = message.data.mode;
			}
		});
		tagChannel = $.cometd.subscribe('/tag', function(message) {
			var tagChange = message.data;
			if(tagChange.event == "REMOVED") {
				removeTag(tagChange.reader, tagChange.slot);
			} else if(tagChange.event == "ADDED") {
				addTag(tagChange);
			}
		});
		readerChannel = $.cometd.subscribe('/reader', function(message) {
			var readerChange = message.data;
			if(readerChange.event == "REMOVED") {
				removeReader(readerChange.reader);
			} else if(readerChange.event == "ADDED") {
				addReader(readerChange.reader);
			}
		});
		actionChannel = $.cometd.subscribe("/service/rest/**", function(message) {
			var messageData = message.data;
			if(messageData.type == "assignApplication") {
				assignApp(messageData);
			}

			// if (readerChange.event == "REMOVED") {
			// removeReader(readerChange.reader);
			// } else if (readerChange.event == "ADDED") {
			// addReader(readerChange.reader);
			// }
		});

		$.cometd.publish("/info", {
			type : "ReadersInfo"
		});
	});
	$.cometd.configure({
		url : 'http://localhost:4242/cometd',
		logLevel : 'DEBUG'
	});
	$.cometd.handshake();

	setupDialog();

	$("#learn").click("dat", function(eventObject) {
		$.cometd.publish("/service/rest/bla", {
			method : "PUT",
			type : "setLearnMode",
			learn : $(this)[0].checked
		});
	});
});
function removeReader(reader) {
	var tid = "#R" + reader.terminalId;
	if($(tid) != undefined) {
		$(tid).remove();
	}
}

function addReader(reader) {
	var tid = "#R" + reader.terminalId;
	var t = $(tid);
	if($(tid).size() == 0) {
		var row = $(".readerrow", ".template").clone().attr("id", "R" + reader.terminalId);
		row.find(".readername").text(reader.pcscName);
		for(var s = 0; s < reader.slots; s++) {
			var slot = $(".readerslot", ".template").clone();
			slot.attr("id", "R" + reader.terminalId + "S" + s);
			row.find(".readerslots").append(slot);
		}
		$("#readers").append(row);
	}
}


function tagContentNdef(tc) {
	var html = '<li>';
	html += '<span>' + tc.type + '</span>'
	html += '<span><ul>';
	for(c in tc.content) {
		var ndef = tc.content[c];
		html += '<li>' +ndef.content + '<li>'; 
	}
	html += '</ul></span>'
	html += '</li>';
	return html;		
}

function tagContent(tc) {
	if("NDEF" == tc.type) {
		return tagContentNdef(tc);
	}
	var html = '<li>';
	html += '<span>' + tc.type + '</span>'
	html += '<span>' + tc.content + '</span>'
	html += '</li>';
	return html;		
}

function tagRepresentation(tagChange) {
	var  html = '<div class="tag">' +
	
	'<span class="tagid">' +tagChange.nfcid + '</span>';
	
	
	for(c in tagChange.content) {
		html += tagContent(tagChange.content[c]);
	}

	
	html += '</div>';
	return html;
	
//	<div class="tag">
//	<div class="tagtype">
//		<span class="tagtypeid"></span><span class="tagtypename">NXP
//			Classic</span>
//	</div>
//	<span class="tagid">01020304050607</span>
//	<ul class="ndef">
//		<li>
//			http://www.iotope.com
//		</li>
//		<li>
//			text
//		</li>
//		<li>
//			signature
//		</li>
//	</ul>
//</div>

}


/**
 * Add the tag to the reader slot, and display application
 */
function addTag(tagChange) {
	var reader = tagChange.reader;
	var slot = tagChange.slot;
	var nfcid = tagChange.nfcid;
	
	var sid = "#R" + reader.terminalId + "S" + slot;
	//$(sid).text(nfcid);
	
	
	$(sid).html(tagRepresentation(tagChange));
	
	
	// for now only support slot 1
	if(slot == 0) {
		var app = $(".app", ".template").clone();
		$(".tagaction", app).click(function() {
			$("#tagId", "#context").text(nfcid);
			$("#readerId", "#context").text(reader.terminalId);
			$("#app-dialog").dialog('open');
			loadDialog();
		});
		$("table", app).attr("id", "A" + nfcid);
		var cnt_app = $(".cnt_app", "#R" + reader.terminalId);
		cnt_app.html(app);
	}
	showAppData(reader.terminalId,nfcid,tagChange.application,tagChange.fields);
}

/**
 *
 */
function removeTag(reader, slot) {
	var sid = "#R" + reader.terminalId + "S" + slot;
	$(sid).text("");
	//
	$(".cnt_app", "#R" + reader.terminalId).text("");
}

function saveApp(obj) {
	var tableBody = $("tbody", "#R" + $(obj.srcElement).attr("readerId"));
	var fields = $("input", tableBody);

	var message = {
		type : "saveApplication",
		tagId : $(obj.srcElement).attr("tagId"),
		readerId : $(obj.srcElement).attr("readerId"),
		appId : $(obj.srcElement).attr("appId"),
		fields : new Array(fields.length)
	}

	for( i = 0; i < fields.length; i++) {
		message.fields[i] = {
			name : $(fields[i]).attr("name"),
			value : $(fields[i]).attr("value")
		}
	}
	$.cometd.publish("/service/rest/app", message);
}

function assignApp(message) {
	var tableBody = $("tbody", "#A" + message.tagId);
	tableBody.text("");
	for(field in message.fields) {
		var field = message.fields[field];
		tableBody.append('<tr><td>' + field.displayName + '</td><td><input name="' + field.name + '"></input></td><td>' + field.description + '</td></tr>');
	}
	var savelink = $(".tagsave", "#R" + message.readerId);
	savelink.attr("tagId", message.tagId);
	savelink.attr("appId", message.appId);
	savelink.attr("readerId", message.readerId);
	savelink.click(saveApp);
}

// TODO: JOIN assignApp and showAppData!
function showAppData(readerId,nfcid,application,fields) {
	if (application) {
		appId = application.appId;
		var tableBody = $("tbody", "#A" + nfcid);
		tableBody.text("");
		for (field in fields) {
			var field = fields[field];
			tableBody.append('<tr><td>' + field.displayName
					+ '</td><td><input name="' + field.name
					+ '" value="'+field.value+'"></input></td><td>' + field.description + '</td></tr>');
		}
		var savelink = $(".tagsave", "#R" + readerId);
		savelink.attr("tagId", nfcid);
		savelink.attr("appId", appId);
		savelink.attr("readerId", readerId);
		savelink.click(saveApp);
	}
}