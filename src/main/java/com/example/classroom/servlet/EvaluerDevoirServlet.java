package com.example.classroom.servlet;

import com.example.classroom.dao.RenduDAO;
import com.example.classroom.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/evaluer-devoir")
public class EvaluerDevoirServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String renduIdParam = request.getParameter("renduId");
        String noteParam = request.getParameter("note");
        String commentaire = request.getParameter("commentaire");
        String referer = request.getHeader("Referer"); // Get referer for redirect

        int renduId;
        int note;

        try {
            renduId = Integer.parseInt(renduIdParam);
            note = Integer.parseInt(noteParam);
        } catch (NumberFormatException e) {
            // Handle invalid input, perhaps set an error message in session and redirect
            e.printStackTrace(); // Log error
            // It might be better to redirect with an error query parameter or session attribute
            response.sendRedirect(referer != null ? referer : request.getContextPath() + "/jsp/prof-accueil.jsp");
            return;
        }


        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            RenduDAO dao = new RenduDAO(em);
            dao.evaluerRendu(renduId, note, commentaire);
            em.getTransaction().commit();
            // Optionally, set a success message in session if needed
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Log error
            // Optionally, set an error message in session
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/jsp/prof-accueil.jsp");
    }
}
