var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');

router.use(bodyParser.json()); // support json encoded bodies
router.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

var queryinfo = [];

router.post('/api/ExecuteQuery', function(req, res) {

	// if no CORS allowed, request denied
	res.header("Access-Control-Allow-Origin", "http://localhost");
	res.header("Access-Control-Allow-Headers");

	queryinfo.push(req.body);

	// reply to java app all went well
	res.json({ "success": true, "message": "query info sent", "data sent:": queryinfo });
});

router.get('/api/ExecuteQuery', function(req, res) {

	// if no CORS allowed, request denied
	res.header("Access-Control-Allow-Origin", "http://localhost");
	res.header("Access-Control-Allow-Headers");

	// show client query info in http://localhost:8080/api/ExecuteQuery
	res.json(queryinfo);
});

module.exports = router;