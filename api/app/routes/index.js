var express = require('express');
var router = express.Router();

router.get('/api/', function(req, res) {
	res.send(`
		<h1>Default test page for Cinecubes Rest Api</h1>
	`);
});

module.exports = router;