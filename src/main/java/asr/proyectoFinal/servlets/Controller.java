package asr.proyectoFinal.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
//import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
//import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
//import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions.Tone;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;
import asr.proyectoFinal.services.Traductor;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/traducir", "/analyzer"})
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
				if(store.getDB() == null) {
					  out.println("No hay DB");
				}else {
					Collection<Palabra> palabrasalmacenadas = store.getAll();
					request.setAttribute("palabrasalmacenadas", palabrasalmacenadas);  
					request.getRequestDispatcher("").forward(request, response);
				}
				break;
				
			case "/analyzer":
				ToneAnalyzer service = new ToneAnalyzer("2017-09-21");
				service.setUsernameAndPassword("47a6432a-3fbd-41a6-828b-d3b03f387033", "Vq1UCIaSy1xl");
				service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

				String text = "Hello my name is John how are you im very fine thank you Are you working in pwc";

				    ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
				    ToneAnalysis tone = service.tone(toneOptions).execute();
				    System.out.println(tone);
				    
				    String tonename = tone.getDocumentTone().getTones().get(0).getToneName();
				    System.out.println(tonename);
				    
				    Palabra tonetraducir = new Palabra();
					String parametrotraducir2 = tonename;

					if(parametrotraducir2==null)
					{
						out.println("No hay nada que traducir");
					}
					else
					{
						if(store.getDB() == null) 
						{
							out.println(String.format("Palabra: %s", tonetraducir));
						}
						else
						{
							tonetraducir.setName(Traductor.translate(parametrotraducir2));
							store.persist(tonetraducir);
						    out.println(String.format("Almacenada la palabra: %s", tonetraducir.getName()));			    	  
						}
					}
				    
				    request.setAttribute("tonetraducido", tonetraducir);  
					request.getRequestDispatcher("").forward(request, response);
				  
	    
				break;
						
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
//					    out.println(String.format("Almacenada la palabra: %s", palabratraducir.getName()));			    	  
						 request.setAttribute("palabratraducida", palabratraducir);  
						request.getRequestDispatcher("").forward(request, response);
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
						 request.setAttribute("palabraaañadir", palabra);  
							request.getRequestDispatcher("").forward(request, response);		    	  
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
	private String extractFileName(Part part) {
	      String contentDisp = part.getHeader("content-disposition");
	      String[] items = contentDisp.split(";");
	      for (String s : items) {
	          if (s.trim().startsWith("filename")) {
	              return s.substring(s.indexOf("=") + 2, s.length()-1);
	          }
	      }
	      return "";
	  }

}
