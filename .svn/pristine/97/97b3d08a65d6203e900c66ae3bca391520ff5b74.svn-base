package com.carel.supervisor.base.util;

public class ClassManaged
{
    public static final String BYTE_TYPE = "byte";
    public static final String CHAR_TYPE = "char";
    public static final String DOUBLE_TYPE = "double";
    public static final String FLOAT_TYPE = "float";
    public static final String INT_TYPE = "int";
    public static final String LONG_TYPE = "long";
    public static final String SHORT_TYPE = "short";
    public static final String BOOLEAN_TYPE = "boolean";

    private ClassManaged()
    {	
    }
    
    public static String interpretArray(Class className)
    {
        if (className.isArray())
        {
            char[] ac = className.getName().toCharArray();
            int i = 0;
            int j;

            for (j = 0; ac[j] == '['; j++)
            {
                i++;
            }

            StringBuffer buffer = new StringBuffer();

            switch (ac[j++])
            {
            case 66: // 'B'
                buffer.append(BYTE_TYPE);

                break;

            case 67: // 'C'
                buffer.append(CHAR_TYPE);

                break;

            case 68: // 'D'
                buffer.append(DOUBLE_TYPE);

                break;

            case 70: // 'F'
                buffer.append(FLOAT_TYPE);

                break;

            case 73: // 'I'
                buffer.append(INT_TYPE);

                break;

            case 74: // 'J'
                buffer.append(LONG_TYPE);

                break;

            case 83: // 'S'
                buffer.append(SHORT_TYPE);

                break;

            case 90: // 'Z'
                buffer.append(BOOLEAN_TYPE);

                break;

            case 76: // 'L'
                buffer.append(ac, j, ac.length - j - 1);

                break;
            }

            for (int k = 0; k < i; k++)
            {
                buffer.append("[]");
            }

            return buffer.toString();
        }
        else
        {
            return null;
        }
    }

    public static int getDimensionArray(Class className)
    {
        Class classNameTmp = className;

        if (classNameTmp.isArray())
        {
            int dimensions = 0;

            while (classNameTmp.isArray())
            {
                dimensions++;
                classNameTmp = classNameTmp.getComponentType();
            }

            return dimensions;
        }
        else
        {
            return 0;
        }
    }

    public static String retrieveType(Class className)
    {
        String code = null;
    	if (className.isArray())
        {
            char[] ac = className.getName().toCharArray();
            int j;

            for (j = 0; ac[j] == '['; j++)
            {
            }

            switch (ac[j++])
            {
            case 66: // 'B'
                code = BYTE_TYPE;
                break;
            case 67: // 'C'
            	code = CHAR_TYPE;
            	break;
            case 68: // 'D'
            	code = DOUBLE_TYPE;
            	break;
            case 70: // 'F'
            	code = FLOAT_TYPE;
            	break;
            case 73: // 'I'
            	code = INT_TYPE;
            	break;
            case 74: // 'J'
            	code = LONG_TYPE;
            	break;
            case 83: // 'S'
            	code = SHORT_TYPE;
            	break;
            case 90: // 'Z'
            	code = BOOLEAN_TYPE;
            	break;
            case 76: // 'L'
            {
                StringBuffer buffer = new StringBuffer();
                buffer.append(ac, j, ac.length - j - 1);
                code = buffer.toString();
                break;
            }
            default:
            	code = null;
            }
        }
        else
        {
        	code = null;
        }
    	return code;
    }
}
