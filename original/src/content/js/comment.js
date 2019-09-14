var comment = new Object();

comment.private = new Object();

comment.private.createCommentNode = function (data) {
    var listNode = document.getElementById("comment_list");
    var node = document.createElement("div");
    var userNodeId = "user_" + data.userId;
    var deleteTag = "comment_delete_" + data.commentId;
    var editTag = "comment_edit_" + data.commentId;

    node.innerHTML = "<div class='comment'><div class='user_tag " + userNodeId + "'>" + data.userId + "</div>" + 
            "<div class='comment_text'>" + data.text + "</div>" +
            "<div><div class='comment_controls'><span class='clickable' id='" + editTag +"'>Edit</span><span id='"+ deleteTag +"' class='clickable'>Delete</span></div></div>" +
            "</div>";
    listNode.appendChild(node);

    if (!(session.isLoggedIn && (data.userId === session.userToken.userId)))
    {
        $('#' + editTag).css("visibility", "hidden");
        $('#' + deleteTag).css("visibility", "hidden");
    }
    user.loadUser(data.userId, function (user) {
        $('.' + userNodeId).html(user.userName);
    })


}

comment.private.clear = function (article) {
    if (!session.isLoggedIn)
    {
        
        $("#main_content").html(
                "<div id='comment_title'>" + article.title + "</div>" +
                "<div id='comment_list'/>");

    } else
    {
        $("#main_content").html(
                "<div id='comment_title'>" + article.title + "</div>" +
                "<div id='comment_header'><textarea id='comment_textarea'></textarea>" +
                "<div><button id='post_comment_button'>Post Comment</button></div></div>" +
                "<div id='comment_list'/>");
    }

    $("#post_comment_button").click(function ()
    {
        var data = new Object();
        data.comment = $("#comment_textarea").val();
        data.userToken = JSON.stringify(session.userToken);

        $.ajax({
            type: "POST",
            url: '/comment/' + article.id,
            data: data,
            success: function (data) {
                comment.drawPage(article);
            },
            dataType: null
        });
    })
}

comment.drawPage = function (article) {
    comment.private.clear(article);

    $.get("/comment/" + article.id, function (data, status) {
        data.forEach(comment.private.createCommentNode)
    })
}

