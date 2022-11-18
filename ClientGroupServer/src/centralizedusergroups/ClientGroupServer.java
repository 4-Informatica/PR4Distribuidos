/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package centralizedusergroups;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author usuario
 */
public class ClientGroupServer implements GroupServerInterface {

    private HashMap<String,Group> listaGrupos;
    private HashMap<String,Condition> condiciones;
    private HashMap<String,Boolean> booleanos; 
    
    public Scanner scan;
    public String alias;
    public int puerto;
    public String IpServidor;
    public String hostname;
    
    static GroupServerInterface servidor;
    
    ReentrantLock cerrojo;
    
    public ClientGroupServer() throws UnknownHostException{
        super();
        this.condiciones = new HashMap();
        this.booleanos = new HashMap();
        this.cerrojo = new ReentrantLock(true);
        this.listaGrupos = new HashMap();
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        this.hostname = InetAddress.getLocalHost().getHostName();
        System.out.println("Intoduce el alias del cliente");
        scan = new Scanner(System.in);
        this.alias = scan.next();
        System.out.println("Intoduce el puerto del cliente");
        scan = new Scanner(System.in);
        this.puerto= scan.nextInt();
        System.out.println("Intoduce la ip del servidor");
        scan = new Scanner(System.in);
        this.IpServidor = scan.next();
        
    }
    
    public static void main(String[] args) throws UnknownHostException, MalformedURLException{
        System.setProperty("java.security.policy", "/home/usuario/Escritorio/PR4Distribuidos/ClientGroupServer/src/centralizedusergroups/client-policy");

        try{
            ClientGroupServer cliente = new ClientGroupServer();
           
            
            Registry reg = LocateRegistry.getRegistry(cliente.IpServidor,1099);
            servidor=(GroupServerInterface) reg.lookup("GroupServer");
          
            System.out.println("Cliente conectado desde: " + InetAddress.getLocalHost().getHostAddress() + ":" + cliente.puerto);
        
            boolean terminar = true; // variable para salir del bucle
            while(terminar){
                System.out.println("1. Crear grupo");
                System.out.println("2. Eliminar grupo");
                System.out.println("3. Añadirse como miembro");
                System.out.println("4. Eliminarse como miembro");
                System.out.println("5. Bloquear altas y bajas");
                System.out.println("6. Desbloquear altas y bajas");
                System.out.println("7. Mostrar miembros de un grupo");
                System.out.println("8. Mostrar lista de grupos");
                System.out.println("9. Mostrar propietario de un grupo");
                System.out.println("10. Comprobar si es miembro de un grupo");
                System.out.println("11. Terminar Ejeución");
                int s=cliente.scan.nextInt();


                switch (s) {
                    case 1://1. Crear grupo
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        String grupo = cliente.scan.next();

                        servidor.createGroup(grupo,cliente.alias,cliente.hostname);
                        break;

                    case 2://2. Eliminar grupo
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.removeGroup(grupo,cliente.alias));
                        break;

                    case 3://3. Añadir miembro
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.addMember(grupo, cliente.alias, cliente.hostname));
                        break;

                    case 4://4. Eliminar miembro
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.removeMember(grupo,cliente.alias));
                        break;

                    case 5://5. Bloquear altas y bajas             
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.StopMembers(grupo));
                        break;

                    case 6: //6. Desbloquear altas y bajas
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.AllowMembers(grupo));
                        break;

                    case 7://7. Mostrar miembros de un grupo
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.ListMembers(grupo));
                        break;

                    case 8://8. Mostrar lista de grupos
                        System.out.println(servidor.ListGroups());
                        break;

                    case 9://9. Mostrar propietario de un grupo
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.Owner(grupo));
                        break;
                    case 10://10. Comprobar si es miembro de un grupo
                        cliente.scan = new Scanner(System.in);
                        System.out.println("Introduce el nombre del grupo");
                        grupo = cliente.scan.next();
                        System.out.println(servidor.isMember(grupo, cliente.alias));
                        break;
                    case 11://11.Terminar Ejecucion
                        terminar = false;
                        break;
                    default:
                        System.out.println("Esta entrada no es válida, por favor introduce otra");
                }  
            }
            }catch(RemoteException ex){
                System.out.println(ex.getMessage());
            }catch(NotBoundException e){
                System.out.println(e.getMessage());
            }
        
    
    }

    @Override
    public boolean createGroup(String galias, String oalias, String ohostname) throws RemoteException{
        
        if (!this.isGroup(galias)){
            this.cerrojo.lock();
            this.listaGrupos.put(galias,new Group(galias,new GroupMember(oalias,ohostname)));
            this.condiciones.put(galias,this.cerrojo.newCondition());
            this.booleanos.put(galias,false);
            this.cerrojo.unlock();
            return true;
        }else return false;
        
        
    }

    @Override
    public boolean isGroup(String galias) throws RemoteException{
        
        if (this.listaGrupos.containsKey(galias)){
        return true;
        }else return false;
              
        
    }

    @Override
    public boolean removeGroup(String galias, String oalias) throws RemoteException {
        
        if(this.isGroup(galias) && this.listaGrupos.get(galias).propietario.equals(oalias)){//Esto funciona porque java cortocicuita la condicion
            this.cerrojo.lock();
            this.listaGrupos.remove(galias);
            this.cerrojo.unlock();
            return true;
        }else return false;
        
            
        
        
    }

    @Override
    public boolean addMember(String galias, String alias, String hostname) throws RemoteException {
        
        
        if(this.isGroup(galias) && this.isMember(galias, alias)){
            this.cerrojo.lock();
            this.listaGrupos.get(galias).listaMiembros.add(new GroupMember(alias,hostname));
            this.cerrojo.unlock();
            return true;
        }else return false;
           
    }

    @Override
    public boolean removeMember(String galias, String alias) throws RemoteException {
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
    public boolean isMember(String galias, String alias) throws RemoteException {
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
    public String Owner(String galias) throws RemoteException {
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
    public boolean StopMembers(String galias) throws RemoteException {
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
    public boolean AllowMembers(String galias) throws RemoteException {
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
        LinkedList devolver = new LinkedList();
        for (String key:this.listaGrupos.keySet()){
            devolver.add(key);
        }
            
        
        return devolver;
    }
    
}
class Group{
    
    String nombreGrupo;
    
    GroupMember propietario;
    
    LinkedList<GroupMember> listaMiembros;
    
    
    public Group(String nombreGrupo, GroupMember groupMember){
        this.listaMiembros = new LinkedList();
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
        return false;
    }
}
    

