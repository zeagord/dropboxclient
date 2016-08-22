const pathArray= []
function onLoad(){
    $('#app-getfiles').append('<span>Test Token: yrfs5Sfq0zAAAAAAAAAADUjINoo3xmgwVx6kqkoJc-Zodypc-80adJo4uAnEowi6</span>\
      <input type="text" class="col-sm-4 form-control" id="auth" placeholder="OAuth Token">\
      <button onclick="getData()" type="button" class="col-sm-4 btn btn-primary" >Get DropBox Files</button><br /><br />');
}

function getData(path, id){
    $.ajax({
      type: 'POST',
      url: "http://localhost:9020/getFiles",
      contentType: "application/json",
      data: JSON.stringify({
          auth: document.getElementById('auth').value,//'yrfs5Sfq0zAAAAAAAAAADUjINoo3xmgwVx6kqkoJc-Zodypc-80adJo4uAnEowi6',
          path: path ? path : "",
        }),
      success: function (result){ render(result, id)},
      failure: function (error){ renderError(error)},
      beforeSend: function() {
        $('#loader').css("display", "block");
      },
      complete: function(){
        $('#loader').css("display", "none");
      },
      });
}
function renderError(error) {
  $('#app-info').append("Unable to fetch <br>" + error);
}

function render(response, id) {
  if (id === '' || id === undefined ) {
    $('#app-info').empty();
    $('#app-info').append('<br /><br /><br /><a nohreh onclick="getData()">Drop Box</a>');
  } else {
    $('#app-info').empty();
    $('#app-info').append('<br /><br /> <a nohreh onclick="getData()">Drop Box</a>');
    $('#app-info').append(' > <a nohref onclick="getData(pathArray[this.id], this.id)" id="'+id+'">'+pathArray[id]+'</a>');
  }
  $('#app-content').empty();
  if(response.length > 0) {
    var fileList = '<div class="list-group">';
    for (var i in response){
      if (response[i][".tag"]==="folder") {
        pathArray[response[i].id] = response[i].path_lower
        fileList += '<a onclick="getData(pathArray[this.id], this.id)" id="'+response[i].id+'" class="list-group-item list-group-item-action list-group-item-success"><span class="col-xs-1 glyphicon glyphicon-folder-close" aria-hidden="true"></span>&nbsp;&nbsp;' +
        '<span class="col-xs-3">'+response[i].name + '</span>'+
        '</a>';
      } else {
        fileList += '<li class="list-group-item"><span class="col-xs-1 glyphicon glyphicon-file" aria-hidden="true"></span>'+
        '<span class="col-xs-3">'+response[i].name + '</span>'+
        '<span class="label label-success">'+Math.round(response[i].size/1024) +' kb</span>&nbsp;&nbsp;&nbsp;'+
        '<span class="label label-info">'+formatDate(response[i].client_modified) +'</span>'+
        '</li>';
      }
    }
    fileList += '</div>';
    $('#app-content').append(fileList);

  } else {
    $('#app-info').empty();
    $('#app-info').append("<br /><br />No content to disaplay, Check you auth token");
  }
}


function formatDate(dateObj) {
   var date = dateObj ? new Date(dateObj) : new Date();
   var day = date.getDate();
   var monthIndex = date.getMonth() + 1;
   var year = date.getFullYear();
   var hours = date.getHours();
   var mins = date.getMinutes();
   return monthIndex + '-' + day + '-' + year + " " + hours + " hr:" + mins + " mins";
}
