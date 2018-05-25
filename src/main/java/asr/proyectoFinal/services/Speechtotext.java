package asr.proyectoFinal.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
//import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import asr.proyectoFinal.dominio.Palabra;

import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;

/**
 * Servlet implementation class Controller
 */

@WebServlet(urlPatterns = {"/speechtotext"})
@MultipartConfig
public class Speechtotext extends HttpServlet {
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
    SpeechToText servicest = new SpeechToText();
    servicest.setUsernameAndPassword("11248ba7-c13a-410d-be28-44c1bdfe7719", "IVOVQIA7RqlU");

    Part filePart = request.getPart("audio");
    
    String appPath = request.getServletContext().getRealPath("");
	 // constructs path of the directory to save uploaded file
	 String savePath = appPath + "audio";
	 // creates the save directory if it does not exists
	 File fileSaveDir = new File(savePath);
	 if (!fileSaveDir.exists()) {
	     fileSaveDir.mkdir();
	 }
	 
	 String savedFile = "";
	
	 for (Part part : request.getParts()) {
	         String fileName = extractFileName(part);
	         // refines the fileName in case it is an absolute path
	         fileName = new File(fileName).getName();
	         savedFile=savePath + File.separator + fileName;
	         part.write(savedFile);
	 }
    
	 String ext = savedFile.substring(savedFile.lastIndexOf(".")+1);    
    
    RecognizeOptions options = new RecognizeOptions.Builder()
      .model("en-US_BroadbandModel").contentType("audio/wav")
      .interimResults(true).maxAlternatives(3)
      .keywords(new String[]{"colorado", "tornado", "tornadoes"})
      .keywordsThreshold(0.5).build();
           

    BaseRecognizeCallback callback = new BaseRecognizeCallback() {
      @Override
      public void onTranscription(SpeechResults speechResults) {
        System.out.println(speechResults);
        //transcript = speechResults;
		
      }

      @Override
      public void onDisconnected() {
        System.exit(0);
      }
    };
    String resultado = new String("");
    
    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
    SpeechResults result = servicest.recognize(new File(savedFile), options).execute();
	if(result.getResults().size()>0) {
		 for (int i = 0; i < result.getResults().size() ; i++)  
		 {
			 System.out.println(i);
			 System.out.println(result.getResults().size());
//			 System.out.println(result.getResults().get(i).getAlternatives().get(0).getTranscript());
			 resultado = resultado.concat(result.getResults().get(i).getAlternatives().get(0).getTranscript());
			 System.out.println(resultado);
		 }
	}
		
	
     //SpeechResults speechResults = service.recognize(dest, options).execute();
	
	Palabra res = Analizador.analizador(resultado);
	String res2 = res.toString();
	
	request.setAttribute("sentimiento", res2);  
	request.setAttribute("audio", resultado);  
	
	request.getRequestDispatcher("").forward(request, response);
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