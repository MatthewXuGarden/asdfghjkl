package com.carel.supervisor.plugin.energy;

public class EnergyReportRecord {

	private Float kgco2;
	private int id;
	private String name;
	private Float kw;
	private Float kwh;
	private Float cost;
	
	private double anKWh[];
	private double anCost[];

	public EnergyReportRecord(int id, String name, Float kw, Float kwh, Float kgco2, Float cost,
			double anKWh[], double anCost[]) {
		this.id = id;
		this.name = name;
		this.kw = kw;
		this.kwh = kwh;
		this.kgco2 = kgco2;
		this.cost = cost;
		// time slot related data
		this.anKWh = anKWh;
		this.anCost = anCost;
	}

	public Float getKgco2() {
		return kgco2;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Float getKw() {
		return kw;
	}
	
	// always return the value from fixed cost log
	public Float getKwhCsv()
	{
		return kwh;
	}
	
	
	public Float getKwh() {
//		return EnergyMgr.getInstance().getStringProperty("active_cfg").equals("time_slot")
//			? anKWh != null ? new Float(anKWh[EnergyProfile.TIMESLOT_NO]) : kwh
//			: kwh;
		//kevin, return kwh always from diff, not from anKWh.
		//because from anKWh, if there is vacuum in the middle, kwh will be lost, value is not accurate(less than the actual)
		return kwh;
	}

	public Float getCost() {
		return EnergyMgr.getInstance().getStringProperty("active_cfg").equals("time_slot")
			? anCost != null ? new Float(anCost[EnergyProfile.TIMESLOT_NO]) : cost
			: cost;
	}
	
	public double[] getKWhTS() {
		return anKWh;
	}
	
	public double[] getCostTS() {
		return anCost;
	}

	public double[] getKWhTSinPercent()
	{
		double anPercents[] = new double[EnergyProfile.TIMESLOT_NO];
		if( anKWh != null && anKWh[anKWh.length - 1] != 0 ) {
			for(int i = 0; i < anPercents.length; i++)
				anPercents[i] = anKWh[i] / anKWh[anKWh.length - 1] * 100;
		}
		return anPercents;
	}
	
	public double[] getCostTSinPercent()
	{
		double anPercents[] = new double[EnergyProfile.TIMESLOT_NO];
		if( anCost != null && anCost[anKWh.length - 1] != 0 ) {
			for(int i = 0; i < anPercents.length; i++)
				anPercents[i] = anCost[i] / anCost[anKWh.length - 1] * 100;
		}
		return anPercents;
	}
	
	public String toString(){
		return "("+this.id+", "+
		this.name+", "+
		this.kw+", "+
		this.kwh+", "+
		this.kgco2+", "+
		this.cost+")";
	}
}
