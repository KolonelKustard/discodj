module.exports.query = function() {
  return getPlaylist();
};

module.exports.add = function(id, allMedia) {
  var toAdd = findMedia(id, allMedia);
  if (toAdd) {
    var playlist = getPlaylist();
    playlist.playlist.push(toAdd);
    savePlaylist(playlist);
    return true;
  } else {
    return false;
  }
};

module.exports.moveUp = function(id, allMedia) {
  var playlist = getPlaylist();
  for (var num = 1; num < playlist.playlist.length; num++) {
    var media = playlist.playlist[num];
    if (media.id === id) {
      var swapsy = playlist.playlist[num - 1];
      playlist.playlist[num - 1] = media;
      playlist.playlist[num] = swapsy;
      savePlaylist(playlist);
      break;
    }
  }
}

module.exports.moveDown = function(id, allMedia) {
  var playlist = getPlaylist();
  for (var num = 0; num < playlist.playlist.length - 1; num++) {
    var media = playlist.playlist[num];
    if (media.id === id) {
      var swapsy = playlist.playlist[num + 1];
      playlist.playlist[num + 1] = media;
      playlist.playlist[num] = swapsy;
      savePlaylist(playlist);
      break;
    }
  }
}

module.exports.next = function() {
  var response = {
    queueEmpty: true,
    type: null,
    id: null,
    artist: null,
    title: null,
    requestedBy: null
  };

  var playlist = getPlaylist();
  if (playlist.playlist.length > 0) {
    var next = playlist.playlist.shift();
    playlist.nowPlaying = next;
    savePlaylist(playlist);

    response.queueEmpty = false;
    response.type = "audio/mp3";
    response.id = next.id;
    response.artist = next.artist;
    response.title = next.title;
  } else if (playlist.nowPlaying) {
    playlist.nowPlaying = null;
    savePlaylist(playlist);
  }

  return response;
}

var getPlaylist = function() {
  if (window.sessionStorage && window.sessionStorage.getItem("discoDjPlaylist")) {
    return JSON.parse(window.sessionStorage.getItem("discoDjPlaylist"));
  } else if (window.discoDjPlaylist) {
    return window.discoDjPlaylist;
  } else {
    return {
      nowPlaying: null,
      playlist: []
    };
  }
};

var savePlaylist = function(playlist) {
  if (window.sessionStorage) {
    window.sessionStorage.setItem("discoDjPlaylist", JSON.stringify(playlist));
  } else {
    window.discoDjPlaylist = playlist;
  }
};

var findMedia = function(id, allMedia) {
  for (var num = 0; num < allMedia.length; num++) {
    if (allMedia[num].id == id) {
      return allMedia[num];
    }
  }
  return null;
};
