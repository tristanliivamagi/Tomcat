//javac -classpath .;c:\Tomcat\lib\servlet-api.jar;ojdbc6.jar;commons-lang-2.6.jar;commons-logging-1.1.1.jar;hsqldb.jar;jackcess-2.1.2.jar;ucanaccess-3.0.1.jar;commons-io-2.6.jar;commons-fileupload-1.4.jar FileUploadServlet.java

//
/*

CREATE TABLE gallery 
(
name varchar(60),
longitude float(32),
latitude float(32),
caption varchar(100),
ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

*/
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

//@WebServlet(name = "uploadServlet", urlPatterns = { "/upload" }, loadOnStartup = 1)

@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
		maxFileSize = 10485760L, // 10 MB
		maxRequestSize = 20971520L // 20 MB
)
public class FileUploadServlet extends HttpServlet {

    private String filePath;
    private File file ;
   
	private static final long serialVersionUID = 5619951677845873534L;
	
	private static final String UPLOAD_DIR = "uploads";
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("    <head>\r\n")
		.append("        <title>File Upload Form</title>\r\n")
		.append("    </head>\r\n")
		.append("    <body>\r\n");
		writer.append("<h1>Upload file</h1>\r\n");
		writer.append("<form method=\"POST\" action=\"upload\" ")
		.append("enctype=\"multipart/form-data\">\r\n");
		writer.append("<input type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
		
		//writer.append("name:<input type=\"text\" name=\"name\" /><br/>\r\n");
		writer.append("latitude:<input type=\"number\" step=\"any\" name=\"latitude\" /><br/>\r\n");
		writer.append("longitude:<input type=\"number\" step=\"any\" name=\"longitude\" /><br/>\r\n");
        writer.append("caption:<input type=\"text\" name=\"caption\" /><br/>\r\n");
		
		writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		writer.append("</form>\r\n");
		writer.append("    </body>\r\n").append("</html>\r\n");
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)

			throws ServletException, IOException {
				response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("    <head>\r\n")
		.append("        <title>File Upload Form</title>\r\n")
		.append("    </head>\r\n")
		.append("    <body>\r\n");
		writer.append("<h1>Upload file</h1>\r\n");
		writer.append("<form method=\"POST\" action=\"upload\" ")
		.append("enctype=\"multipart/form-data\">\r\n");
		writer.append("<input type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
		
		//writer.append("name:<input type=\"text\" name=\"name\" /><br/>\r\n");
		writer.append("latitude:<input type=\"number\" step=\"any\" name=\"latitude\" /><br/>\r\n");
		writer.append("longitude:<input type=\"number\" step=\"any\" name=\"longitude\" /><br/>\r\n");
        writer.append("caption:<input type=\"text\" name=\"caption\" /><br/>\r\n");
		
		writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		writer.append("</form>\r\n");
		writer.append("    </body>\r\n").append("</html>\r\n");
				
				 
		// Check that we have a file upload request
		//isMultipart = ServletFileUpload.isMultipartContent(request);
				
		String fileName=null;
		response.setContentType("text/html");
		
		java.io.PrintWriter out = response.getWriter( );
		 
		response.setCharacterEncoding("UTF-8");
		

		////////////////////////	
		// gets absolute path of the web application
		String applicationPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		// creates upload folder if it does not exists
		File uploadFolder = new File(uploadFilePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		//PrintWriter writer = response.getWriter();
		// write all files in upload folder
		for (Part part : request.getParts()) {
			if (part != null && part.getSize() > 0) {
				fileName = part.getSubmittedFileName();
				String contentType = part.getContentType();
				
				// allows only JPEG files to be uploaded
				if (!contentType.equalsIgnoreCase("image/jpeg")) {
					continue;
					//need to redirect the user
				}

				
			
					
				// DATABASE
//////////////////////////////////

		//String name = request.getParameter("name");

        float longitude, latitude;
		String caption;
		
		//if no locations are given
		try {
			longitude = Float.parseFloat(request.getParameter("longitude"));
			System.out.println(longitude);
		}
		catch (Exception e) {
			longitude = -123;
			System.out.println("Threw error, default: " + longitude);
		}
		try {
			latitude = Float.parseFloat(request.getParameter("latitude"));
			System.out.println(latitude);
		}
		catch (Exception e) {
			latitude = 49;
			System.out.println("Threw error, default: " + latitude);
		}
		
		// if no caption given
		caption = request.getParameter("caption");
		System.out.println(caption + "i got the caption");
		if(caption == null || caption.length() == 0 || caption == ""){
			caption = " ";
			System.out.println("Threw error, default: " + caption);
			}
			
	/* 	}
		catch (Exception e) {
			caption = " ";
		} */

	        //String databaseURL = "jdbc:ucanaccess://c://Tomcat//webapps//storage//Contacts.accdb";
			//String databaseURL = "jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle";
         	
		
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("tried Class.forName for oracle");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			System.out.println("error at oracle");
		}			
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "oracle")) {
             
             
            String sql = "INSERT INTO gallery (name, longitude, latitude, caption) VALUES (?, ?, ?, ?)";
            System.out.println(sql);
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
            System.out.println(statement);
            System.out.println(result);
            while (result.next()) {
                
                String fname = result.getString("name");//accesing the database
				float lon = result.getFloat("longitude");
				float lat = result.getFloat("latitude");
                String cap = result.getString("caption");
                String time = result.getString("ts");
                System.out.println(fname + ", " +  lon  + ", " + lat + ", " + cap + ", " +time);
            }
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
/////////////////////////					
					
			part.write(uploadFilePath + File.separator + fileName);
				
				
						
						//response.setContentType("text/html");
					//	.append("    <body>\r\n");
						writer.append("File successfully uploaded to " 
						+ uploadFolder.getAbsolutePath() 
						+ File.separator
						+ fileName
						+ "<br>\r\n");
					//			writer.append("<form method=\"POST\" action=\"reset\" ");
					//writer.append("<input type=\"reset\" value=\"redo\"/>\r\n");
					//writer.append("</form>\r\n");
				//	writer.append("    </body>\r\n").append("</html>\r\n");
	
						
			}
		}
////////////////////////////////////


	}
}