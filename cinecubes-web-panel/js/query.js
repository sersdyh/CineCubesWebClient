$(document).ready(function() {

    var dbNames = "";

    // populate list with database names
	$.ajax({
		url: "php/db_names.php",
	    type: "GET",
	    async: false,
	    success: function(response) {
	        dbNames = response;
	    }
	});

    dbNames = dbNames.trim();
    var dbNamesList = dbNames.split(" ");
    var found = false;
    for (name in dbNamesList) {
    	if (dbNamesList[name] == "pkdd99") {
    		$("#databaseSelection").append("<option selected>" + dbNamesList[name] + "</option>");
    		$.ajax({
		    	url: "php/db_tables.php",
		    	type: "GET",
		    	//async: false,
		    	data: {
		    		theTable: $("#databaseSelection").val()
		    	},
			    success: function(response) {
			        let dbTables = response;
			        dbTables = dbTables.trim();
					var dbTablesList = dbTables.split(" ");
					for (table in dbTablesList) {
						$("#selectTable").append("<option>" + dbTablesList[table] + "</option>");
					}
			    }
	    	});
    	} else {
    		$("#databaseSelection").append("<option>" + dbNamesList[name] + "</option>");
    	}
    }

    // populate list with table names according to what database user selected
	document.getElementById("databaseSelection").addEventListener("change", function esketit() {
		var dbTables = null;
		var theTable = $("#databaseSelection").val();
	    $.ajax({
	    	url: "php/db_tables.php",
	    	type: "GET",
	    	//async: false,
	    	data: {
	    		theTable: theTable
	    	},
		    success: function(response) {
		    	let selectbox = document.getElementById("selectTable");
		    	for(let i = selectbox.options.length - 1; i >= 0; i--) {
			        selectbox.remove(i);
			    }
		        dbTables = response;
		        dbTables = dbTables.trim();
				var dbTablesList = dbTables.split(" ");
				for (table in dbTablesList) {
					$("#selectTable").append("<option>" + dbTablesList[table] + "</option>");
				}
		    }
	    });
	});

	// send execute client request to Cinecubes server if user clicks confirm
	/* data send is a json object like this example(if it is string query, file query option available too):
		"cubeName":aCubeName
		"Name":aCubeQueryName(e.g. CubeQueryLoan)
		"AggrFunc":aFunc(e.g. Avg)
		"Measure":aMeasure(e.g. amount)
		"Gamma":table.level
		"Sigma":table.level=value
	*/
	$("#executeQueryConfirm").click(function() {
		var queryType = $(".queryTypeCheckbox:checked").val();

		if (queryType == "stringQuery") { // if string query get data from inputs
			var database = $("#databaseSelection").val();
			var cubeName = $("#cname").val();
			var name = $("#name").val();
			var AggrFunc = $("#aggrFunc").val();
			var Measure = $("#measure").val();
			var Gamma = $("#gamma").val();
			var Sigma = $("#sigma").val();
			$.ajax({
		    	url: "http://localhost:8080/api/ExecuteServer",
		    	type: "POST",
		    	data: {
		    		"database": database,
		    		"queryType": queryType,
		    		"cubeName": cubeName,
		    		"name": name,
		    		"AggrFunc": AggrFunc,
		    		"Measure": Measure,
		    		"Gamma": Gamma,
		    		"Sigma": Sigma
		    	}
		    });
		} else if (queryType == "fileQuery") { // if file query get data from files
			var database = $("#databaseSelection").val();
			var cubeName = $("#cname").val();
			console.log(cubeName);
			console.log(lastUrl);
			$.ajax({
		    	url: "http://localhost:8080/api/ExecuteServer",
		    	type: "POST",
		    	data: {
		    		"database": database,
		    		"queryType": queryType,
		    		"fileList": lastUrl,
		    		"cubeName": cubeName
		    	}
		    });
		    lastUrl = "";
		} else {
			alert("ERROR in string or file");
		}
	});

	var lastUrl = "";
	$(".container").on("change", "#file", function() {
		window.URL = window.URL || window.webkitURL;
		var file = document.getElementById("file");
	    var request = new XMLHttpRequest();
	    request.open("POST", "http://localhost/cinecubes/php/process.php", false);
	    var formData = new FormData();
	    for (let i = 0; i < file.files.length; i++) {
	    	lastUrl = "C://xampp/htdocs/cinecubes/uploads/" + file.files[i].name;
	    	formData.append("uploaded_file", file.files[i]);
	    }
	    request.send(formData);
	    //console.log(request.response);
	});

	var last = "";
	$("#sigma").focus(function() {
		last = "sigma";
	});
	$("#gamma").focus(function() {
		last = "gamma";
	});
	$("#selectTable").on("dblclick", function() {
    	if (last == "sigma") {
    		let theSigma = $("#sigma").val();
    		theSigma += " " + $("#selectTable").val();
    		$("#sigma").val(theSigma);
    	} else if (last == "gamma") {
    		let theGamma = $("#gamma").val();
    		theGamma += " " + $("#selectTable").val();
    		$("#gamma").val(theGamma);
    	}
	});

	// change modal content according to what type of query user selected(from string or from file)
	$("#queryTypeSelect").on("change", function() {
		//console.log($(".queryTypeCheckbox:checked").val());
		var stringOrFile = $(".queryTypeCheckbox:checked").val();
		if (stringOrFile == "stringQuery") {
			let theContent = "<div class=\"row\"><form><div class=\"form-group\"><label for=\"cname\">Cube Name:</label><input type=\"text\" class=\"form-control\" id=\"cname\" placeholder=\"Cube Name\"></div><div class=\"form-group\"><label for=\"name\">Name:</label><input type=\"text\" class=\"form-control\" id=\"name\" placeholder=\"Name\"></div><div class=\"form-group\"><label for=\"aggrFunc\" class=\"col-sm-3 control-label\">Aggregate Function:</label><div class=\"col-sm-3\"><select class=\"form-control\" id=\"aggrFunc\"><option>--</option><option>AVG</option><option>COUNT</option><option>MIN</option><option>MAX</option><option>SUM</option></select></div></div><br><br><div class=\"form-group\"><label for=\"measure\">Measure:</label><input type=\"text\" class=\"form-control\" id=\"measure\" placeholder=\"Measure\"></div><div class=\"form-group\"><label for=\"gamma\">Gamma:</label><input type=\"text\" class=\"form-control\" id=\"gamma\" placeholder=\"Gamma\"></div><div class=\"form-group\"><label for=\"sigma\">Sigma:</label><input type=\"text\" class=\"form-control\" id=\"sigma\" placeholder=\"Sigma\"></div></form><label for=\"selectTable\"><p><big>Tables in selected database:</big></p></label><select class=\"form-control\" id=\"selectTable\" multiple></select></div>";
			$("#stringOrFile").html(theContent);
		} else if (stringOrFile == "fileQuery") {
			//let theContent = "<form method=\"post\" enctype=\"multipart/form-data\"><div id=\"fileDiv\"><label for=\"file\">Choose file to upload (.ini)<br /></label><input type=\"file\" id=\"file\" name=\"file\" accept=\".ini\" multiple></div></form>";
			let theContent = "<label for=\"cname\">Cube Name:</label><input type=\"text\" class=\"form-control\" id=\"cname\" placeholder=\"Cube Name\"></div><br/><form method=\"post\" enctype=\"multipart/form-data\" id=\"uploadForm\"><input type=\"file\" name=\"uploaded_file\" id=\"file\">";
			$("#stringOrFile").html(theContent);
		} else {
			$("#stringOrFile").html("ERROR");
		}
	});

	// clear data if user clicks on reset button
	$("#resetButton2").click(function() {
		$("#cname").val(null);
		$("#name").val(null);
		$("#aggrFunc").val("--");
		$("#measure").val(null);
		$("#gamma").val(null);
		$("#sigma").val(null);
	});

});