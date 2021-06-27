package kmeans;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JButton;


class Board extends JPanel implements ActionListener, MouseListener, MouseMotionListener{


    static int size = 100;  //used mainly for board size
    static int cellSize = 10;
    static boolean bool[][];
    static JButton cells[][];
    static Color c;
    static Timer timer;
    static boolean isStopped;
    static int k = 0;
    Point[] points;
    Point[] centroids;
    int numPoints = 0;
    static boolean pressedStart = false;

    public Board() {
    	c = Color.white;
    	bool = new boolean[size][size];
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// Initialize empty array.
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				bool[i][j] = false;
			}
		}
    }
    
    public void startGame() {
    	if (boardIsEmpty()) {
    		System.out.println("Can not get centroid. There are no points."); 
    	}
    	if (k > 4) {
    		JOptionPane.showMessageDialog(null, "Error. K must be less than or equal to 4.");
    	}
    	else {
    		pressedStart = true;
    		numPoints = getNumPoints(bool);
    		points = new Point[numPoints];
    		getPoints(bool, points);
    		centroids = new Point[k];
    		centroids = createCentroids(points, numPoints, k);
    		calculateNearest(points, centroids);
    		for (int i = 0; i < 10; i++) {
    			updateCentroids(centroids, points);
    			calculateNearest(points, centroids);
    		}
    		for (int i = 0; i < k; i++) {
    			if (centroids[i].getX()%10 != 0) {
    				centroids[i].x = centroids[i].getX()/10*10;
    			}
    			if (centroids[i].getY()%10 != 0) {
    				centroids[i].y = centroids[i].getY()/10*10;
    			}
    		}
    		updateBoard();
    	}
    }
    
    private ActionListener ActionListener() {
		// TODO Auto-generated method stub
		return null;
	}

    
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == cells[0][0]) {
    		cells[0][0].setBackground(Color.white);
    	}
    }

    
    
    public void clearBoard() {
    	pressedStart = false;
    		for (int i = 0; i < size; i++) {
        		for (int j = 0; j < size; j++) {
        			bool[i][j] = false;
        		}
        	}
    	updateBoard();
    }
    
    
    public void mouseClicked (MouseEvent e) {
		//Don't actually need.
	}
    public void mouseMoved(MouseEvent e) {
    	
    }
    
    public void mouseDragged(MouseEvent e) {
    	if (e.getY()/cellSize >= size || e.getX()/cellSize >= size) {
			JOptionPane.showMessageDialog(null, "Error. Click out of bounds.");
		}
    	else if (bool[e.getY()/cellSize][e.getX()/cellSize]) {
			bool[e.getY()/cellSize][e.getX()/cellSize] = false;
			updateBoard();
		}
    	else {
			bool[e.getY()/cellSize][e.getX()/cellSize] = true;
			updateBoard();
		}
    }
	
	public void mousePressed (MouseEvent e) {
		//if (!game.isStarted());
		if (e.getY()/cellSize >= size || e.getX()/cellSize >= size) {
			JOptionPane.showMessageDialog(null, "Error. Click out of bounds.");
		}
		else if (bool[e.getY()/cellSize][e.getX()/cellSize]) {
			bool[e.getY()/cellSize][e.getX()/cellSize] = false;
		}
		else {
			bool[e.getY()/cellSize][e.getX()/cellSize] = true;
		}
		updateBoard();
	}
	
	public void mouseEntered (MouseEvent e) {	
		//Don't actually need.
	}
	
	public void mouseReleased (MouseEvent e) {
		//Don't actually need.
	}
	
	public void mouseExited (MouseEvent e) {	
		//Don't actually need.
	}
	
	public void updateBoard() {
		removeAll();
		revalidate();
		repaint();
	}
	
	//Draws the board.
	public void paint(Graphics g) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (bool[i][j] == true) {
					g.setColor(Color.black);
					g.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
				}
				else {
					g.setColor(c);
					g.fillRect(j*cellSize, i*cellSize, cellSize, cellSize);
				}
				g.setColor(Color.black);
				g.drawRect(j*cellSize, i*cellSize, cellSize, cellSize);
			}
		}
		if (pressedStart) {
			g.setColor(Color.magenta);
			for (int i = 0; i < k; i++) {
				g.fillRect(centroids[i].getX(), centroids[i].getY(), cellSize, cellSize);
			}
			if (k >= 1) {
				g.setColor(Color.red);
				for (int i = 0; i < numPoints; i++) {
					if (points[i].getCluster() == 1) {
						g.fillRect(points[i].getX(), points[i].getY(), cellSize, cellSize);
					}
				}
			}
			if (k >= 2) {
				g.setColor(Color.blue);
				for (int i = 0; i < numPoints; i++) {
					if (points[i].getCluster() == 2) {
						g.fillRect(points[i].getX(), points[i].getY(), cellSize, cellSize);
					}
				}
			}
			if (k >= 3) {
				g.setColor(Color.green);
				for (int i = 0; i < numPoints; i++) {
					if (points[i].getCluster() == 3) {
						g.fillRect(points[i].getX(), points[i].getY(), cellSize, cellSize);
					}
				}
			}
			if (k >= 4) {
				g.setColor(Color.yellow);
				for (int i = 0; i < numPoints; i++) {
					if (points[i].getCluster() == 4) {
						g.fillRect(points[i].getX(), points[i].getY(), cellSize, cellSize);
					}
				}
			}
		}
	}

	// checks board to see if any live cells present; returns a boolean. 
	public boolean boardIsEmpty() {
		boolean isEmpty = true; 	// assume the board is empty 
		
		// iterate through the board and check each cell to see if alive
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++ ) {
				// if cell at board position (i,j) is alive, board is not empty
				if (bool[i][j] == true) {
					isEmpty = false;
				}
			}
		}
		
		return isEmpty; 
	}
	
	public static Point[] createCentroids(Point points[], int numPoints, int k) {
		int min = 0;
		int max = numPoints-1;
		Point[] centroids = new Point[k];
		int rand = (int)(Math.random()*(max-min+1));
		points[rand].setCluster(1);
		points[rand].setCentroid(true);
		centroids[0] = new Point(points[rand].getX(), points[rand].getY());
		for (int i = 0; i < k-1; i++) {
			int furthestPoint = 0;
			double dist = 0;
			for (int j = 0; j < numPoints; j++) {
				if (i == 0) {
					double ac = Math.abs(centroids[0].getY() - points[j].getY());
					double cb = Math.abs(centroids[0].getX() - points[j].getX());
					if (Math.hypot(ac, cb) > dist) {
						dist = Math.hypot(ac, cb);
						furthestPoint = j;
					}
				}
				if (i == 1) {
					int midPointX = (centroids[1].getX() + centroids[0].getX())/2/10*10;
					int midPointY = (centroids[1].getY() + centroids[0].getY())/2/10*10;
					Point mid = new Point(midPointX, midPointY);
					double ac = Math.abs(mid.getY() - points[j].getY());
					double cb = Math.abs(mid.getX() - points[j].getX());
					if (Math.hypot(ac, cb) > dist) {
						dist = Math.hypot(ac, cb);
						furthestPoint = j;
					}
				}
				if (i == 2) {
					int midPointX = (centroids[1].getX() + centroids[0].getX())/2/10*10;
					int midPointY = (centroids[1].getY() + centroids[0].getY())/2/10*10;
					Point mid1 = new Point(midPointX, midPointY);
					midPointX = (centroids[2].getX() + mid1.getX()/2/10*10);
					midPointY = (centroids[2].getY() + mid1.getY()/2/10*10);
					Point mid = new Point(midPointX, midPointY);
					double ac = Math.abs(mid.getY() - points[j].getY());
					double cb = Math.abs(mid.getX() - points[j].getX());
					if (Math.hypot(ac, cb) > dist) {
						dist = Math.hypot(ac, cb);
						furthestPoint = j;
					}
				}
			}
			points[furthestPoint].setCluster(i+2);
			points[furthestPoint].setCentroid(true);
			centroids[i+1] = new Point(points[furthestPoint].getX(), points[furthestPoint].getY());
		}
		return centroids;
	}
	public static void getPoints(boolean bool[][], Point points[]) {
		int x = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (bool[i][j]) {
					points[x] = new Point(j*cellSize, i*cellSize);
					x++;
				}
			}
		}
	}
	public static int getNumPoints(boolean bool[][]) {
		int numPoints = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (bool[i][j]) {
					numPoints++;
				}
			}
		}
		return numPoints;
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
				centroids[i].setX(totalX/(totalPoints+1));
				centroids[i].setY(totalY/(totalPoints+1));
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
}