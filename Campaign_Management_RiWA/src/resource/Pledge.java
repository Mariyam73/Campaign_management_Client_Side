package resource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import util.DBConnection;

public class Pledge {
	
	//DBConnection object to establish connection
		DBConnection dbConnect = new DBConnection();
		
		
		// ---Method to read all concepts as a consumer---
				public String readAllConcepts()
				{
					String output = "";
					try
					{
						Connection con = dbConnect.connect();
						if (con == null)
						{
							return "Error while connecting to database!";
						}
						
						// Displaying the read concepts
						output = "<table border='1'><tr><th>Concept Code</th>"
						+"<th>Researcher ID</th><th>Concept Name</th><th>Concept Description</th>"
						+ "<th>Start Date</th><th>Deadline</th>"
						+ "<th>Pledge Goal</th><th>Reward</th>"
						+ "<th>Pledged Amount</th>"
						+ "<th>Status</th><th>Work Update</th>"
						+ "<th>Support</th></tr>";
						
						// retrieving all the concept details
						String query = "select c.conceptID, c.conceptCode, hn.nKey as conceptName, hd.nKey as conceptDesc, c.startDate, c.deadline, c.pledgeGoal, c.reward, c.status, c.workUpdt, c.researcherID from concept c, hconceptname hn, hconceptdesc hd where c.conceptName = hn.Value and c.conceptDesc = hd.Value ";
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery(query);
						
						// Iterate through the rows in the result set
						while (rs.next())
						{
							String conceptID = Integer.toString(rs.getInt("conceptID"));
							String conceptCode = rs.getString("conceptCode");
							String conceptName = rs.getString("conceptName");
							String conceptDesc = rs.getString("conceptDesc");
							String startDate = rs.getString("startDate");
							String deadline = rs.getString("deadline");
							String pledgeGoal = Double.toString(rs.getDouble("pledgeGoal"));
							String reward = rs.getString("reward");
							String status = rs.getString("status");
							String workUpdt = rs.getString("workUpdt");
							String researcherID = rs.getString("researcherID");
							
							/** -- Calling the stored function to retrieve the total pledged amount for concept -- **/
							//Preparing a CallableStatement to call a function
						    CallableStatement cstmt = con.prepareCall("{? = call funcGetAmount(?)}");
						    
						    //Registering the out parameter of the function (return type)
						    cstmt.registerOutParameter(1, Types.DOUBLE);
						    
						    //Setting the input parameters of the function
						    cstmt.setString(2, conceptCode);
						    
						    //Executing the statement
						    cstmt.execute();
						    
						    //Set the value returned by function to a variable
						    String pledgedAmount = cstmt.getString(1); 
						    /*** End of function execution ***/
						    
						    //If no values returned set the total as 0.00
						    if(pledgedAmount == null) {
						    	pledgedAmount = "0.00";
						    }
							
						    
							// Add a row into the HTML table
							output += "<tr><td>" + conceptCode + "</td>";
							output += "<td>" + researcherID + "</td>";
							output += "<td>" + conceptName + "</td>";
							output += "<td>" + conceptDesc + "</td>";
							output += "<td>" + startDate + "</td>";
							output += "<td>" + deadline + "</td>";
							output += "<td>" + pledgeGoal + "</td>";
							output += "<td>" + reward + "</td>";
							output += "<td>" + pledgedAmount + "</td>";
							output += "<td>" + status + "</td>";
							output += "<td>" + workUpdt + "</td>";
							
							// button for backing a concept
							output += "<td><input name='btnBacks' type='button' value='Back the project' "
									+ "class='btnBacks btn btn-success' data-conceptcode='" + conceptCode + "'></td></tr>";
							
							}
							con.close();
							
							// Completion of the HTML table
							output += "</table>";
						}
						catch (Exception e)
						{
							output = "Error while retrieving the concepts!";
							System.err.println(e.getMessage());
						}
						return output;
				}
				
		
		// -- Method to insert pledge --
		public String insertPledge(String conceptID, String backerID, String pledgedAmnt){
			
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null)
				{
					return "Database Connection failed!!";
				}
				
				
				// create a prepared statement
				String query = " insert into backs(`conceptID`,`backerID`,`pledgedAmnt`)"
				+ " values (?, ?, ?)";
				PreparedStatement preparedStmt = con.prepareStatement(query);
				
				// binding values
				preparedStmt.setString(1, conceptID);
				preparedStmt.setString(2, backerID);
				preparedStmt.setDouble(3, Double.parseDouble(pledgedAmnt));
				
				//execute the statement
				preparedStmt.execute();
				con.close();
				
				String newConcepts = readAllConcepts();
				output = "{\"status\":\"success\", \"data\": \"" +newConcepts + "\"}";
			}
			catch (Exception e)
			{
				output = "{\"status\":\"error\", \"data\":\"Error while launching the concept\"}";
				System.err.println(e.getMessage());
			}
			return output;
		}
		
		

}
