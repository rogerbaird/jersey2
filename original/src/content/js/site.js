var site = new Object();

site.dialogOpen = false;

site.closeVerifyDialog = function() {
   $("#verify_dialog").html("");
   $("#verify_dialog").css("visibility", "hidden");
}

site.verify = function(msg, onYes) {
//     $("#verify_dialog").html("<div class='dialog_title'>Verify</div>" +
//            "<div id='verify_label'>"+ msg + "</div>" +
//            "<div id='dialog_button_row'><button id='cancel_button' class='dialog_button'>Cancel</button><button id='yes_button' class='dialog_button'>Yes</button></div>");
    
     
    $("#verify_dialog").html("<div class='dialog_title'>Verify</div>" +
            "<div>" +
            "<p><label id='verify_label'>" + msg + "</label></p>" +
            "<p><label id='error_label'></label><button id='cancel_button'>Cancel</button><button id='yes_button'>Yes</button></p>" +
            "</div>");
    
     $("#yes_button").click(function () {
        site.closeVerifyDialog();
        onYes();
      });
      
      $("#cancel_button").click(function () {
        site.closeVerifyDialog();
      });
    
     $("#verify_dialog").css("visibility", "visible");
}