var nav = new Object();


function createNavItem(tag, handler) {
    var node = $("<a>" + tag + "</a>");
    node.click( handler);
    return node;
}

function createSearchBox()
{
    return $("<span id='search_control'><input id='searchBox'></input><button id='search_button'>Search</button><span>");
}

nav.search = function()
{
    var text = $("#searchBox").val();
    article.search(text);
}

nav.draw = function() {
   $(".topnav").append(createNavItem("Home", article.load));
   $(".topnav").append(createNavItem("Post", article.post));
   $(".topnav").append(createSearchBox());
   
   $("#search_button").click(nav.search);
}

