/*******************************************************************************
 * Copyright (c) 2006 Vladimir Silva and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Vladimir Silva - initial API and implementation
 *******************************************************************************/

package ch01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Servlet1 extends HttpServlet {

    private static final long serialVersionUID = 2143882647223456914L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        resp.setContentType("text/html");
        dumpHttpHeaders(req, resp.getWriter());
    }

    @SuppressWarnings("unchecked")
    private void dumpHttpHeaders(HttpServletRequest req, PrintWriter out) 
    {
        out.println("URI:" + req.getRequestURI() + "<br/>");

        Enumeration<String> names = req.getHeaderNames();

        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            out.println(name + "=" + req.getHeader(name) + "<br/>");
        }
    }

}
