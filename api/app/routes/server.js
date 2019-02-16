var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

router.use(bodyParser.json()); // support json encoded bodies
router.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

var success = false;
var date = null;
var registered = null;
var port = null;
var serverinfo = null;

var status = null;
var server = null;
var shutdown = false;

router.post('/api/ServerStatus', function(req, res) {

	// if no CORS allowed, request denied
	res.header("Access-Control-Allow-Origin", "http://localhost:8080/api/ServerStatus");
	res.header("Access-Control-Allow-Headers");

	console.log(req.body);

	if (req.body.success == false || req.body.success == "false") {
		success = false;
	} else {
		success = true;
	}
	if (success == false) {
		status = req.body.status;
		server = req.body.server;
		shutdown = true;
		// reply to java app what's wrong
		res.json({ "success": success, "status": status, "server": server });
	} else {
		date = req.body.date;
		registered = req.body.registered;
		port = req.body.port;
		serverinfo = req.body.serverinfo;
		// reply to java app all went well
		res.json({ "success": success, "message": "server status sent" });
	}
});

router.get('/api/ServerStatus', function(req, res) {

	// if no CORS allowed, request denied
	res.header("Access-Control-Allow-Origin", "http://localhost");
	res.header("Access-Control-Allow-Headers");

	if (success == false || success == "false") {
		var err;
		if (shutdown == true) {
			err = "server shutdown from Cinecubes system";
			// reply to java app what's wrong
			res.json({ "success": success, "status": status, "server": server, "message": err });
		} else {
			err = "no post request found from Cinecubes";
			// reply to java app what's wrong
			res.json({ "success": success, "error message": err, "data": null });
		}
	} else {
		// reply to java app all went well
		res.json({ "success": success, "server": "running", "registered @": registered, "date and time server registered": date, "port": port, "server details": serverinfo });
	}
});

module.exports = router;