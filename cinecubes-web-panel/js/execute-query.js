$(document).ready(function() {

	var isActive = true; //IF FOR WHATEVER REASON I WANT TO STOP POLLING
	var arrayData = [];
	var clients = 0;
	var selected = 1;

	$(function() {
		pollServer();
	});

	function pollServer() {
	    if (isActive) {
	        window.setTimeout(function () {
	            $.ajax({
	                url: "http://localhost:8080/api/ExecuteQuery",
	                type: "GET",
	                success: function (result) {

	                    if (arrayData.length == 0) {
	                    	for (var i = 0; i < result.length; i++) {
		                    	arrayData.push(result[i]);
		                    }
		                    var theType = "bar";
                    		createD3Viz(arrayData, theType);
	                	}
                    	if (isEqual(arrayData, result)) {
                    		//console.log("data received is already in array, skip.");
                    	} else {
                    		//console.log("new data");
                    		arrayData = [];
                    		for (var j = 0; j < result.length; j++) {
                    			arrayData.push(result[j]);
                    		}
                    		var theType = "bar";
                    		createD3Viz(arrayData, theType);
                    	}
	                    pollServer();
	                },
	                error: function () {
	                    //ERROR HANDLING
	                    console.log("ERROR");
	                    pollServer();
	                }});
	        }, 1000);
	    }
	}

	// change page content on client id click
	$("#secondnav").on("click", "li", function() {
    	$("#secondnav li.active").removeClass("active");
    	$(this).addClass("active");
    	var splitStr = "";
    	var toSplit = $(this).text();
    	splitStr = toSplit.split(" ");
    	selected = splitStr[1]; // what client tab user has selected
    	var theContent = "<div id=\"graphWell\" class=\"well\"><div class=\"card-columns\"><div class=\"card text-center\" id=\"infoCard\"><div class=\"card-body\"><h5 class=\"card-title text-center\" id=\"cardTitleText\">Info about query</h5></div></div><br></div><div class=\"text\" id=\"t2\"><h5 class=\"medium\">CineCubes Report</h5><div class=\"pull-right\" id=\"moreDetailsDiv\"><button type=\"button\" class=\"btn btn-primary btn-md\" id=\"moreDetailsButton\">More Details</button></div><p id=\"queryReport\"></p></div><br/><h5>Table representation of original act data</h5><p id=\"originalActText\"></p><div id=\"originalDataTable\"></div><div id=\"viz\"></div><br><br></div>";
    	$("#clientContent").html(theContent);
    	var theType = "bar";
    	createD3Viz(arrayData, theType);
	});

	function createD3Viz(theData, type) {

		var newArrayData = []; // data for original act bar viz
		document.getElementById("queryReport").innerHTML = null;
		document.getElementById("originalActText").innerHTML = null;
		document.getElementById("originalDataTable").innerHTML = null;

		for (var i = 0; i < theData.length; i++) {

			clients = parseInt(theData[i].clientID);

			if (theData[i].clientID == selected) {

				if (theData[i].notes) {
					let notes = theData[i].notes;
					notes = notes.replace(/Here, you can see the answer of the original query. /g, "");
					notes = notes.replace(/You can observe the results in this table. /g, "");
					notes = notes.replace(/We highlight the largest value with red and the lowest value with blue color. /g, "")
					
					document.getElementById("queryReport").innerHTML += notes + "<br />";
				}

				var originalPivotTable = theData[i].data;
				var originalData2dArray = [];
				if (originalPivotTable) {
					var splitOriginalTable = originalPivotTable.split("^");
					var region = "";
					for (let k = 0; k < splitOriginalTable.length; k++) {
						if (k == 0) { // get the region or header or w/e
							region = splitOriginalTable[k].replace(/\[|]|/g, "").trim(); // replace [ ] and , to get only the region
							var splitRegion = region.split(",");
							originalData2dArray.push(new Array(splitRegion.length));
							for (let m = 0; m < splitRegion.length; m++) {
								originalData2dArray[0][m] = splitRegion[m].trim();
							}
							originalData2dArray[0][0] = "y-column";
						} else { // get the data
							if (splitOriginalTable[k] != "") { // if it's not empty
								var originalActData = splitOriginalTable[k].replace(/\[|]/g, "").trim();
								var splitOriginalData = originalActData.split(",");
								originalData2dArray.push(new Array(splitOriginalData.length));
								var splitDate = splitOriginalData[0].split("-");
								var theDate = "";
								if (splitDate.length > 1) {
									theDate = splitDate[1] + "-" + splitDate[0];
								} else {
									theDate = splitDate[0] + "";
								}
								for (let m = 1; m < splitOriginalData.length; m++) {
									originalData2dArray[k][m] = parseFloat(splitOriginalData[m]);
									originalData2dArray[k][0] = theDate;
									newArrayData.push({ year: theDate, value: parseFloat(splitOriginalData[m]), region: originalData2dArray[0][m] });
								}
							}
						}
					}
					createTable(originalData2dArray);

					var regions = [];
					let temp = {"All": "All"};
					regions.push(temp);
					for (let r = 0; r < newArrayData.length; r++) {
						if (newArrayData[r].region) {
							let temp = {};
							temp[newArrayData[r].region] = newArrayData[r].region;
							regions.push(temp);
						}
					}
					document.getElementById("viz").innerHTML = null;
					var visualization = d3plus.viz()
						.container("#viz")
						.width(1000)
						.height(500)
						.data(newArrayData)
						.title("Original act chart for client " + selected)
						.type("bar", function(value, viz) {
							if (value == "scatter") {
								viz.shape("circle");
							} else if (value == "bar") {
								viz.shape("square");
							}
						})
						.id("region")
						.y("value")
						.x("year")
						.color("region")
						.ui([
					      	{ 
						        "label": "Visualization Type",
						        "method": "type",
						        "value": [ "bar", "scatter" ]
						    },
						    {
						    	"label": "Show 1:",
						    	"type": "drop",
						    	"value": regions,
						    	"method": function(value, viz) {
						    		if (value == "all") {
										viz.id( { "value": "region", "solo" : [] } ).depth(0).draw();
						    		} else {
						    			viz.id( {"value": "region", "solo" : [value]} ).depth(1).draw();
						    		}
						    	}
						    },
							{
							 	"label": "Group by:",
							   	"value": [ "region", "year" ],
							   	"method": function(value, viz) {
							   		if (value == "year") {
							   			viz.id({"value": "year"}).x({"value": "region"}).time("year").draw();
							   		} else if (value == "region") {
							   			viz.id({"value": "region"}).x({"value": "year"}).order({"sort": "asc", "value": "year"}).time(false).draw();
							   		}
							   	}
							}
						])
						.draw();
				}
			}
		}
		//var counter = document.getElementById("secondlist").getElementsByClassName("LI").length;
		var counter = $('ul#secondlist li').length;
		for (var i = counter+1; i <= clients; i++) {
			$("#secondlist").append("<li><a href=\"#\" id=\"client\"" + i + ">Client " + i + "</a></li>");
		}
	}

	$(".container").on("click", "#moreDetailsButton", function () {
		var moreDetailsWindow = window.open("more_details.html?client="+selected+"");
	});

	// checks if 2 objects are equal
	var isEqual = function (value, other) {

		// Get the value type
		var type = Object.prototype.toString.call(value);

		// If the two objects are not the same type, return false
		if (type !== Object.prototype.toString.call(other)) return false;

		// If items are not an object or array, return false
		if (['[object Array]', '[object Object]'].indexOf(type) < 0) return false;

		// Compare the length of the length of the two items
		var valueLen = type === '[object Array]' ? value.length : Object.keys(value).length;
		var otherLen = type === '[object Array]' ? other.length : Object.keys(other).length;
		if (valueLen !== otherLen) return false;

		// Compare two items
		var compare = function (item1, item2) {

			// Get the object type
			var itemType = Object.prototype.toString.call(item1);

			// If an object or array, compare recursively
			if (['[object Array]', '[object Object]'].indexOf(itemType) >= 0) {
				if (!isEqual(item1, item2)) return false;
			}

			// Otherwise, do a simple comparison
			else {

				// If the two items are not the same type, return false
				if (itemType !== Object.prototype.toString.call(item2)) return false;

				// Else if it's a function, convert to a string and compare
				// Otherwise, just compare
				if (itemType === '[object Function]') {
					if (item1.toString() !== item2.toString()) return false;
				} else {
					if (item1 !== item2) return false;
				}

			}
		};

		// Compare properties
		if (type === '[object Array]') {
			for (var i = 0; i < valueLen; i++) {
				if (compare(value[i], other[i]) === false) return false;
			}
		} else {
			for (var key in value) {
				if (value.hasOwnProperty(key)) {
					if (compare(value[key], other[key]) === false) return false;
				}
			}
		}

		// If nothing failed, return true
		return true;

	};

});

function createTable(tableData) {
	// find max and min value
	var max = -9999999;
	var min = 9999999;
	for (let i = 1; i < tableData.length; i++) {
		for (var j = 1; j < tableData[i].length; j++) {
			if (parseFloat(tableData[i][j])) {
				if (parseFloat(tableData[i][j]) > max) {
					max = parseFloat(tableData[i][j]);
				}
				if (parseFloat(tableData[i][j]) < min) {
					min = parseFloat(tableData[i][j]);
				}
			}
		}
	}
	// create <table> element and fill it with data
	document.getElementById("originalDataTable").innerHTML += "<br/>";
	var table = document.createElement('table');
	var tableBody = document.createElement('tbody');
	tableData.forEach(function(rowData) {
		var row = document.createElement('tr');
		rowData.forEach(function(cellData) {
			var cell = document.createElement('td');
			if (parseFloat(cellData) == max) {
				cell.style.color = "red";
			} else if (parseFloat(cellData) == min) {
				cell.style.color = "blue";
			}
			cell.appendChild(document.createTextNode(cellData));
			row.appendChild(cell);
		});
		tableBody.appendChild(row);
	});
	table.appendChild(tableBody);
	//document.body.appendChild(table);
	document.getElementById("originalDataTable").appendChild(table);
}