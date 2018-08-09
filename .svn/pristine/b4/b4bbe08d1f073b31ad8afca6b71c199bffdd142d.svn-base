package com.carel.supervisor.presentation.xmllibrary;

public class Flash
{
    public static String CreateHML(int gaugeNumber, String[][] attribute,
        String name)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            XMLibrary.append("\n\n\n<Item Name=\"\" Icon=\"" + name + (i + 1) +
                "Ico.png\" Tooltip=\"%%\" Image=\"" + name + (i + 1) +
                "Img.png\" Type=\"\"	DefaultContent=\"\">");
            XMLibrary.append("\n<ReplaceableTags>");
            XMLibrary.append(
                "\n<Tag Destination=\"$SET$\" Value=\"&lt;div  id='flashset$IDOBJECT$'&gt;");

            for (int j = 0; j < (attribute[i].length - 1); j++)
            {
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append(";");
                XMLibrary.append("$");
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append("$;");
            } //for

            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append(";");
            XMLibrary.append("$");
            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append("$");
            XMLibrary.append("&lt;/div&gt;\" />  ");
            XMLibrary.append(
                "\n    <Tag Destination=\"$INPUT$\" Value=\" &lt; input name='input$IDOBJECT$'  value='variable=$TheVar$' &gt;\" />");
            XMLibrary.append(
                "\n    <Tag Destination=\"$OBJECT$\" Value=\" &lt; embed id='flashobj$IDOBJECT$' style='position:absolute;top:$TOP$;left:$LEFT$;width:$WIDTH$px;height:$HEIGHT$px;z-index:2;' wmode='transparent' src='flash/maps/" +
                name + (i + 1) +
                ".swf' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'&gt; &lt; /embed &gt;\" />");
            XMLibrary.append("\n</ReplaceableTags>");
            XMLibrary.append("\n<DynamicProperties>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <Property Name=\"$" + attribute[i][j] +
                    "$\" Descr=\"%%\" Type=\"\" />");
            } //for

            XMLibrary.append(
                "\n    <Property Name=\"$TheUnit$\" Descr=\"%%\" Type=\"Device\" />");
            XMLibrary.append(
                "\n    <Property Name=\"$TheVar$\" Descr=\"%%\" Type=\"Variable\" DependsOn=\"$TheUnit$\" />");
            XMLibrary.append("\n</DynamicProperties>");
            XMLibrary.append("\n</Item>");
        } //for

        return XMLibrary.toString();
    }

    public static String CreateHML(int gaugeNumber, String[][] attribute,
        String name, int startItem)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            String itemName = "Item" + (startItem + i);
            XMLibrary.append("\n\n\n<Item Name=\"" + itemName + "\" Icon=\"" +
                name + (i + 1) + "Ico.png\" Tooltip=\"%" + itemName +
                ".Tooltip%\" Image=\"" + name + (i + 1) +
                "Img.png\" Type=\"\"	DefaultContent=\"\">");
            XMLibrary.append("\n<ReplaceableTags>");
            XMLibrary.append(
                "\n<Tag Destination=\"$SET$\" Value=\"&lt;div  id='flashset$IDOBJECT$'&gt;");

            for (int j = 0; j < (attribute[i].length - 1); j++)
            {
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append(";");
                XMLibrary.append("$");
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append("$;");
            } //for

            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append(";");
            XMLibrary.append("$");
            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append("$");
            XMLibrary.append("&lt;/div&gt;\" />  ");
            XMLibrary.append(
                "\n    <Tag Destination=\"$INPUT$\" Value=\" &lt; input name='input$IDOBJECT$'  value='variable=$TheVar$' &gt;\" />");
            XMLibrary.append(
                "\n    <Tag Destination=\"$OBJECT$\" Value=\" &lt; embed id='flashobj$IDOBJECT$' style='position:absolute;top:$TOP$;left:$LEFT$;width:$WIDTH$px;height:$HEIGHT$px;z-index:2;' wmode='transparent' src='flash/maps/" +
                name + (i + 1) +
                ".swf' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'&gt; &lt; /embed &gt;\" />");
            XMLibrary.append("\n</ReplaceableTags>");
            XMLibrary.append("\n<DynamicProperties>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <Property Name=\"$" + attribute[i][j] +
                    "$\" Descr=\"%" + itemName + ".P_$" + attribute[i][j] +
                    "$_Descr" + "%\" Type=\"\" />");
            } //for

            XMLibrary.append("\n    <Property Name=\"$TheUnit$\" Descr=\"%" +
                itemName + ".P_$TheUnit$_Descr" + "%\" Type=\"Device\" />");
            XMLibrary.append("\n    <Property Name=\"$TheVar$\" Descr=\"%" +
                itemName + ".P_$TheVar$_Descr" +
                "%\" Type=\"Variable\" DependsOn=\"$TheUnit$\" />");
            XMLibrary.append("\n</DynamicProperties>");
            XMLibrary.append("\n</Item>");
        } //for

        return XMLibrary.toString();
    }

    public static String CreateHMLTank(int gaugeNumber, String[][] attribute,
        String name)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            XMLibrary.append("\n\n\n<Item Name=\"\" Icon=\"" + name + (i + 2) +
                "DIco.png\" Tooltip=\"%%\" Image=\"" + name + (i + 2) +
                "DImg.png\" Type=\"\"	DefaultContent=\"\">");
            XMLibrary.append("\n<ReplaceableTags>");
            XMLibrary.append(
                "\n<Tag Destination=\"$SET$\" Value=\"&lt;div  id='flashset$IDOBJECT$'&gt;");

            for (int j = 0; j < (attribute[i].length - 1); j++)
            {
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append(";");
                XMLibrary.append("$");
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append("$;");
            } //for

            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append(";");
            XMLibrary.append("$");
            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append("$");
            XMLibrary.append("&lt;/div&gt;\" />  ");
            XMLibrary.append(
                "\n    <Tag Destination=\"$INPUT$\" Value=\" &lt; input name='input$IDOBJECT$'  value='variable=$TheVar$' &gt;\" />");
            XMLibrary.append(
                "\n    <Tag Destination=\"$OBJECT$\" Value=\" &lt; embed id='flashobj$IDOBJECT$' style='position:absolute;top:$TOP$;left:$LEFT$;width:$WIDTH$px;height:$HEIGHT$px;z-index:2;' wmode='transparent' src='flash/maps/" +
                name + (i + 2) +
                "D.swf' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'&gt; &lt; /embed &gt;\" />");
            XMLibrary.append("\n</ReplaceableTags>");
            XMLibrary.append("\n<DynamicProperties>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <Property Name=\"$" + attribute[i][j] +
                    "$\" Descr=\"%%\" Type=\"\" />");
            } //for

            XMLibrary.append(
                "\n    <Property Name=\"$TheUnit$\" Descr=\"%%\" Type=\"Device\" />");
            XMLibrary.append(
                "\n    <Property Name=\"$TheVar$\" Descr=\"%%\" Type=\"Variable\" DependsOn=\"$TheUnit$\" />");
            XMLibrary.append("\n</DynamicProperties>");
            XMLibrary.append("\n</Item>");
        } //for

        return XMLibrary.toString();
    }

    public static String CreateHMLSuffix(int gaugeNumber, String[][] attribute,
        String name, String[] suffix)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            XMLibrary.append("\n\n\n<Item Name=\"\" Icon=\"" + name +
                suffix[i] + "Ico.png\" Tooltip=\"%%\" Image=\"" + name +
                suffix[i] + "Img.png\" Type=\"\"	DefaultContent=\"\">");
            XMLibrary.append("\n<ReplaceableTags>");
            XMLibrary.append(
                "\n<Tag Destination=\"$SET$\" Value=\"&lt;div  id='flashset$IDOBJECT$'&gt;");

            for (int j = 0; j < (attribute[i].length - 1); j++)
            {
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append(";");
                XMLibrary.append("$");
                XMLibrary.append(attribute[i][j]);
                XMLibrary.append("$;");
            } //for

            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append(";");
            XMLibrary.append("$");
            XMLibrary.append(attribute[i][attribute[i].length - 1]);
            XMLibrary.append("$");
            XMLibrary.append("&lt;/div&gt;\" />  ");
            XMLibrary.append(
                "\n    <Tag Destination=\"$INPUT$\" Value=\" &lt; input name='input$IDOBJECT$'  value='variable=$TheVar$' &gt;\" />");
            XMLibrary.append(
                "\n    <Tag Destination=\"$OBJECT$\" Value=\" &lt; embed id='flashobj$IDOBJECT$' style='position:absolute;top:$TOP$;left:$LEFT$;width:$WIDTH$px;height:$HEIGHT$px;z-index:2;' wmode='transparent' src='flash/maps/" +
                name + suffix[i] +
                ".swf' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash'&gt; &lt; /embed &gt;\" />");
            XMLibrary.append("\n</ReplaceableTags>");
            XMLibrary.append("\n<DynamicProperties>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <Property Name=\"$" + attribute[i][j] +
                    "$\" Descr=\"%%\" Type=\"\" />");
            } //for

            XMLibrary.append(
                "\n    <Property Name=\"$TheUnit$\" Descr=\"%%\" Type=\"Device\" />");
            XMLibrary.append(
                "\n    <Property Name=\"$TheVar$\" Descr=\"%%\" Type=\"Variable\" DependsOn=\"$TheUnit$\" />");
            XMLibrary.append("\n</DynamicProperties>");
            XMLibrary.append("\n</Item>");
        } //for

        return XMLibrary.toString();
    }

    public static String CreateDictionary(int gaugeNumber,
        String[][] attribute, String name, int startItem)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            XMLibrary.append("\n<section name=\"Item" + (i + startItem) +
                "\">");
            XMLibrary.append(
                "\n    <item code=\"Tooltip\" value=\"Testo statico\"/>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <item code=\"P_$" + attribute[i][j] +
                    "$_Descr\" value=\"" + attribute[i][j] + "\"/>");
            } //for j

            XMLibrary.append("\n</section>");
        } //for

        return XMLibrary.toString();
    }

    public static String CompliteProperty(int gaugeNumber,
        String[][] attribute, String name, int startItem)
    {
        StringBuffer XMLibrary = new StringBuffer();

        for (int i = 0; i < gaugeNumber; i++)
        {
            XMLibrary.append("\n<section name=\"Item" + (i + startItem) +
                "\">");
            XMLibrary.append(
                "\n    <item code=\"Tooltip\" value=\"Testo statico\"/>");

            for (int j = 0; j < attribute[i].length; j++)
            {
                XMLibrary.append("\n    <item code=\"P_$" + attribute[i][j] +
                    "$_Descr\" value=\"" + attribute[i][j] + "\"/>");
            } //for j

            XMLibrary.append("\n</section>");
        } //for

        return XMLibrary.toString();
    }
}
