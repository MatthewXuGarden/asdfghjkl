package com.carel.supervisor.director.graph;

import com.carel.supervisor.dataaccess.dataconfig.VariableInfo;


public class NormalizeSerie
{
    public static final int TYPE_A = 0;
    public static final int TYPE_D = 1;
    private float[][] ySerie = null;
    private Float startData = null;
    private Float endData = null;
//    private Float granularity = null;
//    private Float deltaNormalized = null;
    private StringBuffer cosmeticParameter = null;
    private int type = 0;
    private FlashObjParameters flashParameters = null;
    private Float maxValueCurve= null;
    private Float minValueCurve= null;
    // BUG 4898
    // "old" average: sum(avg(i))/number of samples
    // private Float avgValueCurve= null;
    // new average: sum(i)/number of samples 
    private Double avgActualValueCurve= null;
    
    private int   xMaxValue= 0;
    private int   xMinValue= 0;
    
    private static final float NULLP = Float.MIN_VALUE;
	private static final char DATUM_SEPARATOR = '|';
	private static final char VALUE_SEPARATOR = '@';
	private static final char NULL_VALUE = 'n';
    
    public NormalizeSerie()
    {
    } //NormalizeSerie

    public float[][] startNormalize(YGraphCoordinates[] coordinates,
        FlashObjParameters flashParameters,Object [] informations, GraphRequest graphRequest)
    {
        this.flashParameters=flashParameters;
    	switch (type)
        {
	        case TYPE_A:
	        	findMinMaxAvgValue(coordinates, graphRequest);
	        	if(informations[7]==null) //se ymin==null
	        		return startNormalizedAnalogic(coordinates);
	        	else
	        		return startNormalizedAnalogic(coordinates,informations);
	        case TYPE_D:
	            return startNormalizedDigital(coordinates, flashParameters);
	    }
       return null;
    } //startNormalize

//    public Float getGranularity()
//    {
//        return granularity;
//    } //getGranularity

    public Float getStartData()
    {
        return startData;
    } //getStartData

    public Float getEndData()
    {
        //Questo sarebbe il corretto ma per 3 pixel va bene ache l'altro il che impliCa fondo scala y visivamente corretta
    	/*if (startData != null)
            return new Float(startData.floatValue() +
                ((this.flashParameters.getYHeightAnalogic() +
                this.flashParameters.getYHeightEmpty()) * granularity.floatValue()));
         */
        return endData;
//    	if (startData != null)
////    		return new Float(startData.floatValue() +
////    				         this.flashParameters.getYHeightAnalogic() * granularity.floatValue());
//    	
//        return null;
    }

