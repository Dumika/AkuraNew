<?php
include 'Constants.php';
$itemArray = array();
$response = array();

	$query = "SELECT SerialNo, Type, Section, PersonInCharge,Quantity,Supplier FROM item";
	$rows = array();

	if($sth = mysqli_query($con, $query)){
		$rows = array();
		while($r = mysqli_fetch_assoc($sth)) {
			$rows[] = $r;
		}
		print json_encode($rows);
		
	}else{
		//Whe some error occurs
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}

//Display JSON response

?>