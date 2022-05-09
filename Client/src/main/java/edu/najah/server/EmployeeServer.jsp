<%
if(request.getParameter("function").equals("login")) {
  out.print(edu.najah.server.EmployeeServer.login(request.getParameter("email"), request.getParameter("password")));
} else if (request.getParameter("function").equals("updateInfo")) {
  out.print(edu.najah.server.EmployeeServer.updateInfo(request.getParameter("id"), request.getParameter("email"), request.getParameter("password"), request.getParameter("name"), request.getParameter("address")));
} else if (request.getParameter("function").equals("updateImage")) {
  out.print(edu.najah.server.EmployeeServer.updateImage(request.getParameter("photoExtension"), request.getParameter("id"), request.getParameter("photo")));
}
%>