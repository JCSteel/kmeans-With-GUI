package kmeans;

public class Point {
	int x;
	int y;
	int cluster;
	boolean isCentroid;
	double distanceFromNearest;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		isCentroid = false;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getCluster() {
		return cluster;
	}
	public void setCluster(int newCluster) {
		cluster = newCluster;
	}
	public void setCentroid(boolean isCentroid) {
		this.isCentroid = isCentroid;
	}
	public boolean getIsCentroid() {
		return isCentroid;
	}
	public void setDistance(double dist) {
		distanceFromNearest = dist;
	}
	public double getDistance() {
		return distanceFromNearest;
	}
}
