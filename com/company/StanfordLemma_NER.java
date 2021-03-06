package com.company;

/**
 * Created by lynn on 10/5/17.
 */
// Acknowledgement: The StanfordLemmatizer code was referenced from https://stackoverflow.com/questions/1578062/lemmatization-java
        import java.util.LinkedList;
        import java.util.List;
        import java.util.ArrayList;
        import java.util.Properties;

        import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
        import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
        import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
        import edu.stanford.nlp.ling.CoreLabel;
        import edu.stanford.nlp.pipeline.Annotation;
        import edu.stanford.nlp.pipeline.StanfordCoreNLP;
        import edu.stanford.nlp.util.CoreMap;
        import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

public class StanfordLemma_NER {

    protected StanfordCoreNLP pipeline;

    ArrayList<String> lemmas = new ArrayList<String>();
    ArrayList<String> ners = new ArrayList<String>();
    ArrayList<String> newText = new ArrayList<String>();

    public StanfordLemma_NER() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma,ner");

        /*
         * This is a pipeline that takes in a string and returns various analyzed linguistic forms.
         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator),
         * and then other sequence model style annotation can be used to add things like lemmas,
         * POS tags, and named entities. These are returned as a list of CoreLabels.
         * Other analysis components build and store parse trees, dependency graphs, etc.
         *
         * This class is designed to apply multiple Annotators to an Annotation.
         * The idea is that you first build up the pipeline by adding Annotators,
         * and then you take the objects you wish to annotate and pass them in and
         * get in return a fully annotated object.
         *
         *  StanfordCoreNLP loads a lot of models, so you probably
         *  only want to do this once per execution
         */
        this.pipeline = new StanfordCoreNLP(props);
    }

    public ArrayList<String> lemma_ner(String documentText){
        lemmatize(documentText);
        ner(documentText);
        convert();
        return newText;
    }

    public void lemmatize(String documentText) {
        //ArrayList<String> lemmas = new ArrayList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
    }

    public void ner(String documentText) {
       // ArrayList<String> ners = new ArrayList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                ners.add(token.get(NamedEntityTagAnnotation.class));
            }
        }
    }

    public void convert(){
        String nerWord = lemmas.get(0);
        for (int i = 1; i < ners.size();i++){
            String word = ners.get(i);
            if(!word.equals("O") && nerWord == "") {
                nerWord = lemmas.get(i);
            }else if (!word.equals("O") && word.equals(ners.get(i-1))){
                nerWord = nerWord + " " + lemmas.get(i);
            }else if(!word.equals("O") && !word.equals(ners.get(i-1))) {
                newText.add(nerWord);
                nerWord = lemmas.get(i);
            }else if (word.equals("O") && nerWord != ""){
                newText.add(nerWord);
                nerWord = "";
                newText.add(lemmas.get(i));
            }else if (word.equals("O") && nerWord == ""){
                newText.add(lemmas.get(i));
            }
        }
        if (nerWord != ""){
            newText.add(nerWord);
        }
    }
/*
    public static void main(String[] args) {
        System.out.println("Starting Stanford Lemmatizer");
        String text = "William Smith you be seeing into United States like open doors? \n"+
                "You the Microsoft Corporation into my core where I've became so numb \n"+
                "Without a soul my spirit's sleeping somewhere cold \n"+
                "Until you find it there and led it back home \n"+
                "You woke me up inside \n"+
                "Called my name and saved me from the dark \n"+
                "You have bidden my blood and it ran \n"+
                "Before I would become undone \n"+
                "You saved me from the nothing I've almost become \n"+
                "You were bringing me to life \n"+
                "Now that I knew what I'm without \n"+
                "You can've just left me \n"+
                "You breathed into me and made me real \n"+
                "Frozen inside without your touch \n"+
                "Without your love, darling \n"+
                "Only you are the life among the dead \n"+
                "I've been living a lie, there's nothing inside \n"+
                "You were bringing me Joe Scott";
        StanfordLemma_NER slem = new StanfordLemma_NER();



        System.out.println("...................");
        System.out.println(slem.lemma_ner(text));

    }
*/
}

