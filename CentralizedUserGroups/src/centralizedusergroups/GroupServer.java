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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario
 */
public class GroupServer implements GroupServerInterface{
    
    private HashMap<String,Group> listaGrupos;
    private HashMap<String,Condition> condiciones;
    private HashMap<String,Boolean> booleanos; 
    
    ReentrantLock cerrojo;
    
    public GroupServer(){
        super();
        this.cerrojo = new ReentrantLock(true);
        this.listaGrupos = new HashMap();
    }

    @Override
    public boolean createGroup(String galias, String oalias, String ohostname) {
        
        if (this.isGroup(galias)){
            this.cerrojo.lock();
            this.listaGrupos.put(galias,new Group(galias,new GroupMember(oalias,ohostname)));
            this.condiciones.put(galias,this.cerrojo.newCondition());
            this.booleanos.put(galias,false);
            this.cerrojo.unlock();
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
        
        if(this.isGroup(galias) && this.listaGrupos.get(galias).propietario.equals(oalias)){//Esto funciona porque java cortocicuita la condicion
            this.cerrojo.lock();
            this.listaGrupos.remove(galias);
            this.cerrojo.unlock();
            return true;
        }else return false;
        
            
        
        
    }

    @Override
    public boolean addMember(String galias, String alias, String hostname) {
        
        
        if(this.isGroup(galias) && this.isMember(galias, alias)){
            this.cerrojo.lock();
            this.listaGrupos.get(galias).listaMiembros.add(new GroupMember(alias,hostname));
            this.cerrojo.unlock();
            return true;
        }else return false;
           
    }

    @Override
    public boolean removeMember(String galias, String alias) {
        if(this.booleanos.get(galias)){
            try {
                this.condiciones.get(galias).await();//Si los miembros del grupo estan parados me espero a que vuelvan a estar permitidos
            } catch (InterruptedException ex) {
                System.out.println("Ha ocurrido una excepcion en el await de removeMember");
            }
        }
        
        if(this.isMember(galias,alias)){
            return this.listaGrupos.get(galias).removeMember(alias);
        }else{
            return false;
        }    
        
    }

    @Override
    public boolean isMember(String galias, String alias) {
        this.cerrojo.lock();
        try{
            boolean devolver = false;
            if(this.isGroup(galias)){
            for (GroupMember miembro :this.listaGrupos.get(galias).listaMiembros){
                if (miembro.nombre.equals(alias)){
                    devolver = true;
                }
            }
            }
        return devolver;
        }finally{
            this.cerrojo.unlock();
        }
        
    }

    @Override
    public String Owner(String galias) {
        this.cerrojo.lock();
        try{
            if (this.isGroup(galias)){
            return this.listaGrupos.get(galias).propietario.nombre;
        }else return null;
        }finally{
            this.cerrojo.unlock();
        }
        
    }

    @Override
    public boolean StopMembers(String galias) {
        if(this.isGroup(galias)){
            this.cerrojo.lock();
            this.booleanos.put(galias,true);//cambio el valor del booleano del grupo
            this.cerrojo.unlock();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean AllowMembers(String galias) {
        if(this.isGroup(galias)){
            this.cerrojo.lock();
            this.booleanos.put(galias,false);//cambio el valor del booleano del grupo
            this.condiciones.get(galias).signalAll();
            this.cerrojo.unlock();
            return true;
        }else{
            return false;
        }
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
    
    
    public Group(String nombreGrupo, GroupMember groupMember){
        this.nombreGrupo = nombreGrupo;
        this.propietario = groupMember;
        this.listaMiembros.add(groupMember);
    }
    
    public String getNombreGrupo(){
        return this.nombreGrupo;
    }
    
    //Funcion para eliminar un miembro con alias
    public boolean removeMember(String alias){
        if (this.propietario.nombre.equals(alias)){ // Comprobamos que no es el propietario
            return false;
        }else{
            for(GroupMember miembro:listaMiembros){
                if(miembro.nombre.equals(alias)){
                    this.listaMiembros.remove(miembro);
                    return true;
                }
            }
        }
    }
}
