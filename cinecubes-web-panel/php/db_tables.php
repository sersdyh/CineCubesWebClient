<?php

$dbhost = "localhost";
$dbusername = "root";
$dbpassword = null;

$dbcnx = mysql_connect($dbhost, $dbusername, $dbpassword); 

//$dbname = $_GET["table"];
$theTable = $_GET['theTable'];
$query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='{$theTable}'";
$result = @mysql_query($query);

while ($row = mysql_fetch_array($result)) { 
	echo($row[0] . " ");
}
