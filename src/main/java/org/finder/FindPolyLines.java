package org.finder;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import static org.finder.PolylinesVisualization.totalLength;

public class FindPolyLines {
    private JButton saveButton;


    public void finder() {

        ArrayList<ArrayList<LineSegment>> polylines = new ArrayList<>();

        ArrayList<LineSegment> segments = loadSegmentsFromFile();

        // Find polylines
        while (!segments.isEmpty()) {
            LineSegment startSegment = segments.get(0);
            ArrayList<LineSegment> polyline = findPolyline(startSegment, segments);
            polylines.add(polyline);
        }



        // Sort polylines by total length (descending)
        polylines.sort((p1, p2) -> Double.compare(totalLength(p2), totalLength(p1)));

        displayPolylinesSortedByLength(polylines);


        // Display visualization
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Polylines Finder");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PolylinesVisualization polylinesVisualization = new PolylinesVisualization(polylines);
            //int height = polylinesVisualization.getHeight();
            //int width = polylinesVisualization.getWidth();
            polylinesVisualization.showRandomTest();

            frame.getContentPane().add(polylinesVisualization);
            Container contentPane = frame.getContentPane();
            contentPane.setBackground(Color.black);
            //Color background = frame.getContentPane().getBackground();


            frame.setLocationRelativeTo(null);
            frame.setBackground(Color.black);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);



            // Obtain the default screen device
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayMode dm = gd.getDisplayMode();

            // Get screen resolution
            int screenWidth = dm.getWidth();
            int screenHeight = dm.getHeight();


            frame.setMinimumSize(new Dimension(screenWidth, screenHeight)); // Example maximum size
            frame.setLocation(0,0);



            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            //frame.pack();
            //frame.setSize(screenSize.width,screenSize.height);
            //frame.pack();
            frame.setVisible(true);
            Dimension size = frame.getSize();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            System.out.println();
        });
    }




    static ArrayList<LineSegment> findPolyline(LineSegment startSegment, ArrayList<LineSegment> segments) {
        ArrayList<LineSegment> polyline = new ArrayList<>();
        polyline.add(startSegment);
        startSegment.visited = true;
        segments.remove(startSegment);

        boolean extended = true;
        while (extended) {
            extended = false;
            for (int i = 0; i < segments.size(); i++) {
                LineSegment segment = segments.get(i);
                LineSegment lastSegment = polyline.get(polyline.size() - 1);
                boolean connected = lastSegment.isConnected(segment);
                boolean reverseConnection=segment.isConnected(lastSegment);

                if (!segment.visited && connected && reverseConnection) {
                    segment.setPolyline(true);
                    segment.setConnectedSegmentNextId(lastSegment.getLineNumberId());
                    lastSegment.setConnectedSegmentPreviousId(segment.getLineNumberId());
                    lastSegment.setPolyline(true);
                    polyline.add(segment);
                    segment.visited = true;
                    segments.remove(i);
                    extended = true;
                    i--; // Adjust index due to removal
                }
            }
        }

        return polyline;
    }


    public static ArrayList<LineSegment> loadSegmentsFromFile(){
        ArrayList<LineSegment> segments = new ArrayList<>();
        int lineNumber=1;
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                LineSegment lineSegment = new LineSegment(x1, y1, x2, y2);
                lineSegment.setLineNumberId(lineNumber);
                lineNumber++;
                segments.add(lineSegment);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return segments;
    }



    static void displayPolylinesSortedByLength(ArrayList<ArrayList<LineSegment>> polylines){
        ArrayList<LineSegment> polylineSorted = new ArrayList<>();
        for (ArrayList<LineSegment> polyline : polylines) {

            for (LineSegment segment : polyline) {
                //System.out.println(segment);
                polylineSorted.add(segment);
            }
            //System.out.println();

        }

        polylineSorted.sort(Comparator.comparing(LineSegment::getLength));
        Collections.reverse(polylineSorted);
        System.out.println("sorted by their total length longest to shortest");
        for (LineSegment segment : polylineSorted) {
            double length = segment.getLength();
            BigDecimal bd = new BigDecimal(length);
            bd = bd.setScale(2, RoundingMode.HALF_UP); // RoundingMode.HALF_UP is commonly used for rounding

            double roundedNumber = bd.doubleValue();
            //DecimalFormat df = new DecimalFormat("#.##");
            //String formattedNumber = df.format(length);
            segment.setLength(roundedNumber);
            System.out.println(segment);
        }

    }


}