    // 20090126 - getXMLNormalized Method modified in order to reduce the amount of space used for data encoding
    // Data now is encoded in "CSV"-fashion (|v1@v2@v3|v1@v2@v3|...) instead of the more space-consuming
    // traditional XML way (one XML node for each datum, with atomic values as attributes)
    public String getXMLNormalized()
    {
    	//NumberFormat nf = NumberFormat.getCurrencyInstance(inLocale); // warning: NumberFormat is NOT thread safe. Do not make this a singleton
        StringBuffer xmlSerie = new StringBuffer();
        // 20090126 - Changed old tag "curva" (italian) to "graph"
        xmlSerie.append("<graph");
        xmlSerie.append(cosmeticParameter);
        xmlSerie.append("maxValue=\"");
        xmlSerie.append(maxValueCurve==null?"":String.valueOf(maxValueCurve));
        xmlSerie.append("\" ");
        xmlSerie.append("xMaxValue=\"");
        xmlSerie.append(maxValueCurve==null?"":String.valueOf(xMaxValue));
        xmlSerie.append("\" ");
        
        xmlSerie.append("minValue=\"");
        xmlSerie.append(minValueCurve==null?"":String.valueOf(minValueCurve));
        xmlSerie.append("\" ");
        xmlSerie.append("xMinValue=\"");
        xmlSerie.append(minValueCurve==null?"":String.valueOf(xMinValue));
        xmlSerie.append("\" ");
        
        // fix BUG 4898 - start
        // xmlSerie.append("avgValue=\"");
        // xmlSerie.append(avgValueCurve==null?"":String.valueOf(avgValueCurve));
        // xmlSerie.append("\" ");
        
        xmlSerie.append("avgValue=\"");
        xmlSerie.append(avgActualValueCurve==null?"":String.valueOf(avgActualValueCurve));
        xmlSerie.append("\" ");
        // fix BUG 4898 - end
        
        xmlSerie.append(">"); //aggiungere i parametri di configurazione che probabilmente mi arriveranno dalla servlet

        /*new graph*/
//        xmlSerie.append("<d L=\"");
//        xmlSerie.append(ySerie[0][1]);
//        xmlSerie.append("\" U=\"");
//        xmlSerie.append(ySerie[0][0]);
//        xmlSerie.append("\" t='"+0+"'/>"); /*new graph*/
        /*new graph*/
        
        // 20090116 - new tag "gdata" which contains all the data for a curve in a "CSV"-style
        xmlSerie.append("<gdata>");
        
        for (int i = 0; i < this.flashParameters.getXWidth()-1; i++)
        {
            /*new graph*/
        	// If the lower and upper data don't change over a temporal slice (from t to t+1), there's no need
        	// to stream the datum to the client. Thus, they're filtered, in order to save space and 
        	// therefore improve the overall performance.
        	if(ySerie[i][1] != ySerie[i+1][1] || ySerie[i][0] != ySerie[i+1][0]){
        		csvEncode(xmlSerie, ySerie[i][1], ySerie[i][0], i);

        		// ENHANCEMENT 20090202 see method csvEncode
//        		xmlSerie.append("<d L=\"");
//                xmlSerie.append(ySerie[i][1]);
//                xmlSerie.append("\" U=\"");
//                xmlSerie.append(ySerie[i][0]);
//                xmlSerie.append("\" t='"+i+"'/>"); /*new graph*/
        	}
        	/*new graph*/
        } //for
        /*new graph*/
        
        // ENHANCEMENT 20090202 - commented
//      xmlSerie.append("<d L=\"");
//      xmlSerie.append(ySerie[this.flashParameters.getXWidth()-1][1]);
//      xmlSerie.append("\" U=\"");
//      xmlSerie.append(ySerie[this.flashParameters.getXWidth()-1][0]);
//      xmlSerie.append("\" t='"+this.flashParameters.getXWidth()+"'/>"); /*new graph*/
        
        // Last datum is never filtered  
        csvEncode(xmlSerie, ySerie[this.flashParameters.getXWidth()-1][1],
        		  ySerie[this.flashParameters.getXWidth()-1][0],
        		  this.flashParameters.getXWidth());
        
        /*new graph*/
        xmlSerie.append("</gdata>");
        xmlSerie.append("</graph>");

        return xmlSerie.toString();
    } //startNormalize

    // 20090126 new method for formatting the datum "CSV"-style
    private void csvEncode(StringBuffer sb, float yLowerValue, float yUpperValue, int t) {
    	sb.append(DATUM_SEPARATOR); // datum separator
    	if (yLowerValue == NULLP || yUpperValue == NULLP) {
    		sb.append(NULL_VALUE); // y lower value
    		sb.append(VALUE_SEPARATOR); // value separator
    		sb.append(NULL_VALUE); // y upper value

    	} else {
    		sb.append(yLowerValue); // y lower value
    		sb.append(VALUE_SEPARATOR); // value separator
    		sb.append(yUpperValue); // y upper value
    	}
    	sb.append(VALUE_SEPARATOR); 
		sb.append(t); // time (x)
    }
    
    public float[][] getYSerie()
    {
        return ySerie;
    } //getYSerie

    public void setParameters(Object[] objects)
    {
        int type = new Integer((String) objects[3]).intValue();

        if ((type == VariableInfo.TYPE_ALARM) ||
                (type == VariableInfo.TYPE_DIGITAL))
            objects[3] = new String("D");
        else
            objects[3] = new String("A");

        cosmeticParameter = new StringBuffer();
        cosmeticParameter.append(" id=\"");
        cosmeticParameter.append(objects[1]);
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" type=\"");
        cosmeticParameter.append(objects[3]);
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" descr=\"");
        cosmeticParameter.append(objects[2]); //objects[2]);
