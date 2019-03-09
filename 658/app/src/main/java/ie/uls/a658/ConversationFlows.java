package ie.uls.a658;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import ie.uls.a658.auxiliary.DAO;
import ie.uls.a658.preferences.Score;

public class ConversationFlows {
    // Mapping of User / Domain Model to Logic Layer
    // Structured as a 16 point conversation
    private Context context;
    private Score score = Score.getScore();
    private String greeting, farewell;

    ConversationFlows(Context contxt){
        this.context = contxt;
    }

    protected String getLevel1(String domain){
        getFormalities(score.getDomain());
        return greeting;
    }

    protected String getLevel2(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }

    protected String getLevel3(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }
    protected String getLevel4(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }
    protected String getLevel5(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }
    protected String getLevel6(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }
    protected String getLevel7(String domain){
        String reply = String.format("Tiger, Tiger burning bright in the forests of the night");
        return reply;
    }
    protected String getLevel8(String domain){
        return farewell;
    }


    private void getFormalities(String domain){
        DAO dao = new DAO(context,"ContentDeliverySystem.db",null,1);
        String formalquery = String.format("SELECT greeting, farewell FROM Domain WHERE domain = '%s'",domain);
        Cursor cursor = dao.getMessages(formalquery);
        if(cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                this.greeting = cursor.getString(cursor.getColumnIndex("greeting"));
                this.farewell = cursor.getString(cursor.getColumnIndex("farewell"));
                cursor.move(1);
            }
        }
        cursor.close();
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
                moodText = "-Happy, counting your years by your smiles";
                break;
        }

        String startTxt = String.format("My friend %s is,\n %s \n %s",score.getUserName(),companyText,moodText);
        return startTxt;
    }


}
