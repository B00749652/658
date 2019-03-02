package ie.uls.a658;

public class Point {
    private String item;
    private int start,end;

    Point(String word, int start, int end){
        this.start = start;
        this.end = end;
        this.item = word;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String toString(){
        return "#"+ this.item + "," + this.start + "," + this.end;
    }
}
