package ie.uls.a658;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Locale;

import ie.uls.a658.preferences.Score;

public class RatingsActivity extends AppCompatActivity {
    private static Score score = Score.getScore();
    TextView tviewratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        Bundle extra = getIntent().getExtras();
        int num = score.getCounter();
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        float max = (float) (num > 10.0 ? num : 10.0);
        int calc = (int)(num/max*4.0);
        ratingBar.setNumStars(calc);

        tviewratings = findViewById(R.id.tviewratings);
        String ratings = String.format(Locale.ENGLISH,"Estimated Star Rating %d",calc);
        tviewratings.setText(ratings);

    }
}
