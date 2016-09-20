/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Logica;

import Interfaces.Interface_servidor;
import Interfaces.Mensaje;
import Interfaces.Util;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.util.LinkedList;
import java.util.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author j
 */
public class Mensajes {
    private static int sesion=0;
    private static Interface_servidor servidor;
    
    
    public static void conectarse_al_servidor() {
        Util.setCodebase(Interface_servidor.class);
        try {
            Registry registro= LocateRegistry.getRegistry(8888);       
            try {
                servidor=(Interface_servidor) registro.lookup("servidor mensajeria");
            } catch (NotBoundException | AccessException ex) {
                Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void autenticar(String nombre) {
        try {
            sesion=servidor.autenticar(nombre);
        } catch (RemoteException ex) {
            Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
    public static int agregar_contacto(String contacto){
        try {
            int sesion_2=servidor.agregar(contacto, sesion);
            return sesion_2;
        } catch (RemoteException ex) {
            Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    public static void enviar_mensaje(String mensaje, int sesion_2) {
        try {
            servidor.enviar(mensaje, sesion_2,sesion);
        } catch (RemoteException ex) {
            Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String recibir_mensaje() {
        String mensaje="";
        try {
            List<Mensaje> mensajes=servidor.recibir(sesion);
            System.out.println("re:"+mensajes.get(0).getCuerpo()+" de: "+mensajes.get(0).getRemitente());
            for (int i=0;i<mensajes.size();i++) {
                mensaje+="De "+ mensajes.get(i).getRemitente()+": "+mensajes.get(i).getCuerpo()+"\n";
            }
            
            servidor.limpiar_buffer(sesion);
            
        } catch (RemoteException ex) {
            Logger.getLogger(Mensajes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensaje;
    }
}
