/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedusergroups;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author usuario
 */
public interface GroupServerInterface extends Remote{
    
    boolean createGroup(String galias, String oalias, String ohostname) throws RemoteException;
    
    boolean isGroup(String galias)throws RemoteException;
    
    boolean removeGroup(String galias, String oalias)throws RemoteException;
    
    boolean addMember(String galias, String alias, String hostname)throws RemoteException;
    
    boolean removeMember(String galias, String alias)throws RemoteException;
    
    boolean isMember(String galias, String alias)throws RemoteException;
    
    String Owner(String galias)throws RemoteException;
    
    boolean StopMembers(String galias)throws RemoteException;   
    
    boolean AllowMembers(String galias)throws RemoteException;
    
    LinkedList<String> ListMembers(String galias)throws RemoteException;
    
    LinkedList<String> ListGroups()throws RemoteException;
}
