package ie.uls.a658.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Score {
    private static List<AttributeText> attributes = new ArrayList<>();
    private int SocialScore =0, moodScore =0, socialMax =0, moodMax =0;
    private final String SOCIAL = "Social";
    private final String MOOD = "Mood";
    private final String POS = "Positive";
    private final String NEG = "Negative";
    private static Score score = new Score();
    private String userName = "example";
    private String domain = "Alternative";

    public synchronized static Score getScore(){
        /* Singleton Constructor*/
        return score;
    }


    private Score(){
        Collections.addAll(attributes,
                new AttributeText(SOCIAL,"help others",POS),
                new AttributeText(SOCIAL,"digs in",POS),
                new AttributeText(SOCIAL,"45's player",POS),
                new AttributeText(SOCIAL,"aloner",NEG),
                new AttributeText(SOCIAL,"scrounger",NEG),
                new AttributeText(SOCIAL,"wallflower",NEG),
                new AttributeText(MOOD,"has a dog",POS),
                new AttributeText(MOOD,"athlete",POS),
                new AttributeText(MOOD,"rounded",POS),
                new AttributeText(MOOD,"red mist",NEG),
                new AttributeText(MOOD,"monopoliser",NEG),
                new AttributeText(MOOD,"overworked",NEG));

    }

    static List<AttributeText> getAttributes(){
        return attributes;
    }


    /*Getters and Setters*/
    public String getDomain(){return this.domain;}
    public void setDomain(String domain){this.domain = domain;}
    public String getUserName(){return this.userName;}
    public void setUserName(String name){this.userName = name;}
    public String getSocialScore() {
        String result = "?";
        int temp = (SocialScore > 0) ?((int) Math.min((SocialScore * socialMax / 3),3)) : 1;
        switch(temp) {
            case 1:
                result = "Solo";
                break;
            case 2:
                result = "Duo";
                break;
            case 3:
                result = "Trio";
                break;
        }
        return result;
    }



    public String getMoodScore() {
        String result = "?";
        int temp = moodScore > 0 ? ((int) Math.min((moodScore * moodMax / 3),3)) : 1;
        switch(temp) {
            case 1:
                result = "Sad";
                break;
            case 2:
                result = "Indifferent";
                break;
            case 3:
                result = "Happy";
                break;
        }
        return result;
    }
    
    private void updateScores(int card,String text){
        for(AttributeText at: attributes) {
            if (at.getText().equals(text)) {
                if (at.getAttribute().equals(SOCIAL)) {
                    socialMax += 1;
                    if (card <= 2) {
                        SocialScore = (at.getAgreement().equals(POS)) ? SocialScore + 1 : SocialScore;
                        break;
                    } else {
                        SocialScore = (at.getAgreement().equals(NEG)) ? SocialScore + 1 : SocialScore;
                        break;
                    }
                } else {
                    moodMax += 1;
                    if (card < 3) {
                        moodScore = (at.getAgreement().equals(POS)) ? moodScore + 1 : moodScore;
                        break;
                    } else {
                        moodScore = (at.getAgreement().equals(NEG)) ? moodScore+1 : moodScore ;
                        break;
                    }
                }
            }
        }
    }//updateScoresMethod


    void setCardcounter1(String text) {
        updateScores(1,text);
    }

    void setCardcounter2(String text) {
        updateScores(2,text);
    }

    void setCardcounter3(String text) {
        updateScores(3,text);
    }

    void setCardcounter4(String text) {
        updateScores(4,text);
    }
}
