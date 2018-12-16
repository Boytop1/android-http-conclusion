<?php

$json=file_get_contents('php://input');

$obj=json_decode($json);

if(@$obj->{'username2'}=='123'&&@$obj->{'password2'}=='123'){

	echo "Login Success";

}else{

	echo "Login Failed";

}

// @$username1 = $_GET["username1"];
// @$password1 = $_GET["password1"];

// @$username2 = $_POST["username2"];
// @$password2 = $_POST["password2"];

// if($username1 == 'admin'&&$password1 == '123'){
// 	echo "Login Success";
// }
// else{
// 	echo "Login Failed";
// }

// if(@$username2 == 'a'&&@$password2 == 'a'){
// 	echo "Login Success";
// }
// else{
// 	echo "Login Failed";
// }

?>