//javac -classpath .;c:\Tomcat\lib\servlet-api.jar;ojdbc6.jar;commons-lang-2.6.jar;commons-logging-1.1.1.jar;hsqldb.jar;jackcess-2.1.2.jar;ucanaccess-3.0.1.jar;commons-fileupload-1.4.jar;commons-io-2.6.jar AndroidUploadServlet.java
// Import required java libraries
import java.util.*;
import java.sql.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.nio.file.Paths; 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class AndroidUploadServlet extends HttpServlet {
   
   private static final String TAG = "AndroidUploadServlet";
   private boolean isMultipart;
   private String filePath = "";
   private String directoryPath = "";
   private int maxFileSize = 50 * 1024 * 1024;
   private int maxMemSize = 4 * 1024 * 1024;
   private File file ;
   private String fileName = "";

   public void init( ){
	  System.out.println(TAG + ": " + "init called");
      // Get the file location where it would be stored.
//      directoryPath = getServletContext().getInitParameter("file-upload"); 
      directoryPath = System.getProperty("catalina.base") + "\\webapps\\servletFileUploader\\uploads\\";
      System.out.println(directoryPath);
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	  System.out.println(TAG + ": " + "doPost called");
	   
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      java.io.PrintWriter out = response.getWriter( );
   
      if( !isMultipart ) {
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
         out.println("<p>No file uploaded</p>"); 
         out.println("</body>");
         out.println("</html>");
         return;
      }
	  
	     DiskFileItemFactory factory = new DiskFileItemFactory();
	   
	     // maximum size that will be stored in memory
	     factory.setSizeThreshold(maxMemSize);
	   
	     // Location to save data that is larger than maxMemSize.
	     factory.setRepository(new File("C:\\tomcat\\temp"));
	
	     // Create a new file upload handler
	     ServletFileUpload upload = new ServletFileUpload(factory);
	   
	     // maximum file size to be uploaded.
	     upload.setSizeMax( maxFileSize );
	
	     try { 
         // Parse the request to get file items.
         List fileItems = upload.parseRequest(request);
	
         // Process the uploaded file items
         Iterator i = fileItems.iterator();

         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
   
         while ( i.hasNext () ) {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () ) {
               // Get the uploaded file parameters
               String fieldName = fi.getFieldName();
               String fileNameI = fi.getName();
               String contentType = fi.getContentType();
               
               int endIndex = fileNameI.length() - 1;
               fileName = fileNameI.substring(1, endIndex); 	//Remove extra quotes

              
               boolean isInMemory = fi.isInMemory();
               long sizeInBytes = fi.getSize();
            
               // Write the file
               if( fileName.lastIndexOf("\\") >= 0 ) {
                  file = new File( directoryPath + fileName.substring(fileName.lastIndexOf("\\"))) ;
               } else {
                  file = new File( directoryPath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
               }
               fi.write( file ) ;
               out.println("Uploaded Filename: " + fileName + "<br>");
               System.out.println("Uploaded Filename: " + fileName);
            }
         }
         out.println("</body>");
         out.println("</html>");
         } catch(Exception ex) {
            System.out.println(ex);
         }
		 
	     
	     	// DATABASE SIDE //
	        float longitude = 0, latitude = 0;
			String caption = "";
			
			
			// .dat file
			boolean isData = fileName.matches("(.*)IMG_[0-9]{5}.dat(.*)");
			
			// Recover the object
			if (isData) {		
		        System.out.println("Loading the object from the storage");
		        ImageData imageData = null;
				System.out.println("filename: " + fileName);
				System.out.println("directoryPath: " + directoryPath);
				String filePath = directoryPath + fileName;
				System.out.println("filePath: " + filePath);
				
		        imageData = (ImageData) Pickle.load(imageData, filePath);
		        System.out.println("caption: "+ imageData.caption + ". timeStamp: " + imageData.timeStamp 
		        		+ ". locationStampLat: " + imageData.locationStampLat + ". locationStampLong: " + imageData.locationStampLong);
				try {
					longitude = Float.parseFloat(imageData.locationStampLong);
				}
				catch (Exception e) {
					longitude = -122.957F;
				}
				
				try {
					latitude = Float.parseFloat(imageData.locationStampLat);
				}
				catch (Exception e) {
					latitude = 49.189F;
				}
				
				try {
					caption = imageData.caption;
				}
				catch (Exception e) {
					caption = "";
				}
			}
			
	     
			try {
				Class.forName("oracle.jdbc.OracleDriver");
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}	
			
	        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle")) {
	             
	            String sql = "INSERT INTO gallery (name, longitude, latitude, caption) VALUES (?, ?, ?, ?)";
	             
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, fileName);
	            preparedStatement.setFloat(2, longitude);
	            preparedStatement.setFloat(3, latitude);
	            preparedStatement.setString(4, caption);
	            int row = preparedStatement.executeUpdate();
	             
	            if (row > 0) {
	                System.out.println("A row has been inserted successfully.");
	            }
	             
	            sql = "SELECT * FROM gallery";
	             
	            Statement statement = connection.createStatement();
	            ResultSet result = statement.executeQuery(sql);
	             
	            while (result.next()) {
	               
	                String fname = result.getString("name");//accesing the database
	                float lon = result.getFloat("longitude");
	                float lat = result.getFloat("latitude");
	                String cap = result.getString("caption");
	                String time = result.getString("ts");
	               /* if( !isMultipart ) {
	                System.out.println(fname + ", " +  lon  + ", " + lat + ", " + cap + ", " +time);
					  } */
	            }
	             
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
/* 	/////////////////////////					
						
			part.write(uploadFilePath + File.separator + fileName);		
						//response.setContentType("text/html");
					//	.append("    <body>\r\n");
				writer.append("File successfully uploaded to " 
					+ uploadFolder.getAbsolutePath() 
					+ File.separator
					+ fileName
					+ "<br>\r\n");
	//			writer.append("<form method=\"POST\" action=\"reset\" ");
				writer.append("<input type=\"reset\" value=\"redo\"/>\r\n");
			  //writer.append("</form>\r\n");
			//	writer.append("    </body>\r\n").append("</html>\r\n"); */
	     
	 
		
							
				
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {
    	 System.out.println(TAG + ": " + "doGet called");

         throw new ServletException("GET method used with " +
            getClass( ).getName( )+": POST method required.");
      }
   
}