
var article = new Object();
article.private = new Object();

$("#post_dialog").hide();

article.closeDialog = function ()
{
    $("#post_dialog").html("");
    $("#post_dialog").css("visibility", "hidden");
    site.dialogOpen = false;
}

article.vote = function (article, vote, callback)
{
    var data = new Object();
    data.userToken = JSON.stringify(session.userToken);
    data.vote = vote;

    $.ajax({
        type: "POST",
        url: '/article/' + article.id,
        data: data,
        success: function (data) {
            callback(data);
        },
        error: function (data) {
            if (data.status == 401)
            {
                user.login("", "", "Invalid session, please log in again");
            }
        },
        dataType: null
    });
}

article.private.createArticleNode = function (a) {
    var listNode = document.getElementById("article_list");
    var node = document.createElement("div");
    var articleTag = "art_" + a.id;
    var totalTag = "total_" + a.id;
    var commentTag = "comment_" + a.id;
    var upVoteTag = "upvote_" + a.id;
    var downVoteTag = "downvote_" + a.id;
    var vote = user.getVote(a.id);
    
    article.commentCount(a.id, function (count) {
        $("#" + commentTag).html(count + " Comments")
    });
    var deleteTag = "delete_" + articleTag;
    node.innerHTML = "<div class='article_tag'>" +
            "<div><a class='article_title' target='_blank' href='" + a.link + "'" + ">" + a.title + "</a></div>" +
            "<span id='" + totalTag + "' class='article_total'>" + a.total + "</span>" +
            "<span id='" + upVoteTag + "' class='up_arrow'>&uarr;</span>" +
            "<span id='" + downVoteTag + "' class='down_arrow'>&darr;</span>" +
            "<span class='clickable' id='" + commentTag + "'>0 Comments</span>" +
            "<span class='clickable' id='" + deleteTag + "'>Delete</span>" +
            "<span class='info'>posted by: " + a.username + "</span>" +
            "</div>";

    listNode.appendChild(node);

    $("#" + upVoteTag).click(function () {
        article.vote(a, vote == 1 ? 0 : 1, function(total){
            $("#"+ totalTag).html(total);
            article.load();
        });
        
    });
    
    $("#" + downVoteTag).click(function () {
        article.vote(a, vote == -1 ? 0 : -1, function(total){
            $("#"+ totalTag).html(total);
            article.load();
        });
        
    });
    
    if (vote == 1)
    {
        $("#" + upVoteTag).css("background-color", "green");
    } else if (vote == -1)
    {
        $("#" + downVoteTag).css("background-color", "red");
    }
    
    if (!session.isLoggedIn)
    {
        $(".up_arrow").css("visibility", "hidden");
        $(".down_arrow").css("visibility", "hidden");
        $('#' + deleteTag).css("visibility", "hidden");
    } else if (!(a.username === session.userToken.username))
    {
        $('#' + deleteTag).css("visibility", "hidden");
    }

    $("#" + commentTag).click(function () {
        comment.drawPage(a);
    });

    $("#" + deleteTag).click(function () {
        var data = new Object();
        data.userToken = JSON.stringify(session.userToken);

        site.verify("Delete this article?", function () {
            $.ajax({
                type: "POST",
                url: '/article/delete/' + a.id,
                data: data,
                success: function (data) {
                    article.load();
                },
                error: function (data) {
                    if (data.status == 200) {
                        article.load();
                    }
                }
            });
        });
    });

};

article.load = function ()
{
    $("#main_content").html("<div id='article_list'/>");

    user.updateVotes( function()
        {
            $.get("/article", function (data, status) {
                data.reverse().forEach(article.private.createArticleNode)
            });
        });
}

article.search = function (text)
{
    $("#main_content").html("<div id='article_list'/>");

    if (text == "")
    {
        article.load();
    } else
    {
        user.updateVotes( function() 
        {
            $.get("/article/search/" + text, function (data, status) {
                data.reverse().forEach(article.private.createArticleNode)
            });
        });
    }
}

article.commentCount = function (articleId, handler) {
    $.get("/comment/count/" + articleId, function (data, status) {
        handler(data);
    })
}

article.post = function ()
{
    if (site.dialogOpen)
    {
        return;
    }

    if (session.isLoggedIn)
    {
        site.dialogOpen = true;

        $("#post_dialog").html("<div class='dialog_title'>Post Article</div>" +
                "<div>" +
                "<p><label id='link_label'>Link</label><input id='link_input'></input></p>" +
                "<p><label id='title_label'>Title</label><input id='title_input'></input></p>" +
                "<p><label id='error_label'></label><button id='post_cancel_button'>Cancel</button><button id='post_submit_button'>Post</button></p>" +
                "</div>");

        $("#post_submit_button").click(function () {
            var data = new Object();
            data.userToken = JSON.stringify(session.userToken);
            data.title = $('#title_input').val();
            data.link = $('#link_input').val();

            $.ajax({
                type: "POST",
                url: '/article',
                data: data,
                success: function (data) {
                    article.closeDialog();
                    article.load();
                },
                error: function (data) {
                    if (data.status == 401)
                    {
                        article.closeDialog();
                        user.login("", "", "Invalid session, please log in again");
                    }
                },
                dataType: null
            });
        });

        $("#post_cancel_button").click(function () {
            article.closeDialog();
        });

        $("#post_dialog").css("visibility", "visible");
    } else
    {
        user.login("", "", "You must log in before you can post.");
    }
}