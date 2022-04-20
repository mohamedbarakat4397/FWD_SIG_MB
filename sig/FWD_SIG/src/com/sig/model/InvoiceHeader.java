/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sig.model;

import common.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Mohamed Barakat
 */
public class InvoiceHeader {
    private int num;
    private String customer;
    private Date invDate;
    private ArrayList<InvoiceLine> lines;

    public InvoiceHeader() {
    }
    
    public static List<InvoiceHeader> fromCSV(List<String[]> list){
        ArrayList<InvoiceHeader> invList=new ArrayList();
        for(int i=0;i<list.size();i++){
           Date date=  DateUtils.formatDate(list.get(i)[1]);
            invList.add(new InvoiceHeader(Integer.parseInt(list.get(i)[0]),list.get(i)[2],
                  date));
        }
        return invList;
    }
     

    public InvoiceHeader(int num, String customer, Date invDate) {
        this.num = num;
        this.customer = customer;
        this.invDate = invDate;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public ArrayList<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<InvoiceLine> lines) {
        this.lines = lines;
    }
    
    public double getInvoiceTotal(){
        double total =0.0;
        if(lines!=null&&!lines.isEmpty()){
                    for (int i=0; i < lines.size(); i++){
            
            total += lines.get(i).getLineTotal();            
        }
        }
       
        return total;
        
    }
}
