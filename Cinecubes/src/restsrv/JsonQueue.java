package restsrv;

import java.util.Queue;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.LinkedList;

public class JsonQueue {

	private static Queue<String> queue = new LinkedList<String>();

	public JsonQueue() {
	}

	public static Queue<String> getQueue() {
		return JsonQueue.queue;
	}

	public static boolean isEmpty() {
		return queue.size() == 0;
	}

	public static int getSize() {
		return JsonQueue.queue.size();
	}

	public static void addJsonObject(String jsonObject) {
		JsonQueue.queue.add(jsonObject);
		try {
			postRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String removeJsonObject() {
		String first = JsonQueue.queue.poll();
		return first;
	}

	public static void postRequest() throws MalformedURLException, ProtocolException, IOException {
		if (JsonQueue.isEmpty()) {
			return;
		}
		String first = removeJsonObject();
		ApiRequest cineApi = new ApiRequest("http://localhost:8080/api/ExecuteQuery");
		cineApi.postRequest(first);
	}

	public String toString() {
		String data = "";
		for (String item: JsonQueue.queue) {
			data += item;
		}
		return data;
	}
}
