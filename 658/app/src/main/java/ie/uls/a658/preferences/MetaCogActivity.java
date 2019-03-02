package ie.uls.a658.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ie.uls.a658.MessagingActivity;
import ie.uls.a658.R;
import ie.uls.a658.RatingsActivity;


public class MetaCogActivity extends AppCompatActivity {
    private List<String> textattrib = new ArrayList<>();
    private Set<Integer> indices = new HashSet<>();
    TextView attribute1, attribute2, attribute3, attribute4, attribute5, attribute6, attribute7, attribute8;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognition);
        attribute1 = findViewById(R.id.attribute1);
        attribute2 = findViewById(R.id.attribute2);
        attribute3 = findViewById(R.id.attribute3);
        attribute4 = findViewById(R.id.attribute4);
        attribute5 = findViewById(R.id.attribute5);
        attribute6 = findViewById(R.id.attribute6);
        attribute7 = findViewById(R.id.attribute7);
        attribute8 = findViewById(R.id.attribute8);


        /* Attribute Text Source to be finalised*/

        Collections.addAll(textattrib,"help others","digs in","goes out","has a dog","monopoliser","aloner","45's player","scrounger","athlete","rounded");
        Collections.shuffle(textattrib);


        Iterator<Integer> it = indices.iterator();
        attribute1.setText(textattrib.get(0));
        attribute2.setText(textattrib.get(1));
        attribute3.setText(textattrib.get(2));
        attribute4.setText(textattrib.get(3));
        attribute5.setText(textattrib.get(4));
        attribute6.setText(textattrib.get(5));
        attribute7.setText(textattrib.get(6));
        attribute8.setText(textattrib.get(7));


        attribute1.setOnTouchListener(new RefTouchListener());
        attribute2.setOnTouchListener(new RefTouchListener());
        attribute3.setOnTouchListener(new RefTouchListener());
        attribute4.setOnTouchListener(new RefTouchListener());
        attribute5.setOnTouchListener(new RefTouchListener());
        attribute6.setOnTouchListener(new RefTouchListener());
        attribute7.setOnTouchListener(new RefTouchListener());
        attribute8.setOnTouchListener(new RefTouchListener());


        RelativeLayout card1 =  findViewById(R.id.card1txt);
        RelativeLayout card2 =  findViewById(R.id.card2txt);
        RelativeLayout card3 =  findViewById(R.id.card3txt);
        RelativeLayout card4 =  findViewById(R.id.card4txt);

        card1.setOnDragListener(new RefDragListener());
        card2.setOnDragListener(new RefDragListener());
        card3.setOnDragListener(new RefDragListener());
        card4.setOnDragListener(new RefDragListener());


    }

    public void onGoClick(View view){
        Intent intent = new Intent(this, MessagingActivity.class);
        startActivity(intent);
    }




}
