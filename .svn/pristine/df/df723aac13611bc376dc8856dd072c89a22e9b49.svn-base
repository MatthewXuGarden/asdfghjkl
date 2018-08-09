
function sendLogOnMail()
{
	var path = "";
	var mail_body = document.getElementById('mail_body').value;
	var dest_email = document.getElementById('dest_email').value;
	CommSend("servlet/error", "POST", "cmd=sendmail&path="+path+"&mail_body="+mail_body+"&dest_email="+dest_email+"&local=false","sendLogOnMail_back()",true);
}

function Callback_sendLogOnMail_back()
{
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("response")[0] != null)
	{
		var msg = xmlResponse.getElementsByTagName("response")[0].childNodes[0].nodeValue;
	
		var showmsg = "";
		if(msg == "ERROR_MAIL")
		{
			showmsg = document.getElementById("mail_error_conf").value;
			document.getElementById("msg1").innerHTML = showmsg; 

		}
		if(msg == "ERROR")
		{
			showmsg = document.getElementById("mail_error").value;
			document.getElementById("msg1").innerHTML = showmsg;
		}
		if(msg == "ok")
		{
			showmsg = document.getElementById("mail_sent").value;
			document.getElementById("msg1").innerHTML = showmsg;
		}
	}
}

function savelog_savefile(local,path,filename)
{
	path += ".zip";
	CommSend("servlet/error", "POST", "cmd=savelog&path="+path+"&filename="+filename+"&local="+local,"savelog_back("+local+")",true);
}

function Callback_savelog_back(local)
{
	MTstopServerComm();
	if (xmlResponse!=null && xmlResponse.getElementsByTagName("file")[0] != null)
	{
		var filename = xmlResponse.getElementsByTagName("file")[0].childNodes[0].nodeValue;
		var msg = "";
		if(filename == "ERROR")
		{
			msg = document.getElementById("save_error").value;
			alert(msg);
		}
		else
		{
			if(local == true)
			{
					msg = document.getElementById("save_confirm").value;
					alert(msg + filename);
			}
			else
			{
				var sUrl = top.frames["manager"].getDocumentBase() + "app/report/popup.html?"+filename;
				window.open(sUrl);
			}
		}
	}
}
