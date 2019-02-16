var express = require('express');
var reload = require('reload');
var app = express();

app.set('port', process.env.PORT || 8080);

app.use(require('./routes/index'));
app.use(require('./routes/server'));
app.use(require('./routes/query'));
app.use(require('./routes/call_server'));

var server = app.listen(app.get('port'), function(){
	console.log('Listening on port ' + app.get('port'));
});

reload(app);