package asr.proyectoFinal.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.util.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions.Builder;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
public class Traductor
{
	public static String translate(String palabra)
	{
		LanguageTranslator service = new LanguageTranslator();
		service.setUsernameAndPassword("8be2928e-8016-402f-9bfe-ea58cb752f27", "0SPALgHeCebt");
		
		TranslateOptions translateOptions = new
		TranslateOptions.Builder()
		.addText(palabra)
		.modelId("en-es")
		.build();
		
		TranslationResult translationResult =
		service.translate(translateOptions).execute();
		System.out.println(translationResult);
		String traduccionJSON = translationResult.toString();
		JsonParser parser = new JsonParser();
		JsonObject rootObj =
		parser.parse(traduccionJSON).getAsJsonObject();
		
		String wordCount = rootObj.get("word_count").getAsString();
		JsonArray traducciones = rootObj.getAsJsonArray("translations");
		String traduccionPrimera = palabra;
		if(traducciones.size()>0) {
			System.out.println(traducciones.size());
			traduccionPrimera = traducciones.get(0).getAsJsonObject().get("translation").getAsString();
			System.out.println(traduccionPrimera);
		}
		return traduccionPrimera;
	}
}