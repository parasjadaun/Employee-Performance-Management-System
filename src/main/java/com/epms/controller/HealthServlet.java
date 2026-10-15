package com.epms.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides the lightweight health check used by deployment platforms.
 */
@WebServlet(urlPatterns = "/health")
public class HealthServlet extends HttpServlet {

    /**
     * Returns a plain-text health response.
     *
     * @param request HTTP request
     * @param response HTTP response
     * @throws IOException if the response cannot be written
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("OK");
    }
}
