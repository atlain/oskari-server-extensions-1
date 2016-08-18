<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>${viewName}</title>
    <meta charset="utf-8" />
    <link rel="shortcut icon" href="http://locationframework.eu/sites/default/files/elffavicon_0.jpg" type="image/jpeg" />
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>

    <!-- ############# css ################# -->
    <link type="text/css" rel="stylesheet" href="//fonts.googleapis.com/css?family=Open+Sans:400,400italic,700,700italic,800,800italic,600italic,600" />
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari/resources/css/forms.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari/resources/css/portal.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/icons.css"/>
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/css/overwritten.css"/>
    <style type="text/css">
        @media screen {
			#contentMap { margin-left: 0px !important; }
        }
    </style>
    <!-- ############# /css ################# -->
</head>
<body>
<div id="contentMap" class="published">
    <div id="mapdiv"></div>
</div>


<!-- ############# Javascript ################# -->

<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var language = '${language}';
    var controlParams = ${controlParams};
</script>

<script type="text/javascript"
        src="/Oskari/bundles/bundle.js">
</script>

<c:if test="${preloaded}">
    <!-- Pre-compiled application JS, empty unless created by build job -->
    <script type="text/javascript"
            src="/Oskari${path}/oskari.min.js">
    </script>
    <!-- Minified CSS for preload -->
    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/oskari.min.css"
            />
    <%--language files --%>
    <script type="text/javascript"
            src="/Oskari${path}/oskari_lang_${language}.js">
    </script>
</c:if>

<script type="text/javascript"
        src="/Oskari${path}/index.js">
</script>


<!-- ############# /Javascript ################# -->
</body>
</html>
