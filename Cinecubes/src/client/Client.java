package client;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import mainengine.IMainEngine;

public class Client {

	// Host or IP of Server
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;

	public static void executeServerFromStringQuery(String database, String queryType, String cubeName, String name, String AggrFunc, String measure, String Gamma, String Sigma) throws Exception {
		registry = LocateRegistry.getRegistry(HOST, PORT);
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class
				.getSimpleName());
		service.optionsChoice(false, true);
		service.initializeConnection(database, "CinecubesUser",	"Cinecubes", database, cubeName);

		if (queryType.equals("stringQuery")) {
			String stringQuery = "CubeName:" + cubeName + " " + "\n" +
								 "Name:" + name + " " + "\n" +
								 "AggrFunc:" + AggrFunc + " " + "\n" +
								 "Measure:" + measure + " " + "\n" +
								 "Gamma:" + Gamma + " " + "\n" +
								 "Sigma:" + Sigma + " " + "\n" +
								 "";
			service.answerQueryFromString(stringQuery);
		}
	}

	public static void executeServerFromFile(String database, String queryType, String fileList, String cubeName) throws Exception {
		registry = LocateRegistry.getRegistry(HOST, PORT);
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class
				.getSimpleName());
		service.optionsChoice(false, true);
		service.initializeConnection(database, "CinecubesUser",	"Cinecubes", database, cubeName);

		if (queryType.equals("fileQuery")) {
			File temp = new File(fileList.trim());
			service.answerCubeQueriesFromFile(temp);
		}
	}

}
