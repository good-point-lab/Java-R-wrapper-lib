
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html lang="en">
    <%@ include file="common-head.jsp" %>
    <body>
        <div class="container">
            RStat Frame Plots
            <hr> 
            Iris dataset stored in DB<br>
            <div id="plot-iris-class"></div>
            Mock data using JRSEY class<br>
            <div id="plot-frame-class"></div>
            Mock data using servlet<br>
            <div id="plot-frame-servlet"></div>
            <hr>
            <footer>
                <p>&copy; Core Prototype 2013</p>
            </footer>
        </div>
    </body>
    <script>       
        $(function(){        
            $.ajax({
                type: "GET",
                url: "<%=absPath%>/services/plot-iris-class-db/plot",
                async: true,
                contentType: "image/jpg",
                timeout: 10000,
                error: function (XMLHttpRequest, textStatus, errorThrown) { },
                success: function(data) {
                    $("#plot-iris-class").html('<img src="data:image/jpg;base64,' + data + '" />');
                }
            }) 
        });             
        $(function(){        
            $.ajax({
                type: "GET",
                url: "<%=absPath%>/services/plot-frame-class/plot",
                async: true,
                contentType: "image/jpg",
                timeout: 10000,
                error: function (XMLHttpRequest, textStatus, errorThrown) { },
                success: function(data) {
                    $("#plot-frame-class").html('<img src="data:image/jpg;base64,' + data + '" />');
                }
            }) 
        });
        $(function(){        
            $.ajax({
                type: "GET",
                url: "<%=absPath%>/plot-frame-servlet", 
                async: true,
                contentType: "image/jpg",
                timeout: 10000,
                error: function (XMLHttpRequest, textStatus, errorThrown) { },
                success: function(data) {
                    $("#plot-frame-servlet").html('<img src="data:image/jpg;base64,' + data + '" />');
                }
            }) 
        });            
       
    </script>
</html>
