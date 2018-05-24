package asr.proyectoFinal.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice.*;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectFacesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;
import asr.proyectoFinal.services.Traductor;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/hablar", "/traducir", "/analyzer", "/voice", "/text", "/visual"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<html><head><meta charset=\"UTF-8\"></head><body>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":
				if(store.getDB() == null)
					  out.println("No hay DB");
				else
					out.println("Palabras en la BD Cloudant:<br />" + store.getAll());
				break;
				
			case "/analyzer":
				ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
				service.setUsernameAndPassword("47a6432a-3fbd-41a6-828b-d3b03f387033", "Vq1UCIaSy1xl");

				String text = "Team, I know that times are tough! Product sales have "
				    + "been disappointing for the past three quarters. We have a "
				    + "competitive product, but we need to do a better job of selling it!";

				    ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
				    ToneAnalysis tone = service.tone(toneOptions).execute();
				    System.out.println(tone);
				break;
				
			case "/visual":
				System.out.println("1");
				VisualRecognition servicevi = new VisualRecognition("2018-03-19");
				System.out.println("2");
				servicevi.setApiKey("27fff8ca8df4ffc25eba09bf6a07d328093c7be8");
				System.out.println("3");

				DetectFacesOptions detectFacesOptions = new DetectFacesOptions.Builder()
				  .imagesFile(new File("C:\\Users\\Carlos\\Desktop\\obamasface.jpg"))
				  .build();
				System.out.println("4");
				DetectedFaces result = servicevi.detectFaces(detectFacesOptions).execute();
				System.out.println("5");
				System.out.println(result);
				
				
				break;
				
				
//			case "/text":
//				SpeechToText servicet = new SpeechToText();
//				servicet.setUsernameAndPassword("11248ba7-c13a-410d-be28-44c1bdfe7719", "IVOVQIA7RqlU");
//
//				RecognizeOptions options = new RecognizeOptions.Builder()
//				  .contentType("audio/wav").timestamps(true)
//				  .wordAlternativesThreshold(0.9)
//				  .keywords(new String[]{"colorado", "tornado", "tornadoes"})
//				  .keywordsThreshold(0.5).build();
//
//				String[] files = {"C:\\Users\\Carlos\\Downloads\\g3.wav"};
//				for (String file : files) {
//				  SpeechResults results = servicet.recognize(new File(file), options).execute();
//				  System.out.println(results);
//				}
//				break;
				
			case "/voice":
//				TextToSpeech servicev = new TextToSpeech();
//				servicev.setUsernameAndPassword("1b818e6f-3e24-46d1-ac50-2898ec58f20e", "KDyURtkMMkG5");
//
//				try {
//				  String texto = "Hello world";
//				  //InputStream stream = servicev.synthesize(texto, Voice.EN_ALLISON,AudioFormat.WAV).execute();
////				  SynthesizeOptions option = new SynthesizeOptions(texto,Accept.AUDIO_WAV,"es-ES_LauraVoice","a");
////				  InputStream stream = servicev.synthesize();
//				  InputStream in = WaveUtils.reWriteWaveHeader(stream);
//				  OutputStream outv = new FileOutputStream("hello_world.wav");
//				  byte[] buffer = new byte[1024];
//				  int length;
//				  while ((length = in.read(buffer)) > 0) {
//					  outv.write(buffer, 0, length);
//				  }
//				  outv.close();
//				  in.close();
//				  stream.close();
//				}
//				catch (Exception e) {
//				  e.printStackTrace();
//				}
//				break;
//				
				
			case "/traducir":
				Palabra palabratraducir = new Palabra();
				String parametrotraducir = request.getParameter("palabra");

				if(parametrotraducir==null)
				{
					out.println("usage: /insertar?palabra=palabra_a_traducir");
				}
				else
				{
					if(store.getDB() == null) 
					{
						out.println(String.format("Palabra: %s", palabratraducir));
					}
					else
					{
						palabratraducir.setName(Traductor.translate(parametrotraducir));
						store.persist(palabratraducir);
					    out.println(String.format("Almacenada la palabra: %s", palabratraducir.getName()));			    	  
					}
				}
				break;
				
			case "/insertar":
				Palabra palabra = new Palabra();
				String parametro = request.getParameter("palabra");

				if(parametro==null)
				{
					out.println("usage: /insertar?palabra=palabra_a_traducir");
				}
				else
				{
					if(store.getDB() == null) 
					{
						out.println(String.format("Palabra: %s", palabra));
					}
					else
					{
						palabra.setName(parametro);
						store.persist(palabra);
					    out.println(String.format("Almacenada la palabra: %s", palabra.getName()));			    	  
					}
				}
				break;
		}
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
