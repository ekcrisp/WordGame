import roombacomm.*;
import java.lang.*;
import java.util.Random;

public class robotController {
  
  static //Tribble tribble = new Tribble();
  RoombaCommSerial roombacomm = new RoombaCommSerial();
  static String roombacommPort = "/dev/ttyS0";
  Random random = new Random();
 
 int robotMood = 0;
 
 // positive mood thresholds
 int h1 = 4;
 int h2 = 8;
 int h3 = 12;
 int h4 = 16;
 int h5 = 20;
 
 // negative mood threshold
 int s1 = -4;
 int s2 = -8;
 int s3 = -12;
 int s4 = -16;
 int s5 = -20;
 
 public void h1MoodAction(){
   //Happy
   
   roombacomm.drive(5,5); //fast-ish
   // roombacomm.Drive serialportname 100 -1 waittime
   
   if (random.nextFloat() > .5) {
  // tribble.purr();
   }
 }
 
 public void h2MoodAction(){
   //Elated
   
   //roombacomm.waggle();
   
   if (random.nextFloat() > .5) {
   roombacomm.playNote(91, 50);
   roombacomm.playNote(96, 150);
   }
   
   if (random.nextFloat() > .5) {
  // tribble.createTribblePurrSong();
   }
 }
 
 public void h3MoodAction(){
  
 }
 
 public void h4MoodAction(){
  
 }
 
 public void h5MoodAction(){
  
 }
 
 public void s1MoodAction(){
  
 }
 
 public void s2MoodAction(){
  
 }
 
 public void s3MoodAction(){
   //Depressed
   
    if (random.nextFloat() > .4) {
      roombacomm.drive(2,2); //slowly
   }
  
    if (random.nextFloat() > .3) {
   roombacomm.playNote(45, 100);
   roombacomm.playNote(43, 100);
   roombacomm.playNote(42, 100);
   roombacomm.playNote(41, 100);
   }
  
 }
 
 public void s4MoodAction(){
   //The robot hates its life...
   
   if (random.nextFloat() > .1) {
     roombacomm.playNote(38, 200);
     roombacomm.pause(100);
     roombacomm.playNote(36, 200);
   }
   
   if (random.nextFloat() > .1) {
     roombacomm.drive(1,1); //very small increments, slowly
   }
 }
 
 public void s5MoodAction(){
   
 
 }
 

 /**
  * @param args
  */
 public static void main(String[] args) {
	 
	 roombacomm.connect(roombacommPort);


 }

}
