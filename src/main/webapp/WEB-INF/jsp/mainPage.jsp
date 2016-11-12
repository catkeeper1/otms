<html>

<%
String verNoDojo = "verNo=10";
String verNo = "false".equals(verNoDojo) ? "": "?" + verNoDojo;

%>

<head>
	<title>OTMS</title>
	<link rel="stylesheet" href="./pub/dijit/themes/claro/claro.css<%= verNo%>">
	<link rel="stylesheet" href="./pub/gridx/resources/claro/Gridx.css<%= verNo%>"/>
	
	
	
	<link href="./pub/app/css/main.css<%= verNo%>" rel="stylesheet"/>
</head>
<body class="claro">

<div id="appMainPanel"></div>

<script>
    dojoConfig= {
        async: true,
        cacheBust:"<%= verNoDojo%>"
    };
</script>

<script src="./pub/dojo/dojo.js<%= verNo%>" data-dojo-config="async: true"></script>

<script>

require.on('error', function( error ) {
	
	alert("cannot load the module.");
});

require([
    'dojo/dom',
    'app/MainAppFrame',
    'dojo/domReady!'
], function (dom, MainAppFrame,BorderContainer,ContentPane) {
    var mainPanel = dom.byId('appMainPanel');
    //greeting.innerHTML += ' from Dojo!';
    
    var mainApp = new MainAppFrame().placeAt(mainPanel);
    mainApp.startup();
    
    
    
});
</script>

</body>
</html>