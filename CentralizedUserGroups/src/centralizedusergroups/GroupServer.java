/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedusergroups;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author usuario
 */
public class GroupServer implements GroupServerInterface{
    
    private LinkedList<Group> listaGrupos;
    ReentrantLock cerrojo;

    @Override
    public boolean createGroup(String galias, String oalias, String ohostname) {
        if (this.isGroup(galias)){
            this.listaGrupos.add(new Group(galias,new GroupMember(oalias,ohostname)));
            return true;
        }else return false;
        
    }

    @Override
    public boolean isGroup(String galias) {
        for (Group grupo:this.listaGrupos){
            if (grupo.getNombreGrupo().equals(galias)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeGroup(String galias, String oalias) {
        for (int i = 0; i<this.listaGrupos.size(); i++){
            if (this.listaGrupos.get(i).nombreGrupo.equals(galias) && this.listaGrupos.get(i).propietario.nombre.equals(oalias)){
                this.listaGrupos.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addMember(String galias, String alias, String hostname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeMember(String galias, String alias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMember(String galias, String alias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String Owner(String galias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StopMembers(String galias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean AllowMembers(String galias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListMembers(String galias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListGroups() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

class Group{
    
    String nombreGrupo;
    
    GroupMember propietario;
    
    LinkedList<GroupMember> listaMiembros;
    
    boolean bol;
    
    Condition condition;
    
    public Group(String nombreGrupo, GroupMember groupMember){
        this.nombreGrupo = nombreGrupo;
        this.propietario = groupMember;
        this.listaMiembros.add(groupMember);
        bol = false;
    }
    
    public String getNombreGrupo(){
        return this.nombreGrupo;
    }
}
