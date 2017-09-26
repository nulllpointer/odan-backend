package com.odan.common.application;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.odan.common.utils.IO;

/**
 * Servlet implementation class JSONSchema
 */
public class UIServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UIServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getPathInfo();
//		ServletContext sc = Application.getServletContext();
//		String path = sc.getRealPath("/UI/" + url);
//		String contentType = sc.getMimeType(path);
		String content = IO.loadUI(url);
//		response.setHeader("Content-Type", contentType);
//		response.setHeader("Content-Length", String.valueOf(content.length()));
		response.getWriter().append(content);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
