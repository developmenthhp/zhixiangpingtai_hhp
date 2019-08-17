$(function(){

    if($("#teShiro").html()==""||$("#teShiro").html()==null){
        window.location.href="/login";
    }

    $(".myTableTooltip").tooltip({
        effect:'toggle',
        cancelDefault: true
    });
});
$(".side").on("click", "li", function(){
  $("#cube").removeClass().addClass($(this).attr("class"));
}).on("mouseover", "li", function(){
  var me   = $(this);
  var attr = me.attr("data-url");

  if(attr != undefined){
    $("h2").text(attr).css("visibility", "visible");
  }
});