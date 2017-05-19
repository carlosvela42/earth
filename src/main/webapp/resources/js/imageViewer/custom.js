$('.image-viewer').on('click',function(){
var modalId = $(this).data('modal');
$(modalId).modal('show');
$('.modal-backdrop').removeClass('in');
});
