package edu.wpi.first.smartdashboard.gui.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.properties.StringProperty;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 *
 * @author Greg Granito
 */
public class Team100CameraViewer extends StaticWidget implements ITableListener {
	private static final long serialVersionUID = 1L;

	public static final String NAME = "Team100CameraViewer";

    private static final int[] START_BYTES = new int[]{0xFF, 0xD8};
    private static final int[] END_BYTES = new int[]{0xFF, 0xD9};

    private boolean ipChanged = true;
    private String ipString = null;
    private double rotateAngleRad = 0;
    private long lastFPSCheck = 0;
    private int lastFPS = 0;
    private int fpsCounter = 0;
    
    public final Stroke stroke = new BasicStroke(3);
    
    double[] empty = {};
    
    public NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");
    	double[] centerX = table.getNumberArray("centerX", empty);
    	double[] centerY = table.getNumberArray("centerY", empty);
    	double[] width = table.getNumberArray("width", empty);
    	double[] height = table.getNumberArray("height", empty);
    	
    public class BGThread extends Thread {    	
        boolean destroyed = false;

        public BGThread() {
            super("Camera Viewer Background");
        }

        long lastRepaint = 0;
        @Override
        public void run() {
            URLConnection connection = null;
            InputStream stream = null;
            ByteArrayOutputStream imageBuffer = new ByteArrayOutputStream();
            while (!destroyed) {
                try{
                    System.out.println("Connecting to camera");
                    ipChanged = false;
                    URL url = new URL(ipString);
                    connection = url.openConnection();
                    connection.setReadTimeout(250);
                    stream = connection.getInputStream();

                    while(!destroyed && !ipChanged){
                        while(System.currentTimeMillis()-lastRepaint<10){
                            stream.skip(stream.available());
                            Thread.sleep(1);
                        }
                        stream.skip(stream.available());

                        imageBuffer.reset();
                        for(int i = 0; i<START_BYTES.length;){
                            int b = stream.read();
                            if(b==START_BYTES[i])
                                i++;
                            else
                                i = 0;
                        }
                        for(int i = 0; i<START_BYTES.length;++i)
                            imageBuffer.write(START_BYTES[i]);

                        for(int i = 0; i<END_BYTES.length;){
                            int b = stream.read();
                            imageBuffer.write(b);
                            if(b==END_BYTES[i])
                                i++;
                            else
                                i = 0;
                        }

                        fpsCounter++;
                        if(System.currentTimeMillis()-lastFPSCheck>500){
                            lastFPSCheck = System.currentTimeMillis();
                            lastFPS = fpsCounter*2;
                            fpsCounter = 0;
                        }

                        lastRepaint = System.currentTimeMillis();
                        ByteArrayInputStream tmpStream = new ByteArrayInputStream(imageBuffer.toByteArray());
                        imageToDraw = ImageIO.read(tmpStream);
                        System.out.println(System.currentTimeMillis()-lastRepaint);
                        repaint();
                    }

                } catch(Exception e){
                    imageToDraw = null;
                    repaint();
                    e.printStackTrace();
                }

                if(!ipChanged){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {}
                }
            }

        }

        @Override
        public void destroy() {
            destroyed = true;
        }
    }
    private BufferedImage imageToDraw;
    private BGThread bgThread = new BGThread();
    public final StringProperty ipProperty = new StringProperty(this, "Camera IP Address or mDNS name", "axis-camera.local");
    public final IntegerProperty rotateProperty = new IntegerProperty(this, "Degrees Rotation", 0);
    
    @Override
    public void init() {
        setPreferredSize(new Dimension(100, 100));
        ipString = ipProperty.getSaveValue();
        rotateAngleRad = Math.toRadians(rotateProperty.getValue());
        bgThread.start();
        revalidate();
        repaint();
        
        table.addTableListener(this);
    }

    @Override
    public void propertyChanged(Property property) {
        if (property == ipProperty) {
            ipString = ipProperty.getSaveValue();
            ipChanged = true;
        }
        if (property == rotateProperty) {
            rotateAngleRad = Math.toRadians(rotateProperty.getValue());
        }
    }

