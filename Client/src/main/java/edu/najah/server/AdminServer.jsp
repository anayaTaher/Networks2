<%
if(request.getParameter("function").equals("login")) {
  out.print(edu.najah.server.AdminServer.login(request.getParameter("email"), request.getParameter("password")));
} else if (request.getParameter("function").equals("updateInfo")) {
  out.print(edu.najah.server.AdminServer.updateInfo(request.getParameter("id"), request.getParameter("email"), request.getParameter("password"), request.getParameter("name"), request.getParameter("address")));
} else if (request.getParameter("function").equals("updateImage")) {
  out.print(edu.najah.server.AdminServer.updateImage(request.getParameter("photoExtension"), request.getParameter("id"), request.getParameter("photo")));
} else if (request.getParameter("function").equals("addNewUser")) {
  out.print(edu.najah.server.AdminServer.addNewUser(request.getParameter("email"), request.getParameter("password"), request.getParameter("name"), request.getParameter("address"), request.getParameter("id"), request.getParameter("photo"), request.getParameter("photoExtension"), request.getParameter("isAdmin")));
} else if (request.getParameter("function").equals("getUsersFromDB")) {
  out.print(edu.najah.server.AdminServer.getUsersFromDB(request.getParameter("usersType"), request.getParameter("withSearch"), request.getParameter("searchBy"), request.getParameter("searchField")));
} else if (request.getParameter("function").equals("deleteUser")) {
  out.print(edu.najah.server.AdminServer.deleteUser(request.getParameter("id")));
}
%>