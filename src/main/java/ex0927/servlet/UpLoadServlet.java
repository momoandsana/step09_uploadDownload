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
@MultipartConfig(
		maxFileSize = 1024 * 1024 * 5, // 5M - 한 번에 업로드 할 수 있는 파일 크기 제한
		maxRequestSize = 1024 * 1024 * 50 // 50M - 전체 요청의 크기 제한
)
public class UpLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 1. HTTP POST 요청을 처리하는 메서드
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name"); // 2. 이름 파라미터 가져오기
		String subject = request.getParameter("subject"); // 3. 과목 파라미터 가져오기

		Part part = request.getPart("file"); // 4. 파일 파트 가져오기
		String fileName = this.getFilename(part); // 5. 파일 이름 조회
		long fileSize = part.getSize(); // 6. 첨부된 파일 크기

		String saveDir = "C:\\Edu\\WebProgramming\\save\\"; // 7. 파일 저장 경로

		if (fileName != null) {
			part.write(saveDir + "/" + fileName); // 8. 파일 저장
		}

		// 9. 콘솔에 정보 출력
		System.out.println("name = " + name);
		System.out.println("subject = " + subject);
		System.out.println("fileName = " + fileName);
		System.out.println("fileSize = " + fileSize);

		// 10. 요청 속성에 정보 저장
		request.setAttribute("name", name); // 뷰에서 ${requestScope.name}
		request.setAttribute("subject", subject);
		request.setAttribute("fileSize", fileSize);
		request.setAttribute("fileName", fileName);
		request.setAttribute("saveDir", saveDir);

		// 11. 결과 페이지로 포워딩
		request.getRequestDispatcher("upLoadResult.jsp").forward(request, response);
	}

	// 12. 첨부된 파일 이름 추출하기
	private String getFilename(Part part) {
		String headerContent = part.getHeader("content-disposition"); // 13. 헤더에서 content-disposition 가져오기
		// contentDisp의 결과 form-data; name="fileName"; filename="추가한 파일 이름"
		System.out.println(headerContent); // 14. 헤더 출력

		String[] split = headerContent.split(";"); // 15. 헤더를 분리하여 배열로 저장

		for (String temp : split) {
			if (temp.trim().startsWith("filename")) { // 16. filename 찾기
				System.out.println(temp); // 17. filename 출력
				return temp.substring(temp.indexOf("=") + 2, temp.length() - 1); // 18. 파일 이름 추출
			}
		}
		return null; // 19. 파일 이름이 없을 경우 null 반환
	}
}
