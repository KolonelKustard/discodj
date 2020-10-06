import { useState, useEffect } from 'react';

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
  const [searchQuery, setSearchQuery] = useState('');
  const [artistFacets, setArtistFacets] = useState([]);
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

  const search = (query: string) => {
    setSearchQuery(query);
  }

  useEffect(() => {
    const url = `/search?q=${searchQuery}`;
    fetch(url)
      .then(res => res.json())
      .then(
        (result) => {
          setIsLoaded(true);
          setResults(result);
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          setIsLoaded(true);
          setError(error);
        }
      );
    }, [searchQuery]);

    return {
      searchQuery,
      setSearchQuery,
      results,
    };
}
