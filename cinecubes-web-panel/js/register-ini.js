$(document).ready(function() {

    // if user presses reset button, clear all data dialog shows
    $("#resetButtonConfirm").on("click", function(event) {

        // first delete textarea content
        document.getElementById("cname").value = "";
        document.getElementById("name").value = "";
        document.getElementById("measure").value = "";
        document.getElementById("gamma").value = "";
        document.getElementById("sigma").value = "";
        document.getElementById("aggrFunc").value = "--";

        // then reset cube .ini file preview pane
        document.getElementById("panel-content").innerHTML = "This panel is a preview for the cube .ini file.<br></br>";

        var jsonObject = {};

        jsonObject["CubeName"] = "";
        jsonObject["Name"] = "";
        jsonObject["Measure"] = "";
        jsonObject["Gamma"] = "";
        jsonObject["Sigma"] = "";
        jsonObject["AggrFunc"] = "";

        for (var key in jsonObject) {
            document.getElementById("panel-content").innerHTML += key + "<font color=#ff0000>:</font><br>";
        }
    });

    // transfer textarea content to cube preview panel
    var oldVal = "";
    var jsonObject = {};

    jsonObject["CubeName"] = document.getElementById("cname").value;
    jsonObject["Name"] = document.getElementById("name").value;
    jsonObject["Measure"] = document.getElementById("measure").value;
    jsonObject["Gamma"] = document.getElementById("gamma").value;
    jsonObject["Sigma"] = document.getElementById("sigma").value;
    jsonObject["AggrFunc"] = document.getElementById("aggrFunc").value;

    for (var key in jsonObject) {
        document.getElementById("panel-content").innerHTML += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
    }

    $("#cname").on("change keyup paste", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;

        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    $("#name").on("change keyup paste", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;
        
        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    $("#measure").on("change keyup paste", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;
        
        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    $("#gamma").on("change keyup paste", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;
        
        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    $("#sigma").on("change keyup paste", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;
        
        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    $("#aggrFunc").on("change", function() {

        var currentVal = $(this).val();
        if (currentVal == oldVal) {
            return; // check to prevent multiple simultaneous triggers
        }

        var cname = document.getElementById("cname");
        var name = document.getElementById("name");
        var measure = document.getElementById("measure");
        var gamma = document.getElementById("gamma");
        var sigma = document.getElementById("sigma");
        var aggrFunc = document.getElementById("aggrFunc");
        var panel = document.getElementById("panel-content");

        oldVal = currentVal;

        jsonObject["CubeName"] = cname.value;
        jsonObject["Name"] = name.value;
        jsonObject["Measure"] = measure.value;
        jsonObject["Gamma"] = gamma.value;
        jsonObject["Sigma"] = sigma.value;
        jsonObject["AggrFunc"] = aggrFunc.value;
        
        var lines = panel.innerHTML.split("<br>");
        var data = "";

        for (var i = 0; i < lines.length; i++) {
            if (i > lines.length-8) {
                for (var key in jsonObject) {
                    data += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
                }
                break;
            } else {
                data += lines[i];
                data += "<br>";
            }
        }
        document.getElementById("panel-content").innerHTML = data;
    });

    // if the user presses + Cube button
    $("#cubeAddButton").on("click", function(event) {

        document.getElementById("cname").value = "";
        document.getElementById("name").value = "";
        document.getElementById("measure").value = "";
        document.getElementById("gamma").value = "";
        document.getElementById("sigma").value = "";
        document.getElementById("aggrFunc").value = "--";

        var jsonObject = {};

        jsonObject["CubeName"] = "";
        jsonObject["Name"] = "";
        jsonObject["Measure"] = "";
        jsonObject["Gamma"] = "";
        jsonObject["Sigma"] = "";
        jsonObject["AggrFunc"] = "";

        document.getElementById("panel-content").innerHTML += "<font color=#ff0000>@</font><br>";

        for (var key in jsonObject) {
            document.getElementById("panel-content").innerHTML += key + "<font color=#ff0000>:</font>" + jsonObject[key] + "<br>";
        }

    });

    // if the user presses the - Cube button
    $("#cubeRemoveButtonConfirm").on("click", function(event) {

        // if nothing to delete, do nothing
        var div = document.getElementById("panel-content");
        var nodelist = div.getElementsByTagName("br");

        if (nodelist.length == 8) { // .ini file must have at least one cube
            alert("Error: .ini file must have at least one cube.");
            return;
        }

        var panel = document.getElementById("panel-content");
        var lines = panel.innerHTML.split("<br>");
        lines.pop(); lines.pop(); lines.pop();
        lines.pop(); lines.pop(); lines.pop();
        lines.pop(); lines.pop();
        panel.innerHTML = "";
        for (var i = 0; i < lines.length; i++) {
            panel.innerHTML += lines[i];
            panel.innerHTML += "<br>";
        }
        // fill text area with old content
        var cname = lines[lines.length-6];
        cname = cname
            .replace("CubeName", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        var name = lines[lines.length-5];
        name = name
            .replace("Name", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        var measure = lines[lines.length-4];
        measure = measure
            .replace("Measure", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        var gamma = lines[lines.length-3];
        gamma = gamma
            .replace("Gamma", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        var sigma = lines[lines.length-2];
        sigma = sigma
            .replace("Sigma", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        var aggrFunc = lines[lines.length-1];
        aggrFunc = aggrFunc
            .replace("AggrFunc", "")
            .replace(/<font color="#ff0000">:/, "")
            .replace(/<\/font>/, "");
        document.getElementById("cname").value = cname;
        document.getElementById("name").value = name;
        document.getElementById("measure").value = measure;
        document.getElementById("gamma").value = gamma;
        document.getElementById("sigma").value = sigma
        document.getElementById("aggrFunc").value = aggrFunc;
    });

    // if the user presses the download button
    $("#downloadButton").on("click", function(event) {
        var panel = document.getElementById("panel-content");

        // remove html tags from data
        var data = panel.innerHTML.toString();

        data = data.replace(/<br>/g, "\n");
        data = data.replace(/<font color="#ff0000">/g, "");
        data = data.replace(/<\/font>/g, "");
        data = data.replace("This panel is a preview for the cube .ini file.", "");
        data = data.trim();

        //console.log(data); // testing

        var file = new Blob([data], { type: "text/plain;charset=utf-8" });
        if (window.navigator.msSaveOrOpenBlob) { // IE10+
            window.navigator.msSaveOrOpenBlob(file, "cube.ini");
        } else { // other browsers
            var a = document.createElement("a"),
            url = URL.createObjectURL(file);
            a.href = url;
            a.download = "cube.ini";
            a.click();
        }
    });
});