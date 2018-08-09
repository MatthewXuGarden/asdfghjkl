package com.carel.supervisor.presentation.session;

import java.io.Serializable;
import java.util.Properties;


public abstract class Transaction implements Serializable
{
    private transient Properties graphParameter = null;
    private transient Properties systemParameter = null;
    private transient Properties logicDeviceParameter = null;
    private transient Properties logicVariableParameter = null;

    public abstract void setIdDevices(int[] idDevices);
    public abstract void setIdDevicesCombo(int[] idDevices);

    public abstract void setIdDevices(int[] idDevices, String[] description)
        throws Exception;

    public abstract int[] getIdDevices();
    public abstract int[] getIdDevicesCombo();

    public Properties getGraphParameter()
    {
        return graphParameter;
    } //getGraphParameter

    public void setGraphParameter(Properties graphParameter)
    {
        this.graphParameter = graphParameter;
    } //setGraphParameter

    public Properties getSystemParameter()
    {
        return systemParameter;
    } //getSystemParameter

    public void setSystemParameter(Properties systemParameter)
    {
        this.systemParameter = systemParameter;
    } //setSystemParameter

    public Properties getLogicDeviceParameter()
    {
        return logicDeviceParameter;
    } //getLogicDeviceParameter

    public void setLogicDeviceParameter(Properties logicDeviceParameter)
    {
        this.logicDeviceParameter = logicDeviceParameter;
    } //setLogicDeviceParameter

    public Properties getLogicVariableParameter()
    {
        return logicVariableParameter;
    } //getLogicVariableParameter

    public void setLogicVariableParameter(Properties logicVariableParameter)
    {
        this.logicVariableParameter = logicVariableParameter;
    } //setLogicVariableParameter
}
