package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.rules.ExpectedException;

import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/dummy/myerp/testbusiness/business/resources/bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplSIT extends BusinessTestCase {

    @Mock
    private ComptabiliteManagerImpl manager;
    private EcritureComptable vEcritureComptable;
    private JournalComptable journalComptable;


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void comptabiliteManagerImplSIT_Init(){
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
    }


    @After
    public void ResetvEcritureComptable(){
        journalComptable = new JournalComptable();
        vEcritureComptable=new EcritureComptable();
    }

    @Test
    public void test1_ConnectioToDB() throws FunctionalException {

        List<EcritureComptable> ecritureComptableList=manager.getListEcritureComptable();
        Assert.assertNotEquals(ecritureComptableList.size(),null);

    }

    @Test
    public void test2_InsertEcrtirueComptable_withFreeReference() throws FunctionalException {

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
    public void test3_InsertEcrtirueComptable_withUsedReference() throws FunctionalException {

        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setLibelle("Cartouches d'imprimante");
        manager.insertEcritureComptable(vEcritureComptable);
    }

    @Test
    public void test4_DeleteEcritureComptable(){

        EcritureComptable ecritureComptable= manager.getListEcritureComptable().get(5);
        int sizeOfList = manager.getListEcritureComptable().size();
        manager.deleteEcritureComptable(ecritureComptable.getId());
        Assert.assertEquals(sizeOfList,manager.getListEcritureComptable().size()+1);

    }

    @Test
    public void test5_UpdateEcritureComptable() throws FunctionalException{

        EcritureComptable ecritureComptable=manager.getListEcritureComptable().get(0);
        String ancienLibelle = ecritureComptable.getLibelle();
        ecritureComptable.setLibelle("libelle updated");
        manager.updateEcritureComptable(ecritureComptable);
        String nouveauLibelle = manager.getListEcritureComptable().get(0).getLibelle();
        Assert.assertNotEquals(ancienLibelle,nouveauLibelle);
        ecritureComptable.setLibelle(ancienLibelle);
        manager.updateEcritureComptable(ecritureComptable);

    }

    @Test
    public void test6_checkEcitureComptable() throws FunctionalException{

        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Test_Insert");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));
        manager.checkEcritureComptable(vEcritureComptable);

    }

    @Test(expected = FunctionalException.class)
    public void test7_checkEcitureComptable_withUsedReference() throws FunctionalException{

        vEcritureComptable.setReference("AC-2016/00001");
        vEcritureComptable.setLibelle("Test");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));
        manager.checkEcritureComptable(vEcritureComptable);

    }

    @Test
    public void test8_checkFormatEtContenuOfReferenceOfEcritureCompatble() throws FunctionalException{
        vEcritureComptable.setReference("AC-2016/00001");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String date1 = "2016";
        Date date=null;
        try {
            date = simpleDateFormat.parse(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        vEcritureComptable.setDate(date);
        manager.checkFormatEtContenuOfReferenceOfEcritureCompatble(vEcritureComptable);
    }


    @ParameterizedTest(expected = FunctionalException.class)
    @ValueSource(strings = {"1C-2016/00001","A2-2016/00001","AC/2016/00001","AC-A016/00001","AC-2B16/00001","AC-20C6/00001","AC-201D/00001","AC-2016-00001","AC-2016/A0001","AC-2016/0B001","AC-2016/00C01","AC-2016/000D1","AC-2016/0000E" })
    public void test8_checkFormatEtContenuOfReferenceOfEcritureCompatble_withErrorsInReference_expectFunctionalException(String args) throws FunctionalException{
        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        journalComptable = new JournalComptable("AC", "Achat");
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.setDate(new Date());

        vEcritureComptable.setReference(args);

        manager.checkFormatEtContenuOfReferenceOfEcritureCompatble(vEcritureComptable);

        exceptionRule.expect(FunctionalException.class);
    }

}