//        cosmeticParameter.append("\"");
//        cosmeticParameter.append(" step=\"");
//        cosmeticParameter.append((getGranularity() == null) ? new Float(0)
//                                                            : getGranularity());
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" minScale=\"");
        cosmeticParameter.append((getStartData() == null) ? new Float(0)
                                                          : getStartData());
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" maxScale=\"");
        cosmeticParameter.append((getEndData() == null) ? new Float(0)
                                                        : getEndData());
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" mu=\"");
        cosmeticParameter.append(prepareUnitMeasurement4Flash(new String((String) objects[4])));
        cosmeticParameter.append("\"");
        cosmeticParameter.append(" color=\"");
        cosmeticParameter.append("0x"+((String) objects[5]));
        cosmeticParameter.append("\" ");
        
       
        cosmeticParameter.append(" device=\"");
        cosmeticParameter.append(objects[6]);
        cosmeticParameter.append("\" ");
        
        
    } //

    public void setType(int type)
    {
        this.type = type;
    } //setType

    private float[][] startNormalizedDigital(YGraphCoordinates[] coordinates,
        FlashObjParameters flashParameters)
    {
    	this.flashParameters = flashParameters;
    	//Normalizzo
        ySerie = new float[(int) this.flashParameters.getXWidth()][2];
       
        float uniqueValue = NULLP;
        int n = 0;

        for (int i = 0; i < this.flashParameters.getXWidth(); i++)
            if (coordinates[i] != null)
                n = i;

        for (int i = 0;
                ((i < (n + 1)) && (i < this.flashParameters.getXWidth()));
                i++)
        {
            if (coordinates[i] != null)
            {
            	try{
            	ySerie[i][1] = ySerie[i][0] = (coordinates[i].getMaxValue() != null)
                            ? coordinates[i].getMaxValue().floatValue() : (NULLP);
                
                uniqueValue = (coordinates[i].getPointToPadd() != null)
                            ? coordinates[i].getPointToPadd().floatValue() : (NULLP);
            	  }//try
                catch(Exception e){
                	ySerie[i][0] = ySerie[i][1] = NULLP;
                }//catch
            	
            } //if
           
            else {
                //ySerie[i][1] = ySerie[i][0] = oldMaxValue;
            	// If a sample is missing, its lower and upper values are set by default to "uniqueValue" 
            	ySerie[i][1] = ySerie[i][0] = uniqueValue;
            }
          //  if ((ySerie[i][0] > this.flashParameters.getYHeightAnalogic())&&(i!=this.flashParameters.getXWidth()-1))
         //   	LoggerMgr.getLogger(this.getClass()).error("DEBUG:--->>>ERRORE IN NORMALIZZAZIONE sul punto "+ i );
            
            
        } //
        for (int i = n + 1; i < this.flashParameters.getXWidth(); i++)
        {
            ySerie[i][0] = NULLP;
            ySerie[i][1] = NULLP;
        } //for
        ySerie[this.flashParameters.getXWidth()-1][0] = NULLP;
        ySerie[this.flashParameters.getXWidth()-1][1] = NULLP;
    
        return ySerie;
    }

    private float[][] startNormalizedAnalogic(YGraphCoordinates[] coordinates)
    {
        //Find Max e Min
        Float maxValue = null;
        Float minValue = null;
        int n = 0;
        
        for (int i = 0; i < this.flashParameters.getXWidth()-1; i++)
        {
            if (coordinates[i] != null)
            {
                n = i;
                if ((coordinates[i].getMaxValue() != null))
                {
                    if (maxValue != null)
                    {
                        maxValue = (maxValue.floatValue() < coordinates[i].getMaxValue()
                                                                          .floatValue())
                            ? coordinates[i].getMaxValue() : maxValue;
                        minValue = (minValue.floatValue() > coordinates[i].getMinValue()
                                                                          .floatValue())
                            ? coordinates[i].getMinValue() : minValue;
                    } //if
                    else
                    {
                        maxValue = new Float(coordinates[i].getMaxValue()
                                                           .floatValue());
                        minValue = new Float(coordinates[i].getMinValue()
                                                           .floatValue());
                    } //else
                } //if
            } //if
        } //for

        //l'ultimo punto serve al padding ma non al max e min
        if (coordinates[this.flashParameters.getXWidth()-1] != null)
        {
            n = this.flashParameters.getXWidth()-1;
        }//if
        
        startData = minValue;
        endData = maxValue;

//        if (maxValue != null)
//        {
//            deltaNormalized = new Float(((maxValue.floatValue() -
//                    minValue.floatValue()) != 0)
//                    ? Math.abs((maxValue.floatValue() - minValue.floatValue()))
//                    : Math.abs(maxValue.floatValue()));
//            // 20090126 From now on, scale constant ("granularity") is computed by the client (flash application)
//            granularity = new Float(deltaNormalized.floatValue() / (float) this.flashParameters.getYHeightAnalogic());
//        } //if

        //Normalizzo
        // 20090126 From now on, Y values aren't normalized any more (now, the Y values are the measured values)
        ySerie = new float[(int) this.flashParameters.getXWidth()][2];

        float uniqueValue = NULLP;
      
        for (int i = 0;
                ((i < (n + 1)) && (i < this.flashParameters.getXWidth()));
                i++)
        {
            if (coordinates[i] != null)
            {
                try{
//                	// Normalized max
//	            	ySerie[i][0] = (coordinates[i].getMaxValue() != null)
//	                        ? (int) (((float) this.flashParameters.getYHeightAnalogic() * (coordinates[i].getMaxValue()
//	                                                                                                     .floatValue() -
//	                        startData.floatValue())) / deltaNormalized.floatValue())
//	                        : (-1);
//	                // Normalized min
//	                ySerie[i][1] =  (coordinates[i].getMinValue() != null)
//	                        ? (int) (((float) this.flashParameters.getYHeightAnalogic() * (coordinates[i].getMinValue()
//	                                                                                                     .floatValue() -
//	                        startData.floatValue())) / deltaNormalized.floatValue())
//	                        : (-1);
//	                uniqueValue=  (coordinates[i].getPointToPadd() != null)
//	                		? (int) (((float) this.flashParameters.getYHeightAnalogic() * (coordinates[i].getPointToPadd()
//	                        .floatValue() -
//	                        startData.floatValue())) / deltaNormalized.floatValue())
//	                        : (-1);

                	// Normalized max
	            	ySerie[i][0] = (coordinates[i].getMaxValue() != null) ? coordinates[i].getMaxValue().floatValue() : NULLP;
	            	// Normalized min	            	
	                ySerie[i][1] =  (coordinates[i].getMinValue() != null) ? coordinates[i].getMinValue().floatValue() : NULLP;
	                // Normalized Point to Pad
	                uniqueValue=  (coordinates[i].getPointToPadd() != null) ? coordinates[i].getPointToPadd(): NULLP;
                }//try
                catch(Exception e){
                	ySerie[i][0] = ySerie[i][1] = NULLP;
                }//catch
            } //if
            else
            {
                ySerie[i][0] = uniqueValue;
                ySerie[i][1] = uniqueValue;
            } //else

//            if ((ySerie[i][0] > this.flashParameters.getYHeightAnalogic())&&(i!=this.flashParameters.getXWidth()-1))
//            	LoggerMgr.getLogger(this.getClass()).error("Error while processing point "+ i ); 

        } //for
      
        for (int i = n + 1; i < this.flashParameters.getXWidth(); i++)
        {
            ySerie[i][0] = NULLP;
            ySerie[i][1] = NULLP;
        } //for
        ySerie[this.flashParameters.getXWidth()-1][0] = NULLP;
        ySerie[this.flashParameters.getXWidth()-1][1] = NULLP;
        return ySerie;
    }
    
    
    private float[][] startNormalizedAnalogic(YGraphCoordinates[] coordinates,Object [] informations)
        {
            //Find Max e Min
            Float minValue= new Float(((Float)informations[7]).floatValue()); // range min settato dall'utente?
            Float maxValue = new Float(((Float)informations[8]).floatValue()); // range max settato dall'utente?
            int n = 0;

            for (int i = 0; i < this.flashParameters.getXWidth()-1; i++)
            {
        		if (coordinates[i] != null)
				{
					n = i;
					/* DYN: remove hi/lo filter
				    if ((coordinates[i].getMaxValue() != null))
				    {
				       if( ((coordinates[i].getMaxValue().floatValue() > maxValue.floatValue())
				    	   &&(coordinates[i].getMinValue().floatValue() > maxValue.floatValue()))
				    	   ||((coordinates[i].getMaxValue().floatValue() < minValue.floatValue())
				    	   &&(coordinates[i].getMinValue().floatValue() < minValue.floatValue())) ){
				    	   coordinates[i].setMaxValue(null);
				    	   coordinates[i].setMinValue(null);
				       }//if
				       else{
				           if(coordinates[i].getMaxValue().floatValue() > maxValue.floatValue()){
				        	   coordinates[i].setMaxValue(new Float(maxValue.floatValue()));
				           }//if
				           if(coordinates[i].getMinValue().floatValue() < minValue.floatValue()){
				        	   coordinates[i].setMinValue(new Float(minValue.floatValue()));
				           }//if
				      }//else
				      if ((coordinates[i].getPointToPadd() != null))
				      {
				    	  if((coordinates[i].getPointToPadd().floatValue()>maxValue.floatValue())||(coordinates[i].getPointToPadd().floatValue()<minValue.floatValue())){
				    		   coordinates[i].setPointToPadd(null);
				    	  }//if
				      }//if
				    } //if
				    */
				} //if
            }//for

            //l'ultimo punto serve al padding ma non al max e min
            if (coordinates[this.flashParameters.getXWidth()-1] != null)
            {
                n = this.flashParameters.getXWidth()-1;
            }//if
            startData = minValue;
            endData = maxValue;

//            if (maxValue != null)
//            {
//                deltaNormalized = new Float(((maxValue.floatValue() -
//                        minValue.floatValue()) != 0)
//                        ? Math.abs((maxValue.floatValue() - minValue.floatValue()))
//                        : Math.abs(maxValue.floatValue()));
//                granularity = new Float(deltaNormalized.floatValue() / (float) this.flashParameters.getYHeightAnalogic());
//            } //if

            //Normalizzo
            ySerie = new float[(int) this.flashParameters.getXWidth()][2];

            float uniqueValue = NULLP;

            for (int i = 0;
                    ((i < (n + 1)) && (i < this.flashParameters.getXWidth()));
                    i++)
            {
                if (coordinates[i] != null)
                {
                  try{
                	// Normalized max.
  	            	ySerie[i][0] = (coordinates[i].getMaxValue() != null) ? coordinates[i].getMaxValue().floatValue() : NULLP;
	            	// Normalized min	            	
	                ySerie[i][1] =  (coordinates[i].getMinValue() != null) ? coordinates[i].getMinValue().floatValue() : NULLP;
	                // Normalized Point to Pad
	                uniqueValue=  (coordinates[i].getPointToPadd() != null) ? coordinates[i].getPointToPadd(): NULLP;
                  }//try
                  catch(Exception e){
                  	ySerie[i][0] = ySerie[i][1] = NULLP;
                  }//catch
                } //if
                else
                {
                    ySerie[i][0] = uniqueValue;
                    ySerie[i][1] = uniqueValue;
                } //else
//                if ((ySerie[i][0] > this.flashParameters.getYHeightAnalogic())&&(i!=this.flashParameters.getXWidth()-1))
//                	LoggerMgr.getLogger(this.getClass()).error("Error while processing point "+ i );

            } //for

            for (int i = n + 1; i < this.flashParameters.getXWidth(); i++)
            {
                ySerie[i][0] = NULLP;
                ySerie[i][1] = NULLP;
            } //for
            ySerie[this.flashParameters.getXWidth()-1][0] = NULLP;
            ySerie[this.flashParameters.getXWidth()-1][1] = NULLP;
            return ySerie;
        }
    
    
	private void findMinMaxAvgValue(YGraphCoordinates[] coordinates, GraphRequest graphRequest){
		int n=0;
		for (int i = 0; i < this.flashParameters.getXWidth();i++)
			if (coordinates[i] != null)
				n=i;	
		if(n==0)
			return;
//		float avgValue=0;
//		int countAvg=0;
	
		for (int i = 0; i < this.flashParameters.getXWidth()-1;i++){
			if (coordinates[i] != null){
				if(coordinates[i].getMaxValue()!=null){
					if(maxValueCurve==null){
						maxValueCurve=coordinates[i].getMaxValue();
						xMaxValue=i;
					}//if
					else
						if(maxValueCurve.floatValue()<coordinates[i].getMaxValue().floatValue()){
							maxValueCurve=coordinates[i].getMaxValue();
							xMaxValue=i;
						}//if
					if(minValueCurve==null){
						minValueCurve=coordinates[i].getMinValue();
						xMinValue=i;
					}//if
					else
						if(minValueCurve.floatValue()>coordinates[i].getMinValue().floatValue()){
							minValueCurve=coordinates[i].getMinValue();
							xMinValue=i;
						}//if
					
					// "old" average: sum(avg(i))/number of samples
//					avgValue+= (maxValueCurve.floatValue()+minValueCurve.floatValue())/2;
//					countAvg++;
				}//if
			}//if
		}//for
		
		// fix BUG 4898 - start
//		if(countAvg!=0) {
//			avgValueCurve= new Float(avgValue/countAvg);
//		}
		double avg = graphRequest.getAvg();
		if (!Double.isNaN(avg)) {
			// new average: sum(i)/number of samples
			avgActualValueCurve = new Double(avg);
		}		
		// fix BUG 4898 - end
			
	}//findMinMaxValue
	
	public String prepareUnitMeasurement4Flash(String string){
		//string=string.trim().replace("%","x100"); //gestita a livello js con codifica
		string=string.replace("&deg","°");
		string=string.replace("&micro","µ");
		return string;
	}//prepareUnitMeasurement4Flash

}//Class NormalizeSerie
