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
  var keyword = (params && params.q) ? params.q : "";
  var facets = (params && params.facet) ? extractFacets(params.facet) : [];
  console.log(facets);
  var subset = [];
  for (var num = 0; num < allMedia.length; num++) {
    var media = allMedia[num];
    if (isKeywordMatch(media, keyword) && isFacetMatch(media, facets)) {
      subset.push(media);
    }
  }
  return subset;
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

var extractFacets = function(facetStrs) {
  var facets = [];
  if (Array.isArray(facetStrs)) {
    for (var num = 0; num < facetStrs.length; num++) {
      facets.push(extractFacet(facetStrs[num]));
    }
  } else {
    facets.push(extractFacet(facetStrs));
  }
  return facets;
}

var extractFacet = function(facetStr) {
  var separator = facetStr.indexOf(":");
  return {
    propertyName: facetStr.substring(0, separator),
    value: facetStr.substring(separator + 1)
  };
}

var isFacetMatch = function(media, facets) {
  if (facets.length <= 0) {
    return true;
  } else {
    for (var num = 0; num < facets.length; num++) {
      var facet = facets[num];
      if (media[facet.propertyName] == facet.value) {
        return true;
      }
    }
    return false;
  }
}

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
