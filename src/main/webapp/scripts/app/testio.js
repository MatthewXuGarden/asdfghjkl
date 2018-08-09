var GLB_MOD_VIS = null;
var GLB_DEVICE = null;
var GLB_LIBERO = "guidato"
var GLB_DFT_DEV = "F"


function Tio_changeModalita(obj)
{
	var sv = obj.value;
	var od = document.getElementById(sv);
	
	if(od != null) {
		if(GLB_MOD_VIS == null)
			GLB_MOD_VIS = document.getElementById(GLB_LIBERO);	
		
		if(GLB_MOD_VIS != null) {
			GLB_MOD_VIS.style.visibility = "hidden";
			GLB_MOD_VIS.style.display    = "none";
		}
		
		GLB_MOD_VIS = od;
		GLB_MOD_VIS.style.visibility = "visible";
		GLB_MOD_VIS.style.display    = "block";
	}
}

function Tio_changeDevice(obj)
{
	var sv = obj.value;
	Tio_displaySelect(sv);
}

function Tio_displaySelect(strname)
{
	var os = document.getElementById(strname);
	if(os != null)
	{
		if(GLB_DEVICE == null)
			GLB_DEVICE = document.getElementById(GLB_DFT_DEV);
		
		if(GLB_DEVICE != null) {
			GLB_DEVICE.style.visibility = "hidden";
			GLB_DEVICE.style.display    = "none";
		}
		
		GLB_DEVICE = os;
		GLB_DEVICE.style.visibility = "visible";
		GLB_DEVICE.style.display    = "block";
	}
}

function setAddress(obj)
{
	document.getElementById("tioaddress").value = obj.value;
}

function Tio_exec()
{
	new AjaxRequest('servlet/ajrefresh', 'GET','cmd=IOTEST', '', false);
	var ofrm = document.getElementById("frmtio");
	if(ofrm != null)
			MTstartServerComm();
	document.getElementById("tested").value='true';
	ofrm.submit();
}

function onload_testio()
{
	var tested = document.getElementById("tested").value;
	if (tested=="true")
		alert(document.getElementById("testresult").value);
}
