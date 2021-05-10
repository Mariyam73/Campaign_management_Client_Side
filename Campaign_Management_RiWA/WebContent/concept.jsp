<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import = "resource.Concept" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Concepts Management</title>
	<link rel="stylesheet" href="Views/bootstrap.min.css">
	<link rel="stylesheet" href="Views/form.css">
	<script src="Components/jquery-3.2.1.min.js"></script>
	<script src="Components/concept.js"></script>
</head>
<body>

	<div class="container">
	<div class="row">
	<div class="col-6">
		<h1 align="center">Concept Management</h1>
		<form id="formConcept" name="formConcept">
			<input id="conceptName" name="conceptName" type="text" class="form-control form-control-sm" placeholder="Concept name">
			<br> 
		
			<input id="conceptDesc" name="conceptDesc" type="text"class="form-control form-control-sm" placeholder="Concept description">
			<br> 
			
			<div class="form-row">
            	<div class="col">
                 	<input id="startDate" name="startDate" type="text" class="form-control form-control-sm" placeholder="Start date">
                    <br>
                </div>
        	    <div class="col">
                    <input id="deadline" name="deadline" type="text" class="form-control form-control-sm" placeholder="Deadline">
                    <br>
                </div>
            </div>
			
			<div class="form-row">
                <div class="col">
                    <input id="pledgeGoal" name="pledgeGoal" type="text" class="form-control form-control-sm" placeholder="PledgeGoal">
                    <br>
                </div>
                <div class="col">
                     <input id="reward" name="reward" type="text" class="form-control form-control-sm" placeholder="Reward">
                     <br>
                </div>
             </div>
			
			<input id="workUpdt" name="workUpdt" type="text"class="form-control form-control-sm" placeholder="Work Update">
			<br>
			
			<div class="form-row">
                <div class="col">
                    <input id="researcherID" name="researcherID" type="text" class="form-control form-control-sm" placeholder="Researcher ID">
                    <br>
                </div>
            	<div class="col">
                    <input id="manufactID" name="manufactID" type="text" class="form-control form-control-sm" placeholder="Manufacturer ID">
                    <br>
                </div>
            </div>
			
			<input id="btnSave" name="btnSave" type="button" value="Add Concept" class="btn btn-primary">
            <input type="hidden" id="hidConceptIDSave" name="hidConceptIDSave" value="">
		</form>
		
		<!--  <div id="alertSuccess" class="alert alert-success"></div>
		<div id="alertError" class="alert alert-danger"></div>-->
		<br>
		<div id="divItemsGrid">
		<%
			Concept conceptObj = new Concept();
			out.print(conceptObj.readAllConcepts());
		%>
		</div>
	</div>
	</div> 
	</div>

</body>
</html>