import { useState, useEffect } from 'react';
import axios from 'axios';

export interface Facet {
  id: string,
  name: string,
  numMatches: number
}

export interface Result {
  id: string,
  artist: string,
  title: string
}

export interface Results {
  page: number,
  numPages: number,
  artistFacets: Facet[],
  albumFacets: Facet[],
  genreFacets: Facet[],
  decadeFacets: Facet[],
  results: Result[]
}

export default function useTrackSearch() {
  const [searchText, setSearchText] = useState('');
  const [searchArtistFacet, setSearchArtistFacet] = useState<Facet | null>(null);
  const [searchAlbumFacet, setSearchAlbumFacet] = useState<Facet | null>(null);
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [results, setResults] = useState<Results>({
    page: 1,
    numPages: 1,
    artistFacets: [],
    albumFacets: [],
    genreFacets: [],
    decadeFacets: [],
    results: []
  });

  useEffect(() => {
    axios.get<Results>('/search', {
      params: {
        q: searchText,
        facet: facetsToParam(searchArtistFacet, searchAlbumFacet)
      }
    }).then(
      (result) => {
        setIsLoaded(true);
        setResults(result.data);
      },
      // Note: it's important to handle errors here
      // instead of a catch() block so that we don't swallow
      // exceptions from actual bugs in components.
      (error) => {
        setIsLoaded(true);
        setError(error);
      }
    );
  }, [searchText, searchArtistFacet, searchAlbumFacet]);

  return {
    searchText,
    setSearchText,
    setSearchArtistFacet,
    setSearchAlbumFacet,
    results,
  };
}

function facetsToParam(searchArtistFacet: Facet | null, searchAlbumFacet: Facet | null): string[] {
  const params = [];

  if (searchArtistFacet) {
    params.push(searchArtistFacet.id);
  }

  if (searchAlbumFacet) {
    params.push(searchAlbumFacet.id);
  }

  return params;
}
