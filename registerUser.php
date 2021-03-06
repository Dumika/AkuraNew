<?php 
 
require_once '../includes/DbOperations.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
    if(
       // isset($_POST['userid']) and
            isset($_POST['fullname']) and
			isset($_POST['username']) and
                isset($_POST['password']))
        {
        //operate the data further 
 
        $db = new DbOperations(); 
 
         $result =  $db->createUser(  
                                    $_POST['fullname'],
									$_POST['username'],
                                    $_POST['password']
                                ); 
        if($result == 1){
            $response['error'] = false; 
            $response['message'] = "User registered successfully";
        }elseif($result == 2){
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";          
        
		}elseif($result==0){
            $response['error'] = true; 
            $response['message'] = "It seems you are already registered, please choose a different username and password";                     
        }
 
    }else{
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request";
}
 
echo json_encode($response);

//-------------------------------------------------------------------

/*<?php 
 
require_once '../includes/DbOperations.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(
            isset($_POST['fullname']) and
			isset($_POST['username']) and
                isset($_POST['password']))
{
				//operate the data further	
				$db = new DbOperations(); 
 
       if($db->createUser( 
                $_POST['fullname'],
				$_POST['username'],
                $_POST['password']
                                )){
			$response['error'] = false; 
            $response['message'] = "User registered successfully";
				}else{
			$response['error'] = true; 
            $response['message'] = "Some error occurred please try again"; 
				}else{
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
	
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request";
}
echo json_encode($response);
    */


