$(document).ready(function()
{
	if ($("#alertSuccess").text().trim() == ""){
		$("#alertSuccess").hide();
	}
	
	$("#alertError").hide();
});


//BACKS==========================================
$(document).on("click", ".btnBacks", function(event)
{
	$("#conceptCode").val($(this).closest("tr").find('td:eq(0)').text());
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
	var type = ($("#hidPledgeIDSave").val() == "") ? "POST" : "PUT";
	$.ajax(
	{
		url : "PledgeAPI",
		type : type,
		data : $("#formPledge").serialize(),
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
		$("#hidPledgeIDSave").val("");
		$("#formPledge")[0].reset();
}



//========== VALIDATION ================================================
function validateConceptForm()
{
		// Concept Name
		if ($("#conceptCode").val().trim() == "")
		{
			return "Insert Concept Code!!";
		}
		
		// Concept Description
		if ($("#backherID").val().trim() == "")
		{
			return "Select username!!";
		}
		
		// Concept Start Date
		if ($("#pledgedAmnt").val().trim() == "")
		{
			return "Insert Pledged Amount!!";
		}
		
		// is numerical value
		var pledgedAmnt = $("#pledgedAmnt").val().trim();
		if (!$.isNumeric(pledgedAmnt))
		{
			return "Invalid Pledge Amount (Please enter a number)";
		}
		
		// convert to decimal price
		$("#pledgedAmnt").val(parseFloat(pledgedAmnt).toFixed(2));
		
		return true;
}
