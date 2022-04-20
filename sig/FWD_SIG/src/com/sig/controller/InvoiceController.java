
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sig.controller;

import com.sig.model.CSVModel;
import com.sig.model.InvoiceHeader;
import com.sig.view.InvoiceForm;
import common.DateUtils;
import java.io.IOException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 *
 * @author Mohamed Barakat
 */
public class InvoiceController {
    CSVModel model;
    InvoiceForm view;
    private int selectedHeadersIndex;


    public InvoiceController(
            CSVModel model, InvoiceForm view
    ) {
        this.model=model;
        this.view=view;
        initButtonsActionListeners();
        initMenuItemsActionListeners();
        initTableModels();

    }
    
    private void initTableModels(){
      JTable invTable=  view.getInvTable();
      invTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      invTable.setCellSelectionEnabled(false);
      invTable.setFocusable(false);
      invTable.setRowSelectionAllowed(true);
      invTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
             public void valueChanged(ListSelectionEvent e) {
                 invoiceTableSelectionListener(e);
      }
      });
      JTable itmTable=view.getItmTable();
      invTable.setModel(model.getInvTableModel());
      itmTable.setModel(model.getItmTableModel());
      System.out.println("Model is"+   invTable.getModel());
   
    }
    
    private void invoiceTableSelectionListener(ListSelectionEvent e){
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
       int index= lsm.getAnchorSelectionIndex();
if(index!=-1){
      InvoiceHeader selectedHeader=  model.selectHeader(index);
      if(selectedHeader!=null){
        selectedHeadersIndex=index;
       view.getInvNumLbl().setText(String.valueOf(selectedHeader.getNum()));
           view.getInvDateLbl().setText(String.valueOf(DateUtils.readDate(selectedHeader.getInvDate())));
               view.getCstNameLbl().setText(String.valueOf(selectedHeader.getCustomer()));
                   view.getInvTotalLbl().setText(String.valueOf(selectedHeader.getInvoiceTotal()));
      }
}
    }
    
    private void initButtonsActionListeners(){
        view.getCreateNewInvBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             JTextField field1 = new JTextField();
JTextField field2 = new JTextField();
Object[] message = {
    "Enter date:", field1,
    "Enter customer name:", field2,
};
int option = JOptionPane.showConfirmDialog(null, message, "Create New Invoice", JOptionPane.OK_CANCEL_OPTION);
if (option == JOptionPane.OK_OPTION)
{
    String date = field1.getText();

    String cstName = field2.getText();

   if( validateCreateInvoice(date,cstName)){

  model.createNewInvoice(date, cstName);
   }
    
}
            }
        });
        view.getCancelInvBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        view.getDeleteInvBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
             
                     model.deleteInvoice(selectedHeadersIndex);
               
            }
        });
        view.getSaveInvBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    private void initMenuItemsActionListeners(){
        view.getLoadInvFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });
        view.getSaveInvFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if( model.saveFiles()){
                     JOptionPane.showMessageDialog(null, "Your file has been saved successfully.");
               }
               else{
                     JOptionPane.showMessageDialog(null, "Some error happened while saving your .");
               }
            }
        });
    }


    public void loadFile(){
        String headersFile= loadHeaderFile();
        if(headersFile!=null){
            String linesFile= loadLinesFile();
            if(linesFile!=null){
                try {
                    model.readFiles(headersFile, linesFile);
                } catch (IOException ex) {
                  JOptionPane.showMessageDialog(null, "Please select valid files");
                }
             
            }
        }

    }
    
    
    private String loadHeaderFile(){
        JOptionPane.showMessageDialog(null, "Please select the headers file");
        String headersFile=selectFile();
        return headersFile;

    }

    private String loadLinesFile(){
        JOptionPane.showMessageDialog(null, "Please select the lines file");
        String linesFile=selectFile();
        return linesFile;
    }

    private String selectFile(){
        JFileChooser j = new JFileChooser();

        if(j.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            String selectedFilePath= j.getSelectedFile().getPath();
            return selectedFilePath;
        }
        return null;
    }

public boolean validateCreateInvoice(String date,String customerName){
    boolean validation=true;
                     try {
                     DateUtils.validateDate(date);
                 } catch (ParseException ex) {
                    validation=false;
                     JOptionPane.showMessageDialog(null, "Please enter a valid date!");
                     
                 }
                         if(customerName.equals("")){
                             validation=false;
         JOptionPane.showMessageDialog(null, "Please enter a valid customer name!");
    }
                         return validation;
}

}
