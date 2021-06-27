package kmeans;

import java.io.*;
import java.util.Scanner;

public class Driver {
	public static void main(String [] args) throws FileNotFoundException {
		//Setting up variables. Loading runtime arguments into them.
		int k = Integer.parseInt(args[0]);
		File data = new File(args[1]);
		int numLines = 0;
		numLines = getLineCount(data);
		Point points[] = new  Point[numLines];
		Point centroids[] = new Point[k];
		
		if (!data.exists()) {
			System.out.println("Could not find file!");
			System.out.println("Make sure file is in the FIRST kmeans folder! Not src and not the one inside src!");
		}
		// Creating output.txt
		createOutput();
		
		// Printing debug data...
		System.out.println("Number of clusters: " + k);
		System.out.println("File name: " + data.getName());
		System.out.println("Number of points in file: " + numLines);
		
		//Parsing the kmeans.txt into point objects.
		parseData(data, numLines, points);
		
		
		//Pick a K number of random points to be the first centroids
		centroids = createCentroids(points, numLines, k);
		calculateNearest(points, centroids);
		
		
		//Get the average location of each point to its centroid.
		//Set that location as our new centroid.
		//Recalculate the nearest centroid
		//We're gonna do that 10 times for accuracy
		for (int i = 0; i < 10; i++) {
			updateCentroids(centroids, points);
			calculateNearest(points, centroids);
		}
		
		//Writing coordinates and clusters to the file...
		writeToOutput(numLines, points);
	}
	
	public static void updateCentroids(Point[] centroids, Point[] points) {
		for (int i = 0; i < centroids.length; i++) {
			int totalX = 0;
			int totalY = 0;
			int totalPoints = 0;
			for (int j = 0; j < points.length; j++) {
				if (points[j].getCluster() == i+1) {
					totalX += points[j].getX();
					totalY += points[j].getY();
					totalPoints++;
				}
			}
				centroids[i].setX(totalX/totalPoints);
				centroids[i].setY(totalY/totalPoints);
		}
	}
	
	public static void calculateNearest(Point[] points, Point[] centroids) {
		double ac;
		double cb;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < centroids.length; j++) {
				ac = Math.abs(centroids[j].getY() - points[i].getY());
				cb = Math.abs(centroids[j].getX() - points[i].getX());
				double dist = Math.hypot(ac, cb);
				if (j == 0) {
					points[i].setDistance(dist);
					points[i].setCluster(j+1);
				}
				else if (dist < points[i].getDistance()) {
					points[i].setDistance(dist);
					points[i].setCluster(j+1);
				}
			}
		}
	}
	
	public static Point[] createCentroids(Point points[], int numLines, int k) {
		int min = 0;
		int max = numLines-1;
		Point[] centroids = new Point[k];
		for (int i = 0; i < k; i++) {
			int rand = (int)(Math.random() * (max-min+1));
			//Makes sure that a point is not selected as a centroid more than once in one iteration.
			while (points[rand].getIsCentroid()) {
				rand = (int)(Math.random() * (max-min+1));
			}
			if (!points[rand].getIsCentroid()) {
				points[rand].setCluster(i+1);
				points[rand].setCentroid(true);
				centroids[i] = new Point(points[rand].getX(), points[rand].getY());
			}
		}
		return centroids;
	}
	
	public static void parseData(File data, int numLines, Point points[]) throws FileNotFoundException {
		Scanner parseData = new Scanner(data);
		for (int i = 0; i < numLines; i++) {
			String line = parseData.nextLine();
			String[] split = line.split("\\s+");
			points[i] = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
		}	
		parseData.close();
	}
	
	public static void createOutput() {
		File output = new File("output.txt");
		try {
			if (output.createNewFile()) {
				System.out.println("Creating output: " + output.getName());
				System.out.println("Output location: " + output.getAbsolutePath());
			} else {
				System.out.println(output.getName() + " already exists.");
				System.out.println("Output location: " + output.getAbsolutePath());
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToOutput(int numLines, Point points[]) {
		try {
			FileWriter outputWrite = new FileWriter("output.txt");
			for (int i = 0; i < numLines; i++) {
				
				outputWrite.write(points[i].getX() + "\t" + (points[i].getY() + "\t" + (points[i].getCluster() + "")));
				outputWrite.write(String.format("%n"));
			}
			outputWrite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getLineCount(File data) {
		int numLines = 0;
		try {
			Scanner lineCount = new Scanner(data);
			while (lineCount.hasNextLine()) {
				lineCount.nextLine();
				numLines++;
			}
			lineCount.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return numLines++;
	}
}
