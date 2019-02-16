$(document).ready(function() {

    $("#statusButton").click(function() {
        $.get("http://localhost:8080/api/ServerStatus", function(data, status) {
            var str = "";
            data.success = data.success == true;
            if(data.success == true) {
                str += "<font color=#008000>";
                $.each(data, function(key, value) {
                    //console.log(key + ": " + value); // testing
                    str += key + ": " + value + "<br>";
                });
                str += "</font>";
            } else if (data.success == false) {
                str += "<font color=#ff0807>";
                $.each(data, function(key, value) {
                    //console.log(key + ": " + value); // testing
                    str += key + ": " + value + "<br>";
                });
                str += "</font>";
            } else {
                str = "<font color=#ff0807>error: unknown error</font>";
            }
            document.getElementById("panel-content").innerHTML = str;
        }).fail(function(xhr, status, error) {
            var err = "<font color=#ff0807>GET http://localhost:8080/api/ServerStatus<br>A GET error occured: error(fail)</br>net::ERR_CONNECTION_REFUSED<br>*** Make sure Rest API is up and running ***</br></font>";
            document.getElementById("panel-content").innerHTML = err;
        });
    });

});