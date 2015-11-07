package transmetteurs;

import information.Information;
import information.InformationNonConforme;
import tools.ArrayTool;

/**
 * Classe d'un composant qui transmet des informations de type Double dans un
 * canal bruite et avec des trajets .
 *
 * @author Antoine GIRARD
 * @author Cedric HERZOG
 */
public class TransmetteurAnalogiqueBruiteMulti extends TransmetteurAnalogiqueBruite {

    //Decalage en echantillions
    private final Integer[] dt;
    //Amplitude relative
    private final Double[] ar;

    public TransmetteurAnalogiqueBruiteMulti(Integer[] dt, Double[] ar, Double SNR) throws Exception {
        this(dt, ar, SNR, (int) (Math.random() * 1024), false);
    }

    public TransmetteurAnalogiqueBruiteMulti(Integer[] dt, Double[] ar, Double SNR, boolean quickMode) throws Exception {
        this(dt, ar, SNR, (int) (Math.random() * 1024), quickMode);
    }

    public TransmetteurAnalogiqueBruiteMulti(Integer[] dt, Double[] ar, Double SNR, int seed, boolean quickMode) throws Exception {
        super(SNR, seed, quickMode);
        if (dt.length != ar.length) {
            throw new Exception("Arguments de multiple trajet donnee invalide");
        }
        this.dt = dt;
        this.ar = ar;
    }

    protected void addMultiTrajet() {

        //System.out.println("nbEch avant multi-trajet : " + this.informationEmise.nbElements());
        /* Mise en forme pour les multi-trajet */
        Information<Double> infBruite = new Information<>(this.informationEmise);
        for (int i = 0; i < dt.length; i++) {
            if (ar[i] == 0) {
                continue;
            }
            //System.out.println("Generating trajet n°" + i + " ( dt : " + dt[i] + ", ar : " + ar[i] + " ) ");
            //TODO check if we should maybe do Information retard = ArrayTool.factArrays(this.informationEmise, ar[i]); 
            Information<Double> retard = ArrayTool.factArrays(infBruite, ar[i]); //On genère une information factorise par l'attenuation
            for (int j = 0; j < dt[i]; j++) {
                retard.addAt(0, 0.0); // On ajoute les retards
            }
            //System.out.println("Taille du tableau de retard : " + retard.nbElements());
            this.informationEmise = ArrayTool.sumArrays(this.informationEmise, retard);
        }

        //System.out.println("nbEch dans sortie : " + this.informationEmise.nbElements());
    }

    /**
     * emet l'information construite par le transmetteur
     *
     * @throws InformationNonConforme Information nulle
     */
    @Override
    public void emettre() throws InformationNonConforme {

        checkInformationRecue();
        /* Generation du Bruit */
        addBruit();

        /* Ajout des multi-trajet */
        addMultiTrajet();
        envoyerAuxSuivants();
    }

}
