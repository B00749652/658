package ie.uls;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.InvalidFormatException;

/**
 * Hello world!
 *
 */
public class TextChk 
{

    private static List<String> verbs = new ArrayList<>(), nouns = new ArrayList<>();
    
public TextChk(){}

public TextChk(String input){
     try{tagAndTokenize(input);}
     catch(InvalidFormatException ij){ij.printStackTrace();}
     catch(IOException ev){ev.printStackTrace();}

}


    public static List<String> getVerbs(){
        return verbs;
    }

    public static List<String> getNouns(){
        return nouns;
    }



    public static String stopwordremoval(String input) {
            List<String> stoplist = new ArrayList<>();
            StringBuilder sb = new StringBuilder(input.length());
            Path file = Paths.get("english_stop.txt");

            InputStream in = (TextChk.class).getResourceAsStream(file.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            try{
            while(reader.readLine()!= null){
                    stoplist.add(reader.readLine());
                }
                StringTokenizer st = new StringTokenizer(input, " ,.-:\'");
                while(st.hasMoreTokens()){
                    String checkToken = st.nextToken().toLowerCase();
                    if(!(stoplist.contains(checkToken))){
                        sb.append(checkToken).append(" ");
                    }
                }
            }catch(IOException e){e.printStackTrace();}

            return sb.toString();
        }//stopwordremovalMethod



    public static void tagAndTokenize(String input) throws InvalidFormatException, IOException{
        String sample = stopwordremoval(input);
        SimpleTokenizer tokeniser = SimpleTokenizer.INSTANCE;
        String[] tokens = tokeniser.tokenize(sample);
        Path file = Paths.get("en-pos-maxent.bin");
        
        InputStream in = (TextChk.class).getResourceAsStream(file.toString());
        
        String[] tags = new String[tokens.length];
        try {
            POSModel model = new POSModel(in);
            POSTaggerME posTagger = new POSTaggerME(model);
            tags = posTagger.tag(tokens);
            
        for (int loc = 0; loc < tags.length; loc++) {
            if (tags[loc].contains("VB")) {
                verbs.add(tokens[loc]);
            }
            if (tags[loc].contains("NN")) {
                nouns.add(tokens[loc]);
            }
            in.close();
        }
    }catch(IOException io){io.printStackTrace();}
    }//tagAndTokenize Method

    public static void main(String[] args){
        try{
        
        if(args.length > 0){
        tagAndTokenize(args[0]);
    }else{
        tagAndTokenize("The Big Brown Fox Jumped over the lazy dog");
    }

}catch(Exception er ){er.printStackTrace();}

    System.out.println((getVerbs().toString()));

    System.out.println((getNouns().toString()));
    
}


}
