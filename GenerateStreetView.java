package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

public class GenerateStreetView {
	private String apiKey=""; // MUST PUT AN API KEY HERE FOR THIS TO WORK
	private boolean allDirections = false; //1 image or 3 next to eachother
	private int defaultHeading = 0; //0 is north

	// i have provided the 50 most populus cities in the US as examples to work with.
	private static double nyc[] = {40.7128, -74.0060}; //index 0
	private static double la[] = {34.0522, -118.2437};
	private static double chicago[] = {41.8781,-87.6298};
	private static double houston[] = {29.7604, -95.3698};
	private static double philly[] = {39.9526, -75.1652};
	private static double phoenix[] = {33.4484,-112.0740};
	private static double sanAntonio[] = {29.4241,-98.4936};
	private static double sanDiego[] = {32.7157, -117.1611};
	private static double dallas[] = {32.7767,-96.7970};
	private static double sanJose[] =  {37.3382, -121.8863};
	private static double austin[] =  {30.2672,-97.7431}; //index 10
	private static double jacksonville[] =  {30.3322,-81.6557};
	private static double sanFrancisco[] =  {37.7749, -122.4194};
	private static double indianapolis[] =  {39.7684,-86.1581};
	private static double columbus[] =  {39.9612,-82.9988};
	private static double fortWorth[] =  {32.7555,-97.3308};
	private static double charlotte[] =  {35.2271,-80.8431}; 
	private static double seattle[] =  {47.6062,-122.3321}; 
	private static double denver[] =  {39.7392,-104.9903};
	private static double elPaso[] =  {31.7619,-106.4850};
	private static double detroit[] =  {42.3314,-83.0458}; //index 20
	private static double washington[] =  {38.9072,-77.0369};//left off here, start with index 21
	private static double boston[] =  {42.3601,-71.0589}; //left off here. start with index 22
	private static double memphis[] =  {35.1495,-90.0490};
	private static double nashville[] =  {36.1627,-86.7816};
	private static double portland[] =  {45.5122,-122.6587};
	private static double okc[] =  {35.4676,-97.5164};
	private static double vegas[] =  {36.1699,-115.1398};
	private static double baltimore[] =  {39.2904,-76.6122};
	private static double louisville[] =  {38.2527,-85.7585};
	private static double milwaukee[] =  {43.0389,-87.9065};//index 30
	private static double albuquerque[] =  {35.0844,-106.6504};
	private static double tuscon[] =  {32.2226,-110.9747};
	private static double fresno[] =  {36.7378,-119.7871};
	private static double sacramento[] =  {38.5816,-121.4944};
	private static double kansasCity[] =  {39.0997,-94.5786};
	private static double longBeach[] =  {33.7701,-118.1937};
	private static double mesa[] =  {33.4152,-111.8315};
	private static double atlanta[] =  {33.7490,-84.3880};
	private static double coloradoSprings[] =  {38.8339,-104.8214};
	private static double virginiaBeach[] =  {36.8529,-75.9780};//index 40
	private static double raleigh[] =  {35.7796,-78.6382};
	private static double omaha[] =  {41.2565, 95.9345};
	private static double maimi[] =  {25.7617,-80.1918};
	private static double oakland[] =  {37.8044,-122.2711};
	private static double minneapolis[] =  {44.9778,-93.2650};
	private static double tulsa[] =  {36.1540,-95.9928};
	private static double wichita[] =  {37.6872,-97.3301};
	private static double newOrleans[] =  {29.9511,-90.0715};
	private static double arlington[] =  {32.7357,-97.1081}; //index 49//suburb of dallas?
	
	private static double allCities[][] ={nyc,la,chicago,houston,philly,phoenix,sanAntonio,sanDiego,dallas,sanJose,
										austin,jacksonville,sanFrancisco,indianapolis, columbus,fortWorth,charlotte,seattle,denver,elPaso,
										detroit,washington,boston,memphis,nashville,portland,okc,vegas,baltimore,louisville,
										milwaukee,albuquerque,tuscon, fresno,sacramento,kansasCity,longBeach,mesa,atlanta,coloradoSprings,
										virginiaBeach,raleigh,omaha,maimi,oakland,minneapolis,tulsa,wichita,newOrleans,arlington};
	private static String names[];
	
	
	
	
	
