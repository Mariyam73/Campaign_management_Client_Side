package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import resource.Concept;

/**
 * Servlet implementation class ConceptAPI
 */
@WebServlet("/ConceptAPI")
public class ConceptAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Concept conceptObj = new Concept();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConceptAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String output = conceptObj.insertConcept(request.getParameter("conceptName"), 
				request.getParameter("coneptDesc"), 
				request.getParameter("startDate"), 
				request.getParameter("deadline"), 
				request.getParameter("pledgeGoal"), 
				request.getParameter("reward"), 
				request.getParameter("workUpdt"), 
				request.getParameter("researcherID"), 
				request.getParameter("manufactID"));
				response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
