package com.carel.supervisor.dispatcher.plantwatch1;
import java.io.Serializable;
/*
 //------------------------------------------------------------------------------
typedef struct 
{
  int     Unit;
  int     Address;
  long    lValue;
  BYTE    bValue;
  double  dValue;
  BYTE    VarType;
  
}TVariable;
//---------------------------------------------------------------------------
typedef struct 
{
  bool        SerialModem;
  int         CommPort;
  char        Tel[200];
  char        UserName[200];
  char        Password[200];
  char        DirPath[512];
  
}TCommunicationChannel;



typedef struct 
{
  int MessageId;
  TVariable Var;
  TCommunicationChannel Comm;
  bool Result;
}TCommRec;
//---------------------------------------------------------------------------

 * */
public class PlantWatchCommRec implements Serializable {
	  /*
	   public static final int OBJECT_SHELL_SIZE   = 8;
		public static final int OBJREF_SIZE         = 4;
		public static final int LONG_FIELD_SIZE     = 8;
		public static final int INT_FIELD_SIZE      = 4;
		public static final int SHORT_FIELD_SIZE    = 2;
		public static final int CHAR_FIELD_SIZE     = 2;
		public static final int BYTE_FIELD_SIZE     = 1;
		public static final int BOOLEAN_FIELD_SIZE  = 1;
		public static final int DOUBLE_FIELD_SIZE   = 8;
		public static final int FLOAT_FIELD_SIZE    = 4;
	  */
	  //WARNING: NON CAMBIARE LE POSIZIONI DELLE VARIABILI
	  public int 	  		MessageId = 0;	  
	  public int     		Unit = 0;
	  public int     		Address = 0;
	  public long    		lValue = 0;
	  public byte    		bValue = 0;
	  public double  		dValue = 0;
	  public byte    		VarType = 0;
	  public byte    	    SerialModem = 0;
	  public byte  			Result = 0;
	  public int         	CommPort = 0;
	  public char        	Tel[] = new char[200];
	  public char        	UserName[] = new char[200];
	  public char        	Password[] = new char[200];
	  public char        	DirPath[] = new char[512];
	  public char        	Modem[] = new char[512];
	  public int 	  		Ident= 0;
	  public long           FromDate = 0;
	  public long           ToDate = 0;
	  
	  
}
