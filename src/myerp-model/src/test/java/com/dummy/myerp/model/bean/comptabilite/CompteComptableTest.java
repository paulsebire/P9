package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompteComptableTest {

    @Test
    public void checkMethodGetByNumero(){
        CompteComptable compteComptable=new CompteComptable();
        compteComptable.setLibelle("1er compte");
        compteComptable.setNumero(1);
        List<CompteComptable> compteComptables =new ArrayList<>();
        compteComptables.add(compteComptable);
        Assert.assertEquals(CompteComptable.getByNumero(compteComptables,1).getLibelle(),"1er compte");
        Assert.assertEquals(CompteComptable
                .getByNumero(compteComptables, 1).getNumero().intValue(), 1);

    }
    @Test
    public void checkMethodGetByNumero_withNullReturn(){
        CompteComptable compteComptable=new CompteComptable();
        compteComptable.setLibelle("1er compte");
        compteComptable.setNumero(1);
        List<CompteComptable> compteComptables =new ArrayList<>();
        compteComptables.add(compteComptable);
        Assert.assertEquals(CompteComptable.getByNumero(compteComptables,2),null);


    }

    @Test
    public void chekToStringMethod(){
        CompteComptable compteComptable=new CompteComptable(1,"1er compte");
        Assert.assertEquals(compteComptable.toString(),"CompteComptable{numero=1, libelle='1er compte'}");
    }

}
