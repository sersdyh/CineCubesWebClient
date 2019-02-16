$(document).ready(function() {

	var url_string = window.location.href;
	var url = new URL(url_string);
	var currentClient = url.searchParams.get("client");
	document.getElementById("clientID").innerHTML += currentClient;

	var arrayData = []; // all data

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
		}
	});

	function createD3Viz(theData, theType) {
		var act1GraphCounter = 0;
		var tc = 0;
		var newArrayData = []; // data for original act bar viz
		var innerNode = document.getElementById("appendNodes");
		innerNode.innerHTML = "";

		for (var i = 0; i < theData.length; i++) {
			var act1Data2dArray = []; // data for act1 viz
			if (theData[i].clientID == currentClient) {
				var act1PivotTable = theData[i].act1Data;
				if (theData[i].act1Notes) {
					$(".inner").append("<br><br/><br><br/><p>" + theData[i].act1Notes + "</p><br/><br/>");
				}
				if (act1PivotTable) {
					newArrayData = [];
					act1GraphCounter++;
					var splitAct1Table = act1PivotTable.split("^");
					var region = "";
					for (let k = 0; k < splitAct1Table.length; k++) {
						if (k == 0) { // get the region or header or w/e
							region = splitAct1Table[k].replace(/\[|]|/g, "").trim(); // replace [ ] and , to get only the region
							var splitRegion = region.split(",");
							act1Data2dArray.push(new Array(splitRegion.length));
							for (let m = 0; m < splitRegion.length; m++) {
								act1Data2dArray[0][m] = splitRegion[m].trim();
							}
							//act1Data2dArray[0][0] = "y-column";
						} else { // get the data
							if (splitAct1Table[k] != "") { // if it's not empty
								var act1Data = splitAct1Table[k].replace(/\[|]/g, "").trim();
								var splitAct1Data = act1Data.split(",");
								//document.getElementById("originalActText").innerHTML += act1Data + "<br/>";
								act1Data2dArray.push(new Array(splitAct1Data.length));
								var splitDate = splitAct1Data[0].split("-");
								var theDate = "";
								if (splitDate.length > 1) {
									theDate = splitDate[1] + "-" + splitDate[0];
								} else {
									theDate = splitDate[0] + "";
								}
								for (let m = 1; m < splitAct1Data.length; m++) {
									tc++;
									if (parseFloat(splitAct1Data[m])) {
										act1Data2dArray[k][m] = splitAct1Data[m];
									} else {
										act1Data2dArray[k][m] = "-";
									}
									act1Data2dArray[k][0] = theDate;
									if (act1Data2dArray[k][m] != "-") {
										newArrayData.push({ year: theDate, value: parseFloat(act1Data2dArray[k][m]), region: act1Data2dArray[0][m] });
									}
								}
							}
						}
					}
					createTable(act1Data2dArray);

					var regions = [];
					let temp = {"All": "All"};
					regions.push(temp)
					for (let r = 0; r < newArrayData.length; r++) {
						if (newArrayData[r].region) {
							let temp = {};
							temp[newArrayData[r].region] = newArrayData[r].region;
							regions.push(temp);
						}
					}

					$(".inner").append("<div id=\"viz" + act1GraphCounter + "\"></div>");
					var act1Visualization = d3plus.viz()
						.container("#viz" + act1GraphCounter)
						.data(newArrayData)
						.title("Act1 chart for client " + currentClient)
						.type("bar")
						.id("region")
						.width(1000)
						.height(600)
						.y("value")
						.x("year")
						.ui([
							{ 
						       "label": "Visualization Type",
						        "method": "type", 
						        "value": [ "bar", "scatter" ]
							},
						    {
						    	"label": "Show:",
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
						.color("region")
						.draw();
				}
			}
		}
	}

	$("#refreshEpisodesButton").on("click", function() {
		var innerNode = document.getElementById("appendNodes");
		innerNode.innerHTML = "";
		$("#detailsNav li.active").removeClass("active");
		$("#act1").addClass("active");
		$.ajax({
		    url: "http://localhost:8080/api/ExecuteQuery",
		    type: "GET",
		    success: function (result) {
		    	arrayData = [];
			    if (arrayData.length == 0) {
			        for (var i = 0; i < result.length; i++) {
				        arrayData.push(result[i]);
				    }
				    var theType = $("#graphSelection").val();
		            createD3Viz(arrayData, theType);
			    }
			}
		});
	});

	$("#detailsNav").on("click", "li", function() {
    	$("#detailsNav li.active").removeClass("active");
    	$(this).addClass("active");
    	var episode = $(this).text().trim();
    	var theType = $("#graphSelection").val();
    	var innerNode = document.getElementById("appendNodes");
		innerNode.innerHTML = "";
		if (episode == "Act 1") {
			var act1GraphCounter = 0;
			for (var i = 0; i < arrayData.length; i++) {
				var act1Data2dArray = []; // data for act1 viz
				var tc = 0;
				if (arrayData[i].clientID == currentClient) {

					if (arrayData[i].act1Notes) {
						$(".inner").append("<br><br/><br><br/><p>" + arrayData[i].act1Notes + "</p><br/><br/>");
					}

					var act1PivotTable = arrayData[i].act1Data;
					if (act1PivotTable) {
						var newArrayData = [];
						act1GraphCounter++;
						var splitAct1Table = act1PivotTable.split("^");
						var region = "";
						for (let k = 0; k < splitAct1Table.length; k++) {
							if (k == 0) { // get the region or header or w/e
								region = splitAct1Table[k].replace(/\[|]|/g, "").trim(); // replace [ ] and , to get only the region
								var splitRegion = region.split(",");
								act1Data2dArray.push(new Array(splitRegion.length));
								for (let m = 0; m < splitRegion.length; m++) {
									act1Data2dArray[0][m] = splitRegion[m].trim();
								}
								//act1Data2dArray[0][0] = "y-column";
							} else { // get the data
								if (splitAct1Table[k] != "") { // if it's not empty
									var act1Data = splitAct1Table[k].replace(/\[|]/g, "").trim();
									var splitAct1Data = act1Data.split(",");
									//document.getElementById("originalActText").innerHTML += act1Data + "<br/>";
									act1Data2dArray.push(new Array(splitAct1Data.length));
									var splitDate = splitAct1Data[0].split("-");
									var theDate = "";
									if (splitDate.length > 1) {
										theDate = splitDate[1] + "-" + splitDate[0];
									} else {
										theDate = splitDate[0] + "";
									}
									for (let m = 1; m < splitAct1Data.length; m++) {
										tc++;
										if (parseFloat(splitAct1Data[m])) {
											act1Data2dArray[k][m] = splitAct1Data[m];
										} else {
											act1Data2dArray[k][m] = "-";
										}
										act1Data2dArray[k][0] = theDate;
										if (act1Data2dArray[k][m] != "-") {
											newArrayData.push({ year: theDate, value: parseFloat(act1Data2dArray[k][m]), region: act1Data2dArray[0][m] });
										}
									}
								}
							}
						}
						createTable(act1Data2dArray);

						var regions = [];
						let temp = {"All": "All"};
						regions.push(temp)
						for (let r = 0; r < newArrayData.length; r++) {
							if (newArrayData[r].region) {
								let temp = {};
								temp[newArrayData[r].region] = newArrayData[r].region;
								regions.push(temp);
							}
						}
						$(".inner").append("<div id=\"viz" + act1GraphCounter + "\"></div>");
						var act1Visualization = d3plus.viz()
							.container("#viz" + act1GraphCounter)
							.data(newArrayData)
							.title("Act1 chart for client " + currentClient)
							.type("bar")
							.id("region")
							.width(1000)
							.height(600)
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
							    	"label": "Show:",
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
		} else if (episode == "Act 2") {
			var act2GraphCounter = 0;
			for (var i = 0; i < arrayData.length; i++) {
				var act2Data2dArray = []; // data for act1 viz
				var act2DataArray = []; // data for act1 viz
				var act2RowsOrCols = "";
				if (arrayData[i].clientID == currentClient) {

					if (arrayData[i].act2Notes) {
						$(".inner").append("<br/><br/><p>" + arrayData[i].act2Notes + "</p><br/><br/>");
					}

					var act2PivotTable = arrayData[i].act2Data;
					if (act2PivotTable) {
						var newArrayData = [];
						act2GraphCounter++;
						var splitAct2Table = act2PivotTable.split("^");
						var region = "";
						//console.log(splitAct2Table);
						for (let k = 0; k < splitAct2Table.length; k++) {
							if (k == 0) { // get the region or header or w/e
								region = splitAct2Table[k].replace(/\[|]|/g, "").trim(); // replace [ ] and , to get only the region
								var splitRegion = region.split(",");
								act2Data2dArray.push(new Array(splitRegion.length));
								for (let m = 0; m < splitRegion.length; m++) {
									if (splitRegion[m].trim() != "") {
										act2Data2dArray[0][m] = splitRegion[m].trim();
									}
								}
								//act2Data2dArray[0][0] = "y-column";
							} else { // get the data
								if (splitAct2Table[k] != "") { // if it's not empty
									var act2Data = splitAct2Table[k].replace(/\[|]/g, "").trim();
									var splitAct2Data = act2Data.split(",");
									//document.getElementById("originalActText").innerHTML += act2Data + "<br/>";
									act2Data2dArray.push(new Array(splitAct2Data.length));
									var splitDate = splitAct2Data[0].split("-");
									var theDate = "";
									if (splitDate.length > 1) {
										theDate = splitDate[1] + "-" + splitDate[0];
									} else {
										theDate = splitDate[0] + "";
									}
									for (let m = 1; m < splitAct2Data.length; m++) {
										tc++;
										if (parseFloat(splitAct2Data[m])) {
											act2Data2dArray[k][m] = parseFloat(splitAct2Data[m]);
										} else {
											act2Data2dArray[k][m] = "-";
										}
										act2Data2dArray[k][0] = theDate;
										if (act2Data2dArray[k][m] != "-") {
											newArrayData.push({ year: theDate, value: act2Data2dArray[k][m], region: act2Data2dArray[0][m] });
										}
									}
								}
							}
						}
						//createTable(act2Data2dArray);

						var regions = [];
						let temp = {"All": "All"};
						regions.push(temp)
						for (let r = 0; r < newArrayData.length; r++) {
							if (newArrayData[r].region) {
								let temp = {};
								temp[newArrayData[r].region] = newArrayData[r].region;
								regions.push(temp);
							}
						}
						var test = act2PivotTable.split("^");
						//console.log(test);
						var dataArr = [];
						//console.log(test.length);
						for (let s = 0; s < test.length; s++) {
							test[s] = test[s].replace(/\[|]|/g, "").trim();
							var splitTest = test[s].split(",");
							//console.log(test[s]);
							//console.log(splitTest);
							var tmp = [];
							for (let d = 0; d < splitTest.length; d++) {
								if (splitTest[d] != "") {
									tmp.push(splitTest[d].trim());
									//console.log(splitTest[d]);
								}
							}
							if (tmp.length != 0) {
								dataArr.push(tmp);
							}
						}
						delete dataArr[0][1];
						filter_array(dataArr[0]);
						//console.log("length1: " + dataArr.length);
						//console.log("length2: " + dataArr[0].length);
						//console.log(dataArr.length);
						var vizData = [];
						var found = "";

						for (var s = 1; s < dataArr.length; s++) {
							if (dataArr[s][0].includes("-") || parseInt(dataArr[s][0])) {
								var confirmDate = dataArr[s][0].split("-");
								if (parseInt(confirmDate[0])) {
									found = "date";
								} else {
									found =  "region";
								}
							} else if (dataArr[s][0].toString()) {
								found = "region";
							}
							for (var f = 1; f < dataArr[0].length; f++) {
								if (parseFloat(dataArr[s][f])) {
									if (found == "date") {
										var exists = false;
										for (var find = 0; find < vizData.length; find++) {
											if ("value" in vizData[find]) {
												if (vizData[find].value == parseFloat(dataArr[s][f])) {
													exists = true;
												}
											}
										}
										if (!exists) {
											//console.log(dataArr[s][0]);
											vizData.push({ year: dataArr[s][0], value: parseFloat(dataArr[s][f]), region: dataArr[0][2] });
										}
									} else if (found == "region") {
										//console.log(dataArr[0][f+1]);
										var splitDate = dataArr[0][f+1].split("-");
										var theDate = "";
										if (splitDate.length > 1) {
											theDate = splitDate[1] + "-" + splitDate[0];
										} else {
											theDate = splitDate[0] + "";
										}
										vizData.push({ year: theDate,/*year: dataArr[0][f+1],*/ value: parseFloat(dataArr[s][f]), region: dataArr[s][0] });
									}
								}
							}
						}
						//console.log(vizData);
						createTable(dataArr);

						$(".inner").append("<div id=\"viz" + act2GraphCounter + "\"></div>");
						var act2Visualization = d3plus.viz()
							.container("#viz" + act2GraphCounter)
							.data(vizData)
							.title("Act2 chart for client " + currentClient)
							.type("bar")
							.id("region")
							.width(1000)
							.height(600)
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
							    	"label": "Show:",
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
								   	"value": [ "year", "region" ],
								   	"method": function(value, viz) {
								   		if (value == "year") {
								   			//viz.id({"value": "year"}).x({"value": "region"}).time("year").draw();
								   			viz.id({"value": "region"}).x({"value": "year"}).order({"sort": "asc", "value": "year"}).time(false).draw();
								   		} else if (value == "region") {
								   			//viz.id({"value": "region"}).x({"value": "year"}).order({"sort": "asc", "value": "year"}).time(false).draw();
								   			viz.id({"value": "year"}).x({"value": "region"}).time("year").draw();
								   		}
								   	}
								}
							])
							.draw();
					}
				}
			}
		} else if (episode == "Summary") {
			for (var i = 0; i < arrayData.length; i++) {
				if (arrayData[i].summaryNotes) {
					let summaryString = arrayData[i].summaryNotes;
					summaryString = summaryString.replace(/@@/g, "<br />- ");
					summaryString = summaryString.replace(/~~/g, "<br />- ");
					summaryString = summaryString.replace(/@/g, "<br />- ");
					summaryString = summaryString.replace(/##/g, "<br />- ");
					$(".inner").append("<br><br/><p>" + summaryString + "</p><br><br>");
				}
			}
		}
	});

});

function createTable(tableData) {
	// find max and min value
	var max = -9999999;
	var min = 9999999;
	for (var i = 1; i < tableData.length; i++) {
		for (var j = 1; j < tableData[i].length; j++) {
			if (parseFloat(tableData[i][j])) {
				if (parseFloat(tableData[i][j]) > max) {
					max = parseFloat(tableData[i][j]);
				} else if (parseFloat(tableData[i][j]) < min) {
					min = parseFloat(tableData[i][j]);
				}
			}
		}
	}
	// create <table> element and fill it with data
	var table = document.createElement('table');
	var tableBody = document.createElement('tbody');
	tableData.forEach(function(rowData) {
		var row = document.createElement('tr');
		rowData.forEach(function(cellData) {
			var cell = document.createElement('td');
			if (parseFloat(cellData)) {
				//console.log(cellData + ": " + max + ", " + min);
				if (parseFloat(cellData) == max) {
					cell.style.color = "red";
				} else if (parseFloat(cellData) == min) {
					cell.style.color = "blue";
				}
			}
			cell.appendChild(document.createTextNode(cellData));
			row.appendChild(cell);
		});
		tableBody.appendChild(row);
	});
	table.appendChild(tableBody);
	//document.body.appendChild(table);
	$(".inner").append(table);
	$(".inner").append("<br/>");
}

function filter_array(test_array) {
    var index = -1,
        arr_length = test_array ? test_array.length : 0,
        resIndex = -1,
        result = [];

    while (++index < arr_length) {
        var value = test_array[index];

        if (value) {
            result[++resIndex] = value;
        }
    }

    return result;
}