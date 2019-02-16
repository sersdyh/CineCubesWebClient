package mainengine;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import mainengine.MainEngine;
import mainengine.IMainEngine;

import restsrv.ApiRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
	private static final int PORT = 2020;
	private static Registry registry;

	public static void startRegistry() throws RemoteException {
		// Create server registry
		registry = LocateRegistry.createRegistry(PORT);
	}

	public static void registerObject(String name, Remote remoteObj)
			throws RemoteException, AlreadyBoundException {
		// Bind the object in the registry.  It is bind with certain name.
		// Client will lookup on the registration of the name to get object.
		registry.bind(name, remoteObj);
		System.out.println("Registered: " + name + " -> "
				+ remoteObj.getClass().getName() + "[" + remoteObj + "]");
	}

	public static void main(String[] args) throws Exception {

		System.out.println("Server starting...");
		startRegistry();
		registerObject(IMainEngine.class.getSimpleName(), new MainEngine());
		long timeServerStarted = System.currentTimeMillis();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now)); // date/time server started

		// Server was the start, and was listening to the request from the
		System.out.println("Server started!");

		ApiRequest cineApi = new ApiRequest("http://localhost:8080/api/ServerStatus");
		Remote remObj = registry.lookup("IMainEngine");
		String serverInfoPostReq = "success=" + true + "&date=" + dtf.format(now) + "&registered=" + registry.list()[0] + "&port=" + PORT + "&serverinfo=" + remObj.toString();
		System.out.println(serverInfoPostReq);
		cineApi.postRequest(serverInfoPostReq);

		// catch server forced shutdown
		/*Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Running Shutdown Hook");
				long timeServerStopped = System.currentTimeMillis();
				long diffInStartStop = (long) ((timeServerStopped-timeServerStarted)*0.001); // convert to sec
				String postString = "";
				postString = "success=" + false + "&status=" + "forced shutdown" + "&server=" + "stopped after " + diffInStartStop + " sec";
				try {
					cineApi.postRequest(postString);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});*/

		int sec = 4000;
		TimeUnit.SECONDS.sleep(sec);
		serverInfoPostReq = "success=" + false + "&status=" + "shutdown" + "&server=" + "stopped after " + sec + " sec";
		cineApi.postRequest(serverInfoPostReq);
		System.out.println("Server stopped!");
		System.exit(0);
	}
}
