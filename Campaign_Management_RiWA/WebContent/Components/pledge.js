//BACKS==========================================
$(document).on("click", ".btnBacks", function(event)
{
	$("#hidPledgeIDSave").val($(this).data("conceptcode"));
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
	$.ajax(
	{
		url : "PledgeAPI",
		type : "POST",
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