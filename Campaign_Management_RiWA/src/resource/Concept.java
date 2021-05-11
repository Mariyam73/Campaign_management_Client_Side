package resource;

import java.sql.*;

import security.ConceptHashing;
import util.DBConnection;

public class Concept {
	
	//DBConnection object to connect to database
		DBConnection dbConnect = new DBConnection();
		String dbErrorMessage = "Database Connection failed!!";
		
		// ---Method to retrieve concept details of a researcher---
		public String readMyConcepts()
		{
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null)
				{
					return dbErrorMessage;
				}
				
				// Displaying the read concepts
				output = "<table border='1'><tr><th>Concept Code</th>"
				+"<th>Concept Name</th><th>Concept Description</th>"
				+ "<th>Start Date</th><th>Deadline</th>"
				+ "<th>Pledge Goal</th><th>Reward</th>"
				+ "<th>Pledged Amount</th>"
				+ "<th>Status</th><th>Work Update</th>"
				+ "<th>Researcher</th><th>Manufacturer</th>"
				+"<th>Update</th><th>Remove</th></tr>";
				
				// Retrieving the concepts launched by a particular researcher
				String query = "select c.conceptID, c.conceptCode, hn.nKey as conceptName, hd.nKey as conceptDesc, c.startDate, c.deadline, c.pledgeGoal, c.reward, c.status, c.workUpdt, c.researcherID, c.manufactID from concept c, hconceptname hn, hconceptdesc hd where c.conceptName = hn.Value and c.conceptDesc = hd.Value";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				
				
				// iterate through the rows in the result set
				while (rs.next())
				{
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
					String manufactID = rs.getString("manufactID");
					
					// -- Calling a stored function in the database to retrieve the total pledged amount --
					//Preparing a CallableStatement to call a function
				    CallableStatement cstmt = con.prepareCall("{? = call funcGetAmount(?)}");
				    
				    //Registering the out parameter of the function (return type)
				    cstmt.registerOutParameter(1, Types.DOUBLE);
				    
				    //Setting the input parameters of the function
				    cstmt.setString(2, conceptCode);
				    
				    //Executing the statement
				    cstmt.execute();
				    
				    //Set the value returned by the function to a variable
				    String pledgedAmount = cstmt.getString(1);
				    
				    //If no amounts pledged so far set the value as 0.00
				    if(pledgedAmount == null) {
				    	pledgedAmount = "0.00";
				    }
					
					// Add a row into the HTML table
					output += "<tr><td>" + conceptCode + "</td>";
					output += "<td>" + conceptName + "</td>";
					output += "<td>" + conceptDesc + "</td>";
					output += "<td>" + startDate + "</td>";
					output += "<td>" + deadline + "</td>";
					output += "<td>" + pledgeGoal + "</td>";
					output += "<td>" + reward + "</td>";
					output += "<td>" + pledgedAmount + "</td>";
					output += "<td>" + status + "</td>";
					output += "<td>" + workUpdt + "</td>";
					output += "<td>" + researcherID + "</td>";
					output += "<td>" + manufactID + "</td>";
					
					output += "<td><input name='btnUpdate' type='button' value='Update' "
							+ "class='btnUpdate btn btn-secondary' data-conceptCode='" + conceptCode + "'></td>"
							+ "<td><input name='btnRemove' type='button' value='Remove' "
							+ "class='btnRemove btn btn-danger' data-conceptCode='" + conceptCode + "'></td></tr>";
					
					}
					con.close();
					
