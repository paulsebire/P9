package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    private EcritureComptable vEcritureComptable;


    @Before
    public void initCompatibiliteManagerImpl(){
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
    }

    @Test
    public void isEquilibreeRG2() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());
    }
    @Test
    public void isNotEquilibreeRG2() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

    @Test
    public void checkGetTotalDebit_isEqualTo_SumOfDebits() {
        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setLibelle("test gettotalDebit");
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertEquals(vEcritureComptable.getTotalDebit(), BigDecimal.valueOf(200.50+100.50+40).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void checkGetTotalCredit_isEqualTo_SumOfCredits() {

        vEcritureComptable = new EcritureComptable();

        vEcritureComptable.setLibelle("test getTotalCredit");
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcritureComptable.getListLigneEcriture().add(this.createLigne(2, "40", "7.2"));
        Assert.assertEquals(vEcritureComptable.getTotalCredit(), BigDecimal.valueOf(33+301+7.2).setScale(2, BigDecimal.ROUND_HALF_UP));
    }



    @Test
    public void checkMontantLigneEcritureSignesRG4(){
        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(-543.21),
                new BigDecimal(-123.45)));

        Assert.assertEquals(vEcritureComptable.getTotalDebit(),BigDecimal.valueOf(-543.21));
        Assert.assertNotEquals(vEcritureComptable.getTotalDebit(),BigDecimal.valueOf(543.21));

        Assert.assertEquals(vEcritureComptable.getTotalCredit(),BigDecimal.valueOf(-123.45));
        Assert.assertNotEquals(vEcritureComptable.getTotalCredit(),BigDecimal.valueOf(123.45));


    }

}
