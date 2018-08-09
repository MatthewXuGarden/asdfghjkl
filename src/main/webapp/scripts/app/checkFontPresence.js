// Lo script crea livelli invisibili contenenti lo stesso testo nei vari font da verificare,
// se il font non è disponibile lo scrive in courier (lo fa di default x il primo livello).
// Confrontando la lunghezza dei livelli con quella del primo determina la presenza dei font.
// Il controllo potrebbe fallire perchè: 
// - il testo in un determinato font occupa lo stesso spazio scritto in courier
// - in alcuni browser non è possibile rilevare in questo modo la lunghezza del livello 
// (x es. OP5, dovrebbe fallire silenziosamente)

// copyleft 2k+3 xinod@html.it
// sostituisci nell' array caratteri i nomi dei font di cui vuoi verificare la presenza, ma lascia il primo vuoto

/*----Funzione che verifica la presenza del font nel SO----*/
function check_fonts(){
  var caratteri = new Array('','Arial Unicode MS');
  var risultati = new Array();
  var myStr = "stringa&nbsp;per&nbsp;controllare&nbsp;la&nbsp;lunghezza&nbsp;del&nbsp;testo";

		// scrive i livelli
		for(var k=0; k<caratteri.length; k++){
		      /*----create div----*/
          mydiv = document.createElement("div");      //crea blocco div
          mydiv.id = 'font'+k;                    //assegna al div un nome
          document.body.appendChild(mydiv);           //appende al documento html il blocco
          
          mynewdiv = document.getElementById('font'+k);  //lo trova
          mynewdiv.innerHTML = myStr;
          
          mynewdiv.style.position    = 'absolute';
          mynewdiv.style.top         = '1'+(k+1)*10;
          mynewdiv.style.left        = '0';
          mynewdiv.style.visibility  = 'hidden';
          mynewdiv.style.fontFamily = caratteri[k];
          //mynewdiv.innerHTML = str;
			}//for
		
    // confronta le lunghezze
    for(k=0; k<caratteri.length; k++){
			  var len = document.getElementById('font'+k).offsetWidth;
        if(len!=undefined) {       //--se la lunghezza è buona...
          risultati.push(len);
				  }
		    }
    
    if (risultati[0]==risultati[1]){
        document.getElementById('fontInfo').style.display='inline';
        }//
  }//controllo_fonts


