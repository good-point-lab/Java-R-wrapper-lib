
<%String absPath = request.getContextPath();%>


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" href="<%=absPath%>/css/ui-lightness/jquery-ui-1.8.23.custom.css" rel="stylesheet">
    <link href="<%=absPath%>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=absPath%>/css/docs.css" rel="stylesheet">
    <link href="<%=absPath%>/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- jQuery -->
    <script type="text/javascript" src="<%=absPath%>/js/jquery-1.8.2.js"></script>
    <script type="text/javascript" src="<%=absPath%>/js/jquery-ui-1.8.23.custom.min.js"></script>
    <!-- Bootstrap -->
    <script src="<%=absPath%>/js/bootstrap.js"></script>
    <!-- D3 -->
    <script type="text/javascript" src="<%=absPath%>/js/d3/d3.v2.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=absPath%>/css/d3/colorbrewer.css"/>
    <link type="text/css" rel="stylesheet" href="<%=absPath%>/css/d3/calendar.css"/>
    <!-- RGraph -->
    <link rel="stylesheet" href="<%=absPath%>/css/rgraph/demos.css" type="text/css" media="screen" />   
    <script src="<%=absPath%>/js/rgraph/RGraph.common.core.js"></script>
    <script src="<%=absPath%>/js/rgraph/RGraph.common.dynamic.js"></script>
    <script src="<%=absPath%>/js/rgraph/RGraph.common.tooltips.js"></script>
    <script src="<%=absPath%>/js/rgraph/RGraph.pie.js"></script>
    <!-- Google JSAPI -->
    <script type="text/javascript" src="<%=absPath%>/js/googlejsapi/jsapi.js"></script>

    <!-- YUI3 -->
    <script  src="http://yui.yahooapis.com/3.7.2/build/yui/yui-min.js"></script>
    <link rel="stylesheet" href="http://yui.yahooapis.com/3.7.2/build/cssgrids/cssgrids-min.css">
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.7.2/build/cssreset/reset-min.css">

    <style>

        .inline {

            white-space:nowrap;
            font-size: 6px;
        }

    </style>

    <style type="text/css">
        #query
        {
            margin: 1px 0;
        }

        #controls
        {
            margin: 10px 0;
        }

        .rpp
        {
            padding-left: 1.5em;
        }

        .yui3-skin-sam .yui3-datatable caption
        {
            padding: 0 !important;
        }

        .yui3-skin-sam .yui3-paginator-rpp-options
        {
            margin-left: 0;
        }
    </style>

</head>
