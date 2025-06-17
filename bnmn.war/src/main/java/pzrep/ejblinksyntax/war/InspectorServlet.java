package pzrep.ejblinksyntax.war;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pzrep.ejblinksyntax.api.Cat;

import java.io.IOException;

@WebServlet(value = "/inspector", loadOnStartup = 1)
public class InspectorServlet extends HttpServlet {
    @EJB
    private Cat cat;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().printf("Cat of color: %s and creature with name: %s",
                                cat.color(),
                                cat.creatureName());
    }
}
