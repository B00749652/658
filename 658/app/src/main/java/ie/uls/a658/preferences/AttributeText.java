package ie.uls.a658.preferences;


public class AttributeText {
    private String agreement, attribute, text;

    AttributeText(String attribute, String text,String agreement){
        this.agreement = agreement;
        this.attribute = attribute;
        this.text = text;
    }

    protected String getAttribute(){
        return this.attribute;
    }

    protected String getText() {
        return this.text;
    }

    protected String getAgreement(){
        return this.agreement;
    }



}
