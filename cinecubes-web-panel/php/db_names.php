<?php

/*$dbhost = "localhost";
$dbusername = "CinescubesUser";
$dbpassword = "Cinecubes";*/

$dbhost = "localhost";
$dbusername = "root";
$dbpassword = null;

$dbcnx = mysql_connect($dbhost, $dbusername, $dbpassword); 

$query = "show databases";
$result = @mysql_query($query);

while ($row = mysql_fetch_array($result)) { 
	//print_r($row[0] . " ");
	echo($row[0] . " ");
}
