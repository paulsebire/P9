package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;

import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = {"classpath:bootstrapContext.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplSIT extends BusinessTestCase {

    @Mock
    private ComptabiliteManagerImpl manager;
    private EcritureComptable vEcritureComptable;
    private JournalComptable journalComptable;


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
        manager.deleteEcritureComptable(vEcritureComptable.getId());

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
    public void test4_DeleteEcritureComptable() throws FunctionalException{
        vEcritureComptable.setReference("AC-2020/00001");
        vEcritureComptable.setLibelle("Test_Insert");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));
        manager.insertEcritureComptable(vEcritureComptable);
        EcritureComptable ecritureComptable= manager.getListEcritureComptable().get(manager.getListEcritureComptable().size()-1);
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
        vEcritureComptable.setReference("TE-2020/00001");
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
    


    @ParameterizedTest
    @ValueSource(strings = {"1C-2016/00001","A2-2016/00001","AC/2016/00001","AC-A016/00001","AC-2B16/00001","AC-20C6/00001","AC-201D/00001","AC-2016-00001","AC-2016/A0001","AC-2016/0B001","AC-2016/00C01","AC-2016/000D1","AC-2016/0000E" })
    public void test9_checkFormatEtContenuOfReferenceOfEcritureCompatble_withErrorsInReference_expectFunctionalException(String args) throws FunctionalException{
        ComptabiliteManagerImpl manag = new ComptabiliteManagerImpl();
         EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(1);
        ecritureComptable.setLibelle("test_RG6");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),null, null,new BigDecimal(123)));

        ecritureComptable.setReference(args);

        System.out.println(ecritureComptable.getReference());
        System.out.println(ecritureComptable.toString());

        Assertions.assertThrows(FunctionalException.class, () -> {
                    manag.checkFormatEtContenuOfReferenceOfEcritureCompatble(ecritureComptable);
                });
    }


    @Test
    public void test10_AddReference() throws FunctionalException{

        manager = new ComptabiliteManagerImpl();
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setLibelle("libelle addReference");
        vEcritureComptable.setDate(new Date());
        journalComptable = (new JournalComptable("BQ","Banque"));
        vEcritureComptable.setJournal(journalComptable);
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),null, new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),null, null,new BigDecimal(123)));
        manager.addReference(vEcritureComptable);
        Assert.assertEquals(vEcritureComptable.getReference(),"BQ-2020/00001");
        manager.deleteSequenceEcritureComptable(manager.getSequenceEcritureComptable("BQ",2020));

    }

    @Test
    public void test11_checkUpdateSequenceComptable() throws FunctionalException{
        SequenceEcritureComptable vSequenceEcritureComptable = manager.getSequenceEcritureComptable("AC",2016);
        vSequenceEcritureComptable.setDerniereValeur(vSequenceEcritureComptable.getDerniereValeur()+1);
        manager.updateSequenceEcritureComptable(vSequenceEcritureComptable);
        assertThat(manager.getSequenceEcritureComptable(vSequenceEcritureComptable.getJournalComptable().getCode(),vSequenceEcritureComptable.getAnnee()).getDerniereValeur()).isEqualTo(41);
        vSequenceEcritureComptable.setDerniereValeur(vSequenceEcritureComptable.getDerniereValeur()-1);
        manager.updateSequenceEcritureComptable(vSequenceEcritureComptable);

    }
    @Test
    public void test11_checkUpdateSequenceComptable_withNewSequence() throws FunctionalException{
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2020,0);
        sequenceEcritureComptable.setJournalComptable(journalComptable);
        manager.insertSequenceEcritureComptable(sequenceEcritureComptable);
        sequenceEcritureComptable.setDerniereValeur(sequenceEcritureComptable.getDerniereValeur()+1);
        manager.updateSequenceEcritureComptable(sequenceEcritureComptable);
        assertThat(manager.getSequenceEcritureComptable(sequenceEcritureComptable.getJournalComptable().getCode(),sequenceEcritureComptable.getAnnee()).getDerniereValeur()).isEqualTo(1);
        manager.deleteSequenceEcritureComptable(sequenceEcritureComptable);

    }

    @Test
    public  void test12_checkInsertAndDeleteSequenceEcritureComptable() throws FunctionalException{
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2020,0);
        sequenceEcritureComptable.setJournalComptable(journalComptable);
        int sizeList = manager.getListSequenceEcritureComptable().size();
        manager.insertSequenceEcritureComptable(sequenceEcritureComptable);
        assertThat(manager.getListSequenceEcritureComptable().size()).isEqualTo(sizeList+1);
        manager.deleteSequenceEcritureComptable(sequenceEcritureComptable);
        assertThat(manager.getListSequenceEcritureComptable().size()).isEqualTo(sizeList);
    }



}
