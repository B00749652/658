package ie.uls.a658.preferences;

public class Score {
    private int cardcounter1 =0,cardcounter2 =0, cardcounter3 =0, cardcounter4=0;
    private int counter = 0;
    private static Score score = new Score();

    public synchronized static Score getScore(){
        /* Singleton Constructor*/
        return score;
    }

    private Score(){
        /* Empty Constructor*/
    }

    private void updateCounter(){
        int boost =2;
        counter = 2*boost*cardcounter1 + boost*cardcounter2 - cardcounter3 -2*cardcounter4;
    }
    /*Getters and Setters*/

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter += counter;
        updateCounter();
    }

    public int getCardcounter1() {
        return cardcounter1;
    }

    public void setCardcounter1(int cardcounter1) {
        this.cardcounter1 += cardcounter1;
        updateCounter();
    }

    public int getCardcounter2() {
        return cardcounter2;
    }

    public void setCardcounter2(int cardcounter2) {
        this.cardcounter2 += cardcounter2;
        updateCounter();
    }

    public int getCardcounter3() {
        return cardcounter3;
    }

    public void setCardcounter3(int cardcounter3) {
        this.cardcounter3 += cardcounter3;
        updateCounter();
    }

    public int getCardcounter4() {
        return cardcounter4;
    }

    public void setCardcounter4(int cardcounter4) {
        this.cardcounter4 += cardcounter4;
        updateCounter();
    }
}
