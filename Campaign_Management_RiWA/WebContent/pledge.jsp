<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import = "resource.Pledge" %>
<%@page import = "util.UserDBConnection" %>
<%@page import = "java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Pledge Management</title>
	<link rel="stylesheet" href="Views/bootstrap.min.css">
	<link rel="stylesheet" href="Views/form.css">
	<script src="Components/jquery-3.2.1.min.js"></script>
	<script src="Components/concept.js"></script>
</head>
<body>
<% UserDBConnection userConn = new UserDBConnection(); %>

	<div class="container">
	<div class="row">
	<div class="col-12">
		<center><h1>Pledge Management</h1></center>
			<br>
			<form id="formPledge" name="formPledge" style = "border: 1px solid black;padding: 3%;">
			<input id="conceptID" name="conceptID" type="text" class="form-control form-control-sm" placeholder="Concept Code">
			<br> 
		
			 <select id = "backerID" name = "backerID" class="form-control form-control-sm">
                <option class="dropdown-menu">User Name</option>
                  <%
                  	try{
                    	Connection con = userConn.connect();
                   		Statement st = con.createStatement();
                 		String query = "select * from consumer";
                    	ResultSet rs = st.executeQuery(query);
                    			
                    	while(rs.next()){
                    	%>
                    	<option value="<%=rs.getString("userCode")%>"><%=rs.getString("userName") %></option>
                    	<%
                    	}
                    	con.close();
                    	}catch(Exception e){
                    			
                    	}
                    	%>
              </select>
              <br>
                
              <input id="pledgedAmnt" name="pledgedAmnt" type="text" class="form-control form-control-sm" placeholder="Pledged Amount">
		      <br> 
			<input id="btnSave" name="btnSave" type="button" value="Pledge Concept" class="btn btn-primary">
            <input type="hidden" id="hidPledgeIDSave" name="hidPledgeIDSave" value="">
		</form><br><br>
		
		<div id="divItemsGrid">
		<%
			Pledge conceptObj = new Pledge();
			out.print(conceptObj.readAllConcepts());
		%>
		</div>

	
		<br>

	</div>
	</div> 
	</div>

</body>
</html>