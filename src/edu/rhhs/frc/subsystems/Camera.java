package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.GlobalProperties;
import edu.rhhs.frc.vision.ImageProcessor;
import edu.rhhs.frc.vision.Target;
import edu.rhhs.frc.vision.TargetSelector;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Camera extends Subsystem {
      
    private static final double CAMERA_FOV_HORIZONTAL_ANGLE = 48.0;  // M206 54 deg actual
    private static final double CAMERA_FOV_VERTICAL_ANGLE = 36.9;  // M206 41.8 deg actual
     
    private AxisCamera m_camera = null;
    private int m_imageCounter = 9;
    private boolean m_imageProcessingFinished = false;
    private TargetSelector m_targetSelector = null;
    private int m_selectedTarget = Target.TOP;
    private double m_angleOffset = 0;
    private double m_distanceOffset = 0;
   
    public Camera() {        
        setupCamera();
    }
    
    private void setupCamera() {
        try {
            m_camera = AxisCamera.getInstance("10.33.10.11");

            // Camera defaults
            if (m_camera != null) {
                m_camera.writeResolution(GlobalProperties.CAMERA_RESOULTION);
                m_camera.writeExposurePriority(AxisCamera.ExposurePriorityT.imageQuality);
                m_camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedIndoor);
            }
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing camera.  Message = " + e.getMessage());
        }
    }
    
    private ColorImage getCameraImage() {
        ColorImage cameraImage = null;
        try {
            cameraImage = m_camera.getImage();

//            String padding = (m_imageCounter < 10) ? "0" : "";
//            String inputFilename = "../../tmp/testImage" + padding + m_imageCounter + ".jpg";
//            cameraImage = new HSLImage(inputFilename);
//            System.out.println("Original Image Loaded = " + inputFilename + ", size = " + cameraImage.getHeight() + "X" + cameraImage.getWidth());
        } 
        catch (AxisCameraException e) {
            System.out.println("Error getting image from camera = " + e.getMessage());
        } 
        catch (NIVisionException e) {
            System.out.println("NIVision error = " + e.getMessage());
        }
        
        return cameraImage;
    }
    
    public void setSelectedTarget(int selectedTarget) {
        m_selectedTarget = selectedTarget;
    }
    
    public void setAngleOffset(double deltaAngle) {
        m_angleOffset = deltaAngle;
    }
 
    public double getAngleOffset() {
        return m_angleOffset;
    }
   
    public void setDistanceOffset(double deltaDistance) {
        m_distanceOffset = deltaDistance;
    }
 
    public double getDistanceOffset() {
        return m_distanceOffset;
    }
   
    public void writeImages() {
        processImage(true);
    }
    
    public void identifyTargets() {
        processImage(false);
    }
 
    private void processImage(boolean outputImages) {
        if (m_camera == null) {
            setupCamera();
            if (m_camera == null) {
                return;
            }
        }
        
        ColorImage originalImage = null;
        try {
            m_imageProcessingFinished = false;
            originalImage = getCameraImage();
            if (originalImage == null) {
                return;
            }

            if (outputImages == true) {
                String outputFilename = "../../tmp/originalImage" + m_imageCounter + ".bmp";
                originalImage.write(outputFilename);
                System.out.println("Original Image Saved = " + outputFilename + ", size = " + originalImage.getHeight() + "X" + originalImage.getWidth());
            }
            
            // Process the image...
            String processedFilname = (outputImages == true) ? "../../tmp/processedImage" + m_imageCounter + ".bmp" : null;
            ParticleAnalysisReport[] reports = ImageProcessor.identifyRectangularTargets(originalImage, processedFilname);
            m_targetSelector = new TargetSelector(reports, m_selectedTarget, CAMERA_FOV_HORIZONTAL_ANGLE);
            
            m_imageCounter++;
        }
        catch (Exception e) {
            System.out.println("Unknown image error = " + e.getMessage());   
        }
        finally {
            m_imageProcessingFinished = true;
            try {
                if (originalImage != null) {
                    originalImage.free();
                }
             }
            catch (NIVisionException e) {
                System.out.println("Error freeing image = " + e.getMessage());            
            }
        }
    }
    
    public boolean isImageProcessingFinished() {
        return m_imageProcessingFinished;
    }
    
    public double getBestTargetCameraDistanceFt() {
        if (m_targetSelector != null) {
            return m_targetSelector.getBestTargetCameraDistanceFt() + m_distanceOffset;
        }
        return 0;
    }
    
    public double getAverageTargetCameraDistanceFt() {
        if (m_targetSelector != null) {
            return m_targetSelector.getAverageTargetCameraDistanceFt() + m_distanceOffset;
        }
        return 0;
    }
    
    public double getBestHorizontalAngleToSelectedTargetDeg() {
        if (m_targetSelector != null) {
            return m_targetSelector.getBestHorizontalAngleToSelectedTargetDeg() + m_angleOffset;
        }
        return 0;
    }
    
    public double getAverageHorizontalAngleToSelectedTargetDeg() {
        if (m_targetSelector != null) {
            return m_targetSelector.getAverageHorizontalAngleToSelectedTargetDeg() + m_angleOffset;
        }
        return 0;
    }
    
    public String getSelectedTargetName() {
        return Target.LOCATION_NAMES[m_selectedTarget];
    }

    public String getBestTargetName() {
        if (m_targetSelector != null) {
            int bestTarget = m_targetSelector.getBestTarget();
            if (bestTarget != -1) {
                return Target.LOCATION_NAMES[bestTarget];
            }
            return "No Target";
        }
        return "No Image";
    }

    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putInt("Image #", m_imageCounter-1);
        SmartDashboard.putString("Selected Target", getSelectedTargetName());
        SmartDashboard.putString("Best Target", getBestTargetName());
        SmartDashboard.putDouble("Camera Distance (Best)", getBestTargetCameraDistanceFt());
        SmartDashboard.putDouble("Camera Distance (Average)", getAverageTargetCameraDistanceFt());     
        SmartDashboard.putDouble("Camera Angle (Best)", getBestHorizontalAngleToSelectedTargetDeg());
        SmartDashboard.putDouble("Camera Angle (Average)", getAverageHorizontalAngleToSelectedTargetDeg());
        SmartDashboard.putDouble("Distance Offset", m_distanceOffset);
        SmartDashboard.putDouble("Angle Offset", m_angleOffset);
   }
}
