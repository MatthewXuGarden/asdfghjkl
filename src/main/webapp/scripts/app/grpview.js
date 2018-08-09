function grpviewLoad()
{
	if(typeof(Lsw9) != "undefined" && typeof(Lsw9.mData) != "undefined" && Lsw9.mData.length>0)
		enableAction(1);
	if(typeof(Lsw1) != "undefined" && typeof(Lsw1.mData) != "undefined" && Lsw1.mData.length>0)
		enableAllAction();
	resizeTableTab1();
	resizeTableTab3();
}

function grpviewLoadTab2()
{
	if(typeof(aValue)!="undefined" && aValue.length>0){
		enableAllAction();
	}
	resizeTableTab1();
	resizeTableTab3();
}

function grpviewLoadTab3()
{
	if(typeof(Lsw1) != "undefined" && typeof(Lsw1.mData) != "undefined" && Lsw1.mData.length>0)
		enableAllAction(1);
	if(typeof(Lsw2) != "undefined" && typeof(Lsw2.mData) != "undefined" && Lsw2.mData.length>0)
		enableAction(2);
	resizeTableTab1();
	resizeTableTab3();
}

function var_group_save()
{
	var ofrm = document.getElementById("frm_groupvarparam");
	if(ofrm != null)
			MTstartServerComm();
	ofrm.submit();
}

function resizeTableTab1()
{
	var hdev = MTcalcObjectHeight("trDevList");
	var halr = MTcalcObjectHeight("trAlrList");
	if(hdev != 0)
		MTresizeHtmlTable(9,hdev-60);
	if(halr != 0)
		MTresizeHtmlTable(1,halr-60);
} 

function resizeTableTab3()
{
	var h1 = MTcalcObjectHeight("trActAlrList");
	var h2 = MTcalcObjectHeight("trRclAlrList");
	if(h1 != 0)
		MTresizeHtmlTable(1,h1-60);
	if(h2 != 0)
		MTresizeHtmlTable(2,h2-60);
} 
