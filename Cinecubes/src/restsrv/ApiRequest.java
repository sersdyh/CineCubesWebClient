package restsrv;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiRequest {

	//private String urlParameters;
	private String url;

	public ApiRequest(String url) {
    	this.url = url; // e.g. "http://localhost:8080/api/ServerStatus" for server status
	}

	public void postRequest(String parameters) throws MalformedURLException, ProtocolException, IOException {

        String urlParameters = parameters;
        HttpURLConnection postConnection = null;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            URL myPostServerStatusUrl = new URL(url);

            postConnection = (HttpURLConnection) myPostServerStatusUrl.openConnection();

            postConnection.setDoOutput(true);
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("User-Agent", "Java client");
            postConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream writePostData = new DataOutputStream(postConnection.getOutputStream())) {
            	writePostData.write(postData);
            	writePostData.flush();
            	writePostData.close();
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
                in.close();
            }
            System.out.print(postConnection.getURL() + " reply: " + content.toString());
        } finally {
        	if (postConnection != null) {
        		postConnection.disconnect();
        	}
        }
	}

}
