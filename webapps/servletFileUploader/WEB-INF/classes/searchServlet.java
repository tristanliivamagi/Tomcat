//Javac -classpath .;C:\Tomcat\lib\servlet-api.jar;ojdbc6.jar;commons-lang-2.6.jar;commons-logging-1.1.1.jar;hsqldb.jar;jackcess-2.1.2.jar;ucanaccess-3.0.1.jar searchServlet.java

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


import java.sql.*;
import java.io.*;
import javax.servlet.*;  
import javax.servlet.http.*;  

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
@MultipartConfig(fileSizeThreshold = 6291456, // 6 MB
		maxFileSize = 10485760L, // 10 MB
		maxRequestSize = 20971520L // 20 MB
)
public class searchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5619951677845873534L;
	
	private static final String UPLOAD_DIR = "uploads";
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.append("<!DOCTYPE html>\r\n")
		.append("<html>\r\n")
		.append("    <head>\r\n")
		.append("        <title>File search Form</title>\r\n")
		.append("    </head>\r\n")
		.append("    <body>\r\n");
		//writer.append("<img src=\"welcome.jpg\" alt=\"Trulli\" width\\="500\" height\="333\"/><br/>\r\n");
		writer.append("<h1>search file</h1>\r\n");
		writer.append("<form method=\"POST\" action=\"search\" ")
		.append("enctype=\"multipart/form-data\">\r\n");
		//writer.append("<input type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
		
		//writer.append("name:<input type=\"text\" name=\"name\" /><br/>\r\n");
		writer.append("maxlongitude:<input type=\"number\" step=\"any\" name=\"maxlongitude\" /><br/>\r\n");
		writer.append("minlongitude:<input type=\"number\" step=\"any\" name=\"minlongitude\" /><br/>\r\n");
        writer.append("maxlatitude:<input type=\"number\" step=\"any\" name=\"maxlatitude\" /><br/>\r\n");
		writer.append("minlatitude:<input type=\"number\" step=\"any\" name=\"minlatitude\" /><br/>\r\n");
		writer.append("end date:<input type=\"date\" step=\"any\" name=\"maxts\" /><br/>\r\n");
		writer.append("start date:<input type=\"date\" step=\"any\" name=\"mints\" /><br/>\r\n");
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
		.append("        <title>File search Form</title>\r\n")
		.append("    </head>\r\n")
		.append("    <body>\r\n");
		//writer.append("<img src=\"welcome.jpg\" alt=\"Trulli\" width\\="500\" height\="333\"/><br/>\r\n");
		writer.append("<h1>search file</h1>\r\n");
		writer.append("<form method=\"POST\" action=\"search\" ")
		.append("enctype=\"multipart/form-data\">\r\n");
		//writer.append("<input type=\"file\" name=\"fileName1\"/><br/><br/>\r\n");
		
		//writer.append("name:<input type=\"text\" name=\"name\" /><br/>\r\n");
		writer.append("maxlongitude:<input type=\"number\" step=\"any\" name=\"maxlongitude\" /><br/>\r\n");
		writer.append("minlongitude:<input type=\"number\" step=\"any\" name=\"minlongitude\" /><br/>\r\n");
        writer.append("maxlatitude:<input type=\"number\" step=\"any\" name=\"maxlatitude\" /><br/>\r\n");
		writer.append("minlatitude:<input type=\"number\" step=\"any\" name=\"minlatitude\" /><br/>\r\n");
		writer.append("end date:<input type=\"date\" step=\"any\" name=\"maxts\" /><br/>\r\n");
		writer.append("start date:<input type=\"date\" step=\"any\" name=\"mints\" /><br/>\r\n");
        writer.append("caption:<input type=\"text\" name=\"caption\" /><br/>\r\n");
		
		writer.append("<input type=\"submit\" value=\"Submit\"/>\r\n");
		writer.append("</form>\r\n");
		writer.append("    </body>\r\n").append("</html>\r\n");
				////////////////////////	
		// gets absolute path of the web application
		String applicationPath = request.getServletContext().getRealPath("");
		// constructs path of the directory to save uploaded file
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		// creates upload folder if it does not exists
		File uploadFolder = new File(uploadFilePath);
		if (!uploadFolder.exists()) {
			//no files exist
		}
		 
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		//PrintWriter writer = response.getWriter();
		PrintWriter out = response.getWriter();
		//out.println(uploadFilePath);
		
		float fmaxlon =0;
		float fminlon =0;
		float fmaxlat =0;
		float fminlat =0;
		String maxlon = "";
		String minlon = "";
		String maxlat = "";
		String minlat = "";
		String cap = "";
		//String name = request.getParameter("name");
		cap = request.getParameter("caption");
		if(cap == null || cap.length() == 0){cap = " ";}
		try{ fmaxlon = Float.parseFloat(request.getParameter("maxlongitude"));}catch(Exception e){fmaxlon=1000;}
		 try{ fminlon = Float.parseFloat(request.getParameter("minlongitude"));}catch(Exception e){fminlon=-1000;}
		 try{ fmaxlat = Float.parseFloat(request.getParameter("maxlatitude"));}catch(Exception e){fmaxlat=1000;}
		 try{ fminlat = Float.parseFloat(request.getParameter("minlatitude"));}catch(Exception e){fminlat=-1000;}
		 /*
		String maxts = request.getParameter("maxts");
		if(maxts == null || maxts.length() == 0){maxts = "2095-03-01";}
		
		//Timestamp tmaxts = Timestamp.valueOf(maxts.replace("T", " "));
		
		String mints = request.getParameter("mints");
		if(mints == null || mints.length() == 0){mints = "2002-03-01";}
		
		//Timestamp tmints = Timestamp.valueOf(mints.replace("T", " "));
		 */
		
		
		String maxts = request.getParameter("maxts");
		if(maxts == null || maxts.length() == 0){maxts = "2095-03-01";}
		maxts = maxts + " 23:59:59" ;
		System.out.println("End Date: " + maxts);
		
		
		//Timestamp tmaxts = Timestamp.valueOf(maxts.replace("T", " "));
		
		String mints = request.getParameter("mints");
		if(mints == null || mints.length() == 0){mints = "2002-03-01";}
		mints = mints + " 01:01:01" ;
		System.out.println("Stard Date: " + mints);
		
		//Timestamp tmints = Timestamp.valueOf(mints.replace("T", " "));
		 
		
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","oracle");
			String stringState = "SELECT name, longitude, latitude, caption, ts  FROM  gallery  WHERE  	longitude < " +fmaxlon+" AND longitude >" +fminlon+" 	AND latitude < " +fmaxlat+" AND latitude >" +fminlat+" 	AND caption = '"+cap+"' OR '"+cap+"' = ' ' AND ts BETWEEN CAST('" +mints+ "' AS TIMESTAMP) AND CAST('" +maxts+ "' AS TIMESTAMP)";//   OR ts BETWEEN CAST('" +maxts+ "'AS TIMESTAMP) AND CAST('" +mints+ "' AS TIMESTAMP)  ";
			out.println(stringState);
			PreparedStatement ps=con.prepareStatement(stringState);
			 
			 
			//ps.setString(4,cap);
			
			//out.print("<table width=50% border=1>");
			out.print("<caption>Result:</caption>");
			ResultSet rs=ps.executeQuery();
			
			/* Printing column names */
			ResultSetMetaData rsmd=rs.getMetaData();
			int total=rsmd.getColumnCount();
			/*
			out.print("<tr>");
			for(int i=1;i<=total;i++)
			{
				out.print("<th>"+rsmd.getColumnName(i)+"</th>");
				
			}
			out.print("</tr>");
			*/
			/* Printing result */
			out.print("</tr>");
			while(rs.next())
			{
				
				String imageName=rs.getString(1);
						
				out.print("<tr><td>   Image Name: "+imageName+"</td><td>   Longitude: "+rs.getFloat(2)+"</td><td>   Latitude: "+rs.getFloat(3)+"</td><td>   Caption: "+rs.getString(4)+"</td></tr>   Time Stamp: "+rs.getTimestamp(5)+"</td></tr>");
				response.setContentType("image/jpeg"); 
				out.print("<img src='uploads/"+imageName + "'>"); 
						
					
				
					
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
			}
			//out.print("</table>");
			
		}catch (Exception e2) {e2.printStackTrace();}
		
		finally{out.close();}
	}

}
