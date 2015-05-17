package utils;

import java.net.*;

public class translate {
    
    private static String[] language_codes = new String[]{"nl", "fr", "zh", "es",
                                                          "hi", "it", "ta", "ru",
                                                          "ar", "de", "ja", "ko",
                                                          "lt", "vi"};
    
    /**
     * Translate inputted text to given language code, using google translate.
     * @param to_translate the text to be translated
     * @param from_language the language code from which it has to be translated
     * @param to_language the language to which it has to be translated
     * @return 
     * @throws java.lang.Exception if it cannot reach the google translate server.
     */
    public static String translate(String to_translate, String from_language, String to_language) throws Exception{
        String page, result, hl, sl, q;
        
        String before_trans = "class=\"t0\">";
        
        //String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String charset = "UTF-8";
        
        try{
            hl = URLEncoder.encode(to_language, charset);
            sl = URLEncoder.encode(from_language, charset);
            q = URLEncoder.encode(to_translate, charset);
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
        
        String query = String.format("https://translate.google.com/m?hl=%s&sl=%s&q=%s", hl,sl,q);
        
        page = URLConnectionReader.getText(query);
        
        result = page.substring(page.indexOf(before_trans)+before_trans.length());
        result = result.split("<")[0];
        return result;
    }
    
    /**
     * Return an array of translated versions of the given text based on predefined
     * language codes.
     * 
     * @param to_translate text to be translated
     * @return an array of translated versions of given text in the order 
     * {Dutch, French, Chinese, Spanish, Hindi, Italian, Tamil, Russian, Arabic,
     * German, Japanese, Korean, Lithuanian, Vietnamese}
     * @throws java.lang.Exception if it cannot reach the google translate server.
     */
    public static String[] translate_all(String to_translate) throws Exception {
        String[] translated = new String[language_codes.length];
        for(int i=0; i<language_codes.length; i++) {
            translated[i] = translate(to_translate, "auto", language_codes[i]);
        }
        return translated;
    }
    
    /**
     * Get the number of language codes defined.
     * @return the number of language codes.
     */
    public static int getNumberOfLanguages() {
        return language_codes.length;
    }
    
    public static void main(String[] args) throws Exception {
        String text = "Hello";
        System.out.println(text+" >> "+translate(text,"auto","zh")); //simple example to see if it works
        System.out.println(text+" >> "+translate(text,"auto","fr"));
    }
}
