package tbs.server;

public class TBSExceptions extends Exception {

	//This is an Exception object that allows clear visibility of possible exceptions thrown in the TBSServerImpl class
	public TBSExceptions(String message){
		super (message);
	}
	
	public TBSExceptions() {
	}
}
