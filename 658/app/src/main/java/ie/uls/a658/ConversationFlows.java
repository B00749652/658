package ie.uls.a658;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ie.uls.a658.auxiliaryObjects.Content;
import ie.uls.a658.auxiliaryObjects.ContentDeliverySystem;
import ie.uls.a658.auxiliaryObjects.ContentQuery;
import ie.uls.a658.auxiliaryObjects.DatabaseAccessObject;
import ie.uls.a658.auxiliaryObjects.Domain;
import ie.uls.a658.auxiliaryObjects.DomainandCategory;
import ie.uls.a658.auxiliaryObjects.EateryDublin;
import ie.uls.a658.preferences.Score;

class ConversationFlows {
    // Mapping of User / Domain Model to Logic Layer
    // Structured as a 16 point conversation
    private Context context;
    private Score score = Score.getScore();
    private double lat, lng;
    private String greeting, farewell, cat = "That's nice ";
    private DatabaseAccessObject dao;
    String[] typeOfEstablishment = {"'Fast Food'","'Restaurant'","'Hotel'","'Bar'","'Coffee Shop'"};
    private GPSInfoFinder gpsinfo = new GPSInfoFinder();
    ConversationFlows(Context contxt){
        this.context = contxt;
    }

    protected String getLevelOne(String domain){
        getFormalities(domain);
        return greeting;
    }

    protected String getLevelTwo(String domain){
              getContentText(2, domain);
        return cat;
    }

    protected String getLevelThree(String domain){
             getContentText(3,domain);
        return cat;
    }

    protected String getLevelFour(String domain){
            getContentText(4,domain );
        return cat;
    }

    protected String getLevelFive(String domain){
            getContentText(5,domain );
        return cat;
    }
    protected String getLevelSix(String domain){

            getContentText(6,domain );
        return cat;
    }

    protected String getLevelSeven(String domain){
            getContentText(7,domain );
        return cat;
    }
    protected String getLevelEight(String domain){
        return this.farewell;
    }

    protected void setLat(double lat){this.lat = lat;}
    protected void setLng(double lng){this.lng = lng;}
    protected double getLat(){return this.lat;}
    protected double getLng(){return this.lng;}

    private void getContentText(int level, String choice) {
        List<String> relevantContent = null;
        dao = ContentDeliverySystem.getInstance(context).dao();
        List<String> answer = dao.getAll(choice, level);
        relevantContent = answer.isEmpty() ? null : dao.getContent(answer.get(0));

        if (relevantContent != null && relevantContent.size() != 0) {
            int random = (int) (Math.random() * relevantContent.size());
            this.cat = relevantContent.get(random);
        } else {
            getNearbyPlacesInfo();
            /* Or take in an API Call Response Here*/
        }
    }

    private void getNearbyPlacesInfo(){
        // Method selects a random eatery type to
        // query database with current GPS location identifying cluster
        // returns a random entry on that matching list
        int randomnum =  new Random().nextInt(typeOfEstablishment.length);
        List<EateryDublin> result = dao.getEaterys(typeOfEstablishment[randomnum], gpsinfo.getNearestCluster(lat,lng));
        int randomnum2 = new Random().nextInt(result.size());
        String suffix;
        if (result.size() != 0) {
            if (typeOfEstablishment[randomnum].equalsIgnoreCase("'coffee shop'")) {
                suffix = "\nWhy not have go in and have another cuppa? ";
            } else {
                suffix = "\nWhy not have bite to eat for some " + result.get(randomnum2).getType().toLowerCase() +" grub?";
            }
            this.cat = "Have you tried " + result.get(randomnum2).getEstablishment() +
                    "\nIt is nearby at " + result.get(randomnum2).getAddress() + suffix;
        } else {
            this.cat = "That's nice ";
        }
    }



    private void getFormalities(String domain) {
        dao = ContentDeliverySystem.getInstance(context).dao();
        Domain dom = dao.getFormalities(1)[0];
        this.greeting = dom.getGreeting();
        this.farewell = dom.getFarewell();
    }



    protected LinearLayout getHeader(Context context){
        LinearLayout horizLayout = new LinearLayout(context);
        horizLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView picture = new ImageView(context);
        picture.setMaxWidth(50);
        picture.setMaxHeight(50);
        String username = score.getUserName();
        if(username == null){username = "example";}
        try{
            File file = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String user = username + ".png";
            File photo = new File(file,user);
            Bitmap image = BitmapFactory.decodeFile(photo.getAbsolutePath());
            picture.setImageBitmap(image);
        }
        catch(NullPointerException ex){ex.printStackTrace();}

        TextView textView = new TextView(context);
        textView.setText(getstartTxt());

        horizLayout.addView(picture);
        horizLayout.addView(textView);

        return horizLayout;

    }


    protected String getstartTxt(){
        String usermood = score.getMoodScore();
        String userCompany = score.getSocialScore();
        String companyText = "?", moodText = "?";
        switch (userCompany){
            case "Solo":
                companyText = "-Solo, unique in every way";
                break;
            case "Duo":
                companyText = "-one half of a good thing";
                break;
            case "Trio":
                companyText = "-In company, gathering crowds are thunderous";
                break;
        }
        switch (usermood){
            case "Sad":
                moodText = "-Sad, some broken hearts never mend";
                break;
            case "Indifferent":
                moodText = "-Indifferent, and very polite to strangers";
                break;
            case "Happy":
                moodText = "-Happy, counting the years by your smiles";
                break;
        }

        String startTxt = String.format("My friend %s is,\n %s \n %s",score.getUserName(),companyText,moodText);
        return startTxt;
    }


}
