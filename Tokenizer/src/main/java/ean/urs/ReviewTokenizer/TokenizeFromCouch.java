package ean.urs.ReviewTokenizer;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.StringReader;
import java.util.List;
import java.util.Properties;

public class TokenizeFromCouch {
    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize");
        props.put("tokenize.options", "americanize=true");

        final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        final String text = " Add your text here!  ";

        // create an empty Annotation just with the given text
        final Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        final List<CoreLabel> sentences = document.get(CoreAnnotations.TokensAnnotation.class);


        final PTBTokenizer tokens = new PTBTokenizer(
                new StringReader("Loads of string stuff"),
                new CoreLabelTokenFactory(), "");
        List<CoreLabel> items = (List<CoreLabel>) tokens;

        if (tokens == null) {

        }
    }
}
