package org.finder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static org.finder.FindPolyLines.findPolyline;


public class PolylinesVisualization extends JPanel {
    private ArrayList<ArrayList<LineSegment>> polylines;
    private ArrayList<Color> colors;
    private double minX, minY, maxX, maxY;
    private double scaleX, scaleY;
    private final int WIDTH = 1000;
    private final int HEIGHT = 900;


    public PolylinesVisualization(ArrayList<ArrayList<LineSegment>> polylines) {
        this.polylines = polylines;
        this.colors = generateDistinctColors();

        findMinMaxCoordinates();
        calculateScaleFactors();

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Set the preferred size based on the screen size
        //setPreferredSize(new Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight()));


        //int width = getWidth();
        //int height = getHeight();
        //setPreferredSize(new Dimension(width, height));







    }

    private void findMinMaxCoordinates() {
        minX = minY = Double.POSITIVE_INFINITY;
        maxX = maxY = Double.NEGATIVE_INFINITY;
        for (ArrayList<LineSegment> polyline : polylines) {
            for (LineSegment segment : polyline) {
                minX = Math.min(minX, segment.x1);
                minY = Math.min(minY, segment.y1);
                minX = Math.min(minX, segment.x2);
                minY = Math.min(minY, segment.y2);
                maxX = Math.max(maxX, segment.x1);
                maxY = Math.max(maxY, segment.y1);
                maxX = Math.max(maxX, segment.x2);
                maxY = Math.max(maxY, segment.y2);
            }
        }
    }

    private void calculateScaleFactors() {
        minX=-10;
        minY=-10;



        // Obtain the default screen device
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        // Get screen resolution
        int screenWidth = dm.getWidth();
        int screenHeight = dm.getHeight();

        scaleX = screenWidth / (screenWidth - minX);
        scaleY = screenHeight / (screenHeight - minY);

        scaleX = 0.50;
        scaleY = 0.50;

        int width = getWidth();
        int height = getHeight();

        // Calculate scaleX and scaleY based on current panel size
        if (getWidth() != 0 && getHeight() != 0) {
            scaleX = (double) getWidth() / (maxX - minX);
            scaleY = (double) getHeight() / (maxY - minY);
        }

    }



    private ArrayList<Color> generateDistinctColors() {
        ArrayList<Color> colors = new ArrayList<>();

        // Predefined set of 14 distinct colors
        colors.add(new Color(255, 0, 0));        // Red
        colors.add(new Color(0, 255, 0));        // Green
        colors.add(new Color(0, 0, 255));        // Blue
        colors.add(new Color(255, 255, 0));      // Yellow
        colors.add(new Color(255, 0, 255));      // Magenta
        colors.add(new Color(0, 255, 255));      // Cyan
        colors.add(new Color(255, 128, 0));      // Orange
        colors.add(new Color(128, 0, 255));      // Purple
        colors.add(new Color(0, 255, 128));      // Turquoise
        colors.add(new Color(128, 255, 0));      // Lime
        colors.add(new Color(255, 0, 128));      // Rose
        colors.add(new Color(0, 128, 255));      // Azure
        colors.add(new Color(255, 128, 128));    // Salmon
        colors.add(new Color(128, 255, 128));    // Spring Green

        return colors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        this.setBackground(Color.BLACK);
        int colorIndex=0;
        for (int i = 0; i < polylines.size(); i++) {
            ArrayList<LineSegment> polyline = polylines.get(i);

            int fontMargin=0;
            for (LineSegment segment : polyline) {
                g2d.setColor(colors.get(colorIndex));
                segment.setLineColor(colors.get(colorIndex));
                int x1 = (int) ((segment.x1 - minX) * scaleX);
                int y1 = (int) ((segment.y1 - minY) * scaleY);
                int x2 = (int) ((segment.x2 - minX) * scaleX);
                int y2 = (int) ((segment.y2 - minY) * scaleY);
                g2d.drawLine(x1, y1, x2, y2);


                // Draw coordinates next to the line
                g2d.setColor(Color.WHITE);
                if(x1==300.0 && y1==39){
                    fontMargin=-15;
                }else {
                    fontMargin=-15;
                }

                g2d.drawString("(" + segment.x1 + ", " + segment.y1 + ")", x1, y1 - fontMargin);
                g2d.drawString("(" + segment.x2 + ", " + segment.y2 + ")", x2, y2 - fontMargin);

                int x3= (int)(x1+ x2)/2;
                int y3= (int)(y1+ y2)/2;
                //g2d.setColor(colors.get(colorIndex));
                g2d.drawString("("+segment.lineNumberId+","+segment.isPolyline+")", x3+5, y3-5);

                //System.out.println("(" + segment.x1 + ", " + segment.y1 + ")");
                //System.out.println("(" + segment.x2 + ", " + segment.y2 + ")");
                colorIndex=colorIndex+1;
            }
        }
        showSortedPolyLines(g2d);
    }



