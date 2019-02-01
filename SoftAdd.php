<?php
include 'Constants.php';
$response = array();
 
//Check for mandatory parameters
if(empty($_POST['SerialNo'])||empty($_POST['Software'])||empty($_POST['SProvider'])||empty($_POST['Status'])||empty($_POST['Warranty'])||empty($_POST['PurchasedD'])){
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
 
}else{
	
	$SerialNo = $_POST['SerialNo'];
	$Software = $_POST['Software'];
	$SProvider = $_POST['SProvider'];
	$Status = $_POST['Status'];
	$Warranty = $_POST['Warranty'];
	$PurchasedD = $_POST['PurchasedD'];
	
	//Query to insert a movie
	$query = "INSERT INTO soft( SerialNo, Software, SProvider, Status, Warranty, PurchasedD) VALUES (?,?,?,?,?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//Bind parameters
		$stmt->bind_param("ssssis",$SerialNo,$Software,$SProvider,$Status,$Warranty,$PurchasedD);
		//Exceting MySQL statement
		$stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Soft Asset Successfully Added ";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding soft asset";
		}					
	}else{
		//Some error while inserting
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
	
	
	
}
//Displaying JSON response
echo json_encode($response);    


?>