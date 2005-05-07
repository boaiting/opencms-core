/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/workplace/tools/widgetdemo/Attic/CmsAdminWidgetDemo1.java,v $
 * Date   : $Date: 2005/05/07 16:08:28 $
 * Version: $Revision: 1.1 $
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

package org.opencms.workplace.tools.widgetdemo;

import org.opencms.jsp.CmsJspActionElement;
import org.opencms.workplace.CmsWidgetDialog;
import org.opencms.workplace.CmsWidgetParameter;
import org.opencms.workplace.CmsWorkplaceSettings;
import org.opencms.workplace.xmlwidgets.CmsXmlBooleanWidget;
import org.opencms.workplace.xmlwidgets.CmsXmlImageGalleryWidget;
import org.opencms.workplace.xmlwidgets.CmsXmlStringWidget;
import org.opencms.workplace.xmlwidgets.CmsXmlTextareaWidget;
import org.opencms.workplace.xmlwidgets.CmsXmlVfsFileWidget;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * A basic example and proof-of-concept on how to use OpenCms widgets within a custom build form
 * without XML contents.<p>
 * 
 * @author Alexander Kandzior (a.kandzior@alkacon.com)
 * 
 * @version $Revision: 1.1 $
 * @since 5.9.1
 */
public class CmsAdminWidgetDemo1 extends CmsWidgetDialog {

    /** Value for the action: save the settings. */
    public static final int ACTION_SAVE = 300;

    /** Request parameter value for the action: save the dialog. */
    public static final String DIALOG_SAVE = "save";

    /** The dialog type. */
    public static final String DIALOG_TYPE = "widgetdemo1";

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsAdminWidgetDemo1(CmsJspActionElement jsp) {

        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsAdminWidgetDemo1(PageContext context, HttpServletRequest req, HttpServletResponse res) {

        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Builds the HTML for the demo1 form.<p>
     * 
     * @return the HTML for the demo1 form
     */
    public String buildDemo1Form() {

        StringBuffer retValue = new StringBuffer(512);

        try {

            retValue.append("<table class=\"xmlTable\">");

            Iterator i = getWidgets().iterator();
            while (i.hasNext()) {

                // get the current widget base definition
                CmsWidgetParameter base = (CmsWidgetParameter)i.next();
                List sequence = (List)getParameters().get(base.getName());
                int count = sequence.size();

                if ((count < 1) && (base.getMinOccurs() > 0)) {
                    // no parameter with the value present, but also not optional: use base as parameter
                    sequence = new ArrayList();
                    sequence.add(base);
                }

                Iterator j = sequence.iterator();
                while (j.hasNext()) {
                    CmsWidgetParameter param = (CmsWidgetParameter)j.next();
                    retValue.append("<tr>\n");
                    retValue.append(getWidget(param));
                    retValue.append("</tr>\n");
                }
            }

            retValue.append("</table>\n");

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return retValue.toString();
    }

    /**
     * @see org.opencms.workplace.CmsWidgetDialog#defineWidgets()
     */
    protected void defineWidgets() {

        addWidget(new CmsWidgetParameter("stringwidget", new CmsXmlStringWidget()));
        addWidget(new CmsWidgetParameter("textwidget", new CmsXmlTextareaWidget()));
        addWidget(new CmsWidgetParameter("boolwidget", new CmsXmlBooleanWidget()));
        addWidget(new CmsWidgetParameter("vfsfilewidget", new CmsXmlVfsFileWidget()));
        addWidget(new CmsWidgetParameter("imagegalwidget", new CmsXmlImageGalleryWidget()));
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {

        // set the dialog type
        setParamDialogtype(DIALOG_TYPE);

        // fill the parameter values in the get/set methods
        fillParamValues(request);

        // fill the widget map
        defineWidgets();
        fillWidgetValues(request);

        // set the action for the JSP switch 
        if (DIALOG_SAVE.equals(getParamAction())) {
            // ok button pressed
            setAction(ACTION_SAVE);
        } else if (DIALOG_OK.equals(getParamAction())) {
            // ok button pressed
            setAction(ACTION_CANCEL);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            // cancel button pressed
            setAction(ACTION_CANCEL);
        } else {
            // set the default action               
            setAction(ACTION_DEFAULT);
        }
    }
}
