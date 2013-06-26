package com.totalchange.discodj.search;

import java.util.List;

public class SearchQuery {
    private int start;
    private int rows;
    private String keywords;
    private List<SearchFacet> artistFacets;
    private List<SearchFacet> albumFacets;
    private List<SearchFacet> genreFacets;
    private List<SearchFacet> decadeFacets;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<SearchFacet> getArtistFacets() {
        return artistFacets;
    }

    public void setArtistFacets(List<SearchFacet> artistFacets) {
        this.artistFacets = artistFacets;
    }

    public List<SearchFacet> getAlbumFacets() {
        return albumFacets;
    }

    public void setAlbumFacets(List<SearchFacet> albumFacets) {
        this.albumFacets = albumFacets;
    }

    public List<SearchFacet> getGenreFacets() {
        return genreFacets;
    }

    public void setGenreFacets(List<SearchFacet> genreFacets) {
        this.genreFacets = genreFacets;
    }

    public List<SearchFacet> getDecadeFacets() {
        return decadeFacets;
    }

    public void setDecadeFacets(List<SearchFacet> decadeFacets) {
        this.decadeFacets = decadeFacets;
    }
}