    public void showSortedPolyLines(Graphics2D g2d){

        ArrayList<LineSegment> lineSegmentsSorted = new ArrayList<>();

        for (int i = 0; i < polylines.size(); i++) {
            ArrayList<LineSegment> lineSegments = polylines.get(i);

            for (int j = 0; j < lineSegments.size(); j++) {
                lineSegmentsSorted.add(lineSegments.get(j));
            }
        }

        lineSegmentsSorted.sort(Comparator.comparing(LineSegment::getLength));
        Collections.reverse(lineSegmentsSorted);
        int xl=650;
        int yl=80;
        int colorIndex=0;
            for (int j = 0; j < lineSegmentsSorted.size(); j++) {
                if(lineSegmentsSorted.get(j).isPolyline) {
                    g2d.setColor(lineSegmentsSorted.get(j).getLineColor());
                    g2d.drawString("(line id = " + lineSegmentsSorted.get(j).lineNumberId + ",is poly line = " + lineSegmentsSorted.get(j).isPolyline + ", line length= " + lineSegmentsSorted.get(j).length + ")", xl, yl);
                    yl = yl + 20;
                    colorIndex=colorIndex+1;
                }
            }
        yl=yl+100;

        for (int j = 0; j < lineSegmentsSorted.size(); j++) {
            if(!lineSegmentsSorted.get(j).isPolyline) {
                g2d.setColor(lineSegmentsSorted.get(j).getLineColor());
                g2d.drawString("(line id = " + lineSegmentsSorted.get(j).lineNumberId + ",is poly line = " + lineSegmentsSorted.get(j).isPolyline + ", line length= " + lineSegmentsSorted.get(j).length + ")", xl, yl);
                yl = yl + 20;
            }
        }




        /*for () {
            g2d.drawString("(hello)", 1200, 222);

        }*/

    }





    public void showRandomTest(){

        Button button=new Button("random Polylines test");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                polylines.clear();

                ArrayList<LineSegment> segments = generateLines(13);

                // Find polylines
                while (!segments.isEmpty()) {
                    LineSegment startSegment = segments.get(0);
                    ArrayList<LineSegment> polyline = findPolyline(startSegment, segments);
                    polylines.add(polyline);
                }
                repaint();
            }
        });
        button.setLocation(500,500);
        this.add(button);
        JButton saveButton;

        saveButton = new JButton("Save as Image");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFrameAsImage();
            }
        });
        add(saveButton, BorderLayout.SOUTH);



    }

    private void saveFrameAsImage() {
        try {
            // Get the size of the frame
            Dimension size = getSize();
            // Create a BufferedImage for the frame content
            BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            // Create a graphics context from the BufferedImage
            Graphics2D g = image.createGraphics();
            // Paint the frame content to the graphics context
            paint(g);
            g.dispose();

            // Choose a file to save the image
            File outputFile = new File("frame_image.png");

            // Write the BufferedImage to the file
            ImageIO.write(image, "png", outputFile);

            JOptionPane.showMessageDialog(this, "Frame saved as image successfully!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving frame as image.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    public ArrayList<LineSegment> generateLines(int numberOfLines) {
        Random random = new Random();
        ArrayList<LineSegment> segments = new ArrayList<>();
        int lineNumber = 1;

        // Generate initial line segments
        for (int i = 0; i < numberOfLines; i++) {
            int x1 = random.nextInt(1025); // x1 ranges from 0 to 1024 (inclusive)
            int y1 = random.nextInt(1025); // y1 ranges from 0 to 1024 (inclusive)
            int x2 = random.nextInt(1025); // x2 ranges from 0 to 1024 (inclusive)
            int y2 = random.nextInt(1025); // y2 ranges from 0 to 1024 (inclusive)

            LineSegment lineSegment = new LineSegment(x1, y1, x2, y2);
            lineSegment.setLineNumberId(lineNumber);
            segments.add(lineSegment);
            lineNumber++;
        }

        // Connect segments that are not already part of a polyline
        for (int i = 0; i < segments.size()-5; i++) {
            LineSegment currentSegment = segments.get(i);

            // Check if currentSegment is already part of a polyline
            if (!currentSegment.isPolyline()) {
                // Try to connect currentSegment with another segment
                for (int j = 0; j < segments.size(); j++) {
                    if (i != j) {
                        LineSegment otherSegment = segments.get(j);


                        if(j%2==0) {
                            otherSegment.setX1(currentSegment.getX1());
                            otherSegment.setY1(currentSegment.getY1());
                        }else {

                            otherSegment.setX2(currentSegment.getX2());
                            otherSegment.setY2(currentSegment.getY2());
                        }

                        // Check if otherSegment is not already part of a polyline and can be connected
                        if (!otherSegment.isPolyline() && canConnect(currentSegment, otherSegment)) {
                            currentSegment.setPolyline(true);
                            //currentSegment.setConnectedSegment(otherSegment);
                            otherSegment.setPolyline(true);
                            //otherSegment.setConnectedSegment(currentSegment);
                            break; // Stop trying to connect currentSegment after successful connection
                        }
                    }
                }
            }
        }

        return segments;
    }

    // Method to check if two segments can be connected
    private boolean canConnect(LineSegment segment1, LineSegment segment2) {
        // Check if any endpoint of segment1 matches any endpoint of segment2
        return segment1.isConnected(segment2);
    }



    static double totalLength(ArrayList<LineSegment> polyline) {
        double totalLength = 0.0;
        for (LineSegment segment : polyline) {
            totalLength += segment.getLength();
        }
        return totalLength;
    }
}
