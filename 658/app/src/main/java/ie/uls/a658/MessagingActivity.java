package ie.uls.a658;

import android.content.Context;
import android.graphics.Color;
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
    private int ladderRung = 1;
    private Context context;
    private Score score = Score.getScore();
    private static JSONObject webResponse = new JSONObject();
    private String inputText;
    private String domain;
    private ConversationFlows conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_messaging);
        conversation = new ConversationFlows(this.context);
        this.domain = score.getDomain();
        LinearLayout linearLayout = findViewById(R.id.preamble);
        LinearLayout horizLayout = conversation.getHeader(context);

        linearLayout.addView(horizLayout);

    }//onCreate Method



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
        rating.setOnClickListener(response -> rating.setImageResource(R.drawable.smallbtnexp));

        TextView replytextView = new TextView(context);
        replytextView.setPadding(10,10,10,10);
        String reply = "?";

        if(!(this.domain.equals(score.getDomain()))){
            ladderRung = 2;
            this.domain = score.getDomain();
        }
        switch(ladderRung){
            case 1:
                reply = conversation.getLevel1(score.getDomain());
                ladderRung++;
                break;
            case 2:
                reply = conversation.getLevel2(score.getDomain());
                ladderRung++;
                break;
            case 3:
                reply = conversation.getLevel3(score.getDomain());
                ladderRung++;
                break;
            case 4:
                reply = conversation.getLevel4(score.getDomain());
                ladderRung++;
                break;
            case 5:
                reply = conversation.getLevel5(score.getDomain());
                ladderRung++;
                break;
            case 6:
                reply = conversation.getLevel6(score.getDomain());
                ladderRung++;
                break;
            case 7:
                reply = conversation.getLevel7(score.getDomain());
                ladderRung++;
                break;
            case 8:
                reply = conversation.getLevel8(score.getDomain());
                ladderRung++;
                break;
            case 9:
                ladderRung = 0;
                finish();
        }
        replytextView.setText(reply);

        horizreplylayout.addView(rating);
        horizreplylayout.addView(replytextView);

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
