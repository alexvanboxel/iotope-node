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
				addTag(tagChange.reader, tagChange.slot, tagChange.tag);
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

/**
 * Add the tag to the reader slot, and display application
 */
function addTag(reader, slot, tag, tagapp) {
	var sid = "#R" + reader.terminalId + "S" + slot;
	$(sid).text(tag);
	// for now only support slot 1
	if(slot == 0) {
		var app = $(".app", ".template").clone();
		$(".tagaction", app).click(function() {
			$("#tagId", "#context").text(tag);
			$("#readerId", "#context").text(reader.terminalId);
			$("#app-dialog").dialog('open');
			loadDialog();
		});
		$("table", app).attr("id", "A" + tag);
		var cnt_app = $(".cnt_app", "#R" + reader.terminalId);
		cnt_app.html(app);
	}
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