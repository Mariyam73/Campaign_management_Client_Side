$(document).ready(function()
{
	if ($("#alertSuccess").text().trim() == ""){
		$("#alertSuccess").hide();
	}
	
	$("#alertError").hide();
});
	
//SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
	// Clear alerts---------------------
	$("#alertSuccess").text("");
	$("#alertSuccess").hide();
	$("#alertError").text("");
	$("#alertError").hide();
		
	// Form validation-------------------
	var status = validateConceptForm();
	if (status != true){
		$("#alertError").text(status);
		$("#alertError").show();
		return;
	}
		
	// If valid------------------------
	var type = ($("#hidConceptIDSave").val() == "") ? "POST" : "PUT";
	$.ajax(
	{
		url : "ConceptAPI",
		type : type,
		data : $("#formConcept").serialize(),
		dataType : "text",
		complete : function(response, status){
			onConceptSaveComplete(response.responseText, status);
		}
		});
});



function onConceptSaveComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		
		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully saved.");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
		} else if (status == "error")
		{
			$("#alertError").text("Error while saving.");
			$("#alertError").show();
		} else
		{
			$("#alertError").text("Unknown error while saving..");
			$("#alertError").show();
	}
		$("#hidConceptIDSave").val("");
		$("#formConcept")[0].reset();
}



//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
	//$("#hidConceptIDSave").val($(this).data("conceptCode"));
	$("#conceptName").val($(this).closest("tr").find('td:eq(1)').text());
	$("#conceptDesc").val($(this).closest("tr").find('td:eq(2)').text());
	$("#startDate").val($(this).closest("tr").find('td:eq(3)').text());
	$("#deadline").val($(this).closest("tr").find('td:eq(4)').text());
	$("#pledgeGoal").val($(this).closest("tr").find('td:eq(5)').text());
	$("#reward").val($(this).closest("tr").find('td:eq(6)').text());
	$("#workUpdt").val($(this).closest("tr").find('td:eq(9)').text());
	$("#researcherID").val($(this).closest("tr").find('td:eq(10)').text());
	$("#manufactID").val($(this).closest("tr").find('td:eq(11)').text());
});


//DELETE=================================================
$(document).on("click", ".btnRemove", function(event)
		{
			$.ajax(
			{
				url : "ConceptAPI",
				type : "DELETE",
				data : "conceptCode=" + $(this).data("conceptCode"),
				dataType : "text",
				complete : function(response, status)
				{
					onConceptDeleteComplete(response.responseText, status);
				}
			});
});


function onConceptDeleteComplete(response, status)
{
	if (status == "success")
	{
		var resultSet = JSON.parse(response);
		if (resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully deleted.");
			$("#alertSuccess").show();
			$("#divItemsGrid").html(resultSet.data);
		} else if (resultSet.status.trim() == "error")
		{
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
		} else if (status == "error")
		{
			$("#alertError").text("Error while deleting.");
			$("#alertError").show();
		} else
		{
			$("#alertError").text("Unknown error while deleting..");
			$("#alertError").show();
	}
}


//========== VALIDATION ================================================
function validateConceptForm()
{
		// Concept Name
		if ($("#conceptName").val().trim() == "")
		{
			return "Insert Concept Name!!";
		}
		
		// Concept Description
		if ($("#conceptDesc").val().trim() == "")
		{
			return "Insert Concept Description!!";
		}
		
		// Concept Start Date
		if ($("#startDate").val().trim() == "")
		{
			return "Insert Start Date!!";
		}
		
		// Concept deadline
		if ($("#deadline").val().trim() == "")
		{
			return "Insert Deadline!!";
		}
		
		// Concept pledge Goal
		if ($("#pledgeGoal").val().trim() == "")
		{
			return "Insert Pledge Goal!!";
		}
		
		// is numerical value
		var pledgeGoal = $("#pledgeGoal").val().trim();
		if (!$.isNumeric(pledgeGoal))
		{
			return "Invalid Pledge Goal (Please enter a number)";
		}
		
		// Concept Reward
		if ($("#reward").val().trim() == "")
		{
			return "Insert Reward!!";
		}
		
		// Concept Work Update
		if ($("#workUpdt").val().trim() == "")
		{
			return "Insert Work Update!!";
		}
		
		// Researcher ID
		if ($("#researcherID").val().trim() == "")
		{
			return "Insert Researcher ID!!";
		}
		
		// Manufacturer ID
		if ($("#manufactID").val().trim() == "")
		{
			return "Insert Manufacturer ID!!";
		}
		
		// convert to decimal price
		$("#pledgeGoal").val(parseFloat(pledgeGoal).toFixed(2));
		
		return true;
}
