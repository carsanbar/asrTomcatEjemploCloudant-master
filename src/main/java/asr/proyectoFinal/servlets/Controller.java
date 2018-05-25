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
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
//import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
//import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

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
				service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

				String text = "Team, I know that times are tough! Product sales have "
				    + "been disappointing for the past three quarters."
				    + "I am happy";

				    ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
				    ToneAnalysis tone = service.tone(toneOptions).execute();
				    System.out.println(tone);
//				
				
				break;
				
			case "/visual":
//				System.out.println("1");
//				VisualRecognition servicevi = new VisualRecognition("2018-03-19");
//				System.out.println("2");
//				servicevi.setApiKey("27fff8ca8df4ffc25eba09bf6a07d328093c7be8");
//				System.out.println("3");
//
//				DetectFacesOptions detectFacesOptions = new DetectFacesOptions.Builder()
//				  .imagesFile(new File("C:\\Users\\Carlos\\Desktop\\obamasface.jpg"))
//				  .build();
//				System.out.println("4");
//				DetectedFaces result = servicevi.detectFaces(detectFacesOptions).execute();
//				System.out.println("5");
//				System.out.println(result);
				
				
				break;
				
			case "/visual2":
//				VisualRecognition servicevi2 = new VisualRecognition("2018-03-19");
//				servicevi2.setApiKey("{api-key}");
//
//				InputStream imagesStream = new FileInputStream("./fruitbowl.jpg");
//				ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
//				  .imagesFile(imagesStream)
//				  .imagesFilename("fruitbowl.jpg")
//				  .threshold((float) 0.6)
//				  .owners(Arrays.asList("me"))
//				  .build();
//				ServiceCall result2 = servicevi2.classify(classifyOptions).execute();
//				System.out.println(result2);
				break;
				
				
			case "/text":
				SpeechToText servicest = new SpeechToText();
				servicest.setUsernameAndPassword("11248ba7-c13a-410d-be28-44c1bdfe7719", "IVOVQIA7RqlU");

				RecognizeOptions options = new RecognizeOptions.Builder()
				  .model("en-US_BroadbandModel").contentType("audio/wav")
				  .interimResults(true).maxAlternatives(3)
				  .keywords(new String[]{"colorado", "tornado", "tornadoes"})
				  .keywordsThreshold(0.5).build();

				BaseRecognizeCallback callback = new BaseRecognizeCallback() {
				  @Override
				  public void onTranscription(SpeechResults speechResults) {
				    System.out.println(speechResults);
				  }

				  @Override
				  public void onDisconnected() {
				    System.exit(0);
				  }
				};

				try {
					servicest.recognizeUsingWebSocket
				    (new FileInputStream("C:\\Users\\Carlos\\Downloads\\g6.wav"), options, callback);
				}
				catch (FileNotFoundException e) {
				  e.printStackTrace();
				}
				
				break;
				
			case "/voice":
				TextToSpeech servicevoi = new TextToSpeech();
				servicevoi.setUsernameAndPassword("1b818e6f-3e24-46d1-ac50-2898ec58f20e", "KDyURtkMMkG5");

				try {
				  String textovo = "Hello world";
				  InputStream stream = servicevoi.synthesize(textovo, Voice.ES_LAURA,AudioFormat.WAV).execute();
				  InputStream in = WaveUtils.reWriteWaveHeader(stream);
				  OutputStream out3 = new FileOutputStream("hello_world.wav");
				  byte[] buffer = new byte[1024];
				  int length;
				  while ((length = in.read(buffer)) > 0) {
					  out3.write(buffer, 0, length);
				  }
				  out3.close();
				  in.close();
				  stream.close();
				}
				catch (Exception e) {
				  e.printStackTrace();
				}
				
				
				
