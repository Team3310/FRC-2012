package edu.rhhs.frc.vision;

import edu.wpi.first.wpilibj.image.*;

/**
 * @author rhhs
 * 
 * This class takes an image as input and applies various filters and
 * techniques to identify the targets
 */
public class ImageProcessor {
    
    private static final int NUM_SMALL_OBJECT_EROSIONS = 2;
    
    private static final int THRESHOLD_HUE_MIN = 0;
    private static final int THRESHOLD_HUE_MAX = 255;
    private static final int THRESHOLD_SATURATION_MIN = 0;
    private static final int THRESHOLD_SATURATION_MAX = 45;
    private static final int THRESHOLD_LUMINANCE_MIN = 198;
    private static final int THRESHOLD_LUMINANCE_MAX = 255;
    
    private static final int PARTICLE_SEARCH_HEIGHT_MIN = 30;
    private static final int PARTICLE_SEARCH_HEIGHT_MAX = 150;
    private static final int PARTICLE_SEARCH_WIDTH_MIN = 40;
    private static final int PARTICLE_SEARCH_WIDTH_MAX = 200;
    
    public static ParticleAnalysisReport[] identifyRectangularTargets(ColorImage originalImage, String outputFilename) {
        if (originalImage == null) {
            return null;
        }

        BinaryImage thresholdImage = null;
        BinaryImage bigObjectsImage = null;
        BinaryImage convexHullImage = null;
        ParticleAnalysisReport[] reports = null;
        try {
            thresholdImage = originalImage.thresholdHSL(THRESHOLD_HUE_MIN, THRESHOLD_HUE_MAX, THRESHOLD_SATURATION_MIN, THRESHOLD_SATURATION_MAX, THRESHOLD_LUMINANCE_MIN, THRESHOLD_LUMINANCE_MAX);
            if (thresholdImage == null) {
                return null;
            }
            
            bigObjectsImage = thresholdImage.removeSmallObjects(true, NUM_SMALL_OBJECT_EROSIONS);
            if (bigObjectsImage == null) {
                return null;
            }
            
            convexHullImage = bigObjectsImage.convexHull(true);
            if (convexHullImage == null) {
                return null;
            }
            
            // Set up the particle criteria
            CriteriaCollection particleSearchCriteria = new CriteriaCollection();
            particleSearchCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, PARTICLE_SEARCH_WIDTH_MIN, PARTICLE_SEARCH_WIDTH_MAX, false);
            particleSearchCriteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, PARTICLE_SEARCH_HEIGHT_MIN, PARTICLE_SEARCH_HEIGHT_MAX, false);
            convexHullImage.particleFilter(particleSearchCriteria);

            // Get the particle reports
            reports = convexHullImage.getOrderedParticleAnalysisReports();
           
            if (outputFilename != null) {
                convexHullImage.write(outputFilename);
                System.out.println("Processed Image Saved = " + outputFilename + ", size = " + convexHullImage.getHeight() + "X" + convexHullImage.getWidth());
            }
        } 
        catch (NIVisionException e) {
            System.out.println("NIVision error = " + e.getMessage());            
        } 
        finally {
            try {
                if (thresholdImage != null) {
                    thresholdImage.free();
                }
                if (bigObjectsImage != null) {
                    bigObjectsImage.free();
                }
                if (convexHullImage != null) {
                    convexHullImage.free();
                }
            }
            catch (NIVisionException e) {
                System.out.println("Error freeing image = " + e.getMessage());            
            }
        }

        return reports;
    }

}
