/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedusergroups;

import java.util.LinkedList;

/**
 *
 * @author usuario
 */
public interface GroupServerInterface {
    
    boolean createGroup(String galias, String oalias, String ohostname);
    
    boolean isGroup(String galias);
    
    boolean removeGroup(String galias, String oalias);
    
    boolean addMember(String galias, String alias, String hostname);
    
    boolean removeMember(String galias, String alias);
    
    boolean isMember(String galias, String alias);
    
    String Owner(String galias);
    
    boolean StopMembers(String galias);   
    
    boolean AllowMembers(String galias);
    
    LinkedList<String> ListMembers(String galias);
    
    LinkedList<String> ListGroups();
}