	private static double meterInGPS=0.00001/1.1;
	private static double mileInGPS=1604*meterInGPS;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		names = new String[] {"NewYorkCity" , "LosAngeles","Chicago","Houston","Philadelphia","Phoenix","SanAntonio","SanDiego","Dallas","SanJose",
				"Austin","Jacksonville","SanFrancisco","Indianapolis","Columbus","FortWorth","Charlotte","Seattle","Denver","ElPaso",
				"Detroit","WashingtonDC","Boston","Memphis","Nashville","Portland","OklahomaCity","LasVegas","Baltimore","Louisville",
				"Milwaukee","Albuquerque","Tuscon","Fresno","Sacramento","KansasCity","LongBeach","Mesa","Atlanta","ColoradoSprings",
				"VirginiaBeach","Raleigh","Omaha","Maimi","Oakland","Minneapolis","Tulsa","Wichita","NewOrleans","Arlington"};
			GenerateStreetView gsv = new GenerateStreetView();
			try {
				for(int i =22;i<allCities.length;i++) {
					new File("src/"+names[i]).mkdir();
					gsv.makeRepeated(allCities[i][0],allCities[i][1],mileInGPS*2,mileInGPS*2,300,names[i]);
				}
				//gsv.makeRepeated(sanDiego[0],sanDiego[1], mileInGPS*1.5, mileInGPS*1.5, 100, "SanDiego");
				//gsv.generateImage(43.035648, -87.924266);
				//gsv.generateImage(43.097716, -87.533667);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private int height=640;
	private int width = 480;
	public boolean generateImage(double lat, double lon, String destinationFile, int heading) {
		//String imageUrl = "http://maps.googleapis.com/maps/api/streetview?size=640x480&location=43.040761,-87.917311&fov=120&key=&heading=235&pitch=10&sensor=false";
		
		String url ="http://maps.googleapis.com/maps/api/streetview?size="+height+"x"+width+"&location="+lat+","+lon+"&fov=120&key="+apiKey+"&heading="+(heading%360)+"&pitch=10&sensor=false";
		URL imageUrl;
		try {
			imageUrl = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		//String destinationFile = "src/image"+lat+","+lon+".jpg";
	       BufferedImage img;
		try {
			img = ImageIO.read(imageUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
	       if(-1776673!=img.getRGB(10,10) && allDirections) {
	    	   System.out.println(url);
	    	   url ="http://maps.googleapis.com/maps/api/streetview?size="+height+"x"+width+"&location="+lat+","+lon+"&fov=120&key="+apiKey+"&heading="+((heading+120)%360)+"&pitch=10&sensor=false";
	    	   try {
				imageUrl = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				return false;
			}
	    	   BufferedImage img2;
			try {
				img2 = ImageIO.read(imageUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
	    	   url ="http://maps.googleapis.com/maps/api/streetview?size="+height+"x"+width+"&location="+lat+","+lon+"&fov=120&key="+apiKey+"&heading="+((heading+240)%360)+"&pitch=10&sensor=false";
	    	   try {
				imageUrl = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				return false;
			}
	    	   BufferedImage img3;
			try {
				img3 = ImageIO.read(imageUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
	    	   BufferedImage tempMerged;
               tempMerged = new BufferedImage(3*img.getWidth(),img.getHeight(),img.getType());
               Graphics2D g2 = tempMerged.createGraphics();
               g2.drawImage(img,null,0,0);
               g2.drawImage(img2,null,img.getWidth(),0);
               g2.drawImage(img3,null,img.getWidth()+img2.getWidth(),0);
               File file = new File(destinationFile);
               System.out.println(destinationFile);
   	        	try {
					ImageIO.write(tempMerged, "jpg", file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return false;
				}
   	        	return true;
	       }
	       else if(-1776673!=img.getRGB(10,10)) {
	    	   BufferedImage tempMerged;
               tempMerged = new BufferedImage(img.getWidth(),img.getHeight(),img.getType());
               Graphics2D g2 = tempMerged.createGraphics();
               g2.drawImage(img,null,0,0);
               File file = new File(destinationFile);
               System.out.println(destinationFile);
   	        	try {
					ImageIO.write(tempMerged, "jpg", file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return false;
				}
   	        	return true;
	       }
	       return false;
	        
	}
	private void makeRepeated(double latCenter, double lonCenter, double width, double height, int total, String baseFile) throws IOException {
		Random rand = new Random();
		int i=0;
		while(i<total) {
			double tempLat = latCenter + width*rand.nextDouble()-width/2;
			double tempLon = lonCenter + height*rand.nextDouble()-height/2;
			String tempString = "src/"+baseFile+"/"+i+".png";
			if(generateImage(tempLat,tempLon,tempString,defaultHeading)) {
				i++;
			}
		}
		
	}

}
