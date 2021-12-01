package it.hu;

import java.io.*;
import java.net.*;

public class Client {
    String nomeServer = "localhost"; // indirizzo del server su cui ci vogliamo connettere
    int portaServer = 6789;// porta su cui ci vogliamo collegare
    Socket miosocket;// canale di comunicazione tra clint e sarver
    BufferedReader tastiera;// buffered per memorizzare la stringa ottenuta da tastiera
    String stringaUtente;// stringa inserita dal client
    String stringaRicevutaDalServer;// stringa ricevuta dal server
    DataOutputStream outVersoServer;// stream output
    BufferedReader inDalServer;// stream input
    Boolean controllo = true;//controllo se numero inseritop è corretto
    int tentativi;//numero tentativi


    /**
   * metodo per effettuare  la connessione tra client server
   */

    public Socket connetti() {// metodo per connetterci al server
        System.out.println("CLIENT in esecuzione ...");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));// input tastiera
            miosocket = new Socket(nomeServer, portaServer);// creazione socket con indirizzo e porta

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());// istanza per output verso al server
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));// istanza per input dal
                                                                                                // server

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione");
            System.exit(1);

        }
        return miosocket;
    }

    /**
   * metodo per effettuare lo scambio dei messaggi tra client server
   */
    public void comunica() {// comunicazione con il server
        try {// leggo una riga
             // S-> connessione effettuata
            System.out.println(inDalServer.readLine());
            for (;;) {
                //regole del gioco
                System.out.println("Gioco dell'indovina numero!!!" + '\n'
                        + "il SERVER genererà un numero casuale e l'utente dovrà indovinare il numero tramite indizi forniti dal SERVER");
                // S -> Indovina il numero:
                stringaRicevutaDalServer = inDalServer.readLine();
                System.out.println(stringaRicevutaDalServer);

                for (;;) {
                    do {
                        stringaUtente = tastiera.readLine();
                        try {//controllo se il valore inserito è un int
                            int i = Integer.parseInt(stringaUtente);
                            if (i < 101 && i > 0) {
                                controllo = true;
                            } else {
                                System.out.println("S -> inserisci un numero tra 1 e 100");
                                controllo = false;
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println("S -> non hai inserito un numero intero");
                            controllo = false;
                        }
                    } while (!controllo);

                    outVersoServer.writeBytes(stringaUtente + '\n');// invio del numero indovinato
                    tentativi++;
                    stringaRicevutaDalServer = inDalServer.readLine();
                    System.out.println(stringaRicevutaDalServer);
                    if (stringaRicevutaDalServer.equals("S -> Indovinato!!!")) {//richiesta se si vuole continuare il gioco dopo aver vinto
                        System.out.println("S -> hai indovinato il numero in " + tentativi + " tentativi");
                        tentativi = 0;
                        System.out.println("S -> vuoi ricominciare il gioco? (Y/N)");
                        break;
                    }
                }
                do {//controllo sulla scelta inserita
                    stringaUtente = tastiera.readLine();
                    if (!stringaUtente.equals("Y") && !stringaUtente.equals("N")) {
                        System.out.println("S -> inserisci Y o N");
                    }
                } while (!stringaUtente.equals("Y") && !stringaUtente.equals("N"));
                if (stringaUtente.equals("N")) {//chiusura stream e socket se utente sceglie N
                    System.out.println("S -> gioco in chiusura...");
                    inDalServer.close();
                    outVersoServer.close();
                    miosocket.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione con il server!");
            System.exit(1);
        }

    }
}
