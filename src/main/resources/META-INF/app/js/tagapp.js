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
	$.cometd.publish("/service/rest/applications", {
		method : "PUT",
		type : "getApplications"
	});
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
			if(message.channel == "/service/rest/applications") {
				// add apps
				html = "";
				for(c in message.data) {
					app = message.data[c]
					html += '<li><a class="appLink" appId="'+ app.appId + '">' + app.displayName + '</a></li>';
				}
				$("#app-links").html(html);
				// link and select current
				var links = $("a", "#app-dialog");
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
	$(".optchkbox").click("dat", function(eventObject) {
		$.cometd.publish("/service/rest/bla", {
			method : "PUT",
			type : "setBooleanOption",
			name: $(this)[0].name,
			value : $(this)[0].checked
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
	html += '<span>NDEF <span class="explain">Messages formatted following the NFC spec.</span></span>'
	html += '<span><ul>';
	for(c in tc.ndef.records) {
		var ndef = tc.ndef.records[c];
		
		html += '<li>' +ndef.representation + '</li>'; 
	}
	html += '</ul></span>'
	html += '</li>';
	return html;		
}

function tagContentBlock(tc,type,explain) {
	var html = '<li>';
	html += '<span>' + type + ' <span class="explain">'+explain+'</span></span><br/>'
	html += '<span class="binhex">' + tc.block + '</span>'
	html += '</li>';
	return html;		
}

function tagContent(tc) {
	if("NDEF" == tc.type) {
		return tagContentNdef(tc);
	}
	else if("LEGACY_HASH" == tc.type) {
		return tagContentBlock(tc,"Touchatag Signature Hash","A recognized Touchatag signature.");
	}
	else if("MEMORY_RW_BLOCK" == tc.type) {
		return tagContentBlock(tc,"RW Memory","Current content of a writable block of memory in the tag.");
	}
	var html = '<li>';
	html += '<span>' + tc.type + '</span>'
	html += '<span class="binhex">' + tc.content + '</span>'
	html += '</li>';
	return html;		
}

function tagRepresentation(tagChange) {
	var  html = '<div class="tag">' +
	'<span class="tagid">' +tagChange.nfcId + '</span>';
	for(c in tagChange.content.blocks) {
		html += tagContent(tagChange.content.blocks[c]);
	}
	html += '</div>';
	return html;
}


/**
 * Add the tag to the reader slot, and display application
 */
function addTag(tagChange) {
	var reader = tagChange.reader;
	var slot = tagChange.slot;
	var nfcId = tagChange.nfcId;
	
	var sid = "#R" + reader.terminalId + "S" + slot;
	$(sid).html(tagRepresentation(tagChange));
	
	// for now only support slot 1
	if(slot == 0) {
		var app = $(".app", ".template").clone();
		$(".tagaction", app).click(function() {
			$("#tagId", "#context").text(nfcId);
			$("#readerId", "#context").text(reader.terminalId);
			$("#app-dialog").dialog('open');
			loadDialog();
		});
		$("table", app).attr("id", "A" + nfcId);
		var cnt_app = $(".cnt_app", "#R" + reader.terminalId);
		cnt_app.html(app);
	}
	showAppData(reader.terminalId,nfcId,tagChange.application,tagChange.fields);
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
function showAppData(readerId,nfcId,application,fields) {
	if (application) {
		appId = application.appId;
		var tableBody = $("tbody", "#A" + nfcId);
		tableBody.text("");
		for (field in fields) {
			var field = fields[field];
			tableBody.append('<tr><td>' + field.displayName
					+ '</td><td><input name="' + field.name
					+ '" value="'+field.value+'"></input></td><td>' + field.description + '</td></tr>');
		}
		var savelink = $(".tagsave", "#R" + readerId);
		savelink.attr("tagId", nfcId);
		savelink.attr("appId", appId);
		savelink.attr("readerId", readerId);
		savelink.click(saveApp);
	}
}