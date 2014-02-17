/**
 *
 */
package at.ac.uibk.inforet.twittermood;

/**
 * @author vikpek
 *
 */
public class ARFFExportController {
    public static void main(String args[]) throws Exception {
        ARFFGenerator ag = new ARFFGenerator();

        ag.export2ARFF();

    }
}
