package mainengine;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMainEngine extends Remote {

	public void initializeConnection(String schemaName, String login, String passwd,
			 String inputfile, String cubeName) throws RemoteException;
	
	public void answerCubeQueriesFromFile(File file) throws RemoteException;

	public void answerQueryFromString(String queryString) throws RemoteException;
	
	public void optionsChoice(boolean audio, boolean word) throws RemoteException;
}