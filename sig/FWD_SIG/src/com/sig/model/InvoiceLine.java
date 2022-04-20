/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sig.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohamed Barakat
 */
public class InvoiceLine {
    private InvoiceHeader header;
    private String Name;
    private double price;
    private int count;

    public InvoiceLine() {
    }
    
     public static List<InvoiceLine> fromCSV(List<String[]> list,List<InvoiceHeader> headers){
        ArrayList<InvoiceLine> invLineList=new ArrayList();
        for(int i=0;i<list.size();i++){
          int id= Integer.parseInt(list.get(i)[0]);
          InvoiceHeader header=null;
          for(int j=0;j<headers.size();j++){
              if(headers.get(j).getNum()==id){
                  header=headers.get(j);
              }
          }
          
            invLineList.add
        (new InvoiceLine(header,list.get(i)[1],Double.parseDouble(list.get(i)[2]),Integer.parseInt(list.get(i)[3])));
        }
        return invLineList;
    }

    public InvoiceLine(InvoiceHeader header, String Name, double price, int count) {
        this.header = header;
        this.Name = Name;
        this.price = price;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InvoiceHeader getHeader() {
        return header;
    }

    public void setHeader(InvoiceHeader header) {
        this.header = header;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getLineTotal(){
        return count * price ;
    }
}
