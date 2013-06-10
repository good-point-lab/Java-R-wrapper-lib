
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html lang="en">
    <%@ include file="common-head.jsp" %>
    <body>
        <div class="container">
            R Iris Plots
            <hr>
            <div class="btn-group">
                <button id="points" class="btn">points</button>
                <button id="histogram" class="btn">histogram</button>
            </div>
            <div id="result"></div>
            <hr>

            <footer>
                <p>&copy; Core Prototype 2013</p>
            </footer>
        </div>
    </body>
    <script>       
        function loadPlot(plot_type) {
            $.ajax({
                type: "GET",
                url: "<%=absPath%>/services/plot-iris-class/plot?plot-type=" + plot_type,
                async: true,
                contentType: "image/jpg",
                timeout: 10000,
                error: function (XMLHttpRequest, textStatus, errorThrown) { },
                success: function(data) {
                    $("#result").html('<img src="data:image/jpg;base64,' + data + '" />');
                }
            })        
        }
        
        $(function(){                    
            $("#points").click(function(){   
                loadPlot("p");
            });
            $("#histogram").click(function(){
                loadPlot("h");            
            });                         
        });
    </script>
</html>
