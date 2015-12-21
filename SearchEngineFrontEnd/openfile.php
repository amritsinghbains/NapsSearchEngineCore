<?php
// echo $_GET['link'];
$string =  file_get_contents($_GET['link']);
// echo $_GET['words'];
$myArray = explode(',', $_GET['words']);

foreach($myArray as $my_Array){
    $string = str_ireplace($my_Array, "<span style=\"background:yellow;\">".$my_Array."</span>", $string);
}
echo $string;
?>