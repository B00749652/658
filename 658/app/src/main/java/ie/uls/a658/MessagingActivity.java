package ie.uls.a658;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
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


public class MessagingActivity extends AppCompatActivity {
    private Context context;
    private static JSONObject webResponse = new JSONObject();
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        setContentView(R.layout.activity_messaging);

    }//onCreate Method



    public void onClickText(View view) {
        EditText input = findViewById(R.id.editinput);
        String text = String.format(Locale.ENGLISH, "%s", input.getText());

        String sampleText = "What the hammer? what the chain,\n" +
                " In what furnace was thy brain?\n" +
                " What the anvil? what dread grasp,\n" +
                " Dare its deadly terrors clasp!";

        inputText = input.getText().toString();
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


    protected String parseJson(){
        /* To be written */
        return " " ;
    }



    protected void sendRequest(String text){
        TextView output = findViewById(R.id.MsgTxt);
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
            output.setText(ftext, TextView.BufferType.SPANNABLE);
        }, error -> { });
        webAppQueue.add(jRequest);

    }//sendRequst Method to WebServer APP



}//Messaging Activity
