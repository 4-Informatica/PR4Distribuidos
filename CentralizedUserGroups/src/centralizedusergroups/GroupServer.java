/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedusergroups;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author usuario
 */
public class GroupServer implements GroupServerInterface{
    
    private HashMap<String,Group> listaGrupos;
    ReentrantLock cerrojo;

    @Override
    public boolean createGroup(String galias, String oalias, String ohostname) {
        if (this.isGroup(galias)){
            this.listaGrupos.put(galias,new Group(galias,new GroupMember(oalias,ohostname)));
            return true;
        }else return false;
        
    }

    @Override
    public boolean isGroup(String galias) {
        if (this.listaGrupos.containsKey(galias)){
            return true;
        }else return false;
    }

    @Override
    public boolean removeGroup(String galias, String oalias) {
        this.cerrojo.lock();
        try{
        if(this.isGroup(galias) && this.listaGrupos.get(galias).propietario.equals(oalias)){//Esto funciona porque java cortocicuita la condicion
            this.listaGrupos.remove(galias);
            return true;
        }else return false;
        }finally{
            this.cerrojo.unlock();
        }
        
    }

    @Override
    public boolean addMember(String galias, String alias, String hostname) {
        this.cerrojo.lock();
        try{
            if(this.isGroup(galias) && this.isMember(galias, alias)){
                this.listaGrupos.get(galias).listaMiembros.add(new GroupMember(alias,hostname));
                return true;
            }else return false;
        }finally{
            this.cerrojo.unlock();
        }     
    }

    @Override
    public boolean removeMember(String galias, String alias) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMember(String galias, String alias) {
        boolean devolver = false;
        if(this.isGroup(galias)){
            for (GroupMember miembro :this.listaGrupos.get(galias).listaMiembros){
                if (miembro.nombre.equals(alias)){
                    devolver = true;
                }
            }
        }
        return devolver;
    }

    @Override
    public String Owner(String galias) {
        if (this.isGroup(galias)){
            return this.listaGrupos.get(galias).propietario.nombre;
        }else return null;
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
        LinkedList<String> devolver = new LinkedList<>();
        Group grupo = this.listaGrupos.get(galias);
        for (GroupMember miembro:grupo.listaMiembros){
            devolver.add(miembro.nombre);
        }
        return devolver;
    }

    @Override
    public LinkedList<String> ListGroups() {
        LinkedList<String> devolver = (LinkedList)this.listaGrupos.keySet();
        return devolver;
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
