function onLoadDashboard()
{
	// make zebra rows
	var table = document.getElementById("energyreporttable");
	if( table ) { 
		for(var i = 1; i < table.rows.length; i++) {
			var className = i % 2 ? "Row1" : "Row2";
			table.rows[i].className = className;
		}
	}

	enableAction(ACTION_REFRESH);
	startstopinit();
}


function onCommand(cmd, cnf) {
	if( cnf != null && !confirm(document.getElementById(cnf).value) )
		return;

	document.getElementById("cmd").value = cmd;
	MTstartServerComm();
	document.getElementById("energycommand").submit();
}
