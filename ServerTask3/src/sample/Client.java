package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.text.NumberFormat;

public class Client extends javax.swing.JApplet {

    private JTextField jtfAnnualInterest =new JTextField(22);
    private JTextField jtfNumberOfYears =new JTextField(22);
    private JTextField jtfLoanAmount =new JTextField(22);
    private JTextArea jtaText=new JTextArea(5,20);
    private JButton jtbSubmit =new JButton("Submit");

    Socket socket;
    // months in a year = 12
    double MONTHS_IN_A_YEAR = 12;
  
    // create a new instance of the client
    public void init(){
        //create panel for input
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(3,1));
        // label panel for client
        panel.add(new JLabel("Annual Interest Rate\n"));
        panel.add(new JLabel("Number OF Years\n"));
        panel.add(new JLabel("Loan Amount\n"));

        JPanel panel2=new JPanel();
        panel2.setLayout(new GridLayout(3,1));

        jtfAnnualInterest.setHorizontalAlignment(JTextField.RIGHT);
        jtfNumberOfYears.setHorizontalAlignment(JTextField.RIGHT);
        jtfLoanAmount.setHorizontalAlignment(JTextField.RIGHT);

        panel2.add(jtfAnnualInterest);
        panel2.add(jtfNumberOfYears);
        panel2.add(jtfLoanAmount);

        JPanel left=new JPanel(new BorderLayout());
        left.add(panel,BorderLayout.WEST);
        left.add(panel2,BorderLayout.CENTER);
        JScrollPane scrollPane=new JScrollPane(jtaText);
        add(left,BorderLayout.CENTER);
        add(jtbSubmit,BorderLayout.EAST);
        add(scrollPane,BorderLayout.SOUTH);
        jtbSubmit.addActionListener(new ButtonListener());

        try{
            socket=new Socket("localhost",8000);} catch(IOException ex) {
            System.err.println(ex);
        }
    }

    private class ButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            try{
                double interestRate = Double.parseDouble(jtfAnnualInterest.getText());
                double years = Double.parseDouble(jtfNumberOfYears.getText());
                double amount = Double.parseDouble(jtfLoanAmount.getText());

                DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
                DataInputStream dis=new DataInputStream(socket.getInputStream());

                dos.writeDouble(interestRate);
                dos.writeDouble(years);
                dos.writeDouble(amount);
                // calculate money figures
                double monthlyPayment= dis.readDouble();
                double numberOfPayments = (years * MONTHS_IN_A_YEAR);
                double totalPayment  = (monthlyPayment * numberOfPayments);

                jtaText.append("\nAnnual Interest Rate: "+ interestRate + "%\n");
                jtaText.append("Number Of Years: "+ years + "\n");
                jtaText.append("Loan Amount "+ amount + "\n");
                jtaText.append("Monthly payment: "+ NumberFormat.getCurrencyInstance().format(monthlyPayment)
                        +"\nTotal payment: "+ NumberFormat.getCurrencyInstance().format(totalPayment) +"\n");

            } catch(IOException ex){
                System.err.println(ex);

            }

        }

    }

    public static void main(String[] args){

        JFrame frame=new JFrame("Client");
        Client applet=new Client();
        frame.add(applet,BorderLayout.CENTER);
        applet.init();
        applet.start();
        frame.pack();
        frame.setVisible(true);

    }

}

