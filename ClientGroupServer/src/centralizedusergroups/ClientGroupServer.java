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

   
    
    public Scanner scan;
    public String alias;
    public int puerto;
    public String IpServidor;
    public String hostname;
    
    static GroupServerInterface servidor;
    
    ReentrantLock cerrojo;
    
    public ClientGroupServer() throws UnknownHostException{
        super();
        
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
    
    public static void main(String[] args) throws UnknownHostException, MalformedURLException, NotBoundException{
        System.setProperty("java.rmi.server.hostname","192.168.0.26");
        System.setProperty("java.security.policy", "/home/usuario/Escritorio/PR4Distribuidos/ClientGroupServer/src/centralizedusergroups/client-policy");

        try{
            ClientGroupServer cliente = new ClientGroupServer();
           
            
            Registry reg = LocateRegistry.getRegistry(cliente.IpServidor,1099);
            servidor=(GroupServerInterface) reg.lookup("gabriel/GroupServer");
          
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
                System.out.println("11. Terminar Ejecución");
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
    public boolean createGroup(String galias, String oalias, String ohostname) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isGroup(String galias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeGroup(String galias, String oalias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addMember(String galias, String alias, String hostname) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeMember(String galias, String alias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isMember(String galias, String alias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String Owner(String galias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean StopMembers(String galias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean AllowMembers(String galias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListMembers(String galias) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinkedList<String> ListGroups() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
    

