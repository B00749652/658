package ie.uls.a658.preferences;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class RefTouchListener implements View.OnTouchListener {



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean answer = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuild = new View.DragShadowBuilder(v);
            ClipData data = ClipData.newPlainText("", "");
            v.startDrag(data, shadowBuild, v, 0);
            answer =true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){

        }

        return answer;

    }//onTouch

}//class