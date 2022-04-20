package com.sig.model;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

import common.DateUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class CSVModel {
    String headersFilePath;
    List<InvoiceHeader> headersFile;
    String linesFilePath;
     List<InvoiceLine> linesFile;
     ArrayList<InvoiceLine> currentSelectedInvoicesLineList;
    private DefaultTableModel invTableModel;
    private DefaultTableModel itmTableModel;
    
    public CSVModel(){
         invTableModel=new DefaultTableModel();
               String[] columnNames = new String[] {"No.", "Date", "Customer","Total"};

invTableModel.setColumnIdentifiers(columnNames);
               String[] columnNames2 = new String[] {"No.", "Name", "Price","Count","Total"};


         itmTableModel=new DefaultTableModel();
         itmTableModel.setColumnIdentifiers(columnNames2);
    }
    
        /**
     * @return the invTableModel
     */
    public DefaultTableModel getInvTableModel() {
        return invTableModel;
    }
    
    /**
     * @return the itmTableModel
     */
    public DefaultTableModel getItmTableModel() {
        return itmTableModel;
    }
    
      public void readFiles(String headersFilePath,String linesFilePath) throws IOException{
          invTableModel.setRowCount(0);
          this.headersFilePath=headersFilePath;
        List<String[]> csvHeaders=   readFromCsvFile(headersFilePath);

           headersFile= InvoiceHeader.fromCSV(csvHeaders);
          
            this.linesFilePath=linesFilePath;
        List<String[]> csvLines= readFromCsvFile(linesFilePath);
        
    linesFile=InvoiceLine.fromCSV(csvLines, headersFile);
    
            if(headersFile==null||linesFile==null){
                throw new IOException();
            }
            else{
           fillInvoicesItemList();
                fillHeadersTableModel();
            }
  }
      
      public boolean saveFiles(){
          boolean result=false;
       try{
          if(headersFile!=null&&linesFile!=null&&!headersFile.isEmpty()&&!linesFile.isEmpty()
                  &&headersFilePath!=null&&linesFilePath!=null){
              //Write headers file
             List<String[]> headersData=new ArrayList();
             
            for(InvoiceHeader header: headersFile){
                String[] strArr=new String[3];
                strArr[0]=String.valueOf(header.getNum());
                        strArr[1]=DateUtils.readDate(header.getInvDate());
                        strArr[2]=header.getCustomer();
                headersData.add(strArr);
            }
              writeToCsvFile(headersData,headersFilePath);
              //Write lines file
               List<String[]> linesData=new ArrayList();
             
            for(InvoiceLine line: linesFile){
                    String[] strArr=new String[4];
                    strArr[0]=String.valueOf(line.getHeader().getNum());
                    strArr[1]=line.getName();
                    strArr[2]=String.valueOf(line.getPrice());
                    strArr[3]=String.valueOf(line.getCount());
                linesData.add(strArr);
            }
             writeToCsvFile(linesData,linesFilePath);
             result=true;
          }
          }catch(Exception e){
              result=false;
          }
          return result;
      }
  

    private void writeToCsvFile(List<String[]> data,String fileName){
    try (FileWriter writer = new FileWriter(fileName)){
        for (String[] strings : data) {
            for (int i = 0; i < strings.length; i++) {
                writer.append(strings[i]);
                if(i < (strings.length-1))
                    writer.append(",");
            }
            writer.append(System.lineSeparator());
        }
        writer.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private List<String[]> readFromCsvFile(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            List<String[]> list = new ArrayList<>();
            String line = "";
            while((line = reader.readLine()) != null){
                String[] array = line.split(",");
                list.add(array);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
     public InvoiceHeader selectHeader(int headerIndex){
         System.out.println("header index is"+headerIndex);
         if(headersFile.size()-1>=headerIndex){
      fillLinesTableModel(headerIndex);
      return headersFile.get(headerIndex);
         }
         return null;
  }
     
    
  private void fillHeadersTableModel(){
      if(headersFile!=null){
          for(InvoiceHeader header: headersFile){
              Object[] object={header.getNum(),
                  DateUtils.readDate(header.getInvDate()),header.getCustomer(),header.getInvoiceTotal()};
              System.out.println("Headers object is"+object);
              invTableModel.addRow(object);
          }
          invTableModel.fireTableDataChanged();
      }
  }

  
   private void fillLinesTableModel(int headerIndex){
      if(linesFile!=null){
          itmTableModel.setRowCount(0); //Resetting the table model
          currentSelectedInvoicesLineList=new ArrayList();
          for(InvoiceLine line: linesFile){
              //To filter the current selected header
              if(line.getHeader().getNum()==headerIndex+1){
                  
           Object[] object={line.getHeader().getNum(),line.getName(),line.getPrice(),line.getCount(),line.getLineTotal()};
              itmTableModel.addRow(object);
              currentSelectedInvoicesLineList.add(line);
          }
          }
             itmTableModel.fireTableDataChanged();
      }
  }
   
   private void fillInvoicesItemList(){
           for(InvoiceHeader invoice: headersFile){
                   ArrayList<InvoiceLine> itemsList=new ArrayList();
                   for(InvoiceLine item:linesFile){
                       if(invoice.getNum()==item.getHeader().getNum()){
                           itemsList.add(item);
                       }
                   }
                  invoice.setLines(itemsList);
               }
   }

   public void createNewInvoice(String date,String customerName){
       if(headersFile!=null&&!headersFile.isEmpty()){
            int size=headersFile.size()+1;
           InvoiceHeader header=new InvoiceHeader(size,customerName,DateUtils.formatDate(date));

       getInvTableModel().addRow(new Object[]{header.getNum(), date, customerName,"0"});
                  headersFile.add(header);
           invTableModel.fireTableDataChanged();
   }
   }
   
   public void deleteInvoice(int index){
       invTableModel.removeRow(index);
       headersFile.remove(index);
        invTableModel.fireTableDataChanged();
   }
   
      public void createNewItem(InvoiceHeader header,String name,String count,String price){
       if(linesFile!=null){
            int size;
            if(!linesFile.isEmpty()){
            size=linesFile.size()+1;
       }
           InvoiceLine line=new InvoiceLine(header,name,Double.parseDouble(price),Integer.parseInt(count));

       getItmTableModel().addRow(new Object[]{header.getNum(), name, price,count});
                  linesFile.add(line);
                  selectHeader(header.getNum()-1);
           invTableModel.fireTableDataChanged();
           itmTableModel.fireTableDataChanged();
   }
   }
      
        public void deleteItem(int index){
       itmTableModel.removeRow(index);
      InvoiceLine currentSelectedInvoiceLine= currentSelectedInvoicesLineList.get(index);
    linesFile.remove(currentSelectedInvoiceLine);
        itmTableModel.fireTableDataChanged();
          itmTableModel.fireTableDataChanged();
   }
   

}
