/*
 * Questa funzione permette all'utente 
 * di accedere alla pagina di dettaglio dispositivo.
 * Parametri:
 * - idDevice: Id del dispositivo. Si recupera da CurrUnit.getId();
 */
function PVP_goToDetail(idDevice)
{
	PVPK_goToDetail(idDevice);	
}

/*
 * Questa funzione permette all'utente 
 * di accedere alla pagina delle variabili in sola lettura.
 * Parametri:
 * - sDescription: Descrizione da dare all'utente nel NAVMENU
 */
function PVP_goToVariables(sDescription)
{
	if(!sDescription)
		sDescription = "";
	PVPK_goToMoreVars(sDescription);	
}

/*
 * Questa funzione permette all'utente 
 * di avere una callback una volta che la pagina è stata caricata.
 * Parametri:
 * - sFolder: Cartella della mappa;
 * - sResource: Nome del file JSP chiamato;
 * - sDevice: Nome della cartella del dispositivo; 
 *	 [Presente solo se in dettaglio dispositivo]
 */
function PVP_OnLoad(sFolder,sResource,sDevice)
{
	PVPK_ActiveRefresh(2);
	PVPK_addButtons();
}

/*
 * Questa funzione permette all'utente 
 * di operare sui valori dei parametri che si vogliono modificare
 * prima e dopo l?invio al server, inserendo codice javascript 
 * prima e/o dopo la chiamata alla funzione architetturale.
 * Parametri:
 * - obj: oggetto che ha scatenato la chiamata, tipicamente 
 * 	un pulsante di conferma in pagina
 */
function PVP_setData(obj)
{
	PVPK_setData(obj);      
}