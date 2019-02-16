# CineCubes Web Client (Diploma thesis)
## Description
Web based client for CineCubes java application (https://github.com/DAINTINESS-Group/CinecubesPublic).
<br/>
<br/>
What is CineCubes? (from CineCubes page)
> CineCubes produce "movies" as answers to OLAP queries. The user submits a query over an underlying star schema. Taking this query as input, the system comes up with a set of queries complementing the information content of the original query, and executes them. Then, the system visualizes the query results and accompanies this presentation with a text commenting on the result highlights. Moreover, via a text-to-speech conversion the system automatically produces audio for the constructed text. Each combination of visualization, text and audio practically constitutes a cube movie, which is wrapped as a PowerPoint presentation and returned to the user.

## Instalation and Usage
1. Go to api folder and run npm install (must have node.js pre-installed).
2. Replace CineCubes src folder you downloaded from CineCubers github page with the one existing here in CineCubes/src
3. Have CineCubes server running from Cinecubes\src\mainengine\Server.java (check instructions in its github page for details to run).
4. Index page is at cinecubes-web-panel/index.html. Was tested using apache and mysql.

## Demo of the panel
Index page
![Alt text](https://github.com/sersdyh/Web-panel-for-CineCubes/blob/master/demo-images/index_page.png?raw=true "Title")
<br/>
A basic function of this panel is to let you check weather the server is up and running like this:
![Alt text](https://github.com/sersdyh/Web-panel-for-CineCubes/blob/master/demo-images/monitor_server_status.png?raw=true "Title")
Original Act of a cube query
![Alt text](https://github.com/sersdyh/Web-panel-for-CineCubes/blob/master/demo-images/original-act.png?raw=true "Title")
