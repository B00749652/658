package ie.uls.a658.preferences;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ie.uls.a658.R;


public class RefDragListener implements View.OnDragListener {
    private static int cardcounter1 =0, cardcounter2 =0,cardcounter3=0,cardcounter4=0;
    private static Score score  = Score.getScore();
//    private static List<String> card1content = new ArrayList<>();
//    private static List<String> card2content = new ArrayList<>();
//    private static List<String> card3content = new ArrayList<>();
//    private static List<String> card4content = new ArrayList<>();


    @Override
    public boolean onDrag(View v, DragEvent event) {

        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_ENTERED:
                /*Do Something*/
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                /*Do Something*/
                return true;
            case DragEvent.ACTION_DRAG_ENDED:

                return true;
            case DragEvent.ACTION_DROP:
                TextView dropped = (TextView) event.getLocalState();
                RelativeLayout dropee = (RelativeLayout) v;
                if (dropee != null && ((dropee.getParent()) != null) && (dropped != null) &&(dropped.getParent() != null)) {

                    /* Update Counter to tally Score and Check for a full card */

                    switch(dropee.getId()){
                        case R.id.card1txt:
                            if(cardcounter1 > 3){return false;}
                            cardcounter1++;
                            score.setCardcounter1(dropped.getText().toString());
                            break;
                        case R.id.card2txt:
                            if(cardcounter2 > 3){return false;}
                            cardcounter2++;
                            score.setCardcounter2(dropped.getText().toString());
                            break;
                        case R.id.card3txt:
                            if(cardcounter3 > 3){return false;}
                            cardcounter3++;
                            score.setCardcounter3(dropped.getText().toString());
                            break;
                        case R.id.card4txt:
                            if(cardcounter4 > 3){return false;}
                            cardcounter4++;
                            score.setCardcounter4(dropped.getText().toString());
                            break;
                    }

                    /* Determine Where View is Dragged */

                    ViewGroup vg = (ViewGroup) dropped.getParent();
                    TextView temp = dropped;
                    vg.removeView(dropped);
                    ViewGroup vg2 = (ViewGroup) dropee.getParent();

                    /* Transform View to Text String */

                    String stuff = dropped.getText().toString();
                    TextView txt = (TextView) dropee.getChildAt(0);
                    String oldStuff = txt.getText().toString();
                    txt.setText(oldStuff + "\n - " + stuff);

//                    switch (oldStuff.charAt(0)) {
//                        case 'L':
//                            card1content.add(stuff);
//                            break;
//                        case 'W':
//                            card2content.add(stuff);
//                            break;
//                        case 'A':
//                            card3content.add(stuff);
//                            break;
//                        case 'N':
//                            card4content.add(stuff);
//                            break;
//                        default:
//                            break;
//                    }

                    return true;
                } else {
                    return false;
                }


        }
        return true;
    }

}//onDrag





