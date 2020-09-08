import React, { useState, useEffect } from 'react';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';
import Input from '@material-ui/core/Input';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemText from '@material-ui/core/ListItemText';
import Select from '@material-ui/core/Select';
import Checkbox from '@material-ui/core/Checkbox';

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

  const ShowSearchResults = function() {
    return (
      <ul>
        {results.results.map(item => (
          <li key={item.title}>
            {item.title}
          </li>
        ))}
      </ul>
    );
  }

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
          <Select
            multiple
            value={artistFacets}
            input={<Input />}
          >
            {results.artistFacets.map((artistFacet) => (
              <MenuItem key={artistFacet.name} value={artistFacet.id}>
                <Checkbox checked={false} />
                <ListItemText primary={artistFacet.name} />
              </MenuItem>
            ))}
          </Select>
        </Grid>
      </Grid>
      <ShowSearchResults />
    </div>
  );
}
