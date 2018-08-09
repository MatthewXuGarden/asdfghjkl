<%

String jsession = request.getSession().getId();
%>


<div style="display:none;">
	$SET$
	<form id="frm"  method="post" action="servlet/MapsRefresh;jsessionid=<%=jsession%>" target="Receiver">
		$INPUT$
	</form>
</div>

<div style='position:absolute;left:0;' >
	$OBJECT$
</div>