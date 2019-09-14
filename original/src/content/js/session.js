
var session = new Object();

session.isLoggedIn = false;

session.login = function(userToken) {
    session.userToken = userToken;
    session.isLoggedIn = true;
    session.refresh();
    article.load();
}

session.logout = function() {
    session.userToken = null;
    session.isLoggedIn = false;
    user.clear();
    session.refresh();
    article.load();
}

session.refresh = function() {
    
    if (session.isLoggedIn)
    {
        $("#login_bar").html("<span id='logged_in_msg'>Logged in as " + session.userToken.username + "</span><span id='logout_button' class='clickable'>Log Out</span>");
        $("#logout_button").click(session.logout);
    }
    else
    {
         $("#login_bar").html("<span id='register_button' class='clickable'>Register</span>" + 
                "<span id='login_button' class='clickable'>Log In</span>");
        $("#register_button").click( user.register );
        $("#login_button").click( function(){
            user.login();
        });
    }
}

session.drawLogin = session.refresh

