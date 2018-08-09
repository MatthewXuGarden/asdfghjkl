//Initialization
$(document).ready();

var divW;
var divH;
var clientH;	
var clientW;	
var divTitle;	
var pageUrl;	
var div_X;	
var div_Y;	
var runCount = 0;

function DivWindowOpen(divWidth,divHeight,title,url,destArgs,pageType){
	if(runCount==0){
		divW = divWidth;	
		divH = divHeight;	
		divTitle = title;	
		pageUrl = url;	//DIV loading page
		lockScreen();	//lock main page
		divOpen();
		$("#divTitle").append(divTitle);
	}
	$("#divContent").load(pageUrl, function(response, status, xhr){
		runCount = runCount + 1;
		if (status == "success") {	
			if(pageType=="graphDialog"){
				doInit(destArgs);
			}
			if(pageType=="graphLayout"){
				initializeGraphConfig(destArgs);
			}
			runCount = 0;
		}
		if (status == "error" && runCount<4) {
			DivWindowOpen(divWidth,divHeight,title,url,destArgs,pageType);
		}
	});
	
	//change close gif
	$("#x").hover(
		function(){
			$(this).attr("src","images/dbllistbox/delete_on.png");
		},
		function(){
			$(this).attr("src","images/dbllistbox/delete_on.png");
		}
	);
	
	$("#x").click(
		function(){
			clearDivWindow();
			clearLockScreen();
		}
	);

}

function closeWindow(){
	clearDivWindow();
	clearLockScreen();
}

// Retrun child div coordinates
function divOpen(){
	var minTop = 80;	
	if($("#divWindow").length == 0){
//		clientH = $(window).height();	
//		clientW = $(window).width();
		clientH = document.body.scrollHeight;	
		clientW = document.body.scrollWidth;
		div_X = (clientW - divW)/2;	
		div_Y = (clientH - divH)/2;	
		div_X += window.document.documentElement.scrollLeft;	
		div_Y += window.document.documentElement.scrollTop;	
		if(div_Y < minTop){
			div_Y = minTop;
		}
		$("body").append("<div id='divWindow'><div id='divTitle'><img src='images/dbllistbox/delete_on.png' id='x' /></div><div id='divContent'>Loading...</div></div>");
		
		$("#divWindow").css("position","absolute");
		$("#divWindow").css("z-index","200");
		$("#divWindow").css("left",(div_X + "px"));	
		$("#divWindow").css("top",(div_Y + "px"));	
//		$("#divWindow").css("opacity","0.9");
		$("#divWindow").width(divW);
		$("#divWindow").height(divH);
		$("#divWindow").css("background-color","#FFFFFF");
		$("#divWindow").css("border","solid 1px #333333");

		$("#divTitle").css("height","20px");
		$("#divTitle").css("line-height","20px");
		$("#divTitle").css("background-color","#333333");
		$("#divTitle").css("padding","0px 0px 1px 1px");
		$("#divTitle").css("color","#FFFFFF");
		$("#divTitle").css("font-weight","bold");

		$("#x").css("float","right");
		$("#x").css("cursor","pointer");

		$("#divContent").css("padding","10px");
	}
	else{
		clientH = $(window).height();	
		clientW = $(window).width();	
		div_X = (clientW - divW)/2;	
		div_Y = (clientH - divH)/2;	
		div_X += window.document.documentElement.scrollLeft;	
		div_Y += window.document.documentElement.scrollTop;	
		if(div_Y < minTop){
			div_Y = minTop;
		}
		$("#divWindow").css("left",(div_X + "px"));	
		$("#divWindow").css("top",(div_Y + "px"));	
	}
}

function lockScreen(){
	if($("#divLock").length == 0){	
//		clientH = $(window).height();	
//		clientW = $(window).width();	
		clientH = document.body.scrollHeight;	
		clientW = document.body.scrollWidth;
		$("body").append("<div id='divLock'></div>");	
		$("#divLock").height(clientH);
		$("#divLock").width(clientW);
		$("#divLock").css("display","block");
		$("#divLock").css("background-color","#333333");
//		$("#divLock").css("position","fixed");
		$("#divLock").css("position","absolute");
		$("#divLock").css("z-index","100");
		$("#divLock").css("top","0px");
		$("#divLock").css("left","0px");
		$("#divLock").css("opacity","0.5");
	}
	else{
		clientH = $(window).height();	
		clientW = $(window).width();	
		$("#divLock").height(clientH);
		$("#divLock").width(clientW);
	}
}

function clearLockScreen(){
	$("#divLock").remove();
}

function clearDivWindow(){
	$("#divWindow").remove();
}

$(window).resize(
		function(){
			if($("#divLock").length != 0){
				lockScreen();
			}
			if($("#divWindow").length != 0){
				divOpen();
			}
		}
);
