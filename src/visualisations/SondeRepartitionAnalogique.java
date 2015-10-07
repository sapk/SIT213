package visualisations;

import information.Information;

/**
 * Classe réalisant l'affichage de répartition de valeurs composée d'élèments
 * réels (float)
 *
 * @author Antoine GIRARD
 */
public class SondeRepartitionAnalogique extends Sonde<Float> {
    //TODO use a cubic value
    private float resolution = 1f;
    private final float max;
    private final float min;
    private int nbPixels = 20; //Largeur en pixel par point 

    /**
     * pour construire une sonde répartition analogique
     *
     * @param nom le nom de la fenêtre d'affichage
     */
    public SondeRepartitionAnalogique(String nom, float min, float max) {
        super(nom);
        this.min = min;
        this.max = max;
    }
    public SondeRepartitionAnalogique(String nom, float min, float max, float resolution) {
        this(nom,min,max);
        this.resolution = resolution;
    }
    public SondeRepartitionAnalogique(String nom, float min, float max, float resolution, int nbPixels) {
        this(nom,min,max,resolution);
        this.nbPixels = nbPixels;
    }

    public void recevoir(Information<Float> information) {
        informationRecue = information;
        int nbElements = (int) ((max - min) / resolution);
        System.out.println("max : "+max+" min : "+min+" nbElements : "+nbElements);
        float[] table = new float[nbElements];
        for (float f : information) {
            //System.out.println("Test : " + f + /*" / " + f % resolution + */" / "+(f-(f%resolution))+" / "+(int)(f-(f%resolution) - min)*1/resolution + " / " +(int)(f*resolution) - min );
            //table[(int) ((f - (f % resolution) - min) * 1 / resolution)]++;
            if(f>min && f<max){
                table[(int)((f-min)*1/resolution)]++;
            }else{
                System.out.println("Value hors intervalle : "+f+" Index : "+(int)((f-min)*1/resolution));
            }
        }
        new VueCourbe(table,nbPixels, nom);
    }

}
