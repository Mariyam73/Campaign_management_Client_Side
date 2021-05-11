package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    
    
    private static Map getParasMap(HttpServletRequest request)
    {
	    Map<String, String> map = new HashMap<String, String>();
	    try
	    {
		    Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
		    String queryString = scanner.hasNext() ?
		    scanner.useDelimiter("\\A").next() : "";
		    scanner.close();
		    String[] params = queryString.split("&");
		    for (String param : params)
		    {
		    	String[] p = param.split("=");
		    	map.put(p[0], p[1]);
		    }
		}
	    catch (Exception e)
	    {		    
	    	
	    }
		return map;
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
				request.getParameter("conceptDesc"), 
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
		Map paras = getParasMap(request);
		String output = conceptObj.updateConcept(paras.get("hidConceptIDSave").toString(),
				paras.get("conceptName").toString(),
				paras.get("conceptDesc").toString(),
				paras.get("pledgeGoal").toString(),
				paras.get("reward").toString(),
				paras.get("workUpdt").toString());
				response.getWriter().write(output);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map paras = getParasMap(request);
		String output = conceptObj.deleteConcept(paras.get("conceptCode").toString());
		response.getWriter().write(output);
	}

}
