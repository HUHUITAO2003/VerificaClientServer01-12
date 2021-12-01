package it.hu;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    ServerSocket server;// porta
    Socket client;// socket su cui ci andremo a collegare
    String stringaRicevuta; // stringa ricevuta dal client
    String stringaModificata;// stringa di risposta
    BufferedReader inDalClient;// lettura stream dal client
    DataOutputStream outVersoClient;// output stream verso client

    /**
   * metodo per effettuare  la connessione tra client server
   */
    public Socket attendi() { // attesa del collegamento di un client
        try {
            server = new ServerSocket(6789);// istanza della porta
            client = server.accept();// accettazione del collegamento con il client
            server.close();// chiusura della porta
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));// lettura dello stream dal client
            outVersoClient = new DataOutputStream(client.getOutputStream());// invio dello stream verso il client
        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server!");
            System.exit(1);

        }
        return client;
    }

    /**
   * metodo per effettuare lo scambio di messaggi tramite client server
   */
    public void comunica() {// comunicazione con il client
        try {
            outVersoClient.writeBytes("S -> Connessione effettuata" + '\n');
            System.out.println("Connessione effettuata");
            for (;;) {
                outVersoClient.writeBytes("S -> Indovina il numero:" + '\n');
                int numero = new java.util.Random().nextInt(100) + 1;//generazione numero randomico tra 1 e 100
                for (;;) {
                    stringaRicevuta = inDalClient.readLine();// lettura tentativo proveniente dal clientt
                    if (Integer.parseInt(stringaRicevuta) == numero) {//controllo se il tentativo è uguale al numero generato e relativi indizi
                        outVersoClient.writeBytes("S -> Indovinato!!!" + '\n');
                        break;
                    } else {
                        if (Integer.parseInt(stringaRicevuta) > numero) {
                            outVersoClient.writeBytes("S -> Troppo grande" + '\n');
                        }
                        if (Integer.parseInt(stringaRicevuta) < numero) {
                            outVersoClient.writeBytes("S -> Troppo piccolo" + '\n');
                        }
                    }
                }
                if(inDalClient.readLine().equals("N")){
                    System.out.println("Disconnessione effettuata");
                    inDalClient.close();
                    outVersoClient.close();
                    client.close();
                    break;
                }
            }
        } catch (Exception e) {

        }

    }
}
