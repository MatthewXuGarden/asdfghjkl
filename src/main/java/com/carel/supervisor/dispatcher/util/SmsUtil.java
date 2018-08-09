package com.carel.supervisor.dispatcher.util;

import java.util.ArrayList;

public class SmsUtil {

	
	//private static final Short ESC_BYTE = new Short((short) 27);
 	
	// ISO-8859-1 - GSM 03.38 character map
	private static final short[] isoGsmMap = {
	  // Index = GSM, { ISO }
	    0, 163,  36, 165, 232, 233, 249, 236, 242, 199,  10, 216,
	   248,  13, 197, 229,   0,  95,   0,   0,   0,   0,   0,   0,
	     0,   0,   0,   0, 198, 230, 223, 201,  32,  33,  34,  35,
	   164,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
	    48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,
	    60,  61,  62,  63, 161,  65,  66,  67,  68,  69,  70,  71,
	    72,  73,  74,  75,  76,  77,  78,  79,  80,  81,  82,  83,
	    84,  85,  86,  87,  88,  89,  90, 196, 214, 209, 220, 167,
	   191,  97,  98,  99, 100, 101, 102, 103, 104, 105, 106, 107,
	   108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119,
	   120, 121, 122, 228, 246, 241, 252, 224
	};

	// Extended ISO-8859-1 - GSM 03.38 character map
	/*private static final short[][] extIsoGsmMap = {
	  //{ {Ext GSM,ISO} }
	  { 10, 12}, { 20, 94}, { 40,123}, { 41,125}, { 47, 92},
	  { 60, 91}, { 61,126}, { 62, 93}, { 64,124}, {101,164}
	};*/

	
	public static byte[] convert88591ToGsm0338(String isoMsg)
	{			
    	byte[] dataIsoBytes = isoMsg.getBytes();//bbuf.array();
		ArrayList<Short> dataGsm = new ArrayList<Short>();
		
	    for (int dataIndex = 0; dataIndex < dataIsoBytes.length; dataIndex++) {
	      byte currentDataIso = dataIsoBytes[dataIndex];
     	      	      
	      // Search currentDataGsm in the isoGsmMap
	      short currentDataGsm = findGsmChar(currentDataIso);

	      // If the data is not available in the isoGsmMap, search in the extended
	      // ISO-GSM map (extIsoGsmMap)
	      /* if (currentDataGsm == -1) {
	         currentDataGsm = findExtGsmChar(currentDataIso);

	        // If the character is found inside the extended map, add escape byte in
	        // the return byte[]
	        if (currentDataGsm != -1) {
	          dataGsm.add(ESC_BYTE);
	        }
	      }*/
	      
	      // only the 'regular' symbols are added to the SMS text.
	      // The special ones that are: ^ { } \ [ ~ ] | € are replaced with 'blank'
	      // The same replacement is done also for '@' character. Although its encoding is included in
	      // GSM 03.38 this is necessary to temporary avoid problems on 'commsms' dll.
	      // TODO: fix this problem inside 'commsms' dll.
	      if (currentDataGsm != -1)
	    	  dataGsm.add(new Short(currentDataGsm));
	    }

	    Short[] dataGsmShortArray = (Short[]) dataGsm.toArray(new Short[0]);
	    
	    return translateShortToByteArray(dataGsmShortArray);
	}
	    
	private static short findGsmChar(byte isoChar) {
	  short gsmChar = -1;
    
	  for (short mapIndex = 0; mapIndex < isoGsmMap.length; mapIndex++) {
		  if (isoGsmMap[mapIndex] == (short)(isoChar&0xFF)) {
		      gsmChar = mapIndex;
		      break;
		  }
	  }

	  return gsmChar;
	}  
	  
	/* private static short findExtGsmChar(byte isoChar) {
	   short gsmChar = -1;

	   for (short mapIndex = 0; mapIndex < extIsoGsmMap.length; mapIndex++) {
	     if (extIsoGsmMap[mapIndex][1] == isoChar) {
	       gsmChar = extIsoGsmMap[mapIndex][0];
	       break;
	     }
	   }

	   return gsmChar;
	   } */

    private static byte[] translateShortToByteArray(Short[] shortArray) {
      byte[] byteArrayResult = new byte[shortArray.length];

      for (int i = 0; i < shortArray.length; i++) {
        byteArrayResult[i] = (byte) shortArray[i].shortValue();
      }

      return byteArrayResult;
    }   
}