//				TextToSpeech service = new TextToSpeech();
//			    service.setUsernameAndPassword("1b818e6f-3e24-46d1-ac50-2898ec58f20e", "KDyURtkMMkG5");
//
//			    if(request.getParameter("language").equals("en")){
//
//			    String path = this.getServletContext().getRealPath("/")+"/Translate.wav";
//			    File yourFile = new File(path);
//			    yourFile.createNewFile(); // if file already exists will do nothing 
//			      try {
//			          String text = request.getParameter("palabra");
//			          InputStream stream = service.synthesize(text, Voice.EN_ALLISON,AudioFormat.WAV).execute();
//			          InputStream in = WaveUtils.reWriteWaveHeader(stream);
//			          response.setContentType("audio/wav");
//			          response.setHeader("Content-Disposition", "attachment;filename=Translate.wav");
//			          response.setHeader("Pragma", "private");
//			          response.setHeader("Cache-Control", "private, must-revalidate");
//			          response.setHeader("Accept-Ranges", "bytes");
//			          response.setContentLength((int) yourFile.length());
//			          OutputStream output = response.getOutputStream();
//
//			          byte[] buffer = new byte[1024];
//			          int length;
//			          while ((length = in.read(buffer)) > 0) {
//			            output.write(buffer, 0, length);
//			          }
//			          output.close();
//			          in.close();
//			          stream.close();
//			        }
//			        catch (Exception e) {
//			          e.printStackTrace();
//			        }
//			      }else if(request.getParameter("language").equals("fr")){
//
//			          String path = this.getServletContext().getRealPath("/")+"/Traduction.wav";
//			          File yourFile = new File(path);
//			          yourFile.createNewFile(); // if file already exists will do nothing 
//			          try {
//			            String text = request.getParameter("palabra");
//			            InputStream stream = service.synthesize(text, Voice.FR_RENEE,AudioFormat.WAV).execute();
//			            InputStream in = WaveUtils.reWriteWaveHeader(stream);
//			            response.setContentType("audio/wav");
//			            response.setHeader("Content-Disposition", "attachment;filename=Traduction.wav");
//			            response.setHeader("Pragma", "private");
//			            response.setHeader("Cache-Control", "private, must-revalidate");
//			            response.setHeader("Accept-Ranges", "bytes");
//			            response.setContentLength((int) yourFile.length());
//			            OutputStream output = response.getOutputStream();
//
//
//			            byte[] buffer = new byte[1024];
//			            int length;
//			            while ((length = in.read(buffer)) > 0) {
//			              output.write(buffer, 0, length);
//			            }
//			            output.close();
//			            in.close();
//			            stream.close();
//			          }
//			          catch (Exception e) {
//			            e.printStackTrace();
//			          }
//			      }else if(request.getParameter("language").equals("es")){
//			          String path = this.getServletContext().getRealPath("/")+"/Traduccion.wav";
//			          File yourFile = new File(path);
//			          yourFile.createNewFile(); // if file already exists will do nothing 
//			          try {
//			          String text = request.getParameter("palabra");
//			          InputStream stream = service.synthesize(text, Voice.ES_LAURA,AudioFormat.WAV).execute();
//			          InputStream in = WaveUtils.reWriteWaveHeader(stream);
//			          response.setContentType("audio/wav");
//			          response.setHeader("Content-Disposition", "attachment;filename=Traduccion.wav");
//			          response.setHeader("Pragma", "private");
//			          response.setHeader("Cache-Control", "private, must-revalidate");
//			          response.setHeader("Accept-Ranges", "bytes");
//			          response.setContentLength((int) yourFile.length());
//			          OutputStream output = response.getOutputStream();
//
//
//			          byte[] buffer = new byte[1024];
//			          int length;
//			          while ((length = in.read(buffer)) > 0) {
//			            output.write(buffer, 0, length);
//			          }
//			          output.close();
//			          in.close();
//			          stream.close();
//			        }
//			        catch (Exception e) {
//			          e.printStackTrace();
//			        }
//			      }
				
				//Irene
				
//				TextToSpeech serviceIre = new TextToSpeech();
//				serviceIre.setUsernameAndPassword("1b818e6f-3e24-46d1-ac50-2898ec58f20e", "KDyURtkMMkG5");
//						
//				String audioName="";
//				try {
//					  String textIre = "hola";
//					  InputStream in = serviceIre.synthesize(textIre, Voice.FR_RENEE, AudioFormat.WAV).execute();
//					  audioName = "palabra_traducida.webm";
//					  String appPath = request.getServletContext().getRealPath("");
//					  String savepath = appPath + "audio";
//					  System.out.println("PATH: "+savepath+"/"+audioName);
//					  OutputStream outIre = new FileOutputStream(savepath+File.separator+audioName);
//					  byte[] buffer = new byte[1024];
//					  int length;
//					  while ((length = in.read(buffer)) > 0) {
//						  outIre.write(buffer, 0, length);
//					  }
//					  outIre.close();
//					  in.close();
//					}
//					catch (Exception e) {
//					  e.printStackTrace();
//					}
//				
//				String audioIre = "audio/"+audioName; //out tiene que ser el audio resultado
//				System.out.println(audioIre);
//				//return audioIre;
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
