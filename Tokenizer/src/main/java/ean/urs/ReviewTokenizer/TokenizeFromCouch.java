package ean.urs.ReviewTokenizer;

import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.StringReader;

public class TokenizeFromCouch {
    public static void main(String[] args){
        PTBTokenizer ptbt = new PTBTokenizer(new StringReader(""),
                new CoreLabelTokenFactory(), "");
    }
}
