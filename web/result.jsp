<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your key is generated</title>   
    </head>
    <body>
        <p>Your key is : <%= request.getAttribute("B64")%></p><br>
        <p>Keep it a secret !!<p>
    </body>
</html>
