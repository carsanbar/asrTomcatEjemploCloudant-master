package asr.proyectoFinal.services;

import java.io.PrintWriter;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;

public class Analizador {
	
	public static Palabra analizador(String text) {
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		
		ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
		service.setUsernameAndPassword("47a6432a-3fbd-41a6-828b-d3b03f387033", "Vq1UCIaSy1xl");
		service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

		    ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
		    ToneAnalysis tone = service.tone(toneOptions).execute();
		    System.out.println(tone);
		    
		    String tonename = tone.getDocumentTone().getTones().get(0).getToneName();
		    System.out.println(tonename);
		    
		    Palabra tonetraducir = new Palabra();
			String parametrotraducir2 = tonename;

			
				
			tonetraducir.setName(Traductor.translate(parametrotraducir2));
			store.persist(tonetraducir);
			return tonetraducir;
		
	}
	
	
}
