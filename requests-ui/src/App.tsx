import React, { useState, useEffect } from 'react';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';
import FacetSelect from './FacetSelect';
import SearchResults from './SearchResults';

interface Facet {
  id: string,
  name: string,
  numMatches: number
}

interface Result {
  id: string,
  artist: string,
  title: string
}

interface Results {
  page: number,
  numPages: number,
  artistFacets: Facet[],
  albumFacets: Facet[],
  genreFacets: Facet[],
  decadeFacets: Facet[],
  results: Result[]
}

export default function BasicTextFields() {
  const [searchQuery, setSearchQuery] = useState('');
  const [artistFacets, setArtistFacets] = React.useState([]);
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

  return (
    <div>
      <Grid container spacing={1} alignItems="flex-end">
        <Grid item>
          <SearchIcon />
        </Grid>
        <Grid item>
          <TextField id="search" label="Search tracks" value={searchQuery} onChange={(e) => search(e.target.value)} />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.artistFacets}
            label="Artists"
          />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.albumFacets}
            label="Albums"
          />
        </Grid>
      </Grid>
      <SearchResults results={results} />
    </div>
  );
}
