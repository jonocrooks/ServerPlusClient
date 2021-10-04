package sample;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.Date;
import java.lang.Math;

public class Server extends JFrame implements Runnable {

    JTextArea jtaArea;
    int thread=0;
    Socket client;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    double MONTHS_IN_A_YEAR = 12;

    public Server() {
        // set server pane
        jtaArea=new JTextArea();
        JScrollPane pane=new JScrollPane(jtaArea);
        add(pane,BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,300);
        setTitle("Server");
        setVisible(true);

        try{
            ServerSocket ss=new ServerSocket(8000);
            jtaArea.append("Server has started at "+ new Date() +"\n");
            while(true){

                client = ss.accept();
                thread++;
                jtaArea.append("starting thread for client "+thread+" at "+ new Date().toString() +"\n");
                jtaArea.append("Client "+ thread +"'s host name is "+client.getInetAddress().getHostName()+"\n");
                jtaArea.append("Client "+ thread +"'s IP Address is "+client.getInetAddress().getHostAddress()+"\n");

                Thread t=new Thread(this);
                t.start();
            }

        } catch(IOException ex){
            System.err.println(ex);
        }
    }

    public void run(){

        try{
            dis=new DataInputStream(client.getInputStream());
            dos=new DataOutputStream(client.getOutputStream());

            while(true){

                double interest= dis.readDouble();
                double years=dis.readDouble();
                double amount=dis.readDouble();

                jtaArea.append("\nAnnual Interest Rate: "+ interest + "%\n");
                jtaArea.append("Number Of Years: "+ years + "\n");
                jtaArea.append("Loan Amount "+ amount +"\n");
                interest = interest/1200;

                double numberOfPayments = (years * MONTHS_IN_A_YEAR);
                double monthlyPayment =  ( interest + (interest/(Math.pow(1+interest,years*MONTHS_IN_A_YEAR)-1))) * amount ;
                //double newMonthlyPayment = (monthlyPayment * numberOfPayments)
                double totalPayment  = (monthlyPayment * numberOfPayments);
                jtaArea.append("Monthly payment: "+ NumberFormat.getCurrencyInstance().format(monthlyPayment) +
                        "\nTotal payment: "+ NumberFormat.getCurrencyInstance().format(totalPayment) +"\n");

                dos.writeDouble(monthlyPayment);
            }
        } catch(IOException ex){
            System.err.println(ex);
        }
    }
    public static void main(String[] args){
        Server server=new Server();
    }

}