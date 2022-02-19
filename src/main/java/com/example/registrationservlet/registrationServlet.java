package com.example.registrationservlet;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/register")
public class registrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String repass = request.getParameter("re_pass");
        String password = request.getParameter("password");
        Connection con = null;
        RequestDispatcher dispatcher = null;

        if(firstname == null || firstname.equals("")){
            request.setAttribute("status", "invalidFirstName");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        }
        if(lastname == null || lastname.equals("")){
            request.setAttribute("status", "invalidLastName");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        }
        if(email == null || email.equals("")){
            request.setAttribute("status", "invalidEmail");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        }
        if(password == null || password.equals("")){
            request.setAttribute("status", "invalidPassword");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
        } else if(!password.equals(repass)){
            request.setAttribute("status", "invalidConfirmPassword");
            dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);

        }


        PrintWriter out = response.getWriter();
        out.print(firstname);
        out.print(lastname);
        out.print(email);
        out.print(password);

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/registration", "postgres", "11111111");
            PreparedStatement ps = con.prepareStatement("insert into users(first_name, last_name, email, password) values (?, ?, ?, ?)");
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, password);

           int rowCount = ps.executeUpdate();
           dispatcher = request.getRequestDispatcher("registration.jsp");
           if(rowCount >0){
               request.setAttribute("status", "success");
           }else{
               request.setAttribute("status", "failed");
           }
           dispatcher.forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

