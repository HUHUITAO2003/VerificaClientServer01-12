/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.hu;

/**
 *
 * @author juliet
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server server = new Server();//istanza server
        server.attendi();//avvio server mettendosi in ascolto della richiesta di un client
        server.comunica();//comunicazione con il client
    }
    
}
