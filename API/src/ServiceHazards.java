package Services;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface ServiceHazards extends Remote {
    String getHazards() throws RemoteException, IOException, InterruptedException;
    String ping() throws RemoteException, ServerNotActiveException;
}