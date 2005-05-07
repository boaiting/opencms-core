/*
 * File   : $Source: /alkacon/cvs/opencms/src/org/opencms/workplace/xmlwidgets/Attic/CmsXmlColorpickerWidget.java,v $
 * Date   : $Date: 2005/05/07 16:08:27 $
 * Version: $Revision: 1.12 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * Copyright (C) 2002 - 2005 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.workplace.xmlwidgets;

import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsMessages;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplace;
import org.opencms.xml.CmsXmlException;
import org.opencms.xml.types.CmsXmlColorValue;

import java.util.Map;

/**
 * Provides an editor widget for {@link org.opencms.xml.types.CmsXmlColorValue}.<p>
 *
 * @author Andreas Zahner (a.zahner@alkacon.com)
 * 
 * @version $Revision: 1.12 $
 * @since 5.5.2
 */
public class CmsXmlColorpickerWidget extends A_CmsXmlWidget {

    /**
     * Creates a new editor widget.<p>
     */
    public CmsXmlColorpickerWidget() {

        // empty constructor is required for class registration
    }

    /**
     * @see org.opencms.workplace.xmlwidgets.I_CmsXmlWidget#getDialogIncludes(org.opencms.file.CmsObject, org.opencms.workplace.xmlwidgets.I_CmsWidgetDialog)
     */
    public String getDialogIncludes(CmsObject cms, I_CmsWidgetDialog widgetDialog) {

        return getJSIncludeFile(CmsWorkplace.getSkinUri() + "components/widgets/colorpicker.js");
    }

    /**
     * @see org.opencms.workplace.xmlwidgets.I_CmsXmlWidget#getDialogInitCall(org.opencms.file.CmsObject, org.opencms.workplace.xmlwidgets.I_CmsWidgetDialog)
     */
    public String getDialogInitCall(CmsObject cms, I_CmsWidgetDialog widgetDialog) {

        return "\tinitColorPicker();\n";
    }

    /**
     * @see org.opencms.workplace.xmlwidgets.I_CmsXmlWidget#getDialogInitMethod(org.opencms.file.CmsObject, org.opencms.workplace.xmlwidgets.I_CmsWidgetDialog)
     */
    public String getDialogInitMethod(CmsObject cms, I_CmsWidgetDialog widgetDialog) {

        StringBuffer result = new StringBuffer(128);
        result.append("function initColorPicker() {\n");
        result.append("\tcolorPicker.title = \"");
        result.append(widgetDialog.key("dialog.color.title", CmsMessages.formatUnknownKey("dialog.color.title")));
        result.append("\";\n");
        result.append("\tcolorPicker.url=\"");
        result.append(CmsWorkplace.getSkinUri());
        result.append("components/js_colorpicker/index.html\";\n");
        result.append("}\n");
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.xmlwidgets.I_CmsXmlWidget#getDialogWidget(org.opencms.file.CmsObject, I_CmsWidgetDialog, I_CmsWidgetParameter)
     */
    public String getDialogWidget(CmsObject cms, I_CmsWidgetDialog widgetDialog, I_CmsWidgetParameter param) {

        CmsXmlColorValue castValue = (CmsXmlColorValue)param;

        StringBuffer result = new StringBuffer(16);
        result.append("<td class=\"xmlTd\">");
        String colorValue = castValue.getStringValue(cms);
        String id = param.getId();

        result.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>");
        result.append("<input type=\"text\"");
        result.append(" class=\"xmlInputSmall\" name=\"");
        result.append(id);
        result.append("\" value=\"");
        result.append(colorValue);
        result.append("\" maxlength=\"7\" onkeyup=\"previewColor('");
        result.append(id);
        result.append("');\"");
        result.append(" style=\"background-color: ");
        result.append(checkColor(colorValue));
        result.append("; color: ");
        result.append(getInputFontColor(colorValue));
        result.append(";\"></td>");

        result.append(widgetDialog.buttonBarSpacer(1));
        result.append(widgetDialog.button(
            "javascript:showColorPicker('" + id + "');",
            null,
            "color_fill",
            "button.color",
            widgetDialog.getButtonStyle()));
        result.append("</tr></table>");

        result.append("</td>");
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.xmlwidgets.I_CmsXmlWidget#setEditorValue(org.opencms.file.CmsObject, java.util.Map, I_CmsWidgetDialog, I_CmsWidgetParameter)
     */
    public void setEditorValue(
        CmsObject cms,
        Map formParameters,
        I_CmsWidgetDialog widgetDialog,
        I_CmsWidgetParameter param) throws CmsXmlException {

        String[] values = (String[])formParameters.get(param.getId());
        if ((values != null) && (values.length > 0)) {
            CmsXmlColorValue castValue = (CmsXmlColorValue)param;
            String castColorValue = castValue.getStringValue(cms);
            String colorValue = values[0].trim();
            if (CmsStringUtil.isNotEmpty(colorValue)) {
                castColorValue = colorValue;
            }
            param.setStringValue(cms, String.valueOf(castColorValue));
        }
    }

    /**
     * Check the stored color value to prevent display issues in the generated HTML ouput.<p>
     * 
     * @param color the color value to check
     * @return the checked color value
     */
    private String checkColor(String color) {

        if (color != null) {
            if (color.indexOf("#") == -1) {
                // add the "#" to the color string
                color = "#" + color;
            }
            int colLength = color.length();
            if (colLength == 4 || colLength == 7) {
                return color;
            }
        }
        return "#FFFFFF";
    }

    /**
     * Returns the font color of the input field depending on the selected color value.<p>
     * 
     * @param backgroundColor the selected color value which is displayed as the input field background
     * @return the font color to use
     */
    private String getInputFontColor(String backgroundColor) {

        if (backgroundColor != null && backgroundColor.indexOf("#") == 0) {
            // remove the "#" from the color string
            backgroundColor = backgroundColor.substring(1);
            int colorValue = 50001;
            try {
                // calculate int value of color
                colorValue = Integer.parseInt(backgroundColor, 16);
            } catch (NumberFormatException nf) {
                // this should never happen    
            }
            if (colorValue < 50000) {
                // for dark colors set font color to white
                return "#FFFFFF";
            } else {
                // for other colors use black
                return "#000000";
            }
        }
        return "#000000";
    }

}