package ean.urs.ReviewTokenizer;

import ean.ReviewInfoParser;
import ean.db.ReviewInfo;
import ean.util.CouchDbUtil;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

public class TokenizeFromCouch {
    private static int QUERY_LIMIT = 100;
    private static int MAX_ITERATIONS = 20;

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize");
        props.put("tokenize.options", "americanize=true");

        final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        ReviewInfoParser reviewService = new ReviewInfoParser();
        CouchDbUtil dbUtil = new CouchDbUtil(false);
        LinkedHashMap<Integer, ReviewInfo> reviews = null;
        int iteration = 0;
        StringBuilder fullReviewSb = new StringBuilder();
        int previousHotelId = 0;
        do {
            reviews = reviewService.getCouchDbResponses(dbUtil, QUERY_LIMIT, null, null, iteration * QUERY_LIMIT);
            for (ReviewInfo reviewInfo : reviews.values()) {
                if (previousHotelId == 0) {
                    previousHotelId = reviewInfo.getHotelId();
                }

                if (previousHotelId != reviewInfo.getHotelId()) {
                    // string is built go out and do the tokenization
                    ExtractTokens(pipeline, fullReviewSb, previousHotelId);


                    fullReviewSb = new StringBuilder();
                    previousHotelId = reviewInfo.getHotelId();
                }
                if (StringUtils.isNotBlank(reviewInfo.getTitle())
                        && StringUtils.isNotBlank(reviewInfo.getReviewText())
                        && reviewInfo.getLocale().toLowerCase().startsWith("en")) {
                    fullReviewSb.append(reviewInfo.getTitle().replace("/t", " ").replace("/n", " "));
                    fullReviewSb.append("-");
                    fullReviewSb.append(reviewInfo.getReviewText().replace("/t", " ").replace("/n", " "));
                }
            }
            iteration++;
        } while (reviews != null && !reviews.isEmpty() );//&& iteration < MAX_ITERATIONS);


//
//        final PTBTokenizer tokens = new PTBTokenizer(
//                new StringReader("Loads of string stuff"),
//                new CoreLabelTokenFactory(), "");
//        List<CoreLabel> items = (List<CoreLabel>) tokens;
//
//        if (tokens == null) {
//
//        }
    }

    private static void ExtractTokens(StanfordCoreNLP pipeline, StringBuilder fullReviewSb, Integer hotelId) {
        // create an empty Annotation just with the given text
        final Annotation document = new Annotation(fullReviewSb.toString());

        // run all Annotators on this text
        pipeline.annotate(document);

        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        final List<CoreLabel> tokens = document.get(CoreAnnotations.TokensAnnotation.class);
        final StringBuilder sb = new StringBuilder();
        sb.append(hotelId);
        sb.append(" ");
        sb.append(hotelId);
        sb.append(" ");
        for (CoreLabel item : tokens) {
            final String value = item.get(CoreAnnotations.ValueAnnotation.class);
            if (StringUtils.isAlpha(value) && StringUtils.length(value) > 2) {
                sb.append(value);
                sb.append(" ");
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());

    }
}
