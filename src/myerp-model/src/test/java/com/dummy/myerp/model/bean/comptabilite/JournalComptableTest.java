package com.dummy.myerp.model.bean.comptabilite;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class JournalComptableTest {

    /**
     * test on method getByCode
     */
    @Test
   public void checkMethodGetByCode(){
        JournalComptable journalComptable=new JournalComptable();
        journalComptable.setLibelle("Achat");
        journalComptable.setCode("AC");
        List<JournalComptable> journalComptableList=new ArrayList<>();
        journalComptableList.add(journalComptable);
        Assert.assertEquals(JournalComptable.getByCode(journalComptableList,"AC").getLibelle(),"Achat");
        Assert.assertEquals(JournalComptable.getByCode(journalComptableList,"AC").getCode(),"AC");
    }

    /**
     * test on method getByCode, expect null JournalComptable
     */
    @Test
    public void checkMethodGetByCode_withNullReturn(){
        JournalComptable journalComptable=new JournalComptable();
        journalComptable.setLibelle("Achat");
        journalComptable.setCode("AC");
        List<JournalComptable> journalComptableList=new ArrayList<>();
        journalComptableList.add(journalComptable);
        Assert.assertEquals(JournalComptable.getByCode(journalComptableList,"VE"),null);
    }

    /**
     * Unnecessary test, fulfill the report coverage, not very usefull
     */
    @Test
    public void chekToStringMethod(){
        JournalComptable journalComptable=new JournalComptable("AC","Achat");
        Assert.assertEquals(journalComptable.toString(),"JournalComptable{code='AC', libelle='Achat'}");
    }

}
