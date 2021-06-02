import java.io.*;
import java.util.*;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.*;

public class akagra {
    static String apikey="-Icg2tjxEzgyz6N8ng6h1py9gPaUZKxVQ5zNThSUjnnY";
    static String version="9.0.2";
    static String url="https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/4b9637b3-5938-4fe1-b629-c2d1bb676a8a";
    public static void main(String args[]){
        String inputfile="C:\\Users\\akagr\\Downloads\\input_1.csv";
        String outputfile="C:\\My Place\\csv\\output.csv";
        convert(inputfile,outputfile);
    }

    public static void convert(String inputfile, String outputfile){
        HashMap<String,String> map= new HashMap<>();
        PrintWriter writer=null;
        Scanner sc =null;
        IamAuthenticator authenticator = new IamAuthenticator(apikey);
        LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
        languageTranslator.setServiceUrl(url);
        Languages languages = languageTranslator.listLanguages().execute().getResult();
        int n=languages.getLanguages().size();
        for(int i=0;i<n;i++){
            map.put(languages.getLanguages().get(i).getLanguage(),languages.getLanguages().get(i).getLanguageName());
        }

//        for(String key:map.keySet()){
//            System.out.println(key+":"+map.get(key));
//        }

        try{
            sc = new Scanner(new File(inputfile));
            writer = new PrintWriter(new File(outputfile));
            StringBuilder row = new StringBuilder();
            row.append("Source"+','+"Target"+','+"Locale"+'\n');
            writer.write(row.toString());
            sc.useDelimiter(",");
            while (sc.hasNext())
            {
                String arr[] = sc.nextLine().split(",");
                if(!arr[0].equals("Source")){
                    TranslateOptions translateOptions = new TranslateOptions.Builder()
                            .addText(arr[0])
                            .target("English")
                            .build();

                    TranslationResult result = languageTranslator.translate(translateOptions)
                            .execute().getResult();

                    row = new StringBuilder();

                    row.append(arr[0]+','+result.getTranslations().get(0).getTranslation()+','+map.get(result.getDetectedLanguage().toString())+'\n');
                    writer.write(row.toString());
                }

            }


        }
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            sc.close();
            writer.close();
        }

    }
}
