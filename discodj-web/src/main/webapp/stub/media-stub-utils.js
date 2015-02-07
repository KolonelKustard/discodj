var PAGE_SIZE = 10;

module.exports.search = function(allMedia, params) {
  var subset = search(allMedia, params);
  var page = (params && params.page) ? params.page : 1;

  return {
    artistFacets: workOutFacets(subset, "artist", null),
    albumFacets: workOutFacets(subset, "album", null),
    results: subArr(subset, (page - 1) * PAGE_SIZE, PAGE_SIZE),
    page: page,
    numPages: Math.ceil(subset.length / PAGE_SIZE)
  };
};

var search = function(allMedia, params) {
  var subset = [];
  for (var num = 0; num < allMedia.length; num++) {
    if (isMatch(allMedia[num], params)) {
      subset.push(allMedia[num]);
    }
  }
  return subset;
};

var isMatch = function(media, params) {
  if (params && params.q) {
    return isKeywordMatch(media, params.q);
  } else {
    return true;
  }
};

var isKeywordMatch = function(media, keyword) {
  if (keyword.length > 0) {
    return media.artist.indexOf(keyword) > -1
      || media.title.indexOf(keyword) > -1
      || media.album.indexOf(keyword) > -1;
  } else {
    return true;
  }
};

var subArr = function(array, start, size) {
  var subset = [];
  for (var num = start; num < start + size; num++) {
    if (array.length >= num) {
      subset.push(array[num]);
    }
  }
  return subset;
};

var workOutFacets = function(mediaList, propertyName, selectedFacets) {
  var facets = [];
  for (var num = 0; num < mediaList.length; num++) {
    createOrIncrementFacet(mediaList[num], propertyName, facets);
  }
  return facets;
};

var createOrIncrementFacet = function(media, propertyName, facets) {
  var id = propertyName + ":" + media[propertyName];
  var index = findFacet(facets, id);
  if (index > -1) {
    facets[index].numMatches++;
  } else {
    facets.push(makeFacet(media, propertyName, id));
  }
}

var findFacet = function(facets, id) {
  for (var num = 0; num < facets.length; num++) {
    if (facets[num].id == id) {
      return num;
    }
  }
  return -1;
}

var makeFacet = function(media, propertyName, id) {
  return {
    id: id,
    name: media[propertyName],
    numMatches: 1
  };
};
