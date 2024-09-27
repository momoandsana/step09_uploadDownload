package ex0927.servlet;

import java.io.IOException;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


@WebServlet("/UpLoad")
@MultipartConfig( //어노테이션을 통해  서블릿이 파일 업로드 기능을 할 수 있도록 웹 컨테이너에 지시
        maxFileSize = 1024 * 1024 * 5, //5M - 한 번에 업로드 할 수 있는 파일 크기 제한
       maxRequestSize = 1024 * 1024 * 50 //50M -전체 요청의 크기 제한. 기본값은 무제한 
)
public class UpLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");// string 으로 넘어오는 것들은 getParameter 로 얻는다
		
		Part part = request.getPart("file");
		//Servlet 3.0버전부터 제공되는 Part API를 이용한 방법인데, getPart() 메서드
		// 파일들은 getParameter 로 얻지 못한다

		String fileName = this.getFilename(part);// 파일이름 조회
		long fileSize = part.getSize(); // 첨부된 파일 크기
		

//		String saveDir = request.getServletContext().getRealPath("/save"); // 웹 어플리케이션의 save 경로 밑에 저장
		String saveDir="C:\\Edu\\WebProgramming\\save\\";

		if (fileName!=null) {
            part.write( saveDir + "/"+ fileName);
        }//저장(업로드)
		
		System.out.println("name = " + name);
		System.out.println("subject = " + subject);
		System.out.println("fileName = " + fileName);
		System.out.println("fileSize = " + fileSize);
		
		//scope 영역에 저장
		request.setAttribute("name", name); //뷰에서 ${requestScope.name}
		request.setAttribute("subject", subject);
		request.setAttribute("fileSize", fileSize);
		request.setAttribute("fileName", fileName);
		request.setAttribute("saveDir", saveDir);
		
		
		//결과페이지로 이동 - 뷰에서  출력할 정보를 저장해서 간다.!!!
		request.getRequestDispatcher("upLoadResult.jsp").forward(request, response);
		
	}
	/*
	첨부된 파일이름 추출하기
	 */

	private String getFilename(Part part) {
        String headerContent = part.getHeader("content-disposition");

		//contentDisp의 결과 form-data; name="fileName"; filename="추가한 파일 이름"
		System.out.println(headerContent);

		String[] split = headerContent.split(";");// 추가한, 파일, 이름
        for (int i = 0; i < split.length; i++) {
            String temp = split[i];
            if (temp.trim().startsWith("filename")) // 공백을 제거하고 filename 를 찾는다
			{
				System.out.println(temp);
                return temp.substring(temp.indexOf("=") + 2, temp.length() - 1);// 추가한 파일 이름 <-이거를 잘라내기 해서 담아, 파일이름만 추출함
            }
        }
        return null;
    }

}







