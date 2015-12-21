//package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.google.gson.Gson;

import servlet.EditDistance;
import servlet.RedBlackBST;

@WebServlet(
        name = "MyServlet", 
        urlPatterns = {"/didyoumean"}
    )
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	Gson gson = new Gson();
    	EditDistance ed = new EditDistance();
		List<String> matched = ed.findingLeastEditDistance(req.getParameter("q"));
		
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
        ServletOutputStream out = resp.getOutputStream();
        out.write(gson.toJson(matched).getBytes());
        out.flush();
        out.close();
    }
}
