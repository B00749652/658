package ie.uls;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import opennlp.tools.util.InvalidFormatException;


@WebServlet(urlPatterns="/nlp")
public class NlpServer extends HttpServlet {

private  Set<String> word; 

private  Set<String> word2; 
    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
      
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws InvalidFormatException, ServletException, IOException{
        JSONObject reply = partOfSpeech(request);
        request.setAttribute("answer", reply);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws InvalidFormatException, ServletException, IOException{
        JSONObject reply = partOfSpeech(request);
        request.setAttribute("answer", reply);
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }

    private JSONObject partOfSpeech(HttpServletRequest request){
        word = null; word2 =null;
        word = new HashSet<>();
        word2= new HashSet<>();
        String text = request.getParameter("msg");
        text.replace("%20"," ");
        text.replace("%22"," ");

        TextChk scan = new TextChk(text);
        for(String s : scan.getNouns()){
            this.word.add(s);
        }
        for(String ss: scan.getVerbs()){
            this.word2.add(ss);
        }
        JSONObject jo = new JSONObject();
        jo.put("nn",this.word.toString());
        jo.put("vb",this.word2.toString());
        return jo;
    }

}