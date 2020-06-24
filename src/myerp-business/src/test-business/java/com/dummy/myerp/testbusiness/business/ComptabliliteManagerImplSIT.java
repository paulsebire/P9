package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/dummy/myerp/testbusiness/business/resources/bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ExtendWith(MockitoExtension.class)
public class ComptabliliteManagerImplSIT extends BusinessTestCase {

    @Mock
    private ComptabiliteManagerImpl manager;
    private EcritureComptable vEcritureComptable;
    private JournalComptable journalComptable;

    @Test
    public void test1_ConnectioToDB() throws FunctionalException {

        manager = new ComptabiliteManagerImpl();
        List<EcritureComptable> ecritureComptableList=manager.getListEcritureComptable();
        System.out.println("test"+ecritureComptableList.get(1).toString());

    }

    @Test
    public void test_InsertEcrtirueComptable_withFreeReference() throws FunctionalException {

        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Test_Insert");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));
        int tailleDeLalisteAvantInsert = manager.getListEcritureComptable().size();
        manager.insertEcritureComptable(vEcritureComptable);
        Assert.assertEquals(manager.getListEcritureComptable().size(),tailleDeLalisteAvantInsert + 1);
        Assert.assertEquals(vEcritureComptable.getReference(),"AC-2020/00001");

    }

    @Test(expected = FunctionalException.class)
    public void test_InsertEcrtirueComptable_withUsedReference() throws FunctionalException {

        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setLibelle("Cartouches d'imprimante");
        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void test_DeleteEcritureComptable(){

        manager = new ComptabiliteManagerImpl();
        EcritureComptable ecritureComptable= manager.getListEcritureComptable().get(5);
        int sizeOfList = manager.getListEcritureComptable().size();
        manager.deleteEcritureComptable(ecritureComptable.getId());
        Assert.assertEquals(sizeOfList,manager.getListEcritureComptable().size()+1);

    }

    @Test
    public void test_UpdateEcritureComptable() throws FunctionalException{
        manager= new ComptabiliteManagerImpl();
        EcritureComptable ecritureComptable=manager.getListEcritureComptable().get(0);
        String ancienLibelle = ecritureComptable.getLibelle();
        ecritureComptable.setLibelle("libelle updated");
        manager.updateEcritureComptable(ecritureComptable);
        String nouveauLibelle = manager.getListEcritureComptable().get(0).getLibelle();
        Assert.assertNotEquals(ancienLibelle,nouveauLibelle);

    }

}