    @Override
    public void disconnect() {
        bgThread.destroy();
        super.disconnect();
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage drawnImage = imageToDraw; 
        
        if (drawnImage != null) {
        	// cast the Graphics context into a Graphics2D
            Graphics2D g2d = (Graphics2D)g;
            
            // get the existing Graphics transform and copy it so that we can perform scaling and rotation
            AffineTransform origXform = g2d.getTransform();
            AffineTransform newXform = (AffineTransform)(origXform.clone());
            
            // find the center of the original image
            int origImageWidth = drawnImage.getWidth();
            int origImageHeight = drawnImage.getHeight();
            int imageCenterX = origImageWidth/2;
            int imageCenterY = origImageHeight/2;
            
            // perform the desired scaling
            double panelWidth = getBounds().width;
            double panelHeight = getBounds().height;
            double panelCenterX = panelWidth/2.0;
            double panelCenterY = panelHeight/2.0;
            double rotatedImageWidth = origImageWidth * Math.abs(Math.cos(rotateAngleRad)) + origImageHeight * Math.abs(Math.sin(rotateAngleRad));
            double rotatedImageHeight = origImageWidth * Math.abs(Math.sin(rotateAngleRad)) + origImageHeight * Math.abs(Math.cos(rotateAngleRad));         
            		
            // compute scaling needed
            double scale = Math.min(panelWidth / rotatedImageWidth, panelHeight / rotatedImageHeight);
                      
            // set the transform before drawing the image
            // 1 - translate the origin to the center of the panel
            // 2 - perform the desired rotation (rotation will be about origin)
            // 3 - perform the desired scaling (will scale centered about origin)
            newXform.translate(panelCenterX,  panelCenterY);
            newXform.rotate(rotateAngleRad);
            newXform.scale(scale, scale);
            g2d.setTransform(newXform);

            // draw image so that the center of the image is at the "origin"; the transform will take care of the rotation and scaling
            g2d.drawImage(drawnImage, -imageCenterX, -imageCenterY, null);
            
            // restore the original transform
            
            g2d.setColor(Color.BLUE);
            g2d.setStroke(stroke);
            // draw here
        
           final double aspectRatio = (double) drawnImage.getHeight(null) / drawnImage.getWidth(null);
            int x = 0, y = 0, w = getWidth(), h = getHeight();

            // Preserve the image's aspect ratio.  If this component is too wide, make the image less wide and center
            // it horizontally.  If it's too tall, make the image shorter and center it vertically.
            if (w * aspectRatio > h) {
                w = (int) (getHeight() / aspectRatio);
                x = (getWidth() - w) / 2;
            } else {
                h = (int) (getWidth() * aspectRatio);
                y = (getHeight() - h) / 2;
            }
            
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (centerX.length == centerY.length && width.length == height.length && centerX.length == width.length && centerY.length == height.length){
           for (int i = 0; i < centerX.length; i++){
            g2d.drawRect((int) (centerX[i] - (width[i]/2) - imageCenterX), (int) (centerY[i] - (height[i]/2) - imageCenterY), (int)(width[i]), (int)(height[i]));
           }
           
           g2d.setTransform(origXform);
           
           g.setColor(Color.PINK);
           g.drawString("FPS: "+lastFPS, 10, 10);
            }
        } else {
            g.setColor(Color.PINK);
            g.fillRect(0, 0, getBounds().width, getBounds().height);
            g.setColor(Color.BLACK);
            g.drawString("NO CONNECTION", 10, 10);
        }
    }

	@Override
	public void valueChanged(ITable source, String key, Object value,
			boolean isNew) {
    	 centerX = table.getNumberArray("centerX", empty);
    	 centerY = table.getNumberArray("centerY", empty);
    	 width = table.getNumberArray("width", empty);
    	 height = table.getNumberArray("height", empty);
	}
}