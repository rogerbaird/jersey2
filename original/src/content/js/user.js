var user = new Object();

user.private = new Object();
user.private.likedArticles = new Array();
user.private.dislikedArticles = new Array();
    
user.clear = function()
{
    user.private.likedArticles = new Array();
    user.private.dislikedArticles = new Array();
}

user.private.validateUser = function () {
    var errorText = "";
    var errorFound = false;
    var username = $("#username_input").val();
    var password1 = $("#password_input_1").val();
    var password2 = $("#password_input_2").val();
    $("#error_label").html("");
    $.get("/user/exists/" + username, function (data, status) {
        if (data)
        {
            errorText = "Username not available";
            errorFound = true;
        } else
        {
            if (password1.length == 0)
            {
                errorText = "Password is required";
                errorFound = true;

            } else if (!(password1 === password2))
            {
                errorText = "Passwords do not match";
                errorFound = true;
            }
        }

        $("#register_submit_button").prop("disabled", errorFound);

        if (errorFound)
        {
            $("#error_label").html(errorText);

        }
    })
}


user.loadUser = function (userId, handler) {

    $.get("/user/" + userId, function (data, status) {
        handler(data);
    })
}

user.login = function (username, password, message) {

    if (site.dialogOpen)
    {
        return;
    }

    if (username === undefined)
        username = "";

    if (password === undefined)
        password = "";

    if (message === undefined)
        message = "";

    site.dialogOpen = true;

    $("#login_dialog").html("<div class='dialog_title'>Log In</div>" +
            "<div>" +
            "<p><label id='username_label'>Username</label><input id='username_input' value='" + username + "'></input></p>" +
            "<p><label id='password_label'>Password</label><input id='password_input' type='password' value='" + password + "'></input></p>" +
            "<p><label id='error_label'></label><button id='login_cancel_button'>Cancel</button><button id='login_submit_button'>Log In</button></p>" +
            "</div>");

    $("#error_label").html(message);

    $("#login_cancel_button").click(function () {
        $("#login_dialog").html("");
        $("#login_dialog").css("visibility", "hidden");
        site.dialogOpen = false;
    })

    $.ajaxSetup({
        statusCode: {
            401: function () {
                user.login("", "", "Login unsuccesful.")
            }
        }
    });

    $("#login_dialog").css("visibility", "visible");
    $("#login_submit_button").click(function () {
        var form = new Object();
        form.username = $("#username_input").val();
        form.password = $("#password_input").val();

        $("#login_dialog").html("");
        $("#login_dialog").css("visibility", "hidden");
        site.dialogOpen = false;
        $.post("/user/login",
                form,
                function (response) {
                    if (response.username === form.username)
                    {
                        session.login(response);
                        user.updateVotes(function(){});
                    }
                });
    })
}

user.register = function () {
    if (site.dialogOpen)
    {
        return;
    }

    site.dialogOpen = true;

    $("#register_dialog").html("<div class='dialog_title'>Register</div>" +
            "<div>" +
            "<p><label id='username_label'>Username</label><input id='username_input'></input></p>" +
            "<p><label id='password_label_1'>Password</label><input id='password_input_1' type='password'></input></p>" +
            "<p><label id='password_label_2'>Confirm Password</label><input id='password_input_2' type='password'></input></p>" +
            "<p><label id='error_label'></label><button id='register_cancel_button'>Cancel</button><button id='register_submit_button'>Register</button></p>" +
            "</div>");
    $("#username_input").keyup(user.private.validateUser);
    $("#password_input_1").keyup(user.private.validateUser);
    $("#password_input_2").keyup(user.private.validateUser);
    $("#register_cancel_button").click(function () {
        $("#register_dialog").html("");
        $("#register_dialog").css("visibility", "hidden");
        site.dialogOpen = false;
    })
    $("#register_submit_button").prop("disabled", true);
    $("#register_dialog").css("visibility", "visible");
    $("#register_submit_button").click(function () {
        var newUser = new Object();
        newUser.username = $("#username_input").val();
        newUser.password1 = $("#password_input_1").val();
        newUser.password2 = $("#password_input_2").val();

       

        $.post("/user/register",
                newUser,
                function (response, status) {
                    $("#register_dialog").html("");
                    $("#register_dialog").css("visibility", "hidden");
                    site.dialogOpen = false;
                    user.login(response.userName, response.password, "Registration successful, please log in.");
                }).fail(function(resp) {
                    if(resp.status == 400)
                    {
                        $("#error_label").html(JSON.parse(resp.responseText));
                    }
                });
    })
}

// THIS IS BROKEN, the below callbacks need to be chained
user.private.getLikes = function (username, callback)
{
    $.get("/user/" + username + "/likes", function (data) {
        user.private.likedArticles = data;
        callback();
    })
}

user.private.getDislikes = function (username, callback)
{
    $.get("/user/" + username + "/dislikes", function (data) {
         user.private.dislikedArticles = data;
         callback();
    })
}

user.updateVotes = function( callback )
{   
    if ( session.isLoggedIn )
    {
        user.private.getLikes(session.userToken.username, function() {
            user.private.getDislikes(session.userToken.username, callback);
        });
    }
    else
    {
        callback();
    }
}

user.getVote = function(articleId)
{
    if ( user.private.likedArticles != undefined && user.private.likedArticles.includes(articleId))
        return 1;
    if ( user.private.dislikedArticles != undefined && user.private.dislikedArticles.includes(articleId))
        return -1;
    return 0;
}
