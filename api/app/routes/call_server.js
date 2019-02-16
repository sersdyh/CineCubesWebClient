var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var java = require('java');
var fs = require('fs');
const path = require('path');

java.classpath.push(path.resolve("."));
java.classpath.push('../bin/');
//java.classpath.push('../bin/mainengine/MainEngine.class');
//java.classpath.push('../bin/mainengine/IMainEngine.class');

var theClient = java.import('client.Client');

router.use(bodyParser.json()); // support json encoded bodies
router.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

router.post('/api/ExecuteServer', function(req, res) {

	// if no CORS allowed, request denied
	res.header("Access-Control-Allow-Origin", "http://localhost");
	res.header("Access-Control-Allow-Headers");

	// show client query info in http://localhost:8080/api/ExecuteQuery
	console.log(req.body);

	if (req.body.queryType == "stringQuery") {
		theClient.executeServerFromStringQuery(req.body.database, req.body.queryType, req.body.cubeName, req.body.name, req.body.AggrFunc, req.body.Measure, req.body.Gamma, req.body.Sigma);
	} else if (req.body.queryType == "fileQuery") {
		theClient.executeServerFromFile(req.body.database, req.body.queryType, req.body.fileList, req.body.cubeName);
	}

	res.json({ "success": true });
});

module.exports = router;