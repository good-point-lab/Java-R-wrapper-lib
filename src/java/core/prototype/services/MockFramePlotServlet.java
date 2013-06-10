package core.prototype.services;

import core.prototype.config.Application;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Servlet to plot data using R
 * The data passed as R frame
 * 
 */
@WebServlet(name = "plot-frame-servlet", urlPatterns = {"/plot-frame-servlet"})
public class MockFramePlotServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MockFramePlotServlet.class);
    private static final String rScriptName = "frame-data-plot.R";
    private static final String imageName = "frame-data-plot.jpg";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        OutputStream outs = null;
        try {
            Application app = Application.getInstance();
            byte[] imageData = app.getDomain().plotMockDataset(imageName, rScriptName);
            String encoded = new sun.misc.BASE64Encoder().encode(imageData);
            response.setContentLength(encoded.getBytes().length);
            outs = response.getOutputStream();
            outs.write(encoded.getBytes());
            outs.flush();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (outs != null) {
                outs.close();
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}