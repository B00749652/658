package ie.uls.a658;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import ie.uls.a658.preferences.Score;

public class MessagingActivity extends AppCompatActivity {
    private static int ladderRung = 1;
    private Context context;
    private Score score = Score.getScore();
    private static JSONObject webResponse = new JSONObject();
    private String inputText;
    private String reply;
    private ConversationFlows conversation;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private static int domainIndex = 0;
    private String[] domains = {"Alternative","Competition","Compliment","Recurring","Sated","Unlimited","Urgent"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_messaging);
        conversation = new ConversationFlows(this.context);

        /* To be replaced with API Call
        to establish ontological domain*/
        //score.setDomain(getAPIDomainSelection(score.getMoodScore(),score.getSocialScore))
        score.setDomain(domains[domainIndex]);

        LinearLayout linearLayout = findViewById(R.id.preamble);
        LinearLayout horizLayout = conversation.getHeader(context);
        linearLayout.addView(horizLayout);
        getGPS();
        }//onCreate Method



    protected void getGPS(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null) {
                    conversation.setLat(location.getLatitude());
                    conversation.setLng(location.getLongitude());
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        }catch(SecurityException se){se.printStackTrace();}

    }//GetGPS Method



    public void onClickText(View view) {
        EditText editinput = findViewById(R.id.inputtextedit);
        String text = String.format(Locale.ENGLISH, "%s", editinput.getText());
        String sampleText = "What the hammer? what the chain,\n" +
                " In what furnace was thy brain?\n" +
                " What the anvil? what dread grasp,\n" +
                " Dare its deadly terrors clasp!";

        inputText = editinput.getText().toString();
        if(inputText.length() < 2) {
            inputText = sampleText;
        }
            sendRequest(inputText);
            editinput.setText("");
    }//onClick Text Method




    protected SpannableStringBuilder formatString(String input, JSONObject webModifier){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(input);
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();

        String nn = "",vb="";
        int nncolour = Color.GREEN;
        int vbcolour = Color.RED;
        try {
            nn = webModifier.getString("nn");
            vb = webModifier.getString("vb");

            StringTokenizer stringTokenizer = new StringTokenizer(nn,",[]");
            while(stringTokenizer.hasMoreTokens()){
                nouns.add(stringTokenizer.nextToken());
            }
            StringTokenizer stringTokenizer2 = new StringTokenizer(vb,",[]");
            while(stringTokenizer2.hasMoreTokens()){
                verbs.add(stringTokenizer2.nextToken());
            }

        }catch(JSONException je){je.printStackTrace();}

        int counter = 0;
        List<Point> mylist = new ArrayList<>();
        while(counter < input.length()) {
            String word = "";
            while (word.length() < 2) {
                StringBuilder sb = new StringBuilder(15);
                while (counter < input.length() && !(("!?,. ").contains(String.format(Locale.ENGLISH, "%c", input.charAt(counter))))) {
                    sb.append(input.charAt(counter));
                    counter++;
                }
                word = sb.toString();
                int wordlength = word.length();
                if (wordlength > 2) {
                    mylist.add(new Point(word, (counter - wordlength), counter));
                }
                counter++;
            }
        }

        List<Point> myotherlist = new ArrayList<>();
        List<Point> myotherotherlist = new ArrayList<>();

        for(int pos = 0;  pos < mylist.size(); pos++){
            if(nouns.toString().contains(mylist.get(pos).getItem().toLowerCase())) {
                myotherlist.add(mylist.get(pos));
            }
        }
        for(int pos = 0;  pos < mylist.size(); pos++){
            for(int loc = 0; loc < verbs.size(); loc++){
                if (verbs.get(loc).equalsIgnoreCase(mylist.get(pos).getItem().toLowerCase())) {
                    myotherotherlist.add(mylist.get(pos));
                }
            }
        }
        for(Point p : myotherotherlist){
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED),p.getStart(),p.getEnd(),Spannable.SPAN_COMPOSING);
        }
        for(Point p : myotherlist){
            spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.GREEN),p.getStart(),p.getEnd(),Spannable.SPAN_COMPOSING);
        }

      return spannableStringBuilder;
    }//format String Method with NLP Data



    protected void sendRequest(String text){
        final String ADDRESS = "http://10.0.2.2:8080/nlpserver/nlp?";
        RequestQueue webAppQueue = Volley.newRequestQueue(context);
        String escapedText = text.replace(" ", "%20");
        escapedText = escapedText.replace("\"","%22");
        escapedText = escapedText.replace(":","%3A");
        escapedText = escapedText.replace("\'","%27");
        escapedText = escapedText.replace("?","%3F");
        escapedText = escapedText.replace("!","%21");

        /* Volley Request & Response */
        String msg = ADDRESS + "msg=" + escapedText;
        JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.POST,msg ,null, response -> {
           webResponse = response;
            SpannableStringBuilder ftext = formatString(inputText, webResponse);
            updateScroller(ftext);
        }, error -> { });
        webAppQueue.add(jRequest);

    }//sendRequst Method to WebServer APP



    private LinearLayout getResponse(LinearLayout linearLayout){
        LinearLayout horizreplylayout = new LinearLayout(context);
        horizreplylayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton rating = new ImageButton(context);
        rating.setBackgroundColor(Color.WHITE);
        rating.setImageResource(R.drawable.smallbtn);
        rating.setScaleX(0.8f);
        rating.setScaleY(0.8f);
        rating.setScaleType(ImageView.ScaleType.CENTER_CROP);
        rating.setScaleType(ImageView.ScaleType.CENTER);
        rating.setOnClickListener(response -> {
            rating.setImageResource(R.drawable.smallbtnexp);
            domainIndex++;
            if(domainIndex < 6){score.setDomain(domains[domainIndex]);
            ladderRung =1;
            }else{domainIndex = 0;
                score.setDomain(domains[0]);
                ladderRung =1;
            }
        });

        TextView replytextView = new TextView(context);
        replytextView.setPadding(10,10,10,10);


//        if(!(this.domain.equals(score.getDomain()))){
//            ladderRung = 2;
//            score.setDomain(this.domain);
//        }
        synchronized (new Thread()) {
            switch (ladderRung) {
                case 1:
                    new Thread(() -> reply = conversation.getLevelOne(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 2:
                    if(conversation.getLat() != 0){locationManager.removeUpdates(locationListener);}
                    new Thread(() -> reply = conversation.getLevelTwo(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 3:
                    if(conversation.getLat() != 0){locationManager.removeUpdates(locationListener);}
                    new Thread(() -> reply = conversation.getLevelThree(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 4:
                    new Thread(() -> reply = conversation.getLevelFour(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 5:
                    new Thread(() -> reply = conversation.getLevelFive(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 6:
                    new Thread(() -> reply = conversation.getLevelSix(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 7:
                    new Thread(() -> reply = conversation.getLevelSeven(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                case 8:
                    new Thread(() -> reply = conversation.getLevelEight(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    break;
                default:
                    new Thread(() -> reply = conversation.getLevelOne(score.getDomain())).start();
                    try{Thread.sleep(200);}catch(InterruptedException e){e.printStackTrace();}
                    ladderRung = 1;
                    break;
            }
            replytextView.setText(reply);
            horizreplylayout.addView(rating);
            horizreplylayout.addView(replytextView);
            ladderRung++;
        }
        return horizreplylayout;
    }


    private void updateScroller(SpannableStringBuilder ftext){
        ScrollView scrollView = findViewById(R.id.scroll);
        LinearLayout linearLayout = findViewById(R.id.scrolllinlayout);

        TextView textView = new TextView(context);
        textView.setText(ftext, TextView.BufferType.SPANNABLE);
        textView.setGravity(Gravity.START);
        textView.setPadding(10,10,10,10);
        linearLayout.addView(textView);

        LinearLayout horizreplylayout = getResponse(linearLayout);

        ImageView div = new ImageView(context);
        div.setImageResource(R.drawable.divider);
        div.setScaleY(2);
        div.setBackgroundColor(Color.BLUE);

        linearLayout.addView(horizreplylayout);
        linearLayout.addView(div);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


}//Messaging Activity