					// Completion of the HTML table
					output += "</table>";
				}
				catch (Exception e)
				{
					output = "Error while retrieving your concept details!!";
					System.out.println(e.getMessage());
				}
				return output;
		}
		
		
		
		
		// ---Method to insert the concept details---
		public String insertConcept(String conceptName, String conceptDesc, String startDate, String deadline, String pledgeGoal, String reward,String workUpdt, String researcherID, String manufactID){
			
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null)
				{
					return dbErrorMessage;
				}
				
				System.out.println(researcherID);
				//Hashing the concept name and description
				ConceptHashing conceptHash = new ConceptHashing();
				String hName = conceptHash.hashPassword(conceptName);
				String hDesc = conceptHash.hashPassword(conceptDesc);
				
				/*** -- Calling a stored function created in database to auto generate a sequential concept code -- ***/
				//Preparing a CallableStatement to call a function
			    CallableStatement cstmt = con.prepareCall("{? = call getCustomID()}");
			    
			    //Registering the out parameter of the function (return type)
			    cstmt.registerOutParameter(1, Types.CHAR);
			    
			    //Executing the statement
			    cstmt.execute();
			    
			    //Get the value returned by function and set it to a variable
			    String conceptCode = cstmt.getString(1);
				/*** End of execution of stored function ***/
			   
				// create a prepared statement
				String query = " insert into concept(`conceptID`,`conceptCode`,`conceptName`,`conceptDesc`,`startDate`,`deadline`,`pledgeGoal`,`reward`,`workUpdt`,`researcherID`,`manufactID`)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement preparedStmt = con.prepareStatement(query);
				
				// binding values
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, conceptCode);
				preparedStmt.setString(3, hName);
				preparedStmt.setString(4, hDesc);
				preparedStmt.setString(5, startDate);
				preparedStmt.setString(6, deadline);
				preparedStmt.setDouble(7, Double.parseDouble(pledgeGoal));
				preparedStmt.setString(8, reward);
				preparedStmt.setString(9, workUpdt);
				preparedStmt.setString(10, researcherID);
				preparedStmt.setString(11, manufactID);
				
				//execute the statement
				preparedStmt.execute();
				
				//Table for Hash values 
				insertNameForKey(conceptName, hName);
				insertDescForKey(conceptDesc, hDesc);
				
				con.close();
				
				String newConcepts = readMyConcepts();
				output = "{\"status\":\"success\", \"data\": \"" +newConcepts + "\"}";
			}
			catch (Exception e)
			{

				output = "{\"status\":\"error\", \"data\":\"Error while launching the concept\"}";
				System.err.println(e.getMessage());
			}
			return output;
		}
		
		
		//  ---Method to update a concept details---
		public String updateConcept(String conceptID, String conceptName, String conceptDesc, String pledgeGoal, String reward, String workUpdt)
		{
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null){
					return dbErrorMessage; 
				}
				
				//Hashing the concept name and description
				ConceptHashing conceptHash = new ConceptHashing();
				String hName = conceptHash.hashPassword(conceptName);
				String hDesc = conceptHash.hashPassword(conceptDesc);
				
				//Create statement to get the conept status
				String sqlQuery = "select status from concept where conceptCode = '"+conceptID+"' ";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);
				
				if(rs.next()) {
					String conceptStatus = rs.getString("status");
					
					//if completed display cannot update, esle update it
					if(conceptStatus.equals("Completed")) {
						output = "Concept cannot be updated!!";
					}else {
						// create a prepared statement
						String query = "UPDATE concept SET conceptName=?,conceptDesc=?,pledgeGoal=?,reward=?,workUpdt=? WHERE conceptCode=?";
						PreparedStatement preparedStmt = con.prepareStatement(query);
						
						// binding values
						preparedStmt.setString(1, hName);
						preparedStmt.setString(2, hDesc);
						preparedStmt.setDouble(3, Double.parseDouble(pledgeGoal));
						preparedStmt.setString(4, reward);
						preparedStmt.setString(5, workUpdt);
						preparedStmt.setString(6, conceptID);
						
						// execute the statement
						preparedStmt.execute();
						
						//Table for Hash values 
						insertNameForKey(conceptName, hName);
						insertDescForKey(conceptDesc, hDesc);
						
						con.close();
						String newItems = readMyConcepts();
						output = "{\"status\":\"success\", \"data\": \"" +newItems + "\"}";
					}
				}
				
			}
			catch (Exception e)
			{
				output = "{\"status\":\"error\", \"data\":\"Error while updating the concept\"}";
				System.err.println(e.getMessage());
			}
			return output;
		}
		
		
		// ---Method to delete a concept detail---
		public String deleteConcept(String conceptID)
		{
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null) {
					return dbErrorMessage; 
				}
				
				//Retrieving status to check if the pledge goal is reached
				String sqlQuery = "select status from concept where conceptCode = '"+conceptID+"' ";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery);
				
				//validation
				if(rs.next()) {
					//get the status value
					String conceptStatus = rs.getString("status");
					
					//if status completed do not delete, else delete 
					if(conceptStatus.equals("Completed")) {
						output = "Concept cannot be deleted!!";
					}else {
						// create a prepared statement
						String query = "delete from concept where conceptCode=?";
						PreparedStatement preparedStmt = con.prepareStatement(query);
						
						// binding values
						preparedStmt.setString(1, conceptID);
						
						// execute the statement
						preparedStmt.execute();
						con.close();
						String newItems = readMyConcepts();
						output = "{\"status\":\"success\", \"data\": \"" +newItems + "\"}";
					}
				}
			}
			catch (Exception e)
			{
				output = "{\"status\":\"error\", \"data\":\"Error while deleting the concept\"}";
				System.err.println(e.getMessage());
			}
			return output;
		}
		
		
		// ---Method to read all concepts as a consumer---
		public String readAllConcepts()
		{
			String output = "";
			try
			{
				Connection con = dbConnect.connect();
				if (con == null)
				{
					return dbErrorMessage;
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
					output += "<td><form method='post' action=''>"
					+ "<input name='btnBacks' "
					+ " type='submit' value='Back the project' class='btn btn-secondary'>"
					+ "<input name='conceptID' type='hidden' "
					+ " value=' " + conceptID + "'>"
					+ "</form></td></tr>";
					
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
		
		
		/******************************* HASHING METHODS ******************************/
		//Method to Hash during insert
		public int insertDescForKey(String conceptDesc, String hDesc)throws SQLException {
				
				Connection con = dbConnect.connect();
				
				//Making Key Value pairs
				//Name
				String query2 = "INSERT INTO hConceptDesc(`id`, `nKey`, `Value`) VALUES(?,?,?)" ;
				PreparedStatement nameT  = con.prepareStatement(query2);
				//Binding values
				nameT.setInt(1, 0);
				nameT.setString(2, conceptDesc);
				nameT.setString(3, hDesc);
				
				//Execute the statement
				nameT.execute();
				
				return 0;
			}
		
		//Method to Hash during update
		public int insertNameForKey(String conceptName, String hName) throws SQLException {
			
			Connection con = dbConnect.connect();
			
			//Making Key Value pairs
			//Name
			String query2 = "INSERT INTO hConceptName(`id`, `nKey`, `Value`) VALUES(?,?,?)" ;
			PreparedStatement nameT  = con.prepareStatement(query2);
			//Binding values
			nameT.setInt(1, 0);
			nameT.setString(2, conceptName);
			nameT.setString(3, hName);
			
			//Execute the statement
			nameT.execute();
			
			return 0;
		}
		
		
		/**************************** METHODS REQUIRED AS A SERVER FOR INTER SERVICE COMMUNICATION ************************/
		//Retrieve concept ID for payment
		public String readSpecificConceptIDForPayment(String conceptName ) {
	        String output = "";
	        
	        try { 
	             Connection con =dbConnect.connect();
	             
	             if (con == null){
	                 return "Error while connecting to the database for reading."; 
	             }
	        
	             String query = "select * from concept  where conceptName = '"+conceptName+"'";
	             Statement stmt = con.createStatement();
	         
	             ResultSet rs = stmt.executeQuery(query);
	             String conceptCode = null;
	             
	             while (rs.next()){
	                conceptCode =  rs.getString("conceptCode");
	             }

	            con.close();
	           
	            output += conceptCode;
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	         return output;
	    }
		
		
		
		//Method to get Product description when giving the title
		public String getprodDesc(String conceptName) {

			String output = "";
			try{
		
				Connection con = dbConnect.connect();
			
				if (con == null){
					return dbErrorMessage;
				}
			
				// Retrieving the concepts launched by a particular researcher
				String query = "SELECT d.nKey as conceptDesc "
				+ "FROM concept c, hconceptname n, hconceptdesc d "
				+ "WHERE c.conceptName = n.Value AND "
				+ "c.conceptDesc = d.Value AND "
				+ "n.nKey = '"+conceptName+"' ";
			
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
			
				String conceptDescription = null;
			
				// iterate through the rows in the result set
				while (rs.next()){
				conceptDescription = rs.getString("conceptDesc");
				}
			
				con.close();
			
				output += conceptDescription;

			}catch (Exception e){
				output = "Error while retrieving your concept details!!";
				System.err.println(e.getMessage());
			}
				return output;

		}

		//Method to get Product ID when giving the title
		public String getprodID(String conceptName) {

			String output = "";
			try{
		
				Connection con = dbConnect.connect();
			
				if (con == null){
					return dbErrorMessage;
				}
			
				// Retrieving the concepts launched by a particular researcher
				String query = "SELECT c.conceptCode "
				+ "FROM concept c, hconceptname n "
				+ "WHERE c.conceptName = n.Value AND "
				+ "n.nKey = '"+conceptName+"' ";
			
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
			
				String conceptCode = null;
			
				// iterate through the rows in the result set
				while (rs.next()){
				conceptCode = rs.getString("conceptCode");
				}
			
				con.close();
			
				output += conceptCode;
		
			}
			catch (Exception e){
				output = "Error while retrieving your concept details!!";
				System.err.println(e.getMessage());
		}
			return output;
		}
	    

}
